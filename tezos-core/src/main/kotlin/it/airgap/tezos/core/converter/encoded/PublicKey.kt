package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.PublicKey

/**
 * Creates a [PublicKey] from [string].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/PublicKey/PublicKeySamples.Usage#create` for a sample usage.
 */
public fun PublicKey(string: String, tezos: Tezos = Tezos.Default): PublicKey = withTezosContext {
    PublicKey.fromString(string, tezos.coreModule.dependencyRegistry.stringToPublicKeyConverter)
}


@InternalTezosSdkApi
public interface PublicKeyConverterContext {
    public fun PublicKey.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, PublicKey>): PublicKey =
        converter.convert(bytes)

    public fun PublicKey.Companion.fromString(string: String, converter: Converter<String, PublicKey>): PublicKey =
        converter.convert(string)
}