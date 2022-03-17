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

    public val timestampBigIntCoder: TimestampBigIntCoder

    // -- converter --

    public val bytesToAddressConverter: BytesToAddressConverter
    public val stringToAddressConverter: StringToAddressConverter

    public val bytesToImplicitAddressConverter: BytesToImplicitAddressConverter
    public val stringToImplicitAddressConverter: StringToImplicitAddressConverter

    public val bytesToPublicKeyConverter: BytesToPublicKeyConverter
    public val stringToPublicKeyConverter: StringToPublicKeyConverter

    public val bytesToPublicKeyHashConverter: BytesToPublicKeyHashConverter
    public val stringToPublicKeyHashConverter: StringToPublicKeyHashConverter

    public val bytesToBlindedPublicKeyHashConverter: BytesToBlindedPublicKeyHashConverter
    public val stringToBlindedPublicKeyHashConverter: StringToBlindedPublicKeyHashConverter

    public val bytesToSignatureConverter: BytesToSignatureConverter
    public val stringToSignatureConverter: StringToSignatureConverter

    public val signatureToGenericSignatureConverter: SignatureToGenericSignatureConverter
    public val genericSignatureToEd25519SignatureConverter: GenericSignatureToEd25519SignatureConverter
    public val genericSignatureToSecp256K1SignatureConverter: GenericSignatureToSecp256K1SignatureConverter
    public val genericSignatureToP256SignatureConverter: GenericSignatureToP256SignatureConverter
}
