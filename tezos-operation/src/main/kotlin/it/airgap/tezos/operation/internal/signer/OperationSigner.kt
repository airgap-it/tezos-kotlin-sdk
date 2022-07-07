package it.airgap.tezos.operation.internal.signer

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.signer.Signer
import it.airgap.tezos.core.type.Watermark
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.internal.context.TezosOperationContext.decodeFromBytes
import it.airgap.tezos.operation.internal.context.TezosOperationContext.encodeToBytes
import it.airgap.tezos.operation.internal.context.TezosOperationContext.forgeToBytes
import it.airgap.tezos.operation.internal.context.TezosOperationContext.toEd25519Signature
import it.airgap.tezos.operation.internal.context.TezosOperationContext.toP256Signature
import it.airgap.tezos.operation.internal.context.TezosOperationContext.toSecp256K1Signature

internal class OperationSigner(
    private val operationEd25519Signer: Signer<Operation, Ed25519SecretKey, Ed25519PublicKey, Ed25519Signature>,
    private val operationSecp256K1Signer: Signer<Operation, Secp256K1SecretKey, Secp256K1PublicKey, Secp256K1Signature>,
    private val operationP256Signer: Signer<Operation, P256SecretKey, P256PublicKey, P256Signature>,
    private val genericSignatureToEd25519SignatureConverter: Converter<GenericSignature, Ed25519Signature>,
    private val genericSignatureToSecp256K1SignatureConverter: Converter<GenericSignature, Secp256K1Signature>,
    private val genericSignatureToP256SignatureConverter: Converter<GenericSignature, P256Signature>,
) : Signer<Operation, SecretKey, PublicKey, Signature> {

    override fun sign(message: Operation, key: SecretKey): Signature =
        when (key) {
            is Ed25519SecretKey -> operationEd25519Signer.sign(message, key)
            is Secp256K1SecretKey -> operationSecp256K1Signer.sign(message, key)
            is P256SecretKey -> operationP256Signer.sign(message, key)
        }

    override fun verify(message: Operation, signature: Signature, key: PublicKey): Boolean {
        val signature = specifySignature(signature, key)

        return when {
            signature is Ed25519Signature && key is Ed25519PublicKey -> operationEd25519Signer.verify(message, signature, key)
            signature is Secp256K1Signature && key is Secp256K1PublicKey -> operationSecp256K1Signer.verify(message, signature, key)
            signature is P256Signature && key is P256PublicKey -> operationP256Signer.verify(message, signature, key)
            else -> false
        }
    }

    private fun specifySignature(signature: Signature, key: PublicKey): Signature =
        if (signature !is GenericSignature) signature
        else when (key) {
            is Ed25519PublicKey -> signature.toEd25519Signature(genericSignatureToEd25519SignatureConverter)
            is Secp256K1PublicKey -> signature.toSecp256K1Signature(genericSignatureToSecp256K1SignatureConverter)
            is P256PublicKey -> signature.toP256Signature(genericSignatureToP256SignatureConverter)
        }
}

internal class OperationEd25519Signer(
    crypto: Crypto,
    operationBytesCoder: ConsumingBytesCoder<Operation>,
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

internal class OperationSecp256K1Signer(
    crypto: Crypto,
    operationBytesCoder: ConsumingBytesCoder<Operation>,
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

internal class OperationP256Signer(
    crypto: Crypto,
    operationBytesCoder: ConsumingBytesCoder<Operation>,
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
    private val operationBytesCoder: ConsumingBytesCoder<Operation>,
) {
    fun sign(message: Operation, key: ByteArray, signer: Crypto.(ByteArray, ByteArray) -> ByteArray): ByteArray =
        crypto.signer(hash(message), key)

    fun verify(message: Operation, signature: ByteArray, key: ByteArray, verifier: Crypto.(ByteArray, ByteArray, ByteArray) -> Boolean): Boolean =
        crypto.verifier(hash(message), signature, key)

    private fun hash(operation: Operation): ByteArray =
        crypto.hash(Watermark.GenericOperation + operation.forgeToBytes(operationBytesCoder), 32)
}