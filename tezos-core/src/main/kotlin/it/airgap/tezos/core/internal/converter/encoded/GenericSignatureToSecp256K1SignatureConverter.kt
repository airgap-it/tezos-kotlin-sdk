package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.type.encoded.MetaSignature
import it.airgap.tezos.core.type.encoded.Secp256K1Signature
import it.airgap.tezos.core.type.encoded.Signature

internal class GenericSignatureToSecp256K1SignatureConverter(
    signatureBytesCoder: ConsumingBytesCoder<Signature>,
    encodedBytesCoder: EncodedBytesCoder,
) : GenericSignatureToSignatureConverter<Secp256K1Signature, Secp256K1Signature>(signatureBytesCoder, encodedBytesCoder) {
    override val kind: MetaSignature.Kind<Secp256K1Signature, Secp256K1Signature>
        get() = Secp256K1Signature
}