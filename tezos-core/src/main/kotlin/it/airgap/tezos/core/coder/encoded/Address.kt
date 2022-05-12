package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.AddressBytesCoder
import it.airgap.tezos.core.internal.coder.ImplicitAddressBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress

// -- Address <-> ByteArray --

public fun Address.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().addressBytesCoder)

public fun Address.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Address =
    Address.decodeFromBytes(bytes, tezos.dependencyRegistry.core().addressBytesCoder)

@InternalTezosSdkApi
public fun Address.encodeToBytes(addressBytesCoder: AddressBytesCoder): ByteArray =
    addressBytesCoder.encode(meta)

@InternalTezosSdkApi
public fun Address.Companion.decodeFromBytes(bytes: ByteArray, addressBytesCoder: AddressBytesCoder): Address =
    addressBytesCoder.decode(bytes).encoded

@InternalTezosSdkApi
public fun Address.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, addressBytesCoder: AddressBytesCoder): Address =
    addressBytesCoder.decodeConsuming(bytes).encoded

// -- ImplicitAddress <-> ByteArray --

public fun ImplicitAddress.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().implicitAddressBytesCoder)

public fun ImplicitAddress.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ImplicitAddress =
    ImplicitAddress.decodeFromBytes(bytes, tezos.dependencyRegistry.core().implicitAddressBytesCoder)

@InternalTezosSdkApi
public fun ImplicitAddress.encodeToBytes(implicitAddressBytesCoder: ImplicitAddressBytesCoder): ByteArray =
    implicitAddressBytesCoder.encode(meta)

@InternalTezosSdkApi
public fun ImplicitAddress.Companion.decodeFromBytes(bytes: ByteArray, implicitAddressBytesCoder: ImplicitAddressBytesCoder): ImplicitAddress =
    implicitAddressBytesCoder.decode(bytes).encoded

@InternalTezosSdkApi
public fun ImplicitAddress.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, implicitAddressBytesCoder: ImplicitAddressBytesCoder): ImplicitAddress =
    implicitAddressBytesCoder.decodeConsuming(bytes).encoded
