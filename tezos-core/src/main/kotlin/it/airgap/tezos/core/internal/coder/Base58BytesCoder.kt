package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument

public class Base58BytesCoder internal constructor(private val base58Check: Base58Check): BytesCoder<String>, PrefixedCoder<String> {
    override fun encode(value: String): ByteArray = encode(value, prefix = null)
    override fun decode(value: ByteArray): String = decode(value, prefix = null)

    override fun encode(value: String, prefix: Tezos.Prefix?): ByteArray {
        if (prefix != null && !value.startsWith(prefix.value)) failWithInvalidPrefix(value, prefix)

        val prefix = prefix ?: Tezos.Prefix.recognize(value)
        val decoded = base58Check.decode(value)

        return decoded.sliceArray((prefix?.base58Bytes?.size ?: 0) until decoded.size)
    }

    override fun decode(value: ByteArray, prefix: Tezos.Prefix?): String {
        TODO("Not yet implemented")
    }

    private fun failWithInvalidPrefix(value: String, prefix: Tezos.Prefix): Nothing = failWithIllegalArgument("Value `$value` is not prefixed with `${prefix}`.")
}
