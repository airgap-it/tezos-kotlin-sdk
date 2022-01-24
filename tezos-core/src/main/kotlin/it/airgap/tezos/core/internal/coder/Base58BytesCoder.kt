package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument

public class Base58BytesCoder(private val base58Check: Base58Check): BytesCoder<String> {

    override fun encode(value: String): ByteArray = encode(value, prefix = null)
    public fun encode(value: String, prefix: Tezos.Prefix?): ByteArray {
        if (prefix != null && (!value.startsWith(prefix.value) || value.length != prefix.base58Length)) failWithInvalidPrefix(value, prefix)

        val prefix = prefix ?: Tezos.Prefix.recognize(value)
        val decoded = base58Check.decode(value)
        val dataStart = prefix?.base58Bytes?.size ?: 0

        return decoded.sliceArray(dataStart until decoded.size)
    }

    override fun decode(value: ByteArray): String = decode(value, prefix = null)
    public fun decode(value: ByteArray, prefix: Tezos.Prefix?): String {
        if (prefix != null && value.size != prefix.dataLength) failWithInvalidPrefix(value, prefix)
        val bytes = prefix?.plus(value) ?: value
        return base58Check.encode(bytes)
    }

    private fun failWithInvalidPrefix(value: String, prefix: Tezos.Prefix): Nothing = failWithIllegalArgument("Value `$value` is not prefixed with `${prefix}`.")
    private fun failWithInvalidPrefix(value: ByteArray, prefix: Tezos.Prefix): Nothing = failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not meant to be prefixed with `${prefix}`.")
}
