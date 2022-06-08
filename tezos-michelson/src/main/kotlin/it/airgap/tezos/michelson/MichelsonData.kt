package it.airgap.tezos.michelson

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.asHexString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.isHex
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.toHexString

/**
 * Tezos Michelson data types as defined in [the documentation](https://tezos.gitlab.io/active/michelson.html#full-grammar).
 *
 * See also: [Michelson Reference](https://tezos.gitlab.io/michelson-reference/).
 */
public sealed interface MichelsonData : Michelson {
    @JvmInline public value class IntConstant(public val value: String) : MichelsonData {

        public constructor(value: Byte) : this(value.toString())
        public constructor(value: Short) : this(value.toString())
        public constructor(value: Int) : this(value.toString())
        public constructor(value: Long) : this(value.toString())

        init {
            require(isValid(value)) { "Invalid Int constant." }
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
            require(isValid(value)) { "Invalid natural number constant." }
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
            require(isValid(value)) { "Invalid String constant." }
        }

        public companion object {
            public fun isValid(value: String): Boolean = value.matches(Regex("^(\"|\r|\n|\t|\b|\\\\|[^\"\\\\])*$"))
        }
    }

    @JvmInline public value class ByteSequenceConstant(public val value: String) : MichelsonData {

        public constructor(value: ByteArray) : this(value.toHexString().asString(withPrefix = true))

        init {
            require(isValid(value)) { "Invalid Byte sequence." }
        }

        public fun toByteArray(): ByteArray = value.asHexString().toByteArray()

        public companion object {
            public fun isValid(value: String): Boolean = value.isHex() && value.startsWith(HexString.PREFIX)
        }
    }

    public object Unit : MichelsonData, Prim {
        override val name: String = "Unit"
        override val tag: ByteArray = byteArrayOf(11)
    }

    public object True : MichelsonData, Prim {
        override val name: String = "True"
        override val tag: ByteArray = byteArrayOf(10)
    }

    public object False : MichelsonData, Prim {
        override val name: String = "False"
        override val tag: ByteArray = byteArrayOf(3)
    }

    public data class Pair(public val values: List<MichelsonData>) : MichelsonData {
        public constructor(vararg values: MichelsonData) : this(values.toList())

        init {
            require(values.size >= 2)
        }

        public companion object : Prim {
            override val name: String = "Pair"
            override val tag: ByteArray = byteArrayOf(7)
        }
    }

    public data class Left(public val value: MichelsonData) : MichelsonData {
        public companion object : Prim {
            override val name: String = "Left"
            override val tag: ByteArray = byteArrayOf(5)
        }
    }

    public data class Right(public val value: MichelsonData) : MichelsonData {
        public companion object : Prim {
            override val name: String = "Right"
            override val tag: ByteArray = byteArrayOf(8)
        }
    }

    public data class Some(public val value: MichelsonData) : MichelsonData {
        public companion object : Prim {
            override val name: String = "Some"
            override val tag: ByteArray = byteArrayOf(9)
        }
    }

    public object None : MichelsonData, Prim {
        override val name: String = "None"
        override val tag: ByteArray = byteArrayOf(6)
    }

    public data class Sequence(public val values: List<MichelsonData>) : MichelsonData {
        public constructor(vararg values: MichelsonData) : this(values.toList())

        public companion object {}
    }

    public data class Map(public val values: List<Elt>) : MichelsonData {
        public constructor(vararg values: Elt) : this(values.toList())

        public companion object {}
    }

    public data class Elt(public val key: MichelsonData, public val value: MichelsonData) {
        public companion object : Prim {
            override val name: String = "Elt"
            override val tag: ByteArray = byteArrayOf(4)
        }
    }

    public sealed interface Prim : Michelson.Prim {
        public companion object {
            public val values: List<Prim>
                get() = listOf(
                    Unit,
                    True,
                    False,
                    Pair,
                    Left,
                    Right,
                    Some,
                    None,
                    Elt,
                ) + MichelsonInstruction.Prim.values
        }
    }

    public companion object {}
}