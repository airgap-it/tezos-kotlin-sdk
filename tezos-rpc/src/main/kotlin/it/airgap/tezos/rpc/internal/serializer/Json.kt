package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

internal val rpcSerializersModule: SerializersModule = SerializersModule {
    contextual(Long::class, LongSerializer)
    contextual(Pair::class) { typeArgumentsSerializers -> PairSerializer(typeArgumentsSerializers[0], typeArgumentsSerializers[1]) }

    contextual(HexString::class, HexStringSerializer)
    contextual(Timestamp::class, TimestampSerializer)

    contextual(Address::class, AddressSerializer)
    contextual(ImplicitAddress::class, ImplicitAddressSerializer)
    contextual(PublicKeyEncoded::class, PublicKeyEncodedSerializer)
    contextual(PublicKeyHashEncoded::class, PublicKeyHashEncodedSerializer)
    contextual(BlindedPublicKeyHashEncoded::class, BlindedPublicKeyHashEncodedSerializer)
    contextual(SignatureEncoded::class, SignatureEncodedSerializer)

    contextual(BlockHash::class, BlockHashSerializer)
    contextual(BlockPayloadHash::class, BlockPayloadHashSerializer)
    contextual(ChainId::class, ChainIdSerializer)
    contextual(ContextHash::class, ContextHashSerializer)
    contextual(CryptoboxPublicKeyHash::class, CryptoboxPublicKeyHashSerializer)
    contextual(OperationHash::class, OperationHashSerializer)
    contextual(OperationListListHash::class, OperationListListHashSerializer)
    contextual(ProtocolHash::class, ProtocolHashSerializer)
}

internal val rpcJson: Json = Json {
    serializersModule = rpcSerializersModule
    ignoreUnknownKeys = true
}
