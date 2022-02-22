package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.rpc.internal.utils.hasElements
import it.airgap.tezos.rpc.type.p2p.RpcPeerMetadataEntry
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

@OptIn(ExperimentalSerializationApi::class)
internal object RpcPeerMetadataEntrySerializer : KJsonSerializer<RpcPeerMetadataEntry> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(RpcPeerMetadataEntry::class.toString()) {
        element<String>("branch")
        element<String>("head")
        element<String>("block_header")
        element<String>("operations")
        element<String>("protocols")
        element<String>("operation_hashes_for_block")
        element<String>("operations_for_block")
        element<String>("checkpoint", isOptional = true)
        element<String>("protocol_branch", isOptional = true)
        element<String>("predecessor_head", isOptional = true)
        element<String>("other")
    }

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): RpcPeerMetadataEntry {
        val jsonObject = jsonElement as? JsonObject ?: failWithUnexpectedJsonType(jsonElement::class)

        return when {
            jsonObject.isV0() -> jsonDecoder.decodeSerializableValue(RpcPeerMetadataEntry.V0.serializer())
            jsonObject.isV1() -> jsonDecoder.decodeSerializableValue(RpcPeerMetadataEntry.V1.serializer())
            else -> failWithUnknownValue(jsonObject.toString())
        }
    }

    override fun serialize(jsonEncoder: JsonEncoder, value: RpcPeerMetadataEntry) {
        when (value) {
            is RpcPeerMetadataEntry.V0 -> jsonEncoder.encodeSerializableValue(RpcPeerMetadataEntry.V0.serializer(), value)
            is RpcPeerMetadataEntry.V1 -> jsonEncoder.encodeSerializableValue(RpcPeerMetadataEntry.V1.serializer(), value)
        }
    }

    private val v0ElementIndices: Set<Int> = setOf(0, 1, 2, 3, 4, 5, 9)
    private val v1ElementIndices: Set<Int> = v0ElementIndices + setOf(6, 7, 8)

    private fun JsonObject.isV0(): Boolean = hasElements(descriptor, v0ElementIndices)
    private fun JsonObject.isV1(): Boolean = hasElements(descriptor, v1ElementIndices)

    private fun failWithUnknownValue(value: String): Nothing = throw SerializationException("Unknown PeerMetadataEntry value `$value`.")
}