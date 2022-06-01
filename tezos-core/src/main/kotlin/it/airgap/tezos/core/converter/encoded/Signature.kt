package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.Signature

/**
 * Creates [Signature] from [string].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Signature/SignatureSamples.Usage#create` for a sample usage.
 */
public fun Signature(string: String, tezos: Tezos = Tezos.Default): Signature = withTezosContext {
    Signature.fromString(string, tezos.coreModule.dependencyRegistry.stringToSignatureConverter)
}

/**
 * Converts any [Signature] to [GenericSignature].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See:
 *  - `samples/src/test/kotlin/type/Ed25519Signature/Ed25519SignatureSamples.Usage#toGeneric`
 *  - `samples/src/test/kotlin/type/Secp256K1Signature/Secp256K1SignatureSamples.Usage#toGeneric`
 *  - `samples/src/test/kotlin/type/P256Signature/P256SignatureSamples.Usage#toGeneric`
 *  - `samples/src/test/kotlin/type/Signature/SignatureSamples.Usage#toGeneric`
 * for a sample usage.
 */
public fun <T : Signature> T.toGenericSignature(tezos: Tezos = Tezos.Default): GenericSignature = withTezosContext {
    toGenericSignature(tezos.coreModule.dependencyRegistry.signatureToGenericSignatureConverter)
}

@InternalTezosSdkApi
public interface SignatureConverterContext {
    public fun Signature.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, Signature>): Signature =
        converter.convert(bytes)

    public fun Signature.Companion.fromString(string: String, converter: Converter<String, Signature>): Signature =
        converter.convert(string)

    public fun <T : Signature> T.toGenericSignature(converter: Converter<Signature, GenericSignature>): GenericSignature =
        converter.convert(this)
}