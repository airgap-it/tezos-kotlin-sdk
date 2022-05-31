package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

internal class BytesToPublicKeyConverter(base58Check: Base58Check) : BytesToEncodedGroupedConverter<PublicKey, MetaPublicKey<*, PublicKey>>(base58Check) {
    override val kinds: List<MetaEncoded.Kind<MetaPublicKey<*, PublicKey>, PublicKey>>
        get() = listOf(Ed25519PublicKey, Secp256K1PublicKey, P256PublicKey)

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidPublicKeyBytes(value)
    private fun failWithInvalidPublicKeyBytes(value: ByteArray): Nothing =
        failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos public key bytes.")
}