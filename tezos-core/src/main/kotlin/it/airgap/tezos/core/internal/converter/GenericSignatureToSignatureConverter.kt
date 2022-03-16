package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.MetaSignatureEncoded

@InternalTezosSdkApi
public abstract class GenericSignatureToSignatureConverter<out T : MetaSignatureEncoded<T>>(
    private val signatureBytesCoder: SignatureBytesCoder,
    private val encodedBytesCoder: EncodedBytesCoder,
) : Converter<GenericSignature, T> {
    protected abstract val kind: MetaSignatureEncoded.Kind<T>

    override fun convert(value: GenericSignature): T {
        val bytes = signatureBytesCoder.encode(value)
        return encodedBytesCoder.decode(bytes, kind)
    }
}