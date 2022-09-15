package it.airgap.tezos.core.internal.converter.encoded

import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.TezosCoreContext.decodeFromBytes
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.type.encoded.*

internal class PublicKeyToPublicKeyHashConverter(
    private val crypto: Crypto,
    private val encodedBytesCoder: EncodedBytesCoder,
) : Converter<PublicKey, PublicKeyHash> {

    override fun convert(value: PublicKey): PublicKeyHash {
        val hash = crypto.hash(encodedBytesCoder.encode(value), 20)
        
        return when (value) {
            is Ed25519PublicKey -> Ed25519PublicKeyHash.decodeFromBytes(hash, encodedBytesCoder)
            is Secp256K1PublicKey -> Secp256K1PublicKeyHash.decodeFromBytes(hash, encodedBytesCoder)
            is P256PublicKey -> P256PublicKeyHash.decodeFromBytes(hash, encodedBytesCoder)
        }
    }
}