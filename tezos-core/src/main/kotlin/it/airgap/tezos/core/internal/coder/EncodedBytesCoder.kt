package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.utils.consumeUntil
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.Encoded

@InternalTezosSdkApi
public class EncodedBytesCoder(private val base58Check: Base58Check): ConsumingBytesCoder<Encoded<*>> {

    override fun encode(value: Encoded<*>): ByteArray = encode(value, keepPrefix = false)
    public fun encode(value: Encoded<*>, keepPrefix: Boolean): ByteArray {
        val decoded = base58Check.decode(value.base58)
        val bytesStart = if (keepPrefix) 0 else value.kind.base58Bytes.size

        return decoded.sliceArray( bytesStart until decoded.size)
    }

    override fun decode(value: ByteArray): Encoded<*> {
        val kind = Encoded.Kind.recognize(value) ?: failWithInvalidBytes(value)
        return decode(value.sliceArray(kind.base58Bytes.size until value.size), kind)
    }

    public fun <E : Encoded<*>, K : Encoded.Kind<E>> decode(value: ByteArray, kind: K): E {
        val encoded = base58Check.encode(kind.base58Bytes + value)
        return kind.createValueOrNull(encoded) ?: failWithInvalidBytes(value, kind)
    }

    override fun decodeConsuming(value: MutableList<Byte>): Encoded<*> {
        val kind = Encoded.Kind.recognizeConsuming(value) ?: failWithInvalidBytes(value)
        return decodeConsuming(value, kind)
    }

    public fun <E : Encoded<*>, K : Encoded.Kind<E>> decodeConsuming(value: MutableList<Byte>, kind: K): E {
        val bytes = value.consumeUntil(kind.bytesLength)
        return decode(bytes.toByteArray(), kind)
    }

    private fun failWithInvalidBytes(bytes: ByteArray, kind: Encoded.Kind<*>? = null): Nothing = failWithInvalidBytes(bytes.joinToString(prefix = "[", postfix = "]"), kind)
    private fun failWithInvalidBytes(bytes: List<Byte>, kind: Encoded.Kind<*>? = null): Nothing = failWithInvalidBytes(bytes.joinToString(prefix = "[", postfix = "]"), kind)
    private fun failWithInvalidBytes(bytes: String, kind: Encoded.Kind<*>? = null): Nothing {
        val type = kind?.let { kind::class.qualifiedName?.removeSuffix(".Companion")?.substringAfterLast(".") } ?: "Base58 encoded"
        failWithIllegalArgument("Bytes $bytes are not valid $type data.")
    }

    private fun Encoded.Kind.Companion.recognizeConsuming(bytes: MutableList<Byte>): Encoded.Kind<*>? =
        recognize(bytes)?.also { bytes.consumeUntil(it.base58Bytes.size) }
}
