package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.rpc.internal.utils.getSerializable
import it.airgap.tezos.rpc.internal.utils.getString
import it.airgap.tezos.rpc.type.RpcError
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.*

@OptIn(ExperimentalSerializationApi::class)
internal object RpcErrorSerializer : KJsonSerializer<RpcError> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(RpcError::class.toString()) {
        element<RpcError.Kind>("kind")
        element<String>("id")
    }

    private val knownFields: Set<String>
        get() = descriptor.elementNames.toSet()

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): RpcError {
        val jsonObject = jsonElement as? JsonObject ?: failWithUnexpectedJsonType(jsonElement::class)

        val kind = jsonObject.getSerializable(descriptor.getElementName(0), jsonDecoder, RpcError.Kind.serializer())
        val id = jsonObject.getString(descriptor.getElementName(1))

        val details = jsonObject.entries
            .filterNot { knownFields.contains(it.key) }
            .associate { it.key to it.value.toString() }
            .takeIf { it.isNotEmpty() }

        return RpcError(kind, id, details)
    }

    override fun serialize(jsonEncoder: JsonEncoder, value: RpcError) {
        val fields = mapOf(
            descriptor.getElementName(0) to jsonEncoder.json.encodeToJsonElement(RpcError.Kind.serializer(), value.kind),
            descriptor.getElementName(1) to jsonEncoder.json.encodeToJsonElement(JsonPrimitive.serializer(), JsonPrimitive(value.id))
        ) + value.details.orEmpty().map { it.key to jsonEncoder.json.encodeToJsonElement(it.value) }

        jsonEncoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(fields))
    }
}

internal object RpcErrorKindSerializer : KJsonSerializer<RpcError.Kind> {
    private object SerialName {
        const val PERMANENT = "permanent"
        const val TEMPORARY = "temporary"
        const val BRANCH = "branch"
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(RpcError.Kind::class.toString())

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): RpcError.Kind {
        val string = (jsonElement as? JsonPrimitive)?.content ?: failWithUnexpectedJsonType(jsonElement::class)
        return when (string) {
            SerialName.PERMANENT -> RpcError.Kind.Permanent
            SerialName.TEMPORARY -> RpcError.Kind.Temporary
            SerialName.BRANCH -> RpcError.Kind.Branch
            else -> RpcError.Kind.Unknown(string)
        }
    }

    override fun serialize(jsonEncoder: JsonEncoder, value: RpcError.Kind) {
        jsonEncoder.encodeString(serialName(value))
    }

    private fun serialName(kind: RpcError.Kind) = when (kind) {
        is RpcError.Kind.Permanent -> SerialName.PERMANENT
        is RpcError.Kind.Temporary -> SerialName.TEMPORARY
        is RpcError.Kind.Branch -> SerialName.BRANCH
        is RpcError.Kind.Unknown -> kind.name
    }
}