package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.MetaBlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.MetaEncoded

internal class StringToBlindedPublicKeyHashConverter : StringToEncodedGroupedConverter<BlindedPublicKeyHash, MetaBlindedPublicKeyHash<*, BlindedPublicKeyHash>>() {
    override val kinds: List<MetaEncoded.Kind<MetaBlindedPublicKeyHash<*, BlindedPublicKeyHash>, BlindedPublicKeyHash>>
        get() = BlindedPublicKeyHash.kinds

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidImplicitAddress(value)
    private fun failWithInvalidImplicitAddress(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos blinded public key hash.")
}