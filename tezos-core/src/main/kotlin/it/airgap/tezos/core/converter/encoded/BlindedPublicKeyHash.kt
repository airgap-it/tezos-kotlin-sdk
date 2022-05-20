package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlindedPublicKeyHash

// -- BlindedPublicKeyHash <- ByteArray --

public fun BlindedPublicKeyHash.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): BlindedPublicKeyHash =
    BlindedPublicKeyHash.fromBytes(bytes, tezos.coreModule.dependencyRegistry.bytesToBlindedPublicKeyHashConverter)

@InternalTezosSdkApi
public fun BlindedPublicKeyHash.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, BlindedPublicKeyHash>): BlindedPublicKeyHash =
    converter.convert(bytes)

// -- BlindedPublicKeyHash <- String --

public fun BlindedPublicKeyHash.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): BlindedPublicKeyHash =
    BlindedPublicKeyHash.fromString(string, tezos.coreModule.dependencyRegistry.stringToBlindedPublicKeyHashConverter)

@InternalTezosSdkApi
public fun BlindedPublicKeyHash.Companion.fromString(string: String, converter: Converter<String, BlindedPublicKeyHash>): BlindedPublicKeyHash =
    converter.convert(string)