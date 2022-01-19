import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

// -- extensions --

fun <T> String.inRange(range: ClosedRange<T>): Boolean where T : Comparable<T> {
    val maxNumericIfNegative = range.start.toString().removePrefix("-")
    val maxNumericIfPositive = range.endInclusive.toString()

    val (abs, max) = if (startsWith("-")) {
        Pair(removePrefix("-"), maxNumericIfNegative)
    } else {
        Pair(this, maxNumericIfPositive)
    }

    return if (abs.length == max.length) abs <= max
    else abs.length < max.length
}

// -- data sets --

val michelsonMichelinePairs: List<Pair<Michelson, MichelineNode>>
    get() = michelsonDataMichelinePairs + michelsonTypeMichelinePairs

val michelsonDataMichelinePairs: List<Pair<MichelsonData, MichelineNode>>
    get() = listOf(
        MichelsonData.IntConstant(1) to MichelineLiteral.Integer(1),
        MichelsonData.StringConstant("string") to MichelineLiteral.String("string"),
        MichelsonData.ByteSequenceConstant("0x00") to MichelineLiteral.Bytes("0x00"),
        MichelsonData.Unit to MichelinePrimitiveApplication("Unit"),
        MichelsonData.True to MichelinePrimitiveApplication("True"),
        MichelsonData.False to MichelinePrimitiveApplication("False"),
        MichelsonData.Pair(
            MichelsonData.True,
            MichelsonData.False,
        ) to MichelinePrimitiveApplication(
            "Pair",
            listOf(MichelinePrimitiveApplication("True"), MichelinePrimitiveApplication("False")),
        ),
        MichelsonData.Left(MichelsonData.Unit) to MichelinePrimitiveApplication("Left", listOf(MichelinePrimitiveApplication("Unit"))),
        MichelsonData.Right(MichelsonData.Unit) to MichelinePrimitiveApplication("Right", listOf(MichelinePrimitiveApplication("Unit"))),
        MichelsonData.Some(MichelsonData.Unit) to MichelinePrimitiveApplication("Some", listOf(MichelinePrimitiveApplication("Unit"))),
        MichelsonData.None to MichelinePrimitiveApplication("None"),
        MichelsonData.Sequence() to MichelineSequence(),
        MichelsonData.Sequence(MichelsonData.Unit) to MichelineSequence(MichelinePrimitiveApplication("Unit")),
        MichelsonData.Sequence(
            MichelsonInstruction.Unit,
            MichelsonData.Unit,
        ) to MichelineSequence(
            MichelinePrimitiveApplication("UNIT"),
            MichelinePrimitiveApplication("Unit"),
        ),
        MichelsonData.Sequence(
            MichelsonInstruction.Unit,
            MichelsonData.Unit,
        ) to MichelineSequence(
            MichelinePrimitiveApplication("UNIT"),
            MichelinePrimitiveApplication("Unit"),
        ),
        MichelsonData.EltSequence(
            MichelsonData.Elt(MichelsonData.Unit, MichelsonData.Unit)
        ) to MichelineSequence(
            MichelinePrimitiveApplication(
                "Elt",
                listOf(
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                ),
            ),
        ),
    ) + michelsonInstructionMichelinePairs

