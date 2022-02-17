package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

@InternalTezosSdkApi
public class BytesToPublicKeyConverter(base58Check: Base58Check) : BytesToEncodedGroupedConverter<PublicKeyEncoded<*>>(base58Check) {
    override val kinds: List<Encoded.Kind<PublicKeyEncoded<*>>>
        get() = listOf(Ed25519PublicKey, Secp256K1PublicKey, P256PublicKey)

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidPublicKeyBytes(value)
    private fun failWithInvalidPublicKeyBytes(value: ByteArray): Nothing =
        failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos public key bytes.")
}