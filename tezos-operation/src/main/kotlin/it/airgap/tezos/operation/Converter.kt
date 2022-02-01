package it.airgap.tezos.operation

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.operation.internal.converter.TagToOperationContentKindConverter
import it.airgap.tezos.operation.internal.di.scoped

// -- UByte -> OperationContent.Kind --

public fun OperationContent.Kind.Companion.fromTagOrNull(value: UByte): OperationContent.Kind? = fromTagOrNull(value, TezosSdk.instance.dependencyRegistry.scoped().tagToOperationContentKindConverter)
internal fun OperationContent.Kind.Companion.fromTagOrNull(value: UByte, tagToOperationContentKindConverter: TagToOperationContentKindConverter): OperationContent.Kind? =
    runCatching { tagToOperationContentKindConverter.convert(value) }.getOrNull()
