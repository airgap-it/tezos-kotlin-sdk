package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.type.encoded.Signature
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.rpc.converter.asOperationContent
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

internal fun Operation.updateWith(rpcContents: List<RpcOperationContent>, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): Operation {
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

internal val Operation.signatureOrPlaceholder: Signature
    get() = when (this) {
        is Operation.Unsigned -> Signature.placeholder
        is Operation.Signed -> signature
    }

internal val RpcRunnableOperation.isSigned: Boolean
    get() = !signature.isPlaceholder
