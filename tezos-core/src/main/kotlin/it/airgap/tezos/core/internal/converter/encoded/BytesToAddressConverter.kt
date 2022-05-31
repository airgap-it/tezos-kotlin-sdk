package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

internal class BytesToAddressConverter(base58Check: Base58Check) : BytesToEncodedGroupedConverter<Address, MetaAddress<*, Address>>(base58Check) {
    override val kinds: List<MetaEncoded.Kind<MetaAddress<*, Address>, Address>>
        get() = listOf(Ed25519PublicKeyHash, Secp256K1PublicKeyHash, P256PublicKeyHash, ContractHash)

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidAddressBytes(value)
    private fun failWithInvalidAddressBytes(value: ByteArray): Nothing =
        failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos address bytes.")
}