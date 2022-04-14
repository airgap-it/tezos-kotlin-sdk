package it.airgap.tezos.rpc.shell.monitor

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.type.chain.RpcActiveChain
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- /monitor/active_chains --

@Serializable
@JvmInline
public value class MonitorActiveChainsResponse(public val chains: List<RpcActiveChain>)

// -- /monitor/bootstrapped --

@Serializable
public data class MonitorBootstrappedResponse(public val block: @Contextual BlockHash, public val timestamp: @Contextual Timestamp)

// -- /monitor/heads/<chain_id> --

@Serializable
public data class MonitorHeadResponse(
    public val hash: @Contextual BlockHash,
    public val level: Int,
    public val proto: UByte,
    public val predecessor: @Contextual BlockHash,
    public val timestamp: @Contextual Timestamp,
    @SerialName("validation_pass") public val validationPass: UByte,
    @SerialName("operations_hash") public val operationsHash: @Contextual OperationListListHash,
    public val fitness: List<@Contextual HexString>,
    public val context: @Contextual ContextHash,
    @SerialName("protocol_data") public val protocolData: @Contextual HexString,
)

// -- /monitor/protocols --

@Serializable
@JvmInline
public value class MonitorProtocolsResponse(public val hash: @Contextual ProtocolHash)

// -- /monitor/valid_blocks --

@Serializable
public data class MonitorValidBlocksResponse(
    @SerialName("chain_id") public val chainId: @Contextual ChainId,
    public val hash: @Contextual BlockHash,
    public val level: Int,
    public val proto: UByte,
    public val predecessor: @Contextual BlockHash,
    public val timestamp: @Contextual Timestamp,
    @SerialName("validation_pass") public val validationPass: UByte,
    @SerialName("operations_hash") public val operationsHash: @Contextual OperationListListHash,
    public val fitness: List<@Contextual HexString>,
    public val context: @Contextual ContextHash,
    @SerialName("protocol_data") public val protocolData: @Contextual HexString,
)
