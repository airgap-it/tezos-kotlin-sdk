package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.MetaBlindedPublicKeyHash

// -- BlindedPublicKeyHash <- ByteArray --

public fun BlindedPublicKeyHash.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): BlindedPublicKeyHash =
    BlindedPublicKeyHash.fromBytes(bytes, tezos.dependencyRegistry.core().bytesToBlindedPublicKeyHashConverter)

@InternalTezosSdkApi
public fun BlindedPublicKeyHash.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, MetaBlindedPublicKeyHash<*>>): BlindedPublicKeyHash =
    converter.convert(bytes).encoded

// -- BlindedPublicKeyHash <- String --

public fun BlindedPublicKeyHash.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): BlindedPublicKeyHash =
    BlindedPublicKeyHash.fromString(string, tezos.dependencyRegistry.core().stringToBlindedPublicKeyHashConverter)

@InternalTezosSdkApi
public fun BlindedPublicKeyHash.Companion.fromString(string: String, converter: Converter<String, MetaBlindedPublicKeyHash<*>>): BlindedPublicKeyHash =
    converter.convert(string).encoded