package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.utils.consumeUntil
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.MetaEncoded

@InternalTezosSdkApi
public class EncodedBytesCoder(private val base58Check: Base58Check): ConsumingBytesCoder<MetaEncoded<*>> {

    override fun encode(value: MetaEncoded<*>): ByteArray = encode(value, keepPrefix = false)
    public fun encode(value: MetaEncoded<*>, keepPrefix: Boolean): ByteArray {
        val decoded = base58Check.decode(value.encoded.base58)
        val bytesStart = if (keepPrefix) 0 else value.kind.base58Bytes.size

        return decoded.sliceArray( bytesStart until decoded.size)
    }

    override fun decode(value: ByteArray): MetaEncoded<*> {
        val kind = MetaEncoded.Kind.recognize(value) ?: failWithInvalidBytes(value)
        return decode(value.sliceArray(kind.base58Bytes.size until value.size), kind)
    }

    public fun <E : MetaEncoded<*>, K : MetaEncoded.Kind<E>> decode(value: ByteArray, kind: K): E {
        val encoded = base58Check.encode(kind.base58Bytes + value)
        return kind.createValueOrNull(encoded) ?: failWithInvalidBytes(value, kind)
    }

    override fun decodeConsuming(value: MutableList<Byte>): MetaEncoded<*> {
        val kind = MetaEncoded.Kind.recognizeConsuming(value) ?: failWithInvalidBytes(value)
        return decodeConsuming(value, kind)
    }

    public fun <E : MetaEncoded<*>, K : MetaEncoded.Kind<E>> decodeConsuming(value: MutableList<Byte>, kind: K): E {
        val bytes = value.consumeUntil(kind.bytesLength)
        return decode(bytes.toByteArray(), kind)
    }

    private fun failWithInvalidBytes(bytes: ByteArray, kind: MetaEncoded.Kind<*>? = null): Nothing = failWithInvalidBytes(bytes.joinToString(prefix = "[", postfix = "]"), kind)
    private fun failWithInvalidBytes(bytes: List<Byte>, kind: MetaEncoded.Kind<*>? = null): Nothing = failWithInvalidBytes(bytes.joinToString(prefix = "[", postfix = "]"), kind)
    private fun failWithInvalidBytes(bytes: String, kind: MetaEncoded.Kind<*>? = null): Nothing {
        val type = kind?.let { kind::class.qualifiedName?.removeSuffix(".Companion")?.substringAfterLast(".") } ?: "Base58 encoded"
        failWithIllegalArgument("Bytes $bytes are not valid $type data.")
    }

    private fun MetaEncoded.Kind.Companion.recognizeConsuming(bytes: MutableList<Byte>): MetaEncoded.Kind<*>? =
        recognize(bytes)?.also { bytes.consumeUntil(it.base58Bytes.size) }
}
