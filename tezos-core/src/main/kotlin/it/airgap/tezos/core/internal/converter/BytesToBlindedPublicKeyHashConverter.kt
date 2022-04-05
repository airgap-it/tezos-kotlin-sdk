package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.Ed25519BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.MetaBlindedPublicKeyHashEncoded
import it.airgap.tezos.core.type.encoded.MetaEncoded

@InternalTezosSdkApi
public class BytesToBlindedPublicKeyHashConverter(base58Check: Base58Check) : BytesToEncodedGroupedConverter<MetaBlindedPublicKeyHashEncoded<*>>(base58Check) {
    override val kinds: List<MetaEncoded.Kind<MetaBlindedPublicKeyHashEncoded<*>>>
        get() = listOf(Ed25519BlindedPublicKeyHash)

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidImplicitAddressBytes(value)
    private fun failWithInvalidImplicitAddressBytes(value: ByteArray): Nothing =
        failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos blinded public key hash bytes.")
}