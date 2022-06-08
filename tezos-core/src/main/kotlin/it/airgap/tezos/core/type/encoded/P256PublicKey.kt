package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded P-256 public key.
 *
 * @property base58 The encoded string: `p2pk(55)`.
 */
@JvmInline
public value class P256PublicKey(override val base58: String) : PublicKey, MetaPublicKey<P256PublicKey, P256PublicKey> {

    init {
        require(isValid(base58)) { "Invalid P256 public key." }
    }

    @InternalTezosSdkApi
    override val kind: MetaPublicKey.Kind<P256PublicKey, P256PublicKey>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaPublicKey<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: P256PublicKey
        get() = this

    public companion object : MetaPublicKey.Kind<P256PublicKey, P256PublicKey> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "p2pk"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(3, (178).toByte(), (139).toByte(), 127)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 55

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 33

        /**
         * Creates [P256PublicKey] from the [base58] string if it's a valid base58 encoded P-256 public key value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): P256PublicKey? = if (isValid(base58)) P256PublicKey(base58) else null
    }
}