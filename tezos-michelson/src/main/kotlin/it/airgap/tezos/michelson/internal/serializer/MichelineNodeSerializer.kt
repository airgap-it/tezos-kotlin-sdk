package it.airgap.tezos.michelson.internal.serializer

import it.airgap.tezos.michelson.internal.utils.KJsonSerializer
import it.airgap.tezos.michelson.internal.utils.containsOneOfKeys
import it.airgap.tezos.michelson.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.*

internal object MichelineNodeSerializer : KJsonSerializer<MichelineNode> {
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
                MichelineLiteralSerializer.Field.INT,
                MichelineLiteralSerializer.Field.STRING,
                MichelineLiteralSerializer.Field.BYTES,
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