package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// -- RpcOperation --

@Serializable
public data class RpcOperation (
    public val protocol: @Contextual ProtocolHash,
    public val chainId: @Contextual ChainId,
    public val hash: @Contextual OperationHash,
    public val branch: @Contextual BlockHash,
    public val contents: List<RpcOperationContent>,
    public val signature: @Contextual SignatureEncoded?,
)

// -- RpcInjectableOperation --

@Serializable
public data class RpcInjectableOperation(
    public val branch: @Contextual BlockHash,
    public val data: @Contextual HexString,
)