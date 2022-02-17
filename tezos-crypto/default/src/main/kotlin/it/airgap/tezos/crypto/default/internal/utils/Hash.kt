package it.airgap.tezos.crypto.default.internal.utils

import org.bouncycastle.crypto.digests.Blake2bDigest
import org.bouncycastle.crypto.digests.SHA256Digest

internal fun SHA256Digest.hash(message: ByteArray): ByteArray {
    update(message, 0, message.size)

    return ByteArray(digestSize).also {
        doFinal(it, 0)
    }
}

internal fun Blake2bDigest.hash(message: ByteArray, size: Int): ByteArray {
    update(message, 0, message.size)

    return ByteArray(digestSize).also {
        doFinal(it, 0)
    }.sliceArray(0 until size)
}