package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.type.encoded.Ed25519Signature
import it.airgap.tezos.core.type.encoded.MetaSignatureEncoded

@InternalTezosSdkApi
public class GenericSignatureToEd25519SignatureConverter(
    signatureBytesCoder: SignatureBytesCoder,
    encodedBytesCoder: EncodedBytesCoder,
) : GenericSignatureToSignatureConverter<Ed25519Signature>(signatureBytesCoder, encodedBytesCoder) {
    override val kind: MetaSignatureEncoded.Kind<Ed25519Signature>
        get() = Ed25519Signature
}