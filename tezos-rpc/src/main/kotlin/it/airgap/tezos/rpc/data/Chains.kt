package it.airgap.tezos.rpc.data

import it.airgap.tezos.rpc.type.RpcError
import it.airgap.tezos.rpc.type.RpcUnistring
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ==== /chains ====

public typealias RpcBlockHash = RpcUnistring
public typealias RpcChainId = RpcUnistring

// -- /chains/<chain_id> --

@Serializable
public class SetBootstrappedRequest(public val bootstrapped: Boolean)

public typealias SetBootstrappedResponse = Unit

// -- /chains/<chain_id>/blocks --

@Serializable
public class GetBlocksResponse(public val list: List<List<RpcBlockHash>>)


// -- /chains/<chain_id>/chain_id --

@Serializable
@JvmInline
public value class GetChainIdResponse(public val chainId: RpcChainId)

// -- /chains/<chain_id>/invalid_blocks --

@Serializable
public data class RpcInvalidBlock(
    public val block: RpcBlockHash,
    public val level: Int,
    public val errors: List<RpcError>,
)

@Serializable
public data class GetInvalidBlocksResponse(public val list: List<RpcInvalidBlock>)

// -- /chains/<chain_id>/invalid_blocks/<block_hash> --

@Serializable
public data class GetInvalidBlockResponse(public val block: RpcInvalidBlock)

public typealias DeleteInvalidBlockResponse = Unit

// -- /chains/<chain_id>/is_bootstrapped --

@Serializable
public enum class RpcChainStatus {
    @SerialName("stuck") Stuck,
    @SerialName("synced") Synced,
    @SerialName("unsynced") Unsynced,
}

@Serializable
public data class IsBootstrappedResponse(public val bootstrapped: Boolean, @SerialName("sync_state") public val syncState: RpcChainStatus)

// -- /chains/<chain_id>/levels/caboose --

@Serializable
public data class GetCabooseResponse(@SerialName("block_hash") public val blockHash: RpcBlockHash, public val level: Int)

// -- /chains/<chain_id>/levels/checkpoint --

@Serializable
public data class GetCheckpointResponse(@SerialName("block_hash") public val blockHash: RpcBlockHash, public val level: Int)

// -- /chains/<chain_id>/levels/savepoint --

@Serializable
public data class GetSavepointResponse(@SerialName("block_hash") public val blockHash: RpcBlockHash, public val level: Int)