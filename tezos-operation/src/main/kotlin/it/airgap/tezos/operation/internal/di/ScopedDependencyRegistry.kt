package it.airgap.tezos.operation.internal.di

import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.di.findScoped
import it.airgap.tezos.operation.internal.coder.OperationBytesCoder
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.operation.internal.converter.TagToOperationContentKindConverter
import it.airgap.tezos.operation.internal.signer.OperationEd25519Signer
import it.airgap.tezos.operation.internal.signer.OperationP256Signer
import it.airgap.tezos.operation.internal.signer.OperationSecp256K1Signer
import it.airgap.tezos.operation.internal.signer.OperationSigner

internal interface ScopedDependencyRegistry : DependencyRegistry {

    // -- coder --

    val operationBytesCoder: OperationBytesCoder
    val operationContentBytesCoder: OperationContentBytesCoder

    // -- converter --

    val tagToOperationContentKindConverter: TagToOperationContentKindConverter

    // -- signer --

    val operationSigner: OperationSigner
    val operationEd25519Signer: OperationEd25519Signer
    val operationSecp256K1Signer: OperationSecp256K1Signer
    val operationP256Signer: OperationP256Signer
}

internal fun DependencyRegistry.operation(): ScopedDependencyRegistry =
    if (this is ScopedDependencyRegistry) this
    else findScoped<OperationScopedDependencyRegistry>() ?: OperationScopedDependencyRegistry(this).also { addScoped(it) }