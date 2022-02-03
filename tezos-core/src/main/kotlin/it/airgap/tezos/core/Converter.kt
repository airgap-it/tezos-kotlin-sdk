package it.airgap.tezos.core

import it.airgap.tezos.core.internal.converter.*
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.PublicKeyEncoded
import it.airgap.tezos.core.type.encoded.SignatureEncoded

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
    bytesToPublicKeyEncodedConverter: BytesToPublicKeyEncodedConverter = TezosSdk.instance.dependencyRegistry.core().bytesToPublicKeyEncodedConverter
): PublicKeyEncoded<*> = bytesToPublicKeyEncodedConverter.convert(bytes)

// -- PublicKeyEncoded <- String --

public fun PublicKeyEncoded.Companion.fromString(
    string: String,
    stringToPublicKeyEncodedConverter: StringToPublicKeyEncodedConverter = TezosSdk.instance.dependencyRegistry.core().stringToPublicKeyEncodedConverter
): PublicKeyEncoded<*> = stringToPublicKeyEncodedConverter.convert(string)

// -- SignatureEncoded <- ByteArray --

public fun SignatureEncoded.Companion.fromBytes(
    bytes: ByteArray,
    bytesToSignatureEncodedConverter: BytesToSignatureEncodedConverter = TezosSdk.instance.dependencyRegistry.core().bytesToSignatureEncodedConverter
): SignatureEncoded<*> = bytesToSignatureEncodedConverter.convert(bytes)

// -- SignatureEncoded <- String --

public fun SignatureEncoded.Companion.fromString(
    string: String,
    stringToSignatureEncodedConverter: StringToSignatureEncodedConverter = TezosSdk.instance.dependencyRegistry.core().stringToSignatureEncodedConverter
): SignatureEncoded<*> = stringToSignatureEncodedConverter.convert(string)