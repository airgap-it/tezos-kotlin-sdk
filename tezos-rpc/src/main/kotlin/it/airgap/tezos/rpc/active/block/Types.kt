package it.airgap.tezos.rpc.active.block

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.PublicKey
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.operation.contract.Script
import it.airgap.tezos.rpc.type.block.RpcBlock
import it.airgap.tezos.rpc.type.block.RpcFullBlockHeader
import it.airgap.tezos.rpc.type.constants.RpcConstants
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import it.airgap.tezos.rpc.type.contract.RpcUnreachableEntrypoint
import it.airgap.tezos.rpc.type.operation.*
import it.airgap.tezos.rpc.type.sapling.RpcSaplingStateDiff
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- ../<block_id> --

/**
 * Response from [`GET ../<block_id>`](https://tezos.gitlab.io/active/rpc.html#get-block-id)
 */
@Serializable
@JvmInline
public value class GetBlockResponse(public val block: RpcBlock)

// -- ../<block_id>/context/big_maps/<big_map_id> --

/**
 * Response from [`GET ../<block_id>/context/big_maps/<big_map_id>?[offset=<uint>]&[length=<uint>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-big-maps-big-map-id)
 */
@Serializable
@JvmInline
public value class GetBigMapResponse(public val values: List<Micheline>)

// -- ../<block_id>/context/big_maps/<big_map_id>/<script_expr> --

/**
 * Response from [`GET ../<block_id>/context/big_maps/<big_map_id>/<script_expr>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-big-maps-big-map-id-script-expr)
 */
@Serializable
@JvmInline
public value class GetBigMapValueResponse(public val value: Micheline? = null)

// -- ../<block_id>/context/constants --

/**
 * Response from [`GET ../<block_id>/context/constants`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-constants)
 */
@Serializable
@JvmInline
public value class GetConstantsResponse(public val constants: RpcConstants)

// -- ../<block_id>/context/contracts/<contract_id> --

/**
 * Response from [`GET ../<block_id>/context/contracts/<contract_id>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts)
 */
@Serializable
public data class GetContractDetailsResponse(
    public val balance: String,
    public val delegate: @Contextual ImplicitAddress? = null,
    public val script: @Contextual Script? = null,
    public val counter: String? = null,
)

// -- ../<block_id>/context/contracts/<contract_id>/balance --

/**
 * Response from [`GET ../<block_id>/context/contracts/<contract_id>/balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-balance)
 */
@Serializable
@JvmInline
public value class GetContractBalanceResponse(public val balance: String)

// -- ../<block_id>/context/contracts/<contract_id>/counter --

/**
 * Response from [`GET ../<block_id>/context/contracts/<contract_id>/counter`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-counter)
 */
@Serializable
@JvmInline
public value class GetContractCounterResponse(public val counter: String? = null)

// -- ../<block_id>/context/contracts/<contract_id>/delegate --

/**
 * Response from [`GET ../<block_id>/context/contracts/<contract_id>/delegate`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-delegate)
 */
@Serializable
@JvmInline
public value class GetContractDelegateResponse(public val delegate: @Contextual ImplicitAddress? = null)

// -- ../<block_id>/context/contracts/<contract_id>/entrypoints --

/**
 * Response from [`GET ../<block_id>/context/contracts/<contract_id>/entrypoints`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-entrypoints)
 */
@Serializable
public data class GetContractEntrypointsResponse(
    public val unreachable: List<RpcUnreachableEntrypoint> = emptyList(),
    public val entrypoints: Map<String, Micheline>,
)

// -- ../<block_id>/context/contracts/<contract_id>/entrypoints/<string> --

/**
 * Response from [`GET ../<block_id>/context/contracts/<contract_id>/entrypoints/<string>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-entrypoints-string)
 */
@Serializable
@JvmInline
public value class GetContractEntrypointResponse(public val entrypoint: Micheline)

// -- ../<block_id>/context/contracts/<contract_id>/manager_key --

