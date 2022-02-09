package it.airgap.tezos.operation.internal.signer

import it.airgap.tezos.core.decodeFromBytes
import it.airgap.tezos.core.encodeToBytes
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.signer.Signer
import it.airgap.tezos.core.type.Watermark
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.forgeToBytes
import it.airgap.tezos.operation.internal.coder.OperationBytesCoder

@InternalTezosSdkApi
public class OperationSigner(
    private val operationEd25519Signer: OperationEd25519Signer,
    private val operationSecp256K1Signer: OperationSecp256K1Signer,
    private val operationP256Signer: OperationP256Signer,
) : Signer<Operation, SecretKeyEncoded<*>, PublicKeyEncoded<*>, SignatureEncoded<*>> {

    override fun sign(message: Operation, key: SecretKeyEncoded<*>): SignatureEncoded<*> =
        when (key) {
            is Ed25519SecretKey -> operationEd25519Signer.sign(message, key)
            is Secp256K1SecretKey -> operationSecp256K1Signer.sign(message, key)
            is P256SecretKey -> operationP256Signer.sign(message, key)
        }

    override fun verify(message: Operation, signature: SignatureEncoded<*>, key: PublicKeyEncoded<*>): Boolean =
        when {
            signature is Ed25519Signature && key is Ed25519PublicKey -> operationEd25519Signer.verify(message, signature, key)
            signature is Secp256K1Signature && key is Secp256K1PublicKey -> operationSecp256K1Signer.verify(message, signature, key)
            signature is P256Signature && key is P256PublicKey -> operationP256Signer.verify(message, signature, key)
            else -> false
        }
}

@InternalTezosSdkApi
public class OperationEd25519Signer(
    crypto: Crypto,
    operationBytesCoder: OperationBytesCoder,
    private val encodedBytesCoder: EncodedBytesCoder,
) : Signer<Operation, Ed25519SecretKey, Ed25519PublicKey, Ed25519Signature> {
    private val operationRawSigner: OperationRawSigner = OperationRawSigner(crypto, operationBytesCoder)

    override fun sign(message: Operation, key: Ed25519SecretKey): Ed25519Signature {
        val signature = operationRawSigner.sign(message, key.encodeToBytes(encodedBytesCoder), Crypto::signEd25519)
        return Ed25519Signature.decodeFromBytes(signature, encodedBytesCoder)
    }

    override fun verify(message: Operation, signature: Ed25519Signature, key: Ed25519PublicKey): Boolean =
        operationRawSigner.verify(message, signature.encodeToBytes(encodedBytesCoder), key.encodeToBytes(encodedBytesCoder), Crypto::verifyEd25519)
}

@InternalTezosSdkApi
public class OperationSecp256K1Signer(
    crypto: Crypto,
    operationBytesCoder: OperationBytesCoder,
    private val encodedBytesCoder: EncodedBytesCoder,
): Signer<Operation, Secp256K1SecretKey, Secp256K1PublicKey, Secp256K1Signature> {
    private val operationRawSigner: OperationRawSigner = OperationRawSigner(crypto, operationBytesCoder)

    override fun sign(message: Operation, key: Secp256K1SecretKey): Secp256K1Signature {
        val signature = operationRawSigner.sign(message, key.encodeToBytes(encodedBytesCoder), Crypto::signSecp256K1)
        return Secp256K1Signature.decodeFromBytes(signature, encodedBytesCoder)
    }

    override fun verify(message: Operation, signature: Secp256K1Signature, key: Secp256K1PublicKey): Boolean =
        operationRawSigner.verify(message, signature.encodeToBytes(encodedBytesCoder), key.encodeToBytes(encodedBytesCoder), Crypto::verifySecp256K1)
}

@InternalTezosSdkApi
public class OperationP256Signer(
    crypto: Crypto,
    operationBytesCoder: OperationBytesCoder,
    private val encodedBytesCoder: EncodedBytesCoder,
) : Signer<Operation, P256SecretKey, P256PublicKey, P256Signature> {
    private val operationRawSigner: OperationRawSigner = OperationRawSigner(crypto, operationBytesCoder)

    override fun sign(message: Operation, key: P256SecretKey): P256Signature {
        val signature = operationRawSigner.sign(message, key.encodeToBytes(encodedBytesCoder), Crypto::signP256)
        return P256Signature.decodeFromBytes(signature, encodedBytesCoder)
    }

    override fun verify(message: Operation, signature: P256Signature, key: P256PublicKey): Boolean =
        operationRawSigner.verify(message, signature.encodeToBytes(encodedBytesCoder), key.encodeToBytes(encodedBytesCoder), Crypto::verifyP256)
}

private class OperationRawSigner(
    private val crypto: Crypto,
    private val operationBytesCoder: OperationBytesCoder,
) {
    fun sign(message: Operation, key: ByteArray, signer: Crypto.(ByteArray, ByteArray) -> ByteArray): ByteArray =
        crypto.signer(hash(message), key)

    fun verify(message: Operation, signature: ByteArray, key: ByteArray, verifier: Crypto.(ByteArray, ByteArray, ByteArray) -> Boolean): Boolean =
        crypto.verifier(hash(message), signature, key)

    private fun hash(operation: Operation): ByteArray =
        crypto.hash(Watermark.GenericOperation + operation.forgeToBytes(operationBytesCoder), 32)
}