package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.Signature

// -- Signature <- ByteArray --

public fun Signature.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Signature =
    Signature.fromBytes(bytes, tezos.coreModule.dependencyRegistry.bytesToSignatureConverter)

@InternalTezosSdkApi
public fun Signature.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, Signature>): Signature =
    converter.convert(bytes)

// -- Signature <- String --

public fun Signature.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): Signature =
    Signature.fromString(string, tezos.coreModule.dependencyRegistry.stringToSignatureConverter)

@InternalTezosSdkApi
public fun Signature.Companion.fromString(string: String, converter: Converter<String, Signature>): Signature =
    converter.convert(string)

// -- Signature -> GenericSignature --

public fun <T : Signature> T.toGenericSignature(tezos: Tezos = Tezos.Default): GenericSignature =
    toGenericSignature(tezos.coreModule.dependencyRegistry.signatureToGenericSignatureConverter)

@InternalTezosSdkApi
public fun <T : Signature> T.toGenericSignature(converter: Converter<Signature, GenericSignature>): GenericSignature =
    converter.convert(this)