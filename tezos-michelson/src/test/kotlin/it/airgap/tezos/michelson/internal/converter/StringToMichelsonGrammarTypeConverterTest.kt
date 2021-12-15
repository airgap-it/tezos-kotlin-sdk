package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.michelson.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class StringToMichelsonGrammarTypeConverterTest {

    @Test
    fun `should convert name to Michelson GrammarType`() {
        val valuesWithExpected = grammarTypes.map {
            when (it) {
                MichelsonComparableType.Option -> it.name to MichelsonType.Option
                MichelsonComparableType.Pair -> it.name to MichelsonType.Pair
                MichelsonComparableType.Or -> it.name to MichelsonType.Or
                else -> it.name to it
            }
        }

        valuesWithExpected.forEach {
            assertEquals(it.second, StringToMichelsonGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, Michelson.GrammarType.fromStringOrNull(it.first))
        }
    }

    @Test
    fun `should convert name to MichelsonData GrammarType`() {
        val valuesWithExpected = dataGrammarTypes.map { it.name to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, StringToMichelsonDataGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonData.GrammarType.fromStringOrNull(it.first))
        }
    }

    @Test
    fun `should convert name to MichelsonInstruction GrammarType`() {
        val valuesWithExpected = instructionGrammarTypes.map { it.name to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, StringToMichelsonInstructionGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonInstruction.GrammarType.fromStringOrNull(it.first))
        }
    }

    @Test
    fun `should convert name to MichelsonType GrammarType`() {
        val valuesWithExpected = typeGrammarTypes.map {
            when (it) {
                MichelsonComparableType.Option -> it.name to MichelsonType.Option
                MichelsonComparableType.Pair -> it.name to MichelsonType.Pair
                MichelsonComparableType.Or -> it.name to MichelsonType.Or
                else -> it.name to it
            }
        }

        valuesWithExpected.forEach {
            assertEquals(it.second, StringToMichelsonTypeGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonType.GrammarType.fromStringOrNull(it.first))
        }
    }

    @Test
    fun `should convert name to MichelsonComparableType GrammarType`() {
        val valuesWithExpected = comparableTypeGrammarTypes.map { it.name to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, StringToMichelsonComparableTypeGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonComparableType.GrammarType.fromStringOrNull(it.first))
        }
    }

    @Test
    fun `should fail to convert unknown string to Michelson GrammarType`() {
        val unknownStrings = listOf("unknown")

        unknownStrings.forEach {
            val message = "Unknown Michelson grammar type: \"$it\"."

            assertFailsWith<IllegalArgumentException>(message) {
                StringToMichelsonGrammarTypeConverter.convert(it)
            }
            assertNull(Michelson.GrammarType.fromStringOrNull(it))

            assertFailsWith<IllegalArgumentException>(message) {
                StringToMichelsonDataGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonData.GrammarType.fromStringOrNull(it))

            assertFailsWith<IllegalArgumentException>(message) {
                StringToMichelsonInstructionGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonInstruction.GrammarType.fromStringOrNull(it))

            assertFailsWith<IllegalArgumentException>(message) {
                StringToMichelsonTypeGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonType.GrammarType.fromStringOrNull(it))

            assertFailsWith<IllegalArgumentException>(message) {
                StringToMichelsonComparableTypeGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonComparableType.GrammarType.fromStringOrNull(it))
        }
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
}