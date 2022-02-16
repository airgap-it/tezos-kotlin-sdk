package it.airgap.tezos.rpc.data.shell

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.type.RpcBlockHash
import it.airgap.tezos.rpc.type.RpcChainId
import it.airgap.tezos.rpc.type.RpcError
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ==== /chains ====

// -- /<chain_id> --

@Serializable
public class SetBootstrappedRequest(public val bootstrapped: Boolean)

public typealias SetBootstrappedResponse = Unit

// -- /<chain_id>/blocks --

@Serializable
@JvmInline
internal value class GetBlocksTransitionalResponse(val blocks: List<List<RpcBlockHash>>)

@Serializable
@JvmInline
public value class GetBlocksResponse(public val blocks: List<List<@Contextual BlockHash>>)


// -- /<chain_id>/chain_id --

@Serializable
@JvmInline
internal value class GetChainIdTransitionalResponse(val chainId: RpcChainId)

@Serializable
@JvmInline
public value class GetChainIdResponse(public val chainId: @Contextual ChainId)

// -- /<chain_id>/invalid_blocks --

@Serializable
public data class RpcInvalidBlock<T>(
    public val block: T,
    public val level: Int,
    public val errors: List<RpcError>,
)

@Serializable
@JvmInline
internal value class GetInvalidBlocksTransitionalResponse(val blocks: List<RpcInvalidBlock<RpcBlockHash>>)

@Serializable
@JvmInline
public value class GetInvalidBlocksResponse(public val blocks: List<RpcInvalidBlock<@Contextual BlockHash>>)

// -- /<chain_id>/invalid_blocks/<block_hash> --

@Serializable
internal data class GetInvalidBlockTransitionalResponse(val block: RpcInvalidBlock<RpcBlockHash>)

@Serializable
public data class GetInvalidBlockResponse(public val block: RpcInvalidBlock<@Contextual BlockHash>)

public typealias DeleteInvalidBlockResponse = Unit

// -- /<chain_id>/is_bootstrapped --

@Serializable
public enum class RpcChainStatus {
    @SerialName("stuck") Stuck,
    @SerialName("synced") Synced,
    @SerialName("unsynced") Unsynced,
}

@Serializable
public data class IsBootstrappedResponse(public val bootstrapped: Boolean, @SerialName("sync_state") public val syncState: RpcChainStatus)

// -- /<chain_id>/levels/caboose --

@Serializable
internal data class GetCabooseTransitionalResponse(@SerialName("block_hash") val blockHash: RpcBlockHash, val level: Int)

@Serializable
public data class GetCabooseResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)

// -- /<chain_id>/levels/checkpoint --

@Serializable
internal data class GetCheckpointTransitionalResponse(@SerialName("block_hash") val blockHash: RpcBlockHash, val level: Int)

@Serializable
public data class GetCheckpointResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)

// -- /<chain_id>/levels/savepoint --

@Serializable
internal data class GetSavepointTransitionalResponse(@SerialName("block_hash") val blockHash: RpcBlockHash, val level: Int)

@Serializable
public data class GetSavepointResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)