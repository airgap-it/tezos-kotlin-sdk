package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.MetaSignature
import it.airgap.tezos.core.type.encoded.Signature

internal abstract class GenericSignatureToSignatureConverter<out S : Signature, out M : MetaSignature<M, S>>(
    private val signatureBytesCoder: ConsumingBytesCoder<Signature>,
    private val encodedBytesCoder: EncodedBytesCoder,
) : Converter<GenericSignature, S> {
    protected abstract val kind: MetaSignature.Kind<M, S>

    override fun convert(value: GenericSignature): S {
        val bytes = signatureBytesCoder.encode(value)
        return encodedBytesCoder.decode(bytes, kind)
    }
}