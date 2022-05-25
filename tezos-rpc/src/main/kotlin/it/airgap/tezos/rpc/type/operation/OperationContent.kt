package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.operation.contract.Script
import it.airgap.tezos.rpc.internal.serializer.RpcOperationContentSerializer
import it.airgap.tezos.rpc.type.block.RpcBlockHeader
import kotlinx.serialization.*

@OptIn(ExperimentalSerializationApi::class)
@Serializable(with = RpcOperationContentSerializer::class)
public sealed class RpcOperationContent {
    internal abstract val kind: String

    @Serializable
    public data class Endorsement(
        public val slot: UShort,
        public val level: Int,
        public val round: Int,
        @SerialName("block_payload_hash") public val blockPayloadHash: @Contextual BlockPayloadHash,
        public val metadata: RpcOperationMetadata.Endorsement? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "endorsement"
        }
    }

    @Serializable
    public data class Preendorsement(
        public val slot: UShort,
        public val level: Int,
        public val round: Int,
        @SerialName("block_payload_hash") public val blockPayloadHash: @Contextual BlockPayloadHash,
        public val metadata: RpcOperationMetadata.Preendorsement? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "preendorsement"
        }
    }

    @Serializable
    public data class SeedNonceRevelation(
        public val level: Int,
        public val nonce: String,
        public val metadata: RpcOperationMetadata.SeedNonceRevelation? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "seed_nonce_revelation"
        }
    }

    @Serializable
    public data class DoubleEndorsementEvidence(
        public val op1: RpcInlinedEndorsement,
        public val op2: RpcInlinedEndorsement,
        public val metadata: RpcOperationMetadata.DoubleEndorsementEvidence? = null,
    ) : RpcOperationContent()  {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "double_endorsement_evidence"
        }
    }

    @Serializable
    public data class DoublePreendorsementEvidence(
        public val op1: RpcInlinedPreendorsement,
        public val op2: RpcInlinedPreendorsement,
        public val metadata: RpcOperationMetadata.DoublePreendorsementEvidence? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "double_preendorsement_evidence"
        }
    }

    @Serializable
    public data class DoubleBakingEvidence(
        public val bh1: RpcBlockHeader,
        public val bh2: RpcBlockHeader,
        public val metadata: RpcOperationMetadata.DoubleBakingEvidence? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "double_baking_evidence"
        }
    }

    @Serializable
    public data class ActivateAccount(
        public val pkh: @Contextual Ed25519PublicKeyHash,
        public val secret: String,
        public val metadata: RpcOperationMetadata.ActivateAccount? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "activate_account"
        }
    }

    @Serializable
    public data class Proposals(
        public val source: @Contextual ImplicitAddress,
        public val period: Int,
        public val proposals: List<@Contextual ProtocolHash>,
        public val metadata: RpcOperationMetadata.Proposals? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "proposals"
        }
    }

    @Serializable
    public data class Ballot(
        public val source: @Contextual ImplicitAddress,
        public val period: Int,
        public val proposal: @Contextual ProtocolHash,
        public val ballot: Type,
        public val metadata: RpcOperationMetadata.Ballot? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        @Serializable
        public enum class Type {
            @SerialName("nay") Nay,
            @SerialName("yay") Yay,
            @SerialName("pass") Pass,
        }

        public companion object {
            internal const val KIND = "ballot"
        }
    }

    @Serializable
    public data class Reveal(
        public val source: @Contextual ImplicitAddress,
        public val fee: @Contextual Mutez,
        public val counter: String,
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        @SerialName("public_key") public val publicKey: @Contextual PublicKey,
        public val metadata: RpcOperationMetadata.Reveal? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "reveal"
        }
    }

    @Serializable
    public data class Transaction(
        public val source: @Contextual ImplicitAddress,
        public val fee: @Contextual Mutez,
        public val counter: String,
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        public val amount: @Contextual Mutez,
        public val destination: @Contextual Address,
        public val parameters: @Contextual Parameters? = null,
        public val metadata: RpcOperationMetadata.Transaction? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "transaction"
        }
    }

    @Serializable
    public data class Origination(
        public val source: @Contextual ImplicitAddress,
        public val fee: @Contextual Mutez,
        public val counter: String,
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        public val balance: @Contextual Mutez,
        public val delegate: @Contextual ImplicitAddress? = null,
        public val script: @Contextual Script,
        public val metadata: RpcOperationMetadata.Origination? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "origination"
        }
    }

    @Serializable
    public data class Delegation(
        public val source: @Contextual ImplicitAddress,
        public val fee: @Contextual Mutez,
        public val counter: String,
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        public val delegate: @Contextual ImplicitAddress? = null,
        public val metadata: RpcOperationMetadata.Delegation? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "delegation"
        }
    }

    @Serializable
    public data class SetDepositsLimit(
        public val source: @Contextual ImplicitAddress,
        public val fee: @Contextual Mutez,
        public val counter: String,
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        public val limit: @Contextual Mutez? = null,
        public val metadata: RpcOperationMetadata.SetDepositsLimit? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "set_deposits_limit"
        }
    }

    @Serializable
    public data class FailingNoop(
        public val arbitrary: String,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "failing_noop"
        }
    }

    @Serializable
    public data class RegisterGlobalConstant(
        public val source: @Contextual ImplicitAddress,
        public val fee: @Contextual Mutez,
        public val counter: String,
        @SerialName("gas_limit") public val gasLimit: String,
        @SerialName("storage_limit") public val storageLimit: String,
        public val value: MichelineNode,
        public val metadata: RpcOperationMetadata.RegisterGlobalConstant? = null,
    ) : RpcOperationContent() {
        @EncodeDefault
        override val kind: String = KIND

        public companion object {
            internal const val KIND = "register_global_constant"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "kind"
    }
}
