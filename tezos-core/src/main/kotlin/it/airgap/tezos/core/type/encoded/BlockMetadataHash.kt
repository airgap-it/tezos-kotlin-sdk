package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/* bm(52) */

@JvmInline
public value class BlockMetadataHash(override val base58: String) : Encoded, MetaEncoded<BlockMetadataHash> {

    init {
        require(isValid(base58)) { "Invalid block metadata hash." }
    }

    override val kind: MetaEncoded.Kind<BlockMetadataHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*>
        get() = this

    override val encoded: Encoded
        get() = this

    public companion object : MetaEncoded.Kind<BlockMetadataHash> {
        override val base58Prefix: String = "bm"
        override val base58Bytes: ByteArray = byteArrayOf((234).toByte(), (249).toByte())
        override val base58Length: Int = 52

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): BlockMetadataHash? = if (isValid(base58)) BlockMetadataHash(base58) else null
    }
}