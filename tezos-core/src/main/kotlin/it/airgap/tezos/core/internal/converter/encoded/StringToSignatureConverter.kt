package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.MetaEncoded
import it.airgap.tezos.core.type.encoded.MetaSignature
import it.airgap.tezos.core.type.encoded.Signature

internal class StringToSignatureConverter : StringToEncodedGroupedConverter<Signature, MetaSignature<*, Signature>>() {
    override val kinds: List<MetaEncoded.Kind<MetaSignature<*, Signature>, Signature>>
        get() = Signature.kinds

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidPublicKey(value)
    private fun failWithInvalidPublicKey(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos public key.")
}