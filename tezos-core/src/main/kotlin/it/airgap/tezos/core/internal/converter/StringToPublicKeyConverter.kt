package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

@InternalTezosSdkApi
public class StringToPublicKeyConverter : StringToEncodedGroupedConverter<PublicKeyEncoded<*>>() {
    override val kinds: List<Encoded.Kind<PublicKeyEncoded<*>>>
        get() = listOf(Ed25519PublicKey, Secp256K1PublicKey, P256PublicKey)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidPublicKey(value)
    private fun failWithInvalidPublicKey(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos public key.")
}