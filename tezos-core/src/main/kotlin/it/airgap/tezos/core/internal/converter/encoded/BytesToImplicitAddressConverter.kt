package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

internal class BytesToImplicitAddressConverter(base58Check: Base58Check) : BytesToEncodedGroupedConverter<ImplicitAddress, MetaImplicitAddress<*, ImplicitAddress>>(base58Check) {
    override val kinds: List<MetaEncoded.Kind<MetaImplicitAddress<*, ImplicitAddress>, ImplicitAddress>>
        get() = listOf(Ed25519PublicKeyHash, Secp256K1PublicKeyHash, P256PublicKeyHash)

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidImplicitAddressBytes(value)
    private fun failWithInvalidImplicitAddressBytes(value: ByteArray): Nothing =
        failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos implicit address bytes.")
}