package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

@InternalTezosSdkApi
public class StringToSignatureConverter : StringToEncodedGroupedConverter<SignatureEncoded<*>>() {
    override val kinds: List<Encoded.Kind<out SignatureEncoded<*>>>
        get() = listOf(Ed25519Signature, Secp256K1Signature, P256Signature, GenericSignature)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidPublicKey(value)
    private fun failWithInvalidPublicKey(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos public key.")
}