package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

internal class MichelsonToMichelineConverter : Converter<Michelson, MichelineNode> {
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
            MichelsonData.Unit -> MichelinePrimitiveApplication(MichelsonData.Unit, annots = michelineAnnotations)
            MichelsonData.True -> MichelinePrimitiveApplication(MichelsonData.True, annots = michelineAnnotations)
            MichelsonData.False -> MichelinePrimitiveApplication(MichelsonData.False, annots = michelineAnnotations)
            is MichelsonData.Pair -> MichelinePrimitiveApplication(
                MichelsonData.Pair,
                args = values.map { convert(it) },
                annots = michelineAnnotations,
            )
            is MichelsonData.Left -> MichelinePrimitiveApplication(
                MichelsonData.Left,
                args = listOf(convert(this.value)),
                annots = michelineAnnotations,
            )
            is MichelsonData.Right -> MichelinePrimitiveApplication(
                MichelsonData.Right,
                args = listOf(convert(this.value)),
                annots = michelineAnnotations,
            )
            is MichelsonData.Some -> MichelinePrimitiveApplication(
                MichelsonData.Some,
                args = listOf(convert(this.value)),
                annots = michelineAnnotations,
            )
            MichelsonData.None -> MichelinePrimitiveApplication(MichelsonData.None, annots = michelineAnnotations)
            is MichelsonData.Sequence -> MichelineSequence(values.map { convert(it) })
            is MichelsonData.Map -> MichelineSequence(values.map {
                MichelinePrimitiveApplication(
                    MichelsonData.Elt,
                    args = listOf(
                        toMichelineConverter.convert(it.key),
                        toMichelineConverter.convert(it.value),
                    ),
                    annots = michelineAnnotations,
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
                args = listOfNotNull(n?.let(dataToMichelineConverter::convert)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Dup -> MichelinePrimitiveApplication(
                MichelsonInstruction.Dup,
                args = listOfNotNull(n?.let(dataToMichelineConverter::convert)),
                annots = michelineAnnotations,
            )
            MichelsonInstruction.Swap -> MichelinePrimitiveApplication(MichelsonInstruction.Swap, annots = michelineAnnotations)
            is MichelsonInstruction.Dig -> MichelinePrimitiveApplication(
                MichelsonInstruction.Dig,
                args = listOf(dataToMichelineConverter.convert(n)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Dug -> MichelinePrimitiveApplication(
                MichelsonInstruction.Dug,
                args = listOf(dataToMichelineConverter.convert(n)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Push -> MichelinePrimitiveApplication(
                MichelsonInstruction.Push,
                args = listOf(toMichelineConverter.convert(type), toMichelineConverter.convert(this.value)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Some -> MichelinePrimitiveApplication(MichelsonInstruction.Some, annots = michelineAnnotations)
            is MichelsonInstruction. None -> MichelinePrimitiveApplication(
                MichelsonInstruction.None,
                args = listOf(toMichelineConverter.convert(type)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Unit -> MichelinePrimitiveApplication(MichelsonInstruction.Unit, annots = michelineAnnotations)
            is MichelsonInstruction.Never -> MichelinePrimitiveApplication(MichelsonInstruction.Never, annots = michelineAnnotations)
            is MichelsonInstruction.IfNone -> MichelinePrimitiveApplication(
                MichelsonInstruction.IfNone,
                args = listOf(
                    toMichelineConverter.convert(ifBranch),
                    toMichelineConverter.convert(elseBranch),
                ),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Pair -> MichelinePrimitiveApplication(
                MichelsonInstruction.Pair,
                args = listOfNotNull(n?.let(dataToMichelineConverter::convert)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Car -> MichelinePrimitiveApplication(MichelsonInstruction.Car, annots = michelineAnnotations)
            is MichelsonInstruction.Cdr -> MichelinePrimitiveApplication(MichelsonInstruction.Cdr, annots = michelineAnnotations)
            is MichelsonInstruction.Unpair -> MichelinePrimitiveApplication(
                MichelsonInstruction.Unpair,
                args = listOfNotNull(n?.let(dataToMichelineConverter::convert)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Left -> MichelinePrimitiveApplication(
                MichelsonInstruction.Left,
                args = listOf(toMichelineConverter.convert(type)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Right -> MichelinePrimitiveApplication(
                MichelsonInstruction.Right,
                args = listOf(toMichelineConverter.convert(type)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.IfLeft -> MichelinePrimitiveApplication(
                MichelsonInstruction.IfLeft,
                args = listOf(
                    toMichelineConverter.convert(ifBranch),
                    toMichelineConverter.convert(elseBranch),
                ),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Nil -> MichelinePrimitiveApplication(
                MichelsonInstruction.Nil,
                args = listOf(toMichelineConverter.convert(type)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Cons -> MichelinePrimitiveApplication(MichelsonInstruction.Cons, annots = michelineAnnotations)
            is MichelsonInstruction.IfCons -> MichelinePrimitiveApplication(
                MichelsonInstruction.IfCons,
                args = listOf(toMichelineConverter.convert(ifBranch), toMichelineConverter.convert(elseBranch)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Size -> MichelinePrimitiveApplication(MichelsonInstruction.Size, annots = michelineAnnotations)
            is MichelsonInstruction.EmptySet -> MichelinePrimitiveApplication(
                MichelsonInstruction.EmptySet,
                args = listOf(toMichelineConverter.convert(type)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.EmptyMap -> MichelinePrimitiveApplication(
                MichelsonInstruction.EmptyMap,
                args = listOf(
                    toMichelineConverter.convert(keyType),
                    toMichelineConverter.convert(valueType),
                ),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.EmptyBigMap -> MichelinePrimitiveApplication(
                MichelsonInstruction.EmptyBigMap,
                args = listOf(
                    toMichelineConverter.convert(keyType),
                    toMichelineConverter.convert(valueType),
                ),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Map -> MichelinePrimitiveApplication(
                MichelsonInstruction.Map,
                args = listOf(toMichelineConverter.convert(expression)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Iter -> MichelinePrimitiveApplication(
                MichelsonInstruction.Iter,
                args = listOf(toMichelineConverter.convert(expression)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Mem -> MichelinePrimitiveApplication(MichelsonInstruction.Mem, annots = michelineAnnotations)
            is MichelsonInstruction.Get -> MichelinePrimitiveApplication(
                MichelsonInstruction.Get,
                args = listOfNotNull(n?.let(dataToMichelineConverter::convert)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Update -> MichelinePrimitiveApplication(
                MichelsonInstruction.Update,
                args = listOfNotNull(n?.let(dataToMichelineConverter::convert)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.GetAndUpdate -> MichelinePrimitiveApplication(MichelsonInstruction.GetAndUpdate)
            is MichelsonInstruction.If -> MichelinePrimitiveApplication(
                MichelsonInstruction.If,
                args = listOf(
                    toMichelineConverter.convert(ifBranch),
                    toMichelineConverter.convert(elseBranch),
                ),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Loop -> MichelinePrimitiveApplication(
                MichelsonInstruction.Loop,
                args = listOf(toMichelineConverter.convert(body)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.LoopLeft -> MichelinePrimitiveApplication(
                MichelsonInstruction.LoopLeft,
                args = listOf(toMichelineConverter.convert(body)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Lambda -> MichelinePrimitiveApplication(
                MichelsonInstruction.Lambda,
                args = listOf(
                    toMichelineConverter.convert(parameterType),
                    toMichelineConverter.convert(returnType),
                    toMichelineConverter.convert(body)
                ),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Exec -> MichelinePrimitiveApplication(MichelsonInstruction.Exec, annots = michelineAnnotations)
            MichelsonInstruction.Apply -> MichelinePrimitiveApplication(MichelsonInstruction.Apply)
            is MichelsonInstruction.Dip -> MichelinePrimitiveApplication(
                MichelsonInstruction.Dip,
                args = listOfNotNull(
                    n?.let(dataToMichelineConverter::convert),
                    toMichelineConverter.convert(instruction),
                ),
                annots = michelineAnnotations,
            )
            MichelsonInstruction.Failwith -> MichelinePrimitiveApplication(MichelsonInstruction.Failwith, annots = michelineAnnotations)
            is MichelsonInstruction.Cast -> MichelinePrimitiveApplication(MichelsonInstruction.Cast, annots = michelineAnnotations)
            is MichelsonInstruction.Rename -> MichelinePrimitiveApplication(MichelsonInstruction.Rename, annots = michelineAnnotations)
            is MichelsonInstruction.Concat -> MichelinePrimitiveApplication(MichelsonInstruction.Concat, annots = michelineAnnotations)
            MichelsonInstruction.Slice -> MichelinePrimitiveApplication(MichelsonInstruction.Slice, annots = michelineAnnotations)
            MichelsonInstruction.Pack -> MichelinePrimitiveApplication(MichelsonInstruction.Pack, annots = michelineAnnotations)
            is MichelsonInstruction.Unpack -> MichelinePrimitiveApplication(
                MichelsonInstruction.Unpack,
                args = listOf(toMichelineConverter.convert(type)),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.Add -> MichelinePrimitiveApplication(MichelsonInstruction.Add, annots = michelineAnnotations)
            is MichelsonInstruction.Sub -> MichelinePrimitiveApplication(MichelsonInstruction.Sub, annots = michelineAnnotations)
            is MichelsonInstruction.Mul -> MichelinePrimitiveApplication(MichelsonInstruction.Mul, annots = michelineAnnotations)
            is MichelsonInstruction.Ediv -> MichelinePrimitiveApplication(MichelsonInstruction.Ediv, annots = michelineAnnotations)
            is MichelsonInstruction.Abs -> MichelinePrimitiveApplication(MichelsonInstruction.Abs, annots = michelineAnnotations)
            is MichelsonInstruction.Isnat -> MichelinePrimitiveApplication(MichelsonInstruction.Isnat, annots = michelineAnnotations)
            is MichelsonInstruction.Int -> MichelinePrimitiveApplication(MichelsonInstruction.Int, annots = michelineAnnotations)
            is MichelsonInstruction.Neg -> MichelinePrimitiveApplication(MichelsonInstruction.Neg, annots = michelineAnnotations)
            is MichelsonInstruction.Lsl -> MichelinePrimitiveApplication(MichelsonInstruction.Lsl, annots = michelineAnnotations)
            is MichelsonInstruction.Lsr -> MichelinePrimitiveApplication(MichelsonInstruction.Lsr, annots = michelineAnnotations)
            is MichelsonInstruction.Or -> MichelinePrimitiveApplication(MichelsonInstruction.Or, annots = michelineAnnotations)
            is MichelsonInstruction.And -> MichelinePrimitiveApplication(MichelsonInstruction.And, annots = michelineAnnotations)
            is MichelsonInstruction.Xor -> MichelinePrimitiveApplication(MichelsonInstruction.Xor, annots = michelineAnnotations)
            is MichelsonInstruction.Not -> MichelinePrimitiveApplication(MichelsonInstruction.Not, annots = michelineAnnotations)
            is MichelsonInstruction.Compare -> MichelinePrimitiveApplication(MichelsonInstruction.Compare, annots = michelineAnnotations)
            is MichelsonInstruction.Eq -> MichelinePrimitiveApplication(MichelsonInstruction.Eq, annots = michelineAnnotations)
            is MichelsonInstruction.Neq -> MichelinePrimitiveApplication(MichelsonInstruction.Neq, annots = michelineAnnotations)
            is MichelsonInstruction.Lt -> MichelinePrimitiveApplication(MichelsonInstruction.Lt, annots = michelineAnnotations)
            is MichelsonInstruction.Gt -> MichelinePrimitiveApplication(MichelsonInstruction.Gt, annots = michelineAnnotations)
            is MichelsonInstruction.Le -> MichelinePrimitiveApplication(MichelsonInstruction.Le, annots = michelineAnnotations)
            is MichelsonInstruction.Ge -> MichelinePrimitiveApplication(MichelsonInstruction.Ge, annots = michelineAnnotations)
            is MichelsonInstruction.Self -> MichelinePrimitiveApplication(MichelsonInstruction.Self, annots = michelineAnnotations)
            is MichelsonInstruction.SelfAddress -> MichelinePrimitiveApplication(MichelsonInstruction.SelfAddress, annots = michelineAnnotations)
            is MichelsonInstruction.Contract -> MichelinePrimitiveApplication(
                MichelsonInstruction.Contract,
                args = listOf(toMichelineConverter.convert(type)),
                annots = michelineAnnotations,
            )
            MichelsonInstruction.TransferTokens -> MichelinePrimitiveApplication(MichelsonInstruction.TransferTokens, annots = michelineAnnotations)
            is MichelsonInstruction.SetDelegate -> MichelinePrimitiveApplication(MichelsonInstruction.SetDelegate, annots = michelineAnnotations)
            is MichelsonInstruction.CreateContract -> MichelinePrimitiveApplication(
                MichelsonInstruction.CreateContract,
                args = listOf(
                    toMichelineConverter.convert(parameterType),
                    toMichelineConverter.convert(storageType),
                    toMichelineConverter.convert(code),
                ),
                annots = michelineAnnotations,
            )
            is MichelsonInstruction.ImplicitAccount -> MichelinePrimitiveApplication(MichelsonInstruction.ImplicitAccount, annots = michelineAnnotations)
            MichelsonInstruction.VotingPower -> MichelinePrimitiveApplication(MichelsonInstruction.VotingPower, annots = michelineAnnotations)
            is MichelsonInstruction.Now -> MichelinePrimitiveApplication(MichelsonInstruction.Now, annots = michelineAnnotations)
            is MichelsonInstruction.Level -> MichelinePrimitiveApplication(MichelsonInstruction.Level, annots = michelineAnnotations)
            is MichelsonInstruction.Amount -> MichelinePrimitiveApplication(MichelsonInstruction.Amount, annots = michelineAnnotations)
            is MichelsonInstruction.Balance -> MichelinePrimitiveApplication(MichelsonInstruction.Balance, annots = michelineAnnotations)
            is MichelsonInstruction.CheckSignature -> MichelinePrimitiveApplication(MichelsonInstruction.CheckSignature, annots = michelineAnnotations)
            is MichelsonInstruction.Blake2B -> MichelinePrimitiveApplication(MichelsonInstruction.Blake2B, annots = michelineAnnotations)
            is MichelsonInstruction.Keccak -> MichelinePrimitiveApplication(MichelsonInstruction.Keccak, annots = michelineAnnotations)
            is MichelsonInstruction.Sha3 -> MichelinePrimitiveApplication(MichelsonInstruction.Sha3, annots = michelineAnnotations)
            is MichelsonInstruction.Sha256 -> MichelinePrimitiveApplication(MichelsonInstruction.Sha256, annots = michelineAnnotations)
            is MichelsonInstruction.Sha512 -> MichelinePrimitiveApplication(MichelsonInstruction.Sha512, annots = michelineAnnotations)
            is MichelsonInstruction.HashKey -> MichelinePrimitiveApplication(MichelsonInstruction.HashKey, annots = michelineAnnotations)
            is MichelsonInstruction.Source -> MichelinePrimitiveApplication(MichelsonInstruction.Source, annots = michelineAnnotations)
            is MichelsonInstruction.Sender -> MichelinePrimitiveApplication(MichelsonInstruction.Sender, annots = michelineAnnotations)
            is MichelsonInstruction.Address -> MichelinePrimitiveApplication(MichelsonInstruction.Address, annots = michelineAnnotations)
            is MichelsonInstruction.ChainId -> MichelinePrimitiveApplication(MichelsonInstruction.ChainId, annots = michelineAnnotations)
            MichelsonInstruction.TotalVotingPower -> MichelinePrimitiveApplication(MichelsonInstruction.TotalVotingPower, annots = michelineAnnotations)
            MichelsonInstruction.PairingCheck -> MichelinePrimitiveApplication(MichelsonInstruction.PairingCheck, annots = michelineAnnotations)
            is MichelsonInstruction.SaplingEmptyState -> MichelinePrimitiveApplication(
                MichelsonInstruction.SaplingEmptyState,
                args = listOf(dataToMichelineConverter.convert(memoSize)),
                annots = michelineAnnotations,
            )
            MichelsonInstruction.SaplingVerifyUpdate -> MichelinePrimitiveApplication(MichelsonInstruction.SaplingVerifyUpdate, annots = michelineAnnotations)
            MichelsonInstruction.Ticket -> MichelinePrimitiveApplication(MichelsonInstruction.Ticket, annots = michelineAnnotations)
            MichelsonInstruction.ReadTicket -> MichelinePrimitiveApplication(MichelsonInstruction.ReadTicket, annots = michelineAnnotations)
            MichelsonInstruction.SplitTicket -> MichelinePrimitiveApplication(MichelsonInstruction.SplitTicket, annots = michelineAnnotations)
            MichelsonInstruction.JoinTickets -> MichelinePrimitiveApplication(MichelsonInstruction.JoinTickets, annots = michelineAnnotations)
            MichelsonInstruction.OpenChest -> MichelinePrimitiveApplication(MichelsonInstruction.OpenChest, annots = michelineAnnotations)
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
            is MichelsonType.Parameter -> MichelinePrimitiveApplication(
                MichelsonType.Parameter,
                args = listOf(convert(this.type)),
                annots = michelineAnnotations,
            )
            is MichelsonType.Storage -> MichelinePrimitiveApplication(
                MichelsonType.Storage,
                args = listOf(convert(this.type)),
                annots = michelineAnnotations,
            )
            is MichelsonType.Code -> MichelinePrimitiveApplication(
                MichelsonType.Code,
                args = listOf(instructionToMichelineConverter.convert(this.code)),
                annots = michelineAnnotations,
            )
            is MichelsonType.Option -> MichelinePrimitiveApplication(
                MichelsonType.Option,
                args = listOf(convert(this.type)),
                annots = michelineAnnotations,
            )
            is MichelsonType.List -> MichelinePrimitiveApplication(
                MichelsonType.List,
                args = listOf(convert(this.type)),
                annots = michelineAnnotations,
            )
            is MichelsonType.Set -> MichelinePrimitiveApplication(
                MichelsonType.Set,
                args = listOf(convert(this.type)),
                annots = michelineAnnotations,
            )
            is MichelsonType.Operation -> MichelinePrimitiveApplication(MichelsonType.Operation, annots = michelineAnnotations)
            is MichelsonType.Contract -> MichelinePrimitiveApplication(
                MichelsonType.Contract,
                listOf(convert(this.type)),
                annots = michelineAnnotations,
            )
            is MichelsonType.Ticket -> MichelinePrimitiveApplication(
                MichelsonType.Ticket,
                listOf(convert(this.type)),
                annots = michelineAnnotations,
            )
            is MichelsonType.Pair -> MichelinePrimitiveApplication(
                MichelsonType.Pair,
                types.map { convert(it) },
                annots = michelineAnnotations,
            )
            is MichelsonType.Or -> MichelinePrimitiveApplication(
                MichelsonType.Or,
                listOf(convert(lhs), convert(rhs)),
                annots = michelineAnnotations,
            )
            is MichelsonType.Lambda -> MichelinePrimitiveApplication(
                MichelsonType.Lambda,
                listOf(convert(parameterType), convert(parameterType)),
                annots = michelineAnnotations,
            )
            is MichelsonType.Map -> MichelinePrimitiveApplication(
                MichelsonType.Map,
                listOf(convert(keyType), convert(valueType)),
                annots = michelineAnnotations,
            )
            is MichelsonType.BigMap -> MichelinePrimitiveApplication(
                MichelsonType.BigMap,
                listOf(convert(keyType), convert(valueType)),
                annots = michelineAnnotations,
            )
            is MichelsonType.Bls12_381G1 -> MichelinePrimitiveApplication(MichelsonType.Bls12_381G1, annots = michelineAnnotations)
            is MichelsonType.Bls12_381G2 -> MichelinePrimitiveApplication(MichelsonType.Bls12_381G2, annots = michelineAnnotations)
            is MichelsonType. Bls12_381Fr -> MichelinePrimitiveApplication(MichelsonType.Bls12_381Fr, annots = michelineAnnotations)
            is MichelsonType.SaplingTransaction -> MichelinePrimitiveApplication(
                MichelsonType.SaplingTransaction,
                args = listOf(dataToMichelineConverter.convert(memoSize)),
                annots = michelineAnnotations,
            )
            is MichelsonType.SaplingState -> MichelinePrimitiveApplication(
                MichelsonType.SaplingState,
                args = listOf(dataToMichelineConverter.convert(memoSize)),
                annots = michelineAnnotations,
            )
            is MichelsonType.Chest -> MichelinePrimitiveApplication(MichelsonType.Chest, annots = michelineAnnotations)
            is MichelsonType.ChestKey -> MichelinePrimitiveApplication(MichelsonType.ChestKey, annots = michelineAnnotations)
            is MichelsonComparableType -> comparableTypeToMichelineConverter.convert(this)
        }
    }
}

private class MichelsonComparableTypeToMichelineConverter : Converter<MichelsonComparableType, MichelineNode> {
    override fun convert(value: MichelsonComparableType): MichelineNode = with(value) {
        when (this) {
            is MichelsonComparableType.Unit -> MichelinePrimitiveApplication(MichelsonComparableType.Unit, annots = michelineAnnotations)
            is MichelsonComparableType.Never -> MichelinePrimitiveApplication(MichelsonComparableType.Never, annots = michelineAnnotations)
            is MichelsonComparableType.Bool -> MichelinePrimitiveApplication(MichelsonComparableType.Bool, annots = michelineAnnotations)
            is MichelsonComparableType.Int -> MichelinePrimitiveApplication(MichelsonComparableType.Int, annots = michelineAnnotations)
            is MichelsonComparableType.Nat -> MichelinePrimitiveApplication(MichelsonComparableType.Nat, annots = michelineAnnotations)
            is MichelsonComparableType.String -> MichelinePrimitiveApplication(MichelsonComparableType.String, annots = michelineAnnotations)
            is MichelsonComparableType.ChainId ->  MichelinePrimitiveApplication(MichelsonComparableType.ChainId, annots = michelineAnnotations)
            is MichelsonComparableType.Bytes -> MichelinePrimitiveApplication(MichelsonComparableType.Bytes, annots = michelineAnnotations)
            is MichelsonComparableType.Mutez -> MichelinePrimitiveApplication(MichelsonComparableType.Mutez, annots = michelineAnnotations)
            is MichelsonComparableType.KeyHash -> MichelinePrimitiveApplication(MichelsonComparableType.KeyHash, annots = michelineAnnotations)
            is MichelsonComparableType.Key -> MichelinePrimitiveApplication(MichelsonComparableType.Key, annots = michelineAnnotations)
            is MichelsonComparableType.Signature -> MichelinePrimitiveApplication(MichelsonComparableType.Signature, annots = michelineAnnotations)
            is MichelsonComparableType.Timestamp -> MichelinePrimitiveApplication(MichelsonComparableType.Timestamp, annots = michelineAnnotations)
            is MichelsonComparableType.Address -> MichelinePrimitiveApplication(MichelsonComparableType.Address, annots = michelineAnnotations)
            is MichelsonComparableType.Option -> MichelinePrimitiveApplication(
                MichelsonComparableType.Option,
                args = listOf(convert(type)),
                annots = michelineAnnotations,
            )
            is MichelsonComparableType.Or -> MichelinePrimitiveApplication(
                MichelsonComparableType.Or,
                args = listOf(convert(lhs), convert(rhs)),
                annots = michelineAnnotations,
            )
            is MichelsonComparableType.Pair -> MichelinePrimitiveApplication(
                MichelsonComparableType.Pair,
                args = types.map { convert(it) },
                annots = michelineAnnotations,
            )
        }
    }
}

private fun <T : Michelson.Prim> MichelinePrimitiveApplication(
    prim: T,
    args: List<MichelineNode> = emptyList(),
    annots: List<MichelinePrimitiveApplication.Annotation> = emptyList(),
): MichelinePrimitiveApplication =
    MichelinePrimitiveApplication(MichelinePrimitiveApplication.Primitive(prim.name), args, annots)

private val Michelson.michelineAnnotations: List<MichelinePrimitiveApplication.Annotation>
    get() = annotations.mapNotNull { it.asMichelineAnnotation() }

private fun Michelson.Annotation.asMichelineAnnotation(): MichelinePrimitiveApplication.Annotation? =
    if (MichelinePrimitiveApplication.Annotation.isValid(value)) MichelinePrimitiveApplication.Annotation(value) else null
