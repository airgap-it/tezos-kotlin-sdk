package it.airgap.tezos.rpc.shell.data

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.type.RpcChainStatus
import it.airgap.tezos.rpc.type.block.RpcInvalidBlock
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ==== /chains ====

// -- /<chain_id> --

@Serializable
public data class SetBootstrappedRequest(public val bootstrapped: Boolean)

public typealias SetBootstrappedResponse = Unit

// -- /<chain_id>/blocks --

@Serializable
@JvmInline
public value class GetBlocksResponse(public val blocks: List<List<@Contextual BlockHash>>)


// -- /<chain_id>/chain_id --

@Serializable
@JvmInline
public value class GetChainIdResponse(public val chainId: @Contextual ChainId)

// -- /<chain_id>/invalid_blocks --

@Serializable
@JvmInline
public value class GetInvalidBlocksResponse(public val blocks: List<RpcInvalidBlock>)

// -- /<chain_id>/invalid_blocks/<block_hash> --

@Serializable
@JvmInline
public value class GetInvalidBlockResponse(public val block: RpcInvalidBlock)

public typealias DeleteInvalidBlockResponse = Unit

// -- /<chain_id>/is_bootstrapped --

@Serializable
public data class IsBootstrappedResponse(public val bootstrapped: Boolean, @SerialName("sync_state") public val syncState: RpcChainStatus)

// -- /<chain_id>/levels/caboose --

@Serializable
public data class GetCabooseResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)

// -- /<chain_id>/levels/checkpoint --

@Serializable
public data class GetCheckpointResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)

// -- /<chain_id>/levels/savepoint --

@Serializable
public data class GetSavepointResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)