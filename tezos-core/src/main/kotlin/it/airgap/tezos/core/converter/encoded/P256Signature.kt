package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.P256Signature

// -- P256Signature <- GenericSignature --

public fun P256Signature.Companion.fromGenericSignature(genericSignature: GenericSignature, tezos: Tezos = Tezos.Default): P256Signature = withTezosContext {
    P256Signature.fromGenericSignature(genericSignature, tezos.coreModule.dependencyRegistry.genericSignatureToP256SignatureConverter)
}

@InternalTezosSdkApi
public interface P256SignatureConverterContext {
    public fun P256Signature.Companion.fromGenericSignature(genericSignature: GenericSignature, converter: Converter<GenericSignature, P256Signature>): P256Signature =
        converter.convert(genericSignature)
}