package it.airgap.tezos.core.internal.base58

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.utils.splitAt

@InternalTezosSdkApi
public class Base58Check(private val base58: Base58, private val crypto: Crypto) {

    public fun encode(bytes: ByteArray): String {
        val checksum = createChecksum(bytes)

        return base58.encode(bytes + checksum)
    }

    public fun decode(base58check: String): ByteArray {
        val (payload, checksum) = base58.decode(base58check).splitAt { it.size - 4 }
        val newChecksum = createChecksum(payload)
        if (!checksum.contentEquals(newChecksum)) failWithInvalidChecksum()

        return payload
    }

    private fun createChecksum(bytes: ByteArray): ByteArray {
        val hash = crypto.hashSha256(crypto.hashSha256(bytes))

        return hash.sliceArray(0 until 4)
    }

    private fun failWithInvalidChecksum(): Nothing = throw IllegalArgumentException("Base58Check checksum is invalid")
}