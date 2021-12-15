package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.michelson.*

internal object StringToMichelsonGrammarTypeConverter : Converter<String, Michelson.GrammarType> {
    override fun convert(value: String): Michelson.GrammarType =
        grammarTypes.firstOrNull { value == it.name } ?: failWithUnknownGrammarType(value)
}

internal object StringToMichelsonDataGrammarTypeConverter : Converter<String, MichelsonData.GrammarType> {
    override fun convert(value: String): MichelsonData.GrammarType =
        dataGrammarTypes.firstOrNull { value == it.name } ?: failWithUnknownGrammarType(value)
}

internal object StringToMichelsonInstructionGrammarTypeConverter : Converter<String, MichelsonInstruction.GrammarType> {
    override fun convert(value: String): MichelsonInstruction.GrammarType =
        instructionGrammarTypes.firstOrNull { value == it.name } ?: failWithUnknownGrammarType(value)
}

internal object StringToMichelsonTypeGrammarTypeConverter : Converter<String, MichelsonType.GrammarType> {
    override fun convert(value: String): MichelsonType.GrammarType =
        typeGrammarTypes.firstOrNull { value == it.name } ?: failWithUnknownGrammarType(value)
}

internal object StringToMichelsonComparableTypeGrammarTypeConverter : Converter<String, MichelsonComparableType.GrammarType> {
    override fun convert(value: String): MichelsonComparableType.GrammarType =
        comparableTypeGrammarTypes.firstOrNull { value == it.name } ?: failWithUnknownGrammarType(value)
}

private val grammarTypes: List<Michelson.GrammarType>
    get() = dataGrammarTypes + typeGrammarTypes

private val dataGrammarTypes: List<MichelsonData.GrammarType>
    get() = listOf(
        MichelsonData.Unit,
        MichelsonData.True,
        MichelsonData.False,
        MichelsonData.Pair,
        MichelsonData.Left,
        MichelsonData.Right,
        MichelsonData.Some,
        MichelsonData.None,
        MichelsonData.Elt
    ) + instructionGrammarTypes

