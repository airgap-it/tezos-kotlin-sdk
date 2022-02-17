package it.airgap.tezos.crypto.bouncycastle

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.crypto.bouncycastle.internal.utils.generateSignature
import it.airgap.tezos.crypto.bouncycastle.internal.utils.hash
import it.airgap.tezos.crypto.bouncycastle.internal.utils.verifySignature
import org.bouncycastle.crypto.digests.Blake2bDigest
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.params.*
import org.bouncycastle.crypto.signers.ECDSASigner
import org.bouncycastle.crypto.signers.Ed25519Signer
import org.bouncycastle.crypto.signers.HMacDSAKCalculator
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec
import java.math.BigInteger

public class BouncyCastleCryptoProvider : CryptoProvider {
    override fun sha256(message: ByteArray): ByteArray {
        val shA256Digest = SHA256Digest()
        return shA256Digest.hash(message)
    }

    override fun blake2b(message: ByteArray, size: Int): ByteArray {
        if (size !in 1..64) failWithInvalidHashSize(size)

        val digestSize = size * 8
        val blake2bDigest = Blake2bDigest(digestSize)
        return blake2bDigest.hash(message, size)
    }

    override fun signEd25519(message: ByteArray, secretKey: ByteArray): ByteArray {
        val signer = Ed25519Signer().apply {
            init(true, Ed25519PrivateKeyParameters(secretKey.sliceArray(0 until 32)))
        }

        return signer.generateSignature(message)
    }

    override fun verifyEd25519(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean {
        val signer = Ed25519Signer().apply {
            init(false, Ed25519PublicKeyParameters(publicKey))
        }

        return signer.verifySignature(message, signature)
    }

    override fun signSecp256K1(message: ByteArray, secretKey: ByteArray): ByteArray =
        signEC(message, secretKey, ECCurve.Secp256K1)

    override fun verifySecp256K1(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean =
        verifyEC(message, signature, publicKey, ECCurve.Secp256K1)

    override fun signP256(message: ByteArray, secretKey: ByteArray): ByteArray =
        signEC(message, secretKey, ECCurve.Secp256R1)

    override fun verifyP256(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean =
        verifyEC(message, signature, publicKey, ECCurve.Secp256R1)

    private fun signEC(message: ByteArray, secretKey: ByteArray, curve: ECCurve): ByteArray {
        val spec = ecdsaSpec(curve)
        val domainParameters = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
        val d = BigInteger(secretKey)

        val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest())).apply {
            init(true, ECPrivateKeyParameters(d, domainParameters))
        }

        return signer.generateSignature(message, spec)
    }

    private fun verifyEC(message: ByteArray, signature: ByteArray, publicKey: ByteArray, curve: ECCurve): Boolean {
        val spec = ecdsaSpec(curve)
        val domainParameters = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
        val q = spec.curve.decodePoint(publicKey)

        val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest())).apply {
            init(false, ECPublicKeyParameters(q, domainParameters))
        }

        return signer.verifySignature(message, signature)
    }

    private fun ecdsaSpec(curve: ECCurve): ECNamedCurveParameterSpec = ECNamedCurveTable.getParameterSpec(curve.bcName)

    private enum class ECCurve(val bcName: String) {
        Secp256K1("secp256K1"),
        Secp256R1("secp256r1"),
    }

    private fun failWithInvalidHashSize(size: Int): Nothing = failWithIllegalArgument("Hash size $size is invalid, expected: 1 <= size <= 64.")
}