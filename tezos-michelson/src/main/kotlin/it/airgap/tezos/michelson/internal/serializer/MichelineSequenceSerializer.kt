package it.airgap.tezos.michelson.internal.serializer

import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelineSequence
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object MichelineSequenceSerializer : KSerializer<MichelineSequence> {
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