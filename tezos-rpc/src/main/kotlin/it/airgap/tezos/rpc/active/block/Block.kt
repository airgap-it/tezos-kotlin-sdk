package it.airgap.tezos.rpc.active.block

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.PublicKeyHash
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import it.airgap.tezos.rpc.type.operation.RpcApplicableOperation
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

/**
 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>`
 */
public interface Block {
    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetBlockResponse

    public val context: Context
    public val header: Header
    public val helpers: Helpers
    public val operations: Operations

    /**
     * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context`
     */
    public interface Context {

        public val bigMaps: BigMaps
        public val constants: Constants
        public val contracts: Contracts
        public val delegates: Delegates
        public val sapling: Sapling

        /**
         * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/big_maps`
         */
        public interface BigMaps {

            public operator fun invoke(bigMapId: String): BigMap

            /**
             * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/big_maps/<big_map_id>`
             */
            public interface BigMap {
                public suspend fun get(offset: UInt? = null, length: UInt? = null, headers: List<HttpHeader> = emptyList()): GetBigMapResponse

                public operator fun invoke(scriptExpr: ScriptExprHash): Value

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/big_maps/<big_map_id>/<script_expr>`
                 */
                public interface Value {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetBigMapValueResponse
                }
            }
        }

        /**
         * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/constants`
         */
        public interface Constants {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetConstantsResponse
        }

        /**
         * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts`
         */
        public interface Contracts {
            public operator fun invoke(contractId: Address): Contract

            /**
             * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>`
             */
            public interface Contract {
                public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractDetailsResponse

                public val balance: Balance
                public val counter: Counter
                public val delegate: Delegate
                public val entrypoints: Entrypoints
                public val managerKey: ManagerKey
                public val script: Script
                public val singleSaplingGetDiff: SingleSaplingGetDiff
                public val storage: Storage

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>/balance`
                 */
                public interface Balance {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractBalanceResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>/counter`
                 */
                public interface Counter {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractCounterResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>/delegate`
                 */
                public interface Delegate {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractDelegateResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>/entrypoints`
                 */
                public interface Entrypoints {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractEntrypointsResponse

                    public operator fun invoke(string: String): Entrypoint

                    /**
                     * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>/entrypoints/<string>`
                     */
                    public interface Entrypoint {
                        public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractEntrypointResponse
                    }
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>/manager_key`
                 */
                public interface ManagerKey {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractManagerKeyResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>/script`
                 */
                public interface Script {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractScriptResponse

                    public val normalized: Normalized

                    /**
                     * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>/script/normalized`
                     */
                    public interface Normalized {
                        public suspend fun post(unparsingMode: RpcScriptParsing, headers: List<HttpHeader> = emptyList()): GetContractNormalizedScriptResponse
                    }
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>/single_sapling_get_diff`
                 */
                public interface SingleSaplingGetDiff {
                    public suspend fun get(commitmentOffset: ULong? = null, nullifierOffset: ULong? = null, headers: List<HttpHeader> = emptyList()): GetContractSaplingStateDiffResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>/storage`
                 */
                public interface Storage {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractStorageResponse

                    public val normalized: Normalized

                    /**
                     * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/contracts/<contract_id>/storage/normalized`
                     */
                    public interface Normalized {
                        public suspend fun post(unparsingMode: RpcScriptParsing, headers: List<HttpHeader> = emptyList()): GetContractNormalizedStorageResponse
                    }
                }
            }
        }

        /**
         * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates`
         */
        public interface Delegates {
            public operator fun invoke(publicKeyHash: PublicKeyHash): Delegate

            /**
             * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>`
             */
            public interface Delegate {
                public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateDetailsResponse

                public val currentFrozenDeposits: CurrentFrozenDeposits
                public val deactivated: Deactivated
                public val delegatedBalance: DelegatedBalance
                public val delegatedContracts: DelegatedContracts
                public val frozenDeposits: FrozenDeposits
                public val frozenDepositsLimit: FrozenDepositsLimit
                public val fullBalance: FullBalance
                public val gracePeriod: GracePeriod
                public val participation: Participation
                public val stakingBalance: StakingBalance
                public val votingPower: VotingPower

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>/current_frozen_deposits`
                 */
                public interface CurrentFrozenDeposits {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateCurrentFrozenDepositsResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>/deactivated`
                 */
                public interface Deactivated {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateDeactivatedStatusResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>/delegated_balance`
                 */
                public interface DelegatedBalance {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateDelegatedBalanceResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>/delegated_contracts`
                 */
                public interface DelegatedContracts {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateDelegatedContractsResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>/frozen_deposits`
                 */
                public interface FrozenDeposits {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateFrozenDepositsResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>/frozen_deposits_limit`
                 */
                public interface FrozenDepositsLimit {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateFrozenDepositsLimitResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>/full_balance`
                 */
                public interface FullBalance {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateFullBalanceResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>/grace_period`
                 */
                public interface GracePeriod {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateGracePeriodResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>/participation`
                 */
                public interface Participation {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateParticipationResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>/staking_balance`
                 */
                public interface StakingBalance {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateStakingBalanceResponse
                }

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/delegates/<pkh>/voting_power`
                 */
                public interface VotingPower {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateVotingPowerResponse
                }
            }
        }

        /**
         * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/sapling`
         */
        public interface Sapling {
            public operator fun invoke(stateId: String): State

            /**
             * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/context/sapling/<sapling_state_id>`
             */
            public interface State {
                public val getDiff: GetDiff

                /**
                 * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/sapling/<sapling_state_id>/get_diff`
                 */
                public interface GetDiff {
                    public suspend fun get(commitmentOffset: ULong? = null, nullifierOffset: ULong? = null, headers: List<HttpHeader> = emptyList()): GetSaplingStateDiffResponse
                }
            }
        }
    }

    /**
     * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/header`
     */
    public interface Header {
        public suspend fun get(headers: List<HttpHeader> = emptyList()): GetBlockHeaderResponse
    }

    /**
     * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/helpers`
     */
    public interface Helpers {
        public val preapply: Preapply
        public val scripts: Scripts

        /**
         * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/helpers/preapply`
         */
        public interface Preapply {
            public val operations: Operations

            /**
             * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/helpers/preapply/operations`
             */
            public interface Operations {
                public suspend fun post(operations: List<RpcApplicableOperation>, headers: List<HttpHeader> = emptyList()): PreapplyOperationsResponse
            }
        }

        /**
         * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/helpers/scripts`
         */
        public interface Scripts {
            public val runOperation: RunOperation

            /**
             * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/helpers/scripts/run_operation`
             */
            public interface RunOperation {
                public suspend fun post(operation: RpcRunnableOperation, headers: List<HttpHeader> = emptyList()): RunOperationResponse
            }
        }
    }

    /**
     * [Active RPCs](https://tezos.gitlab.io/active/rpc.html): `../<block_id>/operations`
     */
    public interface Operations {
        public suspend fun get(headers: List<HttpHeader> = emptyList()): GetBlockOperationsResponse
    }
}