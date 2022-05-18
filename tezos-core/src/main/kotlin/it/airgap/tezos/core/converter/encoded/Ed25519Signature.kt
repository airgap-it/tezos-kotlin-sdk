package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Ed25519Signature
import it.airgap.tezos.core.type.encoded.GenericSignature

// -- Ed25519Signature <- GenericSignature --

public fun Ed25519Signature.Companion.fromGenericSignature(genericSignature: GenericSignature, tezos: Tezos = Tezos.Default): Ed25519Signature =
    Ed25519Signature.fromGenericSignature(genericSignature, tezos.coreModule.dependencyRegistry.genericSignatureToEd25519SignatureConverter)

@InternalTezosSdkApi
public fun Ed25519Signature.Companion.fromGenericSignature(genericSignature: GenericSignature, converter: Converter<GenericSignature, Ed25519Signature>): Ed25519Signature =
    converter.convert(genericSignature)