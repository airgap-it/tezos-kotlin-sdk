package it.airgap.tezos.michelson.internal.serializer

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.michelson.internal.utils.KJsonSerializer
import it.airgap.tezos.michelson.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

internal object MichelineLiteralSerializer : KJsonSerializer<MichelineLiteral> {
    object Field {
        const val INT = "int"
        const val STRING = "string"
        const val BYTES = "bytes"
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(MichelineLiteral::class.toString())

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): MichelineLiteral {
        val jsonObject = jsonElement as? JsonObject ?: failWithUnexpectedJsonType(jsonElement::class)

        return when {
            jsonObject.containsKey(Field.INT) -> jsonDecoder.json.decodeFromJsonElement(MichelineLiteral.Integer.serializer(), jsonObject)
            jsonObject.containsKey(Field.STRING) -> jsonDecoder.json.decodeFromJsonElement(MichelineLiteral.String.serializer(), jsonObject)
            jsonObject.containsKey(Field.BYTES) -> jsonDecoder.json.decodeFromJsonElement(MichelineLiteral.Bytes.serializer(), jsonObject)
            else -> failWithInvalidSerializedLiteral()
        }
    }

    override fun serialize(jsonEncoder: JsonEncoder, value: MichelineLiteral) {
        when (value) {
            is MichelineLiteral.Integer -> jsonEncoder.encodeSerializableValue(MichelineLiteral.Integer.serializer(), value)
            is MichelineLiteral.String -> jsonEncoder.encodeSerializableValue(MichelineLiteral.String.serializer(), value)
            is MichelineLiteral.Bytes -> jsonEncoder.encodeSerializableValue(MichelineLiteral.Bytes.serializer(), value)
        }
    }

    private fun failWithInvalidSerializedLiteral(): Nothing = throw SerializationException("Could not deserialize, invalid Micheline Literal.")
}

internal object MichelineLiteralBytesSerializer : KSerializer<MichelineLiteral.Bytes> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(MichelineLiteral.Bytes::class.toString()) {
        element<String>("bytes")
    }

    override fun deserialize(decoder: Decoder): MichelineLiteral.Bytes =
        decoder.decodeStructure(descriptor) {
            val bytes = decodeStringElement(descriptor, 0)

            MichelineLiteral.Bytes(bytes.asHexString().asString(withPrefix = true))
        }

    override fun serialize(encoder: Encoder, value: MichelineLiteral.Bytes) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.bytes.asHexString().asString(withPrefix = false))
        }
    }
}