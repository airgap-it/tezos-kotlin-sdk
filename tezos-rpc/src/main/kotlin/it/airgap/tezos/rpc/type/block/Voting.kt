package it.airgap.tezos.rpc.type.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcVotingPeriodInfo --

@Serializable
public data class RpcVotingPeriodInfo(
    @SerialName("voting_period") public val votingPeriod: RpcVotingPeriod,
    public val position: Int,
    public val remaining: Int,
)

// -- RpcVotingPeriod --

@Serializable
public data class RpcVotingPeriod(
    public val index: Int,
    public val kind: Kind,
    @SerialName("start_position") public val startPosition: Int,
) {

    @Serializable
    public enum class Kind {
        @SerialName("proposal") Proposal,
        @SerialName("exploration") Exploration,
        @SerialName("cooldown") Cooldown,
        @SerialName("promotion") Promotion,
        @SerialName("adoption") Adoption,
    }
}