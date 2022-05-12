package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.*
import it.airgap.tezos.core.internal.converter.*
import it.airgap.tezos.core.type.encoded.*

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

    public val tezBytesCoder: TezBytesCoder
    public val mutezBytesCoder: MutezBytesCoder
    public val nanotezBytesCoder: NanotezBytesCoder

    public val timestampBigIntCoder: TimestampBigIntCoder

    // -- converter --

    public val bytesToAddressConverter: Converter<ByteArray, Address>
    public val stringToAddressConverter: Converter<String, Address>

    public val bytesToImplicitAddressConverter: Converter<ByteArray, ImplicitAddress>
    public val stringToImplicitAddressConverter: Converter<String, ImplicitAddress>

    public val bytesToPublicKeyConverter: Converter<ByteArray, PublicKey>
    public val stringToPublicKeyConverter: Converter<String, PublicKey>

    public val bytesToPublicKeyHashConverter: Converter<ByteArray, PublicKeyHash>
    public val stringToPublicKeyHashConverter: Converter<String, PublicKeyHash>

    public val bytesToBlindedPublicKeyHashConverter: Converter<ByteArray, BlindedPublicKeyHash>
    public val stringToBlindedPublicKeyHashConverter: Converter<String, BlindedPublicKeyHash>

    public val bytesToSignatureConverter: Converter<ByteArray, Signature>
    public val stringToSignatureConverter: Converter<String, Signature>

    public val signatureToGenericSignatureConverter: Converter<Signature, GenericSignature>
    public val genericSignatureToEd25519SignatureConverter: Converter<GenericSignature, Ed25519Signature>
    public val genericSignatureToSecp256K1SignatureConverter: Converter<GenericSignature, Secp256K1Signature>
    public val genericSignatureToP256SignatureConverter: Converter<GenericSignature, P256Signature>
}
