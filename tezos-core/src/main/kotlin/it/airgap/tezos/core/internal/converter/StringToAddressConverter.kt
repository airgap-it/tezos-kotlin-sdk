package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

@InternalTezosSdkApi
public class StringToAddressConverter : StringToEncodedGroupedConverter<MetaAddress<*>>() {
    override val kinds: List<MetaEncoded.Kind<MetaAddress<*>>>
        get() = listOf(Ed25519PublicKeyHash, Secp256K1PublicKeyHash, P256PublicKeyHash, ContractHash)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidAddress(value)
    private fun failWithInvalidAddress(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos address.")
}