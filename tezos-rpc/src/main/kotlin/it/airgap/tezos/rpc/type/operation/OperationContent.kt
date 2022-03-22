package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.*
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
        public val op1: InlinedEndorsement,
        public val op2: InlinedEndorsement,
    ) : RpcOperationContent()  {
        public companion object {
            internal const val KIND = "double_endorsement_evidence"
        }
    }

    @Serializable
    @SerialName(DoublePreendorsementEvidence.KIND)
    public data class DoublePreendorsementEvidence(
        public val op1: InlinedPreendorsement,
        public val op2: InlinedPreendorsement,
    ) : RpcOperationContent() {
        public companion object {
            internal const val KIND = "double_preendorsement_evidence"
        }
    }

    @Serializable
    @SerialName(DoubleBakingEvidence.KIND)
    public data class DoubleBakingEvidence(
        public val bh1: FullHeader,
        public val bh2: FullHeader,
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
        public val parameters: Parameters? = null,
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
        public val script: Script,
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

@Serializable
public sealed class RpcOperationContentWithResults {

}