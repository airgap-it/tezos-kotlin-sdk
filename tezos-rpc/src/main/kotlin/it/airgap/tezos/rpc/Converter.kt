package it.airgap.tezos.rpc

import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.rpc.internal.utils.isSigned
import it.airgap.tezos.rpc.internal.utils.signatureOrPlaceholder
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

// -- Operation -> RpcRunnableOperation --

public fun Operation.asRunnable(chainId: ChainId): RpcRunnableOperation =
    RpcRunnableOperation(
        chainId,
        branch,
        contents.map { it.asRpc() },
        signatureOrPlaceholder,
    )

// -- RpcRunnableOperation -> Operation --

public fun RpcRunnableOperation.asOperation(): Operation =
    Operation(contents.map { it.asOperationContent() }, branch, if (isSigned) signature else null)

// -- OperationContent -> RpcOperationContent --

public fun OperationContent.asRpc(): RpcOperationContent = TODO()

// -- RpcOperationContent -> OperationContent --

public fun RpcOperationContent.asOperationContent(): OperationContent = TODO()