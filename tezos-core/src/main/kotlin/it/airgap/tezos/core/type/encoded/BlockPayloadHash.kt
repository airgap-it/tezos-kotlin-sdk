package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/* vh(52) */

@JvmInline
public value class BlockPayloadHash(override val base58: String) : Encoded, MetaEncoded<BlockPayloadHash, BlockPayloadHash> {

    init {
        require(isValid(base58)) { "Invalid block payload hash." }
    }

    override val kind: MetaEncoded.Kind<BlockPayloadHash, BlockPayloadHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    override val encoded: BlockPayloadHash
        get() = this

    public companion object : MetaEncoded.Kind<BlockPayloadHash, BlockPayloadHash> {
        override val base58Prefix: String = "vh"
        override val base58Bytes: ByteArray = byteArrayOf(1, 106, (242).toByte())
        override val base58Length: Int = 52

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): BlockPayloadHash? = if (isValid(base58)) BlockPayloadHash(base58) else null
    }
}