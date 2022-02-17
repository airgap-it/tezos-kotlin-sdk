package it.airgap.tezos.crypto.default.internal.utils

import org.bouncycastle.crypto.signers.Ed25519Signer

internal fun Ed25519Signer.generateSignature(message: ByteArray): ByteArray {
    update(message, 0, message.size)
    return generateSignature()
}

internal fun Ed25519Signer.verifySignature(message: ByteArray, signature: ByteArray): Boolean {
    update(message, 0, message.size)
    return verifySignature(signature)
}