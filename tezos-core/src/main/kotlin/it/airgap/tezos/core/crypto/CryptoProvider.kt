package it.airgap.tezos.core.crypto

/**
 * External Tezos cryptography methods provider.
 *
 * Use this interface to register a custom cryptography implementation in [it.airgap.tezos.core.Tezos].
 * See:
 *  - `tezos-crypto` for ready-to-use implementations
 *  - `samples/src/test/kotlin/Basic/BasicSamples#configuration` to learn how to register a custom provider.
 */
public interface CryptoProvider {

    /**
     * Hashes the [message] using the SHA-256 hash function.
     */
    @Throws(Exception::class)
    public fun sha256(message: ByteArray): ByteArray

    /**
     * Hashes the [message] using the BLAKE2b hash function.
     */
    @Throws(Exception::class)
    public fun blake2b(message: ByteArray, size: Int): ByteArray

    /**
     * Signs the [message] with the EdDSA [secretKey].
     */
    @Throws(Exception::class)
    public fun signEd25519(message: ByteArray, secretKey: ByteArray): ByteArray

    /**
     * Verifies that the EdDSA [signature] is a valid signature for the [message] using the signer's [publicKey].
     */
    @Throws(Exception::class)
    public fun verifyEd25519(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean

    /**
     * Signs the [message] with the secp256k1 [secretKey].
     */
    @Throws(Exception::class)
    public fun signSecp256K1(message: ByteArray, secretKey: ByteArray): ByteArray

    /**
     * Verifies that the secp256k1 [signature] is a valid signature for the [message] using the signer's [publicKey].
     */
    @Throws(Exception::class)
    public fun verifySecp256K1(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean

    /**
     * Signs the [message] with the P-256 [secretKey].
     */
    @Throws(Exception::class)
    public fun signP256(message: ByteArray, secretKey: ByteArray): ByteArray

    /**
     * Verifies that the P-256 [signature] is a valid signature for the [message] using the signer's [publicKey].
     */
    @Throws(Exception::class)
    public fun verifyP256(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean
}