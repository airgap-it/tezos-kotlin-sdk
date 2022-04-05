package it.airgap.tezos.operation.internal.di

import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.internal.di.findScoped
import it.airgap.tezos.michelson.internal.di.michelson
import it.airgap.tezos.operation.internal.coder.OperationBytesCoder
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.operation.internal.converter.TagToOperationContentKindConverter
import it.airgap.tezos.operation.internal.signer.OperationEd25519Signer
import it.airgap.tezos.operation.internal.signer.OperationP256Signer
import it.airgap.tezos.operation.internal.signer.OperationSecp256K1Signer
import it.airgap.tezos.operation.internal.signer.OperationSigner

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
            core().timestampBigIntCoder,
            tagToOperationContentKindConverter,
        )
    }

    // -- converter --

    override val tagToOperationContentKindConverter: TagToOperationContentKindConverter = TagToOperationContentKindConverter()

    // -- signer --

    override val operationSigner: OperationSigner by lazy { OperationSigner(operationEd25519Signer, operationSecp256K1Signer, operationP256Signer) }
    override val operationEd25519Signer: OperationEd25519Signer by lazy { OperationEd25519Signer(crypto, operationBytesCoder, core().encodedBytesCoder) }
    override val operationSecp256K1Signer: OperationSecp256K1Signer by lazy { OperationSecp256K1Signer(crypto, operationBytesCoder, core().encodedBytesCoder) }
    override val operationP256Signer: OperationP256Signer by lazy { OperationP256Signer(crypto, operationBytesCoder, core().encodedBytesCoder) }
}

internal fun DependencyRegistry.operation(): ScopedDependencyRegistry =
    if (this is ScopedDependencyRegistry) this
    else findScoped<OperationScopedDependencyRegistry>() ?: OperationScopedDependencyRegistry(this).also { addScoped(it) }