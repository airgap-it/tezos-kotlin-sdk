package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.MetaEncoded
import it.airgap.tezos.core.type.encoded.MetaPublicKeyHash
import it.airgap.tezos.core.type.encoded.PublicKeyHash

internal class StringToPublicKeyHashConverter : StringToEncodedGroupedConverter<PublicKeyHash, MetaPublicKeyHash<*, PublicKeyHash>>() {
    override val kinds: List<MetaEncoded.Kind<MetaPublicKeyHash<*, PublicKeyHash>, PublicKeyHash>>
        get() = PublicKeyHash.kinds

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidImplicitAddress(value)
    private fun failWithInvalidImplicitAddress(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos public key hash.")
}