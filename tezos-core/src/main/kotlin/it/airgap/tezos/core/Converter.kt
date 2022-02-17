package it.airgap.tezos.core

import it.airgap.tezos.core.internal.converter.*
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.*

// -- Address <- Address --

public fun Address.Companion.fromBytes(
    bytes: ByteArray,
    bytesToAddressConverter: BytesToAddressConverter = TezosSdk.instance.dependencyRegistry.core().bytesToAddressConverter)
: Address<*> = bytesToAddressConverter.convert(bytes)

// -- Address <- String --

public fun Address.Companion.fromString(
    string: String,
    stringToAddressConverter: StringToAddressConverter = TezosSdk.instance.dependencyRegistry.core().stringToAddressConverter
): Address<*> = stringToAddressConverter.convert(string)

// -- ImplicitAddress <- ByteArray --

public fun ImplicitAddress.Companion.fromBytes(
    bytes: ByteArray,
    bytesToImplicitAddressConverter: BytesToImplicitAddressConverter = TezosSdk.instance.dependencyRegistry.core().bytesToImplicitAddressConverter
): ImplicitAddress<*> = bytesToImplicitAddressConverter.convert(bytes)

// -- ImplicitAddress <- String --

public fun ImplicitAddress.Companion.fromString(
    string: String,
    stringToImplicitAddressConverter: StringToImplicitAddressConverter = TezosSdk.instance.dependencyRegistry.core().stringToImplicitAddressConverter
): ImplicitAddress<*> = stringToImplicitAddressConverter.convert(string)

// -- PublicKeyEncoded <- ByteArray --

public fun PublicKeyEncoded.Companion.fromBytes(
    bytes: ByteArray,
    bytesToPublicKeyConverter: BytesToPublicKeyConverter = TezosSdk.instance.dependencyRegistry.core().bytesToPublicKeyConverter
): PublicKeyEncoded<*> = bytesToPublicKeyConverter.convert(bytes)

// -- PublicKeyEncoded <- String --

public fun PublicKeyEncoded.Companion.fromString(
    string: String,
    stringToPublicKeyConverter: StringToPublicKeyConverter = TezosSdk.instance.dependencyRegistry.core().stringToPublicKeyConverter
): PublicKeyEncoded<*> = stringToPublicKeyConverter.convert(string)

// -- SignatureEncoded <- ByteArray --

public fun SignatureEncoded.Companion.fromBytes(
    bytes: ByteArray,
    bytesToSignatureConverter: BytesToSignatureConverter = TezosSdk.instance.dependencyRegistry.core().bytesToSignatureConverter
): SignatureEncoded<*> = bytesToSignatureConverter.convert(bytes)

// -- SignatureEncoded <- String --

public fun SignatureEncoded.Companion.fromString(
    string: String,
    stringToSignatureConverter: StringToSignatureConverter = TezosSdk.instance.dependencyRegistry.core().stringToSignatureConverter
): SignatureEncoded<*> = stringToSignatureConverter.convert(string)

// -- SignatureEncoded -> GenericSignature --

public fun <T : SignatureEncoded<T>> T.toGenericSignature(
    signatureToGenericSignatureConverter: SignatureToGenericSignatureConverter = TezosSdk.instance.dependencyRegistry.core().signatureToGenericSignatureConverter,
): GenericSignature = signatureToGenericSignatureConverter.convert(this)

// -- Ed25519Signature <- GenericSignature --

public fun Ed25519Signature.Companion.fromGenericSignature(
    genericSignature: GenericSignature,
    genericSignatureToEd25519SignatureConverter: GenericSignatureToEd25519SignatureConverter = TezosSdk.instance.dependencyRegistry.core().genericSignatureToEd25519SignatureConverter,
): Ed25519Signature = genericSignatureToEd25519SignatureConverter.convert(genericSignature)

// -- Secp256K1Signature <- GenericSignature --

public fun Secp256K1Signature.Companion.fromGenericSignature(
    genericSignature: GenericSignature,
    genericSignatureToSecp256K1SignatureConverter: GenericSignatureToSecp256K1SignatureConverter = TezosSdk.instance.dependencyRegistry.core().genericSignatureToSecp256K1SignatureConverter,
): Secp256K1Signature = genericSignatureToSecp256K1SignatureConverter.convert(genericSignature)

// -- P256Signature <- GenericSignature --

public fun P256Signature.Companion.fromGenericSignature(
    genericSignature: GenericSignature,
    genericSignatureToP256SignatureConverter: GenericSignatureToP256SignatureConverter = TezosSdk.instance.dependencyRegistry.core().genericSignatureToP256SignatureConverter,
): P256Signature = genericSignatureToP256SignatureConverter.convert(genericSignature)
