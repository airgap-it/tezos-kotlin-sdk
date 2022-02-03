package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.utils.allIsInstance
import it.airgap.tezos.core.internal.utils.anyIsInstance
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.internal.utils.second
import it.airgap.tezos.michelson.internal.utils.third
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

@InternalTezosSdkApi
public class MichelineToMichelsonConverter(
    stringToMichelsonPrimConverter: StringToMichelsonPrimConverter,
    michelineToCompactStringConverter: MichelineToCompactStringConverter,
) : Converter<MichelineNode, Michelson> {
    private val literalToMichelsonConverter: MichelineLiteralToMichelsonConverter = MichelineLiteralToMichelsonConverter(michelineToCompactStringConverter)
    private val primitiveApplicationToMichelsonConverter: MichelinePrimitiveApplicationToMichelsonConverter = MichelinePrimitiveApplicationToMichelsonConverter(
        stringToMichelsonPrimConverter,
        michelineToCompactStringConverter,
        this,
    )
    private val michelineSequenceToMichelsonConverter: MichelineSequenceToMichelsonConverter = MichelineSequenceToMichelsonConverter(michelineToCompactStringConverter, this)

    override fun convert(value: MichelineNode): Michelson =
        when (value) {
            is MichelineLiteral -> literalToMichelsonConverter.convert(value)
            is MichelinePrimitiveApplication -> primitiveApplicationToMichelsonConverter.convert(value)
            is MichelineSequence -> michelineSequenceToMichelsonConverter.convert(value)
        }
}

private class MichelineLiteralToMichelsonConverter(private val michelineToCompactStringConverter: MichelineToCompactStringConverter) : Converter<MichelineLiteral, Michelson> {
    override fun convert(value: MichelineLiteral): Michelson = with(value) {
        try {
            when (this) {
                is MichelineLiteral.Integer -> MichelsonData.IntConstant(int)
                is MichelineLiteral.String -> MichelsonData.StringConstant(string)
                is MichelineLiteral.Bytes -> MichelsonData.ByteSequenceConstant(bytes)
            }
        } catch (e: IllegalArgumentException) {
            failWithInvalidLiteral(this)
        }
    }

    private fun failWithInvalidLiteral(literal: MichelineLiteral): Nothing =
        failWithIllegalArgument("Invalid Micheline Literal: ${literal.toCompactExpression(michelineToCompactStringConverter)}.")
}

