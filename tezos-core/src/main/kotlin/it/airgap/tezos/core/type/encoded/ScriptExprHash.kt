package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos script expression hash.
 *
 * @property base58 The encoded string: `expr(54)`.
 */
@JvmInline
public value class ScriptExprHash(override val base58: String) : Encoded, MetaEncoded<ScriptExprHash, ScriptExprHash> {

    init {
        require(isValid(base58)) { "Invalid script expr hash."}
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<ScriptExprHash, ScriptExprHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: ScriptExprHash
        get() = this

    public companion object : MetaEncoded.Kind<ScriptExprHash, ScriptExprHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "expr"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(13, 44, 64, 27)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 54

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [ScriptExprHash] from the [base58] string if it's a valid base58 encoded script expression hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): ScriptExprHash? = if (isValid(base58)) ScriptExprHash(base58) else null
    }
}