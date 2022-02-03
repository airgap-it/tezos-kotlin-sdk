package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

@InternalTezosSdkApi
public class MichelsonToMichelineConverter : Converter<Michelson, MichelineNode> {
    private val dataToMichelineConverter: MichelsonDataToMichelineConverter = MichelsonDataToMichelineConverter(this)
    private val typeToMichelineConverter: MichelsonTypeToMichelineConverter = MichelsonTypeToMichelineConverter(
        dataToMichelineConverter,
        dataToMichelineConverter.instructionToMichelineConverter
    )

    override fun convert(value: Michelson): MichelineNode =
        when (value) {
            is MichelsonData -> dataToMichelineConverter.convert(value)
            is MichelsonType -> typeToMichelineConverter.convert(value)
        }
}

private class MichelsonDataToMichelineConverter(
    private val toMichelineConverter: MichelsonToMichelineConverter
) : Converter<MichelsonData, MichelineNode> {
    val instructionToMichelineConverter: MichelsonInstructionToMichelineConverter = MichelsonInstructionToMichelineConverter(toMichelineConverter, this)

    override fun convert(value: MichelsonData): MichelineNode = with(value) {
        when (this) {
            is MichelsonData.IntConstant -> MichelineLiteral.Integer(this.value)
            is MichelsonData.NaturalNumberConstant -> MichelineLiteral.Integer(this.value)
            is MichelsonData.StringConstant -> MichelineLiteral.String(this.value)
            is MichelsonData.ByteSequenceConstant -> MichelineLiteral.Bytes(this.value)
            MichelsonData.Unit -> MichelinePrimitiveApplication(MichelsonData.Unit)
            MichelsonData.True -> MichelinePrimitiveApplication(MichelsonData.True)
            MichelsonData.False -> MichelinePrimitiveApplication(MichelsonData.False)
            is MichelsonData.Pair -> MichelinePrimitiveApplication(MichelsonData.Pair, values.map { convert(it) })
            is MichelsonData.Left -> MichelinePrimitiveApplication(MichelsonData.Left, listOf(convert(this.value)))
            is MichelsonData.Right -> MichelinePrimitiveApplication(MichelsonData.Right, listOf(convert(this.value)))
            is MichelsonData.Some -> MichelinePrimitiveApplication(MichelsonData.Some, listOf(convert(this.value)))
            MichelsonData.None -> MichelinePrimitiveApplication(MichelsonData.None)
            is MichelsonData.Sequence -> MichelineSequence(values.map { convert(it) })
            is MichelsonData.EltSequence -> MichelineSequence(values.map {
                MichelinePrimitiveApplication(
                    MichelsonData.Elt,
                    listOf(
                        toMichelineConverter.convert(it.key),
                        toMichelineConverter.convert(it.value),
                    )
                )
            })
            is MichelsonInstruction -> instructionToMichelineConverter.convert(this)
        }
    }
}

