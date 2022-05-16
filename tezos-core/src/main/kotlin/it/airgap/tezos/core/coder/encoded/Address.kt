package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress

// -- Address <-> ByteArray --

public fun Address.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.core().dependencyRegistry.addressBytesCoder)

public fun Address.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Address =
    decodeFromBytes(bytes, tezos.core().dependencyRegistry.addressBytesCoder)

@InternalTezosSdkApi
public fun Address.encodeToBytes(addressBytesCoder: ConsumingBytesCoder<Address>): ByteArray =
    addressBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Address.Companion.decodeFromBytes(bytes: ByteArray, addressBytesCoder: ConsumingBytesCoder<Address>): Address =
    addressBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun Address.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, addressBytesCoder: ConsumingBytesCoder<Address>): Address =
    addressBytesCoder.decodeConsuming(bytes)

// -- ImplicitAddress <-> ByteArray --

public fun ImplicitAddress.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.core().dependencyRegistry.implicitAddressBytesCoder)

public fun ImplicitAddress.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ImplicitAddress =
    decodeFromBytes(bytes, tezos.core().dependencyRegistry.implicitAddressBytesCoder)

@InternalTezosSdkApi
public fun ImplicitAddress.encodeToBytes(implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>): ByteArray =
    implicitAddressBytesCoder.encode(this)

@InternalTezosSdkApi
public fun ImplicitAddress.Companion.decodeFromBytes(bytes: ByteArray, implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>): ImplicitAddress =
    implicitAddressBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun ImplicitAddress.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>): ImplicitAddress =
    implicitAddressBytesCoder.decodeConsuming(bytes)
