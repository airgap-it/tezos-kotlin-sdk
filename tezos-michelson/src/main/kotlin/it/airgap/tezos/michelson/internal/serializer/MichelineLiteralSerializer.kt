package it.airgap.tezos.michelson.internal.serializer

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object MichelineLiteralSerializer : KSerializer<MichelineLiteral> {
    override val descriptor: SerialDescriptor = MichelineLiteralSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): MichelineLiteral {
        val surrogate = decoder.decodeSerializableValue(MichelineLiteralSurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: MichelineLiteral) {
        val surrogate = MichelineLiteralSurrogate(value)
        encoder.encodeSerializableValue(MichelineLiteralSurrogate.serializer(), surrogate)
    }
}

internal object MichelineLiteralBytesSerializer : KSerializer<MichelineLiteral.Bytes> {
    override val descriptor: SerialDescriptor = MichelineLiteralBytesSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): MichelineLiteral.Bytes {
        val surrogate = decoder.decodeSerializableValue(MichelineLiteralBytesSurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: MichelineLiteral.Bytes) {
        val surrogate = MichelineLiteralBytesSurrogate(value)
        encoder.encodeSerializableValue(MichelineLiteralBytesSurrogate.serializer(), surrogate)
    }
}

// -- surrogate --

@Serializable
internal data class MichelineLiteralSurrogate(
    val int: String? = null,
    val string: String? = null,
    val bytes: String? = null,
) {
    fun toTarget(): MichelineLiteral =
        when {
            int != null && string == null && bytes == null -> MichelineLiteral.Integer(int)
            int == null && string != null && bytes == null -> MichelineLiteral.String(string)
            int == null && string == null && bytes != null -> MichelineLiteralBytesSurrogate(bytes).toTarget()
            else -> failWithInvalidSerializedValue()
        }

    private fun failWithInvalidSerializedValue(): Nothing = throw SerializationException("Could not deserialize, invalid Micheline Literal.")
}

internal fun MichelineLiteralSurrogate(literal: MichelineLiteral): MichelineLiteralSurrogate = with(literal) {
    when (this) {
        is MichelineLiteral.Integer -> MichelineLiteralSurrogate(int = int)
        is MichelineLiteral.String -> MichelineLiteralSurrogate(string = string)
        is MichelineLiteral.Bytes -> with(MichelineLiteralBytesSurrogate(this)) { MichelineLiteralSurrogate(bytes = bytes) }
    }
}

@Serializable
internal data class MichelineLiteralBytesSurrogate(val bytes: String) {
    fun toTarget(): MichelineLiteral.Bytes = MichelineLiteral.Bytes(bytes.asHexString().asString(withPrefix = true))
}

internal fun MichelineLiteralBytesSurrogate(literal: MichelineLiteral.Bytes): MichelineLiteralBytesSurrogate =
    MichelineLiteralBytesSurrogate(literal.bytes.asHexString().asString(withPrefix = false))