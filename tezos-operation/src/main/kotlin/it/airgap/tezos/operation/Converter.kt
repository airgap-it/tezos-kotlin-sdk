package it.airgap.tezos.operation

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.operation.internal.converter.TagToOperationContentKindConverter
import it.airgap.tezos.operation.internal.di.operation

// -- UByte -> OperationContent.Kind --

public fun OperationContent.Kind.Companion.fromTagOrNull(
    value: UByte,
    tagToOperationContentKindConverter: TagToOperationContentKindConverter = TezosSdk.instance.dependencyRegistry.operation().tagToOperationContentKindConverter,
): OperationContent.Kind? =
    runCatching { tagToOperationContentKindConverter.convert(value) }.getOrNull()
