package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.type.encoded.MetaSignatureEncoded
import it.airgap.tezos.core.type.encoded.Secp256K1Signature

@InternalTezosSdkApi
public class GenericSignatureToSecp256K1SignatureConverter(
    signatureBytesCoder: SignatureBytesCoder,
    encodedBytesCoder: EncodedBytesCoder,
) : GenericSignatureToSignatureConverter<Secp256K1Signature>(signatureBytesCoder, encodedBytesCoder) {
    override val kind: MetaSignatureEncoded.Kind<Secp256K1Signature>
        get() = Secp256K1Signature
}