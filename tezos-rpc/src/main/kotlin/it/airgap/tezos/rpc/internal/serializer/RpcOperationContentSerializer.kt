package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

internal object RpcOperationContentSerializer : JsonContentPolymorphicSerializer<RpcOperationContent>(RpcOperationContent::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out RpcOperationContent> {
        val jsonObject = element as? JsonObject ?: failWithUnexpectedJsonType(element::class)

        return when (val kind = jsonObject[RpcOperationContent.CLASS_DISCRIMINATOR]?.jsonPrimitive?.content) {
            RpcOperationContent.Endorsement.KIND -> RpcOperationContent.Endorsement.serializer()
            RpcOperationContent.Preendorsement.KIND -> RpcOperationContent.Preendorsement.serializer()
            RpcOperationContent.SeedNonceRevelation.KIND -> RpcOperationContent.SeedNonceRevelation.serializer()
            RpcOperationContent.DoubleEndorsementEvidence.KIND -> RpcOperationContent.DoubleEndorsementEvidence.serializer()
            RpcOperationContent.DoublePreendorsementEvidence.KIND -> RpcOperationContent.DoublePreendorsementEvidence.serializer()
            RpcOperationContent.DoubleBakingEvidence.KIND -> RpcOperationContent.DoubleBakingEvidence.serializer()
            RpcOperationContent.ActivateAccount.KIND -> RpcOperationContent.ActivateAccount.serializer()
            RpcOperationContent.Proposals.KIND -> RpcOperationContent.Proposals.serializer()
            RpcOperationContent.Ballot.KIND -> RpcOperationContent.Ballot.serializer()
            RpcOperationContent.Reveal.KIND -> RpcOperationContent.Reveal.serializer()
            RpcOperationContent.Transaction.KIND -> RpcOperationContent.Transaction.serializer()
            RpcOperationContent.Origination.KIND -> RpcOperationContent.Origination.serializer()
            RpcOperationContent.Delegation.KIND -> RpcOperationContent.Delegation.serializer()
            RpcOperationContent.SetDepositsLimit.KIND -> RpcOperationContent.SetDepositsLimit.serializer()
            RpcOperationContent.FailingNoop.KIND -> RpcOperationContent.FailingNoop.serializer()
            RpcOperationContent.RegisterGlobalConstant.KIND -> RpcOperationContent.RegisterGlobalConstant.serializer()
            else -> failWithUnknownKind(kind)
        }
    }

    private fun failWithUnknownKind(kind: String?): Nothing = throw SerializationException("Could not deserialize, unknown operation content kind `$kind`.")
}