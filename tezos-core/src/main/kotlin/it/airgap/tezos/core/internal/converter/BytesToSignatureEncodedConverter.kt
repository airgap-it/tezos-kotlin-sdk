package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.*

@InternalTezosSdkApi
public class BytesToSignatureEncodedConverter(base58Check: Base58Check) : BytesToEncodedGroupedConverter<SignatureEncoded<*>>(base58Check) {
    override val kinds: List<Encoded.Kind<out SignatureEncoded<*>>>
        get() = listOf(Ed25519Signature, Secp256K1Signature, P256Signature, GenericSignature)

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidSignatureBytes(value)
    private fun failWithInvalidSignatureBytes(value: ByteArray): Nothing =
        failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos signature bytes.")
}