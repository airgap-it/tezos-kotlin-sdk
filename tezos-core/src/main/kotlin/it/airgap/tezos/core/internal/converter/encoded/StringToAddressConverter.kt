package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.MetaAddress
import it.airgap.tezos.core.type.encoded.MetaEncoded

internal class StringToAddressConverter : StringToEncodedGroupedConverter<Address, MetaAddress<*, Address>>() {
    override val kinds: List<MetaEncoded.Kind<MetaAddress<*, Address>, Address>>
        get() = Address.kinds

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidAddress(value)
    private fun failWithInvalidAddress(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos address.")
}