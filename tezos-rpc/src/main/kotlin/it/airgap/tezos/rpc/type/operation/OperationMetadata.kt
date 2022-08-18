package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.PublicKeyHash
import it.airgap.tezos.rpc.internal.serializer.LongSerializer
import it.airgap.tezos.rpc.internal.serializer.RpcBalanceUpdateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

// -- RpcOperationMetadata --

public sealed class RpcOperationMetadata {

    @Transient
    public open val balanceUpdates: List<RpcBalanceUpdate>? = null

    @Transient
    public open val delegate: PublicKeyHash? = null

    @Transient
    public open val endorsementPower: Int? = null

    @Transient
    public open val preendorsementPower: Int? = null

    @Transient
    public open val operationResult: RpcOperationResult? = null

    @Transient
    public open val internalOperationResults: List<RpcInternalOperationResult>? = null

    @Serializable
    public data class Endorsement(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
        override val delegate: @Contextual PublicKeyHash,
        @SerialName("endorsement_power") override val endorsementPower: Int,
    ) : RpcOperationMetadata()

    @Serializable
    public data class Preendorsement(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
        override val delegate: @Contextual PublicKeyHash,
        @SerialName("preendorsement_power") override val preendorsementPower: Int,
    ) : RpcOperationMetadata()

    @Serializable
    public data class SeedNonceRevelation(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
    ) : RpcOperationMetadata()

    @Serializable
    public data class DoubleEndorsementEvidence(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
    ) : RpcOperationMetadata()

    @Serializable
    public data class DoublePreendorsementEvidence(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
    ) : RpcOperationMetadata()

    @Serializable
    public data class DoubleBakingEvidence(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
    ) : RpcOperationMetadata()

    @Serializable
    public data class ActivateAccount(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
    ) : RpcOperationMetadata()

    @Serializable
    public object Proposals : RpcOperationMetadata()

    @Serializable
    public object Ballot : RpcOperationMetadata()

    @Serializable
    public data class Reveal(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
        @SerialName("operation_result") override val operationResult: RpcOperationResult.Reveal,
        @SerialName("internal_operation_results") override val internalOperationResults: List<RpcInternalOperationResult.Reveal>? = null,
    ) : RpcOperationMetadata()

    @Serializable
    public data class Transaction(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
        @SerialName("operation_result") override val operationResult: RpcOperationResult.Transaction,
        @SerialName("internal_operation_results") override val internalOperationResults: List<RpcInternalOperationResult.Transaction>? = null,
    ) : RpcOperationMetadata()

    @Serializable
    public data class Origination(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
        @SerialName("operation_result") override val operationResult: RpcOperationResult.Origination,
        @SerialName("internal_operation_results") override val internalOperationResults: List<RpcInternalOperationResult.Origination>? = null,
    ) : RpcOperationMetadata()

    @Serializable
    public data class Delegation(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
        @SerialName("operation_result") override val operationResult: RpcOperationResult.Delegation,
        @SerialName("internal_operation_results") override val internalOperationResults: List<RpcInternalOperationResult.Delegation>? = null,
    ) : RpcOperationMetadata()

    @Serializable
    public data class SetDepositsLimit(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
        @SerialName("operation_result") override val operationResult: RpcOperationResult.SetDepositsLimit,
        @SerialName("internal_operation_results") override val internalOperationResults: List<RpcInternalOperationResult.SetDepositsLimit>? = null,
    ) : RpcOperationMetadata()

    @Serializable
    public data class RegisterGlobalConstant(
        @SerialName("balance_updates") override val balanceUpdates: List<RpcBalanceUpdate> = emptyList(),
        @SerialName("operation_result") override val operationResult: RpcOperationResult.RegisterGlobalConstant,
        @SerialName("internal_operation_results") override val internalOperationResults: List<RpcInternalOperationResult.RegisterGlobalConstant>? = null,
    ) : RpcOperationMetadata()
}

// -- RpcBalanceUpdate --

@Serializable(with = RpcBalanceUpdateSerializer::class)
public sealed class RpcBalanceUpdate {

    public abstract val change: Long
    public abstract val origin: Origin

    @Transient
    public open val contract: @Contextual Address? = null

    @Transient
    public open val delegate: @Contextual PublicKeyHash? = null

    @Transient
    public open val cycle: Int? = null

    @Transient
    public open val participation: Boolean? = null

    @Transient
    public open val revelation: Boolean? = null

    @Transient
    public open val committer: @Contextual BlindedPublicKeyHash? = null

    @Serializable
    public data class Contract(
        override val contract: @Contextual Address,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class LegacyRewards(
        override val delegate: @Contextual PublicKeyHash,
        override val cycle: Int,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class BlockFees(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class LegacyDeposits(
        override val delegate: @Contextual PublicKeyHash,
        override val cycle: Int,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Deposits(
        override val delegate: @Contextual PublicKeyHash,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class NonceRevelationRewards(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class DoubleSigningEvidenceRewards(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class EndorsingRewards(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class BakingRewards(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class BakingBonuses(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class LegacyFees(
        override val delegate: @Contextual PublicKeyHash,
        override val cycle: Int,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class StorageFees(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class DoubleSigningPunishments(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class LostEndorsingRewards(
        override val delegate: @Contextual PublicKeyHash,
        override val participation: Boolean,
        override val revelation: Boolean,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class LiquidityBakingSubsidies(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Burned(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Commitments(
        override val committer: @Contextual BlindedPublicKeyHash,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Bootstrap(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Invoice(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class InitialCommitments(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Minted(
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public enum class Origin {
        @SerialName("block") Block,
        @SerialName("migration") Migration,
        @SerialName("subsidy") Subsidy,
        @SerialName("simulation") Simulation,
    }

    public companion object {}
}

// -- RpcOperationListMetadata

@Serializable
public data class RpcOperationListMetadata(
    @SerialName("max_size") public val maxSize: Int,
    @SerialName("max_op") public val maxOperations: Int? = null,
)
