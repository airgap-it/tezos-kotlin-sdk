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
            override val tag: kotlin.Int = 32
        }
    }

    public data class Dup(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "DUP"
            override val tag: kotlin.Int = 33
        }
    }

    public object Swap : MichelsonInstruction, GrammarType {
        override val name: String = "SWAP"
        override val tag: kotlin.Int = 76
    }

    public data class Dig(public val n: MichelsonData.NaturalNumberConstant) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "DIG"
            override val tag: kotlin.Int = 112
        }
    }

    public data class Dug(public val n: MichelsonData.NaturalNumberConstant) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "DUG"
            override val tag: kotlin.Int = 113
        }
    }

    public data class Push(public val type: MichelsonType, public val value: MichelsonData) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "PUSH"
            override val tag: kotlin.Int = 67
        }
    }

    public object Some : MichelsonInstruction, GrammarType {
        override val name: String = "SOME"
        override val tag: kotlin.Int = 70
    }

    public data class None(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "NONE"
            override val tag: kotlin.Int = 62
        }
    }

    public object Unit : MichelsonInstruction, GrammarType {
        override val name: String = "UNIT"
        override val tag: kotlin.Int = 79
    }

    public object Never : MichelsonInstruction, GrammarType {
        override val name: String = "NEVER"
        override val tag: kotlin.Int = 121
    }

    public data class IfNone(
        public val ifBranch: MichelsonInstruction,
        public val elseBranch: MichelsonInstruction,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "IF_NONE"
            override val tag: kotlin.Int = 47
        }
    }

    public data class Pair(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "PAIR"
            override val tag: kotlin.Int = 66
        }
    }

    public object Car : MichelsonInstruction, GrammarType {
        override val name: String = "CAR"
        override val tag: kotlin.Int = 22
    }
    public object Cdr : MichelsonInstruction, GrammarType {
        override val name: String = "CDR"
        override val tag: kotlin.Int = 23
    }

    public data class Unpair(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "UNPAIR"
            override val tag: kotlin.Int = 122
        }
    }

    public data class Left(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "LEFT"
            override val tag: kotlin.Int = 51
        }
    }

    public data class Right(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "RIGHT"
            override val tag: kotlin.Int = 68
        }
    }

    public data class IfLeft(
        public val ifBranch: MichelsonInstruction,
        public val elseBranch: MichelsonInstruction,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "IF_LEFT"
            override val tag: kotlin.Int = 46
        }
    }

    public data class Nil(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "NIL"
            override val tag: kotlin.Int = 61
        }
    }

    public object Cons : MichelsonInstruction, GrammarType {
        override val name: String = "CONS"
        override val tag: kotlin.Int = 27
    }

    public data class IfCons(
        public val ifBranch: MichelsonInstruction,
        public val elseBranch: MichelsonInstruction,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "IF_CONS"
            override val tag: kotlin.Int = 45
        }
    }

    public object Size : MichelsonInstruction, GrammarType {
        override val name: String = "SIZE"
        override val tag: kotlin.Int = 69
    }

    public data class EmptySet(public val type: MichelsonComparableType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "EMPTY_SET"
            override val tag: kotlin.Int = 36
        }
    }

    public data class EmptyMap(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "EMPTY_MAP"
            override val tag: kotlin.Int = 35
        }
    }

    public data class EmptyBigMap(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "EMPTY_BIG_MAP"
            override val tag: kotlin.Int = 114
        }
    }

    public data class Map(public val expression: MichelsonInstruction) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "MAP"
            override val tag: kotlin.Int = 56
        }
    }

    public data class Iter(public val expression: MichelsonInstruction) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "ITER"
            override val tag: kotlin.Int = 82
        }
    }

    public object Mem : MichelsonInstruction, GrammarType {
        override val name: String = "MEM"
        override val tag: kotlin.Int = 57
    }

    public data class Get(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "GET"
            override val tag: kotlin.Int = 41
        }
    }

    public data class Update(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : GrammarType {
            override val name: String = "UPDATE"
            override val tag: kotlin.Int = 80
        }
    }

    public object GetAndUpdate : MichelsonInstruction, GrammarType {
        override val name: String = "GET_AND_UPDATE"
        override val tag: kotlin.Int = 140
    }

    public data class If(
        public val ifBranch: MichelsonInstruction,
        public val elseBranch: MichelsonInstruction,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "IF"
            override val tag: kotlin.Int = 44
        }
    }

    public data class Loop(public val body: MichelsonInstruction) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "LOOP"
            override val tag: kotlin.Int = 52
        }
    }
    public data class LoopLeft(public val body: MichelsonInstruction) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "LOOP_LEFT"
            override val tag: kotlin.Int = 83
        }
    }

    public data class Lambda(
        public val parameterType: MichelsonType,
        public val returnType: MichelsonType,
        public val body: MichelsonInstruction,
    ) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "LAMBDA"
            override val tag: kotlin.Int = 49
        }
    }

    public object Exec : MichelsonInstruction, GrammarType {
        override val name: String = "EXEC"
        override val tag: kotlin.Int = 38
    }

    public object Apply : MichelsonInstruction, GrammarType {
        override val name: String = "APPLY"
        override val tag: kotlin.Int = 115
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
            override val tag: kotlin.Int = 31
        }
    }

    public object Failwith : MichelsonInstruction, GrammarType {
        override val name: String = "FAILWITH"
        override val tag: kotlin.Int = 39
    }

    public object Cast : MichelsonInstruction, GrammarType {
        override val name: String = "CAST"
        override val tag: kotlin.Int = 87
    }

    public object Rename : MichelsonInstruction, GrammarType {
        override val name: String = "RENAME"
        override val tag: kotlin.Int = 88
    }

    public object Concat : MichelsonInstruction, GrammarType {
        override val name: String = "CONCAT"
        override val tag: kotlin.Int = 26
    }

    public object Slice : MichelsonInstruction, GrammarType {
        override val name: String = "SLICE"
        override val tag: kotlin.Int = 111
    }

    public object Pack : MichelsonInstruction, GrammarType {
        override val name: String = "PACK"
        override val tag: kotlin.Int = 12
    }

    public data class Unpack(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "UNPACK"
            override val tag: kotlin.Int = 13
        }
    }

    public object Add : MichelsonInstruction, GrammarType {
        override val name: String = "ADD"
        override val tag: kotlin.Int = 18
    }

    public object Sub : MichelsonInstruction, GrammarType {
        override val name: String = "SUB"
        override val tag: kotlin.Int = 75
    }

    public object Mul : MichelsonInstruction, GrammarType {
        override val name: String = "MUL"
        override val tag: kotlin.Int = 58
    }

    public object Ediv : MichelsonInstruction, GrammarType {
        override val name: String = "EDIV"
        override val tag: kotlin.Int = 34
    }

    public object Abs : MichelsonInstruction, GrammarType {
        override val name: String = "ABS"
        override val tag: kotlin.Int = 17
    }

    public object Isnat : MichelsonInstruction, GrammarType {
        override val name: String = "ISNAT"
        override val tag: kotlin.Int = 86
    }

    public object Int : MichelsonInstruction, GrammarType {
        override val name: String = "INT"
        override val tag: kotlin.Int = 48
    }

    public object Neg : MichelsonInstruction, GrammarType {
        override val name: String = "NEG"
        override val tag: kotlin.Int = 59
    }

    public object Lsl : MichelsonInstruction, GrammarType {
        override val name: String = "LSL"
        override val tag: kotlin.Int = 53
    }

    public object Lsr : MichelsonInstruction, GrammarType {
        override val name: String = "LSR"
        override val tag: kotlin.Int = 54
    }

    public object Or : MichelsonInstruction, GrammarType {
        override val name: String = "OR"
        override val tag: kotlin.Int = 65
    }

    public object And : MichelsonInstruction, GrammarType {
        override val name: String = "AND"
        override val tag: kotlin.Int = 20
    }

    public object Xor : MichelsonInstruction, GrammarType {
        override val name: String = "XOR"
        override val tag: kotlin.Int = 81
    }

    public object Not : MichelsonInstruction, GrammarType {
        override val name: String = "NOT"
        override val tag: kotlin.Int = 63
    }

    public object Compare : MichelsonInstruction, GrammarType {
        override val name: String = "COMPARE"
        override val tag: kotlin.Int = 25
    }

    public object Eq : MichelsonInstruction, GrammarType {
        override val name: String = "EQ"
        override val tag: kotlin.Int = 37
    }

    public object Neq : MichelsonInstruction, GrammarType {
        override val name: String = "NEQ"
        override val tag: kotlin.Int = 60
    }

    public object Lt : MichelsonInstruction, GrammarType {
        override val name: String = "LT"
        override val tag: kotlin.Int = 55
    }

    public object Gt : MichelsonInstruction, GrammarType {
        override val name: String = "GT"
        override val tag: kotlin.Int = 42
    }

    public object Le : MichelsonInstruction, GrammarType {
        override val name: String = "LE"
        override val tag: kotlin.Int = 50
    }

    public object Ge : MichelsonInstruction, GrammarType {
        override val name: String = "GE"
        override val tag: kotlin.Int = 40
    }

    public object Self : MichelsonInstruction, GrammarType {
        override val name: String = "SELF"
        override val tag: kotlin.Int = 73
    }

    public object SelfAddress : MichelsonInstruction, GrammarType {
        override val name: String = "SELF_ADDRESS"
        override val tag: kotlin.Int = 119
    }

    public data class Contract(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "CONTRACT"
            override val tag: kotlin.Int = 85
        }
    }

    public object TransferTokens : MichelsonInstruction, GrammarType {
        override val name: String = "TRANSFER_TOKENS"
        override val tag: kotlin.Int = 77
    }

    public object SetDelegate : MichelsonInstruction, GrammarType {
        override val name: String = "SET_DELEGATE"
        override val tag: kotlin.Int = 78
    }

    public data class CreateContract(
        public val parameterType: MichelsonType,
        public val storageType: MichelsonType,
        public val code: MichelsonInstruction,
    ) :
        MichelsonInstruction {
        public companion object : GrammarType {
            override val name: String = "CREATE_CONTRACT"
            override val tag: kotlin.Int = 29
        }
    }

    public object ImplicitAccount : MichelsonInstruction, GrammarType {
        override val name: String = "IMPLICIT_ACCOUNT"
        override val tag: kotlin.Int = 30
    }

    public object VotingPower : MichelsonInstruction, GrammarType {
        override val name: String = "VOTING_POWER"
        override val tag: kotlin.Int = 123
    }

    public object Now : MichelsonInstruction, GrammarType {
        override val name: String = "NOW"
        override val tag: kotlin.Int = 64
    }

    public object Level : MichelsonInstruction, GrammarType {
        override val name: String = "LEVEL"
        override val tag: kotlin.Int = 118
    }

    public object Amount : MichelsonInstruction, GrammarType {
        override val name: String = "AMOUNT"
        override val tag: kotlin.Int = 19
    }

    public object Balance : MichelsonInstruction, GrammarType {
        override val name: String = "BALANCE"
        override val tag: kotlin.Int = 21
    }

    public object CheckSignature : MichelsonInstruction, GrammarType {
        override val name: String = "CHECK_SIGNATURE"
        override val tag: kotlin.Int = 24
    }

    public object Blake2B : MichelsonInstruction, GrammarType {
        override val name: String = "BLAKE2B"
        override val tag: kotlin.Int = 14
    }

    public object Keccak : MichelsonInstruction, GrammarType {
        override val name: String = "KECCAK"
        override val tag: kotlin.Int = 125
    }

    public object Sha3 : MichelsonInstruction, GrammarType {
        override val name: String = "SHA3"
        override val tag: kotlin.Int = 126
    }

    public object Sha256 : MichelsonInstruction, GrammarType {
        override val name: String = "SHA256"
        override val tag: kotlin.Int = 15
    }

    public object Sha512 : MichelsonInstruction, GrammarType {
        override val name: String = "SHA512"
        override val tag: kotlin.Int = 16
    }

    public object HashKey : MichelsonInstruction, GrammarType {
        override val name: String = "HASH_KEY"
        override val tag: kotlin.Int = 43
    }

    public object Source : MichelsonInstruction, GrammarType {
        override val name: String = "SOURCE"
        override val tag: kotlin.Int = 71
    }

    public object Sender : MichelsonInstruction, GrammarType {
        override val name: String = "SENDER"
        override val tag: kotlin.Int = 72
    }

    public object Address : MichelsonInstruction, GrammarType {
        override val name: String = "ADDRESS"
        override val tag: kotlin.Int = 84
    }

    public object ChainId : MichelsonInstruction, GrammarType {
        override val name: String = "CHAIN_ID"
        override val tag: kotlin.Int = 117
    }

    public object TotalVotingPower : MichelsonInstruction, GrammarType {
        override val name: String = "TOTAL_VOTING_POWER"
        override val tag: kotlin.Int = 124
    }

    public object PairingCheck : MichelsonInstruction, GrammarType {
        override val name: String = "PAIRING_CHECK"
        override val tag: kotlin.Int = 127
    }

    public data class SaplingEmptyState(public val memoSize: MichelsonData.NaturalNumberConstant) : MichelsonInstruction {

        public constructor(memoSize: UByte) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UShort) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UInt) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: ULong) : this(MichelsonData.NaturalNumberConstant(memoSize))

        public companion object : GrammarType {
            override val name: String = "SAPLING_EMPTY_STATE"
            override val tag: kotlin.Int = 133
        }
    }

    public object SaplingVerifyUpdate : MichelsonInstruction, GrammarType {
        override val name: String = "SAPLING_VERIFY_UPDATE"
        override val tag: kotlin.Int = 134
    }

    public object Ticket : MichelsonInstruction, GrammarType {
        override val name: String = "TICKET"
        override val tag: kotlin.Int = 136
    }

    public object ReadTicket : MichelsonInstruction, GrammarType {
        override val name: String = "READ_TICKET"
        override val tag: kotlin.Int = 137
    }

    public object SplitTicket : MichelsonInstruction, GrammarType {
        override val name: String = "SPLIT_TICKET"
        override val tag: kotlin.Int = 138
    }

    public object JoinTickets : MichelsonInstruction, GrammarType {
        override val name: String = "JOIN_TICKETS"
        override val tag: kotlin.Int = 139
    }

    public object OpenChest : MichelsonInstruction, GrammarType {
        override val name: String = "OPEN_CHEST"
        override val tag: kotlin.Int = 143
    }

    public sealed interface GrammarType : MichelsonData.GrammarType {
        public companion object {}
    }

    public companion object {
        public fun Sequence(vararg instructions: MichelsonInstruction): Sequence = Sequence(instructions.toList())
    }
}