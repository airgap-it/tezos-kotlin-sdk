package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/* Net(15) */

@JvmInline
public value class ChainId(override val base58: String) : Encoded, MetaEncoded<ChainId, ChainId> {

    init {
        require(isValid(base58)) { "Invalid chain ID." }
    }

    override val kind: MetaEncoded.Kind<ChainId, ChainId>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    override val encoded: ChainId
        get() = this

    public companion object : MetaEncoded.Kind<ChainId, ChainId> {
        override val base58Prefix: String = "Net"
        override val base58Bytes: ByteArray = byteArrayOf(87, 82, 0)
        override val base58Length: Int = 15

        override val bytesLength: Int = 4

        override fun createValueOrNull(base58: String): ChainId? = if (isValid(base58)) ChainId(base58) else null
    }
}