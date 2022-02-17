package it.airgap.tezos.rpc.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RpcNetworkVersion<DistributedDbVersionName>(
    @SerialName("chain_name") public val chainName: DistributedDbVersionName,
    @SerialName("distributed_db_version") public val distributedDbVersion: UShort,
    @SerialName("p2p_version") public val p2pVersion: UShort,
)
