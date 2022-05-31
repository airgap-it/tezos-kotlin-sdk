package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress

// -- Address <- ByteArray --

public fun Address.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Address = withTezosContext {
    Address.fromBytes(bytes, tezos.coreModule.dependencyRegistry.bytesToAddressConverter)
}

// -- Address <- String --

public fun Address.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): Address = withTezosContext {
    Address.fromString(string, tezos.coreModule.dependencyRegistry.stringToAddressConverter)
}

// -- ImplicitAddress <- ByteArray --

public fun ImplicitAddress.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ImplicitAddress = withTezosContext {
    ImplicitAddress.fromBytes(bytes, tezos.coreModule.dependencyRegistry.bytesToImplicitAddressConverter)
}

// -- ImplicitAddress <- String --

public fun ImplicitAddress.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): ImplicitAddress = withTezosContext {
    ImplicitAddress.fromString(string, tezos.coreModule.dependencyRegistry.stringToImplicitAddressConverter)
}

@InternalTezosSdkApi
public interface AddressConverterContext {
    public fun Address.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, Address>): Address =
        converter.convert(bytes)

    public fun Address.Companion.fromString(string: String, converter: Converter<String, Address>): Address =
        converter.convert(string)
}

@InternalTezosSdkApi
public interface ImplicitAddressConverterContext {
    public fun ImplicitAddress.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, ImplicitAddress>): ImplicitAddress =
        converter.convert(bytes)

    public fun ImplicitAddress.Companion.fromString(string: String, converter: Converter<String, ImplicitAddress>): ImplicitAddress =
        converter.convert(string)
}