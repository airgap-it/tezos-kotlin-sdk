package it.airgap.tezos.core.internal.crypto

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.type.HexString

@InternalTezosSdkApi
public class Crypto(private val provider: CryptoProvider)  {
    public fun hashSha256(message: HexString): ByteArray = hashSha256(message.toByteArray())
    public fun hashSha256(message: ByteArray): ByteArray = provider.sha256(message)

    public fun hash(message: HexString, size: Int): ByteArray = hash(message.toByteArray(), size)
    public fun hash(message: ByteArray, size: Int): ByteArray = provider.blake2b(message, size)

    public fun signEd25519(message: HexString, secretKey: HexString): ByteArray = signEd25519(message.toByteArray(), secretKey.toByteArray())
    public fun signEd25519(message: ByteArray, secretKey: ByteArray): ByteArray = provider.signEd25519(message, secretKey)

    public fun verifyEd25519(message: HexString, signature: HexString, publicKey: HexString): Boolean = verifyEd25519(message.toByteArray(), signature.toByteArray(), publicKey.toByteArray())
    public fun verifyEd25519(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean = provider.verifyEd25519(message, signature, publicKey)

    public fun signSecp256K1(message: HexString, secretKey: HexString): ByteArray = signSecp256K1(message.toByteArray(), secretKey.toByteArray())
    public fun signSecp256K1(message: ByteArray, secretKey: ByteArray): ByteArray = provider.signSecp256K1(message, secretKey)

    public fun verifySecp256K1(message: HexString, signature: HexString, publicKey: HexString): Boolean = verifySecp256K1(message.toByteArray(), signature.toByteArray(), publicKey.toByteArray())
    public fun verifySecp256K1(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean = provider.verifySecp256K1(message, signature, publicKey)

    public fun signP256(message: HexString, secretKey: HexString): ByteArray = signP256(message.toByteArray(), secretKey.toByteArray())
    public fun signP256(message: ByteArray, secretKey: ByteArray): ByteArray = provider.signP256(message, secretKey)

    public fun verifyP256(message: HexString, signature: HexString, publicKey: HexString): Boolean = verifyP256(message.toByteArray(), signature.toByteArray(), publicKey.toByteArray())
    public fun verifyP256(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean = provider.verifyP256(message, signature, publicKey)
}