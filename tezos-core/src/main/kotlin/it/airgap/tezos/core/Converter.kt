package it.airgap.tezos.core

import it.airgap.tezos.core.internal.converter.*
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.toBigInt
import it.airgap.tezos.core.internal.utils.toMutez
import it.airgap.tezos.core.internal.utils.toNanotez
import it.airgap.tezos.core.internal.utils.toTez
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.core.type.tez.Tez

// -- Address <- Address --

public fun Address.Companion.fromBytes(
    bytes: ByteArray,
    bytesToAddressConverter: BytesToAddressConverter = TezosSdk.instance.dependencyRegistry.core().bytesToAddressConverter)
: Address = bytesToAddressConverter.convert(bytes).encoded

// -- Address <- String --

public fun Address.Companion.fromString(
    string: String,
    stringToAddressConverter: StringToAddressConverter = TezosSdk.instance.dependencyRegistry.core().stringToAddressConverter
): Address = stringToAddressConverter.convert(string).encoded

// -- ImplicitAddress <- ByteArray --

public fun ImplicitAddress.Companion.fromBytes(
    bytes: ByteArray,
    bytesToImplicitAddressConverter: BytesToImplicitAddressConverter = TezosSdk.instance.dependencyRegistry.core().bytesToImplicitAddressConverter
): ImplicitAddress = bytesToImplicitAddressConverter.convert(bytes).encoded

// -- ImplicitAddress <- String --

public fun ImplicitAddress.Companion.fromString(
    string: String,
    stringToImplicitAddressConverter: StringToImplicitAddressConverter = TezosSdk.instance.dependencyRegistry.core().stringToImplicitAddressConverter
): ImplicitAddress = stringToImplicitAddressConverter.convert(string).encoded

// -- PublicKeyEncoded <- ByteArray --

public fun PublicKeyEncoded.Companion.fromBytes(
    bytes: ByteArray,
    bytesToPublicKeyConverter: BytesToPublicKeyConverter = TezosSdk.instance.dependencyRegistry.core().bytesToPublicKeyConverter
): PublicKeyEncoded = bytesToPublicKeyConverter.convert(bytes).encoded

// -- PublicKeyEncoded <- String --

public fun PublicKeyEncoded.Companion.fromString(
    string: String,
    stringToPublicKeyConverter: StringToPublicKeyConverter = TezosSdk.instance.dependencyRegistry.core().stringToPublicKeyConverter
): PublicKeyEncoded = stringToPublicKeyConverter.convert(string).encoded

// -- PublicKeyHashEncoded <- ByteArray --

public fun PublicKeyHashEncoded.Companion.fromBytes(
    bytes: ByteArray,
    bytesToPublicKeyHashConverter: BytesToPublicKeyHashConverter = TezosSdk.instance.dependencyRegistry.core().bytesToPublicKeyHashConverter
): PublicKeyHashEncoded = bytesToPublicKeyHashConverter.convert(bytes).encoded

// -- PublicKeyHashEncoded <- String --

public fun PublicKeyHashEncoded.Companion.fromString(
    string: String,
    stringToPublicKeyHashConverter: StringToPublicKeyHashConverter = TezosSdk.instance.dependencyRegistry.core().stringToPublicKeyHashConverter
): PublicKeyHashEncoded = stringToPublicKeyHashConverter.convert(string).encoded

// -- BlindedPublicKeyHashEncoded <- ByteArray --

public fun BlindedPublicKeyHashEncoded.Companion.fromBytes(
    bytes: ByteArray,
    bytesToBlindedPublicKeyHashConverter: BytesToBlindedPublicKeyHashConverter = TezosSdk.instance.dependencyRegistry.core().bytesToBlindedPublicKeyHashConverter
): BlindedPublicKeyHashEncoded = bytesToBlindedPublicKeyHashConverter.convert(bytes).encoded

// -- BlindedPublicKeyHashEncoded <- String --

public fun BlindedPublicKeyHashEncoded.Companion.fromString(
    string: String,
    stringToBlindedPublicKeyHashConverter: StringToBlindedPublicKeyHashConverter = TezosSdk.instance.dependencyRegistry.core().stringToBlindedPublicKeyHashConverter
): BlindedPublicKeyHashEncoded = stringToBlindedPublicKeyHashConverter.convert(string).encoded

// -- SignatureEncoded <- ByteArray --

public fun SignatureEncoded.Companion.fromBytes(
    bytes: ByteArray,
    bytesToSignatureConverter: BytesToSignatureConverter = TezosSdk.instance.dependencyRegistry.core().bytesToSignatureConverter
): SignatureEncoded = bytesToSignatureConverter.convert(bytes).encoded

// -- SignatureEncoded <- String --

public fun SignatureEncoded.Companion.fromString(
    string: String,
    stringToSignatureConverter: StringToSignatureConverter = TezosSdk.instance.dependencyRegistry.core().stringToSignatureConverter
): SignatureEncoded = stringToSignatureConverter.convert(string).encoded

// -- SignatureEncoded -> GenericSignature --

public fun <T : SignatureEncoded> T.toGenericSignature(
    signatureToGenericSignatureConverter: SignatureToGenericSignatureConverter = TezosSdk.instance.dependencyRegistry.core().signatureToGenericSignatureConverter,
): GenericSignature = signatureToGenericSignatureConverter.convert(meta)

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

// -- Tez -> Mutez --

public fun Tez.toMutez(): Mutez {
    val mutezValue = toBigInt() * 1_000_000
    return mutezValue.toMutez()
}

// -- Tez -> Nanotez --

public fun Tez.toNanotez(): Nanotez {
    val nanotezValue = toBigInt() * 1_000_000_000
    return nanotezValue.toNanotez()
}

// -- Mutez -> Tez --

public fun Mutez.toTez(): Tez {
    val tezValue = toBigInt().div(1_000_000, BigInt.RoundingMode.Up)
    return tezValue.toTez()
}

// -- Mutez -> Nanotez --

public fun Mutez.toNanotez(): Nanotez {
    val nanotezValue = toBigInt() * 1_000
    return nanotezValue.toNanotez()
}

// -- Nanotez -> Tez --

public fun Nanotez.toTez(): Tez {
    val tezValue = toBigInt().div(1_000_000_000, BigInt.RoundingMode.Up)
    return tezValue.toTez()
}

// -- Nanotez -> Mutez --

public fun Nanotez.toMutez(): Mutez {
    val mutezValue = toBigInt().div(1_000, BigInt.RoundingMode.Up)
    return mutezValue.toMutez()
}