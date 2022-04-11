package it.airgap.tezos.rpc.active.block

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.PublicKeyHashEncoded
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.rpc.active.data.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.type.operation.RpcApplicableOperation
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

public interface Block {
    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetBlockResponse

    public val context: Context
    public val header: Header
    public val helpers: Helpers
    public val operations: Operations

    public interface Context {

        public val bigMaps: BigMaps
        public val constants: Constants
        public val contracts: Contracts
        public val delegates: Delegates
        public val sapling: Sapling

        public interface BigMaps {

            public fun bigMapId(bigMapId: String): BigMap

            public interface BigMap {
                public suspend fun get(offset: UInt? = null, length: UInt? = null, headers: List<HttpHeader> = emptyList()): GetBigMapValuesResponse

                public fun scriptExpr(scriptExpr: ScriptExprHash): Value

                public interface Value {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetBigMapValueResponse
                }
            }
        }

        public interface Constants {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetConstantsResponse
        }

        public interface Contracts {
            public suspend fun contractId(contractId: Address): Contract

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

                public interface Balance {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractBalanceResponse
                }

                public interface Counter {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractCounterResponse
                }

                public interface Delegate {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractDelegateResponse
                }

                public interface Entrypoints {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractEntrypointsResponse

                    public fun string(string: String): Entrypoint

                    public interface Entrypoint {
                        public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractEntrypointTypeResponse
                    }
                }

                public interface ManagerKey {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractManagerResponse
                }

                public interface Script {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractScriptResponse
                }

                public interface SingleSaplingGetDiff {
                    public suspend fun get(commitmentOffset: ULong? = null, nullifierOffset: ULong? = null, headers: List<HttpHeader> = emptyList()): GetContractSaplingStateDiffResponse
                }

                public interface Storage {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetContractStorageResponse
                }
            }
        }

        public interface Delegates {
            public fun pkh(publicKeyHash: PublicKeyHashEncoded): Delegate

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

                public interface CurrentFrozenDeposits {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateCurrentFrozenDepositsResponse
                }

                public interface Deactivated {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateDeactivatedStatusResponse
                }

                public interface DelegatedBalance {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateDelegatedBalanceResponse
                }

                public interface DelegatedContracts {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateDelegatedContractsResponse
                }

                public interface FrozenDeposits {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateFrozenDepositsResponse
                }

                public interface FrozenDepositsLimit {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateFrozenDepositsLimitResponse
                }

                public interface FullBalance {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateFullBalanceResponse
                }

                public interface GracePeriod {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateGracePeriodResponse
                }

                public interface Participation {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateParticipationResponse
                }

                public interface StakingBalance {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateStakingBalanceResponse
                }

                public interface VotingPower {
                    public suspend fun get(headers: List<HttpHeader> = emptyList()): GetDelegateVotingPowerResponse
                }
            }
        }

        public interface Sapling {
            public fun saplingStateId(stateId: String): State

            public interface State {
                public val getDiff: GetDiff

                public interface GetDiff {
                    public suspend fun get(commitmentOffset: ULong? = null, nullifierOffset: ULong? = null, headers: List<HttpHeader> = emptyList()): GetSaplingStateDiffResponse
                }
            }
        }
    }

    public interface Header {
        public suspend fun get(headers: List<HttpHeader> = emptyList()): GetBlockHeaderResponse
    }

    public interface Helpers {
        public val preapply: Preapply
        public val scripts: Scripts

        public interface Preapply {
            public val operations: Operations

            public interface Operations {
                public suspend fun post(operations: List<RpcApplicableOperation>, headers: List<HttpHeader> = emptyList()): PreapplyOperationsResponse
            }
        }

        public interface Scripts {
            public val runOperation: RunOperation

            public interface RunOperation {
                public suspend fun post(operation: RpcRunnableOperation, headers: List<HttpHeader> = emptyList()): RunOperationResponse
            }
        }
    }

    public interface Operations {
        public suspend fun get(headers: List<HttpHeader> = emptyList()): GetBlockOperationsResponse
    }
}