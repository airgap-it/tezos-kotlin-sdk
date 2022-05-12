package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

internal class BytesToPublicKeyHashConverter(base58Check: Base58Check) : BytesToEncodedGroupedConverter<PublicKeyHash, MetaPublicKeyHash<*, PublicKeyHash>>(base58Check) {
    override val kinds: List<MetaEncoded.Kind<MetaPublicKeyHash<*, PublicKeyHash>, PublicKeyHash>>
        get() = listOf(Ed25519PublicKeyHash, Secp256K1PublicKeyHash, P256PublicKeyHash)

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidImplicitAddressBytes(value)
    private fun failWithInvalidImplicitAddressBytes(value: ByteArray): Nothing =
        failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos public key hash bytes.")
}