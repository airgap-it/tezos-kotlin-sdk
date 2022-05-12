package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.*
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.MetaAddress
import it.airgap.tezos.core.type.encoded.MetaImplicitAddress

// -- Address <- ByteArray --

public fun Address.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Address =
    Address.fromBytes(bytes, tezos.dependencyRegistry.core().bytesToAddressConverter)

@InternalTezosSdkApi
public fun Address.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, MetaAddress<*>>): Address =
    converter.convert(bytes).encoded

// -- Address <- String --

public fun Address.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): Address =
    Address.fromString(string, tezos.dependencyRegistry.core().stringToAddressConverter)

@InternalTezosSdkApi
public fun Address.Companion.fromString(string: String, converter: Converter<String, MetaAddress<*>>): Address =
    converter.convert(string).encoded

// -- ImplicitAddress <- ByteArray --

public fun ImplicitAddress.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ImplicitAddress =
    ImplicitAddress.fromBytes(bytes, tezos.dependencyRegistry.core().bytesToImplicitAddressConverter)

@InternalTezosSdkApi
public fun ImplicitAddress.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, MetaImplicitAddress<*>>): ImplicitAddress =
    converter.convert(bytes).encoded

// -- ImplicitAddress <- String --

public fun ImplicitAddress.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): ImplicitAddress =
    ImplicitAddress.fromString(string, tezos.dependencyRegistry.core().stringToImplicitAddressConverter)

@InternalTezosSdkApi
public fun ImplicitAddress.Companion.fromString(string: String, converter: Converter<String, MetaImplicitAddress<*>>): ImplicitAddress =
    converter.convert(string).encoded