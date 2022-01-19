package it.airgap.tezos.core.crypto

public interface CryptoProvider {
    @Throws(Exception::class)
    public fun hash256(message: ByteArray): ByteArray
}