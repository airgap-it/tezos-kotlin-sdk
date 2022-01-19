package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coder.Base58BytesCoder
import it.airgap.tezos.core.internal.coder.PrefixedCoder
import it.airgap.tezos.core.internal.coder.StringPrefixedBytesCoder
import it.airgap.tezos.core.internal.type.ByteTag
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument

internal class AddressBytesCoder(
    private val keyHashBytesCoder: KeyHashBytesCoder,
    private val base58BytesCoder: Base58BytesCoder
) : StringPrefixedBytesCoder(InnerCoder(keyHashBytesCoder, base58BytesCoder)) {
    override fun encode(value: String): ByteArray =
        when (val prefix = Tezos.Prefix.recognize(value)) {
            Tezos.Prefix.Ed25519PublicKeyHash, Tezos.Prefix.Secp256K1PublicKeyHash, Tezos.Prefix.P256PublicKeyHash -> AddressTag.ImplicitAccount + keyHashBytesCoder.encode(value, prefix)
            Tezos.Prefix.ContractHash -> AddressTag.OriginatedAccount + base58BytesCoder.encode(value, prefix)
            else -> failWithInvalidAddress(value)
        }

    override fun decode(value: ByteArray): String {
        TODO("Not yet implemented")
    }

    override fun decode(value: ByteArray, prefix: Tezos.Prefix?): String {
        TODO("Not yet implemented")
    }

    override fun tag(prefix: Tezos.Prefix?): ByteTag? = AddressTag.fromPrefix(prefix)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidAddress(value)
    private fun failWithInvalidAddress(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos address.")
}

private class InnerCoder(
    private val keyHashBytesCoder: KeyHashBytesCoder,
    private val base58BytesCoder: Base58BytesCoder
) : PrefixedCoder<String> {
    override fun encode(value: String, prefix: Tezos.Prefix?): ByteArray =
        when (prefix) {
            Tezos.Prefix.Ed25519PublicKeyHash, Tezos.Prefix.Secp256K1PublicKeyHash, Tezos.Prefix.P256PublicKeyHash -> keyHashBytesCoder.encode(value, prefix)
            Tezos.Prefix.ContractHash -> base58BytesCoder.encode(value, prefix)
            else -> failWithInvalidAddress(value)
        }

    override fun decode(value: ByteArray, prefix: Tezos.Prefix?): String {
        TODO("Not yet implemented")
    }

    private fun failWithInvalidAddress(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos address.")
}

private enum class AddressTag(override val value: Byte) : ByteTag {
    ImplicitAccount(0),
    OriginatedAccount(1);

    companion object {
        fun fromPrefix(prefix: Tezos.Prefix?): AddressTag? =
            when (prefix) {
                Tezos.Prefix.Ed25519PublicKeyHash, Tezos.Prefix.Secp256K1PublicKeyHash, Tezos.Prefix.P256PublicKeyHash -> ImplicitAccount
                Tezos.Prefix.ContractHash -> OriginatedAccount
                else -> null
            }
    }
}
