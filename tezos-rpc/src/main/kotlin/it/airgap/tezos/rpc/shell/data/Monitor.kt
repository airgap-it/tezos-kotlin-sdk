package it.airgap.tezos.rpc.shell.data

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.type.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// ==== /monitor ====

// -- /active_chains --

@Serializable
@JvmInline
internal value class MonitorActiveChainsTransitionalResponse(val chains: List<TransitionalRpcActiveChain>)

@Serializable
@JvmInline
public value class MonitorActiveChainsResponse(public val chains: List<RpcActiveChain>)

// -- /bootstrapped --

@Serializable
internal data class MonitorBootstrappedTransitionalResponse(val block: TransitionalRpcBlockHash, val timestamp: TransitionalRpcTimestamp)

@Serializable
public data class MonitorBootstrappedResponse(public val block: @Contextual BlockHash, public val timestamp: @Contextual Timestamp)

// -- /heads/<chain_id> --

@Serializable
@JvmInline
internal value class MonitorHeadsTransitionalResponse(val blockHeader: TransitionalRpcBlockHeader)

@Serializable
@JvmInline
public value class MonitorHeadsResponse(public val blockHeader: RpcBlockHeader)

// -- /protocols --

@Serializable
@JvmInline
internal value class MonitorProtocolsTransitionalResponse(val hash: TransitionalRpcProtocolHash)

@Serializable
@JvmInline
public value class MonitorProtocolsResponse(public val hash: @Contextual ProtocolHash)

// -- valid_blocks --

@Serializable
@JvmInline
internal value class MonitorValidBlocksTransitionalResponse(val blockHeader: TransitionalRpcBlockHeader)

@Serializable
@JvmInline
public value class MonitorValidBlocksResponse(public val blockHeader: RpcBlockHeader)