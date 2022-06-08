package it.airgap.tezos.core.internal.coder.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.coder.ConfigurableConsumingBytesCoder
import it.airgap.tezos.core.internal.context.TezosCoreContext.consumeUntil
import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.Encoded
import it.airgap.tezos.core.type.encoded.MetaEncoded

@InternalTezosSdkApi
public class EncodedBytesCoder internal constructor(private val base58Check: Base58Check)
    : ConfigurableConsumingBytesCoder<Encoded, EncodedBytesCoder.EncoderConfiguration, Unit> {

    public fun encode(value: Encoded): ByteArray = encode(value, EncoderConfiguration(keepPrefix = false))
    override fun encode(value: Encoded, configuration: EncoderConfiguration): ByteArray =
        with(configuration) {
            val decoded = base58Check.decode(value.base58)
            val bytesStart = if (keepPrefix) 0 else value.meta.kind.base58Bytes.size

            return decoded.sliceArray( bytesStart until decoded.size)
        }

    public fun decode(value: ByteArray): Encoded {
        val kind = MetaEncoded.Kind.recognize(value) ?: failWithInvalidBytes(value)
        return decode(value.sliceArray(kind.base58Bytes.size until value.size), kind)
    }

    public fun <E : Encoded, M : MetaEncoded<*, E>, K : MetaEncoded.Kind<M, E>> decode(value: ByteArray, kind: K): E {
        val encoded = base58Check.encode(kind.base58Bytes + value)
        return kind.createValueOrNull(encoded)?.encoded ?: failWithInvalidBytes(value, kind)
    }

    public fun decodeConsuming(value: MutableList<Byte>): Encoded {
        val kind = MetaEncoded.Kind.recognizeConsuming(value) ?: failWithInvalidBytes(value)
        return decodeConsuming(value, kind)
    }

    public fun <E : Encoded, M : MetaEncoded<*, E>, K : MetaEncoded.Kind<M, E>> decodeConsuming(value: MutableList<Byte>, kind: K): E {
        val bytes = value.consumeUntil(kind.bytesLength)
        return decode(bytes.toByteArray(), kind)
    }

    override fun decode(value: ByteArray, configuration: Unit): Encoded = decode(value)
    override fun decodeConsuming(value: MutableList<Byte>, configuration: Unit): Encoded = decodeConsuming(value)

    private fun failWithInvalidBytes(bytes: ByteArray, kind: MetaEncoded.Kind<*, *>? = null): Nothing = failWithInvalidBytes(bytes.joinToString(prefix = "[", postfix = "]"), kind)
    private fun failWithInvalidBytes(bytes: List<Byte>, kind: MetaEncoded.Kind<*, *>? = null): Nothing = failWithInvalidBytes(bytes.joinToString(prefix = "[", postfix = "]"), kind)
    private fun failWithInvalidBytes(bytes: String, kind: MetaEncoded.Kind<*, *>? = null): Nothing {
        val type = kind?.let { kind::class.qualifiedName?.removeSuffix(".Companion")?.substringAfterLast(".") } ?: "Base58 encoded"
        failWithIllegalArgument("Bytes $bytes are not valid $type data.")
    }

    private fun MetaEncoded.Kind.Companion.recognizeConsuming(bytes: MutableList<Byte>): MetaEncoded.Kind<*, *>? =
        recognize(bytes)?.also { bytes.consumeUntil(it.base58Bytes.size) }

    public data class EncoderConfiguration(public val keepPrefix: Boolean)
}
