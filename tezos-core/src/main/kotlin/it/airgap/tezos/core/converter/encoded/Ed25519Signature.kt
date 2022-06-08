package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.Ed25519Signature
import it.airgap.tezos.core.type.encoded.GenericSignature

/**
 * Creates [Ed25519Signature] from [genericSignature].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Ed25519Signature/Ed25519SignatureSamples.Usage#fromGeneric` for a sample usage.
 */
public fun Ed25519Signature.Companion.fromGenericSignature(genericSignature: GenericSignature, tezos: Tezos = Tezos.Default): Ed25519Signature = withTezosContext {
    Ed25519Signature.fromGenericSignature(genericSignature, tezos.coreModule.dependencyRegistry.genericSignatureToEd25519SignatureConverter)
}

@InternalTezosSdkApi
public interface Ed25519SignatureConverterContext {
    public fun Ed25519Signature.Companion.fromGenericSignature(genericSignature: GenericSignature, converter: Converter<GenericSignature, Ed25519Signature>): Ed25519Signature =
        converter.convert(genericSignature)
}