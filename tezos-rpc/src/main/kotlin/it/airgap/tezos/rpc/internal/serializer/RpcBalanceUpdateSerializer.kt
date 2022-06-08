package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.PublicKeyHash
import it.airgap.tezos.rpc.type.operation.RpcBalanceUpdate
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator

internal object RpcBalanceUpdateSerializer : KSerializer<RpcBalanceUpdate> {
    override val descriptor: SerialDescriptor = RpcBalanceUpdateSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): RpcBalanceUpdate {
        val surrogate = decoder.decodeSerializableValue(RpcBalanceUpdateSurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: RpcBalanceUpdate) {
        val surrogate = RpcBalanceUpdateSurrogate(value)
        encoder.encodeSerializableValue(RpcBalanceUpdateSurrogate.serializer(), surrogate)
    }
}

// -- surrogate --

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(RpcBalanceUpdateSurrogate.CLASS_DISCRIMINATOR)
private sealed class RpcBalanceUpdateSurrogate {
    abstract val change: Long
    abstract val origin: RpcBalanceUpdate.Origin

    @Transient
    open val contract: Address? = null

    @Transient
    open val delegate: PublicKeyHash? = null

    @Transient
    open val cycle: Int? = null

    @Transient
    open val participation: Boolean? = null

    @Transient
    open val revelation: Boolean? = null

    @Transient
    open val committer: BlindedPublicKeyHash? = null

    abstract fun toTarget(): RpcBalanceUpdate

    @Serializable
    @SerialName(Contract.KIND)
    data class Contract(
        override val contract: @Contextual Address,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: RpcBalanceUpdate.Origin,
    ) : RpcBalanceUpdateSurrogate() {

        override fun toTarget(): RpcBalanceUpdate = RpcBalanceUpdate.Contract(contract, change, origin)

        companion object {
            const val KIND = "contract"
        }
    }

    @Serializable
    @SerialName(Freezer.KIND)
    data class Freezer(
        val category: Category,
        override val delegate: @Contextual PublicKeyHash,
        override val cycle: Int? = null,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: RpcBalanceUpdate.Origin,
    ) : RpcBalanceUpdateSurrogate() {

        override fun toTarget(): RpcBalanceUpdate = when {
            category == Category.LegacyRewards && cycle != null -> RpcBalanceUpdate.LegacyRewards(delegate, cycle, change, origin)
            category == Category.LegacyDeposits && cycle != null -> RpcBalanceUpdate.LegacyDeposits(delegate, cycle, change, origin)
            category == Category.Deposits -> RpcBalanceUpdate.Deposits(delegate, change, origin)
            category == Category.LegacyFees && cycle != null -> RpcBalanceUpdate.LegacyFees(delegate, cycle, change, origin)
            else -> failWithInvalidSerializedValue(this)
        }

        private fun failWithInvalidSerializedValue(value: Freezer): Nothing =
            throw SerializationException("Could not deserialize, `$value` is not a valid BalanceUpdate.Freezer value.")

        @Serializable
        enum class Category {
            @SerialName("legacy_rewards") LegacyRewards,
            @SerialName("legacy_deposits") LegacyDeposits,
            @SerialName("deposits") Deposits,
            @SerialName("legacy_fees") LegacyFees,
        }

        companion object {
            const val KIND = "freezer"
        }
    }

    @Serializable
    @SerialName(Accumulator.KIND)
    data class Accumulator(
        val category: Category,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: RpcBalanceUpdate.Origin,
    ) : RpcBalanceUpdateSurrogate() {

        override fun toTarget(): RpcBalanceUpdate = when (category) {
            Category.BlockFees -> RpcBalanceUpdate.BlockFees(change, origin)
        }

        @Serializable
        enum class Category {
            @SerialName("block fees") BlockFees,
        }

        companion object {
            const val KIND = "accumulator"
        }
    }

