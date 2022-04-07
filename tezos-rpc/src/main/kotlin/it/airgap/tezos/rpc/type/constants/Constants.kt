package it.airgap.tezos.rpc.type.constants

import it.airgap.tezos.rpc.type.RpcRatio
import it.airgap.tezos.rpc.type.delegate.RpcDelegateSelection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

// -- RpcConstants --

@Serializable
public sealed class RpcConstants {

    public abstract val proofOfWorkNonceSize: UByte
    public abstract val nonceLength: UByte
    public abstract val maxOperationDataLength: Int
    public abstract val preservedCycles: UByte
    public abstract val blocksPerCycle: Int
    public abstract val blocksPerCommitment: Int
    public abstract val blocksPerVotingPeriod: Int
    public abstract val michelsonMaximumTypeSize: UShort
    public abstract val hardGasLimitPerOperation: String
    public abstract val hardGasLimitPerBlock: String
    public abstract val proofOfWorkThreshold: Long
    public abstract val tokensPerRoll: String
    public abstract val seedNonceRevelationTip: String
    public abstract val costPerByte: String
    public abstract val hardStorageLimitPerOperation: String

    @Transient
    public open val maxAnonOpsPerBlock: UByte? = null

    @Transient
    public open val maxProposalsPerDelegate: UByte? = null

    @Transient
    public open val maxMichelineNodeCount: Int? = null

    @Transient
    public open val maxMichelineBytesLimit: Int? = null

    @Transient
    public open val maxAllowedGlobalConstantsDepth: Int? = null

    @Transient
    public open val cacheLayout: List<Long>? = null

    @Transient
    public open val blocksPerStakeSnapshot: Int? = null

    @Transient
    public open val originationSize: Int? = null

    @Transient
    public open val bakingRewardFixedPortion: String? = null

    @Transient
    public open val bakingRewardBonusPerSlot: String? = null

    @Transient
    public open val endorsingRewardPerSlot: String? = null

    @Transient
    public open val quorumMin: Int? = null

    @Transient
    public open val quorumMax: Int? = null

    @Transient
    public open val minProposalQuorum: Int? = null

    @Transient
    public open val liquidityBakingSubsidy: String? = null

    @Transient
    public open val liquidityBakingSunsetLevel: Int? = null

    @Transient
    public open val liquidityBakingEscapeEmaThreshold: Int? = null

    @Transient
    public open val maxOperationsTimeToLive: Short? = null

    @Transient
    public open val minimalBlockDelay: Long? = null

    @Transient
    public open val delayIncrementPerRound: Long? = null

    @Transient
    public open val consensusCommitteeSize: Int? = null

    @Transient
    public open val consensusThreshold: Int? = null

    @Transient
    public open val minimalParticipationRatio: RpcRatio? = null

    @Transient
    public open val maxSlashingPeriod: Int? = null

    @Transient
    public open val frozenDepositsPercentage: Int? = null

    @Transient
    public open val doubleBakingPunishment: String? = null

    @Transient
    public open val ratioOfFrozenDepositsSlashedPerDoubleEndorsement: RpcRatio? = null

    @Transient
    public open val delegateSelection: RpcDelegateSelection? = null

    @Serializable
    public data class Active(
        @SerialName("proof_of_work_nonce_size") override val proofOfWorkNonceSize: UByte,
        @SerialName("nonce_length") override val nonceLength: UByte,
        @SerialName("max_anon_ops_per_block") override val maxAnonOpsPerBlock: UByte,
        @SerialName("max_operation_data_length") override val maxOperationDataLength: Int,
        @SerialName("max_proposals_per_delegate") override val maxProposalsPerDelegate: UByte,
        @SerialName("max_micheline_node_count") override val maxMichelineNodeCount: Int,
        @SerialName("max_micheline_bytes_limit") override val maxMichelineBytesLimit: Int,
        @SerialName("max_allowed_global_constants_depth") override val maxAllowedGlobalConstantsDepth: Int,
        @SerialName("cache_layout") override val cacheLayout: List<Long>,
        @SerialName("michelson_maximum_type_size") override val michelsonMaximumTypeSize: UShort,
        @SerialName("preserved_cycles") override val preservedCycles: UByte,
        @SerialName("blocks_per_cycle") override val blocksPerCycle: Int,
        @SerialName("blocks_per_commitment") override val blocksPerCommitment: Int,
        @SerialName("blocks_per_stake_snapshot") override val blocksPerStakeSnapshot: Int,
        @SerialName("blocks_per_voting_period") override val blocksPerVotingPeriod: Int,
        @SerialName("hard_gas_limit_per_operation") override val hardGasLimitPerOperation: String,
        @SerialName("hard_gas_limit_per_block") override val hardGasLimitPerBlock: String,
        @SerialName("proof_of_work_threshold") override val proofOfWorkThreshold: Long,
        @SerialName("tokens_per_roll") override val tokensPerRoll: String,
        @SerialName("seed_nonce_revelation_tip") override val seedNonceRevelationTip: String,
        @SerialName("origination_size") override val originationSize: Int,
        @SerialName("baking_reward_fixed_portion") override val bakingRewardFixedPortion: String,
        @SerialName("baking_reward_bonus_per_slot") override val bakingRewardBonusPerSlot: String,
        @SerialName("endorsing_reward_per_slot") override val endorsingRewardPerSlot: String,
        @SerialName("cost_per_byte") override val costPerByte: String,
        @SerialName("hard_storage_limit_per_operation") override val hardStorageLimitPerOperation: String,
        @SerialName("quorum_min") override val quorumMin: Int,
        @SerialName("quorum_max") override val quorumMax: Int,
        @SerialName("min_proposal_quorum") override val minProposalQuorum: Int,
        @SerialName("liquidity_baking_subsidy") override val liquidityBakingSubsidy: String,
        @SerialName("liquidity_baking_sunset_level") override val liquidityBakingSunsetLevel: Int,
        @SerialName("liquidity_baking_escape_ema_threshold") override val liquidityBakingEscapeEmaThreshold: Int,
        @SerialName("max_operations_time_to_live") override val maxOperationsTimeToLive: Short,
        @SerialName("minimal_block_delay") override val minimalBlockDelay: Long,
        @SerialName("delay_increment_per_round") override val delayIncrementPerRound: Long,
        @SerialName("consensus_committee_size") override val consensusCommitteeSize: Int,
        @SerialName("consensus_threshold") override val consensusThreshold: Int,
        @SerialName("minimal_participation_ratio") override val minimalParticipationRatio: RpcRatio,
        @SerialName("max_slashing_period") override val maxSlashingPeriod: Int,
        @SerialName("frozen_deposits_percentage") override val frozenDepositsPercentage: Int,
        @SerialName("double_baking_punishment") override val doubleBakingPunishment: String,
        @SerialName("ratio_of_frozen_deposits_slashed_per_double_endorsement") override val ratioOfFrozenDepositsSlashedPerDoubleEndorsement: RpcRatio,
        @SerialName("delegate_selection") override val delegateSelection: RpcDelegateSelection? = null,
    ) : RpcConstants()
}
