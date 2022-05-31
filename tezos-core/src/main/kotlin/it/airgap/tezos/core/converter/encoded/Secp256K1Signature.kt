package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.Secp256K1Signature

// -- Secp256K1Signature <- GenericSignature --

public fun Secp256K1Signature.Companion.fromGenericSignature(genericSignature: GenericSignature, tezos: Tezos = Tezos.Default): Secp256K1Signature = withTezosContext {
    Secp256K1Signature.fromGenericSignature(genericSignature, tezos.coreModule.dependencyRegistry.genericSignatureToSecp256K1SignatureConverter)
}

@InternalTezosSdkApi
public interface Secp256K1SignatureConverterContext {
    public fun Secp256K1Signature.Companion.fromGenericSignature(genericSignature: GenericSignature, converter: Converter<GenericSignature, Secp256K1Signature>): Secp256K1Signature =
        converter.convert(genericSignature)
}