private val instructionGrammarTypes: List<MichelsonInstruction.GrammarType>
    get() = listOf(
        MichelsonInstruction.Drop,
        MichelsonInstruction.Dup,
        MichelsonInstruction.Swap,
        MichelsonInstruction.Dig,
        MichelsonInstruction.Dug,
        MichelsonInstruction.Push,
        MichelsonInstruction.Some,
        MichelsonInstruction.None,
        MichelsonInstruction.Unit,
        MichelsonInstruction.Never,
        MichelsonInstruction.IfNone,
        MichelsonInstruction.Pair,
        MichelsonInstruction.Car,
        MichelsonInstruction.Cdr,
        MichelsonInstruction.Unpair,
        MichelsonInstruction.Left,
        MichelsonInstruction.Right,
        MichelsonInstruction.IfLeft,
        MichelsonInstruction.Nil,
        MichelsonInstruction.Cons,
        MichelsonInstruction.IfCons,
        MichelsonInstruction.Size,
        MichelsonInstruction.EmptySet,
        MichelsonInstruction.EmptyMap,
        MichelsonInstruction.EmptyBigMap,
        MichelsonInstruction.Map,
        MichelsonInstruction.Iter,
        MichelsonInstruction.Mem,
        MichelsonInstruction.Get,
        MichelsonInstruction.Update,
        MichelsonInstruction.If,
        MichelsonInstruction.Loop,
        MichelsonInstruction.LoopLeft,
        MichelsonInstruction.Lambda,
        MichelsonInstruction.Exec,
        MichelsonInstruction.Apply,
        MichelsonInstruction.Dip,
        MichelsonInstruction.Failwith,
        MichelsonInstruction.Cast,
        MichelsonInstruction.Rename,
        MichelsonInstruction.Concat,
        MichelsonInstruction.Slice,
        MichelsonInstruction.Pack,
        MichelsonInstruction.Unpack,
        MichelsonInstruction.Add,
        MichelsonInstruction.Sub,
        MichelsonInstruction.Mul,
        MichelsonInstruction.Ediv,
        MichelsonInstruction.Abs,
        MichelsonInstruction.Isnat,
        MichelsonInstruction.Int,
        MichelsonInstruction.Neg,
        MichelsonInstruction.Lsl,
        MichelsonInstruction.Lsr,
        MichelsonInstruction.Or,
        MichelsonInstruction.And,
        MichelsonInstruction.Xor,
        MichelsonInstruction.Not,
        MichelsonInstruction.Compare,
        MichelsonInstruction.Eq,
        MichelsonInstruction.Neq,
        MichelsonInstruction.Lt,
        MichelsonInstruction.Gt,
        MichelsonInstruction.Le,
        MichelsonInstruction.Ge,
        MichelsonInstruction.Self,
        MichelsonInstruction.SelfAddress,
        MichelsonInstruction.Contract,
        MichelsonInstruction.TransferTokens,
        MichelsonInstruction.SetDelegate,
        MichelsonInstruction.CreateContract,
        MichelsonInstruction.ImplicitAccount,
        MichelsonInstruction.VotingPower,
        MichelsonInstruction.Now,
        MichelsonInstruction.Level,
        MichelsonInstruction.Amount,
        MichelsonInstruction.Balance,
        MichelsonInstruction.CheckSignature,
        MichelsonInstruction.Blake2B,
        MichelsonInstruction.Keccak,
        MichelsonInstruction.Sha3,
        MichelsonInstruction.Sha256,
        MichelsonInstruction.Sha512,
        MichelsonInstruction.HashKey,
        MichelsonInstruction.Source,
        MichelsonInstruction.Sender,
        MichelsonInstruction.Address,
        MichelsonInstruction.ChainId,
        MichelsonInstruction.TotalVotingPower,
        MichelsonInstruction.PairingCheck,
        MichelsonInstruction.SaplingEmptyState,
        MichelsonInstruction.SaplingVerifyUpdate,
        MichelsonInstruction.Ticket,
        MichelsonInstruction.ReadTicket,
        MichelsonInstruction.SplitTicket,
        MichelsonInstruction.JoinTickets,
        MichelsonInstruction.OpenChest,
    )

private val typeGrammarTypes: List<MichelsonType.GrammarType>
    get() = listOf(
        MichelsonType.Option,
        MichelsonType.List,
        MichelsonType.Set,
        MichelsonType.Operation,
        MichelsonType.Contract,
        MichelsonType.Ticket,
        MichelsonType.Pair,
        MichelsonType.Or,
        MichelsonType.Lambda,
        MichelsonType.Map,
        MichelsonType.BigMap,
        MichelsonType.Bls12_381G1,
        MichelsonType.Bls12_381G2,
        MichelsonType.Bls12_381Fr,
        MichelsonType.SaplingTransaction,
        MichelsonType.SaplingState,
        MichelsonType.Chest,
        MichelsonType.ChestKey,
    ) + comparableTypeGrammarTypes

private val comparableTypeGrammarTypes: List<MichelsonComparableType.GrammarType>
    get() = listOf(
        MichelsonComparableType.Unit,
        MichelsonComparableType.Never,
        MichelsonComparableType.Bool,
        MichelsonComparableType.Int,
        MichelsonComparableType.Nat,
        MichelsonComparableType.String,
        MichelsonComparableType.ChainId,
        MichelsonComparableType.Bytes,
        MichelsonComparableType.Mutez,
        MichelsonComparableType.KeyHash,
        MichelsonComparableType.Key,
        MichelsonComparableType.Signature,
        MichelsonComparableType.Timestamp,
        MichelsonComparableType.Address,
        MichelsonComparableType.Option,
        MichelsonComparableType.Or,
        MichelsonComparableType.Pair,
    )

private fun failWithUnknownGrammarType(grammarType: String): Nothing =
    failWithIllegalArgument("Unknown Michelson grammar type: \"$grammarType\".")
