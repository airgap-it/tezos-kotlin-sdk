package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

internal class StringToPublicKeyConverter : StringToEncodedGroupedConverter<PublicKey, MetaPublicKey<*, PublicKey>>() {
    override val kinds: List<MetaEncoded.Kind<MetaPublicKey<*, PublicKey>, PublicKey>>
        get() = listOf(Ed25519PublicKey, Secp256K1PublicKey, P256PublicKey)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidPublicKey(value)
    private fun failWithInvalidPublicKey(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos public key.")
}