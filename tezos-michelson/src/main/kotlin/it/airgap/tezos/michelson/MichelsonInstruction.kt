package it.airgap.tezos.michelson

// https://tezos.gitlab.io/active/michelson.html#full-grammar
public sealed interface MichelsonInstruction : MichelsonData {
    public data class Sequence(public val instructions: List<MichelsonInstruction>) : MichelsonInstruction {
        public companion object {}
    }

    public data class Drop(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "DROP"
        }
    }

    public data class Dup(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "DUP"
        }
    }

    public object Swap : MichelsonInstruction, GrammarType {
        override val name: String = "SWAP"
    }

    public data class Dig(public val n: MichelsonData.NaturalNumberConstant) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "DIG"
        }
    }

    public data class Dug(public val n: MichelsonData.NaturalNumberConstant) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "DUG"
        }
    }

    public data class Push(public val type: MichelsonType, public val value: MichelsonData) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "PUSH"
        }
    }

    public object Some : MichelsonInstruction, GrammarType {
        override val name: String = "SOME"
    }

    public data class None(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "NONE"
        }
    }

    public object Unit : MichelsonInstruction, GrammarType {
        override val name: String = "UNIT"
    }

    public object Never : MichelsonInstruction, GrammarType {
        override val name: String = "NEVER"
    }

    public data class IfNone(
        public val ifBranch: MichelsonInstruction,
        public val elseBranch: MichelsonInstruction,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "IF_NONE"
        }
    }

    public data class Pair(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "PAIR"
        }
    }

    public object Car : MichelsonInstruction, GrammarType {
        override val name: String = "CAR"
    }
    public object Cdr : MichelsonInstruction, GrammarType {
        override val name: String = "CDR"
    }

    public data class Unpair(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "UNPAIR"
        }
    }

    public data class Left(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "LEFT"
        }
    }

    public data class Right(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "RIGHT"
        }
    }

    public data class IfLeft(
        public val ifBranch: MichelsonInstruction,
        public val elseBranch: MichelsonInstruction,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "IF_LEFT"
        }
    }

    public data class Nil(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "NIL"
        }
    }

    public object Cons : MichelsonInstruction, GrammarType {
        override val name: String = "CONS"
    }

    public data class IfCons(
        public val ifBranch: MichelsonInstruction,
        public val elseBranch: MichelsonInstruction,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "IF_CONS"
        }
    }

    public object Size : MichelsonInstruction, GrammarType {
        override val name: String = "SIZE"
    }

    public data class EmptySet(public val type: MichelsonComparableType) : MichelsonInstruction {

        public companion object : GrammarType {
            override val name: String = "EMPTY_SET"
        }
    }

    public data class EmptyMap(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "EMPTY_MAP"
        }
    }

    public data class EmptyBigMap(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "EMPTY_BIG_MAP"
        }
    }

    public data class Map(public val expression: MichelsonInstruction) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "MAP"
        }
    }

    public data class Iter(public val expression: MichelsonInstruction) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "ITER"
        }
    }

    public object Mem : MichelsonInstruction, GrammarType {
        override val name: String = "MEM"
    }

    public data class Get(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "GET"
        }
    }

    public data class Update(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "UPDATE"
        }
    }

    public data class If(
        public val ifBranch: MichelsonInstruction,
        public val elseBranch: MichelsonInstruction,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "IF"
        }
    }

    public data class Loop(public val body: MichelsonInstruction) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "LOOP"
        }
    }
    public data class LoopLeft(public val body: MichelsonInstruction) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "LOOP_LEFT"
        }
    }

    public data class Lambda(
        public val parameterType: MichelsonType,
        public val returnType: MichelsonType,
        public val body: MichelsonInstruction,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "LAMBDA"
        }
    }

    public object Exec : MichelsonInstruction, GrammarType {
        override val name: String = "EXEC"
    }

    public object Apply : MichelsonInstruction, GrammarType {
        override val name: String = "APPLY"
    }

    public data class Dip(
        public val instruction: MichelsonInstruction,
        public val n: MichelsonData.NaturalNumberConstant? = null,
    ) : MichelsonInstruction {

        public constructor(instruction: MichelsonInstruction, n: UByte) : this(instruction, MichelsonData.NaturalNumberConstant(n))
        public constructor(instruction: MichelsonInstruction, n: UShort) : this(instruction, MichelsonData.NaturalNumberConstant(n))
        public constructor(instruction: MichelsonInstruction, n: UInt) : this(instruction, MichelsonData.NaturalNumberConstant(n))
        public constructor(instruction: MichelsonInstruction, n: ULong) : this(instruction, MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "DIP"
        }
    }

    public object Failwith : MichelsonInstruction, GrammarType {
        override val name: String = "FAILWITH"
    }

    public object Cast : MichelsonInstruction, GrammarType {
        override val name: String = "CAST"
    }
    public object Rename : MichelsonInstruction, GrammarType {
        override val name: String = "RENAME"
    }
    public object Concat : MichelsonInstruction, GrammarType {
        override val name: String = "CONCAT"
    }
    public object Slice : MichelsonInstruction, GrammarType {
        override val name: String = "SLICE"
    }
    public object Pack : MichelsonInstruction, GrammarType {
        override val name: String = "PACK"
    }

    public data class Unpack(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "UNPACK"
        }
    }

    public object Add : MichelsonInstruction, GrammarType {
        override val name: String = "ADD"
    }

    public object Sub : MichelsonInstruction, GrammarType {
        override val name: String = "SUB"
    }

    public object Mul : MichelsonInstruction, GrammarType {
        override val name: String = "MUL"
    }

    public object Ediv : MichelsonInstruction, GrammarType {
        override val name: String = "EDIV"
    }

    public object Abs : MichelsonInstruction, GrammarType {
        override val name: String = "ABS"
    }

    public object Isnat : MichelsonInstruction, GrammarType {
        override val name: String = "ISNAT"
    }

    public object Int : MichelsonInstruction, GrammarType {
        override val name: String = "INT"
    }

    public object Neg : MichelsonInstruction, GrammarType {
        override val name: String = "NEG"
    }

    public object Lsl : MichelsonInstruction, GrammarType {
        override val name: String = "LSL"
    }

    public object Lsr : MichelsonInstruction, GrammarType {
        override val name: String = "LSR"
    }

    public object Or : MichelsonInstruction, GrammarType {
        override val name: String = "OR"
    }

    public object And : MichelsonInstruction, GrammarType {
        override val name: String = "AND"
    }

    public object Xor : MichelsonInstruction, GrammarType {
        override val name: String = "XOR"
    }

    public object Not : MichelsonInstruction, GrammarType {
        override val name: String = "NOT"
    }

    public object Compare : MichelsonInstruction, GrammarType {
        override val name: String = "COMPARE"
    }

    public object Eq : MichelsonInstruction, GrammarType {
        override val name: String = "EQ"
    }

    public object Neq : MichelsonInstruction, GrammarType {
        override val name: String = "NEQ"
    }

    public object Lt : MichelsonInstruction, GrammarType {
        override val name: String = "LT"
    }

    public object Gt : MichelsonInstruction, GrammarType {
        override val name: String = "GT"
    }

    public object Le : MichelsonInstruction, GrammarType {
        override val name: String = "LE"
    }

    public object Ge : MichelsonInstruction, GrammarType {
        override val name: String = "GE"
    }

    public object Self : MichelsonInstruction, GrammarType {
        override val name: String = "SELF"
    }

    public object SelfAddress : MichelsonInstruction, GrammarType {
        override val name: String = "SELF_ADDRESS"
    }

    public data class Contract(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "CONTRACT"
        }
    }

    public object TransferTokens : MichelsonInstruction, GrammarType {
        override val name: String = "TRANSFER_TOKENS"
    }

    public object SetDelegate : MichelsonInstruction, GrammarType {
        override val name: String = "SET_DELEGATE"
    }

    public data class CreateContract(
        public val parameterType: MichelsonType,
        public val storageType: MichelsonType,
        public val code: MichelsonInstruction,
    ) :
        MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "CREATE_CONTRACT"
        }
    }

    public object ImplicitAccount : MichelsonInstruction, GrammarType {
        override val name: String = "IMPLICIT_ACCOUNT"
    }

    public object VotingPower : MichelsonInstruction, GrammarType {
        override val name: String = "VOTING_POWER"
    }

    public object Now : MichelsonInstruction, GrammarType {
        override val name: String = "NOW"
    }

    public object Level : MichelsonInstruction, GrammarType {
        override val name: String = "LEVEL"
    }

    public object Amount : MichelsonInstruction, GrammarType {
        override val name: String = "AMOUNT"
    }

    public object Balance : MichelsonInstruction, GrammarType {
        override val name: String = "BALANCE"
    }

    public object CheckSignature : MichelsonInstruction, GrammarType {
        override val name: String = "CHECK_SIGNATURE"
    }

    public object Blake2B : MichelsonInstruction, GrammarType {
        override val name: String = "BLAKE2B"
    }

    public object Keccak : MichelsonInstruction, GrammarType {
        override val name: String = "KECCAK"
    }

    public object Sha3 : MichelsonInstruction, GrammarType {
        override val name: String = "SHA3"
    }

    public object Sha256 : MichelsonInstruction, GrammarType {
        override val name: String = "SHA256"
    }

    public object Sha512 : MichelsonInstruction, GrammarType {
        override val name: String = "SHA512"
    }

    public object HashKey : MichelsonInstruction, GrammarType {
        override val name: String = "HASH_KEY"
    }

    public object Source : MichelsonInstruction, GrammarType {
        override val name: String = "SOURCE"
    }

    public object Sender : MichelsonInstruction, GrammarType {
        override val name: String = "SENDER"
    }

    public object Address : MichelsonInstruction, GrammarType {
        override val name: String = "ADDRESS"
    }

    public object ChainId : MichelsonInstruction, GrammarType {
        override val name: String = "CHAIN_ID"
    }

    public object TotalVotingPower : MichelsonInstruction, GrammarType {
        override val name: String = "TOTAL_VOTING_POWER"
    }

    public object PairingCheck : MichelsonInstruction, GrammarType {
        override val name: String = "PAIRING_CHECK"
    }

    public data class SaplingEmptyState(public val memoSize: MichelsonData.NaturalNumberConstant) : MichelsonInstruction {

        public constructor(memoSize: UByte) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UShort) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UInt) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: ULong) : this(MichelsonData.NaturalNumberConstant(memoSize))

        public companion object : GrammarType {
            override val name: String = "SAPLING_EMPTY_STATE"
        }
    }

    public object SaplingVerifyUpdate : MichelsonInstruction, GrammarType {
        override val name: String = "SAPLING_VERIFY_UPDATE"
    }

    public object Ticket : MichelsonInstruction, GrammarType {
        override val name: String = "TICKET"
    }

    public object ReadTicket : MichelsonInstruction, GrammarType {
        override val name: String = "READ_TICKET"
    }

    public object SplitTicket : MichelsonInstruction, GrammarType {
        override val name: String = "SPLIT_TICKET"
    }

    public object JoinTickets : MichelsonInstruction, GrammarType {
        override val name: String = "JOIN_TICKETS"
    }

    public object OpenChest : MichelsonInstruction, GrammarType {
        override val name: String = "OPEN_CHEST"
    }

    public sealed interface GrammarType : MichelsonData.GrammarType {
        public companion object {}
    }

    public companion object {
        public fun Sequence(vararg instructions: MichelsonInstruction): Sequence = Sequence(instructions.toList())
    }
}