package it.airgap.tezos.michelson.internal.packer

import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.type.BytesTag
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.comparator.isPrim
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.consumeAt
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.decodeConsumingFromBytes
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.decodeConsumingString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.decodeFromBytes
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.encodeToBytes
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.failWithIllegalArgument
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.fromString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.fromStringOrNull
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.normalized
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.replacingAt
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.startsWith
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.toCompactExpression
import it.airgap.tezos.michelson.internal.utils.second
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

internal class MichelinePacker(
    private val michelineBytesCoder: ConsumingBytesCoder<Micheline>,
    private val michelinePrimitiveApplicationNormalizer: Normalizer<MichelinePrimitiveApplication>,
    private val stringToMichelsonPrimConverter: Converter<String, Michelson.Prim>,
    private val michelineToCompactStringConverter: Converter<Micheline, String>,
    private val encodedBytesCoder: EncodedBytesCoder,
    private val addressBytesCoder: ConsumingBytesCoder<Address>,
    private val publicKeyBytesCoder: ConsumingBytesCoder<PublicKey>,
    private val implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>,
    private val signatureBytesCoder: ConsumingBytesCoder<Signature>,
    private val timestampBigIntCoder: Coder<Timestamp, BigInt>,
    private val stringToAddressConverter: Converter<String, Address>,
    private val stringToImplicitAddressConverter: Converter<String, ImplicitAddress>,
    private val stringToPublicKeyConverter: Converter<String, PublicKey>,
    private val stringToSignatureConverter: Converter<String, Signature>,
) : Packer<Micheline> {
    override fun pack(value: Micheline, schema: Micheline?): ByteArray {
        val prePacked = if (schema != null) prePack(value, schema) else value
        return Tag.Node + prePacked.encodeToBytes(michelineBytesCoder)
    }

    override fun unpack(bytes: ByteArray, schema: Micheline?): Micheline =
        when (Tag.recognize(bytes)) {
            Tag.Node -> {
                val prePacked = Micheline.decodeFromBytes(bytes.sliceArray(1 until bytes.size), michelineBytesCoder)
                if (schema != null) postUnpack(prePacked, schema) else prePacked
            }
            else -> failWithUnknownTag()
        }

    private fun prePack(value: Micheline, schema: Micheline): Micheline =
        when {
            schema is MichelinePrimitiveApplication -> prePack(value, schema)
            schema is MichelineSequence && value is MichelineSequence -> prePack(value, schema)
            else -> failWithInvalidSchema(schema)
        }

    private fun prePack(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        when (val prim = Michelson.Prim.fromStringOrNull(schema.prim.value, stringToMichelsonPrimConverter)) {
            is MichelsonType.Prim -> when (prim) {
                MichelsonType.Option, MichelsonComparableType.Option -> prePackOptionData(value, schema)
                MichelsonType.Set, MichelsonType.List -> prePackSequenceData(value, schema)
                MichelsonType.Contract, MichelsonComparableType.Address -> prePackAddressData(value, schema)
                MichelsonType.Pair, MichelsonComparableType.Pair -> prePackPairData(value, schema)
                MichelsonType.Or, MichelsonComparableType.Or -> prePackOrData(value, schema)
                MichelsonType.Lambda -> prePackLambdaData(value, schema)
                MichelsonType.Map -> prePackMapData(value, schema)
                MichelsonType.BigMap -> prePackBigMapData(value, schema)
                MichelsonComparableType.ChainId -> prePackChainIdData(value, schema)
                MichelsonComparableType.KeyHash -> prePackKeyHashData(value, schema)
                MichelsonComparableType.Key -> prePackKeyData(value, schema)
                MichelsonComparableType.Signature -> prePackSignatureData(value, schema)
                MichelsonComparableType.Timestamp -> prePackTimestampData(value, schema)
                else -> value // TODO: verify if value prim matches schema prim?
            }
            else -> failWithInvalidSchema(schema)
        }

    private fun prePack(value: MichelineSequence, schema: MichelineSequence): Micheline {
        if (value.nodes.size != schema.nodes.size) failWithValueSchemaMismatch(value, schema)

        val prePackedArgs = prePack(value.nodes, schema.nodes)
        return MichelineSequence(prePackedArgs)
    }


    private fun prePackOptionData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline {
        if (value !is MichelinePrimitiveApplication) failWithValueSchemaMismatch(value, schema)

        return when {
            value.isPrim(MichelsonData.Some) -> {
                if (value.args.size != schema.args.size) failWithValueSchemaMismatch(value, schema)

                val prePackedArgs = prePack(value.args, schema.args)
                value.copy(args = prePackedArgs)
            }
            value.isPrim(MichelsonData.None) -> value
            else -> failWithValueSchemaMismatch(value, schema)
        }
    }

    private fun prePackSequenceData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline {
        if (value !is MichelineSequence) failWithValueSchemaMismatch(value, schema)
        if (schema.args.size != 1) failWithInvalidSchema(schema)

        val prePacked = prePack(value.nodes, schema.args.first())

        return MichelineSequence(prePacked)
    }

    private fun prePackAddressData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        prePackStringToBytes(value, schema) {
            val (address, entrypoint) = splitAddress(it)
            val addressBytes = Address.fromString(address, stringToAddressConverter).encodeToBytes(addressBytesCoder)
            val entrypointBytes = entrypoint?.encodeToBytes() ?: byteArrayOf()

            addressBytes + entrypointBytes
        }

    private fun prePackPairData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        when {
            value is MichelineSequence -> {
                val pair = MichelinePrimitiveApplication(
                    prim = MichelsonData.Pair,
                    args = value.nodes,
                ).normalized(michelinePrimitiveApplicationNormalizer)

                prePack(pair, schema)
            }
            value.isPrim(MichelsonData.Pair) -> {
                val valueNormalized = value.normalized(michelinePrimitiveApplicationNormalizer)
                val schemaNormalized = schema.normalized(michelinePrimitiveApplicationNormalizer)

                if (valueNormalized.args.size != schemaNormalized.args.size) failWithValueSchemaMismatch(value, schema)

                val prePackedArgs = prePack(valueNormalized.args, schemaNormalized.args)
                valueNormalized.copy(args = prePackedArgs)
            }
            else -> failWithValueSchemaMismatch(value, schema)
        }

    private fun prePackOrData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline {
        if (value !is MichelinePrimitiveApplication) failWithValueSchemaMismatch(value, schema)
        if (schema.args.size != 2) failWithInvalidSchema(schema)

        val type = when {
            value.isPrim(MichelsonData.Left) && value.args.size == 1 -> schema.args.first()
            value.isPrim(MichelsonData.Right) && value.args.size == 1 -> schema.args.second()
            else -> failWithValueSchemaMismatch(value, schema)
        }

        return value.copy(args = listOf(prePack(value.args.first(), type)))
    }

    private fun prePackLambdaData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline {
        if (value !is MichelineSequence) failWithValueSchemaMismatch(value, schema)

        val prePacked = value.nodes.map {
            when (it) {
                is MichelineLiteral -> failWithValueSchemaMismatch(value, schema)
                is MichelinePrimitiveApplication -> prePackInstruction(it, schema)
                is MichelineSequence -> prePackLambdaData(it, schema)
            }
        }

        return MichelineSequence(prePacked)
    }

    private fun prePackMapData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline {
        if (value !is MichelineSequence) failWithValueSchemaMismatch(value, schema)

        val prePacked = value.nodes.map {
            if (!it.isPrim(MichelsonData.Elt) || it.args.size != schema.args.size) failWithValueSchemaMismatch(value, schema)

            val prePackedArgs = prePack(it.args, schema.args)
            it.copy(args = prePackedArgs)
        }

        return MichelineSequence(prePacked)
    }

    private fun prePackBigMapData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        when (value) {
            is MichelineLiteral.Integer -> value
            else -> prePackMapData(value, schema)
        }

    private fun prePackChainIdData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        prePackStringToBytes(value, schema) { ChainId(it).encodeToBytes(encodedBytesCoder) }

    private fun prePackKeyHashData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        prePackStringToBytes(value, schema) { ImplicitAddress.fromString(it, stringToImplicitAddressConverter).encodeToBytes(implicitAddressBytesCoder) }

    private fun prePackKeyData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        prePackStringToBytes(value, schema) { PublicKey.fromString(it, stringToPublicKeyConverter).encodeToBytes(publicKeyBytesCoder) }

    private fun prePackSignatureData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        prePackStringToBytes(value, schema) { Signature.fromString(it, stringToSignatureConverter).encodeToBytes(signatureBytesCoder) }

    private fun prePackTimestampData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        prePackStringToInt(value, schema) { timestampBigIntCoder.encode(Timestamp.Rfc3339(it)) }

    private fun prePackStringToBytes(value: Micheline, schema: MichelinePrimitiveApplication, prePackMethod: (String) -> ByteArray): Micheline =
        when (value) {
            is MichelineLiteral.String -> MichelineLiteral.Bytes(prePackMethod(value.string))
            is MichelineLiteral.Bytes -> value
            else -> failWithValueSchemaMismatch(value, schema)
        }

    private fun prePackStringToInt(value: Micheline, schema: MichelinePrimitiveApplication, prePackMethod: (String) -> BigInt): Micheline =
        when (value) {
            is MichelineLiteral.String -> MichelineLiteral.Integer(prePackMethod(value.string).toString(10))
            is MichelineLiteral.Integer -> value
            else -> failWithValueSchemaMismatch(value, schema)
        }

    private fun prePackInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        when (val prim = Michelson.Prim.fromStringOrNull(value.prim.value, stringToMichelsonPrimConverter)) {
            is MichelsonInstruction.Prim -> when (prim) {
                MichelsonInstruction.Map, MichelsonInstruction.Iter -> prePackIteratingInstruction(value, schema)
                MichelsonInstruction.Loop, MichelsonInstruction.LoopLeft -> prePackLoopInstruction(value, schema)
                MichelsonInstruction.Lambda -> prePackLambdaInstruction(value, schema)
                MichelsonInstruction.Dip -> prePackDipInstruction(value, schema)
                MichelsonInstruction.IfNone, MichelsonInstruction.IfLeft, MichelsonInstruction.IfCons, MichelsonInstruction.If -> prePackIfInstruction(value, schema)
                MichelsonInstruction.Push -> prePackPushInstruction(value)
                MichelsonInstruction.CreateContract -> prePackCreateContractInstruction(value, schema)
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
        prePackInstructionArgument(value, schema, argumentIndex = value.args.lastIndex)

    private fun prePackCreateContractInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        prePackInstructionArgument(value, schema, argumentIndex = 2)

    private fun prePackIfInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication {
        if (value.args.size != 2) failWithInvalidValue(value)
        return value.copy(args = value.args.map { prePackLambdaData(it, schema) })
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

    private fun prePack(values: List<Micheline>, schemas: List<Micheline>): List<Micheline> =
        values.zip(schemas).map { (value, type) -> prePack(value, type) }

    private fun prePack(values: List<Micheline>, schema: Micheline): List<Micheline> =
        values.map { prePack(it, schema) }

    private fun postUnpack(value: Micheline, schema: Micheline): Micheline =
        when {
            schema is MichelinePrimitiveApplication -> postUnpack(value, schema)
            schema is MichelineSequence && value is MichelineSequence -> postUnpack(value, schema)
            else -> failWithInvalidSchema(schema)
        }

    private fun postUnpack(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        when (val prim = Michelson.Prim.fromStringOrNull(schema.prim.value, stringToMichelsonPrimConverter)) {
            is MichelsonType.Prim -> when (prim) {
                MichelsonType.Option, MichelsonComparableType.Option -> postUnpackOptionData(value, schema)
                MichelsonType.Set, MichelsonType.List -> postUnpackSequenceData(value, schema)
                MichelsonType.Contract, MichelsonComparableType.Address -> postUnpackAddressData(value, schema)
                MichelsonType.Pair, MichelsonComparableType.Pair -> postUnpackPairData(value, schema)
                MichelsonType.Or, MichelsonComparableType.Or -> postUnpackOrData(value, schema)
                MichelsonType.Lambda -> postUnpackLambdaData(value, schema)
                MichelsonType.Map -> postUnpackMapData(value, schema)
                MichelsonType.BigMap -> postUnpackBigMapData(value, schema)
                MichelsonComparableType.ChainId -> postUnpackChainIdData(value, schema)
                MichelsonComparableType.KeyHash -> postUnpackKeyHashData(value, schema)
                MichelsonComparableType.Key -> postUnpackKeyData(value, schema)
                MichelsonComparableType.Signature -> postUnpackSignatureData(value, schema)
                MichelsonComparableType.Timestamp -> postUnpackTimestampData(value, schema)
                else -> value // TODO: verify if value prim matches schema prim?
            }
            else -> failWithInvalidSchema(schema)
        }

    private fun postUnpack(value: MichelineSequence, schema: MichelineSequence): Micheline {
        if (value.nodes.size != schema.nodes.size) failWithValueSchemaMismatch(value, schema)

        val postUnpackedArgs = postUnpack(value.nodes, schema.nodes)
        return MichelineSequence(postUnpackedArgs)
    }

    private fun postUnpackOptionData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline {
        if (value !is MichelinePrimitiveApplication) failWithValueSchemaMismatch(value, schema)

        return when {
            value.isPrim(MichelsonData.Some) -> {
                if (value.args.size != schema.args.size) failWithValueSchemaMismatch(value, schema)

                val postUnpackedArgs = postUnpack(value.args, schema.args)
                value.copy(args = postUnpackedArgs)
            }
            value.isPrim(MichelsonData.None) -> value
            else -> failWithValueSchemaMismatch(value, schema)
        }
    }

    private fun postUnpackSequenceData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline {
        if (value !is MichelineSequence) failWithValueSchemaMismatch(value, schema)
        if (schema.args.size != 1) failWithInvalidSchema(schema)

        val postUnpacked = postUnpack(value.nodes, schema.args.first())

        return MichelineSequence(postUnpacked)

    }

    private fun postUnpackAddressData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        postUnpackBytesToString(value, schema) {
            val bytes = it.toMutableList()
            val address = Address.decodeConsumingFromBytes(bytes, addressBytesCoder)
            val entrypoint = bytes.decodeConsumingString()

            combineAddress(address, entrypoint)
        }

    private fun postUnpackPairData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline {
        if (value !is MichelinePrimitiveApplication) failWithValueSchemaMismatch(value, schema)

        return when {
            value.isPrim(MichelsonData.Pair) -> {
                val valueNormalized = value.normalized(michelinePrimitiveApplicationNormalizer)
                val schemaNormalized = schema.normalized(michelinePrimitiveApplicationNormalizer)

                if (valueNormalized.args.size != schemaNormalized.args.size) failWithValueSchemaMismatch(value, schema)

                val postUnpackedArgs = postUnpack(valueNormalized.args, schemaNormalized.args)
                valueNormalized.copy(args = postUnpackedArgs)
            }
            else -> failWithValueSchemaMismatch(value, schema)
        }
    }

    private fun postUnpackOrData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline {
        if (value !is MichelinePrimitiveApplication) failWithValueSchemaMismatch(value, schema)
        if (schema.args.size != 2) failWithInvalidSchema(schema)

        val type = when {
            value.isPrim(MichelsonData.Left) && value.args.size == 1 -> schema.args.first()
            value.isPrim(MichelsonData.Right) && value.args.size == 1 -> schema.args.second()
            else -> failWithValueSchemaMismatch(value, schema)
        }

        return value.copy(args = listOf(postUnpack(value.args.first(), type)))
    }

    private fun postUnpackLambdaData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline {
        if (value !is MichelineSequence) failWithValueSchemaMismatch(value, schema)

        val postUnpacked = value.nodes.map {
            when (it) {
                is MichelineLiteral -> failWithValueSchemaMismatch(value, schema)
                is MichelinePrimitiveApplication -> postUnpackInstruction(it, schema)
                is MichelineSequence -> postUnpackLambdaData(it, schema)
            }
        }

        return MichelineSequence(postUnpacked)
    }

    private fun postUnpackMapData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline {
        if (value !is MichelineSequence) failWithValueSchemaMismatch(value, schema)

        val prePacked = value.nodes.map {
            if (it !is MichelinePrimitiveApplication || !it.isPrim(MichelsonData.Elt) || it.args.size != schema.args.size) failWithValueSchemaMismatch(value, schema)

            val postUnpackedArgs = postUnpack(it.args, schema.args)
            it.copy(args = postUnpackedArgs)
        }

        return MichelineSequence(prePacked)
    }

    private fun postUnpackBigMapData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        when (value) {
            is MichelineLiteral.Integer -> value
            else -> postUnpackMapData(value, schema)
        }

    private fun postUnpackChainIdData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        postUnpackBytesToString(value, schema) { ChainId.decodeFromBytes(it, encodedBytesCoder).base58 }

    private fun postUnpackKeyHashData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        postUnpackBytesToString(value, schema) { ImplicitAddress.decodeFromBytes(it, implicitAddressBytesCoder).base58 }

    private fun postUnpackKeyData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        postUnpackBytesToString(value, schema) { PublicKey.decodeFromBytes(it, publicKeyBytesCoder).base58 }

    private fun postUnpackSignatureData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        postUnpackBytesToString(value, schema) { Signature.decodeFromBytes(it, signatureBytesCoder).base58 }

    private fun postUnpackTimestampData(value: Micheline, schema: MichelinePrimitiveApplication): Micheline =
        postUnpackBigIntToString(value, schema) { timestampBigIntCoder.decode(it).toRfc3339().dateString }

    private fun postUnpackBytesToString(value: Micheline, schema: MichelinePrimitiveApplication, postUnpackMethod: (ByteArray) -> String): Micheline =
        when (value) {
            is MichelineLiteral.Bytes -> MichelineLiteral.String(postUnpackMethod(value.toByteArray()))
            is MichelineLiteral.String -> value
            else -> failWithValueSchemaMismatch(value, schema)
        }

    private fun postUnpackBigIntToString(value: Micheline, schema: MichelinePrimitiveApplication, postUnpackMethod: (BigInt) -> String): Micheline =
        when (value) {
            is MichelineLiteral.Integer -> MichelineLiteral.String(postUnpackMethod(BigInt.valueOf(value.int)))
            is MichelineLiteral.String -> value
            else -> failWithValueSchemaMismatch(value, schema)
        }

    private fun postUnpackInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): Micheline =
        when (val prim = Michelson.Prim.fromStringOrNull(value.prim.value, stringToMichelsonPrimConverter)) {
            is MichelsonInstruction.Prim -> when (prim) {
                MichelsonInstruction.Map, MichelsonInstruction.Iter -> postUnpackIteratingInstruction(value, schema)
                MichelsonInstruction.Loop, MichelsonInstruction.LoopLeft -> postUnpackLoopInstruction(value, schema)
                MichelsonInstruction.Lambda -> postUnpackLambdaInstruction(value, schema)
                MichelsonInstruction.Dip -> postUnpackDipInstruction(value, schema)
                MichelsonInstruction.IfNone, MichelsonInstruction.IfLeft, MichelsonInstruction.IfCons, MichelsonInstruction.If -> postUnpackIfInstruction(value, schema)
                MichelsonInstruction.Push -> postUnpackPushInstruction(value)
                MichelsonInstruction.CreateContract -> postUnpackCreateContractInstruction(value, schema)
                else -> value
            }
            else -> failWithValueSchemaMismatch(value, schema)
        }

    private fun postUnpackIteratingInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        postUnpackInstructionArgument(value, schema, argumentIndex = 0)

    private fun postUnpackLoopInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        postUnpackInstructionArgument(value, schema, argumentIndex = 0)

    private fun postUnpackLambdaInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        postUnpackInstructionArgument(value, schema, argumentIndex = 2)

    private fun postUnpackDipInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        postUnpackInstructionArgument(value, schema, argumentIndex = value.args.lastIndex)

    private fun postUnpackCreateContractInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        postUnpackInstructionArgument(value, schema, argumentIndex = 2)

    private fun postUnpackIfInstruction(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication): MichelinePrimitiveApplication {
        if (value.args.size != 2) failWithInvalidValue(value)
        return value.copy(args = value.args.map { postUnpackLambdaData(it, schema) })
    }

    private fun postUnpackPushInstruction(value: MichelinePrimitiveApplication): MichelinePrimitiveApplication {
        if (value.args.size != 2) failWithInvalidValue(value)

        val schema = value.args.first()
        val data = value.args.second()

        return value.copy(args = listOf(schema, postUnpack(data, schema)))
    }

    private fun postUnpackInstructionArgument(value: MichelinePrimitiveApplication, schema: MichelinePrimitiveApplication, argumentIndex: Int): MichelinePrimitiveApplication {
        if (value.args.lastIndex < argumentIndex) failWithInvalidValue(value)

        val prePackedArg = postUnpackLambdaData(value.args[argumentIndex], schema)
        return value.copy(args = value.args.replacingAt(argumentIndex, prePackedArg))
    }

    private fun postUnpack(values: List<Micheline>, schemas: List<Micheline>): List<Micheline> =
        values.zip(schemas).map { (value, type) -> postUnpack(value, type) }

    private fun postUnpack(values: List<Micheline>, schema: Micheline): List<Micheline> =
        values.map { postUnpack(it, schema) }

    private fun splitAddress(address: String): Pair<String, String?> {
        val split = address.split(MichelsonComparableType.Address.ENTRYPOINT_SEPARATOR, limit = 2)

        return Pair(split.get(0), split.getOrNull(1))
    }

    private fun combineAddress(address: Address, entrypoint: String): String =
        if (entrypoint.isNotBlank()) "${address.base58}${MichelsonComparableType.Address.ENTRYPOINT_SEPARATOR}$entrypoint"
        else address.base58

    private fun failWithInvalidValue(value: Micheline): Nothing =
        failWithIllegalArgument("Micheline value ${value.toCompactExpression(michelineToCompactStringConverter)} is invalid.")

    private fun failWithInvalidSchema(schema: Micheline): Nothing =
        failWithIllegalArgument("Micheline schema ${schema.toCompactExpression(michelineToCompactStringConverter)} is invalid.")

    private fun failWithValueSchemaMismatch(value: Micheline, schema: Micheline): Nothing =
        failWithIllegalArgument("Micheline value ${value.toCompactExpression(michelineToCompactStringConverter)} does not match the schema ${schema.toCompactExpression(michelineToCompactStringConverter)}.")

    private fun failWithUnknownTag(): Nothing = failWithIllegalArgument("Data is not packed Micheline.")
}

private enum class Tag(override val value: ByteArray) : BytesTag {
    Node(byteArrayOf(5));

    companion object {
        fun recognize(bytes: ByteArray): Tag? =
            if (bytes.isEmpty()) null
            else values().find { bytes.startsWith(it.value) }
    }
}