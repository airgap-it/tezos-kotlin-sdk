package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.MetaPublicKey
import it.airgap.tezos.core.type.encoded.PublicKey

// -- PublicKey <- ByteArray --

public fun PublicKey.Companion.fromBytes(bytes: ByteArray,tezos: Tezos = Tezos.Default): PublicKey =
    PublicKey.fromBytes(bytes, tezos.dependencyRegistry.core().bytesToPublicKeyConverter)

@InternalTezosSdkApi
public fun PublicKey.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, MetaPublicKey<*>>): PublicKey =
    converter.convert(bytes).encoded

// -- PublicKey <- String --

public fun PublicKey.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): PublicKey =
    PublicKey.fromString(string, tezos.dependencyRegistry.core().stringToPublicKeyConverter)

@InternalTezosSdkApi
public fun PublicKey.Companion.fromString(string: String, converter: Converter<String, MetaPublicKey<*>>): PublicKey =
    converter.convert(string).encoded