private class MichelsonInstructionToMichelineConverter(
    private val toMichelineConverter: MichelsonToMichelineConverter,
    private val dataToMichelineConverter: MichelsonDataToMichelineConverter,
) : Converter<MichelsonInstruction, MichelineNode> {

    override fun convert(value: MichelsonInstruction): MichelineNode = with(value) {
        when (this) {
            is MichelsonInstruction.Sequence -> MichelineSequence(instructions.map { toMichelineConverter.convert(it) })
            is MichelsonInstruction.Drop -> MichelinePrimitiveApplication(
                MichelsonInstruction.Drop,
                listOfNotNull(n?.let(dataToMichelineConverter::convert)),
            )
            is MichelsonInstruction.Dup -> MichelinePrimitiveApplication(
                MichelsonInstruction.Dup,
                listOfNotNull(n?.let(dataToMichelineConverter::convert)),
            )
            MichelsonInstruction.Swap -> MichelinePrimitiveApplication(MichelsonInstruction.Swap)
            is MichelsonInstruction.Dig -> MichelinePrimitiveApplication(
                MichelsonInstruction.Dig,
                listOf(dataToMichelineConverter.convert(n)),
            )
            is MichelsonInstruction.Dug -> MichelinePrimitiveApplication(
                MichelsonInstruction.Dug,
                listOf(dataToMichelineConverter.convert(n)),
            )
            is MichelsonInstruction.Push -> MichelinePrimitiveApplication(
                MichelsonInstruction.Push,
                listOf(toMichelineConverter.convert(type), toMichelineConverter.convert(this.value)),
            )
            MichelsonInstruction.Some -> MichelinePrimitiveApplication(MichelsonInstruction.Some)
            is MichelsonInstruction. None -> MichelinePrimitiveApplication(
                MichelsonInstruction.None,
                listOf(toMichelineConverter.convert(type)),
            )
            MichelsonInstruction.Unit -> MichelinePrimitiveApplication(MichelsonInstruction.Unit)
            MichelsonInstruction.Never -> MichelinePrimitiveApplication(MichelsonInstruction.Never)
            is MichelsonInstruction.IfNone -> MichelinePrimitiveApplication(
                MichelsonInstruction.IfNone,
                listOf(
                    toMichelineConverter.convert(ifBranch),
                    toMichelineConverter.convert(elseBranch),
                ),
            )
            is MichelsonInstruction.Pair -> MichelinePrimitiveApplication(
                MichelsonInstruction.Pair,
                listOfNotNull(n?.let(dataToMichelineConverter::convert)),
            )
            MichelsonInstruction.Car -> MichelinePrimitiveApplication(MichelsonInstruction.Car)
            MichelsonInstruction.Cdr -> MichelinePrimitiveApplication(MichelsonInstruction.Cdr)
            is MichelsonInstruction.Unpair -> MichelinePrimitiveApplication(
                MichelsonInstruction.Unpair,
                listOfNotNull(n?.let(dataToMichelineConverter::convert)),
            )
            is MichelsonInstruction.Left -> MichelinePrimitiveApplication(
                MichelsonInstruction.Left,
                listOf(toMichelineConverter.convert(type)),
            )
            is MichelsonInstruction.Right -> MichelinePrimitiveApplication(
                MichelsonInstruction.Right,
                listOf(toMichelineConverter.convert(type)),
            )
            is MichelsonInstruction.IfLeft -> MichelinePrimitiveApplication(
                MichelsonInstruction.IfLeft,
                listOf(
                    toMichelineConverter.convert(ifBranch),
                    toMichelineConverter.convert(elseBranch),
                ),
            )
            is MichelsonInstruction.Nil -> MichelinePrimitiveApplication(
                MichelsonInstruction.Nil,
                listOf(toMichelineConverter.convert(type)),
            )
            MichelsonInstruction.Cons -> MichelinePrimitiveApplication(MichelsonInstruction.Cons)
            is MichelsonInstruction.IfCons -> MichelinePrimitiveApplication(
                MichelsonInstruction.IfCons,
                listOf(toMichelineConverter.convert(ifBranch), toMichelineConverter.convert(elseBranch)),
            )
            MichelsonInstruction.Size -> MichelinePrimitiveApplication(MichelsonInstruction.Size)
            is MichelsonInstruction.EmptySet -> MichelinePrimitiveApplication(
                MichelsonInstruction.EmptySet,
                listOf(toMichelineConverter.convert(type)),
            )
            is MichelsonInstruction.EmptyMap -> MichelinePrimitiveApplication(
                MichelsonInstruction.EmptyMap,
                listOf(
                    toMichelineConverter.convert(keyType),
                    toMichelineConverter.convert(valueType),
                ),
            )
            is MichelsonInstruction.EmptyBigMap -> MichelinePrimitiveApplication(
                MichelsonInstruction.EmptyBigMap,
                listOf(
                    toMichelineConverter.convert(keyType),
                    toMichelineConverter.convert(valueType),
                ),
            )
            is MichelsonInstruction.Map -> MichelinePrimitiveApplication(
                MichelsonInstruction.Map,
                listOf(toMichelineConverter.convert(expression)),
            )
            is MichelsonInstruction.Iter -> MichelinePrimitiveApplication(
                MichelsonInstruction.Iter,
                listOf(toMichelineConverter.convert(expression)),
            )
            MichelsonInstruction.Mem -> MichelinePrimitiveApplication(MichelsonInstruction.Mem)
            is MichelsonInstruction.Get -> MichelinePrimitiveApplication(
                MichelsonInstruction.Get,
                listOfNotNull(n?.let(dataToMichelineConverter::convert)),
            )
            is MichelsonInstruction.Update -> MichelinePrimitiveApplication(
                MichelsonInstruction.Update,
                listOfNotNull(n?.let(dataToMichelineConverter::convert)),
            )
            is MichelsonInstruction.GetAndUpdate -> MichelinePrimitiveApplication(MichelsonInstruction.GetAndUpdate)
            is MichelsonInstruction.If -> MichelinePrimitiveApplication(
                MichelsonInstruction.If,
                listOf(
                    toMichelineConverter.convert(ifBranch),
                    toMichelineConverter.convert(elseBranch),
                ),
            )
            is MichelsonInstruction.Loop -> MichelinePrimitiveApplication(
                MichelsonInstruction.Loop,
                listOf(toMichelineConverter.convert(body)),
            )
            is MichelsonInstruction.LoopLeft -> MichelinePrimitiveApplication(
                MichelsonInstruction.LoopLeft,
                listOf(toMichelineConverter.convert(body)),
            )
            is MichelsonInstruction.Lambda -> MichelinePrimitiveApplication(
                MichelsonInstruction.Lambda,
                listOf(
                    toMichelineConverter.convert(parameterType),
                    toMichelineConverter.convert(returnType),
                    toMichelineConverter.convert(body)),
            )
            MichelsonInstruction.Exec -> MichelinePrimitiveApplication(MichelsonInstruction.Exec)
            MichelsonInstruction.Apply -> MichelinePrimitiveApplication(MichelsonInstruction.Apply)
            is MichelsonInstruction.Dip -> MichelinePrimitiveApplication(
                MichelsonInstruction.Dip,
                listOfNotNull(
                    n?.let(dataToMichelineConverter::convert),
                    toMichelineConverter.convert(instruction),
                ),
            )
            MichelsonInstruction.Failwith -> MichelinePrimitiveApplication(MichelsonInstruction.Failwith)
            MichelsonInstruction.Cast -> MichelinePrimitiveApplication(MichelsonInstruction.Cast)
            MichelsonInstruction.Rename -> MichelinePrimitiveApplication(MichelsonInstruction.Rename)
            MichelsonInstruction.Concat -> MichelinePrimitiveApplication(MichelsonInstruction.Concat)
            MichelsonInstruction.Slice -> MichelinePrimitiveApplication(MichelsonInstruction.Slice)
            MichelsonInstruction.Pack -> MichelinePrimitiveApplication(MichelsonInstruction.Pack)
            is MichelsonInstruction.Unpack -> MichelinePrimitiveApplication(
                MichelsonInstruction.Unpack,
                listOf(toMichelineConverter.convert(type)),
            )
            MichelsonInstruction.Add -> MichelinePrimitiveApplication(MichelsonInstruction.Add)
            MichelsonInstruction.Sub -> MichelinePrimitiveApplication(MichelsonInstruction.Sub)
            MichelsonInstruction.Mul -> MichelinePrimitiveApplication(MichelsonInstruction.Mul)
            MichelsonInstruction.Ediv -> MichelinePrimitiveApplication(MichelsonInstruction.Ediv)
            MichelsonInstruction.Abs -> MichelinePrimitiveApplication(MichelsonInstruction.Abs)
            MichelsonInstruction.Isnat -> MichelinePrimitiveApplication(MichelsonInstruction.Isnat)
            MichelsonInstruction.Int -> MichelinePrimitiveApplication(MichelsonInstruction.Int)
            MichelsonInstruction.Neg -> MichelinePrimitiveApplication(MichelsonInstruction.Neg)
            MichelsonInstruction.Lsl -> MichelinePrimitiveApplication(MichelsonInstruction.Lsl)
            MichelsonInstruction.Lsr -> MichelinePrimitiveApplication(MichelsonInstruction.Lsr)
            MichelsonInstruction.Or -> MichelinePrimitiveApplication(MichelsonInstruction.Or)
            MichelsonInstruction.And -> MichelinePrimitiveApplication(MichelsonInstruction.And)
            MichelsonInstruction.Xor -> MichelinePrimitiveApplication(MichelsonInstruction.Xor)
            MichelsonInstruction.Not -> MichelinePrimitiveApplication(MichelsonInstruction.Not)
            MichelsonInstruction.Compare -> MichelinePrimitiveApplication(MichelsonInstruction.Compare)
            MichelsonInstruction.Eq -> MichelinePrimitiveApplication(MichelsonInstruction.Eq)
            MichelsonInstruction.Neq -> MichelinePrimitiveApplication(MichelsonInstruction.Neq)
            MichelsonInstruction.Lt -> MichelinePrimitiveApplication(MichelsonInstruction.Lt)
            MichelsonInstruction.Gt -> MichelinePrimitiveApplication(MichelsonInstruction.Gt)
            MichelsonInstruction.Le -> MichelinePrimitiveApplication(MichelsonInstruction.Le)
            MichelsonInstruction.Ge -> MichelinePrimitiveApplication(MichelsonInstruction.Ge)
            MichelsonInstruction.Self -> MichelinePrimitiveApplication(MichelsonInstruction.Self)
            MichelsonInstruction.SelfAddress -> MichelinePrimitiveApplication(MichelsonInstruction.SelfAddress)
            is MichelsonInstruction.Contract -> MichelinePrimitiveApplication(
                MichelsonInstruction.Contract,
                listOf(toMichelineConverter.convert(type)),
            )
            MichelsonInstruction.TransferTokens -> MichelinePrimitiveApplication(MichelsonInstruction.TransferTokens)
            MichelsonInstruction.SetDelegate -> MichelinePrimitiveApplication(MichelsonInstruction.SetDelegate)
            is MichelsonInstruction.CreateContract -> MichelinePrimitiveApplication(
                MichelsonInstruction.CreateContract,
                listOf(
                    toMichelineConverter.convert(parameterType),
                    toMichelineConverter.convert(storageType),
                    toMichelineConverter.convert(code),
                ),
            )
            MichelsonInstruction.ImplicitAccount -> MichelinePrimitiveApplication(MichelsonInstruction.ImplicitAccount)
            MichelsonInstruction.VotingPower -> MichelinePrimitiveApplication(MichelsonInstruction.VotingPower)
            MichelsonInstruction.Now -> MichelinePrimitiveApplication(MichelsonInstruction.Now)
            MichelsonInstruction.Level -> MichelinePrimitiveApplication(MichelsonInstruction.Level)
            MichelsonInstruction.Amount -> MichelinePrimitiveApplication(MichelsonInstruction.Amount)
            MichelsonInstruction.Balance -> MichelinePrimitiveApplication(MichelsonInstruction.Balance)
            MichelsonInstruction.CheckSignature -> MichelinePrimitiveApplication(MichelsonInstruction.CheckSignature)
            MichelsonInstruction.Blake2B -> MichelinePrimitiveApplication(MichelsonInstruction.Blake2B)
            MichelsonInstruction.Keccak -> MichelinePrimitiveApplication(MichelsonInstruction.Keccak)
            MichelsonInstruction.Sha3 -> MichelinePrimitiveApplication(MichelsonInstruction.Sha3)
            MichelsonInstruction.Sha256 -> MichelinePrimitiveApplication(MichelsonInstruction.Sha256)
            MichelsonInstruction.Sha512 -> MichelinePrimitiveApplication(MichelsonInstruction.Sha512)
            MichelsonInstruction.HashKey -> MichelinePrimitiveApplication(MichelsonInstruction.HashKey)
            MichelsonInstruction.Source -> MichelinePrimitiveApplication(MichelsonInstruction.Source)
            MichelsonInstruction.Sender -> MichelinePrimitiveApplication(MichelsonInstruction.Sender)
            MichelsonInstruction.Address -> MichelinePrimitiveApplication(MichelsonInstruction.Address)
            MichelsonInstruction.ChainId -> MichelinePrimitiveApplication(MichelsonInstruction.ChainId)
            MichelsonInstruction.TotalVotingPower -> MichelinePrimitiveApplication(MichelsonInstruction.TotalVotingPower)
            MichelsonInstruction.PairingCheck -> MichelinePrimitiveApplication(MichelsonInstruction.PairingCheck)
            is MichelsonInstruction.SaplingEmptyState -> MichelinePrimitiveApplication(
                MichelsonInstruction.SaplingEmptyState,
                listOf(dataToMichelineConverter.convert(memoSize)),
            )
            MichelsonInstruction.SaplingVerifyUpdate -> MichelinePrimitiveApplication(MichelsonInstruction.SaplingVerifyUpdate)
            MichelsonInstruction.Ticket -> MichelinePrimitiveApplication(MichelsonInstruction.Ticket)
            MichelsonInstruction.ReadTicket -> MichelinePrimitiveApplication(MichelsonInstruction.ReadTicket)
            MichelsonInstruction.SplitTicket -> MichelinePrimitiveApplication(MichelsonInstruction.SplitTicket)
            MichelsonInstruction.JoinTickets -> MichelinePrimitiveApplication(MichelsonInstruction.JoinTickets)
            MichelsonInstruction.OpenChest -> MichelinePrimitiveApplication(MichelsonInstruction.OpenChest)
        }
    }
}

