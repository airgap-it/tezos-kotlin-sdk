package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

internal class StringToSignatureConverter : StringToEncodedGroupedConverter<Signature, MetaSignature<*, Signature>>() {
    override val kinds: List<MetaEncoded.Kind<MetaSignature<*, Signature>, Signature>>
        get() = listOf(Ed25519Signature, Secp256K1Signature, P256Signature, GenericSignature)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidPublicKey(value)
    private fun failWithInvalidPublicKey(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos public key.")
}