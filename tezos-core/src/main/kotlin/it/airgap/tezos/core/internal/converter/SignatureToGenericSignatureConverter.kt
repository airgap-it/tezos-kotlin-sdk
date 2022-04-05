package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.decodeFromBytes
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.MetaSignatureEncoded

@InternalTezosSdkApi
public class SignatureToGenericSignatureConverter(
    private val signatureBytesCoder: SignatureBytesCoder,
    private val encodedBytesCoder: EncodedBytesCoder,
) : Converter<MetaSignatureEncoded<*>, GenericSignature> {
    override fun convert(value: MetaSignatureEncoded<*>): GenericSignature {
        if (value is GenericSignature) return value

        val bytes = signatureBytesCoder.encode(value)
        return GenericSignature.decodeFromBytes(bytes, encodedBytesCoder)
    }
}