private class MichelinePrimitiveApplicationToMichelsonConverter(
    private val stringToMichelsonPrimConverter: StringToMichelsonPrimConverter,
    private val michelineToCompactStringConverter: MichelineToCompactStringConverter,
    private val toMichelsonConverter: MichelineToMichelsonConverter,
) : Converter<MichelinePrimitiveApplication, Michelson> {
    override fun convert(value: MichelinePrimitiveApplication): Michelson = with(value) {
        val prim = Michelson.Prim.fromStringOrNull(prim.value, stringToMichelsonPrimConverter) ?: failWithUnknownPrimitiveApplication(this)
        try {
            when (prim) {
                is MichelsonData.Prim -> fromDataPrimitiveApplication(prim, this)
                is MichelsonType.Prim -> fromTypePrimitiveApplication(prim, this)
            }
        } catch (e: IllegalArgumentException) {
            failWithInvalidPrimitiveApplication(this)
        } catch (e: NoSuchElementException) {
            failWithInvalidPrimitiveApplication(this)
        }
    }

    private fun fromDataPrimitiveApplication(
        prim: MichelsonData.Prim,
        primitiveApplication: MichelinePrimitiveApplication,
    ): MichelsonData = with(primitiveApplication) {
        when (prim) {
            MichelsonData.Unit -> MichelsonData.Unit
            MichelsonData.True -> MichelsonData.True
            MichelsonData.False -> MichelsonData.False
            MichelsonData.Pair -> {
                val values = args.convertToExpected<MichelsonData>()
                require(values.size > 1)

                MichelsonData.Pair(values)
            }
            MichelsonData.Left -> {
                val value = args.first().convertToExpected<MichelsonData>()

                MichelsonData.Left(value)
            }
            MichelsonData.Right -> {
                val value = args.first().convertToExpected<MichelsonData>()

                MichelsonData.Right(value)
            }
            MichelsonData.Some -> {
                val value = args.first().convertToExpected<MichelsonData>()

                MichelsonData.Some(value)
            }
            MichelsonData.None -> MichelsonData.None
            is MichelsonInstruction.Prim -> fromInstructionPrimitiveApplication(prim, this)
            else -> failWithUnknownPrimitiveApplication(primitiveApplication)
        }
    }

    private fun fromInstructionPrimitiveApplication(
        prim: MichelsonInstruction.Prim,
        primitiveApplication: MichelinePrimitiveApplication,
    ): MichelsonInstruction = with(primitiveApplication) {
        when (prim) {
            MichelsonInstruction.Drop -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.Drop(n)
            }
            MichelsonInstruction.Dup -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.Dup(n)
            }
            MichelsonInstruction.Swap -> MichelsonInstruction.Swap
            MichelsonInstruction.Dig -> {
                val n = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.Dig(n)
            }
            MichelsonInstruction.Dug -> {
                val n = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.Dug(n)
            }
            MichelsonInstruction.Push -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val value = args.second().convertToExpected<MichelsonData>()

                MichelsonInstruction.Push(type, value)
            }
            MichelsonInstruction.Some -> MichelsonInstruction.Some
            MichelsonInstruction.None -> {
                val type = args.first().convertToExpected<MichelsonType>()

                MichelsonInstruction.None(type)
            }
            MichelsonInstruction.Unit -> MichelsonInstruction.Unit
            MichelsonInstruction.Never -> MichelsonInstruction.Never
            MichelsonInstruction.IfNone -> {
                val ifBranch = args.first().convertToExpected<MichelsonInstruction.Sequence>()
                val elseBranch = args.second().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.IfNone(ifBranch, elseBranch)
            }
            MichelsonInstruction.Pair -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.Pair(n)
            }
            MichelsonInstruction.Car -> MichelsonInstruction.Car
            MichelsonInstruction.Cdr -> MichelsonInstruction.Cdr
            MichelsonInstruction.Unpair -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.Unpair(n)
            }
            MichelsonInstruction.Left -> {
                val type = args.first().convertToExpected<MichelsonType>()

                MichelsonInstruction.Left(type)
            }
            MichelsonInstruction.Right -> {
                val type = args.first().convertToExpected<MichelsonType>()

                MichelsonInstruction.Right(type)
            }
            MichelsonInstruction.IfLeft -> {
                val ifBranch = args.first().convertToExpected<MichelsonInstruction.Sequence>()
                val elseBranch = args.second().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.IfLeft(ifBranch, elseBranch)
            }
            MichelsonInstruction.Nil -> {
                val type = args.first().convertToExpected<MichelsonType>()

                MichelsonInstruction.Nil(type)
            }
            MichelsonInstruction.Cons -> MichelsonInstruction.Cons
            MichelsonInstruction.IfCons -> {
                val ifBranch = args.first().convertToExpected<MichelsonInstruction.Sequence>()
                val elseBranch = args.second().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.IfCons(ifBranch, elseBranch)
            }
            MichelsonInstruction.Size -> MichelsonInstruction.Size
            MichelsonInstruction.EmptySet -> {
                val type = args.first().convertToExpected<MichelsonComparableType>()

                MichelsonInstruction.EmptySet(type)
            }
            MichelsonInstruction.EmptyMap -> {
                val keyType = args.first().convertToExpected<MichelsonComparableType>()
                val valueType = args.second().convertToExpected<MichelsonType>()

                MichelsonInstruction.EmptyMap(keyType, valueType)
            }
            MichelsonInstruction.EmptyBigMap -> {
                val keyType = args.first().convertToExpected<MichelsonComparableType>()
                val valueType = args.second().convertToExpected<MichelsonType>()

                MichelsonInstruction.EmptyBigMap(keyType, valueType)
            }
            MichelsonInstruction.Map -> {
                val expression = args.first().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.Map(expression)
            }
            MichelsonInstruction.Iter -> {
                val expression = args.first().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.Iter(expression)
            }
            MichelsonInstruction.Mem -> MichelsonInstruction.Mem
            MichelsonInstruction.Get -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.Get(n)
            }
            MichelsonInstruction.Update -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.Update(n)
            }
            MichelsonInstruction.GetAndUpdate -> MichelsonInstruction.GetAndUpdate
            MichelsonInstruction.If -> {
                val ifBranch = args.first().convertToExpected<MichelsonInstruction.Sequence>()
                val elseBranch = args.second().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.If(ifBranch, elseBranch)
            }
            MichelsonInstruction.Loop -> {
                val body = args.first().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.Loop(body)
            }
            MichelsonInstruction.LoopLeft -> {
                val body = args.first().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.LoopLeft(body)
            }
            MichelsonInstruction.Lambda -> {
                val parameterType = args.first().convertToExpected<MichelsonType>()
                val returnType = args.second().convertToExpected<MichelsonType>()
                val body = args.third().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.Lambda(parameterType, returnType, body)
            }
            MichelsonInstruction.Exec -> MichelsonInstruction.Exec
            MichelsonInstruction.Apply -> MichelsonInstruction.Apply
            MichelsonInstruction.Dip -> {
                val (n, instruction) = if (args.size == 1) {
                    val instruction = args.first().convertToExpected<MichelsonInstruction.Sequence>()
                    Pair(null, instruction)
                } else {
                    val n = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()
                    val instruction = args.second().convertToExpected<MichelsonInstruction.Sequence>()
                    Pair(n, instruction)
                }

                MichelsonInstruction.Dip(instruction, n)
            }
            MichelsonInstruction.Failwith -> MichelsonInstruction.Failwith
            MichelsonInstruction.Cast -> MichelsonInstruction.Cast
            MichelsonInstruction.Rename -> MichelsonInstruction.Rename
            MichelsonInstruction.Concat -> MichelsonInstruction.Concat
            MichelsonInstruction.Slice -> MichelsonInstruction.Slice
            MichelsonInstruction.Pack -> MichelsonInstruction.Pack
            MichelsonInstruction.Unpack -> {
                val type = args.first().convertToExpected<MichelsonType>()

                MichelsonInstruction.Unpack(type)
            }
            MichelsonInstruction.Add -> MichelsonInstruction.Add
            MichelsonInstruction.Sub -> MichelsonInstruction.Sub
            MichelsonInstruction.Mul -> MichelsonInstruction.Mul
            MichelsonInstruction.Ediv -> MichelsonInstruction.Ediv
            MichelsonInstruction.Abs -> MichelsonInstruction.Abs
            MichelsonInstruction.Isnat -> MichelsonInstruction.Isnat
            MichelsonInstruction.Int -> MichelsonInstruction.Int
            MichelsonInstruction.Neg -> MichelsonInstruction.Neg
            MichelsonInstruction.Lsl -> MichelsonInstruction.Lsl
            MichelsonInstruction.Lsr -> MichelsonInstruction.Lsr
            MichelsonInstruction.Or -> MichelsonInstruction.Or
            MichelsonInstruction.And -> MichelsonInstruction.And
            MichelsonInstruction.Xor -> MichelsonInstruction.Xor
            MichelsonInstruction.Not -> MichelsonInstruction.Not
            MichelsonInstruction.Compare -> MichelsonInstruction.Compare
            MichelsonInstruction.Eq -> MichelsonInstruction.Eq
            MichelsonInstruction.Neq -> MichelsonInstruction.Neq
            MichelsonInstruction.Lt -> MichelsonInstruction.Lt
            MichelsonInstruction.Gt -> MichelsonInstruction.Gt
            MichelsonInstruction.Le -> MichelsonInstruction.Le
            MichelsonInstruction.Ge -> MichelsonInstruction.Ge
            MichelsonInstruction.Self -> MichelsonInstruction.Self
            MichelsonInstruction.SelfAddress -> MichelsonInstruction.SelfAddress
            MichelsonInstruction.Contract -> {
                val type = args.first().convertToExpected<MichelsonType>()

                MichelsonInstruction.Contract(type)
            }
            MichelsonInstruction.TransferTokens -> MichelsonInstruction.TransferTokens
            MichelsonInstruction.SetDelegate -> MichelsonInstruction.SetDelegate
            MichelsonInstruction.CreateContract -> {
                val parameterType = args.first().convertToExpected<MichelsonType>()
                val storageType = args.second().convertToExpected<MichelsonType>()
                val code = args.third().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.CreateContract(parameterType, storageType, code)
            }
            MichelsonInstruction.ImplicitAccount -> MichelsonInstruction.ImplicitAccount
            MichelsonInstruction.VotingPower -> MichelsonInstruction.VotingPower
            MichelsonInstruction.Now -> MichelsonInstruction.Now
            MichelsonInstruction.Level -> MichelsonInstruction.Level
            MichelsonInstruction.Amount -> MichelsonInstruction.Amount
            MichelsonInstruction.Balance -> MichelsonInstruction.Balance
            MichelsonInstruction.CheckSignature -> MichelsonInstruction.CheckSignature
            MichelsonInstruction.Blake2B -> MichelsonInstruction.Blake2B
            MichelsonInstruction.Keccak -> MichelsonInstruction.Keccak
            MichelsonInstruction.Sha3 -> MichelsonInstruction.Sha3
            MichelsonInstruction.Sha256 -> MichelsonInstruction.Sha256
            MichelsonInstruction.Sha512 -> MichelsonInstruction.Sha512
            MichelsonInstruction.HashKey -> MichelsonInstruction.HashKey
            MichelsonInstruction.Source -> MichelsonInstruction.Source
            MichelsonInstruction.Sender -> MichelsonInstruction.Sender
            MichelsonInstruction.Address -> MichelsonInstruction.Address
            MichelsonInstruction.ChainId -> MichelsonInstruction.ChainId
            MichelsonInstruction.TotalVotingPower -> MichelsonInstruction.TotalVotingPower
            MichelsonInstruction.PairingCheck -> MichelsonInstruction.PairingCheck
            MichelsonInstruction.SaplingEmptyState -> {
                val memoSize = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.SaplingEmptyState(memoSize)
            }
            MichelsonInstruction.SaplingVerifyUpdate -> MichelsonInstruction.SaplingVerifyUpdate
            MichelsonInstruction.Ticket -> MichelsonInstruction.Ticket
            MichelsonInstruction.ReadTicket -> MichelsonInstruction.ReadTicket
            MichelsonInstruction.SplitTicket -> MichelsonInstruction.SplitTicket
            MichelsonInstruction.JoinTickets -> MichelsonInstruction.JoinTickets
            MichelsonInstruction.OpenChest -> MichelsonInstruction.OpenChest
        }
    }

    private fun fromTypePrimitiveApplication(
        prim: MichelsonType.Prim,
        primitiveApplication: MichelinePrimitiveApplication,
    ): MichelsonType = with(primitiveApplication) {
        when (prim) {
            MichelsonType.Parameter -> {
                val type = args.first().convertToExpected<MichelsonType>()

                MichelsonType.Parameter(type)
            }
            MichelsonType.Storage -> {
                val type = args.first().convertToExpected<MichelsonType>()

                MichelsonType.Storage(type)
            }
            MichelsonType.Code -> {
                val code = args.first().convertToExpected<MichelsonInstruction>()

                MichelsonType.Code(code)
            }
            MichelsonType.Option -> {
                val type = args.first().convertToExpected<MichelsonType>()

                if (type is MichelsonComparableType) MichelsonComparableType.Option(type)

                else MichelsonType.Option(type)
            }
            MichelsonType.List -> {
                val type = args.first().convertToExpected<MichelsonType>()

                MichelsonType.List(type)
            }
            MichelsonType.Set -> {
                val type = args.first().convertToExpected<MichelsonComparableType>()

                MichelsonType.Set(type)
            }
            MichelsonType.Operation -> MichelsonType.Operation
            MichelsonType.Contract -> {
                val type = args.first().convertToExpected<MichelsonType>()

                MichelsonType.Contract(type)
            }
            MichelsonType.Ticket -> {
                val type = args.first().convertToExpected<MichelsonComparableType>()

                MichelsonType.Ticket(type)
            }
            MichelsonType.Pair -> {
                val types = args.convertToExpected<MichelsonType>()
                require(types.size > 1)

                @Suppress("UNCHECKED_CAST")
                if (types.allIsInstance<MichelsonComparableType>()) MichelsonComparableType.Pair(types as List<MichelsonComparableType>)
                else MichelsonType.Pair(types)
            }
            MichelsonType.Or -> {
                val lhs = args.first().convertToExpected<MichelsonType>()
                val rhs = args.second().convertToExpected<MichelsonType>()

                if (lhs is MichelsonComparableType && rhs is MichelsonComparableType) MichelsonComparableType.Or(lhs, rhs)
                else MichelsonType.Or(lhs, rhs)
            }
            MichelsonType.Lambda -> {
                val parameterType = args.first().convertToExpected<MichelsonType>()
                val returnType = args.second().convertToExpected<MichelsonType>()

                MichelsonType.Lambda(parameterType, returnType)
            }
            MichelsonType.Map -> {
                val keyType = args.first().convertToExpected<MichelsonComparableType>()
                val valueType = args.second().convertToExpected<MichelsonType>()

                MichelsonType.Map(keyType, valueType)
            }
            MichelsonType.BigMap -> {
                val keyType = args.first().convertToExpected<MichelsonComparableType>()
                val valueType = args.second().convertToExpected<MichelsonType>()

                MichelsonType.BigMap(keyType, valueType)
            }
            MichelsonType.Bls12_381G1 -> MichelsonType.Bls12_381G1
            MichelsonType.Bls12_381G2 -> MichelsonType.Bls12_381G2
            MichelsonType.Bls12_381Fr -> MichelsonType.Bls12_381Fr
            MichelsonType.SaplingTransaction -> {
                val memoSize = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonType.SaplingTransaction(memoSize)
            }
            MichelsonType.SaplingState -> {
                val memoSize = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonType.SaplingState(memoSize)
            }
            MichelsonType.Chest -> MichelsonType.Chest
            MichelsonType.ChestKey -> MichelsonType.ChestKey
            is MichelsonComparableType.Prim -> fromComparableTypePrimitiveApplication(prim, primitiveApplication)
        }
    }

    private fun fromComparableTypePrimitiveApplication(
        prim: MichelsonComparableType.Prim,
        primitiveApplication: MichelinePrimitiveApplication,
    ): MichelsonComparableType = with(primitiveApplication) {
        when (prim) {
            MichelsonComparableType.Unit -> MichelsonComparableType.Unit
            MichelsonComparableType.Never -> MichelsonComparableType.Never
            MichelsonComparableType.Bool -> MichelsonComparableType.Bool
            MichelsonComparableType.Int -> MichelsonComparableType.Int
            MichelsonComparableType.Nat -> MichelsonComparableType.Nat
            MichelsonComparableType.String -> MichelsonComparableType.String
            MichelsonComparableType.ChainId -> MichelsonComparableType.ChainId
            MichelsonComparableType.Bytes -> MichelsonComparableType.Bytes
            MichelsonComparableType.Mutez -> MichelsonComparableType.Mutez
            MichelsonComparableType.KeyHash -> MichelsonComparableType.KeyHash
            MichelsonComparableType.Key -> MichelsonComparableType.Key
            MichelsonComparableType.Signature -> MichelsonComparableType.Signature
            MichelsonComparableType.Timestamp -> MichelsonComparableType.Timestamp
            MichelsonComparableType.Address -> MichelsonComparableType.Address
            MichelsonComparableType.Option -> {
                val type = args.first().convertToExpected<MichelsonComparableType>()

                MichelsonComparableType.Option(type)
            }
            MichelsonComparableType.Or -> {
                val lhs = args.first().convertToExpected<MichelsonComparableType>()
                val rhs = args.second().convertToExpected<MichelsonComparableType>()

                MichelsonComparableType.Or(lhs, rhs)
            }
            MichelsonComparableType.Pair -> {
                val types = args.convertToExpected<MichelsonComparableType>()
                require(types.size > 1)

                MichelsonComparableType.Pair(types)
            }
        }
    }

    private inline fun <reified T : Michelson> MichelineNode.convertToExpected(): T =
        when (T::class) {
            MichelsonData.NaturalNumberConstant::class -> toMichelsonConverter.convert(this)
                .also { require(it is MichelsonData.IntConstant) }
                .run { this as MichelsonData.IntConstant }
                .run { MichelsonData.NaturalNumberConstant(value) } as T
            else -> toMichelsonConverter.convert(this).also { require(it is T) } as T
        }

    private inline fun <reified T : Michelson> List<MichelineNode>.convertToExpected(): List<T> =
        map { it.convertToExpected() }

    private fun failWithUnknownPrimitiveApplication(primitiveApplication: MichelinePrimitiveApplication): Nothing =
        failWithIllegalArgument("Unknown Micheline Primitive Application: ${primitiveApplication.toCompactExpression(michelineToCompactStringConverter)}.")

    private fun failWithInvalidPrimitiveApplication(primitiveApplication: MichelinePrimitiveApplication): Nothing =
        failWithIllegalArgument("Invalid Micheline Primitive Application: ${primitiveApplication.toCompactExpression(michelineToCompactStringConverter)}.")
}

