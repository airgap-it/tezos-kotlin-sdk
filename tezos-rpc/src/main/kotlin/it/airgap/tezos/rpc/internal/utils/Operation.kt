package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.type.encoded.SignatureEncoded
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.rpc.asOperationContent
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

internal fun Operation.updateWith(rpcContents: List<RpcOperationContent>, operationContentBytesCoder: OperationContentBytesCoder): Operation {
    val groupedContents = rpcContents.groupMutableBy { it.asOperationContent().hashCode() }
    val updatedContents = contents.map {
        val rpcContent = groupedContents.next(it.hashCode()) ?: return@map it
        it.updateWith(rpcContent, operationContentBytesCoder)
    }

    return when (this) {
        is Operation.Unsigned -> copy(contents = updatedContents)
        is Operation.Signed -> copy(contents = updatedContents)
    }
}

internal val Operation.signatureOrPlaceholder: SignatureEncoded
    get() = when (this) {
        is Operation.Unsigned -> SignatureEncoded.placeholder
        is Operation.Signed -> signature
    }

internal val RpcRunnableOperation.isSigned: Boolean
    get() = !signature.isPlaceholder
