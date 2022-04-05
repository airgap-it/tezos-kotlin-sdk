package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.PublicKeyEncoded
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.rpc.type.RpcError
import it.airgap.tezos.rpc.type.bigmap.RpcBigMapDiff
import it.airgap.tezos.rpc.type.contract.RpcParameters
import it.airgap.tezos.rpc.type.contract.RpcScript
import it.airgap.tezos.rpc.type.storage.RpcLazyStorageDiff
import kotlinx.serialization.*
import kotlinx.serialization.json.JsonClassDiscriminator

// -- RpcOperationResult --

@OptIn(ExperimentalSerializationApi::class)
public sealed class RpcOperationResult {

    @Serializable
    @JsonClassDiscriminator(Reveal.CLASS_DISCRIMINATOR)
    public sealed class Reveal : RpcOperationResult() {

        @Transient
        public open val consumedGas: String? = null

        @Transient
        public open val consumedMilligas: String? = null

        @Transient
        public open val errors: List<RpcError>? = null

        @Serializable
        @SerialName(Applied.KIND)
        public data class Applied(
            @SerialName("consumed_gas") override val consumedGas: String? = null,
            @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
        ) : Reveal() {
            public companion object {
                internal const val KIND = "applied"
            }
        }

        @Serializable
        @SerialName(Failed.KIND)
        public data class Failed(override val errors: List<RpcError>) : Reveal() {
            public companion object {
                internal const val KIND = "failed"
            }
        }

        @Serializable
        @SerialName(Skipped.KIND)
        public object Skipped : Reveal() {
            internal const val KIND = "skipped"
        }

        @Serializable
        @SerialName(Backtracked.KIND)
        public data class Backtracked(
            override val errors: List<RpcError>? = null,
            @SerialName("consumed_gas") override val consumedGas: String? = null,
            @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
        ) : Reveal() {
            public companion object {
                internal const val KIND = "backtracked"
            }
        }

        public companion object {
            internal const val CLASS_DISCRIMINATOR = "status"
        }
    }

    @Serializable
    @JsonClassDiscriminator(Transaction.CLASS_DISCRIMINATOR)
    public sealed class Transaction : RpcOperationResult() {
        @Transient
        public open val storage: MichelineNode? = null

        @Transient
        public open val bigMapDiff: List<RpcBigMapDiff>? = null

        @Transient
        public open val balanceUpdates: List<RpcBalanceUpdate>? = null

        @Transient
        public open val originatedContracts: List<Address>? = null

        @Transient
        public open val consumedGas: String? = null

        @Transient
        public open val consumedMilligas: String? = null

        @Transient
        public open val storageSize: String? = null

        @Transient
        public open val paidStorageSizeDiff: String? = null

        @Transient
        public open val allocatedDestinationContract: Boolean? = null

        @Transient
        public open val lazyStorageDiff: List<RpcLazyStorageDiff>? = null

        @Transient
        public open val errors: List<RpcError>? = null

        @Serializable
        @SerialName(Applied.KIND)
        public data class Applied(
            override val storage: MichelineNode? = null,
            @SerialName("big_map_diff") override val bigMapDiff: List<RpcBigMapDiff>? = null,
            @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate>? = null,
            @SerialName("originated_contracts") override val originatedContracts: List<Address>? = null,
            @SerialName("consumed_gas") override val consumedGas: String? = null,
            @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
            @SerialName("storage_size") override val storageSize: String? = null,
            @SerialName("paid_storage_size_diff") override val paidStorageSizeDiff: String? = null,
            @SerialName("allocated_destination_contract") override val allocatedDestinationContract: Boolean? = null,
            @SerialName("lazy_storage_diff") override val lazyStorageDiff: List<RpcLazyStorageDiff>? = null,
        ) : Transaction() {
            public companion object {
                internal const val KIND = "applied"
            }
        }

        @Serializable
        @SerialName(Failed.KIND)
        public data class Failed(override val errors: List<RpcError>) : Transaction() {
            public companion object {
                internal const val KIND = "failed"
            }
        }

        @Serializable
        @SerialName(Skipped.KIND)
        public object Skipped : Transaction() {
            internal const val KIND = "skipped"
        }

        @Serializable
        @SerialName(Backtracked.KIND)
        public data class Backtracked(
            override val errors: List<RpcError>? = null,
            override val storage: MichelineNode? = null,
            @SerialName("big_map_diff") override val bigMapDiff: List<RpcBigMapDiff>? = null,
            @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate>? = null,
            @SerialName("originated_contracts") override val originatedContracts: List<Address>? = null,
            @SerialName("consumed_gas") override val consumedGas: String? = null,
            @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
            @SerialName("storage_size") override val storageSize: String? = null,
            @SerialName("paid_storage_size_diff") override val paidStorageSizeDiff: String? = null,
            @SerialName("allocated_destination_contract") override val allocatedDestinationContract: Boolean? = null,
            @SerialName("lazy_storage_diff") override val lazyStorageDiff: List<RpcLazyStorageDiff>? = null,
        ) : Transaction() {
            public companion object {
                internal const val KIND = "backtracked"
            }
        }