private class MichelsonTypeToMichelineConverter(
    private val dataToMichelineConverter: MichelsonDataToMichelineConverter,
    private val instructionToMichelineConverter: MichelsonInstructionToMichelineConverter,
) : Converter<MichelsonType, MichelineNode> {
    val comparableTypeToMichelineConverter: MichelsonComparableTypeToMichelineConverter = MichelsonComparableTypeToMichelineConverter()

    override fun convert(value: MichelsonType): MichelineNode = with(value) {
        when (this) {
            is MichelsonType.Parameter -> MichelinePrimitiveApplication(MichelsonType.Parameter, listOf(convert(this.type)))
            is MichelsonType.Storage -> MichelinePrimitiveApplication(MichelsonType.Storage, listOf(convert(this.type)))
            is MichelsonType.Code -> MichelinePrimitiveApplication(MichelsonType.Code, listOf(instructionToMichelineConverter.convert(this.code)))
            is MichelsonType.Option -> MichelinePrimitiveApplication(MichelsonType.Option, listOf(convert(this.type)))
            is MichelsonType.List -> MichelinePrimitiveApplication(MichelsonType.List, listOf(convert(this.type)))
            is MichelsonType.Set -> MichelinePrimitiveApplication(MichelsonType.Set, listOf(convert(this.type)))
            MichelsonType.Operation -> MichelinePrimitiveApplication(MichelsonType.Operation)
            is MichelsonType.Contract -> MichelinePrimitiveApplication(MichelsonType.Contract, listOf(convert(this.type)))
            is MichelsonType.Ticket -> MichelinePrimitiveApplication(MichelsonType.Ticket, listOf(convert(this.type)))
            is MichelsonType.Pair -> MichelinePrimitiveApplication(MichelsonType.Pair, types.map { convert(it) })
            is MichelsonType.Or -> MichelinePrimitiveApplication(MichelsonType.Or, listOf(convert(lhs), convert(rhs)))
            is MichelsonType.Lambda -> MichelinePrimitiveApplication(MichelsonType.Lambda, listOf(convert(parameterType), convert(parameterType)))
            is MichelsonType.Map -> MichelinePrimitiveApplication(MichelsonType.Map, listOf(convert(keyType), convert(valueType)))
            is MichelsonType.BigMap -> MichelinePrimitiveApplication(MichelsonType.BigMap, listOf(convert(keyType), convert(valueType)), )
            MichelsonType.Bls12_381G1 -> MichelinePrimitiveApplication(MichelsonType.Bls12_381G1)
            MichelsonType.Bls12_381G2 -> MichelinePrimitiveApplication(MichelsonType.Bls12_381G2)
            MichelsonType. Bls12_381Fr -> MichelinePrimitiveApplication(MichelsonType.Bls12_381Fr)
            is MichelsonType.SaplingTransaction -> MichelinePrimitiveApplication(
                MichelsonType.SaplingTransaction,
                listOf(dataToMichelineConverter.convert(memoSize)),
            )
            is MichelsonType.SaplingState -> MichelinePrimitiveApplication(
                MichelsonType.SaplingState,
                listOf(dataToMichelineConverter.convert(memoSize)),
            )
            MichelsonType.Chest -> MichelinePrimitiveApplication(MichelsonType.Chest)
            MichelsonType.ChestKey -> MichelinePrimitiveApplication(MichelsonType.ChestKey)
            is MichelsonComparableType -> comparableTypeToMichelineConverter.convert(this)
        }
    }
}

