package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/* Co(52) */

@JvmInline
public value class ContextHash(override val base58: String) : Encoded, MetaEncoded<ContextHash> {

    init {
        require(isValid(base58)) { "Invalid context hash." }
    }

    override val kind: MetaEncoded.Kind<ContextHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*>
        get() = this

    override val encoded: Encoded
        get() = this

    public companion object : MetaEncoded.Kind<ContextHash> {
        override val base58Prefix: String = "Co"
        override val base58Bytes: ByteArray = byteArrayOf(79, (199).toByte())
        override val base58Length: Int = 52

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): ContextHash? = if (isValid(base58)) ContextHash(base58) else null
    }
}