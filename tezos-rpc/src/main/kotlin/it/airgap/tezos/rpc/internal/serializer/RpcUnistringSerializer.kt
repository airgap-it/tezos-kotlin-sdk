package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.rpc.type.primitive.RpcUnistring
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.*

internal object RpcUnistringSerializer : KJsonSerializer<RpcUnistring> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(RpcUnistring::class.toString())

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): RpcUnistring =
        when (jsonElement) {
            is JsonPrimitive -> jsonDecoder.json.decodeFromJsonElement(RpcUnistring.PlainUtf8.serializer(),  jsonElement)
            is JsonObject -> jsonDecoder.json.decodeFromJsonElement(RpcUnistring.InvalidUtf8.serializer(), jsonElement)
            else -> failWithUnexpectedJsonType(jsonElement::class)
        }

    override fun serialize(jsonEncoder: JsonEncoder, value: RpcUnistring) =
        when (value) {
            is RpcUnistring.PlainUtf8 -> jsonEncoder.encodeString(value.string)
            is RpcUnistring.InvalidUtf8 -> jsonEncoder.encodeSerializableValue(RpcUnistring.InvalidUtf8.serializer(), value)
        }
}