        public companion object {
            internal const val CLASS_DISCRIMINATOR = "status"
        }
    }

    @Serializable
    @JsonClassDiscriminator(Origination.CLASS_DISCRIMINATOR)
    public sealed class Origination : RpcOperationResult() {

        @Transient
        public open val bigMapDiff: List<RpcBigMapDiff>? = null

        @Transient
        public open val balanceUpdates: List<RpcBalanceUpdate>? = null

        @Transient
        public open val originatedContracts: List<Address>? = null

        @Transient
        public open val consumedGas: String? = null

        @Transient
        public open val consumedMilligas: String? = null

        @Transient
        public open val storageSize: String? = null

        @Transient
        public open val paidStorageSizeDiff: String? = null

        @Transient
        public open val lazyStorageDiff: List<RpcLazyStorageDiff>? = null

        @Transient
        public open val errors: List<RpcError>? = null

        @Serializable
        @SerialName(Applied.KIND)
        public data class Applied(
            @SerialName("big_map_diff") override val bigMapDiff: List<RpcBigMapDiff>? = null,
            @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate>? = null,
            @SerialName("originated_contracts") override val originatedContracts: List<Address>? = null,
            @SerialName("consumed_gas") override val consumedGas: String? = null,
            @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
            @SerialName("storage_size") override val storageSize: String? = null,
            @SerialName("paid_storage_size_diff") override val paidStorageSizeDiff: String? = null,
            @SerialName("lazy_storage_diff") override val lazyStorageDiff: List<RpcLazyStorageDiff>? = null,
        ) : Origination() {
            public companion object {
                internal const val KIND = "applied"
            }
        }

        @Serializable
        @SerialName(Failed.KIND)
        public data class Failed(override val errors: List<RpcError>) : Origination() {
            public companion object {
                internal const val KIND = "failed"
            }
        }

        @Serializable
        @SerialName(Skipped.KIND)
        public object Skipped : Origination() {
            internal const val KIND = "skipped"
        }

        @Serializable
        @SerialName(Backtracked.KIND)
        public data class Backtracked(
            override val errors: List<RpcError>? = null,
            @SerialName("big_map_diff") override val bigMapDiff: List<RpcBigMapDiff>? = null,
            @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate>? = null,
            @SerialName("originated_contracts") override val originatedContracts: List<Address>? = null,
            @SerialName("consumed_gas") override val consumedGas: String? = null,
            @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
            @SerialName("storage_size") override val storageSize: String? = null,
            @SerialName("paid_storage_size_diff") override val paidStorageSizeDiff: String? = null,
            @SerialName("lazy_storage_diff") override val lazyStorageDiff: List<RpcLazyStorageDiff>? = null,
        ) : Origination() {
            public companion object {
                internal const val KIND = "backtracked"
            }
        }

        public companion object {
            internal const val CLASS_DISCRIMINATOR = "status"
        }
    }

    @Serializable
    @JsonClassDiscriminator(Delegation.CLASS_DISCRIMINATOR)
    public sealed class Delegation : RpcOperationResult() {

        @Transient
        public open val consumedGas: String? = null

        @Transient
        public open val consumedMilligas: String? = null

        @Transient
        public open val errors: List<RpcError>? = null

        @Serializable
        @SerialName(Applied.KIND)
        public data class Applied(
            @SerialName("consumed_gas") override val consumedGas: String? = null,
            @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
        ) : Delegation() {
            public companion object {
                internal const val KIND = "applied"
            }
        }

        @Serializable
        @SerialName(Failed.KIND)
        public data class Failed(override val errors: List<RpcError>) : Delegation() {
            public companion object {
                internal const val KIND = "failed"
            }
        }

        @Serializable
        @SerialName(Skipped.KIND)
        public object Skipped : Delegation() {
            internal const val KIND = "skipped"
        }

        @Serializable
        @SerialName(Backtracked.KIND)
        public data class Backtracked(
            override val errors: List<RpcError>? = null,
            @SerialName("consumed_gas") override val consumedGas: String? = null,
            @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
        ) : Delegation() {
            public companion object {
                internal const val KIND = "backtracked"
            }
        }

        public companion object {
            internal const val CLASS_DISCRIMINATOR = "status"
        }
    }

