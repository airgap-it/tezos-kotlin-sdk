package it.airgap.tezos.operation.internal.di

import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.di.findScoped
import it.airgap.tezos.operation.internal.coder.OperationBytesCoder
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.operation.internal.converter.TagToOperationContentKindConverter

internal interface ScopedDependencyRegistry : DependencyRegistry {

    // -- coder --

    val operationBytesCoder: OperationBytesCoder
    val operationContentBytesCoder: OperationContentBytesCoder

    // -- converter --

    val tagToOperationContentKindConverter: TagToOperationContentKindConverter
}

internal fun DependencyRegistry.scoped(): ScopedDependencyRegistry =
    if (this is ScopedDependencyRegistry) this
    else findScoped<OperationDependencyRegistry>() ?: OperationDependencyRegistry(this).also { addScoped(it) }