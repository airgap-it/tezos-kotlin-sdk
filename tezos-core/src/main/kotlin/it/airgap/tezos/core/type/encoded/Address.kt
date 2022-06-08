package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Group of base58 encoded Tezos account addresses, either implicit or originated.
 *
 * See `samples/src/test/kotlin/type/Address/Address.Usage` for a sample usage.
 *
 * @see ImplicitAddress
 * @see OriginatedAddress
 */
public sealed interface Address : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaAddress<*, *>

    public companion object {
        internal val kinds: List<MetaAddress.Kind<*, *>>
            get() = ImplicitAddress.kinds + OriginatedAddress.kinds

        /**
         * Checks if [string] is a valid Tezos [Address]
         */
        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

/**
 * Group of base58 encoded implicit Tezos account addresses.
 *
 * See `samples/src/test/kotlin/type/Address/Address.Usage` for a sample usage.
 *
 * @see PublicKeyHash
 */
public sealed interface ImplicitAddress : Address {
    @InternalTezosSdkApi
    override val meta: MetaImplicitAddress<*, *>

    public companion object {
        internal val kinds: List<MetaImplicitAddress.Kind<*, *>>
            get() = PublicKeyHash.kinds

        /**
         * Checks if [string] is a valid Tezos [ImplicitAddress]
         */
        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

/**
 * Group of base58 encoded originated Tezos account addresses.
 *
 * See `samples/src/test/kotlin/type/Address/Address.Usage` for a sample usage.
 *
 * @see ContractHash
 */
public sealed interface OriginatedAddress : Address {
    @InternalTezosSdkApi
    override val meta: MetaOriginatedAddress<*, *>

    public companion object {
        internal val kinds: List<MetaOriginatedAddress.Kind<*, *>>
            get() = listOf(
                ContractHash,
            )

        /**
         * Checks if [string] is a valid Tezos [OriginatedAddress]
         */
        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

// -- meta --

@InternalTezosSdkApi
public sealed interface MetaAddress<out Self : MetaAddress<Self, A>, out A : Address> : MetaEncoded<Self, A> {
    override val encoded: A

    public sealed interface Kind<out M : MetaAddress<M, A>, out A : Address> : MetaEncoded.Kind<M, A>
}

@InternalTezosSdkApi
public sealed interface MetaImplicitAddress<out Self : MetaImplicitAddress<Self, IA>, out IA : ImplicitAddress> : MetaAddress<Self, IA> {
    override val encoded: IA

    public sealed interface Kind<out M : MetaImplicitAddress<M, IA>, out IA : ImplicitAddress> : MetaAddress.Kind<M, IA>
}

@InternalTezosSdkApi
public sealed interface MetaOriginatedAddress<out Self : MetaOriginatedAddress<Self, OA>, out OA : OriginatedAddress> : MetaAddress<Self, OA> {
    override val encoded: OA

    public sealed interface Kind<out M : MetaOriginatedAddress<M, OA>, out OA : OriginatedAddress> : MetaAddress.Kind<M, OA>
}