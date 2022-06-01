package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.PublicKeyHash

/**
 * Creates a [PublicKeyHash] from [string].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/PublicKeyHash/PublicKeyHashSamples.Usage#create` for a sample usage.
 */
public fun PublicKeyHash(string: String, tezos: Tezos = Tezos.Default): PublicKeyHash = withTezosContext {
    PublicKeyHash.fromString(string, tezos.coreModule.dependencyRegistry.stringToPublicKeyHashConverter)
}

@InternalTezosSdkApi
public interface PublicKeyHashConverterContext {
    public fun PublicKeyHash.Companion.fromString(string: String, converter: Converter<String, PublicKeyHash>): PublicKeyHash =
        converter.convert(string)
}