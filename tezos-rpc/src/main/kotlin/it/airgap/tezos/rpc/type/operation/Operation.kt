package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.internal.serializer.RpcRunnableOperationSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcOperation --

@Serializable
public data class RpcOperation(
    public val protocol: @Contextual ProtocolHash,
    @SerialName("chain_id") public val chainId: @Contextual ChainId,
    public val hash: @Contextual OperationHash,
    public val branch: @Contextual BlockHash,
    public val contents: List<RpcOperationContent>,
    public val signature: @Contextual Signature?,
)

// -- RpcApplicableOperation --

@Serializable
public data class RpcApplicableOperation(
    public val protocol: @Contextual ProtocolHash,
    public val branch: @Contextual BlockHash,
    public val contents: List<RpcOperationContent>,
    public val signature: @Contextual Signature,
)

// -- RpcAppliedOperation --

@Serializable
public data class RpcAppliedOperation(
    public val contents: List<RpcOperationContent>,
    public val signature: @Contextual Signature? = null,
)

// -- RpcInjectableOperation --

@Serializable
public data class RpcInjectableOperation(
    public val branch: @Contextual BlockHash,
    public val data: String,
)

// -- RpcRunnableOperation --

@Serializable(with = RpcRunnableOperationSerializer::class)
public data class RpcRunnableOperation(
    @SerialName("chain_id") public val chainId: @Contextual ChainId,
    public val branch: @Contextual BlockHash,
    public val contents: List<RpcOperationContent>,
    public val signature: @Contextual Signature,
)
