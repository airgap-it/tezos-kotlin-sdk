package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.*
import it.airgap.tezos.core.internal.converter.*

@InternalTezosSdkApi
public interface ScopedDependencyRegistry : DependencyRegistry {

    // -- coder --

    public val encodedBytesCoder: EncodedBytesCoder

    public val addressBytesCoder: AddressBytesCoder
    public val implicitAddressBytesCoder: ImplicitAddressBytesCoder

    public val publicKeyBytesCoder: PublicKeyBytesCoder
    public val signatureBytesCoder: SignatureBytesCoder

    public val zarithNaturalBytesCoder: ZarithNaturalBytesCoder
    public val zarithIntegerBytesCoder: ZarithIntegerBytesCoder

    // -- converter --

    public val bytesToAddressConverter: BytesToAddressConverter
    public val stringToAddressConverter: StringToAddressConverter

    public val bytesToImplicitAddressConverter: BytesToImplicitAddressConverter
    public val stringToImplicitAddressConverter: StringToImplicitAddressConverter

    public val bytesToPublicKeyEncodedConverter: BytesToPublicKeyEncodedConverter
    public val stringToPublicKeyEncodedConverter: StringToPublicKeyEncodedConverter

    public val bytesToSignatureEncodedConverter: BytesToSignatureEncodedConverter
    public val stringToSignatureEncodedConverter: StringToSignatureEncodedConverter
}

@InternalTezosSdkApi
public fun DependencyRegistry.core(): ScopedDependencyRegistry =
    if (this is ScopedDependencyRegistry) this
    else findScoped() ?: CoreScopedDependencyRegistry(this).also { addScoped(it) }