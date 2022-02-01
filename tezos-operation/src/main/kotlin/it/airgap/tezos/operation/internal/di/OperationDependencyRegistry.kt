package it.airgap.tezos.operation.internal.di

import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.operation.internal.coder.OperationBytesCoder
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.operation.internal.converter.TagToOperationContentKindConverter

internal class OperationDependencyRegistry(dependencyRegistry: DependencyRegistry) : ScopedDependencyRegistry, DependencyRegistry by dependencyRegistry {

    // -- coder --

    override val operationBytesCoder: OperationBytesCoder by lazy { OperationBytesCoder(operationContentBytesCoder, base58BytesCoder) }
    override val operationContentBytesCoder: OperationContentBytesCoder by lazy {
        OperationContentBytesCoder(
            base58BytesCoder,
            addressBytesCoder,
            keyBytesCoder,
            keyHashBytesCoder,
            signatureBytesCoder,
            zarithNaturalNumberBytesCoder,
            tagToOperationContentKindConverter,
        )
    }

    // -- converter --

    override val tagToOperationContentKindConverter: TagToOperationContentKindConverter = TagToOperationContentKindConverter()
}