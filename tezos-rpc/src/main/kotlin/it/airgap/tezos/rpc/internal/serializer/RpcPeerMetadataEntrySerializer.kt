package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.type.network.RpcPeerMetadataEntry
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object RpcPeerMetadataEntrySerializer : KSerializer<RpcPeerMetadataEntry> {
    override val descriptor: SerialDescriptor = RpcPeerMetadataEntrySurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): RpcPeerMetadataEntry {
        val surrogate = decoder.decodeSerializableValue(RpcPeerMetadataEntrySurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: RpcPeerMetadataEntry) {
        val surrogate = RpcPeerMetadataEntrySurrogate(value)
        encoder.encodeSerializableValue(RpcPeerMetadataEntrySurrogate.serializer(), surrogate)
    }
}

// -- surrogate --

@Serializable
private data class RpcPeerMetadataEntrySurrogate(
    val branch: String,
    val head: String,
    @SerialName("block_header") val blockHeader: String,
    val operations: String,
    val protocols: String,
    @SerialName("operation_hashes_for_block") val operationHashesForBlock: String,
    @SerialName("operations_for_block") val operationsForBlock: String,
    val checkpoint: String? = null,
    @SerialName("protocol_branch") val protocolBranch: String? = null,
    @SerialName("predecessor_header") val predecessorHeader: String? = null,
    val other: String,
) {
    fun toTarget(): RpcPeerMetadataEntry =
        when {
            checkpoint == null && protocolBranch == null && predecessorHeader == null -> RpcPeerMetadataEntry.V0(
                branch,
                head,
                blockHeader,
                operations,
                protocols,
                operationHashesForBlock,
                operationsForBlock,
                other,
            )
            checkpoint != null && protocolBranch != null && predecessorHeader != null -> RpcPeerMetadataEntry.V1(
                branch,
                head,
                blockHeader,
                operations,
                protocols,
                operationHashesForBlock,
                operationsForBlock,
                checkpoint,
                protocolBranch,
                predecessorHeader,
                other,
            )
            else -> failWithInvalidSerializedValue(this)
        }

    private fun failWithInvalidSerializedValue(value: RpcPeerMetadataEntrySurrogate): Nothing =
        throw SerializationException("Could not deserialize, `$value` is not a valid PeerMetadataEntry value.")
}

private fun RpcPeerMetadataEntrySurrogate(peerMetadataEntry: RpcPeerMetadataEntry): RpcPeerMetadataEntrySurrogate = with(peerMetadataEntry) {
    RpcPeerMetadataEntrySurrogate(
        branch,
        head,
        blockHeader,
        operations,
        protocols,
        operationHashesForBlock,
        operationsForBlock,
        checkpoint,
        protocolBranch,
        predecessorHeader,
        other,
    )
}