    @Serializable
    @JsonClassDiscriminator(SetDepositsLimit.CLASS_DISCRIMINATOR)
    public sealed class SetDepositsLimit : RpcOperationResult() {

        @Transient
        public open val consumedGas: String? = null

        @Transient
        public open val consumedMilligas: String? = null

        @Transient
        public open val errors: List<RpcError>? = null

        @Serializable
        @SerialName(Applied.KIND)
        public data class Applied(
            @SerialName("consumed_gas") override val consumedGas: String? = null,
            @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
        ) : SetDepositsLimit() {
            public companion object {
                internal const val KIND = "applied"
            }
        }

        @Serializable
        @SerialName(Failed.KIND)
        public data class Failed(override val errors: List<RpcError>) : SetDepositsLimit() {
            public companion object {
                internal const val KIND = "failed"
            }
        }

        @Serializable
        @SerialName(Skipped.KIND)
        public object Skipped : SetDepositsLimit() {
            internal const val KIND = "skipped"
        }

        @Serializable
        @SerialName(Backtracked.KIND)
        public data class Backtracked(
            override val errors: List<RpcError>? = null,
            @SerialName("consumed_gas") override val consumedGas: String? = null,
            @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
        ) : SetDepositsLimit() {
            public companion object {
                internal const val KIND = "backtracked"
            }
        }

        public companion object {
            internal const val CLASS_DISCRIMINATOR = "status"
        }
    }

    @Serializable
    @JsonClassDiscriminator(RegisterGlobalConstant.CLASS_DISCRIMINATOR)
    public sealed class RegisterGlobalConstant : RpcOperationResult() {

        @Transient
        public open val balanceUpdates: List<RpcBalanceUpdate>? = null

        @Transient
        public open val consumedGas: String? = null

        @Transient
        public open val storageSize: String? = null

        @Transient
        public open val globalAddress: @Contextual ScriptExprHash? = null

        @Transient
        public open val errors: List<RpcError>? = null

        @Serializable
        @SerialName(Applied.KIND)
        public data class Applied(
            @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate>,
            @SerialName("consumed_gas") override val consumedGas: String,
            @SerialName("storage_size") override val storageSize: String,
            @SerialName("global_address") override val globalAddress: @Contextual ScriptExprHash,
        ) : RegisterGlobalConstant() {
            public companion object {
                internal const val KIND = "applied"
            }
        }

        @Serializable
        @SerialName(Failed.KIND)
        public data class Failed(override val errors: List<RpcError>) : RegisterGlobalConstant() {
            public companion object {
                internal const val KIND = "failed"
            }
        }

        @Serializable
        @SerialName(Skipped.KIND)
        public object Skipped : RegisterGlobalConstant() {
            internal const val KIND = "skipped"
        }

        @Serializable
        @SerialName(Backtracked.KIND)
        public data class Backtracked(
            override val errors: List<RpcError>? = null,
            @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate>,
            @SerialName("consumed_gas") override val consumedGas: String,
            @SerialName("storage_size") override val storageSize: String,
            @SerialName("global_address") override val globalAddress: @Contextual ScriptExprHash,
        ) : RegisterGlobalConstant() {
            public companion object {
                internal const val KIND = "backtracked"
            }
        }

        public companion object {
            internal const val CLASS_DISCRIMINATOR = "status"
        }
    }
}

// -- RpcInternalOperationResult --

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(RpcInternalOperationResult.CLASS_DISCRIMINATOR)
public sealed class RpcInternalOperationResult {

    @Serializable
    @SerialName(Reveal.KIND)
    public data class Reveal(
        public val source: @Contextual Address,
        public val nonce: UShort,
        @SerialName("public_key") public val publicKey: @Contextual PublicKeyEncoded,
        public val result: RpcOperationResult.Reveal,
    ) : RpcInternalOperationResult() {
        public companion object {
            internal const val KIND = "reveal"
        }
    }

    @Serializable
    @SerialName(Transaction.KIND)
    public data class Transaction(
        public val source: @Contextual Address,
        public val nonce: UShort,
        public val amount: String,
        public val destination: @Contextual Address,
        public val parameters: RpcParameters? = null,
        public val result: RpcOperationResult.Transaction,
    ) : RpcInternalOperationResult() {
        public companion object {
            internal const val KIND = "transaction"
        }
    }

    @Serializable
    @SerialName(Origination.KIND)
    public data class Origination(
        public val source: @Contextual Address,
        public val nonce: UShort,
        public val balance: String,
        public val delegate: @Contextual ImplicitAddress? = null,
        public val script: RpcScript,
        public val result: RpcOperationResult.Origination,
    ) : RpcInternalOperationResult() {
        public companion object {
            internal const val KIND = "origination"
        }
    }

