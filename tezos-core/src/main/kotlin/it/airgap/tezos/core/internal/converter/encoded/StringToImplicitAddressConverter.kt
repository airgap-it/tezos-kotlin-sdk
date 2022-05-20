package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

internal class StringToImplicitAddressConverter : StringToEncodedGroupedConverter<ImplicitAddress, MetaImplicitAddress<*, ImplicitAddress>>() {
    override val kinds: List<MetaEncoded.Kind<MetaImplicitAddress<*, ImplicitAddress>, ImplicitAddress>>
        get() = listOf(Ed25519PublicKeyHash, Secp256K1PublicKeyHash, P256PublicKeyHash)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidImplicitAddress(value)
    private fun failWithInvalidImplicitAddress(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos implicit address.")
}