package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.PublicKeyHash

// -- PublicKeyHash <- ByteArray --

public fun PublicKeyHash.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): PublicKeyHash =
    PublicKeyHash.fromBytes(bytes, tezos.dependencyRegistry.core().bytesToPublicKeyHashConverter)

@InternalTezosSdkApi
public fun PublicKeyHash.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, PublicKeyHash>): PublicKeyHash =
    converter.convert(bytes)

// -- PublicKeyHash <- String --

public fun PublicKeyHash.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): PublicKeyHash =
    PublicKeyHash.fromString(string, tezos.dependencyRegistry.core().stringToPublicKeyHashConverter)

@InternalTezosSdkApi
public fun PublicKeyHash.Companion.fromString(string: String, converter: Converter<String, PublicKeyHash>): PublicKeyHash =
    converter.convert(string)