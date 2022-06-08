package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.MetaEncoded
import it.airgap.tezos.core.type.encoded.MetaPublicKey
import it.airgap.tezos.core.type.encoded.PublicKey

internal class StringToPublicKeyConverter : StringToEncodedGroupedConverter<PublicKey, MetaPublicKey<*, PublicKey>>() {
    override val kinds: List<MetaEncoded.Kind<MetaPublicKey<*, PublicKey>, PublicKey>>
        get() = PublicKey.kinds

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidPublicKey(value)
    private fun failWithInvalidPublicKey(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos public key.")
}