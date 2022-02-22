package it.airgap.tezos.rpc.type.p2p

import it.airgap.tezos.rpc.type.Unistring
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcNetworkVersion --

@Serializable
public data class GenericRpcNetworkVersion<DistributedDbVersionName>(
    @SerialName("chain_name") public val chainName: DistributedDbVersionName,
    @SerialName("distributed_db_version") public val distributedDbVersion: UShort,
    @SerialName("p2p_version") public val p2pVersion: UShort,
)
internal typealias TransitionalRpcNetworkVersion = GenericRpcNetworkVersion<Unistring>
public typealias RpcNetworkVersion = GenericRpcNetworkVersion<String>

// -- RpcNetworkStat --

@Serializable
public data class RpcNetworkStat(
    @SerialName("total_sent") public val totalSent: Long,
    @SerialName("total_recv") public val totalReceived: Long,
    @SerialName("current_inflow") public val currentInflow: Int,
    @SerialName("current_outflow") public val currentOutflow: Int,
)
