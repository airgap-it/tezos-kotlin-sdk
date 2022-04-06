package it.airgap.tezos.rpc.active.data

import it.airgap.tezos.core.type.encoded.Address
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ==== ../<block_id>/context/delegates ====

// -- /<pkh> --

@Serializable
public data class GetDelegateDetailsResponse(
    @SerialName("full_balance") public val fullBalance: String,
    @SerialName("current_frozen_deposits") public val currentFrozenDeposits: String,
    @SerialName("frozen_deposits") public val frozenDeposits: String,
    @SerialName("staking_balance") public val stakingBalance: String,
    @SerialName("frozen_deposits_limit") public val frozenDepositsLimit: String? = null,
    @SerialName("delegated_contracts") public val delegatedContracts: List<@Contextual Address>,
    @SerialName("delegated_balance") public val delegatedBalance: String,
    @SerialName("deactivated") public val deactivated: Boolean,
    @SerialName("grace_period") public val gracePeriod: Int,
    @SerialName("voting_power") public val votingPower: Int,
)

// -- /<pkh>/current_frozen_deposits --

@Serializable
@JvmInline
public value class GetDelegateCurrentFrozenDepositsResponse(public val currentFrozenDeposits: String)

// -- /<pkh>/deactivated --

@Serializable
@JvmInline
public value class GetDelegateDeactivatedStatusResponse(public val isDeactivated: Boolean)

// -- /<pkh>/delegated_balance --

@Serializable
@JvmInline
public value class GetDelegateDelegatedBalanceResponse(public val delegatedBalance: String)

// -- /<pkh>/delegated_balance --

@Serializable
@JvmInline
public value class GetDelegateDelegatedContractsResponse(public val delegatedContracts: List<@Contextual Address>)

// -- /<pkh>/frozen_deposits --

@Serializable
@JvmInline
public value class GetDelegateFrozenDepositsResponse(public val frozenDeposits: String)

// -- /<pkh>/frozen_deposits_limit --

@Serializable
@JvmInline
public value class GetDelegateFrozenDepositsLimitResponse(public val frozenDepositsLimit: String?)

// -- /<pkh>/full_balance --

@Serializable
@JvmInline
public value class GetDelegateFullBalanceResponse(public val balance: String)

// -- /<pkh>/grace_period --

@Serializable
@JvmInline
public value class GetDelegateGracePeriodResponse(public val gracePeriod: Int)

// -- /<pkh>/participation --

@Serializable
public data class GetDelegateParticipationResponse(
    @SerialName("expected_cycle_activity") public val expectedCycleActivity: Int,
    @SerialName("minimal_cycle_activity") public val minimalCycleActivity: Int,
    @SerialName("missed_slots") public val missedSlots: Int,
    @SerialName("missed_levels") public val missedLevels: Int,
    @SerialName("remaining_allowed_missed_slots") public val remainingAllowedMissedSlots: Int,
    @SerialName("expected_endorsing_rewards") public val expectedEndorsingRewards: String,
)

// -- /<pkh>/staking_balance --

@Serializable
@JvmInline
public value class GetDelegateStakingBalanceResponse(public val stakingBalance: String)

// -- /<pkh>/voting_power --

@Serializable
@JvmInline
public value class GetDelegateVotingPowerResponse(public val votingPower: Int)