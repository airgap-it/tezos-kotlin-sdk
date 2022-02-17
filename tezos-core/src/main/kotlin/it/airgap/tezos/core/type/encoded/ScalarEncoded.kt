package it.airgap.tezos.core.type.encoded

// -- Scalar --

public sealed interface ScalarEncoded<out Self : ScalarEncoded<Self>> : Encoded<Self> {
    public sealed interface Kind<out E : ScalarEncoded<E>> : Encoded.Kind<E>

    public companion object {}
}

// -- EncryptedScalar --

public sealed interface EncryptedScalarEncoded<out Self : EncryptedScalarEncoded<Self>> : Encoded<Self> {
    public sealed interface Kind<out E : EncryptedScalarEncoded<E>> : Encoded.Kind<E>

    public companion object {}
}