package it.airgap.tezos.michelson.internal.packer

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coder.Base58BytesCoder
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.type.ByteTag
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.replacingAt
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.internal.coder.*
import it.airgap.tezos.michelson.internal.converter.MichelinePrimitiveApplicationToNormalizedConverter
import it.airgap.tezos.michelson.internal.converter.MichelineToCompactStringConverter
import it.airgap.tezos.michelson.internal.converter.StringToMichelsonGrammarTypeConverter
import it.airgap.tezos.michelson.internal.utils.second
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

internal class MichelinePacker(
    private val michelineBytesCoder: MichelineBytesCoder,
    private val stringToMichelsonGrammarTypeConverter: StringToMichelsonGrammarTypeConverter,
    private val michelinePrimitiveApplicationToNormalizedConverter: MichelinePrimitiveApplicationToNormalizedConverter,
    private val michelineToCompactStringConverter: MichelineToCompactStringConverter,
    private val base58BytesCoder: Base58BytesCoder,
    private val addressBytesCoder: AddressBytesCoder,
    private val keyBytesCoder: KeyBytesCoder,
    private val keyHashBytesCoder: KeyHashBytesCoder,
    private val signatureBytesCoder: SignatureBytesCoder,
    private val timestampBigIntCoder: TimestampBigIntCoder,
) : Packer<MichelineNode> {
    override fun pack(value: MichelineNode, schema: MichelineNode?): ByteArray {
        val prePacked = if (schema != null) prePack(value, schema) else value
        return Tag.Node + prePacked.encodeToBytes(michelineBytesCoder)
    }

    override fun unpack(bytes: ByteArray, schema: MichelineNode?): MichelineNode {
        TODO("Not yet implemented")
    }

    private fun prePack(value: MichelineNode, schema: MichelineNode): MichelineNode =
        when (schema) {
            is MichelinePrimitiveApplication -> prePack(value, schema)
            else -> failWithInvalidSchema(schema)
        }

    private fun prePack(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode =
        when (val prim = Michelson.GrammarType.fromStringOrNull(schema.prim.value, stringToMichelsonGrammarTypeConverter)) {
            is MichelsonType.GrammarType -> when (prim) {
                MichelsonType.Option, MichelsonComparableType.Option -> prePackOptionData(value, schema)
                MichelsonType.Set, MichelsonType.List -> prePackSequence(value, schema)
                MichelsonType.Contract, MichelsonComparableType.Address -> prePackAddressData(value, schema)
                MichelsonType.Pair, MichelsonComparableType.Pair -> prePackPairData(value, schema)
                MichelsonType.Or, MichelsonComparableType.Or -> prePackOrData(value, schema)
                MichelsonType.Lambda -> prePackLambdaData(value, schema)
                MichelsonType.Map, MichelsonType.BigMap -> prePackMapData(value, schema)
                MichelsonComparableType.ChainId -> prePackChainIdData(value, schema)
                MichelsonComparableType.KeyHash -> prePackKeyHashData(value, schema)
                MichelsonComparableType.Key -> prePackKeyData(value, schema)
                MichelsonComparableType.Signature -> prePackSignatureData(value, schema)
                MichelsonComparableType.Timestamp -> prePackTimestampData(value, schema)
                else -> value
            }
            else -> failWithInvalidSchema(schema)
        }

    private fun prePackOptionData(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode {
        if (value !is MichelinePrimitiveApplication) failWithValueSchemaMismatch(value, schema)

        return when {
            value.isPrim(MichelsonData.Some) -> {
                if (value.args.size != schema.args.size) failWithValueSchemaMismatch(value, schema)

                val prePackedArgs = value.args.zip(schema.args).map { (arg, type) -> prePack(arg, type) }
                value.copy(args = prePackedArgs)
            }
            value.isPrim(MichelsonData.None) -> value
            else -> failWithValueSchemaMismatch(value, schema)
        }
    }

    private fun prePackSequence(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode {
        if (value !is MichelineSequence) failWithValueSchemaMismatch(value, schema)
        if (schema.args.size != 1) failWithInvalidSchema(schema)

        val prePacked = value.nodes.map { prePack(it, schema.args.first()) }

        return MichelineSequence(prePacked)
    }

    private fun prePackAddressData(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode =
        prePackStringToBytes(value, schema, addressBytesCoder::encode)

    private fun prePackPairData(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode =
        when {
            value is MichelineSequence -> {
                val pair = MichelinePrimitiveApplication(
                    prim = MichelsonData.Pair,
                    args = value.nodes,
                ).normalized(michelinePrimitiveApplicationToNormalizedConverter)

                prePack(pair, schema)
            }
            value is MichelinePrimitiveApplication && value.isPrim(MichelsonData.Pair) -> {
                val valueNormalized = value.normalized(michelinePrimitiveApplicationToNormalizedConverter)
                val schemaNormalized = schema.normalized(michelinePrimitiveApplicationToNormalizedConverter)

                if (valueNormalized.args.size != schemaNormalized.args.size) failWithValueSchemaMismatch(value, schema)

                val prePackedArgs = valueNormalized.args.zip(schemaNormalized.args).map { (arg, type) -> prePack(arg, type) }
                valueNormalized.copy(args = prePackedArgs)
            }
            else -> failWithValueSchemaMismatch(value, schema)
        }

    private fun prePackOrData(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode {
        if (value !is MichelinePrimitiveApplication) failWithValueSchemaMismatch(value, schema)
        if (schema.args.size != 2) failWithInvalidSchema(schema)

        val type = when {
            value.isPrim(MichelsonData.Left) && value.args.size == 1 -> schema.args.first()
            value.isPrim(MichelsonData.Right) && value.args.size == 1 -> schema.args.second()
            else -> failWithValueSchemaMismatch(value, schema)
        }

        return value.copy(args = listOf(prePack(value.args.first(), type)))
    }

    private fun prePackLambdaData(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode {
        if (value !is MichelineSequence) failWithValueSchemaMismatch(value, schema)

        val packed = value.nodes.map {
            when (it) {
                is MichelineLiteral -> failWithValueSchemaMismatch(value, schema)
                is MichelinePrimitiveApplication -> prePackInstruction(it, schema)
                is MichelineSequence -> prePackLambdaData(it, schema)
            }
        }

        return MichelineSequence(packed)
    }

    private fun prePackMapData(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode {
        if (value !is MichelineSequence) failWithValueSchemaMismatch(value, schema)

        val prePacked = value.nodes.map {
            if (it !is MichelinePrimitiveApplication || !it.isPrim(MichelsonData.Elt) || it.args.size != schema.args.size) failWithValueSchemaMismatch(value, schema)

            val prePackedArgs = it.args.zip(schema.args).map { (arg, type) -> prePack(arg, type) }
            it.copy(args = prePackedArgs)
        }

        return MichelineSequence(prePacked)
    }

    private fun prePackChainIdData(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode =
        prePackStringToBytes(value, schema) { base58BytesCoder.encode(it, prefix = Tezos.Prefix.ChainId) }

    private fun prePackKeyHashData(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode =
        prePackStringToBytes(value, schema, keyHashBytesCoder::encode)

    private fun prePackKeyData(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode =
        prePackStringToBytes(value, schema, keyBytesCoder::encode)

    private fun prePackSignatureData(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode =
        prePackStringToBytes(value, schema, signatureBytesCoder::encode)

    private fun prePackTimestampData(value: MichelineNode, schema: MichelinePrimitiveApplication): MichelineNode =
        prePackStringToInt(value, schema, timestampBigIntCoder::encode)

    private fun prePackStringToBytes(value: MichelineNode, schema: MichelinePrimitiveApplication, prePackMethod: (String) -> ByteArray): MichelineNode =
        when (value) {
            is MichelineLiteral.String -> MichelineLiteral.Bytes(prePackMethod(value.string))
            is MichelineLiteral.Bytes -> value
            else -> failWithValueSchemaMismatch(value, schema)
        }

    private fun prePackStringToInt(value: MichelineNode, schema: MichelinePrimitiveApplication, prePackMethod: (String) -> BigInt): MichelineNode =
        when (value) {
            is MichelineLiteral.String -> MichelineLiteral.Integer(prePackMethod(value.string).toString(10))
            is MichelineLiteral.Integer -> value
            else -> failWithValueSchemaMismatch(value, schema)
        }

    private fun prePackInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        when (val prim = Michelson.GrammarType.fromStringOrNull(value.prim.value, stringToMichelsonGrammarTypeConverter)) {
            is MichelsonInstruction -> when (prim) {
                MichelsonInstruction.Map, MichelsonInstruction.Iter -> prePackIteratingInstruction(value, schema)
                MichelsonInstruction.Loop, MichelsonInstruction.LoopLeft -> prePackLoopInstruction(value, schema)
                MichelsonInstruction.Lambda -> prePackLambdaInstruction(value, schema)
                MichelsonInstruction.Dip -> prePackDipInstruction(value, schema)
                MichelsonInstruction.IfNone, MichelsonInstruction.IfLeft, MichelsonInstruction.IfCons, MichelsonInstruction.If -> prePackIfInstruction(value, schema)
                MichelsonInstruction.Push -> prePackPushInstruction(value)
                else -> value
            }
            else -> failWithValueSchemaMismatch(value, schema)
        }

    private fun prePackIteratingInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        prePackInstructionArgument(value, schema, argumentIndex = 0)

    private fun prePackLoopInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        prePackInstructionArgument(value, schema, argumentIndex = 0)

    private fun prePackLambdaInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        prePackInstructionArgument(value, schema, argumentIndex = 2)

    private fun prePackDipInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        prePackInstructionArgument(value, schema, argumentIndex = 1)

    private fun prePackIfInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication {
        if (value.args.size != 2) failWithInvalidValue(value)
        return value.copy(args = value.args.map { prePackLambdaData(value, schema) })
    }

    private fun prePackPushInstruction(value: MichelinePrimitiveApplication): MichelinePrimitiveApplication {
        if (value.args.size != 2) failWithInvalidValue(value)

        val schema = value.args.first()
        val data = value.args.second()

        return value.copy(args = listOf(schema, prePack(data, schema)))
    }

    private fun prePackInstructionArgument(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication, argumentIndex: Int): MichelinePrimitiveApplication {
        if (value.args.lastIndex < argumentIndex) failWithInvalidValue(value)

        val prePackedArg = prePackLambdaData(value.args[argumentIndex], schema)
        return value.copy(args = value.args.replacingAt(argumentIndex, prePackedArg))
    }

    private fun failWithInvalidValue(value: MichelineNode): Nothing =
        failWithIllegalArgument("Micheline value ${value.toCompactExpression(michelineToCompactStringConverter)} is invalid.")

    private fun failWithInvalidSchema(schema: MichelineNode): Nothing =
        failWithIllegalArgument("Micheline schema ${schema.toCompactExpression(michelineToCompactStringConverter)} is invalid.")

    private fun failWithValueSchemaMismatch(value: MichelineNode, schema: MichelineNode): Nothing =
        failWithIllegalArgument("Micheline value ${value.toCompactExpression(michelineToCompactStringConverter)} does not match the schema ${schema.toCompactExpression(michelineToCompactStringConverter)}.")
}

private enum class Tag(override val value: Byte) : ByteTag {
    Node(5);
}