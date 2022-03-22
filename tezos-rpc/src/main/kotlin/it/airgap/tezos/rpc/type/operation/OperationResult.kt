package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.rpc.type.bigmap.RpcBigMapDiff
import it.airgap.tezos.rpc.type.storage.RpcLazyStorageDiff
import kotlinx.serialization.*
import kotlinx.serialization.json.JsonClassDiscriminator

// -- RpcSuccessfulManagerOperationResult --

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(RpcSuccessfulManagerOperationResult.CLASS_DISCRIMINATOR)
public sealed class RpcSuccessfulManagerOperationResult {

    @Transient
    public open val consumedGas: String? = null

    @Transient
    public open val consumedMilligas: String? = null

    @Transient
    public open val storage: MichelineNode? = null

    @Transient
    public open val storageSize: String? = null

    @Transient
    public open val paidStorageSizeDiff: String? = null

    @Transient
    public open val bigMapDiff: List<RpcBigMapDiff>? = null

    @Transient
    public open val lazyStorageDiff: List<RpcLazyStorageDiff>? = null

    @Transient
    public open val balanceUpdates: List<RpcBalanceUpdate>? = null

    @Transient
    public open val originatedContracts: List<@Contextual Address>? = null

    @Transient
    public open val allocatedDestinationContract: Boolean? = null

    @Serializable
    @SerialName(Reveal.KIND)
    public data class Reveal(
        @SerialName("consumed_gas") override val consumedGas: String? = null,
        @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
    ) : RpcSuccessfulManagerOperationResult() {
        public companion object {
            internal const val KIND = "reveal"
        }
    }

    @Serializable
    @SerialName(Transaction.KIND)
    public data class Transaction(
        override val storage: MichelineNode? = null,
        @SerialName("big_map_diff") override val bigMapDiff: List<RpcBigMapDiff>? = null,
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate>? = null,
        @SerialName("originated_contracts") override val originatedContracts: List<Address>? = null,
        @SerialName("consumed_gas") override val consumedGas: String? = null,
        @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
        @SerialName("storage_size") override val storageSize: String? = null,
        @SerialName("paid_storage_size_diff") override val paidStorageSizeDiff: String? = null,
        @SerialName("allocatedDestinationContract") override val allocatedDestinationContract: Boolean? = null,
        @SerialName("lazy_storage_diff") override val lazyStorageDiff: List<RpcLazyStorageDiff>? = null,
    ) : RpcSuccessfulManagerOperationResult() {
        public companion object {
            internal const val KIND = "transaction"
        }
    }

    @Serializable
    @SerialName(Origination.KIND)
    public data class Origination(
        @SerialName("big_map_diff") override val bigMapDiff: List<RpcBigMapDiff>? = null,
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate>? = null,
        @SerialName("originated_contracts") override val originatedContracts: List<Address>? = null,
        @SerialName("consumed_gas") override val consumedGas: String? = null,
        @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
        @SerialName("storage_size") override val storageSize: String? = null,
        @SerialName("paid_storage_size_diff") override val paidStorageSizeDiff: String? = null,
        @SerialName("lazy_storage_diff") override val lazyStorageDiff: List<RpcLazyStorageDiff>? = null,
    ) : RpcSuccessfulManagerOperationResult() {
        public companion object {
            internal const val KIND = "origination"
        }
    }

    @Serializable
    @SerialName(Delegation.KIND)
    public data class Delegation(
        @SerialName("consumed_gas") override val consumedGas: String? = null,
        @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
    ) : RpcSuccessfulManagerOperationResult() {
        public companion object {
            internal const val KIND = "delegation"
        }
    }

    @Serializable
    @SerialName(SetDepositsLimit.KIND)
    public data class SetDepositsLimit(
        @SerialName("consumed_gas") override val consumedGas: String? = null,
        @SerialName("consumed_milligas") override val consumedMilligas: String? = null,
    ) : RpcSuccessfulManagerOperationResult() {
        public companion object {
            internal const val KIND = "set_deposits_limit"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "kind"
    }
}