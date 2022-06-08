package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Group of base58 encoded signatures.
 *
 * @see [Ed25519Signature]
 * @see [Secp256K1Signature]
 * @see [P256Signature]
 * @see [GenericSignature]
 */
public sealed interface Signature : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaSignature<*, *>

    public companion object {
        internal val kinds: List<MetaSignature.Kind<*, *>>
            get() = listOf(
                Ed25519Signature,
                Secp256K1Signature,
                P256Signature,
                GenericSignature,
            )

        /**
         * Checks if [string] is a valid Tezos [Signature]
         */
        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

// -- meta --

@InternalTezosSdkApi
public sealed interface MetaSignature<out Self : MetaSignature<Self, S>, out S : Signature> : MetaEncoded<Self, S> {
    override val encoded: S

    @InternalTezosSdkApi
    public sealed interface Kind<out M : MetaSignature<M, S>, out S : Signature> : MetaEncoded.Kind<M, S>
}
