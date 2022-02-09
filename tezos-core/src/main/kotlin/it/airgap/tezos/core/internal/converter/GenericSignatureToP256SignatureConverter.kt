package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.type.encoded.P256Signature
import it.airgap.tezos.core.type.encoded.SignatureEncoded

@InternalTezosSdkApi
public class GenericSignatureToP256SignatureConverter(
    signatureBytesCoder: SignatureBytesCoder,
    encodedBytesCoder: EncodedBytesCoder,
) : GenericSignatureToSignatureConverter<P256Signature>(signatureBytesCoder, encodedBytesCoder) {
    override val kind: SignatureEncoded.Kind<P256Signature>
        get() = P256Signature
}