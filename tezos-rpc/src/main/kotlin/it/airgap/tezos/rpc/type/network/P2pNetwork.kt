package it.airgap.tezos.rpc.type.network

import it.airgap.tezos.rpc.type.primitive.RpcUnistring
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcNetworkVersion --

@Serializable
public data class RpcNetworkVersion(
    @SerialName("chain_name") public val chainName: RpcUnistring,
    @SerialName("distributed_db_version") public val distributedDbVersion: UShort,
    @SerialName("p2p_version") public val p2pVersion: UShort,
)

// -- RpcNetworkStat --

@Serializable
public data class RpcNetworkStat(
    @SerialName("total_sent") public val totalSent: Long,
    @SerialName("total_recv") public val totalReceived: Long,
    @SerialName("current_inflow") public val currentInflow: Int,
    @SerialName("current_outflow") public val currentOutflow: Int,
)
