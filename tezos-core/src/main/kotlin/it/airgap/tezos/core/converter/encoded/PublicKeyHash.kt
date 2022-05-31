package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.PublicKeyHash

// -- PublicKeyHash <- ByteArray --

public fun PublicKeyHash.Companion.fromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): PublicKeyHash = withTezosContext {
    PublicKeyHash.fromBytes(bytes, tezos.coreModule.dependencyRegistry.bytesToPublicKeyHashConverter)
}

// -- PublicKeyHash <- String --

public fun PublicKeyHash.Companion.fromString(string: String, tezos: Tezos = Tezos.Default): PublicKeyHash = withTezosContext {
    PublicKeyHash.fromString(string, tezos.coreModule.dependencyRegistry.stringToPublicKeyHashConverter)
}

@InternalTezosSdkApi
public interface PublicKeyHashConverterContext {
    public fun PublicKeyHash.Companion.fromBytes(bytes: ByteArray, converter: Converter<ByteArray, PublicKeyHash>): PublicKeyHash =
        converter.convert(bytes)

    public fun PublicKeyHash.Companion.fromString(string: String, converter: Converter<String, PublicKeyHash>): PublicKeyHash =
        converter.convert(string)
}