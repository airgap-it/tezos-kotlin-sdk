package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.rpc.type.primitive.Unistring
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.*

internal object UnistringSerializer : KJsonSerializer<Unistring> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(Unistring::class.toString())

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): Unistring =
        when (jsonElement) {
            is JsonPrimitive -> jsonDecoder.decodeSerializableValue(Unistring.PlainUtf8.serializer())
            is JsonObject -> jsonDecoder.decodeSerializableValue(Unistring.InvalidUtf8.serializer())
            else -> failWithUnexpectedJsonType(jsonElement::class)
        }

    override fun serialize(jsonEncoder: JsonEncoder, value: Unistring) =
        when (value) {
            is Unistring.PlainUtf8 -> jsonEncoder.encodeString(value.string)
            is Unistring.InvalidUtf8 -> jsonEncoder.encodeSerializableValue(Unistring.InvalidUtf8.serializer(), value)
        }
}