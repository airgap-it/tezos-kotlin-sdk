package it.airgap.tezos.rpc.shell.monitor

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.type.chain.RpcActiveChain
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- /monitor/active_chains --

/**
 * Response from [`GET /monitor/active_chains`](https://tezos.gitlab.io/shell/rpc.html#get-monitor-active-chains).
 */
@Serializable
@JvmInline
public value class MonitorActiveChainsResponse(public val chains: List<RpcActiveChain>)

// -- /monitor/bootstrapped --

/**
 * Response from [`GET /monitor/bootstrapped`](https://tezos.gitlab.io/shell/rpc.html#get-monitor-bootstrapped).
 */
@Serializable
public data class MonitorBootstrappedResponse(public val block: @Contextual BlockHash, public val timestamp: @Contextual Timestamp)

// -- /monitor/heads/<chain_id> --

/**
 * Response from [`GET /monitor/heads/<chain_id>?(next_protocol=<Protocol_hash>)`](https://tezos.gitlab.io/shell/rpc.html#get-monitor-heads-chain-id).
 */
@Serializable
public data class MonitorHeadResponse(
    public val hash: @Contextual BlockHash,
    public val level: Int,
    public val proto: UByte,
    public val predecessor: @Contextual BlockHash,
    public val timestamp: @Contextual Timestamp,
    @SerialName("validation_pass") public val validationPass: UByte,
    @SerialName("operations_hash") public val operationsHash: @Contextual OperationListListHash,
    public val fitness: List<String>,
    public val context: @Contextual ContextHash,
    @SerialName("protocol_data") public val protocolData: String,
)

// -- /monitor/protocols --

/**
 * Response from [`GET /monitor/protocols`](https://tezos.gitlab.io/shell/rpc.html#get-monitor-protocols).
 */
@Serializable
@JvmInline
public value class MonitorProtocolsResponse(public val hash: @Contextual ProtocolHash)

// -- /monitor/valid_blocks --

/**
 * Response from [`GET /monitor/valid_blocks?(protocol=<Protocol_hash>)*&(next_protocol=<Protocol_hash>)*&(chain=<chain_id>)`](https://tezos.gitlab.io/shell/rpc.html#get-monitor-valid-blocks).
 */
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
    public val fitness: List<String>,
    public val context: @Contextual ContextHash,
    @SerialName("protocol_data") public val protocolData: String,
)