    @Serializable
    @SerialName(Minted.KIND)
    data class Minted(
        val category: Category,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: RpcBalanceUpdate.Origin,
    ) : RpcBalanceUpdateSurrogate() {

        override fun toTarget(): RpcBalanceUpdate = when (category) {
            Category.NonceRevelationRewards -> RpcBalanceUpdate.NonceRevelationRewards(change, origin)
            Category.DoubleSigningEvidenceRewards -> RpcBalanceUpdate.DoubleSigningEvidenceRewards(change, origin)
            Category.EndorsingRewards -> RpcBalanceUpdate.EndorsingRewards(change, origin)
            Category.BakingRewards -> RpcBalanceUpdate.BakingRewards(change, origin)
            Category.BakingBonuses -> RpcBalanceUpdate.BakingBonuses(change, origin)
            Category.Subsidy -> RpcBalanceUpdate.LiquidityBakingSubsidies(change, origin)
            Category.Bootstrap -> RpcBalanceUpdate.Bootstrap(change, origin)
            Category.Invoice -> RpcBalanceUpdate.Invoice(change, origin)
            Category.Commitment -> RpcBalanceUpdate.InitialCommitments(change, origin)
            Category.Minted -> RpcBalanceUpdate.Minted(change, origin)
        }

        @Serializable
        enum class Category {
            @SerialName("nonce revelation rewards") NonceRevelationRewards,
            @SerialName("double signing evidence rewards") DoubleSigningEvidenceRewards,
            @SerialName("endorsing rewards") EndorsingRewards,
            @SerialName("baking rewards") BakingRewards,
            @SerialName("baking bonuses") BakingBonuses,
            @SerialName("subsidy") Subsidy,
            @SerialName("bootstrap") Bootstrap,
            @SerialName("invoice") Invoice,
            @SerialName("commitment") Commitment,
            @SerialName("minted") Minted,
        }

        companion object {
            const val KIND = "minted"
        }
    }

    @Serializable
    @SerialName(Burned.KIND)
    data class Burned(
        val category: Category,
        override val delegate: @Contextual PublicKeyHash? = null,
        override val participation: Boolean? = null,
        override val revelation: Boolean? = null,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: RpcBalanceUpdate.Origin,
    ) : RpcBalanceUpdateSurrogate() {

        override fun toTarget(): RpcBalanceUpdate = when (category) {
            Category.StorageFees -> RpcBalanceUpdate.StorageFees(change, origin)
            Category.Punishments -> RpcBalanceUpdate.DoubleSigningPunishments(change, origin)
            Category.LostEndorsingRewards -> {
                if (delegate == null || participation == null || revelation == null) failWithInvalidSerializedValue(this)
                RpcBalanceUpdate.LostEndorsingRewards(delegate, participation, revelation, change, origin)
            }
            Category.Burned -> RpcBalanceUpdate.Burned(change, origin)
        }

        private fun failWithInvalidSerializedValue(value: Burned): Nothing =
            throw SerializationException("Could not deserialize, `$value` is not a valid BalanceUpdate.Burned value.")

        @Serializable
        enum class Category {
            @SerialName("storage fees") StorageFees,
            @SerialName("punishments") Punishments,
            @SerialName("lost endorsing rewards") LostEndorsingRewards,
            @SerialName("burned") Burned,
        }

        companion object {
            const val KIND = "burned"
        }
    }

    @Serializable
    @SerialName(Commitment.KIND)
    data class Commitment(
        val category: Category,
        override val committer: @Contextual BlindedPublicKeyHash,
        @Serializable(with = LongSerializer::class) override val change: Long,
        override val origin: RpcBalanceUpdate.Origin,
    ) : RpcBalanceUpdateSurrogate() {

        override fun toTarget(): RpcBalanceUpdate = when (category) {
            Category.Commitment -> RpcBalanceUpdate.Commitments(committer, change, origin)
        }

        @Serializable
        enum class Category {
            @SerialName("commitment") Commitment,
        }

        companion object {
            const val KIND = "commitment"
        }
    }

    companion object {
        const val CLASS_DISCRIMINATOR = "kind"
    }
}

