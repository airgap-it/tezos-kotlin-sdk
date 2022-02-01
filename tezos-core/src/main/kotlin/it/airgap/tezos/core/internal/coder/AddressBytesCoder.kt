package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.type.BytesTag
import it.airgap.tezos.core.internal.utils.consumeAt
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.startsWith

public class AddressBytesCoder(
    private val keyHashBytesCoder: KeyHashBytesCoder,
    private val base58BytesCoder: Base58BytesCoder
) : ConsumingBytesCoder<String> {
    override fun encode(value: String): ByteArray {
        val prefix = Tezos.Prefix.recognize(value) ?: failWithInvalidAddress(value)
        val tag = AddressTag.fromPrefix(prefix)
        val encoder: (String, Tezos.Prefix) -> ByteArray = when (tag) {
            AddressTag.ImplicitAccount -> keyHashBytesCoder::encode
            AddressTag.OriginatedAccount -> base58BytesCoder::encode
            else -> failWithInvalidAddress(value)
        }

        return tag + encoder(value, prefix)
    }

    override fun decode(value: ByteArray): String {
        val decoder: (ByteArray) -> String = when (AddressTag.recognize(value)) {
            AddressTag.ImplicitAccount -> keyHashBytesCoder::decode
            AddressTag.OriginatedAccount -> ({ base58BytesCoder.decode(it, Tezos.Prefix.ContractHash) })
            else -> failWithInvalidAddressBytes(value)
        }

        return decoder(value.sliceArray(1 until value.size))
    }

    override fun decodeConsuming(value: MutableList<Byte>): String {
        val tag = AddressTag.recognize(value) ?: failWithInvalidAddressBytes(value)
        value.consumeAt(0 until tag.value.size)

        val decoder: (MutableList<Byte>) -> String = when (tag) {
            AddressTag.ImplicitAccount -> keyHashBytesCoder::decodeConsuming
            AddressTag.OriginatedAccount -> ({ base58BytesCoder.decodeConsuming(it, Tezos.Prefix.ContractHash) })
        }

        return decoder(value)
    }

    private fun failWithInvalidAddress(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos address.")

    private fun failWithInvalidAddressBytes(value: ByteArray): Nothing = failWithInvalidAddressBytes(value.joinToString(prefix = "[", postfix = "]"))
    private fun failWithInvalidAddressBytes(value: MutableList<Byte>): Nothing = failWithInvalidAddressBytes(value.joinToString(prefix = "[", postfix = "]"))
    private fun failWithInvalidAddressBytes(value: String): Nothing = failWithIllegalArgument("Bytes `$value` are not valid Tezos address bytes.")
}

private enum class AddressTag(override val value: ByteArray) : BytesTag {
    ImplicitAccount(byteArrayOf(0)),
    OriginatedAccount(byteArrayOf(1));

    companion object {
        fun fromPrefix(prefix: Tezos.Prefix): AddressTag? =
            when (prefix) {
                Tezos.Prefix.Ed25519PublicKeyHash, Tezos.Prefix.Secp256K1PublicKeyHash, Tezos.Prefix.P256PublicKeyHash -> ImplicitAccount
                Tezos.Prefix.ContractHash -> OriginatedAccount
                else -> null
            }

        fun recognize(bytes: ByteArray): AddressTag? =
            if (bytes.isEmpty()) null
            else find(bytes::startsWith)

        fun recognize(bytes: List<Byte>): AddressTag? =
            if (bytes.isEmpty()) null
            else find(bytes::startsWith)

        private fun find(startsWith: (ByteArray) -> Boolean): AddressTag? =
            values().find { startsWith(it.value) }
    }
}
