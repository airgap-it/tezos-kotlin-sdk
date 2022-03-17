package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

@InternalTezosSdkApi
public class StringToPublicKeyHashConverter : StringToEncodedGroupedConverter<MetaPublicKeyHashEncoded<*>>() {
    override val kinds: List<MetaEncoded.Kind<MetaPublicKeyHashEncoded<*>>>
        get() = listOf(Ed25519PublicKeyHash, Secp256K1PublicKeyHash, P256PublicKeyHash)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidImplicitAddress(value)
    private fun failWithInvalidImplicitAddress(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos public key hash.")
}