val michelsonInstructionMichelinePairs: List<Pair<MichelsonInstruction, MichelineNode>>
    get() = listOf(
        MichelsonInstruction.Sequence(MichelsonInstruction.Unit) to MichelineSequence(MichelinePrimitiveApplication("UNIT")),
        MichelsonInstruction.Drop() to MichelinePrimitiveApplication("DROP"),
        MichelsonInstruction.Drop(1U) to MichelinePrimitiveApplication("DROP", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Dup() to MichelinePrimitiveApplication("DUP"),
        MichelsonInstruction.Dup(1U) to MichelinePrimitiveApplication("DUP", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Swap to MichelinePrimitiveApplication("SWAP"),
        MichelsonInstruction.Dig(1U) to MichelinePrimitiveApplication("DIG", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Dug(1U) to MichelinePrimitiveApplication("DUG", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Push(
            MichelsonComparableType.Unit,
            MichelsonData.Unit,
        ) to MichelinePrimitiveApplication(
            "PUSH",
            listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("Unit"),
            ),
        ),
        MichelsonInstruction.Some to MichelinePrimitiveApplication("SOME"),
        MichelsonInstruction.None(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("NONE", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.Unit to MichelinePrimitiveApplication("UNIT"),
        MichelsonInstruction.Never to MichelinePrimitiveApplication("NEVER"),
        MichelsonInstruction.IfNone(
            MichelsonInstruction.Unit,
            MichelsonInstruction.Unit,
        ) to MichelinePrimitiveApplication(
            "IF_NONE",
            listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("UNIT")),
        ),
        MichelsonInstruction.Pair() to MichelinePrimitiveApplication("PAIR"),
        MichelsonInstruction.Pair(1U) to MichelinePrimitiveApplication("PAIR", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Car to MichelinePrimitiveApplication("CAR"),
        MichelsonInstruction.Cdr to MichelinePrimitiveApplication("CDR"),
        MichelsonInstruction.Unpair() to MichelinePrimitiveApplication("UNPAIR"),
        MichelsonInstruction.Unpair(1U) to MichelinePrimitiveApplication("UNPAIR", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Left(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("LEFT", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.Right(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("RIGHT", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.IfLeft(
            MichelsonInstruction.Unit,
            MichelsonInstruction.Unit,
        ) to MichelinePrimitiveApplication(
            "IF_LEFT",
            listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("UNIT")),
        ),
        MichelsonInstruction.Nil(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("NIL", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.Cons to MichelinePrimitiveApplication("CONS"),
        MichelsonInstruction.IfCons(
            MichelsonInstruction.Unit,
            MichelsonInstruction.Unit,
        ) to MichelinePrimitiveApplication(
            "IF_CONS",
            listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("UNIT")),
        ),
        MichelsonInstruction.Size to MichelinePrimitiveApplication("SIZE"),
        MichelsonInstruction.EmptySet(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("EMPTY_SET", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.EmptyMap(
            MichelsonComparableType.Unit,
            MichelsonComparableType.Unit,
        ) to MichelinePrimitiveApplication(
            "EMPTY_MAP",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonInstruction.EmptyBigMap(
            MichelsonComparableType.Unit,
            MichelsonComparableType.Unit,
        ) to MichelinePrimitiveApplication(
            "EMPTY_BIG_MAP",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonInstruction.Map(MichelsonInstruction.Unit) to MichelinePrimitiveApplication("MAP", listOf(MichelinePrimitiveApplication("UNIT"))),
        MichelsonInstruction.Iter(MichelsonInstruction.Unit) to MichelinePrimitiveApplication("ITER", listOf(MichelinePrimitiveApplication("UNIT"))),
        MichelsonInstruction.Mem to MichelinePrimitiveApplication("MEM"),
        MichelsonInstruction.Get() to MichelinePrimitiveApplication("GET"),
        MichelsonInstruction.Get(1U) to MichelinePrimitiveApplication("GET", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Update() to MichelinePrimitiveApplication("UPDATE"),
        MichelsonInstruction.Update(1U) to MichelinePrimitiveApplication("UPDATE", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.GetAndUpdate to MichelinePrimitiveApplication("GET_AND_UPDATE"),
        MichelsonInstruction.If(
            MichelsonInstruction.Unit,
            MichelsonInstruction.Unit,
        ) to MichelinePrimitiveApplication(
            "IF",
            listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("UNIT")),
        ),
        MichelsonInstruction.Loop(MichelsonInstruction.Unit) to MichelinePrimitiveApplication("LOOP", listOf(MichelinePrimitiveApplication("UNIT"))),
        MichelsonInstruction.LoopLeft(MichelsonInstruction.Unit) to MichelinePrimitiveApplication("LOOP_LEFT", listOf(MichelinePrimitiveApplication("UNIT"))),
        MichelsonInstruction.Lambda(
            MichelsonComparableType.Unit,
            MichelsonComparableType.Unit,
            MichelsonInstruction.Unit,
        ) to MichelinePrimitiveApplication(
            "LAMBDA",
            listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("UNIT"),
            )
        ),
        MichelsonInstruction.Exec to MichelinePrimitiveApplication("EXEC"),
        MichelsonInstruction.Apply to MichelinePrimitiveApplication("APPLY"),
        MichelsonInstruction.Dip(MichelsonInstruction.Unit) to MichelinePrimitiveApplication("DIP", listOf(MichelinePrimitiveApplication("UNIT"))),
        MichelsonInstruction.Dip(MichelsonInstruction.Unit, 1U) to MichelinePrimitiveApplication(
            "DIP",
            listOf(MichelineLiteral.Integer(1), MichelinePrimitiveApplication("UNIT")),
        ),
        MichelsonInstruction.Failwith to MichelinePrimitiveApplication("FAILWITH"),
        MichelsonInstruction.Cast to MichelinePrimitiveApplication("CAST"),
        MichelsonInstruction.Rename to MichelinePrimitiveApplication("RENAME"),
        MichelsonInstruction.Concat to MichelinePrimitiveApplication("CONCAT"),
        MichelsonInstruction.Slice to MichelinePrimitiveApplication("SLICE"),
        MichelsonInstruction.Pack to MichelinePrimitiveApplication("PACK"),
        MichelsonInstruction.Unpack(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("UNPACK", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.Add to MichelinePrimitiveApplication("ADD"),
        MichelsonInstruction.Sub to MichelinePrimitiveApplication("SUB"),
        MichelsonInstruction.Mul to MichelinePrimitiveApplication("MUL"),
        MichelsonInstruction.Ediv to MichelinePrimitiveApplication("EDIV"),
        MichelsonInstruction.Abs to MichelinePrimitiveApplication("ABS"),
        MichelsonInstruction.Isnat to MichelinePrimitiveApplication("ISNAT"),
        MichelsonInstruction.Int to MichelinePrimitiveApplication("INT"),
        MichelsonInstruction.Neg to MichelinePrimitiveApplication("NEG"),
        MichelsonInstruction.Lsl to MichelinePrimitiveApplication("LSL"),
        MichelsonInstruction.Lsr to MichelinePrimitiveApplication("LSR"),
        MichelsonInstruction.Or to MichelinePrimitiveApplication("OR"),
        MichelsonInstruction.And to MichelinePrimitiveApplication("AND"),
        MichelsonInstruction.Xor to MichelinePrimitiveApplication("XOR"),
        MichelsonInstruction.Not to MichelinePrimitiveApplication("NOT"),
        MichelsonInstruction.Compare to MichelinePrimitiveApplication("COMPARE"),
        MichelsonInstruction.Eq to MichelinePrimitiveApplication("EQ"),
        MichelsonInstruction.Neq to MichelinePrimitiveApplication("NEQ"),
        MichelsonInstruction.Lt to MichelinePrimitiveApplication("LT"),
        MichelsonInstruction.Gt to MichelinePrimitiveApplication("GT"),
        MichelsonInstruction.Le to MichelinePrimitiveApplication("LE"),
        MichelsonInstruction.Ge to MichelinePrimitiveApplication("GE"),
        MichelsonInstruction.Self to MichelinePrimitiveApplication("SELF"),
        MichelsonInstruction.SelfAddress to MichelinePrimitiveApplication("SELF_ADDRESS"),
        MichelsonInstruction.Contract(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("CONTRACT", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.TransferTokens to MichelinePrimitiveApplication("TRANSFER_TOKENS"),
        MichelsonInstruction.SetDelegate to MichelinePrimitiveApplication("SET_DELEGATE"),
        MichelsonInstruction.CreateContract(
            MichelsonComparableType.Unit,
            MichelsonComparableType.Unit,
            MichelsonInstruction.Unit,
        ) to MichelinePrimitiveApplication(
            "CREATE_CONTRACT",
            listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("UNIT"),
            )
        ),
        MichelsonInstruction.ImplicitAccount to MichelinePrimitiveApplication("IMPLICIT_ACCOUNT"),
        MichelsonInstruction.VotingPower to MichelinePrimitiveApplication("VOTING_POWER"),
        MichelsonInstruction.Now to MichelinePrimitiveApplication("NOW"),
        MichelsonInstruction.Level to MichelinePrimitiveApplication("LEVEL"),
        MichelsonInstruction.Amount to MichelinePrimitiveApplication("AMOUNT"),
        MichelsonInstruction.Balance to MichelinePrimitiveApplication("BALANCE"),
        MichelsonInstruction.CheckSignature to MichelinePrimitiveApplication("CHECK_SIGNATURE"),
        MichelsonInstruction.Blake2B to MichelinePrimitiveApplication("BLAKE2B"),
        MichelsonInstruction.Keccak to MichelinePrimitiveApplication("KECCAK"),
        MichelsonInstruction.Sha3 to MichelinePrimitiveApplication("SHA3"),
        MichelsonInstruction.Sha256 to MichelinePrimitiveApplication("SHA256"),
        MichelsonInstruction.Sha512 to MichelinePrimitiveApplication("SHA512"),
        MichelsonInstruction.HashKey to MichelinePrimitiveApplication("HASH_KEY"),
        MichelsonInstruction.Source to MichelinePrimitiveApplication("SOURCE"),
        MichelsonInstruction.Sender to MichelinePrimitiveApplication("SENDER"),
        MichelsonInstruction.Address to MichelinePrimitiveApplication("ADDRESS"),
        MichelsonInstruction.ChainId to MichelinePrimitiveApplication("CHAIN_ID"),
        MichelsonInstruction.TotalVotingPower to MichelinePrimitiveApplication("TOTAL_VOTING_POWER"),
        MichelsonInstruction.PairingCheck to MichelinePrimitiveApplication("PAIRING_CHECK"),
        MichelsonInstruction.SaplingEmptyState(1U) to MichelinePrimitiveApplication("SAPLING_EMPTY_STATE", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.SaplingVerifyUpdate to MichelinePrimitiveApplication("SAPLING_VERIFY_UPDATE"),
        MichelsonInstruction.Ticket to MichelinePrimitiveApplication("TICKET"),
        MichelsonInstruction.ReadTicket to MichelinePrimitiveApplication("READ_TICKET"),
        MichelsonInstruction.SplitTicket to MichelinePrimitiveApplication("SPLIT_TICKET"),
        MichelsonInstruction.JoinTickets to MichelinePrimitiveApplication("JOIN_TICKETS"),
        MichelsonInstruction.OpenChest to MichelinePrimitiveApplication("OPEN_CHEST"),
    )

val michelsonTypeMichelinePairs: List<Pair<MichelsonType, MichelineNode>>
    get() = listOf(
        MichelsonType.Parameter(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("parameter", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.Storage(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("storage", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.Code(MichelsonInstruction.Unit) to MichelinePrimitiveApplication("code", listOf(MichelinePrimitiveApplication("UNIT"))),
        MichelsonType.Option(MichelsonType.Operation) to MichelinePrimitiveApplication("option", listOf(MichelinePrimitiveApplication("operation"))),
        MichelsonType.List(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("list", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.Set(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("set", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.Operation to MichelinePrimitiveApplication("operation"),
        MichelsonType.Contract(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("contract", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.Ticket(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("ticket", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.Pair(
            MichelsonType.Operation,
            MichelsonType.Operation,
        ) to MichelinePrimitiveApplication(
            "pair",
            listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("operation")),
        ),
        MichelsonType.Pair(
            MichelsonType.Operation,
            MichelsonComparableType.String,
        ) to MichelinePrimitiveApplication(
            "pair",
            listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("string")),
        ),
        MichelsonType.Or(
            MichelsonType.Operation,
            MichelsonComparableType.Unit,
        ) to MichelinePrimitiveApplication(
            "or",
            listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonType.Lambda(
            MichelsonComparableType.Unit,
            MichelsonComparableType.Unit,
        ) to MichelinePrimitiveApplication(
            "lambda",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonType.Map(
            MichelsonComparableType.Unit,
            MichelsonComparableType.Unit,
        ) to MichelinePrimitiveApplication(
            "map",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonType.BigMap(
            MichelsonComparableType.Unit,
            MichelsonComparableType.Unit,
        ) to MichelinePrimitiveApplication(
            "big_map",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonType.Bls12_381G1 to MichelinePrimitiveApplication("bls12_381_g1"),
        MichelsonType.Bls12_381G2 to MichelinePrimitiveApplication("bls12_381_g2"),
        MichelsonType.Bls12_381Fr to MichelinePrimitiveApplication("bls12_381_fr"),
        MichelsonType.SaplingTransaction(1U) to MichelinePrimitiveApplication("sapling_transaction", listOf(MichelineLiteral.Integer(1))),
        MichelsonType.SaplingState(1U) to MichelinePrimitiveApplication("sapling_state", listOf(MichelineLiteral.Integer(1))),
        MichelsonType.Chest to MichelinePrimitiveApplication("chest"),
        MichelsonType.ChestKey to MichelinePrimitiveApplication("chest_key"),
    ) + michelsonComparableTypeMichelinePairs

val michelsonComparableTypeMichelinePairs: List<Pair<MichelsonComparableType, MichelineNode>>
    get() = listOf(
        MichelsonComparableType.Unit to MichelinePrimitiveApplication("unit"),
        MichelsonComparableType.Never to MichelinePrimitiveApplication("never"),
        MichelsonComparableType.Bool to MichelinePrimitiveApplication("bool"),
        MichelsonComparableType.Int to MichelinePrimitiveApplication("int"),
        MichelsonComparableType.Nat to MichelinePrimitiveApplication("nat"),
        MichelsonComparableType.String to MichelinePrimitiveApplication("string"),
        MichelsonComparableType.ChainId to MichelinePrimitiveApplication("chain_id"),
        MichelsonComparableType.Bytes to MichelinePrimitiveApplication("bytes"),
        MichelsonComparableType.Mutez to MichelinePrimitiveApplication("mutez"),
        MichelsonComparableType.KeyHash to MichelinePrimitiveApplication("key_hash"),
        MichelsonComparableType.Key to MichelinePrimitiveApplication("key"),
        MichelsonComparableType.Signature to MichelinePrimitiveApplication("signature"),
        MichelsonComparableType.Timestamp to MichelinePrimitiveApplication("timestamp"),
        MichelsonComparableType.Address to MichelinePrimitiveApplication("address"),
        MichelsonComparableType.Option(MichelsonComparableType.Unit) to MichelinePrimitiveApplication("option", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonComparableType.Or(
            MichelsonComparableType.Unit,
            MichelsonComparableType.Unit,
        ) to MichelinePrimitiveApplication(
            "or",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonComparableType.Pair(
            MichelsonComparableType.Int,
            MichelsonComparableType.String,
        ) to MichelinePrimitiveApplication(
            "pair",
            listOf(MichelinePrimitiveApplication("int"), MichelinePrimitiveApplication("string"))
        ),
    )