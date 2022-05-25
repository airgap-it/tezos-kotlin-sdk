package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.Signature
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object RpcRunnableOperationSerializer : KSerializer<RpcRunnableOperation> {
    override val descriptor: SerialDescriptor = RpcRunnableOperationSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): RpcRunnableOperation {
        val surrogate = decoder.decodeSerializableValue(RpcRunnableOperationSurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: RpcRunnableOperation) {
        val surrogate = RpcRunnableOperationSurrogate(value)
        encoder.encodeSerializableValue(RpcRunnableOperationSurrogate.serializer(), surrogate)
    }
}

// -- surrogate --

@Serializable
private data class RpcRunnableOperationSurrogate(
    val operation: Content,
    @SerialName("chain_id") val chainId: @Contextual ChainId,
) {

    fun toTarget(): RpcRunnableOperation = RpcRunnableOperation(operation.branch, operation.contents, operation.signature, chainId)

    @Serializable
    data class Content(
        val branch: @Contextual BlockHash,
        val contents: List<RpcOperationContent>,
        val signature: @Contextual Signature,
    )
}

private fun RpcRunnableOperationSurrogate(value: RpcRunnableOperation): RpcRunnableOperationSurrogate = with(value) {
    RpcRunnableOperationSurrogate(
        RpcRunnableOperationSurrogate.Content(branch, contents, signature),
        chainId,
    )
}