private class MichelineSequenceToMichelsonConverter(
    private val michelineToCompactStringConverter: MichelineToCompactStringConverter,
    private val toMichelsonConverter: MichelineToMichelsonConverter,
) : Converter<MichelineSequence, Michelson> {
    @Suppress("UNCHECKED_CAST")
    override fun convert(value: MichelineSequence): Michelson {
        val michelsonValues = value.nodes.map {
            if (it is MichelinePrimitiveApplication && it.isElt) it.convertToElt()
            else toMichelsonConverter.convert(it)
        }
        with(michelsonValues) {
            return when {
                isEmpty() -> MichelsonData.Sequence(emptyList())
                allIsInstance<MichelsonInstruction>() -> MichelsonInstruction.Sequence(this as List<MichelsonInstruction>)
                allIsInstance<MichelsonData.Elt>() -> MichelsonData.EltSequence(this as List<MichelsonData.Elt>)
                anyIsInstance<MichelsonData.Elt>() /* any but not all */ -> failWithInvalidSequence(value)
                allIsInstance<MichelsonData>() -> MichelsonData.Sequence(this as List<MichelsonData>)
                else -> failWithUnknownSequence(value)
            }
        }
    }

    private val MichelinePrimitiveApplication.isElt: Boolean
        get() = prim.value == MichelsonData.Elt.name

    private fun MichelinePrimitiveApplication.convertToElt(): MichelsonData.Elt {
        val key = args.first().convertToExpected<MichelsonData>()
        val value = args.second().convertToExpected<MichelsonData>()

        return MichelsonData.Elt(key, value)
    }

    private inline fun <reified T : Michelson> MichelineNode.convertToExpected(): T =
        when (T::class) {
            MichelsonData.NaturalNumberConstant::class -> toMichelsonConverter.convert(this)
                .also { require(it is MichelsonData.IntConstant) }
                .run { this as MichelsonData.IntConstant }
                .run { MichelsonData.NaturalNumberConstant(value) } as T
            else -> toMichelsonConverter.convert(this).also { require(it is T) } as T
        }

    private fun failWithUnknownSequence(sequence: MichelineSequence): Nothing =
        failWithIllegalArgument("Unknown Micheline Sequence: ${sequence.toCompactExpression(michelineToCompactStringConverter)}.")

    private fun failWithInvalidSequence(sequence: MichelineSequence): Nothing =
        failWithIllegalArgument("Invalid Micheline Sequence: ${sequence.toCompactExpression(michelineToCompactStringConverter)}.")
}
