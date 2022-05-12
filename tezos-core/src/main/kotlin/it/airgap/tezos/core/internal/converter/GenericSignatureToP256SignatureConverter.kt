package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.type.encoded.MetaSignature
import it.airgap.tezos.core.type.encoded.P256Signature

internal class GenericSignatureToP256SignatureConverter(
    signatureBytesCoder: SignatureBytesCoder,
    encodedBytesCoder: EncodedBytesCoder,
) : GenericSignatureToSignatureConverter<P256Signature>(signatureBytesCoder, encodedBytesCoder) {
    override val kind: MetaSignature.Kind<P256Signature, P256Signature>
        get() = P256Signature
}