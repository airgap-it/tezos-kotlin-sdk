package it.airgap.tezos.rpc.shell.data

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.type.RpcActiveChain
import it.airgap.tezos.rpc.type.RpcBlockHeader
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// ==== /monitor ====

// -- /active_chains --

@Serializable
@JvmInline
public value class MonitorActiveChainsResponse(public val chains: List<RpcActiveChain>)

// -- /bootstrapped --

@Serializable
public data class MonitorBootstrappedResponse(public val block: @Contextual BlockHash, public val timestamp: @Contextual Timestamp)

// -- /heads/<chain_id> --

@Serializable
@JvmInline
public value class MonitorHeadsResponse(public val blockHeader: RpcBlockHeader)

// -- /protocols --

@Serializable
@JvmInline
public value class MonitorProtocolsResponse(public val hash: @Contextual ProtocolHash)

// -- valid_blocks --

@Serializable
@JvmInline
public value class MonitorValidBlocksResponse(public val blockHeader: RpcBlockHeader)