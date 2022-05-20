package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.PublicKey

// -- PublicKey <- ByteArray --

public fun PublicKey.Companion.fromBytes(bytes: ByteArray,tezos: Tezos = Tezos.Default): PublicKey =
    PublicKey.fromBytes(bytes, tezos.coreModule.dependencyRegistry.bytesToPublicKeyConverter)

@InternalTezosSdkApi
public fun PublicKey.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, PublicKey>): PublicKey =
    converter.convert(bytes)

// -- PublicKey <- String --

public fun PublicKey.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): PublicKey =
    PublicKey.fromString(string, tezos.coreModule.dependencyRegistry.stringToPublicKeyConverter)

@InternalTezosSdkApi
public fun PublicKey.Companion.fromString(string: String, converter: Converter<String, PublicKey>): PublicKey =
    converter.convert(string)