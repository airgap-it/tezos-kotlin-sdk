package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.decodeFromBytes
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.SignatureEncoded

@InternalTezosSdkApi
public class SignatureToGenericSignatureConverter(
    private val signatureBytesCoder: SignatureBytesCoder,
    private val encodedBytesCoder: EncodedBytesCoder,
) : Converter<SignatureEncoded<*>, GenericSignature> {
    override fun convert(value: SignatureEncoded<*>): GenericSignature {
        if (value is GenericSignature) return value

        val bytes = signatureBytesCoder.encode(value)
        return GenericSignature.decodeFromBytes(bytes, encodedBytesCoder)
    }
}