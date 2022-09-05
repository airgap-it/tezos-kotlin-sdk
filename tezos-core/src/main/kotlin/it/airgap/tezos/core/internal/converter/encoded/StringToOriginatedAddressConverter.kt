package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.MetaEncoded
import it.airgap.tezos.core.type.encoded.MetaOriginatedAddress
import it.airgap.tezos.core.type.encoded.OriginatedAddress

internal class StringToOriginatedAddressConverter : StringToEncodedGroupedConverter<OriginatedAddress, MetaOriginatedAddress<*, OriginatedAddress>>() {
    override val kinds: List<MetaEncoded.Kind<MetaOriginatedAddress<*, OriginatedAddress>, OriginatedAddress>>
        get() = OriginatedAddress.kinds

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidOriginatedAddress(value)
    private fun failWithInvalidOriginatedAddress(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos originated address.")
}