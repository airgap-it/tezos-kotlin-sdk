package it.airgap.tezos.rpc.internal.serializer

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

internal val rpcSerializersModule: SerializersModule = SerializersModule {
    contextual(LongSerializer)
    contextual(Pair::class) { typeArgumentsSerializers -> PairSerializer(typeArgumentsSerializers[0], typeArgumentsSerializers[1]) }

    contextual(HexStringSerializer)
    contextual(TimestampSerializer)
    contextual(TimestampRfc3339Serializer)
    contextual(TimestampMillisSerializer)

    contextual(AddressSerializer)
    contextual(ImplicitAddressSerializer)
    contextual(PublicKeyEncodedSerializer)
    contextual(PublicKeyHashEncodedSerializer)
    contextual(BlindedPublicKeyHashEncodedSerializer)
    contextual(SignatureEncodedSerializer)

    contextual(BlockHashSerializer)
    contextual(BlockPayloadHashSerializer)
    contextual(ChainIdSerializer)
    contextual(ContextHashSerializer)
    contextual(ContractHashSerializer)
    contextual(CryptoboxPublicKeyHashSerializer)
    contextual(Ed25519BlindedPublicKeyHashSerializer)
    contextual(Ed25519PublicKeyHashSerializer)
    contextual(Ed25519SignatureSerializer)
    contextual(NonceHashSerializer)
    contextual(GenericSignatureSerializer)
    contextual(OperationHashSerializer)
    contextual(OperationListListHashSerializer)
    contextual(P256PublicKeyHashSerializer)
    contextual(P256SignatureSerializer)
    contextual(ProtocolHashSerializer)
    contextual(ScriptExprHashSerializer)
    contextual(Secp256K1PublicKeyHashSerializer)
    contextual(Secp256K1SignatureSerializer)
}

internal val rpcJson: Json = Json {
    serializersModule = rpcSerializersModule
    ignoreUnknownKeys = true
}