private class MichelsonComparableTypeToMichelineConverter : Converter<MichelsonComparableType, MichelineNode> {
    override fun convert(value: MichelsonComparableType): MichelineNode = with(value) {
        when (this) {
            MichelsonComparableType.Unit -> MichelinePrimitiveApplication(MichelsonComparableType.Unit)
            MichelsonComparableType.Never -> MichelinePrimitiveApplication(MichelsonComparableType.Never)
            MichelsonComparableType.Bool -> MichelinePrimitiveApplication(MichelsonComparableType.Bool)
            MichelsonComparableType.Int -> MichelinePrimitiveApplication(MichelsonComparableType.Int)
            MichelsonComparableType.Nat -> MichelinePrimitiveApplication(MichelsonComparableType.Nat)
            MichelsonComparableType.String -> MichelinePrimitiveApplication(MichelsonComparableType.String)
            MichelsonComparableType.ChainId ->  MichelinePrimitiveApplication(MichelsonComparableType.ChainId)
            MichelsonComparableType.Bytes -> MichelinePrimitiveApplication(MichelsonComparableType.Bytes)
            MichelsonComparableType.Mutez -> MichelinePrimitiveApplication(MichelsonComparableType.Mutez)
            MichelsonComparableType.KeyHash -> MichelinePrimitiveApplication(MichelsonComparableType.KeyHash)
            MichelsonComparableType.Key -> MichelinePrimitiveApplication(MichelsonComparableType.Key)
            MichelsonComparableType.Signature -> MichelinePrimitiveApplication(MichelsonComparableType.Signature)
            MichelsonComparableType.Timestamp -> MichelinePrimitiveApplication(MichelsonComparableType.Timestamp)
            MichelsonComparableType.Address -> MichelinePrimitiveApplication(MichelsonComparableType.Address)
            is MichelsonComparableType.Option -> MichelinePrimitiveApplication(MichelsonComparableType.Option, listOf(convert(type)))
            is MichelsonComparableType.Or -> MichelinePrimitiveApplication(MichelsonComparableType.Or, listOf(convert(lhs), convert(rhs)))
            is MichelsonComparableType.Pair -> MichelinePrimitiveApplication(MichelsonComparableType.Pair, types.map { convert(it) })
        }
    }
}