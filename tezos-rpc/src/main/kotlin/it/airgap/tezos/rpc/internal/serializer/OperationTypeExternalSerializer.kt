package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.operation.contract.Script
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// -- Script --

internal object ScriptSerializer : KSerializer<Script> {
    override val descriptor: SerialDescriptor = ScriptSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): Script {
        val surrogate = decoder.decodeSerializableValue(ScriptSurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: Script) {
        val surrogate = ScriptSurrogate(value)
        encoder.encodeSerializableValue(ScriptSurrogate.serializer(), surrogate)
    }
}

@Serializable
private data class ScriptSurrogate(val code: Micheline, val storage: Micheline) {
    fun toTarget(): Script = Script(code, storage)
}

private fun ScriptSurrogate(value: Script): ScriptSurrogate = with(value) {
    ScriptSurrogate(code, storage)
}

// -- Parameters --

internal object ParametersSerializer : KSerializer<Parameters> {
    override val descriptor: SerialDescriptor = ParametersSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): Parameters {
        val surrogate = decoder.decodeSerializableValue(ParametersSurrogate.serializer())
        return surrogate.toTarget()
    }

    override fun serialize(encoder: Encoder, value: Parameters) {
        val surrogate = ParametersSurrogate(value)
        encoder.encodeSerializableValue(ParametersSurrogate.serializer(), surrogate)
    }
}

@Serializable
private data class ParametersSurrogate(val entrypoint: @Contextual Entrypoint, val value: Micheline) {
    fun toTarget(): Parameters = Parameters(entrypoint, value)
}

private fun ParametersSurrogate(value: Parameters): ParametersSurrogate = with(value) {
    ParametersSurrogate(entrypoint, this.value)
}

// -- Entrypoint --

internal object EntrypointSerializer : KStringSerializer<Entrypoint>(Entrypoint::class) {
    override fun valueFromString(string: String): Entrypoint = Entrypoint(string)
    override fun valueToString(value: Entrypoint): String = value.value
}