/**
 * Response from [`GET ../<block_id>/context/contracts/<contract_id>/manager-key`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-manager-key)
 */
@Serializable
@JvmInline
public value class GetContractManagerKeyResponse(public val manager: @Contextual PublicKey? = null)

// -- ../<block_id>/context/contracts/<contract_id>/script --

/**
 * Response from [`GET ../<block_id>/context/contracts/<contract_id>/script`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-script)
 */
@Serializable
@JvmInline
public value class GetContractScriptResponse(public val script: @Contextual Script? = null)

// -- ../<block_id>/context/contracts/<contract_id>/script/normalized --

/**
 * Request for `POST ../<block_id>/context/contracts/<contract_id>/script/normalized`
 */
@Serializable
public data class GetContractNormalizedScriptRequest(@SerialName("unparsing_mode") public val unparsingMode: RpcScriptParsing)

/**
 * Response from `POST ../<block_id>/context/contracts/<contract_id>/script/normalized`
 */
@Serializable
@JvmInline
public value class GetContractNormalizedScriptResponse(public val script: @Contextual Script? = null)

// -- ../<block_id>/context/contracts/<contract_id>/single_sapling_get_diff --

/**
 * Response from [`GET ../<block_id>/context/contracts/<contract_id>/single_sapling_get_diff?[offset_commitment=<uint63>]&[offset_nullifier=<uint63>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-single-sapling-get-diff)
 */
@Serializable
@JvmInline
public value class GetContractSaplingStateDiffResponse(public val stateDiff: RpcSaplingStateDiff)

// -- ../<block_id>/context/contracts/<contract_id>/storage --

/**
 * Response from [`GET ../<block_id>/context/contracts/<contract_id>/storage`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-storage)
 */
@Serializable
@JvmInline
public value class GetContractStorageResponse(public val storage: Micheline? = null)

// -- ../<block_id>/context/contracts/<contract_id>/storage/normalized --

/**
 * Request for `POST ../<block_id>/context/contracts/<contract_id>/storage/normalized`
 */
@Serializable
public data class GetContractNormalizedStorageRequest(@SerialName("unparsing_mode") public val unparsingMode: RpcScriptParsing)

/**
 * Response from `POST ../<block_id>/context/contracts/<contract_id>/storage/normalized`
 */
@Serializable
@JvmInline
public value class GetContractNormalizedStorageResponse(public val storage: Micheline? = null)

// -- ../<block_id>/context/delegates/<pkh> --

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh)
 */
@Serializable
public data class GetDelegateDetailsResponse(
    @SerialName("full_balance") public val fullBalance: String,
    @SerialName("current_frozen_deposits") public val currentFrozenDeposits: String,
    @SerialName("frozen_deposits") public val frozenDeposits: String,
    @SerialName("staking_balance") public val stakingBalance: String,
    @SerialName("frozen_deposits_limit") public val frozenDepositsLimit: String? = null,
    @SerialName("delegated_contracts") public val delegatedContracts: List<@Contextual Address>,
    @SerialName("delegated_balance") public val delegatedBalance: String,
    public val deactivated: Boolean,
    @SerialName("grace_period") public val gracePeriod: Int,
    @SerialName("voting_power") public val votingPower: Long,
)

// -- ../<block_id>/context/delegates/<pkh>/current_frozen_deposits --

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>/current_frozen_deposits`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-current-frozen-deposits)
 */
@Serializable
@JvmInline
public value class GetDelegateCurrentFrozenDepositsResponse(public val currentFrozenDeposits: String)

// -- ../<block_id>/context/delegates/<pkh>/deactivated --

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>/deactivated`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-deactivated)
 */
@Serializable
@JvmInline
public value class GetDelegateDeactivatedStatusResponse(public val isDeactivated: Boolean)

// -- ../<block_id>/context/delegates/<pkh>/delegated_balance --

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>/delegated_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-delegated-balance)
 */
@Serializable
@JvmInline
public value class GetDelegateDelegatedBalanceResponse(public val delegatedBalance: String)

