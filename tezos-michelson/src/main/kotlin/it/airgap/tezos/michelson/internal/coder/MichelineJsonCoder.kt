package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.michelson.internal.utils.KJsonSerializer
import it.airgap.tezos.michelson.internal.utils.containsOneOfKeys
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.*
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class)
@InternalTezosSdkApi
public class MichelineJsonCoder : Coder<MichelineNode, JsonElement> {
    override fun encode(value: MichelineNode): JsonElement = Json.encodeToJsonElement(MichelineNode.serializer(), value)
    override fun decode(value: JsonElement): MichelineNode = Json.decodeFromJsonElement(MichelineNode.serializer(), value)

    internal object NodeSerializer : KJsonSerializer<MichelineNode> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor(MichelineNode::class.toString())

        override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): MichelineNode =
            when (jsonElement) {
                is JsonArray -> deserializeJsonArray(jsonDecoder, jsonElement)
                is JsonObject -> deserializeJsonObject(jsonDecoder, jsonElement)
                else -> failWithUnexpectedJsonType(jsonElement::class)
            }

        private fun deserializeJsonArray(decoder: JsonDecoder, jsonArray: JsonArray): MichelineNode =
            decoder.json.decodeFromJsonElement(MichelineSequence.serializer(), jsonArray)

        private fun deserializeJsonObject(decoder: JsonDecoder, jsonObject: JsonObject): MichelineNode =
            when {
                jsonObject.containsOneOfKeys(
                    LiteralSerializer.Field.INT,
                    LiteralSerializer.Field.STRING,
                    LiteralSerializer.Field.BYTES,
                ) -> decoder.json.decodeFromJsonElement(MichelineLiteral.serializer(), jsonObject)
                else -> decoder.json.decodeFromJsonElement(MichelinePrimitiveApplication.serializer(), jsonObject)
            }

        override fun serialize(jsonEncoder: JsonEncoder, value: MichelineNode) {
            when (value) {
                is MichelineLiteral -> jsonEncoder.encodeSerializableValue(MichelineLiteral.serializer(), value)
                is MichelinePrimitiveApplication -> jsonEncoder.encodeSerializableValue(MichelinePrimitiveApplication.serializer(), value)
                is MichelineSequence -> jsonEncoder.encodeSerializableValue(MichelineSequence.serializer(), value)
            }
        }
    }

    internal object LiteralSerializer : KJsonSerializer<MichelineLiteral> {
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

    internal object LiteralBytesSerializer : KSerializer<MichelineLiteral.Bytes> {
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

    internal object SequenceSerializer : KSerializer<MichelineSequence> {
        private val listSerializer = ListSerializer(MichelineNode.serializer())

        override val descriptor: SerialDescriptor = listSerializer.descriptor

        override fun deserialize(decoder: Decoder): MichelineSequence {
            val expressions = listSerializer.deserialize(decoder)

            return MichelineSequence(expressions)
        }

        override fun serialize(encoder: Encoder, value: MichelineSequence) {
            encoder.encodeSerializableValue(listSerializer, value.nodes)
        }
    }
}

private fun failWithUnexpectedJsonType(type: KClass<out JsonElement>): Nothing =
    throw SerializationException("Could not deserialize, unexpected JSON type $type.")