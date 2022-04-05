package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.startsWith

// -- Encoded --

public sealed interface Encoded {
    public val base58: String

    @InternalTezosSdkApi
    public val meta: MetaEncoded<*>

    public companion object {}
}

// -- MetaEncoded --

@InternalTezosSdkApi
public sealed interface MetaEncoded<out Self : MetaEncoded<Self>> {
    public val kind: Kind<Self>
    public val encoded: Encoded

    public sealed interface Kind<out E : MetaEncoded<E>> {
        public val base58Prefix: String
        public val base58Bytes: ByteArray
        public val base58Length: Int

        public val bytesLength: Int

        public fun createValue(base58: String): E = createValueOrNull(base58) ?: failWithIllegalArgument("Invalid Base58 encoded data.")
        public fun createValueOrNull(base58: String): E?

        public operator fun plus(string: String): String = base58Prefix + string
        public operator fun plus(bytes: ByteArray): ByteArray = base58Bytes + bytes

        public fun isValid(string: String): Boolean = string.startsWith(base58Prefix) && string.length == base58Length
        public fun isValid(bytes: ByteArray): Boolean = bytes.size == bytesLength || (bytes.startsWith(base58Bytes) && bytes.size == bytesLength + base58Bytes.size)
        public fun isValid(bytes: List<Byte>): Boolean = bytes.size == bytesLength || (bytes.startsWith(base58Bytes) && bytes.size >= bytesLength + base58Bytes.size)

        public companion object {
            public val values: List<Kind<*>>
                get() = listOf(
                    BlockHash, /* B(51) */
                    BlockMetadataHash, /* bm(52) */
                    BlockPayloadHash, /* vh(52) */

                    OperationHash, /* o(51) */
                    OperationListHash, /* Lo(52) */
                    OperationListListHash, /* LLo(53) */

                    OperationMetadataHash, /* r(51) */
                    OperationMetadataListHash, /* Lr(52) */
                    OperationMetadataListListHash, /* LLr(53) */

                    ProtocolHash, /* P(51) */
                    ContextHash, /* Co(52) */
                    NonceHash, /* nce(53) */

                    Ed25519PublicKeyHash, /* tz1(36) */
                    Ed25519BlindedPublicKeyHash, /* btz1(37) */
                    Secp256K1PublicKeyHash, /* tz2(36) */
                    P256PublicKeyHash, /* tz3(36) */
                    ContractHash, /* KT1(36) */

                    Ed25519PublicKey, /* edpk(54) */
                    Secp256K1PublicKey, /* sppk(55) */
                    P256PublicKey, /* p2pk(55) */

                    Ed25519SecretKey, /* edsk(98) */
                    Secp256K1SecretKey, /* spsk(54) */
                    P256SecretKey, /* p2sk(54) */

                    Secp256K1EncryptedSecretKey, /* spesk(88) */
                    P256EncryptedSecretKey, /* p2esk(88) */

                    Ed25519Seed, /* edsk(54) */
                    Ed25519EncryptedSeed, /* edesk(88) */

                    CryptoboxPublicKeyHash, /* id(30) */

                    Secp256K1Element, /* GSp(54) */
                    Secp256K1Scalar, /* SSp(53) */
                    Secp256K1EncryptedScalar, /* seesk(93) */

                    Ed25519Signature, /* edsig(99) */
                    Secp256K1Signature, /* spsig1(99) */
                    P256Signature, /* p2sig(98) */
                    GenericSignature, /* sig(96) */

                    ChainId, /* Net(15) */

                    SaplingSpendingKey, /* sask(241) */
                    SaplingAddress, /* zet1(69) */

                    ScriptExprHash, /* expr(54) */
                )

            public fun recognize(string: String): Kind<*>? = values.find { it.isValid(string) }
            public fun recognize(bytes: ByteArray): Kind<*>? = values.find { it.isValid(bytes) }
            public fun recognize(bytes: List<Byte>): Kind<*>? = values.find { it.isValid(bytes) }
        }
    }
}