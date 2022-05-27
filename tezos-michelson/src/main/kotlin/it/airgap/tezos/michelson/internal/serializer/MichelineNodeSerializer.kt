package it.airgap.tezos.michelson.internal.serializer

import it.airgap.tezos.michelson.internal.utils.KJsonSerializer
import it.airgap.tezos.michelson.internal.utils.KListSerializer
import it.airgap.tezos.michelson.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

internal object MichelineNodeSerializer : KSerializer<MichelineNode> {
    override val descriptor: SerialDescriptor = MichelineNodeSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): MichelineNode {
        val surrogate = decoder.decodeSerializableValue(MichelineNodeSurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: MichelineNode) {
        val surrogate = MichelineNodeSurrogate(value)
        encoder.encodeSerializableValue(MichelineNodeSurrogate.serializer(), surrogate)
    }
}

// -- surrogate --

@Serializable(with = MichelineNodeSurrogateSerializer::class)
internal sealed interface MichelineNodeSurrogate {
    fun toTarget(): MichelineNode

    @Serializable(with = MichelineNodeSurrogateArraySerializer::class)
    @JvmInline
    value class Array(val nodes: List<MichelineNode>) : MichelineNodeSurrogate {
        override fun toTarget(): MichelineNode = MichelineSequence(nodes)
    }

    @Serializable
    data class Object(
        val int: String? = null,
        val string: String? = null,
        val bytes: String? = null,
        val prim: MichelinePrimitiveApplication.Primitive? = null,
        val args: List<MichelineNode> = emptyList(),
        val annots: List<MichelinePrimitiveApplication.Annotation> = emptyList(),
    ) : MichelineNodeSurrogate {
        override fun toTarget(): MichelineNode =
            when {
                (int != null || string != null || bytes != null) && prim == null -> MichelineLiteralSurrogate(int, string, bytes).toTarget()
                int == null && string == null && bytes == null && prim != null -> MichelinePrimitiveApplication(prim, args, annots)
                else -> failWithInvalidSerializedValue()
            }

        private fun failWithInvalidSerializedValue(): Nothing = throw SerializationException("Could not deserialize, invalid Micheline Node.")
    }
}

internal fun MichelineNodeSurrogate(node: MichelineNode): MichelineNodeSurrogate = with(node) {
    when (this) {
        is MichelineLiteral -> with(MichelineLiteralSurrogate(this)) { MichelineNodeSurrogate.Object(int = int, string = string, bytes = bytes) }
        is MichelinePrimitiveApplication -> MichelineNodeSurrogate.Object(prim = prim, args = args, annots = annots)
        is MichelineSequence -> MichelineNodeSurrogate.Array(nodes)
    }
}

private object MichelineNodeSurrogateSerializer : KJsonSerializer<MichelineNodeSurrogate> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(MichelineNodeSurrogate::class.toString())

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): MichelineNodeSurrogate =
        when (jsonElement) {
            is JsonArray -> jsonDecoder.json.decodeFromJsonElement(MichelineNodeSurrogate.Array.serializer(), jsonElement)
            is JsonObject -> jsonDecoder.json.decodeFromJsonElement(MichelineNodeSurrogate.Object.serializer(), jsonElement)
            else -> failWithUnexpectedJsonType(jsonElement::class)
        }

    override fun serialize(jsonEncoder: JsonEncoder, value: MichelineNodeSurrogate) {
        when (value) {
            is MichelineNodeSurrogate.Array -> jsonEncoder.encodeSerializableValue(MichelineNodeSurrogate.Array.serializer(), value)
            is MichelineNodeSurrogate.Object -> jsonEncoder.encodeSerializableValue(MichelineNodeSurrogate.Object.serializer(), value)
        }
    }
}

private object MichelineNodeSurrogateArraySerializer : KListSerializer<MichelineNodeSurrogate.Array, MichelineNode>(MichelineNode.serializer()) {
    override fun valueFromList(list: List<MichelineNode>): MichelineNodeSurrogate.Array = MichelineNodeSurrogate.Array(list)
    override fun valueToList(value: MichelineNodeSurrogate.Array): List<MichelineNode> = value.nodes
}