package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress

/**
 * Encodes an [Address] to [ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Address/AddressSamples.Coding#toBytes` for a sample usage.
 */
public fun Address.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.addressBytesCoder)
}

/**
 * Decodes an [Address] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Address/AddressSamples.Coding#fromBytes` for a sample usage.
 */
public fun Address.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Address = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.addressBytesCoder)
}

@InternalTezosSdkApi
public interface AddressCoderContext {
    public fun Address.encodeToBytes(addressBytesCoder: ConsumingBytesCoder<Address>): ByteArray =
        addressBytesCoder.encode(this)

    public fun Address.Companion.decodeFromBytes(bytes: ByteArray, addressBytesCoder: ConsumingBytesCoder<Address>): Address =
        addressBytesCoder.decode(bytes)

    public fun Address.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, addressBytesCoder: ConsumingBytesCoder<Address>): Address =
        addressBytesCoder.decodeConsuming(bytes)
}

/**
 * Encodes an [ImplicitAddress] to [ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Address/ImplicitAddressSamples.Coding#toBytes` for a sample usage.
 */
public fun ImplicitAddress.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.implicitAddressBytesCoder)
}

/**
 * Decodes an [ImplicitAddress] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Address/ImplicitAddressSamples.Coding#fromBytes` for a sample usage.
 */
public fun ImplicitAddress.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ImplicitAddress = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.implicitAddressBytesCoder)
}

@InternalTezosSdkApi
public interface ImplicitAddressCoderContext {
    public fun ImplicitAddress.encodeToBytes(implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>): ByteArray =
        implicitAddressBytesCoder.encode(this)

    public fun ImplicitAddress.Companion.decodeFromBytes(bytes: ByteArray, implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>): ImplicitAddress =
        implicitAddressBytesCoder.decode(bytes)

    public fun ImplicitAddress.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>): ImplicitAddress =
        implicitAddressBytesCoder.decodeConsuming(bytes)
}
