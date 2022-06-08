package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.PublicKeyHash

internal class StringToImplicitAddressConverter(
    private val stringToPublicKeyHashConverter: Converter<String, PublicKeyHash>
) : Converter<String, ImplicitAddress> {
    override fun convert(value: String): ImplicitAddress = stringToPublicKeyHashConverter.convert(value)
}