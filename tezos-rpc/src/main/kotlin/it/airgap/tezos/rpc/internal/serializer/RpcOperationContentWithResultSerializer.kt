package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.internal.utils.KOperationContentWithResultSerializer
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcOperationContentWithResult
import it.airgap.tezos.rpc.type.operation.RpcOperationMetadata
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.json.*

internal object RpcOperationContentWithResultSerializer : KJsonSerializer<RpcOperationContentWithResult> {
    override val descriptor: SerialDescriptor = RpcOperationContentWithResultSurrogate.serializer().descriptor

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): RpcOperationContentWithResult {
        val surrogate = jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResultSurrogate.serializer(), jsonElement)
        return when (surrogate.kind) {
            RpcOperationContentWithResultSurrogate.Kind.Endorsement -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.Endorsement.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.Preendorsement -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.Preendorsement.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.SeedNonceRevelation -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.SeedNonceRevelation.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.DoubleEndorsementEvidence -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.DoubleEndorsementEvidence.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.DoublePreendorsementEvidence -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.DoublePreendorsementEvidence.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.DoubleBakingEvidence -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.DoubleBakingEvidence.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.ActivateAccount -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.ActivateAccount.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.Proposals -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.Proposals.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.Ballot -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.Ballot.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.Reveal -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.Reveal.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.Transaction -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.Transaction.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.Origination -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.Origination.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.Delegation -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.Delegation.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.SetDepositsLimit -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.SetDepositsLimit.serializer(), jsonElement)
            RpcOperationContentWithResultSurrogate.Kind.RegisterGlobalConstant -> jsonDecoder.json.decodeFromJsonElement(RpcOperationContentWithResult.RegisterGlobalConstant.serializer(), jsonElement)
        }
    }

    override fun serialize(jsonEncoder: JsonEncoder, value: RpcOperationContentWithResult) {
        val surrogateJson = jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResultSurrogate.serializer(), RpcOperationContentWithResultSurrogate(value)).jsonObject
        val contentWithResultJson = when (value) {
            is RpcOperationContentWithResult.Endorsement -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.Endorsement.serializer(), value)
            is RpcOperationContentWithResult.Preendorsement -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.Preendorsement.serializer(), value)
            is RpcOperationContentWithResult.SeedNonceRevelation -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.SeedNonceRevelation.serializer(), value)
            is RpcOperationContentWithResult.DoubleEndorsementEvidence -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.DoubleEndorsementEvidence.serializer(), value)
            is RpcOperationContentWithResult.DoublePreendorsementEvidence -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.DoublePreendorsementEvidence.serializer(), value)
            is RpcOperationContentWithResult.DoubleBakingEvidence -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.DoubleBakingEvidence.serializer(), value)
            is RpcOperationContentWithResult.ActivateAccount -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.ActivateAccount.serializer(), value)
            is RpcOperationContentWithResult.Proposals -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.Proposals.serializer(), value)
            is RpcOperationContentWithResult.Ballot -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.Ballot.serializer(), value)
            is RpcOperationContentWithResult.Reveal -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.Reveal.serializer(), value)
            is RpcOperationContentWithResult.Transaction -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.Transaction.serializer(), value)
            is RpcOperationContentWithResult.Origination -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.Origination.serializer(), value)
            is RpcOperationContentWithResult.Delegation -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.Delegation.serializer(), value)
            is RpcOperationContentWithResult.SetDepositsLimit -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.SetDepositsLimit.serializer(), value)
            is RpcOperationContentWithResult.RegisterGlobalConstant -> jsonEncoder.json.encodeToJsonElement(RpcOperationContentWithResult.RegisterGlobalConstant.serializer(), value)
        }.jsonObject
        val jsonObject = JsonObject(surrogateJson + contentWithResultJson)

        jsonEncoder.encodeSerializableValue(JsonElement.serializer(), jsonObject)
    }
}

internal object RpcEndorsementOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.Endorsement, RpcOperationContent.Endorsement, RpcOperationMetadata.Endorsement>(
    RpcOperationContentWithResult.Endorsement::class,
    RpcOperationContent.Endorsement.serializer(),
    RpcOperationMetadata.Endorsement.serializer(),
    RpcOperationContentWithResult::Endorsement,
    { Pair(it.content, it.metadata) },
)

internal object RpcPreendorsementOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.Preendorsement, RpcOperationContent.Preendorsement, RpcOperationMetadata.Preendorsement>(
    RpcOperationContentWithResult.Preendorsement::class,
    RpcOperationContent.Preendorsement.serializer(),
    RpcOperationMetadata.Preendorsement.serializer(),
    RpcOperationContentWithResult::Preendorsement,
    { Pair(it.content, it.metadata) },
)

internal object RpcSeedNonceRevelationOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.SeedNonceRevelation, RpcOperationContent.SeedNonceRevelation, RpcOperationMetadata.SeedNonceRevelation>(
    RpcOperationContentWithResult.SeedNonceRevelation::class,
    RpcOperationContent.SeedNonceRevelation.serializer(),
    RpcOperationMetadata.SeedNonceRevelation.serializer(),
    RpcOperationContentWithResult::SeedNonceRevelation,
    { Pair(it.content, it.metadata) },
)

internal object RpcDoubleEndorsementEvidenceOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.DoubleEndorsementEvidence, RpcOperationContent.DoubleEndorsementEvidence, RpcOperationMetadata.DoubleEndorsementEvidence>(
    RpcOperationContentWithResult.DoubleEndorsementEvidence::class,
    RpcOperationContent.DoubleEndorsementEvidence.serializer(),
    RpcOperationMetadata.DoubleEndorsementEvidence.serializer(),
    RpcOperationContentWithResult::DoubleEndorsementEvidence,
    { Pair(it.content, it.metadata) },
)

internal object RpcDoublePreendorsementEvidenceOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.DoublePreendorsementEvidence, RpcOperationContent.DoublePreendorsementEvidence, RpcOperationMetadata.DoublePreendorsementEvidence>(
    RpcOperationContentWithResult.DoublePreendorsementEvidence::class,
    RpcOperationContent.DoublePreendorsementEvidence.serializer(),
    RpcOperationMetadata.DoublePreendorsementEvidence.serializer(),
    RpcOperationContentWithResult::DoublePreendorsementEvidence,
    { Pair(it.content, it.metadata) },
)

internal object RpcDoubleBakingEvidenceOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.DoubleBakingEvidence, RpcOperationContent.DoubleBakingEvidence, RpcOperationMetadata.DoubleBakingEvidence>(
    RpcOperationContentWithResult.DoubleBakingEvidence::class,
    RpcOperationContent.DoubleBakingEvidence.serializer(),
    RpcOperationMetadata.DoubleBakingEvidence.serializer(),
    RpcOperationContentWithResult::DoubleBakingEvidence,
    { Pair(it.content, it.metadata) },
)

internal object RpcActivateAccountOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.ActivateAccount, RpcOperationContent.ActivateAccount, RpcOperationMetadata.ActivateAccount>(
    RpcOperationContentWithResult.ActivateAccount::class,
    RpcOperationContent.ActivateAccount.serializer(),
    RpcOperationMetadata.ActivateAccount.serializer(),
    RpcOperationContentWithResult::ActivateAccount,
    { Pair(it.content, it.metadata) },
)

internal object RpcProposalsOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.Proposals, RpcOperationContent.Proposals, RpcOperationMetadata.Proposals>(
    RpcOperationContentWithResult.Proposals::class,
    RpcOperationContent.Proposals.serializer(),
    RpcOperationMetadata.Proposals.serializer(),
    RpcOperationContentWithResult::Proposals,
    { Pair(it.content, it.metadata) },
)

internal object RpcBallotOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.Ballot, RpcOperationContent.Ballot, RpcOperationMetadata.Ballot>(
    RpcOperationContentWithResult.Ballot::class,
    RpcOperationContent.Ballot.serializer(),
    RpcOperationMetadata.Ballot.serializer(),
    RpcOperationContentWithResult::Ballot,
    { Pair(it.content, it.metadata) },
)

internal object RpcRevealOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.Reveal, RpcOperationContent.Reveal, RpcOperationMetadata.Reveal>(
    RpcOperationContentWithResult.Reveal::class,
    RpcOperationContent.Reveal.serializer(),
    RpcOperationMetadata.Reveal.serializer(),
    RpcOperationContentWithResult::Reveal,
    { Pair(it.content, it.metadata) },
)

internal object RpcTransactionOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.Transaction, RpcOperationContent.Transaction, RpcOperationMetadata.Transaction>(
    RpcOperationContentWithResult.Transaction::class,
    RpcOperationContent.Transaction.serializer(),
    RpcOperationMetadata.Transaction.serializer(),
    RpcOperationContentWithResult::Transaction,
    { Pair(it.content, it.metadata) },
)

internal object RpcOriginationOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.Origination, RpcOperationContent.Origination, RpcOperationMetadata.Origination>(
    RpcOperationContentWithResult.Origination::class,
    RpcOperationContent.Origination.serializer(),
    RpcOperationMetadata.Origination.serializer(),
    RpcOperationContentWithResult::Origination,
    { Pair(it.content, it.metadata) },
)

