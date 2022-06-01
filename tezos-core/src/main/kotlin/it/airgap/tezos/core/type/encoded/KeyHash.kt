package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// --  KeyHash --

public sealed interface KeyHash : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaKeyHash<*, *>

    public companion object {
        internal val kinds: List<MetaKeyHash.Kind<*, *>>
            get() = PublicKeyHash.kinds

        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

public sealed interface PublicKeyHash : KeyHash, ImplicitAddress {
    @InternalTezosSdkApi
    override val meta: MetaPublicKeyHash<*, *>

    public companion object {
        internal val kinds: List<MetaPublicKeyHash.Kind<*, *>>
            get() = listOf(
                Ed25519PublicKeyHash,
                Secp256K1PublicKeyHash,
                P256PublicKeyHash,
            )

        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

// -- BlindedKeyHash --

public sealed interface BlindedKeyHash : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaBlindedKeyHash<*, *>

    public companion object {
        internal val kinds: List<MetaBlindedKeyHash.Kind<*, *>>
            get() = BlindedPublicKeyHash.kinds

        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

public sealed interface BlindedPublicKeyHash : BlindedKeyHash {
    @InternalTezosSdkApi
    override val meta: MetaBlindedPublicKeyHash<*, *>

    public companion object {
        internal val kinds: List<MetaBlindedPublicKeyHash.Kind<*, *>>
            get() = listOf(
                Ed25519BlindedPublicKeyHash,
            )

        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

// -- MetaKeyHash --

@InternalTezosSdkApi
public sealed interface MetaKeyHash<out Self : MetaKeyHash<Self, KH>, out KH : KeyHash> : MetaEncoded<Self, KH> {
    override val encoded: KH

    public sealed interface Kind<out M : MetaKeyHash<M, KH>, out KH : KeyHash> : MetaEncoded.Kind<M, KH>
}

@InternalTezosSdkApi
public sealed interface MetaPublicKeyHash<out Self : MetaPublicKeyHash<Self, PKH>, out PKH : PublicKeyHash> : MetaKeyHash<Self, PKH>, MetaImplicitAddress<Self, PKH> {
    override val encoded: PKH

    public sealed interface Kind<out M : MetaPublicKeyHash<M, PKH>, out PKH : PublicKeyHash> : MetaKeyHash.Kind<M, PKH>, MetaImplicitAddress.Kind<M, PKH>
}

// -- MetaBlindedKeyHash --

@InternalTezosSdkApi
public sealed interface MetaBlindedKeyHash<out Self: MetaBlindedKeyHash<Self, BKH>, out BKH : BlindedKeyHash> : MetaEncoded<Self, BKH> {
    override val encoded: BKH

    public sealed interface Kind<out M : MetaBlindedKeyHash<M, BKH>, out BKH : BlindedKeyHash> : MetaEncoded.Kind<M, BKH>
}

@InternalTezosSdkApi
public sealed interface MetaBlindedPublicKeyHash<out Self: MetaBlindedPublicKeyHash<Self, BPKH>, out BPKH : BlindedPublicKeyHash> : MetaBlindedKeyHash<Self, BPKH> {
    override val encoded: BPKH

    public sealed interface Kind<out M: MetaBlindedPublicKeyHash<M, BPKH>, out BPKH : BlindedPublicKeyHash> : MetaBlindedKeyHash.Kind<M, BPKH>
}