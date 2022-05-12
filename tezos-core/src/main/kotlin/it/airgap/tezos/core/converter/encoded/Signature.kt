package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.MetaSignature
import it.airgap.tezos.core.type.encoded.Signature

// -- Signature <- ByteArray --

public fun Signature.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Signature =
    Signature.fromBytes(bytes, tezos.dependencyRegistry.core().bytesToSignatureConverter)

@InternalTezosSdkApi
public fun Signature.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, MetaSignature<*>>): Signature =
    converter.convert(bytes).encoded

// -- Signature <- String --

public fun Signature.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): Signature =
    Signature.fromString(string, tezos.dependencyRegistry.core().stringToSignatureConverter)

@InternalTezosSdkApi
public fun Signature.Companion.fromString(string: String, converter: Converter<String, MetaSignature<*>>): Signature =
    converter.convert(string).encoded

// -- Signature -> GenericSignature --

public fun <T : Signature> T.toGenericSignature(tezos: Tezos = Tezos.Default): GenericSignature =
    toGenericSignature(tezos.dependencyRegistry.core().signatureToGenericSignatureConverter)

@InternalTezosSdkApi
public fun <T : Signature> T.toGenericSignature(converter: Converter<MetaSignature<*>, GenericSignature>): GenericSignature =
    converter.convert(meta)