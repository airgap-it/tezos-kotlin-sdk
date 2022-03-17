package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// -- Scalar --

public sealed interface ScalarEncoded : Encoded {
    override val meta: MetaScalarEncoded<*>

    public companion object {}
}

// -- EncryptedScalar --

public sealed interface EncryptedScalarEncoded : Encoded {
    override val meta: MetaEncryptedScalarEncoded<*>

    public companion object {}
}

// -- MetaScalar --

@InternalTezosSdkApi
public sealed interface MetaScalarEncoded<out Self : MetaScalarEncoded<Self>> : MetaEncoded<Self> {
    override val encoded: ScalarEncoded

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaScalarEncoded<E>> : MetaEncoded.Kind<E>
}

// -- MetaEncryptedScalar --

@InternalTezosSdkApi
public sealed interface MetaEncryptedScalarEncoded<out Self : MetaEncryptedScalarEncoded<Self>> : MetaEncoded<Self> {
    override val encoded: EncryptedScalarEncoded

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaEncryptedScalarEncoded<E>> : MetaEncoded.Kind<E>
}