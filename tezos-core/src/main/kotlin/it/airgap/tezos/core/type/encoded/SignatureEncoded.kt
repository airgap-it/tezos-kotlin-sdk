package it.airgap.tezos.core.type.encoded

public sealed interface SignatureEncoded<out Self : SignatureEncoded<Self>> : Encoded<Self> {
    public sealed interface Kind<out E : SignatureEncoded<E>> : Encoded.Kind<E>

    public companion object {}
}
