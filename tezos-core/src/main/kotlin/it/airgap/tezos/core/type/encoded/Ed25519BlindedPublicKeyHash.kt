package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/* btz1(37) */

@JvmInline
public value class Ed25519BlindedPublicKeyHash(override val base58: String) : BlindedPublicKeyHashEncoded, MetaBlindedPublicKeyHashEncoded<Ed25519BlindedPublicKeyHash> {

    init {
        require(isValid(base58)) { "Invalid Ed25519 blinded public key hash." }
    }

    override val kind: MetaEncoded.Kind<Ed25519BlindedPublicKeyHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaBlindedPublicKeyHashEncoded<*>
        get() = this

    override val encoded: BlindedPublicKeyHashEncoded
        get() = this

    public companion object : MetaBlindedPublicKeyHashEncoded.Kind<Ed25519BlindedPublicKeyHash> {
        override val base58Prefix: String = "btz1"
        override val base58Bytes: ByteArray = byteArrayOf(1, 2, 49, (223).toByte())
        override val base58Length: Int = 37
        override val bytesLength: Int = 20

        override fun createValueOrNull(base58: String): Ed25519BlindedPublicKeyHash? = if (isValid(base58)) Ed25519BlindedPublicKeyHash(base58) else null
    }
}