package it.airgap.tezos.rpc.active.block

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.PublicKeyEncoded
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.rpc.type.block.RpcBlock
import it.airgap.tezos.rpc.type.block.RpcFullBlockHeader
import it.airgap.tezos.rpc.type.constants.RpcConstants
import it.airgap.tezos.rpc.type.contract.RpcScript
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import it.airgap.tezos.rpc.type.contract.RpcUnreachableEntrypoint
import it.airgap.tezos.rpc.type.operation.*
import it.airgap.tezos.rpc.type.sapling.RpcSaplingStateDiff
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- ../<block_id> --

@Serializable
@JvmInline
public value class GetBlockResponse(public val block: RpcBlock)

// -- ../<block_id>/context/big_maps/<big_map_id> --

@Serializable
@JvmInline
public value class GetBigMapResponse(public val values: List<MichelineNode>)

// -- ../<block_id>/context/big_maps/<big_map_id>/<script_expr> --

@Serializable
@JvmInline
public value class GetBigMapValueResponse(public val value: MichelineNode)

// -- ../<block_id>/context/constants --

@Serializable
@JvmInline
public value class GetConstantsResponse(public val constants: RpcConstants)

// -- ../<block_id>/context/contracts/<contract_id> --

@Serializable
public data class GetContractDetailsResponse(
    public val balance: String,
    public val delegate: @Contextual ImplicitAddress? = null,
    public val script: RpcScript? = null,
    public val counter: String? = null,
)

// -- ../<block_id>/context/contracts/<contract_id>/balance --

@Serializable
@JvmInline
public value class GetContractBalanceResponse(public val balance: String)

// -- ../<block_id>/context/contracts/<contract_id>/counter --

@Serializable
@JvmInline
public value class GetContractCounterResponse(public val counter: String? = null)

// -- ../<block_id>/context/contracts/<contract_id>/delegate --

@Serializable
@JvmInline
public value class GetContractDelegateResponse(public val delegate: @Contextual ImplicitAddress? = null)

// -- ../<block_id>/context/contracts/<contract_id>/entrypoints --

@Serializable
public data class GetContractEntrypointsResponse(
    public val unreachable: List<RpcUnreachableEntrypoint> = emptyList(),
    public val entrypoints: Map<String, MichelineNode>,
)

// -- ../<block_id>/context/contracts/<contract_id>/entrypoints/<string> --

@Serializable
@JvmInline
public value class GetContractEntrypointResponse(public val entrypoint: MichelineNode)

// -- ../<block_id>/context/contracts/<contract_id>/manager_key --

@Serializable
@JvmInline
public value class GetContractManagerKeyResponse(public val manager: @Contextual PublicKeyEncoded? = null)

// -- ../<block_id>/context/contracts/<contract_id>/script --

@Serializable
@JvmInline
public value class GetContractScriptResponse(public val script: RpcScript? = null)

// -- ../<block_id>/context/contracts/<contract_id>/script/normalized --

@Serializable
public data class GetContractNormalizedScriptRequest(@SerialName("unparsing_mode") public val unparsingMode: RpcScriptParsing)

@Serializable
@JvmInline
public value class GetContractNormalizedScriptResponse(public val script: RpcScript? = null)

// -- ../<block_id>/context/contracts/<contract_id>/single_sapling_get_diff --

@Serializable
@JvmInline
public value class GetContractSaplingStateDiffResponse(public val stateDiff: RpcSaplingStateDiff)

// -- ../<block_id>/context/contracts/<contract_id>/storage --

@Serializable
@JvmInline
public value class GetContractStorageResponse(public val storage: MichelineNode)

// -- ../<block_id>/context/delegates/<pkh> --

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

// -- ../<block_id>/context/delegates/<pkh>/current_frozen_deposits --

@Serializable
@JvmInline
public value class GetDelegateCurrentFrozenDepositsResponse(public val currentFrozenDeposits: String)

// -- ../<block_id>/context/delegates/<pkh>/deactivated --

@Serializable
@JvmInline
public value class GetDelegateDeactivatedStatusResponse(public val isDeactivated: Boolean)

// -- ../<block_id>/context/delegates/<pkh>/delegated_balance --

@Serializable
@JvmInline
public value class GetDelegateDelegatedBalanceResponse(public val delegatedBalance: String)

// -- ../<block_id>/context/delegates/<pkh>/delegated_balance --

@Serializable
@JvmInline
public value class GetDelegateDelegatedContractsResponse(public val delegatedContracts: List<@Contextual Address>)

// -- ../<block_id>/context/delegates/<pkh>/frozen_deposits --

@Serializable
@JvmInline
public value class GetDelegateFrozenDepositsResponse(public val frozenDeposits: String)

// -- ../<block_id>/context/delegates/<pkh>/frozen_deposits_limit --

@Serializable
@JvmInline
public value class GetDelegateFrozenDepositsLimitResponse(public val frozenDepositsLimit: String?)

// -- ../<block_id>/context/delegates/<pkh>/full_balance --

@Serializable
@JvmInline
public value class GetDelegateFullBalanceResponse(public val balance: String)

// -- ../<block_id>/context/delegates/<pkh>/grace_period --

@Serializable
@JvmInline
public value class GetDelegateGracePeriodResponse(public val gracePeriod: Int)

// -- ../<block_id>/context/delegates/<pkh>/participation --

@Serializable
public data class GetDelegateParticipationResponse(
    @SerialName("expected_cycle_activity") public val expectedCycleActivity: Int,
    @SerialName("minimal_cycle_activity") public val minimalCycleActivity: Int,
    @SerialName("missed_slots") public val missedSlots: Int,
    @SerialName("missed_levels") public val missedLevels: Int,
    @SerialName("remaining_allowed_missed_slots") public val remainingAllowedMissedSlots: Int,
    @SerialName("expected_endorsing_rewards") public val expectedEndorsingRewards: String,
)

// -- ../<block_id>/context/delegates/<pkh>/staking_balance --

@Serializable
@JvmInline
public value class GetDelegateStakingBalanceResponse(public val stakingBalance: String)

// -- ../<block_id>/context/delegates/<pkh>/voting_power --

@Serializable
@JvmInline
public value class GetDelegateVotingPowerResponse(public val votingPower: Int)

// -- ../<block_id>/header --

@Serializable
@JvmInline
public value class GetBlockHeaderResponse(public val header: RpcFullBlockHeader)

// -- ../<block_id>/helpers/preapply/operations --

@Serializable
@JvmInline
public value class PreapplyOperationsRequest(public val operations: List<RpcApplicableOperation>)

@Serializable
@JvmInline
public value class PreapplyOperationsResponse(public val operations: List<RpcAppliedOperation>)

// -- ../<block_id>/helpers/scripts/run_operation --

@Serializable
@JvmInline
public value class RunOperationRequest(public val operation: RpcRunnableOperation)

@Serializable
@JvmInline
public value class RunOperationResponse(public val contents: List<RpcOperationContent>)

// -- ../<block_id>/operations --

@Serializable
@JvmInline
public value class GetBlockOperationsResponse(public val operations: List<List<RpcOperation>>)

// -- ../<block_id>/context/sapling/<sapling_state_id>/get_diff

@Serializable
@JvmInline
public value class GetSaplingStateDiffResponse(public val stateDiff: RpcSaplingStateDiff)