package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress

// -- Address <- ByteArray --

public fun Address.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Address =
    Address.fromBytes(bytes, tezos.coreModule.dependencyRegistry.bytesToAddressConverter)

@InternalTezosSdkApi
public fun Address.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, Address>): Address =
    converter.convert(bytes)

// -- Address <- String --

public fun Address.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): Address =
    Address.fromString(string, tezos.coreModule.dependencyRegistry.stringToAddressConverter)

@InternalTezosSdkApi
public fun Address.Companion.fromString(string: String, converter: Converter<String, Address>): Address =
    converter.convert(string)

// -- ImplicitAddress <- ByteArray --

public fun ImplicitAddress.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ImplicitAddress =
    ImplicitAddress.fromBytes(bytes, tezos.coreModule.dependencyRegistry.bytesToImplicitAddressConverter)

@InternalTezosSdkApi
public fun ImplicitAddress.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, ImplicitAddress>): ImplicitAddress =
    converter.convert(bytes)

// -- ImplicitAddress <- String --

public fun ImplicitAddress.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): ImplicitAddress =
    ImplicitAddress.fromString(string, tezos.coreModule.dependencyRegistry.stringToImplicitAddressConverter)

@InternalTezosSdkApi
public fun ImplicitAddress.Companion.fromString(string: String, converter: Converter<String, ImplicitAddress>): ImplicitAddress =
    converter.convert(string)