package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

@InternalTezosSdkApi
public class BytesToAddressConverter(base58Check: Base58Check) : BytesToEncodedGroupedConverter<Address<*>>(base58Check) {
    override val kinds: List<Encoded.Kind<out Address<*>>>
        get() = listOf(Ed25519PublicKeyHash, Secp256K1PublicKeyHash, P256PublicKeyHash, ContractHash)

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidAddressBytes(value)
    private fun failWithInvalidAddressBytes(value: ByteArray): Nothing =
        failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos address bytes.")
}