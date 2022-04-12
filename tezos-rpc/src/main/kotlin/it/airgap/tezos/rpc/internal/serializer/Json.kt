package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

internal val rpcSerializersModule: SerializersModule = SerializersModule {
    contextual(HexString::class, HexStringSerializer)
    contextual(Timestamp::class, TimestampSerializer)

    contextual(SignatureEncoded::class, SignatureEncodedSerializer)

    contextual(BlockHash::class, BlockHashSerializer)
    contextual(BlockPayloadHash::class, BlockPayloadHashSerializer)
    contextual(ChainId::class, ChainIdSerializer)
    contextual(ContextHash::class, ContextHashSerializer)
    contextual(OperationListListHash::class, OperationListListHashSerializer)
    contextual(ProtocolHash::class, ProtocolHashSerializer)
}

internal val rpcJson: Json = Json {
    serializersModule = rpcSerializersModule
    ignoreUnknownKeys = true
}
