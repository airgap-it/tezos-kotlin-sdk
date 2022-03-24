package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.rpc.internal.serializer.*
import it.airgap.tezos.rpc.type.block.RpcBlockHeader
import it.airgap.tezos.rpc.type.contract.RpcParameters
import it.airgap.tezos.rpc.type.contract.RpcScript
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(RpcOperationContent.CLASS_DISCRIMINATOR)
public sealed class RpcOperationContent {

    @Serializable
    @SerialName(Endorsement.KIND)
    public data class Endorsement(
        public val slot: UShort,
        public val level: Int,
        public val round: Int,
        @SerialName("block_payload_hash") public val blockPayloadHash: @Contextual BlockPayloadHash,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "endorsement"
        }
    }

    @Serializable
    @SerialName(Preendorsement.KIND)
    public data class Preendorsement(
        public val slot: UShort,
        public val level: Int,
        public val round: Int,
        @SerialName("block_payload_hash") public val blockPayloadHash: @Contextual BlockPayloadHash,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "preendorsement"
        }
    }

    @Serializable
    @SerialName(SeedNonceRevelation.KIND)
    public data class SeedNonceRevelation(
        public val level: Int,
        public val nonce: @Contextual HexString,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "seed_nonce_revelation"
        }
    }

    @Serializable
    @SerialName(DoubleEndorsementEvidence.KIND)
    public data class DoubleEndorsementEvidence(
        public val op1: RpcInlinedEndorsement,
        public val op2: RpcInlinedEndorsement,
    ) : RpcOperationContent()  {
        public companion object {
            internal const val KIND = "double_endorsement_evidence"
        }
    }

    @Serializable
    @SerialName(DoublePreendorsementEvidence.KIND)
    public data class DoublePreendorsementEvidence(
        public val op1: RpcInlinedPreendorsement,
        public val op2: RpcInlinedPreendorsement,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "double_preendorsement_evidence"
        }
    }

    @Serializable
    @SerialName(DoubleBakingEvidence.KIND)
    public data class DoubleBakingEvidence(
        public val bh1: RpcBlockHeader,
        public val bh2: RpcBlockHeader,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "double_baking_evidence"
        }
    }

    @Serializable
    @SerialName(ActivateAccount.KIND)
    public data class ActivateAccount(
        public val pkh: @Contextual Ed25519PublicKeyHash,
        public val secret: @Contextual HexString,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "activate_account"
        }
    }

    @Serializable
    @SerialName(Proposals.KIND)
    public data class Proposals(
        public val source: @Contextual ImplicitAddress,
        public val period: Int,
        public val proposals: List<@Contextual ProtocolHash>,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "proposals"
        }
    }

    @Serializable
    @SerialName(Ballot.KIND)
    public data class Ballot(
        public val source: @Contextual ImplicitAddress,
        public val period: Int,
        public val proposal: @Contextual ProtocolHash,
        public val ballot: BallotType,
    ) : RpcOperationContent() {

        @Serializable
        public enum class BallotType {
            @SerialName("nay") Nay,
            @SerialName("yay") Yay,
            @SerialName("pass") Pass,
        }

        public companion object {
            internal const val KIND = "ballot"
        }
    }

    @Serializable
    @SerialName(Reveal.KIND)
    public data class Reveal(
        public val source: @Contextual ImplicitAddress,
        public val fee: String,
        public val counter: String,
        public val gasLimit: String,
        public val storageLimit: String,
        public val publicKey: @Contextual PublicKeyEncoded,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "reveal"
        }
    }

    @Serializable
    @SerialName(Transaction.KIND)
    public data class Transaction(
        public val source: @Contextual ImplicitAddress,
        public val fee: String,
        public val counter: String,
        public val gasLimit: String,
        public val storageLimit: String,
        public val amount: String,
        public val destination: @Contextual Address,
        public val parameters: RpcParameters? = null,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "transaction"
        }
    }

    @Serializable
    @SerialName(Origination.KIND)
    public data class Origination(
        public val source: @Contextual ImplicitAddress,
        public val fee: String,
        public val counter: String,
        public val gasLimit: String,
        public val storageLimit: String,
        public val balance: String,
        public val delegate: @Contextual ImplicitAddress? = null,
        public val script: RpcScript,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "origination"
        }
    }

    @Serializable
    @SerialName(Delegation.KIND)
    public data class Delegation(
        public val source: @Contextual ImplicitAddress,
        public val fee: String,
        public val counter: String,
        public val gasLimit: String,
        public val storageLimit: String,
        public val delegate: @Contextual ImplicitAddress? = null,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "delegation"
        }
    }

    @Serializable
    @SerialName(SetDepositsLimit.KIND)
    public data class SetDepositsLimit(
        public val source: @Contextual ImplicitAddress,
        public val fee: String,
        public val counter: String,
        public val gasLimit: String,
        public val storageLimit: String,
        public val limit: String? = null,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "set_deposits_limit"
        }
    }

    @Serializable
    @SerialName(FailingNoop.KIND)
    public data class FailingNoop(
        public val arbitrary: @Contextual HexString,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "failing_noop"
        }
    }

    @Serializable
    @SerialName(RegisterGlobalConstant.KIND)
    public data class RegisterGlobalConstant(
        public val source: @Contextual ImplicitAddress,
        public val fee: String,
        public val counter: String,
        public val gasLimit: String,
        public val storageLimit: String,
        public val value: MichelineNode,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "register_global_constant"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "kind"
    }
}