internal object RpcDelegationOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.Delegation, RpcOperationContent.Delegation, RpcOperationMetadata.Delegation>(
    RpcOperationContentWithResult.Delegation::class,
    RpcOperationContent.Delegation.serializer(),
    RpcOperationMetadata.Delegation.serializer(),
    RpcOperationContentWithResult::Delegation,
    { Pair(it.content, it.metadata) },
)

internal object RpcSetDepositsLimitOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.SetDepositsLimit, RpcOperationContent.SetDepositsLimit, RpcOperationMetadata.SetDepositsLimit>(
    RpcOperationContentWithResult.SetDepositsLimit::class,
    RpcOperationContent.SetDepositsLimit.serializer(),
    RpcOperationMetadata.SetDepositsLimit.serializer(),
    RpcOperationContentWithResult::SetDepositsLimit,
    { Pair(it.content, it.metadata) },
)

internal object RpcRegisterGlobalConstantOperationContentWithResultSerializer : KOperationContentWithResultSerializer<RpcOperationContentWithResult.RegisterGlobalConstant, RpcOperationContent.RegisterGlobalConstant, RpcOperationMetadata.RegisterGlobalConstant>(
    RpcOperationContentWithResult.RegisterGlobalConstant::class,
    RpcOperationContent.RegisterGlobalConstant.serializer(),
    RpcOperationMetadata.RegisterGlobalConstant.serializer(),
    RpcOperationContentWithResult::RegisterGlobalConstant,
    { Pair(it.content, it.metadata) },
)

// -- surrogate --

@Serializable
private data class RpcOperationContentWithResultSurrogate(val kind: Kind) {
    @Serializable
    enum class Kind {
        @SerialName("endorsement") Endorsement,
        @SerialName("preendorsement") Preendorsement,
        @SerialName("seed_nonce_revelation") SeedNonceRevelation,
        @SerialName("double_endorsement_evidence") DoubleEndorsementEvidence,
        @SerialName("double_preendorsement_evidence") DoublePreendorsementEvidence,
        @SerialName("double_baking_evidence") DoubleBakingEvidence,
        @SerialName("activate_account") ActivateAccount,
        @SerialName("proposals") Proposals,
        @SerialName("ballot") Ballot,
        @SerialName("reveal") Reveal,
        @SerialName("transaction") Transaction,
        @SerialName("origination") Origination,
        @SerialName("delegation") Delegation,
        @SerialName("set_deposits_limit") SetDepositsLimit,
        @SerialName("register_global_constant") RegisterGlobalConstant,
    }
}

private fun RpcOperationContentWithResultSurrogate(contentWithResult: RpcOperationContentWithResult): RpcOperationContentWithResultSurrogate {
    val kind = when (contentWithResult) {
        is RpcOperationContentWithResult.Endorsement -> RpcOperationContentWithResultSurrogate.Kind.Endorsement
        is RpcOperationContentWithResult.Preendorsement -> RpcOperationContentWithResultSurrogate.Kind.Preendorsement
        is RpcOperationContentWithResult.SeedNonceRevelation -> RpcOperationContentWithResultSurrogate.Kind.SeedNonceRevelation
        is RpcOperationContentWithResult.DoubleEndorsementEvidence -> RpcOperationContentWithResultSurrogate.Kind.DoubleEndorsementEvidence
        is RpcOperationContentWithResult.DoublePreendorsementEvidence -> RpcOperationContentWithResultSurrogate.Kind.DoublePreendorsementEvidence
        is RpcOperationContentWithResult.DoubleBakingEvidence -> RpcOperationContentWithResultSurrogate.Kind.DoubleBakingEvidence
        is RpcOperationContentWithResult.ActivateAccount -> RpcOperationContentWithResultSurrogate.Kind.ActivateAccount
        is RpcOperationContentWithResult.Proposals -> RpcOperationContentWithResultSurrogate.Kind.Proposals
        is RpcOperationContentWithResult.Ballot -> RpcOperationContentWithResultSurrogate.Kind.Ballot
        is RpcOperationContentWithResult.Reveal -> RpcOperationContentWithResultSurrogate.Kind.Reveal
        is RpcOperationContentWithResult.Transaction -> RpcOperationContentWithResultSurrogate.Kind.Transaction
        is RpcOperationContentWithResult.Origination -> RpcOperationContentWithResultSurrogate.Kind.Origination
        is RpcOperationContentWithResult.Delegation -> RpcOperationContentWithResultSurrogate.Kind.Delegation
        is RpcOperationContentWithResult.SetDepositsLimit -> RpcOperationContentWithResultSurrogate.Kind.SetDepositsLimit
        is RpcOperationContentWithResult.RegisterGlobalConstant -> RpcOperationContentWithResultSurrogate.Kind.RegisterGlobalConstant
    }

    return RpcOperationContentWithResultSurrogate(kind)
}