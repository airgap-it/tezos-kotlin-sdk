package it.airgap.tezos.rpc.active.data

import it.airgap.tezos.core.type.encoded.SignatureEncoded
import it.airgap.tezos.rpc.type.operation.RpcApplicableOperation
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// ==== ../<block_id>/helpers ====

// -- /preapply/operations --

@Serializable
public data class PreapplyOperationsRequest(public val operations: List<RpcApplicableOperation>)

@Serializable
public data class PreapplyOperationsResponse(
    public val contents: RpcOperationContent,
    public val signature: @Contextual SignatureEncoded? = null,
)

// -- /scripts/run_operation --

@Serializable
@JvmInline
public value class RunOperationRequest(public val operation: RpcRunnableOperation)

@Serializable
@JvmInline
public value class RunOperationResponse(public val contents: RpcOperationContent)