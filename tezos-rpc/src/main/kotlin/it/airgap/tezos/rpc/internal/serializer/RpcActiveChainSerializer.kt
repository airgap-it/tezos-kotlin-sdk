package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.type.RpcActiveChain
import it.airgap.tezos.rpc.type.RpcMainChain
import it.airgap.tezos.rpc.type.RpcStoppingChain
import it.airgap.tezos.rpc.type.RpcTestChain
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
internal object RpcActiveChainSerializer : KSerializer<RpcActiveChain> {
    override val descriptor: SerialDescriptor = RpcActiveChainSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): RpcActiveChain {
        val surrogate = decoder.decodeSerializableValue(RpcActiveChainSurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: RpcActiveChain) {
        val surrogate = RpcActiveChainSurrogate(value)
        encoder.encodeSerializableValue(RpcActiveChainSurrogate.serializer(), surrogate)
    }
}

// -- surrogate --

@Serializable
private data class RpcActiveChainSurrogate(
    val chainId: @Contextual ChainId? = null,
    val testProtocol: @Contextual ProtocolHash? = null,
    val expirationDate: @Contextual Timestamp? = null,
    val stopping: @Contextual ChainId? = null,
) {

    fun toTarget(): RpcActiveChain =
        when {
            chainId != null && testProtocol == null && expirationDate == null && stopping == null -> RpcMainChain(chainId)
            chainId != null && testProtocol != null && expirationDate != null && stopping == null -> RpcTestChain(chainId, testProtocol, expirationDate)
            chainId == null && testProtocol == null && expirationDate == null && stopping != null -> RpcStoppingChain(stopping)
            else -> failWithInvalidSerializedValue(this)
        }

    private fun failWithInvalidSerializedValue(value: RpcActiveChainSurrogate): Nothing =
        throw SerializationException("Could not deserialize, `$value` is not a valid ActiveChain value.")
}

private fun RpcActiveChainSurrogate(activeChain: RpcActiveChain): RpcActiveChainSurrogate = with(activeChain) {
    RpcActiveChainSurrogate(chainId, testProtocol, expirationDate, stopping)
}