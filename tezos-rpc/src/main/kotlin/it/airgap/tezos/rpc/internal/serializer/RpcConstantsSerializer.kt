package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.type.encoded.RandomHash
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.rpc.type.constants.RpcConstants
import it.airgap.tezos.rpc.type.delegate.RpcDelegateSelection
import it.airgap.tezos.rpc.type.primitive.RpcRatio
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object RpcConstantsSerializer : KSerializer<RpcConstants> {
    override val descriptor: SerialDescriptor = RpcConstantsSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): RpcConstants {
        val surrogate = decoder.decodeSerializableValue(RpcConstantsSurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: RpcConstants) {
        val surrogate = RpcConstantsSurrogate(value)
        encoder.encodeSerializableValue(RpcConstantsSurrogate.serializer(), surrogate)
    }
}

// -- surrogate --

@Serializable
private data class RpcConstantsSurrogate(
    @SerialName("proof_of_work_nonce_size") val proofOfWorkNonceSize: UByte,
    @SerialName("nonce_length") val nonceLength: UByte,
    @SerialName("max_anon_ops_per_block") val maxAnonOpsPerBlock: UByte? = null,
    @SerialName("max_operation_data_length") val maxOperationDataLength: Int,
    @SerialName("max_proposals_per_delegate") val maxProposalsPerDelegate: UByte? = null,
    @SerialName("max_micheline_node_count") val maxMichelineNodeCount: Int? = null,
    @SerialName("max_micheline_bytes_limit") val maxMichelineBytesLimit: Int? = null,
    @SerialName("max_allowed_global_constants_depth") val maxAllowedGlobalConstantsDepth: Int? = null,
    @SerialName("cache_layout") val cacheLayout: List<@Serializable(with = LongSerializer::class) Long>? = null,
    @SerialName("cache_layout_size") val cacheLayoutSize: UByte? = null,
    @SerialName("michelson_maximum_type_size") val michelsonMaximumTypeSize: UShort,
    @SerialName("preserved_cycles") val preservedCycles: UByte,
    @SerialName("blocks_per_cycle") val blocksPerCycle: Int,
    @SerialName("blocks_per_commitment") val blocksPerCommitment: Int,
    @SerialName("blocks_per_stake_snapshot") val blocksPerStakeSnapshot: Int? = null,
    @SerialName("blocks_per_voting_period") val blocksPerVotingPeriod: Int? = null,
    @SerialName("cycles_per_voting_period") val cyclesPerVotingPeriod: Int? = null,
    @SerialName("hard_gas_limit_per_operation") val hardGasLimitPerOperation: String,
    @SerialName("hard_gas_limit_per_block") val hardGasLimitPerBlock: String,
    @SerialName("proof_of_work_threshold") @Serializable(with = LongSerializer::class) val proofOfWorkThreshold: Long,
    @SerialName("tokens_per_roll") val tokensPerRoll: String,
    @SerialName("seed_nonce_revelation_tip") val seedNonceRevelationTip: String,
    @SerialName("origination_size") val originationSize: Int? = null,
    @SerialName("baking_reward_fixed_portion") val bakingRewardFixedPortion: String? = null,
    @SerialName("baking_reward_bonus_per_slot") val bakingRewardBonusPerSlot: String? = null,
    @SerialName("endorsing_reward_per_slot") val endorsingRewardPerSlot: String? = null,
    @SerialName("cost_per_byte") val costPerByte: String,
    @SerialName("hard_storage_limit_per_operation") val hardStorageLimitPerOperation: String,
    @SerialName("quorum_min") val quorumMin: Int? = null,
    @SerialName("quorum_max") val quorumMax: Int? = null,
    @SerialName("min_proposal_quorum") val minProposalQuorum: Int? = null,
    @SerialName("liquidity_baking_subsidy") val liquidityBakingSubsidy: String? = null,
    @SerialName("liquidity_baking_sunset_level") val liquidityBakingSunsetLevel: Int? = null,
    @SerialName("liquidity_baking_escape_ema_threshold") val liquidityBakingEscapeEmaThreshold: Int? = null,
    @SerialName("liquidity_baking_toggle_ema_threshold") val liquidityBakingToggleEmaThreshold: Int? = null,
    @SerialName("max_operations_time_to_live") val maxOperationsTimeToLive: Short? = null,
    @SerialName("minimal_block_delay") @Serializable(with = LongSerializer::class) val minimalBlockDelay: Long? = null,
    @SerialName("delay_increment_per_round") @Serializable(with = LongSerializer::class) val delayIncrementPerRound: Long? = null,
    @SerialName("consensus_committee_size") val consensusCommitteeSize: Int? = null,
    @SerialName("consensus_threshold") val consensusThreshold: Int? = null,
    @SerialName("minimal_participation_ratio") val minimalParticipationRatio: RpcRatio? = null,
    @SerialName("max_slashing_period") val maxSlashingPeriod: Int? = null,
    @SerialName("frozen_deposits_percentage") val frozenDepositsPercentage: Int? = null,
    @SerialName("double_baking_punishment") val doubleBakingPunishment: String? = null,
    @SerialName("ratio_of_frozen_deposits_slashed_per_double_endorsement") val ratioOfFrozenDepositsSlashedPerDoubleEndorsement: RpcRatio? = null,
    @SerialName("delegate_selection") val delegateSelection: RpcDelegateSelection? = null,
    @SerialName("initial_seed") val initialSeed: @Contextual RandomHash? = null,
    @SerialName("cache_script_size") val cacheScriptSize: Int? = null,
    @SerialName("cache_stake_distribution_cycles") val cacheStakeDistributionCycles: Byte? = null,
    @SerialName("cache_sampler_state_cycles") val cacheSamplerStateCycles: Byte? = null,
    @SerialName("tx_rollup_enable") val txRollupEnable: Boolean? = null,
    @SerialName("tx_rollup_origination_size") val txRollupOriginationSize: Int? = null,
    @SerialName("tx_rollup_hard_size_limit_per_inbox") val txRollupHardSizeLimitPerInbox: Int? = null,
    @SerialName("tx_rollup_hard_size_limit_per_message") val txRollupHardSizeLimitPerMessage: Int? = null,
    @SerialName("tx_rollup_max_withdrawals_per_batch") val txRollupMaxWithdrawalsPerBatch: Int? = null,
    @SerialName("tx_rollup_commitment_bond") val txRollupCommitmentBond: @Contextual Mutez? = null,
    @SerialName("tx_rollup_finality_period") val txRollupFinalityPeriod: Int? = null,
    @SerialName("tx_rollup_withdraw_period") val txRollupWithdrawPeriod: Int? = null,
    @SerialName("tx_rollup_max_inboxes_count") val txRollupMaxInboxesCount: Int? = null,
    @SerialName("tx_rollup_max_messages_per_inbox") val txRollupMaxMessagesPerInbox: Int? = null,
    @SerialName("tx_rollup_max_commitments_count") val txRollupMaxCommitmentsCount: Int? = null,
    @SerialName("tx_rollup_cost_per_byte_ema_factor") val txRollupCostPerByteEmaFactor: Int? = null,
    @SerialName("tx_rollup_max_ticket_payload_size") val txRollupMaxTicketPayloadSize: Int? = null,
    @SerialName("tx_rollup_rejection_max_proof_size") val txRollupRejectionMaxProofSize: Int? = null,
    @SerialName("tx_rollup_sunset_level") val txRollupSunsetLevel: Int? = null,
    @SerialName("sc_rollup_enable") val scRollupEnable: Boolean? = null,
    @SerialName("sc_rollup_origination_size") val scRollupOriginationSize: Int? = null,
    @SerialName("sc_rollup_challenge_window_in_blocks") val scRollupChallengeWindowInBlocks: Int? = null,
    @SerialName("sc_rollup_max_available_messages") val scRollupMaxAvailableMessages: Int? = null,
) {
    fun toTarget(): RpcConstants = when {
        maxAnonOpsPerBlock != null && maxProposalsPerDelegate != null && maxMichelineNodeCount != null && maxMichelineBytesLimit != null &&
                maxAllowedGlobalConstantsDepth != null && cacheLayoutSize != null && blocksPerStakeSnapshot != null && cyclesPerVotingPeriod != null &&
                originationSize != null && bakingRewardFixedPortion != null && bakingRewardBonusPerSlot != null && endorsingRewardPerSlot != null &&
                quorumMin != null && quorumMax != null && minProposalQuorum != null && liquidityBakingSubsidy != null && liquidityBakingSunsetLevel != null &&
                liquidityBakingToggleEmaThreshold != null && maxOperationsTimeToLive != null && minimalBlockDelay != null && delayIncrementPerRound != null &&
                consensusCommitteeSize != null && consensusThreshold != null && minimalParticipationRatio != null && maxSlashingPeriod != null &&
                frozenDepositsPercentage != null && doubleBakingPunishment != null && ratioOfFrozenDepositsSlashedPerDoubleEndorsement != null &&
                cacheScriptSize != null && cacheStakeDistributionCycles != null && cacheSamplerStateCycles != null && txRollupEnable != null &&
                txRollupOriginationSize != null && txRollupHardSizeLimitPerInbox != null && txRollupHardSizeLimitPerMessage != null &&
                txRollupMaxWithdrawalsPerBatch != null && txRollupCommitmentBond != null && txRollupFinalityPeriod != null && txRollupWithdrawPeriod != null &&
                txRollupMaxInboxesCount != null && txRollupMaxMessagesPerInbox != null && txRollupMaxCommitmentsCount != null && txRollupCostPerByteEmaFactor != null &&
                txRollupMaxTicketPayloadSize != null && txRollupRejectionMaxProofSize != null && txRollupSunsetLevel != null && scRollupEnable != null &&
                scRollupOriginationSize != null && scRollupChallengeWindowInBlocks != null && scRollupMaxAvailableMessages != null -> {
                    RpcConstants.Active(
                        proofOfWorkNonceSize,
                        nonceLength,
                        maxAnonOpsPerBlock,
                        maxOperationDataLength,
                        maxProposalsPerDelegate,
                        maxMichelineNodeCount,
                        maxMichelineBytesLimit,
                        maxAllowedGlobalConstantsDepth,
                        cacheLayoutSize,
                        michelsonMaximumTypeSize,
                        preservedCycles,
                        blocksPerCycle,
                        blocksPerCommitment,
                        blocksPerStakeSnapshot,
                        cyclesPerVotingPeriod,
                        hardGasLimitPerOperation,
                        hardGasLimitPerBlock,
                        proofOfWorkThreshold,
                        tokensPerRoll,
                        seedNonceRevelationTip,
                        originationSize,
                        bakingRewardFixedPortion,
                        bakingRewardBonusPerSlot,
                        endorsingRewardPerSlot,
                        costPerByte,
                        hardStorageLimitPerOperation,
                        quorumMin,
                        quorumMax,
                        minProposalQuorum,
                        liquidityBakingSubsidy,
                        liquidityBakingSunsetLevel,
                        liquidityBakingToggleEmaThreshold,
                        maxOperationsTimeToLive,
                        minimalBlockDelay,
                        delayIncrementPerRound,
                        consensusCommitteeSize,
                        consensusThreshold,
                        minimalParticipationRatio,
                        maxSlashingPeriod,
                        frozenDepositsPercentage,
                        doubleBakingPunishment,
                        ratioOfFrozenDepositsSlashedPerDoubleEndorsement,
                        initialSeed,
                        cacheScriptSize,
                        cacheStakeDistributionCycles,
                        cacheSamplerStateCycles,
                        txRollupEnable,
                        txRollupOriginationSize,
                        txRollupHardSizeLimitPerInbox,
                        txRollupHardSizeLimitPerMessage,
                        txRollupMaxWithdrawalsPerBatch,
                        txRollupCommitmentBond,
                        txRollupFinalityPeriod,
                        txRollupWithdrawPeriod,
                        txRollupMaxInboxesCount,
                        txRollupMaxMessagesPerInbox,
                        txRollupMaxCommitmentsCount,
                        txRollupCostPerByteEmaFactor,
                        txRollupMaxTicketPayloadSize,
                        txRollupRejectionMaxProofSize,
                        txRollupSunsetLevel,
                        scRollupEnable,
                        scRollupOriginationSize,
                        scRollupChallengeWindowInBlocks,
                        scRollupMaxAvailableMessages,
                    )
                }
        else -> failWithInvalidSerializedValue(this)
    }

    private fun failWithInvalidSerializedValue(value: RpcConstantsSurrogate): Nothing =
        throw SerializationException("Could not deserialize, `$value` is not a valid Constants value.")
}

