package it.airgap.tezos.michelson.internal.serializer

import it.airgap.tezos.michelson.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.Micheline
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

internal object MichelineSerializer : KSerializer<Micheline> {
    override val descriptor: SerialDescriptor = MichelineSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): Micheline {
        val surrogate = decoder.decodeSerializableValue(MichelineSurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: Micheline) {
        val surrogate = MichelineSurrogate(value)
        encoder.encodeSerializableValue(MichelineSurrogate.serializer(), surrogate)
    }
}

// -- surrogate --

@Serializable(with = MichelineSurrogateSerializer::class)
internal sealed interface MichelineSurrogate {
    fun toTarget(): Micheline

    @Serializable(with = MichelineSurrogateArraySerializer::class)
    @JvmInline
    value class Array(val nodes: List<Micheline>) : MichelineSurrogate {
        override fun toTarget(): Micheline = MichelineSequence(nodes)
    }

    @Serializable
    data class Object(
        val int: String? = null,
        val string: String? = null,
        val bytes: String? = null,
        val prim: MichelinePrimitiveApplication.Primitive? = null,
        val args: List<Micheline> = emptyList(),
        val annots: List<MichelinePrimitiveApplication.Annotation> = emptyList(),
    ) : MichelineSurrogate {
        override fun toTarget(): Micheline =
            when {
                (int != null || string != null || bytes != null) && prim == null -> MichelineLiteralSurrogate(int, string, bytes).toTarget()
                int == null && string == null && bytes == null && prim != null -> MichelinePrimitiveApplication(prim, args, annots)
                else -> failWithInvalidSerializedValue()
            }

        private fun failWithInvalidSerializedValue(): Nothing = throw SerializationException("Could not deserialize, invalid Micheline Node.")
    }
}

internal fun MichelineSurrogate(node: Micheline): MichelineSurrogate = with(node) {
    when (this) {
        is MichelineLiteral -> with(MichelineLiteralSurrogate(this)) { MichelineSurrogate.Object(int = int, string = string, bytes = bytes) }
        is MichelinePrimitiveApplication -> MichelineSurrogate.Object(prim = prim, args = args, annots = annots)
        is MichelineSequence -> MichelineSurrogate.Array(nodes)
    }
}

private object MichelineSurrogateSerializer : KJsonSerializer<MichelineSurrogate> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(MichelineSurrogate::class.toString())

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): MichelineSurrogate =
        when (jsonElement) {
            is JsonArray -> jsonDecoder.json.decodeFromJsonElement(MichelineSurrogate.Array.serializer(), jsonElement)
            is JsonObject -> jsonDecoder.json.decodeFromJsonElement(MichelineSurrogate.Object.serializer(), jsonElement)
            else -> failWithUnexpectedJsonType(jsonElement::class)
        }

    override fun serialize(jsonEncoder: JsonEncoder, value: MichelineSurrogate) {
        when (value) {
            is MichelineSurrogate.Array -> jsonEncoder.encodeSerializableValue(MichelineSurrogate.Array.serializer(), value)
            is MichelineSurrogate.Object -> jsonEncoder.encodeSerializableValue(MichelineSurrogate.Object.serializer(), value)
        }
    }
}

private object MichelineSurrogateArraySerializer : KListSerializer<MichelineSurrogate.Array, Micheline>(Micheline.serializer()) {
    override fun valueFromList(list: List<Micheline>): MichelineSurrogate.Array = MichelineSurrogate.Array(list)
    override fun valueToList(value: MichelineSurrogate.Array): List<Micheline> = value.nodes
}