package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlindedPublicKeyHash

/**
 * Creates a [BlindedPublicKeyHash] from [string].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/BlindedPublicKeyHash/BlindedPublicKeyHashSamples.Usage#create` for a sample usage.
 */
public fun BlindedPublicKeyHash(string: String, tezos: Tezos = Tezos.Default): BlindedPublicKeyHash = withTezosContext {
    BlindedPublicKeyHash.fromString(string, tezos.coreModule.dependencyRegistry.stringToBlindedPublicKeyHashConverter)
}

@InternalTezosSdkApi
public interface BlindedPublicKeyHashConverterContext {
    public fun BlindedPublicKeyHash.Companion.fromString(string: String, converter: Converter<String, BlindedPublicKeyHash>): BlindedPublicKeyHash =
        converter.convert(string)
}