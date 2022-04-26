package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.michelson.micheline.MichelineNode
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
        public val metadata: RpcOperationMetadata.Endorsement? = null,
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
        public val metadata: RpcOperationMetadata.Preendorsement? = null,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "preendorsement"
        }
    }

    @Serializable
    @SerialName(SeedNonceRevelation.KIND)
    public data class SeedNonceRevelation(
        public val level: Int,
        public val nonce: String,
        public val metadata: RpcOperationMetadata.SeedNonceRevelation? = null,
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
        public val metadata: RpcOperationMetadata.DoubleEndorsementEvidence? = null,
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
        public val metadata: RpcOperationMetadata.DoublePreendorsementEvidence? = null,
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
        public val metadata: RpcOperationMetadata.DoubleBakingEvidence? = null,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "double_baking_evidence"
        }
    }

    @Serializable
    @SerialName(ActivateAccount.KIND)
    public data class ActivateAccount(
        public val pkh: @Contextual Ed25519PublicKeyHash,
        public val secret: String,
        public val metadata: RpcOperationMetadata.ActivateAccount? = null,
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
        public val metadata: RpcOperationMetadata.Proposals? = null,
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
        public val metadata: RpcOperationMetadata.Ballot? = null,
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
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        public val publicKey: @Contextual PublicKeyEncoded,
        public val metadata: RpcOperationMetadata.Reveal? = null,
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
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        public val amount: String,
        public val destination: @Contextual Address,
        public val parameters: RpcParameters? = null,
        public val metadata: RpcOperationMetadata.Transaction? = null,
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
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        public val balance: String,
        public val delegate: @Contextual ImplicitAddress? = null,
        public val script: RpcScript,
        public val metadata: RpcOperationMetadata.Origination? = null,
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
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        public val delegate: @Contextual ImplicitAddress? = null,
        public val metadata: RpcOperationMetadata.Delegation? = null,
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
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        public val limit: String? = null,
        public val metadata: RpcOperationMetadata.SetDepositsLimit? = null,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "set_deposits_limit"
        }
    }

    @Serializable
    @SerialName(FailingNoop.KIND)
    public data class FailingNoop(
        public val arbitrary: String,
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
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        public val value: MichelineNode,
        public val metadata: RpcOperationMetadata.RegisterGlobalConstant? = null,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "register_global_constant"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "kind"
    }
}
