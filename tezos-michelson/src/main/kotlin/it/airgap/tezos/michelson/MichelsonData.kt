package it.airgap.tezos.michelson

import it.airgap.tezos.core.internal.type.HexString
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.isHex
import it.airgap.tezos.core.internal.utils.toHexString

// https://tezos.gitlab.io/active/michelson.html#full-grammar
public sealed interface MichelsonData : Michelson {
    @JvmInline public value class IntConstant(public val value: String) : MichelsonData {

        public constructor(value: Byte) : this(value.toString())
        public constructor(value: Short) : this(value.toString())
        public constructor(value: Int) : this(value.toString())
        public constructor(value: Long) : this(value.toString())

        init {
            require(isValid(value))
        }

        public fun toByte(): Byte = value.toByte()
        public fun toShort(): Short = value.toShort()
        public fun toInt(): Int = value.toInt()
        public fun toLong(): Long = value.toLong()

        public companion object {
            public fun isValid(value: String): Boolean = value.matches(Regex("^-?[0-9]+$"))
        }
    }

    @JvmInline
    public value class NaturalNumberConstant(public val value: String) : MichelsonData {

        public constructor(value: UByte) : this(value.toString())
        public constructor(value: UShort) : this(value.toString())
        public constructor(value: UInt) : this(value.toString())
        public constructor(value: ULong) : this(value.toString())

        init {
            require(isValid(value))
        }

        public fun toUByte(): UByte = value.toUByte()
        public fun toUShort(): UShort = value.toUShort()
        public fun toUInt(): UInt = value.toUInt()
        public fun toULong(): ULong = value.toULong()

        public companion object {
            public fun isValid(value: String): Boolean = value.matches(Regex("^[0-9]+$"))
        }
    }

    @JvmInline public value class StringConstant(public val value: String) : MichelsonData {
        init {
            require(isValid(value))
        }

        public companion object {
            public fun isValid(value: String): Boolean = value.matches(Regex("^(\"|\r|\n|\t|\b|\\\\|[^\"\\\\])*$"))
        }
    }

    @JvmInline public value class ByteSequenceConstant(public val value: String) : MichelsonData {

        public constructor(value: ByteArray) : this(value.toHexString().asString(withPrefix = true))

        init {
            require(isValid(value))
        }

        public fun toByteArray(): ByteArray = value.asHexString().toByteArray()

        public companion object {
            public fun isValid(value: String): Boolean = value.isHex() && value.startsWith(HexString.PREFIX)
        }
    }

    public object Unit : MichelsonData, GrammarType {
        override val name: String = "Unit"
    }

    public object True : MichelsonData, GrammarType {
        override val name: String = "True"
    }
    public object False : MichelsonData, GrammarType {
        override val name: String = "False"
    }

    public data class Pair(public val values: List<MichelsonData>) : MichelsonData {
        init {
            require(values.size >= 2)
        }

        public companion object : GrammarType {
            override val name: String = "Pair"
        }
    }

    public data class Left(public val value: MichelsonData) : MichelsonData {
        public companion object : GrammarType {
            override val name: String = "Left"
        }
    }

    public data class Right(public val value: MichelsonData) : MichelsonData {
        public companion object : GrammarType {
            override val name: String = "Right"
        }
    }

    public data class Some(public val value: MichelsonData) : MichelsonData {
        public companion object : GrammarType {
            override val name: String = "Some"
        }
    }

    public object None : MichelsonData, GrammarType {
        override val name: String = "None"
    }

    public data class Sequence(public val values: List<MichelsonData>) : MichelsonData {
        public companion object {}
    }

    public data class EltSequence(public val values: List<Elt>) : MichelsonData {
        public companion object {}
    }

    public data class Elt(public val key: MichelsonData, public val value: MichelsonData) {
        public companion object : GrammarType {
            override val name: String = "Elt"
        }
    }

    public sealed interface GrammarType : Michelson.GrammarType {
        public companion object {}
    }

    public companion object {
        public fun Pair(vararg values: MichelsonData): Pair = Pair(values.toList())
        public fun Sequence(vararg values: MichelsonData): Sequence = Sequence(values.toList())
        public fun EltSequence(vararg values: Elt): EltSequence = EltSequence(values.toList())
    }
}