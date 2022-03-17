package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.Ed25519BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.MetaBlindedPublicKeyHashEncoded
import it.airgap.tezos.core.type.encoded.MetaEncoded

@InternalTezosSdkApi
public class StringToBlindedPublicKeyHashConverter : StringToEncodedGroupedConverter<MetaBlindedPublicKeyHashEncoded<*>>() {
    override val kinds: List<MetaEncoded.Kind<MetaBlindedPublicKeyHashEncoded<*>>>
        get() = listOf(Ed25519BlindedPublicKeyHash)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidImplicitAddress(value)
    private fun failWithInvalidImplicitAddress(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos blinded public key hash.")
}