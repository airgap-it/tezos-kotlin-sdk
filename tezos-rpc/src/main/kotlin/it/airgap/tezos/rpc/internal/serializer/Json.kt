package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.type.encoded.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

internal fun rpcSerializersModule(
    stringToAddressConverter: Converter<String, Address>,
    stringToImplicitAddressConverter: Converter<String, ImplicitAddress>,
    stringToPublicKeyConverter: Converter<String, PublicKey>,
    stringToPublicKeyHashConverter: Converter<String, PublicKeyHash>,
    stringToBlindedPublicKeyHashConverter: Converter<String, BlindedPublicKeyHash>,
    stringToSignatureConverter: Converter<String, Signature>,
): SerializersModule = SerializersModule {
    contextual(Pair::class) { typeArgumentsSerializers -> PairSerializer(typeArgumentsSerializers[0], typeArgumentsSerializers[1]) }

    contextual(HexStringSerializer)

    contextual(MutezSerializer)

    contextual(TimestampSerializer)
    contextual(TimestampRfc3339Serializer)
    contextual(TimestampMillisSerializer)

    contextual(AddressSerializer(stringToAddressConverter))
    contextual(ImplicitAddressSerializer(stringToImplicitAddressConverter))
    contextual(PublicKeySerializer(stringToPublicKeyConverter))
    contextual(PublicKeyHashSerializer(stringToPublicKeyHashConverter))
    contextual(BlindedPublicKeyHashSerializer(stringToBlindedPublicKeyHashConverter))
    contextual(SignatureSerializer(stringToSignatureConverter))

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
    contextual(RandomHashSerializer)
    contextual(ScriptExprHashSerializer)
    contextual(Secp256K1PublicKeyHashSerializer)
    contextual(Secp256K1SignatureSerializer)

    contextual(ScriptSerializer)
    contextual(ParametersSerializer)
    contextual(EntrypointSerializer)
}

internal fun rpcJson(
    stringToAddressConverter: Converter<String, Address>,
    stringToImplicitAddressConverter: Converter<String, ImplicitAddress>,
    stringToPublicKeyConverter: Converter<String, PublicKey>,
    stringToPublicKeyHashConverter: Converter<String, PublicKeyHash>,
    stringToBlindedPublicKeyHashConverter: Converter<String, BlindedPublicKeyHash>,
    stringToSignatureConverter: Converter<String, Signature>,
): Json = Json {
    serializersModule = rpcSerializersModule(
        stringToAddressConverter,
        stringToImplicitAddressConverter,
        stringToPublicKeyConverter,
        stringToPublicKeyHashConverter,
        stringToBlindedPublicKeyHashConverter,
        stringToSignatureConverter,
    )
    ignoreUnknownKeys = true
}