private fun RpcConstantsSurrogate(value: RpcConstants): RpcConstantsSurrogate = with(value) {
    RpcConstantsSurrogate(
        proofOfWorkNonceSize,
        nonceLength,
        maxAnonOpsPerBlock,
        maxOperationDataLength,
        maxProposalsPerDelegate,
        maxMichelineNodeCount,
        maxMichelineBytesLimit,
        maxAllowedGlobalConstantsDepth,
        cacheLayout,
        cacheLayoutSize,
        michelsonMaximumTypeSize,
        preservedCycles,
        blocksPerCycle,
        blocksPerCommitment,
        blocksPerStakeSnapshot,
        blocksPerVotingPeriod,
        cyclesPerVotingPeriod,
        hardGasLimitPerOperation,
        hardGasLimitPerBlock,
        proofOfWorkThreshold,
        tokensPerRoll,
        seedNonceRevelationTip,
        originationSize,
        bakingRewardFixedPortion,
        bakingRewardBonusPerSlot,
        endorsingRewardPerSlot,
        costPerByte,
        hardStorageLimitPerOperation,
        quorumMin,
        quorumMax,
        minProposalQuorum,
        liquidityBakingSubsidy,
        liquidityBakingSunsetLevel,
        liquidityBakingEscapeEmaThreshold,
        liquidityBakingToggleEmaThreshold,
        maxOperationsTimeToLive,
        minimalBlockDelay,
        delayIncrementPerRound,
        consensusCommitteeSize,
        consensusThreshold,
        minimalParticipationRatio,
        maxSlashingPeriod,
        frozenDepositsPercentage,
        doubleBakingPunishment,
        ratioOfFrozenDepositsSlashedPerDoubleEndorsement,
        delegateSelection,
        initialSeed,
        cacheScriptSize,
        cacheStakeDistributionCycles,
        cacheSamplerStateCycles,
        txRollupEnable,
        txRollupOriginationSize,
        txRollupHardSizeLimitPerInbox,
        txRollupHardSizeLimitPerMessage,
        txRollupMaxWithdrawalsPerBatch,
        txRollupCommitmentBond,
        txRollupFinalityPeriod,
        txRollupWithdrawPeriod,
        txRollupMaxInboxesCount,
        txRollupMaxMessagesPerInbox,
        txRollupMaxCommitmentsCount,
        txRollupCostPerByteEmaFactor,
        txRollupMaxTicketPayloadSize,
        txRollupRejectionMaxProofSize,
        txRollupSunsetLevel,
        scRollupEnable,
        scRollupOriginationSize,
        scRollupChallengeWindowInBlocks,
        scRollupMaxAvailableMessages,
    )
}
