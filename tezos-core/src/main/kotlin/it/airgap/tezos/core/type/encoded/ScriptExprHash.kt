package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/* expr(54) */

@JvmInline
public value class ScriptExprHash(override val base58: String) : Encoded, MetaEncoded<ScriptExprHash> {

    init {
        require(isValid(base58)) { "Invalid script expr hash."}
    }

    override val kind: MetaEncoded.Kind<ScriptExprHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*>
        get() = this

    override val encoded: Encoded
        get() = this

    public companion object : MetaEncoded.Kind<ScriptExprHash> {
        override val base58Prefix: String = "expr"
        override val base58Bytes: ByteArray = byteArrayOf(13, 44, 64, 27)
        override val base58Length: Int = 54
        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): ScriptExprHash? = if (isValid(base58)) ScriptExprHash(base58) else null
    }
}