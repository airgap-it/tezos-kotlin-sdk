package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// -- Signature --

public sealed interface SignatureEncoded : Encoded {
    override val meta: MetaSignatureEncoded<*>

    public companion object {}
}

// -- MetaSignature --

@InternalTezosSdkApi
public sealed interface MetaSignatureEncoded<out Self : MetaSignatureEncoded<Self>> : MetaEncoded<Self> {
    override val encoded: SignatureEncoded

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaSignatureEncoded<E>> : MetaEncoded.Kind<E>
}
