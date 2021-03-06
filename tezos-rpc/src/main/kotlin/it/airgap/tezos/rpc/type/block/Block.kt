package it.airgap.tezos.rpc.type.block

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.type.error.RpcError
import it.airgap.tezos.rpc.type.operation.RpcOperation
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcBlock --

@Serializable
public data class RpcBlock(
    public val protocol: @Contextual ProtocolHash,
    @SerialName("chain_id") public val chainId: @Contextual ChainId,
    public val hash: @Contextual BlockHash,
    public val header: RpcBlockHeader,
    public val metadata: RpcBlockHeaderMetadata? = null,
    public val operations: List<List<RpcOperation>>,
)

// -- RpcInvalidBlock --

@Serializable
public data class RpcInvalidBlock(
    public val block: @Contextual BlockHash,
    public val level: Int,
    public val errors: List<RpcError>,
)