    @Serializable
    @SerialName(Delegation.KIND)
    public data class Delegation(
        public val source: @Contextual Address,
        public val nonce: UShort,
        public val balance: String,
        public val delegate: @Contextual ImplicitAddress? = null,
        public val result: RpcOperationResult.Delegation,
    ) : RpcInternalOperationResult() {
        public companion object {
            internal const val KIND = "delegation"
        }
    }

    @Serializable
    @SerialName(RegisterGlobalConstant.KIND)
    public data class RegisterGlobalConstant(
        public val source: @Contextual Address,
        public val nonce: UShort,
        public val value: MichelineNode,
        public val result: RpcOperationResult.RegisterGlobalConstant,
    ) : RpcInternalOperationResult() {
        public companion object {
            internal const val KIND = "register_global_constant"
        }
    }

    @Serializable
    @SerialName(SetDepositsLimit.KIND)
    public data class SetDepositsLimit(
        public val source: @Contextual Address,
        public val nonce: UShort,
        public val limit: String? = null,
        public val result: RpcOperationResult.SetDepositsLimit,
    ) : RpcInternalOperationResult() {
        public companion object {
            internal const val KIND = "set_deposits_limit"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "kind"
    }
}

// -- RpcSuccessfulManagerOperationResult --

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(RpcSuccessfulManagerOperationResult.CLASS_DISCRIMINATOR)
public sealed class RpcSuccessfulManagerOperationResult {

    @Serializable
    @SerialName(Reveal.KIND)
    public data class Reveal(
        @SerialName("consumed_gas") public val consumedGas: String? = null,
        @SerialName("consumed_milligas") public val consumedMilligas: String? = null,
    ) : RpcSuccessfulManagerOperationResult() {
        public companion object {
            internal const val KIND = "reveal"
        }
    }

    @Serializable
    @SerialName(Transaction.KIND)
    public data class Transaction(
        public val storage: MichelineNode? = null,
        @SerialName("big_map_diff") public val bigMapDiff: List<RpcBigMapDiff>? = null,
        @SerialName("balance_updates") public val balanceUpdates: List<RpcBalanceUpdate>? = null,
        @SerialName("originated_contracts") public val originatedContracts: List<Address>? = null,
        @SerialName("consumed_gas") public val consumedGas: String? = null,
        @SerialName("consumed_milligas") public val consumedMilligas: String? = null,
        @SerialName("storage_size") public val storageSize: String? = null,
        @SerialName("paid_storage_size_diff") public val paidStorageSizeDiff: String? = null,
        @SerialName("allocated_destination_contract") public val allocatedDestinationContract: Boolean? = null,
        @SerialName("lazy_storage_diff") public val lazyStorageDiff: List<RpcLazyStorageDiff>? = null,
    ) : RpcSuccessfulManagerOperationResult() {
        public companion object {
            internal const val KIND = "transaction"
        }
    }

    @Serializable
    @SerialName(Origination.KIND)
    public data class Origination(
        @SerialName("big_map_diff") public val bigMapDiff: List<RpcBigMapDiff>? = null,
        @SerialName("balance_updates") public val balanceUpdates: List<RpcBalanceUpdate>? = null,
        @SerialName("originated_contracts") public val originatedContracts: List<Address>? = null,
        @SerialName("consumed_gas") public val consumedGas: String? = null,
        @SerialName("consumed_milligas") public val consumedMilligas: String? = null,
        @SerialName("storage_size") public val storageSize: String? = null,
        @SerialName("paid_storage_size_diff") public val paidStorageSizeDiff: String? = null,
        @SerialName("lazy_storage_diff") public val lazyStorageDiff: List<RpcLazyStorageDiff>? = null,
    ) : RpcSuccessfulManagerOperationResult() {
        public companion object {
            internal const val KIND = "origination"
        }
    }

    @Serializable
    @SerialName(Delegation.KIND)
    public data class Delegation(
        @SerialName("consumed_gas") public val consumedGas: String? = null,
        @SerialName("consumed_milligas") public val consumedMilligas: String? = null,
    ) : RpcSuccessfulManagerOperationResult() {
        public companion object {
            internal const val KIND = "delegation"
        }
    }

    @Serializable
    @SerialName(SetDepositsLimit.KIND)
    public data class SetDepositsLimit(
        @SerialName("consumed_gas") public val consumedGas: String? = null,
        @SerialName("consumed_milligas") public val consumedMilligas: String? = null,
    ) : RpcSuccessfulManagerOperationResult() {
        public companion object {
            internal const val KIND = "set_deposits_limit"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "kind"
    }
}