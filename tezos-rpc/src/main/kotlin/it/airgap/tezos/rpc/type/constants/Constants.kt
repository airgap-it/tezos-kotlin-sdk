package it.airgap.tezos.rpc.type.constants

import it.airgap.tezos.core.type.encoded.RandomHash
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.rpc.internal.serializer.LongSerializer
import it.airgap.tezos.rpc.internal.serializer.RpcConstantsSerializer
import it.airgap.tezos.rpc.type.delegate.RpcDelegateSelection
import it.airgap.tezos.rpc.type.primitive.RpcRatio
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

// -- RpcConstants --

@Serializable(with = RpcConstantsSerializer::class)
public sealed class RpcConstants {

    public abstract val proofOfWorkNonceSize: UByte
    public abstract val nonceLength: UByte
    public abstract val maxOperationDataLength: Int
    public abstract val preservedCycles: UByte
    public abstract val blocksPerCycle: Int
    public abstract val blocksPerCommitment: Int
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
    public open val cacheLayoutSize: UByte? = null

    @Transient
    public open val blocksPerStakeSnapshot: Int? = null

    @Transient
    public open val blocksPerVotingPeriod: Int? = null

    @Transient
    public open val cyclesPerVotingPeriod: Int? = null

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
    public open val liquidityBakingToggleEmaThreshold: Int? = null

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

    @Transient
    public open val initialSeed: RandomHash? = null

    @Transient
    public open val cacheScriptSize: Int? = null

    @Transient
    public open val cacheStakeDistributionCycles: Byte? = null

    @Transient
    public open val cacheSamplerStateCycles: Byte? = null

    @Transient
    public open val txRollupEnable: Boolean? = null

    @Transient
    public open val txRollupOriginationSize: Int? = null

    @Transient
    public open val txRollupHardSizeLimitPerInbox: Int? = null

    @Transient
    public open val txRollupHardSizeLimitPerMessage: Int? = null

    @Transient
    public open val txRollupMaxWithdrawalsPerBatch: Int? = null

    @Transient
    public open val txRollupCommitmentBond: Mutez? = null

    @Transient
    public open val txRollupFinalityPeriod: Int? = null

    @Transient
    public open val txRollupWithdrawPeriod: Int? = null

    @Transient
    public open val txRollupMaxInboxesCount: Int? = null

    @Transient
    public open val txRollupMaxMessagesPerInbox: Int? = null

    @Transient
    public open val txRollupMaxCommitmentsCount: Int? = null

    @Transient
    public open val txRollupCostPerByteEmaFactor: Int? = null

    @Transient
    public open val txRollupMaxTicketPayloadSize: Int? = null

    @Transient
    public open val txRollupRejectionMaxProofSize: Int? = null

    @Transient
    public open val txRollupSunsetLevel: Int? = null

    @Transient
    public open val scRollupEnable: Boolean? = null

    @Transient
    public open val scRollupOriginationSize: Int? = null

    @Transient
    public open val scRollupChallengeWindowInBlocks: Int? = null

    @Transient
    public open val scRollupMaxAvailableMessages: Int? = null

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
        @SerialName("cache_layout_size") override val cacheLayoutSize: UByte,
        @SerialName("michelson_maximum_type_size") override val michelsonMaximumTypeSize: UShort,
        @SerialName("preserved_cycles") override val preservedCycles: UByte,
        @SerialName("blocks_per_cycle") override val blocksPerCycle: Int,
        @SerialName("blocks_per_commitment") override val blocksPerCommitment: Int,
        @SerialName("blocks_per_stake_snapshot") override val blocksPerStakeSnapshot: Int,
        @SerialName("blocks_per_voting") override val cyclesPerVotingPeriod: Int,
        @SerialName("hard_gas_limit_per_operation") override val hardGasLimitPerOperation: String,
        @SerialName("hard_gas_limit_per_block") override val hardGasLimitPerBlock: String,
        @SerialName("proof_of_work_threshold") @Serializable(with = LongSerializer::class) override val proofOfWorkThreshold: Long,
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
        @SerialName("liquidity_baking_toggle_ema_threshold") override val liquidityBakingToggleEmaThreshold: Int,
        @SerialName("max_operations_time_to_live") override val maxOperationsTimeToLive: Short,
        @SerialName("minimal_block_delay") @Serializable(with = LongSerializer::class) override val minimalBlockDelay: Long,
        @SerialName("delay_increment_per_round") @Serializable(with = LongSerializer::class) override val delayIncrementPerRound: Long,
        @SerialName("consensus_committee_size") override val consensusCommitteeSize: Int,
        @SerialName("consensus_threshold") override val consensusThreshold: Int,
        @SerialName("minimal_participation_ratio") override val minimalParticipationRatio: RpcRatio,
        @SerialName("max_slashing_period") override val maxSlashingPeriod: Int,
        @SerialName("frozen_deposits_percentage") override val frozenDepositsPercentage: Int,
        @SerialName("double_baking_punishment") override val doubleBakingPunishment: String,
        @SerialName("ratio_of_frozen_deposits_slashed_per_double_endorsement") override val ratioOfFrozenDepositsSlashedPerDoubleEndorsement: RpcRatio,
        @SerialName("initial_seed") override val initialSeed: @Contextual RandomHash? = null,
        @SerialName("cache_script_size") override val cacheScriptSize: Int,
        @SerialName("cache_stake_distribution_cycles") override val cacheStakeDistributionCycles: Byte,
        @SerialName("cache_sampler_state_cycles") override val cacheSamplerStateCycles: Byte,
        @SerialName("tx_rollup_enable") override val txRollupEnable: Boolean,
        @SerialName("tx_rollup_origination_size") override val txRollupOriginationSize: Int,
        @SerialName("tx_rollup_hard_size_limit_per_inbox") override val txRollupHardSizeLimitPerInbox: Int,
        @SerialName("tx_rollup_hard_size_limit_per_message") override val txRollupHardSizeLimitPerMessage: Int,
        @SerialName("tx_rollup_max_withdrawals_per_batch") override val txRollupMaxWithdrawalsPerBatch: Int,
        @SerialName("tx_rollup_commitment_bond") override val txRollupCommitmentBond: @Contextual Mutez,
        @SerialName("tx_rollup_finality_period") override val txRollupFinalityPeriod: Int,
        @SerialName("tx_rollup_withdraw_period") override val txRollupWithdrawPeriod: Int,
        @SerialName("tx_rollup_max_inboxes_count") override val txRollupMaxInboxesCount: Int,
        @SerialName("tx_rollup_max_messages_per_inbox") override val txRollupMaxMessagesPerInbox: Int,
        @SerialName("tx_rollup_max_commitments_count") override val txRollupMaxCommitmentsCount: Int,
        @SerialName("tx_rollup_cost_per_byte_ema_factor") override val txRollupCostPerByteEmaFactor: Int,
        @SerialName("tx_rollup_max_ticket_payload_size") override val txRollupMaxTicketPayloadSize: Int,
        @SerialName("tx_rollup_rejection_max_proof_size") override val txRollupRejectionMaxProofSize: Int,
        @SerialName("tx_rollup_sunset_level") override val txRollupSunsetLevel: Int,
        @SerialName("sc_rollup_enable") override val scRollupEnable: Boolean,
        @SerialName("sc_rollup_origination_size") override val scRollupOriginationSize: Int,
        @SerialName("sc_rollup_challenge_window_in_blocks") override val scRollupChallengeWindowInBlocks: Int,
        @SerialName("sc_rollup_max_available_messages") override val scRollupMaxAvailableMessages: Int,
    ) : RpcConstants()
}
