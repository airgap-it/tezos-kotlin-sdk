package it.airgap.tezos.rpc.type.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RpcLevelInfo(
    public val level: Int,
    @SerialName("level_position") public val levelPosition: Int,
    public val cycle: Int,
    @SerialName("cycle_position") public val cyclePosition: Int,
    @SerialName("expected_commitment") public val expectedCommitment: Boolean,
)