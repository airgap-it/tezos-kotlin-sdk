package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.rpc.type.history.RpcAdditionalCycles
import it.airgap.tezos.rpc.type.history.RpcHistoryMode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

internal object RpcHistoryModeSerializer : KSerializer<RpcHistoryMode> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(RpcHistoryMode::class.toString())

    override fun deserialize(decoder: Decoder): RpcHistoryMode {
        val surrogate = decoder.decodeSerializableValue(RpcHistoryModeSurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: RpcHistoryMode) {
        val surrogate = RpcHistoryModeSurrogate(value)
        encoder.encodeSerializableValue(RpcHistoryModeSurrogate.serializer(), surrogate)
    }
}

// -- surrogate --

@Serializable(with = RpcHistoryModeSurrogateSerializer::class)
private sealed interface RpcHistoryModeSurrogate {

    fun toTarget(): RpcHistoryMode

    @Serializable
    enum class Primitive : RpcHistoryModeSurrogate {
        @SerialName("archive") Archive,
        @SerialName("full") Full,
        @SerialName("rolling") Rolling;

        override fun toTarget(): RpcHistoryMode =
            when (this) {
                Archive -> RpcHistoryMode.Archive
                Full -> RpcHistoryMode.Full()
                Rolling -> RpcHistoryMode.Rolling()
            }
    }

    @Serializable
    data class Object(
        val full: RpcAdditionalCycles? = null,
        val rolling: RpcAdditionalCycles? = null,
    ) : RpcHistoryModeSurrogate {
        override fun toTarget(): RpcHistoryMode =
            when {
                full != null && rolling == null -> RpcHistoryMode.Full(full)
                full == null && rolling != null -> RpcHistoryMode.Rolling(rolling)
                else -> failWithInvalidSerializedValue(this)
            }

        private fun failWithInvalidSerializedValue(value: RpcHistoryModeSurrogate): Nothing =
            throw SerializationException("Could not deserialize, `$value` is not a valid HistoryMode value.")
    }
}

private fun RpcHistoryModeSurrogate(historyMode: RpcHistoryMode): RpcHistoryModeSurrogate = with(historyMode) {
    when (this) {
        RpcHistoryMode.Archive -> RpcHistoryModeSurrogate.Primitive.Archive
        is RpcHistoryMode.Full -> if (full != null) RpcHistoryModeSurrogate.Object(full = full) else RpcHistoryModeSurrogate.Primitive.Full
        is RpcHistoryMode.Rolling -> if (rolling != null) RpcHistoryModeSurrogate.Object(rolling = rolling) else RpcHistoryModeSurrogate.Primitive.Rolling
    }
}

private object RpcHistoryModeSurrogateSerializer : KJsonSerializer<RpcHistoryModeSurrogate> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(RpcHistoryModeSurrogate::class.toString())

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): RpcHistoryModeSurrogate =
        when (jsonElement) {
            is JsonPrimitive -> jsonDecoder.json.decodeFromJsonElement(RpcHistoryModeSurrogate.Primitive.serializer(), jsonElement)
            is JsonObject -> jsonDecoder.json.decodeFromJsonElement(RpcHistoryModeSurrogate.Object.serializer(), jsonElement)
            else -> failWithUnexpectedJsonType(jsonElement::class)
        }

    override fun serialize(jsonEncoder: JsonEncoder, value: RpcHistoryModeSurrogate) {
        when (value) {
            is RpcHistoryModeSurrogate.Primitive -> jsonEncoder.encodeSerializableValue(RpcHistoryModeSurrogate.Primitive.serializer(), value)
            is RpcHistoryModeSurrogate.Object -> jsonEncoder.encodeSerializableValue(RpcHistoryModeSurrogate.Object.serializer(), value)
        }
    }
}