package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.type.encoded.Ed25519Signature
import it.airgap.tezos.core.type.encoded.MetaSignature
import it.airgap.tezos.core.type.encoded.Signature

internal class GenericSignatureToEd25519SignatureConverter(
    signatureBytesCoder: ConsumingBytesCoder<Signature>,
    encodedBytesCoder: EncodedBytesCoder,
) : GenericSignatureToSignatureConverter<Ed25519Signature, Ed25519Signature>(signatureBytesCoder, encodedBytesCoder) {
    override val kind: MetaSignature.Kind<Ed25519Signature, Ed25519Signature>
        get() = Ed25519Signature
}