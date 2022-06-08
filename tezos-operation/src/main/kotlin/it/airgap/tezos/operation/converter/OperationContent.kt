package it.airgap.tezos.operation.converter

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.internal.context.withTezosContext
import it.airgap.tezos.operation.internal.operationModule

/**
 * Creates [operation content kind][OperationContent.Kind] from [ByteArray][value] or returns `null` if the [value] is invalid.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun OperationContent.Kind.Companion.fromTagOrNull(value: UByte, tezos: Tezos = Tezos.Default): OperationContent.Kind? = withTezosContext {
    fromTagOrNull(value, tezos.operationModule.dependencyRegistry.tagToOperationContentKindConverter)
}

@InternalTezosSdkApi
public interface OperationContentConverterContext {
    public fun OperationContent.Kind.Companion.fromTagOrNull(value: UByte, tagToOperationContentKindConverter: Converter<UByte, OperationContent.Kind>): OperationContent.Kind? =
        runCatching { tagToOperationContentKindConverter.convert(value) }.getOrNull()
}