private fun RpcBalanceUpdateSurrogate(balanceUpdate: RpcBalanceUpdate): RpcBalanceUpdateSurrogate = with(balanceUpdate) {
    when (this) {
        is RpcBalanceUpdate.Contract -> RpcBalanceUpdateSurrogate.Contract(contract, change, origin)
        is RpcBalanceUpdate.LegacyRewards -> RpcBalanceUpdateSurrogate.Freezer(RpcBalanceUpdateSurrogate.Freezer.Category.LegacyRewards, delegate, cycle, change, origin)
        is RpcBalanceUpdate.BlockFees -> RpcBalanceUpdateSurrogate.Accumulator(RpcBalanceUpdateSurrogate.Accumulator.Category.BlockFees, change, origin)
        is RpcBalanceUpdate.LegacyDeposits -> RpcBalanceUpdateSurrogate.Freezer(RpcBalanceUpdateSurrogate.Freezer.Category.LegacyDeposits, delegate, cycle, change, origin)
        is RpcBalanceUpdate.Deposits -> RpcBalanceUpdateSurrogate.Freezer(RpcBalanceUpdateSurrogate.Freezer.Category.Deposits, delegate, null, change, origin)
        is RpcBalanceUpdate.NonceRevelationRewards -> RpcBalanceUpdateSurrogate.Minted(RpcBalanceUpdateSurrogate.Minted.Category.NonceRevelationRewards, change, origin)
        is RpcBalanceUpdate.DoubleSigningEvidenceRewards -> RpcBalanceUpdateSurrogate.Minted(RpcBalanceUpdateSurrogate.Minted.Category.DoubleSigningEvidenceRewards, change, origin)
        is RpcBalanceUpdate.EndorsingRewards -> RpcBalanceUpdateSurrogate.Minted(RpcBalanceUpdateSurrogate.Minted.Category.EndorsingRewards, change, origin)
        is RpcBalanceUpdate.BakingRewards -> RpcBalanceUpdateSurrogate.Minted(RpcBalanceUpdateSurrogate.Minted.Category.BakingRewards, change, origin)
        is RpcBalanceUpdate.BakingBonuses -> RpcBalanceUpdateSurrogate.Minted(RpcBalanceUpdateSurrogate.Minted.Category.BakingBonuses, change, origin)
        is RpcBalanceUpdate.LegacyFees -> RpcBalanceUpdateSurrogate.Freezer(RpcBalanceUpdateSurrogate.Freezer.Category.LegacyFees, delegate, cycle, change, origin)
        is RpcBalanceUpdate.StorageFees -> RpcBalanceUpdateSurrogate.Burned(RpcBalanceUpdateSurrogate.Burned.Category.StorageFees, delegate, participation, revelation, change, origin)
        is RpcBalanceUpdate.DoubleSigningPunishments -> RpcBalanceUpdateSurrogate.Burned(RpcBalanceUpdateSurrogate.Burned.Category.Punishments, delegate, participation, revelation, change, origin)
        is RpcBalanceUpdate.LostEndorsingRewards -> RpcBalanceUpdateSurrogate.Burned(RpcBalanceUpdateSurrogate.Burned.Category.LostEndorsingRewards, delegate, participation, revelation, change, origin)
        is RpcBalanceUpdate.LiquidityBakingSubsidies -> RpcBalanceUpdateSurrogate.Minted(RpcBalanceUpdateSurrogate.Minted.Category.Subsidy, change, origin)
        is RpcBalanceUpdate.Burned -> RpcBalanceUpdateSurrogate.Burned(RpcBalanceUpdateSurrogate.Burned.Category.Burned, delegate, participation, revelation, change, origin)
        is RpcBalanceUpdate.Commitments -> RpcBalanceUpdateSurrogate.Commitment(RpcBalanceUpdateSurrogate.Commitment.Category.Commitment, committer, change, origin)
        is RpcBalanceUpdate.Bootstrap -> RpcBalanceUpdateSurrogate.Minted(RpcBalanceUpdateSurrogate.Minted.Category.Bootstrap, change, origin)
        is RpcBalanceUpdate.Invoice -> RpcBalanceUpdateSurrogate.Minted(RpcBalanceUpdateSurrogate.Minted.Category.Invoice, change, origin)
        is RpcBalanceUpdate.InitialCommitments -> RpcBalanceUpdateSurrogate.Minted(RpcBalanceUpdateSurrogate.Minted.Category.Commitment, change, origin)
        is RpcBalanceUpdate.Minted -> RpcBalanceUpdateSurrogate.Minted(RpcBalanceUpdateSurrogate.Minted.Category.Minted, change, origin)
    }
}