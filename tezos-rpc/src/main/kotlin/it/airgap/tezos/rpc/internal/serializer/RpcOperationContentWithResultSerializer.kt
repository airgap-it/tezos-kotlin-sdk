package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.type.operation.RpcOperationContentWithResult
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object RpcOperationContentWithResultSerializer : KSerializer<RpcOperationContentWithResult> {
    override val descriptor: SerialDescriptor = RpcOperationContentWithResultSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): RpcOperationContentWithResult {
        val surrogate = decoder.decodeSerializableValue(RpcOperationContentWithResultSurrogate.serializer())
        return when (surrogate.kind) {
            RpcOperationContentWithResultSurrogate.Kind.Endorsement -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.Preendorsement -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.SeedNonceRevelation -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.DoubleEndorsementEvidence -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.DoublePreendorsementEvidence -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.DoubleBakingEvidence -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.ActivateAccount -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.Proposals -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.Ballot -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.Reveal -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.Transaction -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.Origination -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.Delegation -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.SetDepositsLimit -> TODO()
            RpcOperationContentWithResultSurrogate.Kind.RegisterGlobalConstant -> TODO()
        }
    }

    override fun serialize(encoder: Encoder, value: RpcOperationContentWithResult) {
        when (value) {
            is RpcOperationContentWithResult.ActivateAccount -> TODO()
            is RpcOperationContentWithResult.Ballot -> TODO()
            is RpcOperationContentWithResult.Delegation -> TODO()
            is RpcOperationContentWithResult.DoubleBakingEvidence -> TODO()
            is RpcOperationContentWithResult.DoubleEndorsementEvidence -> TODO()
            is RpcOperationContentWithResult.DoublePreendorsementEvidence -> TODO()
            is RpcOperationContentWithResult.Endorsement -> TODO()
            is RpcOperationContentWithResult.Origination -> TODO()
            is RpcOperationContentWithResult.Preendorsement -> TODO()
            is RpcOperationContentWithResult.Proposals -> TODO()
            is RpcOperationContentWithResult.RegisterGlobalConstant -> TODO()
            is RpcOperationContentWithResult.Reveal -> TODO()
            is RpcOperationContentWithResult.SeedNonceRevelation -> TODO()
            is RpcOperationContentWithResult.SetDepositsLimit -> TODO()
            is RpcOperationContentWithResult.Transaction -> TODO()
        }
    }

}

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