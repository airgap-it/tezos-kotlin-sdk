package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.PublicKey
import it.airgap.tezos.core.type.encoded.PublicKeyHash

/**
 * Creates [PublicKey] from [string].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/PublicKey/PublicKeySamples.Usage#create` for a sample usage.
 */
public fun PublicKey(string: String, tezos: Tezos = Tezos.Default): PublicKey = withTezosContext {
    PublicKey.fromString(string, tezos.coreModule.dependencyRegistry.stringToPublicKeyConverter)
}

/**
 * Creates [PublicKeyHash] from the public key.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun PublicKey.createHash(tezos: Tezos = Tezos.Default): PublicKeyHash = withTezosContext {
    toPublicKeyHash(tezos.coreModule.dependencyRegistry.publicKeyToPublicKeyHashConverter)
}


@InternalTezosSdkApi
public interface PublicKeyConverterContext {
    public fun PublicKey.Companion.fromString(string: String, converter: Converter<String, PublicKey>): PublicKey =
        converter.convert(string)

    public fun PublicKey.toPublicKeyHash(converter: Converter<PublicKey, PublicKeyHash>): PublicKeyHash =
        converter.convert(this)
}