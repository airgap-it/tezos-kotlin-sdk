package it.airgap.tezos.core.type.encoded

// -- Address --

public sealed interface Address<out Self : Address<Self>> : Encoded<Self> {
    public sealed interface Kind<out E : Address<E>> : Encoded.Kind<E>

    public companion object {}
}

public sealed interface ImplicitAddress<out Self : ImplicitAddress<Self>> : Address<Self> {
    public sealed interface Kind<out E : ImplicitAddress<E>> : Address.Kind<E>

    public companion object {}
}

public sealed interface OriginatedAddress<out Self : OriginatedAddress<Self>> : Address<Self> {
    public sealed interface Kind<out E : OriginatedAddress<E>> : Address.Kind<E>

    public companion object {}
}