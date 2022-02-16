package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.rpc.shell.data.RpcHistoryMode
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.*

internal object RpcHistoryModeSerializer : KJsonSerializer<RpcHistoryMode> {
    private object SerialName {
        const val ARCHIVE = "archive"
        const val FULL = "full"
        const val ROLLING = "rolling"
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(RpcHistoryMode::class.toString())

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): RpcHistoryMode =
        when (jsonElement) {
            is JsonPrimitive -> deserializePrimitive(jsonElement)
            is JsonObject -> deserializeObject(jsonDecoder, jsonElement)
            else -> failWithUnexpectedJsonType(jsonElement::class)
        }

    private fun deserializePrimitive(jsonPrimitive: JsonPrimitive): RpcHistoryMode =
        when (jsonPrimitive.content) {
            SerialName.ARCHIVE -> RpcHistoryMode.Archive
            SerialName.FULL -> RpcHistoryMode.Full()
            SerialName.ROLLING -> RpcHistoryMode.Rolling()
            else -> failWithUnknownValue(jsonPrimitive.content)
        }

    private fun deserializeObject(jsonDecoder: JsonDecoder, jsonObject: JsonObject): RpcHistoryMode =
        when {
            jsonObject.containsKey(SerialName.FULL) -> jsonDecoder.decodeSerializableValue(RpcHistoryMode.Full.serializer())
            jsonObject.containsKey(SerialName.ROLLING) -> jsonDecoder.decodeSerializableValue(RpcHistoryMode.Rolling.serializer())
            else -> failWithUnknownValue(jsonObject.toString())
        }

    override fun serialize(jsonEncoder: JsonEncoder, value: RpcHistoryMode) {
        when (value) {
            RpcHistoryMode.Archive -> jsonEncoder.encodeString(SerialName.ARCHIVE)
            is RpcHistoryMode.Full -> value.full?.run { jsonEncoder.encodeSerializableValue(RpcHistoryMode.Full.serializer(), value) } ?: run { jsonEncoder.encodeString(SerialName.FULL) }
            is RpcHistoryMode.Rolling -> value.rolling?.run { jsonEncoder.encodeSerializableValue(RpcHistoryMode.Rolling.serializer(), value) } ?: run { jsonEncoder.encodeString(SerialName.ROLLING) }
        }
    }

    private fun failWithUnknownValue(value: String): Nothing = throw SerializationException("Unknown HistoryMode value `$value`.")
}