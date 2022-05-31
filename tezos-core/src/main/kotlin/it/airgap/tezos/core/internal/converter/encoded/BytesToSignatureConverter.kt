package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

internal class BytesToSignatureConverter(base58Check: Base58Check) : BytesToEncodedGroupedConverter<Signature, MetaSignature<*, Signature>>(base58Check) {
    override val kinds: List<MetaEncoded.Kind<MetaSignature<*, Signature>, Signature>>
        get() = listOf(GenericSignature, Ed25519Signature, Secp256K1Signature, P256Signature)

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidSignatureBytes(value)
    private fun failWithInvalidSignatureBytes(value: ByteArray): Nothing =
        failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos signature bytes.")
}