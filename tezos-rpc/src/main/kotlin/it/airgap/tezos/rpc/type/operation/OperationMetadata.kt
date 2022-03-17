package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.BlindedPublicKeyHashEncoded
import it.airgap.tezos.core.type.encoded.PublicKeyHashEncoded
import it.airgap.tezos.rpc.internal.serializer.RpcBalanceUpdateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

// -- RpcBalanceUpdate --

@Serializable(with = RpcBalanceUpdateSerializer::class)
public sealed class RpcBalanceUpdate {

    public abstract val change: Long
    public abstract val origin: Origin

    @Transient
    public open val contract: @Contextual Address? = null

    @Transient
    public open val delegate: @Contextual PublicKeyHashEncoded? = null

    @Transient
    public open val cycle: Int? = null

    @Transient
    public open val participation: Boolean? = null

    @Transient
    public open val revelation: Boolean? = null

    @Transient
    public open val committer: @Contextual BlindedPublicKeyHashEncoded? = null

    @Serializable
    public data class Contract(
        override val contract: @Contextual Address,
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class LegacyRewards(
        override val delegate: @Contextual PublicKeyHashEncoded,
        override val cycle: Int,
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class BlockFees(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class LegacyDeposits(
        override val delegate: @Contextual PublicKeyHashEncoded,
        override val cycle: Int,
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Deposits(
        override val delegate: @Contextual PublicKeyHashEncoded,
        override val cycle: Int,
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class NonceRevelationRewards(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class DoubleSigningEvidenceRewards(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class EndorsingRewards(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class BakingRewards(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class BakingBonuses(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class LegacyFees(
        override val delegate: @Contextual PublicKeyHashEncoded,
        override val cycle: Int,
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class StorageFees(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class DoubleSigningPunishments(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class LostEndorsingRewards(
        override val delegate: @Contextual PublicKeyHashEncoded,
        override val participation: Boolean,
        override val revelation: Boolean,
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class LiquidityBakingSubsidies(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Burned(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Commitments(
        override val committer: @Contextual BlindedPublicKeyHashEncoded,
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Bootstrap(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Invoice(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class InitialCommitments(
        override val change: Long,
        override val origin: Origin,
    ) : RpcBalanceUpdate() {
        public companion object {}
    }

    @Serializable
    public data class Minted(
        override val change: Long,
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