package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/* id(30) */

@JvmInline
public value class CryptoboxPublicKeyHash(override val base58: String) : Encoded, MetaEncoded<CryptoboxPublicKeyHash, CryptoboxPublicKeyHash> {

    init {
        require(isValid(base58)) { "Invalid Cryptobox key hash." }
    }

    override val kind: MetaEncoded.Kind<CryptoboxPublicKeyHash, CryptoboxPublicKeyHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    override val encoded: CryptoboxPublicKeyHash
        get() = this

    public companion object : MetaEncoded.Kind<CryptoboxPublicKeyHash, CryptoboxPublicKeyHash> {
        override val base58Prefix: String = "id"
        override val base58Bytes: ByteArray = byteArrayOf((153).toByte(), (103).toByte())
        override val base58Length: Int = 30

        override val bytesLength: Int = 16

        override fun createValueOrNull(base58: String): CryptoboxPublicKeyHash? = if (isValid(base58)) CryptoboxPublicKeyHash(base58) else null
    }
}