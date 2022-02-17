package it.airgap.tezos.rpc.shell.data

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.type.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// ==== /monitor ====

// -- /active_chains --

@Serializable
@JvmInline
internal value class MonitorActiveChainsTransitionalResponse(val chains: List<RpcActiveChain<RpcChainId, RpcProtocolHash, RpcTimestamp>>)

@Serializable
@JvmInline
public value class MonitorActiveChainsResponse(public val chains: List<RpcActiveChain<@Contextual ChainId, @Contextual ProtocolHash, @Contextual Timestamp>>)

// -- /bootstrapped --

@Serializable
internal data class MonitorBootstrappedTransitionalResponse(val block: RpcBlockHash, val timestamp: RpcTimestamp)

@Serializable
public data class MonitorBootstrappedResponse(public val block: @Contextual BlockHash, public val timestamp: @Contextual Timestamp)

// -- /heads/<chain_id> --

@Serializable
@JvmInline
internal value class MonitorHeadsTransitionalResponse(val blockHeader: RpcBlockHeader<RpcBlockHash, RpcTimestamp, RpcOperationListListHash, RpcContextHash>)

@Serializable
@JvmInline
public value class MonitorHeadsResponse(public val blockHeader: RpcBlockHeader<@Contextual BlockHash, @Contextual Timestamp, @Contextual OperationListListHash, @Contextual ContextHash>)

// -- /protocols --

@Serializable
@JvmInline
internal value class MonitorProtocolsTransitionalResponse(val hash: RpcProtocolHash)

@Serializable
@JvmInline
public value class MonitorProtocolsResponse(public val hash: @Contextual ProtocolHash)

// -- valid_blocks --

@Serializable
@JvmInline
internal value class MonitorValidBlocksTransitionalResponse(val blockHeader: RpcBlockHeader<RpcBlockHash, RpcTimestamp, RpcOperationListListHash, RpcContextHash>)

@Serializable
@JvmInline
public value class MonitorValidBlocksResponse(public val blockHeader: RpcBlockHeader<@Contextual BlockHash, @Contextual Timestamp, @Contextual OperationListListHash, @Contextual ContextHash>)