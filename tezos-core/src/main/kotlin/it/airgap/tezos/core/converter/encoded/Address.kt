package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress

/**
 * Creates [Address] from [string].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Address/AddressSamples.Usage#create` for a sample usage.
 */
public fun Address(string: String, tezos: Tezos = Tezos.Default): Address = withTezosContext {
    Address.fromString(string, tezos.coreModule.dependencyRegistry.stringToAddressConverter)
}

/**
 * Creates [ImplicitAddress] from [string].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Address/ImplicitAddressSamples.Usage#create` for a sample usage.
 */
public fun ImplicitAddress(string: String, tezos: Tezos = Tezos.Default): ImplicitAddress = withTezosContext {
    ImplicitAddress.fromString(string, tezos.coreModule.dependencyRegistry.stringToImplicitAddressConverter)
}

@InternalTezosSdkApi
public interface AddressConverterContext {
    public fun Address.Companion.fromString(string: String, converter: Converter<String, Address>): Address =
        converter.convert(string)
}

@InternalTezosSdkApi
public interface ImplicitAddressConverterContext {
    public fun ImplicitAddress.Companion.fromString(string: String, converter: Converter<String, ImplicitAddress>): ImplicitAddress =
        converter.convert(string)
}