package it.airgap.tezos.michelson.micheline

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.asHexString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.isHex
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.toHexString
import it.airgap.tezos.michelson.internal.serializer.MichelineLiteralBytesSerializer
import it.airgap.tezos.michelson.internal.serializer.MichelineLiteralSerializer
import kotlinx.serialization.Serializable

/**
 * Micheline literals as defined in [the documentation](https://tezos.gitlab.io/shell/micheline.html#bnf-grammar).
 */
@Serializable(with = MichelineLiteralSerializer::class)
public sealed class MichelineLiteral : MichelineNode() {

    @Serializable
    public data class Integer(public val int: kotlin.String) : MichelineLiteral() {

        public constructor(value: Byte) : this(value.toString())
        public constructor(value: Short) : this(value.toString())
        public constructor(value: Int) : this(value.toString())
        public constructor(value: Long) : this(value.toString())

        init {
            require(isValid(int)) { "Invalid Micheline Integer." }
        }

        public fun toByte(): Byte = int.toByte()
        public fun toShort(): Short = int.toShort()
        public fun toInt(): Int = int.toInt()
        public fun toLong(): Long = int.toLong()

        public companion object {
            public fun isValid(value: kotlin.String): Boolean = value.matches(Regex("^-?[0-9]+$"))
        }
    }

    @Serializable
    public data class String(public val string: kotlin.String) : MichelineLiteral() {
        init {
            require(isValid(string)) { "Invalid Micheline String." }
        }

        public companion object {
            public fun isValid(value: kotlin.String): Boolean = value.matches(Regex("^(\"|\r|\n|\t|\b|\\\\|[^\"\\\\])*$"))
        }
    }

    @Serializable(with = MichelineLiteralBytesSerializer::class)
    public data class Bytes(public val bytes: kotlin.String) : MichelineLiteral() {

        public constructor(value: ByteArray) : this(value.toHexString().asString(withPrefix = true))

        init {
            require(isValid(bytes)) { "Invalid Micheline Bytes." }
        }

        public fun toByteArray(): ByteArray = if (bytes == HexString.PREFIX) byteArrayOf() else bytes.asHexString().toByteArray()

        public companion object {
            public fun isValid(value: kotlin.String): Boolean = (value.isHex() && value.startsWith(HexString.PREFIX)) || value == HexString.PREFIX
        }
    }

    public companion object {}
}