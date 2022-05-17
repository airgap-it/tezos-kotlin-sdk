package it.airgap.tezos.operation.converter

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.internal.operation

// -- UByte -> OperationContent.Kind --

public fun OperationContent.Kind.Companion.fromTagOrNull(value: UByte, tezos: Tezos = Tezos.Default): OperationContent.Kind? =
    fromTagOrNull(value, tezos.operation().dependencyRegistry.tagToOperationContentKindConverter)

@InternalTezosSdkApi
public fun OperationContent.Kind.Companion.fromTagOrNull(value: UByte, tagToOperationContentKindConverter: Converter<UByte, OperationContent.Kind>): OperationContent.Kind? =
    runCatching { tagToOperationContentKindConverter.convert(value) }.getOrNull()
