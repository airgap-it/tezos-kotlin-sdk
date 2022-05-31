package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.Ed25519BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.MetaBlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.MetaEncoded

internal class BytesToBlindedPublicKeyHashConverter(base58Check: Base58Check) : BytesToEncodedGroupedConverter<BlindedPublicKeyHash, MetaBlindedPublicKeyHash<*, BlindedPublicKeyHash>>(base58Check) {
    override val kinds: List<MetaEncoded.Kind<MetaBlindedPublicKeyHash<*, BlindedPublicKeyHash>, BlindedPublicKeyHash>>
        get() = listOf(Ed25519BlindedPublicKeyHash)

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidImplicitAddressBytes(value)
    private fun failWithInvalidImplicitAddressBytes(value: ByteArray): Nothing =
        failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos blinded public key hash bytes.")
}