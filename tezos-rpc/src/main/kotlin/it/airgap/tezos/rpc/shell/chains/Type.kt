package it.airgap.tezos.rpc.shell.chains

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.type.block.RpcInvalidBlock
import it.airgap.tezos.rpc.type.chain.RpcChainStatus
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- /chains/<chain_id> --

/**
 * Request for [`PATCH /chains/<chain_id>`](https://tezos.gitlab.io/shell/rpc.html#patch-chains-chain-id).
 */
@Serializable
public data class SetBootstrappedRequest(public val bootstrapped: Boolean)

/**
 * Response from [`PATCH /chains/<chain_id>`](https://tezos.gitlab.io/shell/rpc.html#patch-chains-chain-id).
 */
public typealias SetBootstrappedResponse = Unit

// -- /chains/<chain_id>/blocks --

/**
 * Response from [`GET /chains/<chain_id>/blocks?[length=<uint>]&(head=<block_hash>)*&[min_date=<date>]`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-blocks).
 */
@Serializable
@JvmInline
public value class GetBlocksResponse(public val blocks: List<List<@Contextual BlockHash>>)


// -- /chains/<chain_id>/chain_id --

/**
 * Response from [`GET /chains/<chain_id>/chain_id`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-chain-id).
 */
@Serializable
@JvmInline
public value class GetChainIdResponse(public val chainId: @Contextual ChainId)

// -- /chains/<chain_id>/invalid_blocks --

/**
 * Response from [`GET /chains/<chain_id>/invalid_blocks`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-invalid-blocks).
 */
@Serializable
@JvmInline
public value class GetInvalidBlocksResponse(public val blocks: List<RpcInvalidBlock>)

// -- /chains/<chain_id>/invalid_blocks/<block_hash> --

/**
 * Response from [`GET /chains/<chain_id>/invalid_blocks/<block_hash>`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-invalid-blocks-block-hash).
 */
@Serializable
@JvmInline
public value class GetInvalidBlockResponse(public val block: RpcInvalidBlock)

/**
 * Response from [`DELETE /chains/<chain_id>/invalid_blocks/<block_hash>`](https://tezos.gitlab.io/shell/rpc.html#delete-chains-chain-id-invalid-blocks-block-hash).
 */
public typealias DeleteInvalidBlockResponse = Unit

// -- /chains/<chain_id>/is_bootstrapped --

/**
 * Response from [`GET /chains/<chain_id>/is_bootstrapped`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-is-bootstrapped).
 */
@Serializable
public data class GetBootstrappedStatusResponse(public val bootstrapped: Boolean, @SerialName("sync_state") public val syncState: RpcChainStatus)

// -- /chains/<chain_id>/levels/caboose --

/**
 * Response from [`GET /chains/<chain_id>/levels/caboose`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-levels-caboose).
 */
@Serializable
public data class GetCabooseResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)

// -- /chains/<chain_id>/levels/checkpoint --

/**
 * Response from [`GET /chains/<chain_id>/levels/checkpoint`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-levels-checkpoint).
 */
@Serializable
public data class GetCheckpointResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)

// -- /chains/<chain_id>/levels/savepoint --

/**
 * Response from [`GET /chains/<chain_id>/levels/savepoint`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-levels-savepoint).
 */
@Serializable
public data class GetSavepointResponse(@SerialName("block_hash") public val blockHash: @Contextual BlockHash, public val level: Int)