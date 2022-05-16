package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.Signature

// -- Signature <- ByteArray --

public fun Signature.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Signature =
    Signature.fromBytes(bytes, tezos.core().dependencyRegistry.bytesToSignatureConverter)

@InternalTezosSdkApi
public fun Signature.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, Signature>): Signature =
    converter.convert(bytes)

// -- Signature <- String --

public fun Signature.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): Signature =
    Signature.fromString(string, tezos.core().dependencyRegistry.stringToSignatureConverter)

@InternalTezosSdkApi
public fun Signature.Companion.fromString(string: String, converter: Converter<String, Signature>): Signature =
    converter.convert(string)

// -- Signature -> GenericSignature --

public fun <T : Signature> T.toGenericSignature(tezos: Tezos = Tezos.Default): GenericSignature =
    toGenericSignature(tezos.core().dependencyRegistry.signatureToGenericSignatureConverter)

@InternalTezosSdkApi
public fun <T : Signature> T.toGenericSignature(converter: Converter<Signature, GenericSignature>): GenericSignature =
    converter.convert(this)