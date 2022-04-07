package it.airgap.tezos.rpc.shell.chains

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.type.block.RpcInvalidBlock
import it.airgap.tezos.rpc.type.chain.RpcChainStatus
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- /chains/<chain_id> --

@Serializable
public data class SetBootstrappedRequest(public val bootstrapped: Boolean)

public typealias SetBootstrappedResponse = Unit

// -- /chains/<chain_id>/blocks --

@Serializable
@JvmInline
public value class GetBlocksResponse(public val blocks: List<List<@Contextual BlockHash>>)


// -- /chains/<chain_id>/chain_id --

@Serializable
@JvmInline
public value class GetChainIdResponse(public val chainId: @Contextual ChainId)

// -- /chains/<chain_id>/invalid_blocks --

@Serializable
@JvmInline
public value class GetInvalidBlocksResponse(public val blocks: List<RpcInvalidBlock>)

// -- /chains/<chain_id>/invalid_blocks/<block_hash> --

@Serializable
@JvmInline
public value class GetInvalidBlockResponse(public val block: RpcInvalidBlock)

public typealias DeleteInvalidBlockResponse = Unit

// -- /chains/<chain_id>/is_bootstrapped --

@Serializable
public data class GetIsBootstrappedResponse(public val bootstrapped: Boolean, @SerialName("sync_state") public val syncState: RpcChainStatus)

// -- /chains/<chain_id>/levels/caboose --

@Serializable
public data class GetCabooseResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)

// -- /chains/<chain_id>/levels/checkpoint --

@Serializable
public data class GetCheckpointResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)

// -- /chains/<chain_id>/levels/savepoint --

@Serializable
public data class GetSavepointResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)