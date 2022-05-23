package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.PublicKeyHash

internal class BytesToImplicitAddressConverter(
    private val bytesToPublicKeyHashConverter: Converter<ByteArray, PublicKeyHash>,
) : Converter<ByteArray, ImplicitAddress> {
    override fun convert(value: ByteArray): ImplicitAddress = bytesToPublicKeyHashConverter.convert(value)
}