package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.Signature

internal class SignatureToGenericSignatureConverter(
    private val signatureBytesCoder: ConsumingBytesCoder<Signature>,
    private val encodedBytesCoder: EncodedBytesCoder,
) : Converter<Signature, GenericSignature> {
    override fun convert(value: Signature): GenericSignature {
        if (value is GenericSignature) return value

        val bytes = signatureBytesCoder.encode(value)
        return GenericSignature.decodeFromBytes(bytes, encodedBytesCoder)
    }
}