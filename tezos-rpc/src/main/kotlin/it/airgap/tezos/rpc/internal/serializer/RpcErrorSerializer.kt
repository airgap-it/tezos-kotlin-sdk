package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.type.RpcError
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

@OptIn(ExperimentalSerializationApi::class)
internal object RpcErrorSerializer : KJsonSerializer<RpcError> {
    override val descriptor: SerialDescriptor = RpcErrorSurrogate.serializer().descriptor

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): RpcError {
        val fields = jsonDecoder.json.decodeFromJsonElement(MapSerializer(String.serializer(), String.serializer()), jsonElement)

        val surrogate = jsonDecoder.json.decodeFromJsonElement(RpcErrorSurrogate.serializer(), jsonElement)
        val details = fields.filterNot { descriptor.elementNames.contains(it.key) }

        return surrogate.toTarget(details)
    }

    override fun serialize(jsonEncoder: JsonEncoder, value: RpcError) {
        val surrogate = RpcErrorSurrogate(value)
        val fields = jsonEncoder.json.encodeToJsonElement(surrogate).jsonObject + value.details.orEmpty().map { it.key to jsonEncoder.json.encodeToJsonElement(it.value) }

        jsonEncoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(fields))
    }
}

internal object RpcErrorKindSerializer : KSerializer<RpcError.Kind> {
    private object SerialName {
        const val PERMANENT = "permanent"
        const val TEMPORARY = "temporary"
        const val BRANCH = "branch"
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(RpcError.Kind::class.toString())

    override fun deserialize(decoder: Decoder): RpcError.Kind =
        when (val string = decoder.decodeString()) {
            SerialName.PERMANENT -> RpcError.Kind.Permanent
            SerialName.TEMPORARY -> RpcError.Kind.Temporary
            SerialName.BRANCH -> RpcError.Kind.Branch
            else -> RpcError.Kind.Unknown(string)
        }

    override fun serialize(encoder: Encoder, value: RpcError.Kind) {
        encoder.encodeString(serialName(value))
    }

    private fun serialName(kind: RpcError.Kind): String = when (kind) {
        is RpcError.Kind.Permanent -> SerialName.PERMANENT
        is RpcError.Kind.Temporary -> SerialName.TEMPORARY
        is RpcError.Kind.Branch -> SerialName.BRANCH
        is RpcError.Kind.Unknown -> kind.name
    }
}

// -- surrogate --

@Serializable
private data class RpcErrorSurrogate(val kind: RpcError.Kind, val id: String) {
    fun toTarget(details: Map<String, String>?): RpcError = RpcError(kind, id, details)
}

private fun RpcErrorSurrogate(error: RpcError): RpcErrorSurrogate = with (error) {
    RpcErrorSurrogate(kind, id)
}