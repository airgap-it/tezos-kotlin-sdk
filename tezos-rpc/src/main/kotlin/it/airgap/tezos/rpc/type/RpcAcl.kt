package it.airgap.tezos.rpc.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class RpcAcl {
    @SerialName("open") Open,
    @SerialName("trust") Trust,
    @SerialName("ban") Ban,
}
