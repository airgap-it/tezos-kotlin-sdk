package it.airgap.tezos.rpc.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class RpcChainStatus {
    @SerialName("stuck") Stuck,
    @SerialName("synced") Synced,
    @SerialName("unsynced") Unsynced,
}