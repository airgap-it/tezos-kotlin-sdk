package it.airgap.tezos.crypto.bouncycastle.internal.utils

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.splitAt
import it.airgap.tezos.core.internal.utils.toHexString
import org.bouncycastle.crypto.signers.ECDSASigner
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec
import java.math.BigInteger

internal fun ECDSASigner.generateSignature(message: ByteArray, spec: ECNamedCurveParameterSpec): ByteArray =
    generateSignature(message).canonical(spec).mergeComponents()

internal fun ECDSASigner.verifySignature(message: ByteArray, signature: ByteArray): Boolean {
    val (r, s) = signature.toComponents()
    return verifySignature(message, r, s)
}

private fun Array<BigInteger>.canonical(spec: ECNamedCurveParameterSpec): Pair<BigInteger, BigInteger> =
    let { (r, s) ->
        val halfCurveOrder = spec.n.shiftRight(1)
        Pair(r, if (s <= halfCurveOrder) s else spec.n - s)
    }

private fun Pair<BigInteger, BigInteger>.mergeComponents(): ByteArray =
    let { (r, s) ->
        val merged = r.toString(16) + s.toString(16)
        merged.asHexString().toByteArray()
    }

private fun ByteArray.toComponents(): Pair<BigInteger, BigInteger> =
    splitAt(32).let { (r, s) ->
        val r = r.toHexString().asString()
        val s = s.toHexString().asString()

        Pair(BigInteger(r, 16), BigInteger(s, 16))
    }