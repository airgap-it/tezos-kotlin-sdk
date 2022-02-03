package it.airgap.tezos.operation.internal.di

import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.michelson.internal.di.michelson
import it.airgap.tezos.operation.internal.coder.OperationBytesCoder
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.operation.internal.converter.TagToOperationContentKindConverter

internal class OperationScopedDependencyRegistry(dependencyRegistry: DependencyRegistry) : ScopedDependencyRegistry, DependencyRegistry by dependencyRegistry {

    // -- coder --

    override val operationBytesCoder: OperationBytesCoder by lazy { OperationBytesCoder(operationContentBytesCoder, core().encodedBytesCoder) }
    override val operationContentBytesCoder: OperationContentBytesCoder by lazy {
        OperationContentBytesCoder(
            core().encodedBytesCoder,
            core().addressBytesCoder,
            core().publicKeyBytesCoder,
            core().implicitAddressBytesCoder,
            core().signatureBytesCoder,
            core().zarithNaturalBytesCoder,
            michelson().michelineBytesCoder,
            tagToOperationContentKindConverter,
        )
    }

    // -- converter --

    override val tagToOperationContentKindConverter: TagToOperationContentKindConverter = TagToOperationContentKindConverter()
}