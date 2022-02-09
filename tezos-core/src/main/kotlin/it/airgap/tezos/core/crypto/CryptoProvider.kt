package it.airgap.tezos.core.crypto

public interface CryptoProvider {
    @Throws(Exception::class)
    public fun hash256(message: ByteArray): ByteArray

    @Throws(Exception::class)
    public fun hash(message: ByteArray, size: Int): ByteArray

    @Throws(Exception::class)
    public fun signEd25519(message: ByteArray, secretKey: ByteArray): ByteArray

    @Throws(Exception::class)
    public fun verifyEd25519(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean

    @Throws(Exception::class)
    public fun signSecp256K1(message: ByteArray, secretKey: ByteArray): ByteArray

    @Throws(Exception::class)
    public fun verifySecp256K1(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean

    @Throws(Exception::class)
    public fun signP256(message: ByteArray, secretKey: ByteArray): ByteArray

    @Throws(Exception::class)
    public fun verifyP256(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean
}