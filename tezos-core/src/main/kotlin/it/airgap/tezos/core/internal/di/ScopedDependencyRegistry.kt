package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.core.type.tez.Tez
import it.airgap.tezos.core.type.zarith.ZarithInteger
import it.airgap.tezos.core.type.zarith.ZarithNatural

@InternalTezosSdkApi
public interface ScopedDependencyRegistry : DependencyRegistry {

    // -- coder --

    public val encodedBytesCoder: EncodedBytesCoder

    public val addressBytesCoder: ConsumingBytesCoder<Address>
    public val implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>
    public val publicKeyBytesCoder: ConsumingBytesCoder<PublicKey>
    public val signatureBytesCoder: ConsumingBytesCoder<Signature>

    public val zarithNaturalBytesCoder: ConsumingBytesCoder<ZarithNatural>
    public val zarithIntegerBytesCoder: ConsumingBytesCoder<ZarithInteger>

    public val tezBytesCoder: ConsumingBytesCoder<Tez>
    public val mutezBytesCoder: ConsumingBytesCoder<Mutez>
    public val nanotezBytesCoder: ConsumingBytesCoder<Nanotez>

    public val timestampBigIntCoder: Coder<Timestamp, BigInt>

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
