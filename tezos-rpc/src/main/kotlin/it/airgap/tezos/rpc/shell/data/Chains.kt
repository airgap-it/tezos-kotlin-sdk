package it.airgap.tezos.rpc.shell.data

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.type.*
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
internal value class GetBlocksTransitionalResponse(val blocks: List<List<TransitionalRpcBlockHash>>)

@Serializable
@JvmInline
public value class GetBlocksResponse(public val blocks: List<List<@Contextual BlockHash>>)


// -- /<chain_id>/chain_id --

@Serializable
@JvmInline
internal value class GetChainIdTransitionalResponse(val chainId: TransitionalRpcChainId)

@Serializable
@JvmInline
public value class GetChainIdResponse(public val chainId: @Contextual ChainId)

// -- /<chain_id>/invalid_blocks --

@Serializable
@JvmInline
internal value class GetInvalidBlocksTransitionalResponse(val blocks: List<TransitionalRpcInvalidBlock>)

@Serializable
@JvmInline
public value class GetInvalidBlocksResponse(public val blocks: List<RpcInvalidBlock>)

// -- /<chain_id>/invalid_blocks/<block_hash> --

@Serializable
@JvmInline
internal value class GetInvalidBlockTransitionalResponse(val block: TransitionalRpcInvalidBlock)

@Serializable
@JvmInline
public value class GetInvalidBlockResponse(public val block: RpcInvalidBlock)

public typealias DeleteInvalidBlockResponse = Unit

// -- /<chain_id>/is_bootstrapped --

@Serializable
public data class IsBootstrappedResponse(public val bootstrapped: Boolean, @SerialName("sync_state") public val syncState: RpcChainStatus)

// -- /<chain_id>/levels/caboose --

@Serializable
internal data class GetCabooseTransitionalResponse(@SerialName("block_hash") val blockHash: TransitionalRpcBlockHash, val level: Int)

@Serializable
public data class GetCabooseResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)

// -- /<chain_id>/levels/checkpoint --

@Serializable
internal data class GetCheckpointTransitionalResponse(@SerialName("block_hash") val blockHash: TransitionalRpcBlockHash, val level: Int)

@Serializable
public data class GetCheckpointResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)

// -- /<chain_id>/levels/savepoint --

@Serializable
internal data class GetSavepointTransitionalResponse(@SerialName("block_hash") val blockHash: TransitionalRpcBlockHash, val level: Int)

@Serializable
public data class GetSavepointResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)