@Serializable(with = RpcOperationContentWithResultSerializer::class)
public sealed class RpcOperationContentWithResult {

    @Serializable(with = RpcEndorsementOperationContentWithResultSerializer::class)
    public data class Endorsement(
        public val content: RpcOperationContent.Endorsement,
        public val metadata: RpcOperationMetadata.Endorsement,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcPreendorsementOperationContentWithResultSerializer::class)
    public data class Preendorsement(
        public val content: RpcOperationContent.Preendorsement,
        public val metadata: RpcOperationMetadata.Preendorsement,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcSeedNonceRevelationOperationContentWithResultSerializer::class)
    public data class SeedNonceRevelation(
        public val content: RpcOperationContent.SeedNonceRevelation,
        public val metadata: RpcOperationMetadata.SeedNonceRevelation,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcDoubleEndorsementEvidenceOperationContentWithResultSerializer::class)
    public data class DoubleEndorsementEvidence(
        public val content: RpcOperationContent.DoubleEndorsementEvidence,
        public val metadata: RpcOperationMetadata.DoubleEndorsementEvidence,
    ) : RpcOperationContentWithResult()  {
        public companion object {}
    }

    @Serializable(with = RpcDoublePreendorsementEvidenceOperationContentWithResultSerializer::class)
    public data class DoublePreendorsementEvidence(
        public val content: RpcOperationContent.DoublePreendorsementEvidence,
        public val metadata: RpcOperationMetadata.DoublePreendorsementEvidence,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcDoubleBakingEvidenceOperationContentWithResultSerializer::class)
    public data class DoubleBakingEvidence(
        public val content: RpcOperationContent.DoubleBakingEvidence,
        public val metadata: RpcOperationMetadata.DoubleBakingEvidence,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcActivateAccountOperationContentWithResultSerializer::class)
    public data class ActivateAccount(
        public val content: RpcOperationContent.ActivateAccount,
        public val metadata: RpcOperationMetadata.ActivateAccount,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcProposalsOperationContentWithResultSerializer::class)
    public data class Proposals(
        public val content: RpcOperationContent.Proposals,
        public val metadata: RpcOperationMetadata.Proposals,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcBallotOperationContentWithResultSerializer::class)
    public data class Ballot(
        public val content: RpcOperationContent.Ballot,
        public val metadata: RpcOperationMetadata.Ballot,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcRevealOperationContentWithResultSerializer::class)
    public data class Reveal(
        public val content: RpcOperationContent.Reveal,
        public val metadata: RpcOperationMetadata.Reveal,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcTransactionOperationContentWithResultSerializer::class)
    public data class Transaction(
        public val content: RpcOperationContent.Transaction,
        public val metadata: RpcOperationMetadata.Transaction,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcOriginationOperationContentWithResultSerializer::class)
    public data class Origination(
        public val content: RpcOperationContent.Origination,
        public val metadata: RpcOperationMetadata.Origination,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcDelegationOperationContentWithResultSerializer::class)
    public data class Delegation(
        public val content: RpcOperationContent.Delegation,
        public val metadata: RpcOperationMetadata.Delegation,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcSetDepositsLimitOperationContentWithResultSerializer::class)
    public data class SetDepositsLimit(
        public val content: RpcOperationContent.SetDepositsLimit,
        public val metadata: RpcOperationMetadata.SetDepositsLimit,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    @Serializable(with = RpcRegisterGlobalConstantOperationContentWithResultSerializer::class)
    public data class RegisterGlobalConstant(
        public val content: RpcOperationContent.RegisterGlobalConstant,
        public val metadata: RpcOperationMetadata.RegisterGlobalConstant,
    ) : RpcOperationContentWithResult() {
        public companion object {}
    }

    public companion object {}
}