// -- ../<block_id>/context/delegates/<pkh>/delegated_balance --

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>/delegated_contracts`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-delegated-contracts)
 */
@Serializable
@JvmInline
public value class GetDelegateDelegatedContractsResponse(public val delegatedContracts: List<@Contextual Address>)

// -- ../<block_id>/context/delegates/<pkh>/frozen_deposits --

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>/frozen_deposits`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-frozen-deposits)
 */
@Serializable
@JvmInline
public value class GetDelegateFrozenDepositsResponse(public val frozenDeposits: String)

// -- ../<block_id>/context/delegates/<pkh>/frozen_deposits_limit --

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>/frozen_deposits_limit`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-frozen-deposits-limit)
 */
@Serializable
@JvmInline
public value class GetDelegateFrozenDepositsLimitResponse(public val frozenDepositsLimit: String?)

// -- ../<block_id>/context/delegates/<pkh>/full_balance --

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>/full_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-full-balance)
 */
@Serializable
@JvmInline
public value class GetDelegateFullBalanceResponse(public val balance: String)

// -- ../<block_id>/context/delegates/<pkh>/grace_period --

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>/grace_period`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-grace-period)
 */
@Serializable
@JvmInline
public value class GetDelegateGracePeriodResponse(public val gracePeriod: Int)

// -- ../<block_id>/context/delegates/<pkh>/participation --

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>/participation`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-participation)
 */
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

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>/staking_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-staking-balance)
 */
@Serializable
@JvmInline
public value class GetDelegateStakingBalanceResponse(public val stakingBalance: String)

// -- ../<block_id>/context/delegates/<pkh>/voting_power --

/**
 * Response from [`GET ../<block_id>/context/delegates/<pkh>/voting_power`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-voting-power)
 */
@Serializable
@JvmInline
public value class GetDelegateVotingPowerResponse(public val votingPower: Long)


// -- ../<block_id>/context/sapling/<sapling_state_id>/get_diff

/**
 * Response from [`GET ../<block_id>/context/sapling/<sapling_state_id>/get_diff?[offset_commitment=<uint63>]&[offset_nullifier=<uint63>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-sapling-sapling-state-id-get-diff)
 */
@Serializable
@JvmInline
public value class GetSaplingStateDiffResponse(public val stateDiff: RpcSaplingStateDiff)

// -- ../<block_id>/header --

/**
 * Response from [`GET ../<block_id>/header`](https://tezos.gitlab.io/active/rpc.html#get-block-id-header)
 */
@Serializable
@JvmInline
public value class GetBlockHeaderResponse(public val header: RpcFullBlockHeader)

// -- ../<block_id>/helpers/preapply/operations --

/**
 * Request for [`POST ../<block_id>/helpers/preapply/operations`](https://tezos.gitlab.io/active/rpc.html#post-block-id-helpers-preapply-operations)
 */
@Serializable
@JvmInline
public value class PreapplyOperationsRequest(public val operations: List<RpcApplicableOperation>)

/**
 * Response from [`POST ../<block_id>/helpers/preapply/operations`](https://tezos.gitlab.io/active/rpc.html#post-block-id-helpers-preapply-operations)
 */
@Serializable
@JvmInline
public value class PreapplyOperationsResponse(public val operations: List<RpcAppliedOperation>)

// -- ../<block_id>/helpers/scripts/run_operation --

/**
 * Request for `POST ../<block_id>/helpers/scripts/run_operation`
 */
@Serializable
@JvmInline
public value class RunOperationRequest(public val operation: RpcRunnableOperation)

/**
 * Response from `POST ../<block_id>/helpers/scripts/run_operation`
 */
@Serializable
@JvmInline
public value class RunOperationResponse(public val contents: List<RpcOperationContent>)

// -- ../<block_id>/operations --

/**
 * Response from [`GET ../<block_id>/operations`](https://tezos.gitlab.io/active/rpc.html#get-block-id-operations)
 */
@Serializable
@JvmInline
public value class GetBlockOperationsResponse(public val operations: List<List<RpcOperation>>)