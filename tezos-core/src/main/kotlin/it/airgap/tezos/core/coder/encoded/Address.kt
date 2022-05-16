package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress

// -- Address <-> ByteArray --

public fun Address.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().addressBytesCoder)

public fun Address.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Address =
    Address.decodeFromBytes(bytes, tezos.dependencyRegistry.core().addressBytesCoder)

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
    encodeToBytes(tezos.dependencyRegistry.core().implicitAddressBytesCoder)

public fun ImplicitAddress.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ImplicitAddress =
    ImplicitAddress.decodeFromBytes(bytes, tezos.dependencyRegistry.core().implicitAddressBytesCoder)

@InternalTezosSdkApi
public fun ImplicitAddress.encodeToBytes(implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>): ByteArray =
    implicitAddressBytesCoder.encode(this)

@InternalTezosSdkApi
public fun ImplicitAddress.Companion.decodeFromBytes(bytes: ByteArray, implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>): ImplicitAddress =
    implicitAddressBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun ImplicitAddress.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>): ImplicitAddress =
    implicitAddressBytesCoder.decodeConsuming(bytes)
