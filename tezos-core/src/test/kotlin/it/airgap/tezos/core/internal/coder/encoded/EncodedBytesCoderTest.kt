package it.airgap.tezos.core.internal.coder.encoded

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.coder.encoded.decodeConsumingFromBytes
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.type.encoded.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EncodedBytesCoderTest {
    @MockK
    private lateinit var crypto: Crypto

    private lateinit var encodedBytesCoder: EncodedBytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)
        encodedBytesCoder = EncodedBytesCoder(base58Check)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode Encoded to bytes`() {
        assertContentEquals(blockHash.second, encodedBytesCoder.encode(blockHash.first))
        assertContentEquals(blockHash.second, blockHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(blockPayloadHash.second, encodedBytesCoder.encode(blockPayloadHash.first))
        assertContentEquals(blockPayloadHash.second, blockPayloadHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(blockMetadataHash.second, encodedBytesCoder.encode(blockMetadataHash.first))
        assertContentEquals(blockMetadataHash.second, blockMetadataHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(chainId.second, encodedBytesCoder.encode(chainId.first))
        assertContentEquals(chainId.second, chainId.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(contextHash.second, encodedBytesCoder.encode(contextHash.first))
        assertContentEquals(contextHash.second, contextHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(contractHash.second, encodedBytesCoder.encode(contractHash.first))
        assertContentEquals(contractHash.second, contractHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(cryptoboxPublicKeyHash.second, encodedBytesCoder.encode(cryptoboxPublicKeyHash.first))
        assertContentEquals(cryptoboxPublicKeyHash.second, cryptoboxPublicKeyHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(ed25519EncryptedSeed.second, encodedBytesCoder.encode(ed25519EncryptedSeed.first))
        assertContentEquals(ed25519EncryptedSeed.second, ed25519EncryptedSeed.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(ed25519PublicKey.second, encodedBytesCoder.encode(ed25519PublicKey.first))
        assertContentEquals(ed25519PublicKey.second, ed25519PublicKey.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(ed25519PublicKeyHash.second, encodedBytesCoder.encode(ed25519PublicKeyHash.first))
        assertContentEquals(ed25519PublicKeyHash.second, ed25519PublicKeyHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(ed25519SecretKey.second, encodedBytesCoder.encode(ed25519SecretKey.first))
        assertContentEquals(ed25519SecretKey.second, ed25519SecretKey.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(ed25519Seed.second, encodedBytesCoder.encode(ed25519Seed.first))
        assertContentEquals(ed25519Seed.second, ed25519Seed.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(ed25519Signature.second, encodedBytesCoder.encode(ed25519Signature.first))
        assertContentEquals(ed25519Signature.second, ed25519Signature.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(genericSignature.second, encodedBytesCoder.encode(genericSignature.first))
        assertContentEquals(genericSignature.second, genericSignature.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(nonceHash.second, encodedBytesCoder.encode(nonceHash.first))
        assertContentEquals(nonceHash.second, nonceHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(operationHash.second, encodedBytesCoder.encode(operationHash.first))
        assertContentEquals(operationHash.second, operationHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(operationListHash.second, encodedBytesCoder.encode(operationListHash.first))
        assertContentEquals(operationListHash.second, operationListHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(operationListListHash.second, encodedBytesCoder.encode(operationListListHash.first))
        assertContentEquals(operationListListHash.second, operationListListHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(operationMetadataHash.second, encodedBytesCoder.encode(operationMetadataHash.first))
        assertContentEquals(operationMetadataHash.second, operationMetadataHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(operationMetadataListHash.second, encodedBytesCoder.encode(operationMetadataListHash.first))
        assertContentEquals(operationMetadataListHash.second, operationMetadataListHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(operationMetadataListListHash.second, encodedBytesCoder.encode(operationMetadataListListHash.first))
        assertContentEquals(operationMetadataListListHash.second, operationMetadataListListHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(p256EncryptedSecretKey.second, encodedBytesCoder.encode(p256EncryptedSecretKey.first))
        assertContentEquals(p256EncryptedSecretKey.second, p256EncryptedSecretKey.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(p256PublicKey.second, encodedBytesCoder.encode(p256PublicKey.first))
        assertContentEquals(p256PublicKey.second, p256PublicKey.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(p256PublicKeyHash.second, encodedBytesCoder.encode(p256PublicKeyHash.first))
        assertContentEquals(p256PublicKeyHash.second, p256PublicKeyHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(p256SecretKey.second, encodedBytesCoder.encode(p256SecretKey.first))
        assertContentEquals(p256SecretKey.second, p256SecretKey.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(p256Signature.second, encodedBytesCoder.encode(p256Signature.first))
        assertContentEquals(p256Signature.second, p256Signature.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(protocolHash.second, encodedBytesCoder.encode(protocolHash.first))
        assertContentEquals(protocolHash.second, protocolHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(saplingAddress.second, encodedBytesCoder.encode(saplingAddress.first))
        assertContentEquals(saplingAddress.second, saplingAddress.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(saplingSpendingKey.second, encodedBytesCoder.encode(saplingSpendingKey.first))
        assertContentEquals(saplingSpendingKey.second, saplingSpendingKey.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(secp256K1Element.second, encodedBytesCoder.encode(secp256K1Element.first))
        assertContentEquals(secp256K1Element.second, secp256K1Element.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(secp256K1EncryptedScalar.second, encodedBytesCoder.encode(secp256K1EncryptedScalar.first))
        assertContentEquals(secp256K1EncryptedScalar.second, secp256K1EncryptedScalar.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(secp256K1EncryptedSecretKey.second, encodedBytesCoder.encode(secp256K1EncryptedSecretKey.first))
        assertContentEquals(secp256K1EncryptedSecretKey.second, secp256K1EncryptedSecretKey.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(secp256K1PublicKey.second, encodedBytesCoder.encode(secp256K1PublicKey.first))
        assertContentEquals(secp256K1PublicKey.second, secp256K1PublicKey.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(secp256K1PublicKeyHash.second, encodedBytesCoder.encode(secp256K1PublicKeyHash.first))
        assertContentEquals(secp256K1PublicKeyHash.second, secp256K1PublicKeyHash.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(secp256K1Scalar.second, encodedBytesCoder.encode(secp256K1Scalar.first))
        assertContentEquals(secp256K1Scalar.second, secp256K1Scalar.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(secp256K1SecretKey.second, encodedBytesCoder.encode(secp256K1SecretKey.first))
        assertContentEquals(secp256K1SecretKey.second, secp256K1SecretKey.first.encodeToBytes(encodedBytesCoder))

        assertContentEquals(secp256K1Signature.second, encodedBytesCoder.encode(secp256K1Signature.first))
        assertContentEquals(secp256K1Signature.second, secp256K1Signature.first.encodeToBytes(encodedBytesCoder))
    }

    @Test
    fun `should decode Encoded from bytes`() {
        assertEquals(blockHash.first, encodedBytesCoder.decode(blockHash.first.kind.base58Bytes + blockHash.second))
        assertEquals(blockHash.first, encodedBytesCoder.decodeConsuming((blockHash.first.kind.base58Bytes + blockHash.second).toMutableList()))
        assertEquals(blockHash.first, encodedBytesCoder.decode(blockHash.second, blockHash.first.kind))
        assertEquals(blockHash.first, encodedBytesCoder.decodeConsuming(blockHash.second.toMutableList(), blockHash.first.kind))
        assertEquals(blockHash.first, BlockHash.decodeFromBytes(blockHash.second, encodedBytesCoder))
        assertEquals(blockHash.first, BlockHash.decodeConsumingFromBytes(blockHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(blockPayloadHash.first, encodedBytesCoder.decode(blockPayloadHash.first.kind.base58Bytes + blockPayloadHash.second))
        assertEquals(blockPayloadHash.first, encodedBytesCoder.decodeConsuming((blockPayloadHash.first.kind.base58Bytes + blockPayloadHash.second).toMutableList()))
        assertEquals(blockPayloadHash.first, encodedBytesCoder.decode(blockPayloadHash.second, blockPayloadHash.first.kind))
        assertEquals(blockPayloadHash.first, encodedBytesCoder.decodeConsuming(blockPayloadHash.second.toMutableList(), blockPayloadHash.first.kind))
        assertEquals(blockPayloadHash.first, BlockPayloadHash.decodeFromBytes(blockPayloadHash.second, encodedBytesCoder))
        assertEquals(blockPayloadHash.first, BlockPayloadHash.decodeConsumingFromBytes(blockPayloadHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(blockMetadataHash.first, encodedBytesCoder.decode(blockMetadataHash.first.kind.base58Bytes + blockMetadataHash.second))
        assertEquals(blockMetadataHash.first, encodedBytesCoder.decodeConsuming((blockMetadataHash.first.kind.base58Bytes + blockMetadataHash.second).toMutableList()))
        assertEquals(blockMetadataHash.first, encodedBytesCoder.decode(blockMetadataHash.second, blockMetadataHash.first.kind))
        assertEquals(blockMetadataHash.first, encodedBytesCoder.decodeConsuming(blockMetadataHash.second.toMutableList(), blockMetadataHash.first.kind))
        assertEquals(blockMetadataHash.first, BlockMetadataHash.decodeFromBytes(blockMetadataHash.second, encodedBytesCoder))
        assertEquals(blockMetadataHash.first, BlockMetadataHash.decodeConsumingFromBytes(blockMetadataHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(chainId.first, encodedBytesCoder.decode(chainId.first.kind.base58Bytes + chainId.second))
        assertEquals(chainId.first, encodedBytesCoder.decodeConsuming((chainId.first.kind.base58Bytes + chainId.second).toMutableList()))
        assertEquals(chainId.first, encodedBytesCoder.decode(chainId.second, chainId.first.kind))
        assertEquals(chainId.first, encodedBytesCoder.decodeConsuming(chainId.second.toMutableList(), chainId.first.kind))
        assertEquals(chainId.first, ChainId.decodeFromBytes(chainId.second, encodedBytesCoder))
        assertEquals(chainId.first, ChainId.decodeConsumingFromBytes(chainId.second.toMutableList(), encodedBytesCoder))

        assertEquals(contextHash.first, encodedBytesCoder.decode(contextHash.first.kind.base58Bytes + contextHash.second))
        assertEquals(contextHash.first, encodedBytesCoder.decodeConsuming((contextHash.first.kind.base58Bytes + contextHash.second).toMutableList()))
        assertEquals(contextHash.first, encodedBytesCoder.decode(contextHash.second, contextHash.first.kind))
        assertEquals(contextHash.first, encodedBytesCoder.decodeConsuming(contextHash.second.toMutableList(), contextHash.first.kind))
        assertEquals(contextHash.first, ContextHash.decodeFromBytes(contextHash.second, encodedBytesCoder))
        assertEquals(contextHash.first, ContextHash.decodeConsumingFromBytes(contextHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(contractHash.first, encodedBytesCoder.decode(contractHash.first.kind.base58Bytes + contractHash.second))
        assertEquals(contractHash.first, encodedBytesCoder.decodeConsuming((contractHash.first.kind.base58Bytes + contractHash.second).toMutableList()))
        assertEquals(contractHash.first, encodedBytesCoder.decode(contractHash.second, contractHash.first.kind))
        assertEquals(contractHash.first, encodedBytesCoder.decodeConsuming(contractHash.second.toMutableList(), contractHash.first.kind))
        assertEquals(contractHash.first, ContractHash.decodeFromBytes(contractHash.second, encodedBytesCoder))
        assertEquals(contractHash.first, ContractHash.decodeConsumingFromBytes(contractHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(cryptoboxPublicKeyHash.first, encodedBytesCoder.decode(cryptoboxPublicKeyHash.first.kind.base58Bytes + cryptoboxPublicKeyHash.second))
        assertEquals(cryptoboxPublicKeyHash.first, encodedBytesCoder.decodeConsuming((cryptoboxPublicKeyHash.first.kind.base58Bytes + cryptoboxPublicKeyHash.second).toMutableList()))
        assertEquals(cryptoboxPublicKeyHash.first, encodedBytesCoder.decode(cryptoboxPublicKeyHash.second, cryptoboxPublicKeyHash.first.kind))
        assertEquals(cryptoboxPublicKeyHash.first, encodedBytesCoder.decodeConsuming(cryptoboxPublicKeyHash.second.toMutableList(), cryptoboxPublicKeyHash.first.kind))
        assertEquals(cryptoboxPublicKeyHash.first, CryptoboxPublicKeyHash.decodeFromBytes(cryptoboxPublicKeyHash.second, encodedBytesCoder))
        assertEquals(cryptoboxPublicKeyHash.first, CryptoboxPublicKeyHash.decodeConsumingFromBytes(cryptoboxPublicKeyHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(ed25519BlindedPublicKeyHash.first, encodedBytesCoder.decode(ed25519BlindedPublicKeyHash.first.kind.base58Bytes + ed25519BlindedPublicKeyHash.second))
        assertEquals(ed25519BlindedPublicKeyHash.first, encodedBytesCoder.decodeConsuming((ed25519BlindedPublicKeyHash.first.kind.base58Bytes + ed25519BlindedPublicKeyHash.second).toMutableList()))
        assertEquals(ed25519BlindedPublicKeyHash.first, encodedBytesCoder.decode(ed25519BlindedPublicKeyHash.second, ed25519BlindedPublicKeyHash.first.kind))
        assertEquals(ed25519BlindedPublicKeyHash.first, encodedBytesCoder.decodeConsuming(ed25519BlindedPublicKeyHash.second.toMutableList(), ed25519BlindedPublicKeyHash.first.kind))
        assertEquals(ed25519BlindedPublicKeyHash.first, Ed25519BlindedPublicKeyHash.decodeFromBytes(ed25519BlindedPublicKeyHash.second, encodedBytesCoder))
        assertEquals(ed25519BlindedPublicKeyHash.first, Ed25519BlindedPublicKeyHash.decodeConsumingFromBytes(ed25519BlindedPublicKeyHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(ed25519EncryptedSeed.first, encodedBytesCoder.decode(ed25519EncryptedSeed.first.kind.base58Bytes + ed25519EncryptedSeed.second))
        assertEquals(ed25519EncryptedSeed.first, encodedBytesCoder.decodeConsuming((ed25519EncryptedSeed.first.kind.base58Bytes + ed25519EncryptedSeed.second).toMutableList()))
        assertEquals(ed25519EncryptedSeed.first, encodedBytesCoder.decode(ed25519EncryptedSeed.second, ed25519EncryptedSeed.first.kind))
        assertEquals(ed25519EncryptedSeed.first, encodedBytesCoder.decodeConsuming(ed25519EncryptedSeed.second.toMutableList(), ed25519EncryptedSeed.first.kind))
        assertEquals(ed25519EncryptedSeed.first, Ed25519EncryptedSeed.decodeFromBytes(ed25519EncryptedSeed.second, encodedBytesCoder))
        assertEquals(ed25519EncryptedSeed.first, Ed25519EncryptedSeed.decodeConsumingFromBytes(ed25519EncryptedSeed.second.toMutableList(), encodedBytesCoder))

        assertEquals(ed25519PublicKey.first, encodedBytesCoder.decode(ed25519PublicKey.first.kind.base58Bytes + ed25519PublicKey.second))
        assertEquals(ed25519PublicKey.first, encodedBytesCoder.decodeConsuming((ed25519PublicKey.first.kind.base58Bytes + ed25519PublicKey.second).toMutableList()))
        assertEquals(ed25519PublicKey.first, encodedBytesCoder.decode(ed25519PublicKey.second, ed25519PublicKey.first.kind))
        assertEquals(ed25519PublicKey.first, encodedBytesCoder.decodeConsuming(ed25519PublicKey.second.toMutableList(), ed25519PublicKey.first.kind))
        assertEquals(ed25519PublicKey.first, Ed25519PublicKey.decodeFromBytes(ed25519PublicKey.second, encodedBytesCoder))
        assertEquals(ed25519PublicKey.first, Ed25519PublicKey.decodeConsumingFromBytes(ed25519PublicKey.second.toMutableList(), encodedBytesCoder))

        assertEquals(ed25519PublicKeyHash.first, encodedBytesCoder.decode(ed25519PublicKeyHash.first.kind.base58Bytes + ed25519PublicKeyHash.second))
        assertEquals(ed25519PublicKeyHash.first, encodedBytesCoder.decodeConsuming((ed25519PublicKeyHash.first.kind.base58Bytes + ed25519PublicKeyHash.second).toMutableList()))
        assertEquals(ed25519PublicKeyHash.first, encodedBytesCoder.decode(ed25519PublicKeyHash.second, ed25519PublicKeyHash.first.kind))
        assertEquals(ed25519PublicKeyHash.first, encodedBytesCoder.decodeConsuming(ed25519PublicKeyHash.second.toMutableList(), ed25519PublicKeyHash.first.kind))
        assertEquals(ed25519PublicKeyHash.first, Ed25519PublicKeyHash.decodeFromBytes(ed25519PublicKeyHash.second, encodedBytesCoder))
        assertEquals(ed25519PublicKeyHash.first, Ed25519PublicKeyHash.decodeConsumingFromBytes(ed25519PublicKeyHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(ed25519SecretKey.first, encodedBytesCoder.decode(ed25519SecretKey.first.kind.base58Bytes + ed25519SecretKey.second))
        assertEquals(ed25519SecretKey.first, encodedBytesCoder.decodeConsuming((ed25519SecretKey.first.kind.base58Bytes + ed25519SecretKey.second).toMutableList()))
        assertEquals(ed25519SecretKey.first, encodedBytesCoder.decode(ed25519SecretKey.second, ed25519SecretKey.first.kind))
        assertEquals(ed25519SecretKey.first, encodedBytesCoder.decodeConsuming(ed25519SecretKey.second.toMutableList(), ed25519SecretKey.first.kind))
        assertEquals(ed25519SecretKey.first, Ed25519SecretKey.decodeFromBytes(ed25519SecretKey.second, encodedBytesCoder))
        assertEquals(ed25519SecretKey.first, Ed25519SecretKey.decodeConsumingFromBytes(ed25519SecretKey.second.toMutableList(), encodedBytesCoder))

        assertEquals(ed25519Seed.first, encodedBytesCoder.decode(ed25519Seed.first.kind.base58Bytes + ed25519Seed.second))
        assertEquals(ed25519Seed.first, encodedBytesCoder.decodeConsuming((ed25519Seed.first.kind.base58Bytes + ed25519Seed.second).toMutableList()))
        assertEquals(ed25519Seed.first, encodedBytesCoder.decode(ed25519Seed.second, ed25519Seed.first.kind))
        assertEquals(ed25519Seed.first, encodedBytesCoder.decodeConsuming(ed25519Seed.second.toMutableList(), ed25519Seed.first.kind))
        assertEquals(ed25519Seed.first, Ed25519Seed.decodeFromBytes(ed25519Seed.second, encodedBytesCoder))
        assertEquals(ed25519Seed.first, Ed25519Seed.decodeConsumingFromBytes(ed25519Seed.second.toMutableList(), encodedBytesCoder))

        assertEquals(ed25519Signature.first, encodedBytesCoder.decode(ed25519Signature.first.kind.base58Bytes + ed25519Signature.second))
        assertEquals(ed25519Signature.first, encodedBytesCoder.decodeConsuming((ed25519Signature.first.kind.base58Bytes + ed25519Signature.second).toMutableList()))
        assertEquals(ed25519Signature.first, encodedBytesCoder.decode(ed25519Signature.second, ed25519Signature.first.kind))
        assertEquals(ed25519Signature.first, encodedBytesCoder.decodeConsuming(ed25519Signature.second.toMutableList(), ed25519Signature.first.kind))
        assertEquals(ed25519Signature.first, Ed25519Signature.decodeFromBytes(ed25519Signature.second, encodedBytesCoder))
        assertEquals(ed25519Signature.first, Ed25519Signature.decodeConsumingFromBytes(ed25519Signature.second.toMutableList(), encodedBytesCoder))

        assertEquals(genericSignature.first, encodedBytesCoder.decode(genericSignature.first.kind.base58Bytes + genericSignature.second))
        assertEquals(genericSignature.first, encodedBytesCoder.decodeConsuming((genericSignature.first.kind.base58Bytes + genericSignature.second).toMutableList()))
        assertEquals(genericSignature.first, encodedBytesCoder.decode(genericSignature.second, genericSignature.first.kind))
        assertEquals(genericSignature.first, encodedBytesCoder.decodeConsuming(genericSignature.second.toMutableList(), genericSignature.first.kind))
        assertEquals(genericSignature.first, GenericSignature.decodeFromBytes(genericSignature.second, encodedBytesCoder))
        assertEquals(genericSignature.first, GenericSignature.decodeConsumingFromBytes(genericSignature.second.toMutableList(), encodedBytesCoder))

        assertEquals(nonceHash.first, encodedBytesCoder.decode(nonceHash.first.kind.base58Bytes + nonceHash.second))
        assertEquals(nonceHash.first, encodedBytesCoder.decodeConsuming((nonceHash.first.kind.base58Bytes + nonceHash.second).toMutableList()))
        assertEquals(nonceHash.first, encodedBytesCoder.decode(nonceHash.second, nonceHash.first.kind))
        assertEquals(nonceHash.first, encodedBytesCoder.decodeConsuming(nonceHash.second.toMutableList(), nonceHash.first.kind))
        assertEquals(nonceHash.first, NonceHash.decodeFromBytes(nonceHash.second, encodedBytesCoder))
        assertEquals(nonceHash.first, NonceHash.decodeConsumingFromBytes(nonceHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(operationHash.first, encodedBytesCoder.decode(operationHash.first.kind.base58Bytes + operationHash.second))
        assertEquals(operationHash.first, encodedBytesCoder.decodeConsuming((operationHash.first.kind.base58Bytes + operationHash.second).toMutableList()))
        assertEquals(operationHash.first, encodedBytesCoder.decode(operationHash.second, operationHash.first.kind))
        assertEquals(operationHash.first, encodedBytesCoder.decodeConsuming(operationHash.second.toMutableList(), operationHash.first.kind))
        assertEquals(operationHash.first, OperationHash.decodeFromBytes(operationHash.second, encodedBytesCoder))
        assertEquals(operationHash.first, OperationHash.decodeConsumingFromBytes(operationHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(operationListHash.first, encodedBytesCoder.decode(operationListHash.first.kind.base58Bytes + operationListHash.second))
        assertEquals(operationListHash.first, encodedBytesCoder.decodeConsuming((operationListHash.first.kind.base58Bytes + operationListHash.second).toMutableList()))
        assertEquals(operationListHash.first, encodedBytesCoder.decode(operationListHash.second, operationListHash.first.kind))
        assertEquals(operationListHash.first, encodedBytesCoder.decodeConsuming(operationListHash.second.toMutableList(), operationListHash.first.kind))
        assertEquals(operationListHash.first, OperationListHash.decodeFromBytes(operationListHash.second, encodedBytesCoder))
        assertEquals(operationListHash.first, OperationListHash.decodeConsumingFromBytes(operationListHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(operationListListHash.first, encodedBytesCoder.decode(operationListListHash.first.kind.base58Bytes + operationListListHash.second))
        assertEquals(operationListListHash.first, encodedBytesCoder.decodeConsuming((operationListListHash.first.kind.base58Bytes + operationListListHash.second).toMutableList()))
        assertEquals(operationListListHash.first, encodedBytesCoder.decode(operationListListHash.second, operationListListHash.first.kind))
        assertEquals(operationListListHash.first, encodedBytesCoder.decodeConsuming(operationListListHash.second.toMutableList(), operationListListHash.first.kind))
        assertEquals(operationListListHash.first, OperationListListHash.decodeFromBytes(operationListListHash.second, encodedBytesCoder))
        assertEquals(operationListListHash.first, OperationListListHash.decodeConsumingFromBytes(operationListListHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(operationMetadataHash.first, encodedBytesCoder.decode(operationMetadataHash.first.kind.base58Bytes + operationMetadataHash.second))
        assertEquals(operationMetadataHash.first, encodedBytesCoder.decodeConsuming((operationMetadataHash.first.kind.base58Bytes + operationMetadataHash.second).toMutableList()))
        assertEquals(operationMetadataHash.first, encodedBytesCoder.decode(operationMetadataHash.second, operationMetadataHash.first.kind))
        assertEquals(operationMetadataHash.first, encodedBytesCoder.decodeConsuming(operationMetadataHash.second.toMutableList(), operationMetadataHash.first.kind))
        assertEquals(operationMetadataHash.first, OperationMetadataHash.decodeFromBytes(operationMetadataHash.second, encodedBytesCoder))
        assertEquals(operationMetadataHash.first, OperationMetadataHash.decodeConsumingFromBytes(operationMetadataHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(operationMetadataListHash.first, encodedBytesCoder.decode(operationMetadataListHash.first.kind.base58Bytes + operationMetadataListHash.second))
        assertEquals(operationMetadataListHash.first, encodedBytesCoder.decodeConsuming((operationMetadataListHash.first.kind.base58Bytes + operationMetadataListHash.second).toMutableList()))
        assertEquals(operationMetadataListHash.first, encodedBytesCoder.decode(operationMetadataListHash.second, operationMetadataListHash.first.kind))
        assertEquals(operationMetadataListHash.first, encodedBytesCoder.decodeConsuming(operationMetadataListHash.second.toMutableList(), operationMetadataListHash.first.kind))
        assertEquals(operationMetadataListHash.first, OperationMetadataListHash.decodeFromBytes(operationMetadataListHash.second, encodedBytesCoder))
        assertEquals(operationMetadataListHash.first, OperationMetadataListHash.decodeConsumingFromBytes(operationMetadataListHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(operationMetadataListListHash.first, encodedBytesCoder.decode(operationMetadataListListHash.first.kind.base58Bytes + operationMetadataListListHash.second))
        assertEquals(operationMetadataListListHash.first, encodedBytesCoder.decodeConsuming((operationMetadataListListHash.first.kind.base58Bytes + operationMetadataListListHash.second).toMutableList()))
        assertEquals(operationMetadataListListHash.first, encodedBytesCoder.decode(operationMetadataListListHash.second, operationMetadataListListHash.first.kind))
        assertEquals(operationMetadataListListHash.first, encodedBytesCoder.decodeConsuming(operationMetadataListListHash.second.toMutableList(), operationMetadataListListHash.first.kind))
        assertEquals(operationMetadataListListHash.first, OperationMetadataListListHash.decodeFromBytes(operationMetadataListListHash.second, encodedBytesCoder))
        assertEquals(operationMetadataListListHash.first, OperationMetadataListListHash.decodeConsumingFromBytes(operationMetadataListListHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(p256EncryptedSecretKey.first, encodedBytesCoder.decode(p256EncryptedSecretKey.first.kind.base58Bytes + p256EncryptedSecretKey.second))
        assertEquals(p256EncryptedSecretKey.first, encodedBytesCoder.decodeConsuming((p256EncryptedSecretKey.first.kind.base58Bytes + p256EncryptedSecretKey.second).toMutableList()))
        assertEquals(p256EncryptedSecretKey.first, encodedBytesCoder.decode(p256EncryptedSecretKey.second, p256EncryptedSecretKey.first.kind))
        assertEquals(p256EncryptedSecretKey.first, encodedBytesCoder.decodeConsuming(p256EncryptedSecretKey.second.toMutableList(), p256EncryptedSecretKey.first.kind))
        assertEquals(p256EncryptedSecretKey.first, P256EncryptedSecretKey.decodeFromBytes(p256EncryptedSecretKey.second, encodedBytesCoder))
        assertEquals(p256EncryptedSecretKey.first, P256EncryptedSecretKey.decodeConsumingFromBytes(p256EncryptedSecretKey.second.toMutableList(), encodedBytesCoder))

        assertEquals(p256PublicKey.first, encodedBytesCoder.decode(p256PublicKey.first.kind.base58Bytes + p256PublicKey.second))
        assertEquals(p256PublicKey.first, encodedBytesCoder.decodeConsuming((p256PublicKey.first.kind.base58Bytes + p256PublicKey.second).toMutableList()))
        assertEquals(p256PublicKey.first, encodedBytesCoder.decode(p256PublicKey.second, p256PublicKey.first.kind))
        assertEquals(p256PublicKey.first, encodedBytesCoder.decodeConsuming(p256PublicKey.second.toMutableList(), p256PublicKey.first.kind))
        assertEquals(p256PublicKey.first, P256PublicKey.decodeFromBytes(p256PublicKey.second, encodedBytesCoder))
        assertEquals(p256PublicKey.first, P256PublicKey.decodeConsumingFromBytes(p256PublicKey.second.toMutableList(), encodedBytesCoder))

        assertEquals(p256PublicKeyHash.first, encodedBytesCoder.decode(p256PublicKeyHash.first.kind.base58Bytes + p256PublicKeyHash.second))
        assertEquals(p256PublicKeyHash.first, encodedBytesCoder.decodeConsuming((p256PublicKeyHash.first.kind.base58Bytes + p256PublicKeyHash.second).toMutableList()))
        assertEquals(p256PublicKeyHash.first, encodedBytesCoder.decode(p256PublicKeyHash.second, p256PublicKeyHash.first.kind))
        assertEquals(p256PublicKeyHash.first, encodedBytesCoder.decodeConsuming(p256PublicKeyHash.second.toMutableList(), p256PublicKeyHash.first.kind))
        assertEquals(p256PublicKeyHash.first, P256PublicKeyHash.decodeFromBytes(p256PublicKeyHash.second, encodedBytesCoder))
        assertEquals(p256PublicKeyHash.first, P256PublicKeyHash.decodeConsumingFromBytes(p256PublicKeyHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(p256SecretKey.first, encodedBytesCoder.decode(p256SecretKey.first.kind.base58Bytes + p256SecretKey.second))
        assertEquals(p256SecretKey.first, encodedBytesCoder.decodeConsuming((p256SecretKey.first.kind.base58Bytes + p256SecretKey.second).toMutableList()))
        assertEquals(p256SecretKey.first, encodedBytesCoder.decode(p256SecretKey.second, p256SecretKey.first.kind))
        assertEquals(p256SecretKey.first, encodedBytesCoder.decodeConsuming(p256SecretKey.second.toMutableList(), p256SecretKey.first.kind))
        assertEquals(p256SecretKey.first, P256SecretKey.decodeFromBytes(p256SecretKey.second, encodedBytesCoder))
        assertEquals(p256SecretKey.first, P256SecretKey.decodeConsumingFromBytes(p256SecretKey.second.toMutableList(), encodedBytesCoder))

        assertEquals(p256Signature.first, encodedBytesCoder.decode(p256Signature.first.kind.base58Bytes + p256Signature.second))
        assertEquals(p256Signature.first, encodedBytesCoder.decodeConsuming((p256Signature.first.kind.base58Bytes + p256Signature.second).toMutableList()))
        assertEquals(p256Signature.first, encodedBytesCoder.decode(p256Signature.second, p256Signature.first.kind))
        assertEquals(p256Signature.first, encodedBytesCoder.decodeConsuming(p256Signature.second.toMutableList(), p256Signature.first.kind))
        assertEquals(p256Signature.first, P256Signature.decodeFromBytes(p256Signature.second, encodedBytesCoder))
        assertEquals(p256Signature.first, P256Signature.decodeConsumingFromBytes(p256Signature.second.toMutableList(), encodedBytesCoder))

        assertEquals(protocolHash.first, encodedBytesCoder.decode(protocolHash.first.kind.base58Bytes + protocolHash.second))
        assertEquals(protocolHash.first, encodedBytesCoder.decodeConsuming((protocolHash.first.kind.base58Bytes + protocolHash.second).toMutableList()))
        assertEquals(protocolHash.first, encodedBytesCoder.decode(protocolHash.second, protocolHash.first.kind))
        assertEquals(protocolHash.first, encodedBytesCoder.decodeConsuming(protocolHash.second.toMutableList(), protocolHash.first.kind))
        assertEquals(protocolHash.first, ProtocolHash.decodeFromBytes(protocolHash.second, encodedBytesCoder))
        assertEquals(protocolHash.first, ProtocolHash.decodeConsumingFromBytes(protocolHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(saplingAddress.first, encodedBytesCoder.decode(saplingAddress.first.kind.base58Bytes + saplingAddress.second))
        assertEquals(saplingAddress.first, encodedBytesCoder.decodeConsuming((saplingAddress.first.kind.base58Bytes + saplingAddress.second).toMutableList()))
        assertEquals(saplingAddress.first, encodedBytesCoder.decode(saplingAddress.second, saplingAddress.first.kind))
        assertEquals(saplingAddress.first, encodedBytesCoder.decodeConsuming(saplingAddress.second.toMutableList(), saplingAddress.first.kind))
        assertEquals(saplingAddress.first, SaplingAddress.decodeFromBytes(saplingAddress.second, encodedBytesCoder))
        assertEquals(saplingAddress.first, SaplingAddress.decodeConsumingFromBytes(saplingAddress.second.toMutableList(), encodedBytesCoder))

        assertEquals(saplingSpendingKey.first, encodedBytesCoder.decode(saplingSpendingKey.first.kind.base58Bytes + saplingSpendingKey.second))
        assertEquals(saplingSpendingKey.first, encodedBytesCoder.decodeConsuming((saplingSpendingKey.first.kind.base58Bytes + saplingSpendingKey.second).toMutableList()))
        assertEquals(saplingSpendingKey.first, encodedBytesCoder.decode(saplingSpendingKey.second, saplingSpendingKey.first.kind))
        assertEquals(saplingSpendingKey.first, encodedBytesCoder.decodeConsuming(saplingSpendingKey.second.toMutableList(), saplingSpendingKey.first.kind))
        assertEquals(saplingSpendingKey.first, SaplingSpendingKey.decodeFromBytes(saplingSpendingKey.second, encodedBytesCoder))
        assertEquals(saplingSpendingKey.first, SaplingSpendingKey.decodeConsumingFromBytes(saplingSpendingKey.second.toMutableList(), encodedBytesCoder))

        assertEquals(scriptExprHash.first, encodedBytesCoder.decode(scriptExprHash.first.kind.base58Bytes + scriptExprHash.second))
        assertEquals(scriptExprHash.first, encodedBytesCoder.decodeConsuming((scriptExprHash.first.kind.base58Bytes + scriptExprHash.second).toMutableList()))
        assertEquals(scriptExprHash.first, encodedBytesCoder.decode(scriptExprHash.second, scriptExprHash.first.kind))
        assertEquals(scriptExprHash.first, encodedBytesCoder.decodeConsuming(scriptExprHash.second.toMutableList(), scriptExprHash.first.kind))
        assertEquals(scriptExprHash.first, ScriptExprHash.decodeFromBytes(scriptExprHash.second, encodedBytesCoder))
        assertEquals(scriptExprHash.first, ScriptExprHash.decodeConsumingFromBytes(scriptExprHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(secp256K1Element.first, encodedBytesCoder.decode(secp256K1Element.first.kind.base58Bytes + secp256K1Element.second))
        assertEquals(secp256K1Element.first, encodedBytesCoder.decodeConsuming((secp256K1Element.first.kind.base58Bytes + secp256K1Element.second).toMutableList()))
        assertEquals(secp256K1Element.first, encodedBytesCoder.decode(secp256K1Element.second, secp256K1Element.first.kind))
        assertEquals(secp256K1Element.first, encodedBytesCoder.decodeConsuming(secp256K1Element.second.toMutableList(), secp256K1Element.first.kind))
        assertEquals(secp256K1Element.first, Secp256K1Element.decodeFromBytes(secp256K1Element.second, encodedBytesCoder))
        assertEquals(secp256K1Element.first, Secp256K1Element.decodeConsumingFromBytes(secp256K1Element.second.toMutableList(), encodedBytesCoder))

        assertEquals(secp256K1EncryptedScalar.first, encodedBytesCoder.decode(secp256K1EncryptedScalar.first.kind.base58Bytes + secp256K1EncryptedScalar.second))
        assertEquals(secp256K1EncryptedScalar.first, encodedBytesCoder.decodeConsuming((secp256K1EncryptedScalar.first.kind.base58Bytes + secp256K1EncryptedScalar.second).toMutableList()))
        assertEquals(secp256K1EncryptedScalar.first, encodedBytesCoder.decode(secp256K1EncryptedScalar.second, secp256K1EncryptedScalar.first.kind))
        assertEquals(secp256K1EncryptedScalar.first, encodedBytesCoder.decodeConsuming(secp256K1EncryptedScalar.second.toMutableList(), secp256K1EncryptedScalar.first.kind))
        assertEquals(secp256K1EncryptedScalar.first, Secp256K1EncryptedScalar.decodeFromBytes(secp256K1EncryptedScalar.second, encodedBytesCoder))
        assertEquals(secp256K1EncryptedScalar.first, Secp256K1EncryptedScalar.decodeConsumingFromBytes(secp256K1EncryptedScalar.second.toMutableList(), encodedBytesCoder))

        assertEquals(secp256K1EncryptedSecretKey.first, encodedBytesCoder.decode(secp256K1EncryptedSecretKey.first.kind.base58Bytes + secp256K1EncryptedSecretKey.second))
        assertEquals(secp256K1EncryptedSecretKey.first, encodedBytesCoder.decodeConsuming((secp256K1EncryptedSecretKey.first.kind.base58Bytes + secp256K1EncryptedSecretKey.second).toMutableList()))
        assertEquals(secp256K1EncryptedSecretKey.first, encodedBytesCoder.decode(secp256K1EncryptedSecretKey.second, secp256K1EncryptedSecretKey.first.kind))
        assertEquals(secp256K1EncryptedSecretKey.first, encodedBytesCoder.decodeConsuming(secp256K1EncryptedSecretKey.second.toMutableList(), secp256K1EncryptedSecretKey.first.kind))
        assertEquals(secp256K1EncryptedSecretKey.first, Secp256K1EncryptedSecretKey.decodeFromBytes(secp256K1EncryptedSecretKey.second, encodedBytesCoder))
        assertEquals(secp256K1EncryptedSecretKey.first, Secp256K1EncryptedSecretKey.decodeConsumingFromBytes(secp256K1EncryptedSecretKey.second.toMutableList(), encodedBytesCoder))

        assertEquals(secp256K1PublicKey.first, encodedBytesCoder.decode(secp256K1PublicKey.first.kind.base58Bytes + secp256K1PublicKey.second))
        assertEquals(secp256K1PublicKey.first, encodedBytesCoder.decodeConsuming((secp256K1PublicKey.first.kind.base58Bytes + secp256K1PublicKey.second).toMutableList()))
        assertEquals(secp256K1PublicKey.first, encodedBytesCoder.decode(secp256K1PublicKey.second, secp256K1PublicKey.first.kind))
        assertEquals(secp256K1PublicKey.first, encodedBytesCoder.decodeConsuming(secp256K1PublicKey.second.toMutableList(), secp256K1PublicKey.first.kind))
        assertEquals(secp256K1PublicKey.first, Secp256K1PublicKey.decodeFromBytes(secp256K1PublicKey.second, encodedBytesCoder))
        assertEquals(secp256K1PublicKey.first, Secp256K1PublicKey.decodeConsumingFromBytes(secp256K1PublicKey.second.toMutableList(), encodedBytesCoder))

        assertEquals(secp256K1PublicKeyHash.first, encodedBytesCoder.decode(secp256K1PublicKeyHash.first.kind.base58Bytes + secp256K1PublicKeyHash.second))
        assertEquals(secp256K1PublicKeyHash.first, encodedBytesCoder.decodeConsuming((secp256K1PublicKeyHash.first.kind.base58Bytes + secp256K1PublicKeyHash.second).toMutableList()))
        assertEquals(secp256K1PublicKeyHash.first, encodedBytesCoder.decode(secp256K1PublicKeyHash.second, secp256K1PublicKeyHash.first.kind))
        assertEquals(secp256K1PublicKeyHash.first, encodedBytesCoder.decodeConsuming(secp256K1PublicKeyHash.second.toMutableList(), secp256K1PublicKeyHash.first.kind))
        assertEquals(secp256K1PublicKeyHash.first, Secp256K1PublicKeyHash.decodeFromBytes(secp256K1PublicKeyHash.second, encodedBytesCoder))
        assertEquals(secp256K1PublicKeyHash.first, Secp256K1PublicKeyHash.decodeConsumingFromBytes(secp256K1PublicKeyHash.second.toMutableList(), encodedBytesCoder))

        assertEquals(secp256K1Scalar.first, encodedBytesCoder.decode(secp256K1Scalar.first.kind.base58Bytes + secp256K1Scalar.second))
        assertEquals(secp256K1Scalar.first, encodedBytesCoder.decodeConsuming((secp256K1Scalar.first.kind.base58Bytes + secp256K1Scalar.second).toMutableList()))
        assertEquals(secp256K1Scalar.first, encodedBytesCoder.decode(secp256K1Scalar.second, secp256K1Scalar.first.kind))
        assertEquals(secp256K1Scalar.first, encodedBytesCoder.decodeConsuming(secp256K1Scalar.second.toMutableList(), secp256K1Scalar.first.kind))
        assertEquals(secp256K1Scalar.first, Secp256K1Scalar.decodeFromBytes(secp256K1Scalar.second, encodedBytesCoder))
        assertEquals(secp256K1Scalar.first, Secp256K1Scalar.decodeConsumingFromBytes(secp256K1Scalar.second.toMutableList(), encodedBytesCoder))

        assertEquals(secp256K1SecretKey.first, encodedBytesCoder.decode(secp256K1SecretKey.first.kind.base58Bytes + secp256K1SecretKey.second))
        assertEquals(secp256K1SecretKey.first, encodedBytesCoder.decodeConsuming((secp256K1SecretKey.first.kind.base58Bytes + secp256K1SecretKey.second).toMutableList()))
        assertEquals(secp256K1SecretKey.first, encodedBytesCoder.decode(secp256K1SecretKey.second, secp256K1SecretKey.first.kind))
        assertEquals(secp256K1SecretKey.first, encodedBytesCoder.decodeConsuming(secp256K1SecretKey.second.toMutableList(), secp256K1SecretKey.first.kind))
        assertEquals(secp256K1SecretKey.first, Secp256K1SecretKey.decodeFromBytes(secp256K1SecretKey.second, encodedBytesCoder))
        assertEquals(secp256K1SecretKey.first, Secp256K1SecretKey.decodeConsumingFromBytes(secp256K1SecretKey.second.toMutableList(), encodedBytesCoder))

        assertEquals(secp256K1Signature.first, encodedBytesCoder.decode(secp256K1Signature.first.kind.base58Bytes + secp256K1Signature.second))
        assertEquals(secp256K1Signature.first, encodedBytesCoder.decodeConsuming((secp256K1Signature.first.kind.base58Bytes + secp256K1Signature.second).toMutableList()))
        assertEquals(secp256K1Signature.first, encodedBytesCoder.decode(secp256K1Signature.second, secp256K1Signature.first.kind))
        assertEquals(secp256K1Signature.first, encodedBytesCoder.decodeConsuming(secp256K1Signature.second.toMutableList(), secp256K1Signature.first.kind))
        assertEquals(secp256K1Signature.first, Secp256K1Signature.decodeFromBytes(secp256K1Signature.second, encodedBytesCoder))
        assertEquals(secp256K1Signature.first, Secp256K1Signature.decodeConsumingFromBytes(secp256K1Signature.second.toMutableList(), encodedBytesCoder))
    }

    @Test
    fun `should fail to decode Encoded from invalid bytes`() {
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(blockHash.second.sliceArray(1 until blockHash.second.size), blockHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(blockHash.second.sliceArray(1 until blockHash.second.size).toMutableList(), blockHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            BlockHash.decodeFromBytes(blockHash.second.sliceArray(1 until blockHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            BlockHash.decodeConsumingFromBytes(blockHash.second.sliceArray(1 until blockHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(blockPayloadHash.second.sliceArray(1 until blockPayloadHash.second.size), blockPayloadHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(blockPayloadHash.second.sliceArray(1 until blockPayloadHash.second.size).toMutableList(), blockPayloadHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            BlockPayloadHash.decodeFromBytes(blockPayloadHash.second.sliceArray(1 until blockPayloadHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            BlockPayloadHash.decodeConsumingFromBytes(blockPayloadHash.second.sliceArray(1 until blockPayloadHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(blockMetadataHash.second.sliceArray(1 until blockMetadataHash.second.size), blockMetadataHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(blockMetadataHash.second.sliceArray(1 until blockMetadataHash.second.size).toMutableList(), blockMetadataHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            BlockMetadataHash.decodeFromBytes(blockMetadataHash.second.sliceArray(1 until blockMetadataHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            BlockMetadataHash.decodeConsumingFromBytes(blockMetadataHash.second.sliceArray(1 until blockMetadataHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(chainId.second.sliceArray(1 until chainId.second.size), chainId.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(chainId.second.sliceArray(1 until chainId.second.size).toMutableList(), chainId.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            ChainId.decodeFromBytes(chainId.second.sliceArray(1 until chainId.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            ChainId.decodeConsumingFromBytes(chainId.second.sliceArray(1 until chainId.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(contextHash.second.sliceArray(1 until contextHash.second.size), contextHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(contextHash.second.sliceArray(1 until contextHash.second.size).toMutableList(), contextHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            ContextHash.decodeFromBytes(contextHash.second.sliceArray(1 until contextHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            ContextHash.decodeConsumingFromBytes(contextHash.second.sliceArray(1 until contextHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(contractHash.second.sliceArray(1 until contractHash.second.size), contractHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(contractHash.second.sliceArray(1 until contractHash.second.size).toMutableList(), contractHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            ContractHash.decodeFromBytes(contractHash.second.sliceArray(1 until contractHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            ContractHash.decodeConsumingFromBytes(contractHash.second.sliceArray(1 until contractHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(cryptoboxPublicKeyHash.second.sliceArray(1 until cryptoboxPublicKeyHash.second.size), cryptoboxPublicKeyHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(cryptoboxPublicKeyHash.second.sliceArray(1 until cryptoboxPublicKeyHash.second.size).toMutableList(), cryptoboxPublicKeyHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            CryptoboxPublicKeyHash.decodeFromBytes(cryptoboxPublicKeyHash.second.sliceArray(1 until cryptoboxPublicKeyHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            CryptoboxPublicKeyHash.decodeConsumingFromBytes(cryptoboxPublicKeyHash.second.sliceArray(1 until cryptoboxPublicKeyHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(ed25519BlindedPublicKeyHash.second.sliceArray(1 until ed25519BlindedPublicKeyHash.second.size), ed25519BlindedPublicKeyHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(ed25519BlindedPublicKeyHash.second.sliceArray(1 until ed25519BlindedPublicKeyHash.second.size).toMutableList(), ed25519BlindedPublicKeyHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519EncryptedSeed.decodeFromBytes(ed25519BlindedPublicKeyHash.second.sliceArray(1 until ed25519BlindedPublicKeyHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519EncryptedSeed.decodeConsumingFromBytes(ed25519BlindedPublicKeyHash.second.sliceArray(1 until ed25519BlindedPublicKeyHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(ed25519EncryptedSeed.second.sliceArray(1 until ed25519EncryptedSeed.second.size), ed25519EncryptedSeed.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(ed25519EncryptedSeed.second.sliceArray(1 until ed25519EncryptedSeed.second.size).toMutableList(), ed25519EncryptedSeed.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519EncryptedSeed.decodeFromBytes(ed25519EncryptedSeed.second.sliceArray(1 until ed25519EncryptedSeed.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519EncryptedSeed.decodeConsumingFromBytes(ed25519EncryptedSeed.second.sliceArray(1 until ed25519EncryptedSeed.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(ed25519PublicKey.second.sliceArray(1 until ed25519PublicKey.second.size), ed25519PublicKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(ed25519PublicKey.second.sliceArray(1 until ed25519PublicKey.second.size).toMutableList(), ed25519PublicKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519PublicKey.decodeFromBytes(ed25519PublicKey.second.sliceArray(1 until ed25519PublicKey.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519PublicKey.decodeConsumingFromBytes(ed25519PublicKey.second.sliceArray(1 until ed25519PublicKey.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(ed25519PublicKeyHash.second.sliceArray(1 until ed25519PublicKeyHash.second.size), ed25519PublicKeyHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(ed25519PublicKeyHash.second.sliceArray(1 until ed25519PublicKeyHash.second.size).toMutableList(), ed25519PublicKeyHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519PublicKeyHash.decodeFromBytes(ed25519PublicKeyHash.second.sliceArray(1 until ed25519PublicKeyHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519PublicKeyHash.decodeConsumingFromBytes(ed25519PublicKeyHash.second.sliceArray(1 until ed25519PublicKeyHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(ed25519SecretKey.second.sliceArray(1 until ed25519SecretKey.second.size), ed25519SecretKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(ed25519SecretKey.second.sliceArray(1 until ed25519SecretKey.second.size).toMutableList(), ed25519SecretKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519SecretKey.decodeFromBytes(ed25519SecretKey.second.sliceArray(1 until ed25519SecretKey.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519SecretKey.decodeConsumingFromBytes(ed25519SecretKey.second.sliceArray(1 until ed25519SecretKey.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(ed25519Seed.second.sliceArray(1 until ed25519Seed.second.size), ed25519Seed.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(ed25519Seed.second.sliceArray(1 until ed25519Seed.second.size).toMutableList(), ed25519Seed.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519Seed.decodeFromBytes(ed25519Seed.second.sliceArray(1 until ed25519Seed.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519Seed.decodeConsumingFromBytes(ed25519Seed.second.sliceArray(1 until ed25519Seed.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(ed25519Signature.second.sliceArray(1 until ed25519Signature.second.size), ed25519Signature.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(ed25519Signature.second.sliceArray(1 until ed25519Signature.second.size).toMutableList(), ed25519Signature.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519Signature.decodeFromBytes(ed25519Signature.second.sliceArray(1 until ed25519Signature.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519Signature.decodeConsumingFromBytes(ed25519Signature.second.sliceArray(1 until ed25519Signature.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(genericSignature.second.sliceArray(1 until genericSignature.second.size), genericSignature.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(genericSignature.second.sliceArray(1 until genericSignature.second.size).toMutableList(), genericSignature.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            GenericSignature.decodeFromBytes(genericSignature.second.sliceArray(1 until genericSignature.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            GenericSignature.decodeConsumingFromBytes(genericSignature.second.sliceArray(1 until genericSignature.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(nonceHash.second.sliceArray(1 until nonceHash.second.size), nonceHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(nonceHash.second.sliceArray(1 until nonceHash.second.size).toMutableList(), nonceHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            NonceHash.decodeFromBytes(nonceHash.second.sliceArray(1 until nonceHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            NonceHash.decodeConsumingFromBytes(nonceHash.second.sliceArray(1 until nonceHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(operationHash.second.sliceArray(1 until operationHash.second.size), operationHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(operationHash.second.sliceArray(1 until operationHash.second.size).toMutableList(), operationHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationHash.decodeFromBytes(operationHash.second.sliceArray(1 until operationHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationHash.decodeConsumingFromBytes(operationHash.second.sliceArray(1 until operationHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(operationListHash.second.sliceArray(1 until operationListHash.second.size), operationListHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(operationListHash.second.sliceArray(1 until operationListHash.second.size).toMutableList(), operationListHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationListHash.decodeFromBytes(operationListHash.second.sliceArray(1 until operationListHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationListHash.decodeConsumingFromBytes(operationListHash.second.sliceArray(1 until operationListHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(operationListListHash.second.sliceArray(1 until operationListListHash.second.size), operationListListHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(operationListListHash.second.sliceArray(1 until operationListListHash.second.size).toMutableList(), operationListListHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationListListHash.decodeFromBytes(operationListListHash.second.sliceArray(1 until operationListListHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationListListHash.decodeConsumingFromBytes(operationListListHash.second.sliceArray(1 until operationListListHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(operationMetadataHash.second.sliceArray(1 until operationMetadataHash.second.size), operationMetadataHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(operationMetadataHash.second.sliceArray(1 until operationMetadataHash.second.size).toMutableList(), operationMetadataHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationMetadataHash.decodeFromBytes(operationMetadataHash.second.sliceArray(1 until operationMetadataHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationMetadataHash.decodeConsumingFromBytes(operationMetadataHash.second.sliceArray(1 until operationMetadataHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(operationMetadataListHash.second.sliceArray(1 until operationMetadataListHash.second.size), operationMetadataListHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(operationMetadataListHash.second.sliceArray(1 until operationMetadataListHash.second.size).toMutableList(), operationMetadataListHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationMetadataListHash.decodeFromBytes(operationMetadataListHash.second.sliceArray(1 until operationMetadataListHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationMetadataListHash.decodeConsumingFromBytes(operationMetadataListHash.second.sliceArray(1 until operationMetadataListHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(operationMetadataListListHash.second.sliceArray(1 until operationMetadataListListHash.second.size), operationMetadataListListHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(operationMetadataListListHash.second.sliceArray(1 until operationMetadataListListHash.second.size).toMutableList(), operationMetadataListListHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationMetadataListListHash.decodeFromBytes(operationMetadataListListHash.second.sliceArray(1 until operationMetadataListListHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            OperationMetadataListListHash.decodeConsumingFromBytes(operationMetadataListListHash.second.sliceArray(1 until operationMetadataListListHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(p256EncryptedSecretKey.second.sliceArray(1 until p256EncryptedSecretKey.second.size), p256EncryptedSecretKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(p256EncryptedSecretKey.second.sliceArray(1 until p256EncryptedSecretKey.second.size).toMutableList(), p256EncryptedSecretKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            P256EncryptedSecretKey.decodeFromBytes(p256EncryptedSecretKey.second.sliceArray(1 until p256EncryptedSecretKey.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            P256EncryptedSecretKey.decodeConsumingFromBytes(p256EncryptedSecretKey.second.sliceArray(1 until p256EncryptedSecretKey.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(p256PublicKey.second.sliceArray(1 until p256PublicKey.second.size), p256PublicKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(p256PublicKey.second.sliceArray(1 until p256PublicKey.second.size).toMutableList(), p256PublicKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            P256PublicKey.decodeFromBytes(p256PublicKey.second.sliceArray(1 until p256PublicKey.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            P256PublicKey.decodeConsumingFromBytes(p256PublicKey.second.sliceArray(1 until p256PublicKey.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(p256PublicKeyHash.second.sliceArray(1 until p256PublicKeyHash.second.size), p256PublicKeyHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(p256PublicKeyHash.second.sliceArray(1 until p256PublicKeyHash.second.size).toMutableList(), p256PublicKeyHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            P256PublicKeyHash.decodeFromBytes(p256PublicKeyHash.second.sliceArray(1 until p256PublicKeyHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            P256PublicKeyHash.decodeConsumingFromBytes(p256PublicKeyHash.second.sliceArray(1 until p256PublicKeyHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(p256SecretKey.second.sliceArray(1 until p256SecretKey.second.size), p256SecretKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(p256SecretKey.second.sliceArray(1 until p256SecretKey.second.size).toMutableList(), p256SecretKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            P256SecretKey.decodeFromBytes(p256SecretKey.second.sliceArray(1 until p256SecretKey.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            P256SecretKey.decodeConsumingFromBytes(p256SecretKey.second.sliceArray(1 until p256SecretKey.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(p256Signature.second.sliceArray(1 until p256Signature.second.size), p256Signature.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(p256Signature.second.sliceArray(1 until p256Signature.second.size).toMutableList(), p256Signature.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            P256Signature.decodeFromBytes(p256Signature.second.sliceArray(1 until p256Signature.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            P256Signature.decodeConsumingFromBytes(p256Signature.second.sliceArray(1 until p256Signature.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(protocolHash.second.sliceArray(1 until protocolHash.second.size), protocolHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(protocolHash.second.sliceArray(1 until protocolHash.second.size).toMutableList(), protocolHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            ProtocolHash.decodeFromBytes(protocolHash.second.sliceArray(1 until protocolHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            ProtocolHash.decodeConsumingFromBytes(protocolHash.second.sliceArray(1 until protocolHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(saplingAddress.second.sliceArray(1 until saplingAddress.second.size), saplingAddress.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(saplingAddress.second.sliceArray(1 until saplingAddress.second.size).toMutableList(), saplingAddress.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            SaplingAddress.decodeFromBytes(saplingAddress.second.sliceArray(1 until saplingAddress.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            SaplingAddress.decodeConsumingFromBytes(saplingAddress.second.sliceArray(1 until saplingAddress.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(saplingSpendingKey.second.sliceArray(1 until saplingSpendingKey.second.size), saplingSpendingKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(saplingSpendingKey.second.sliceArray(1 until saplingSpendingKey.second.size).toMutableList(), saplingSpendingKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            SaplingSpendingKey.decodeFromBytes(saplingSpendingKey.second.sliceArray(1 until saplingSpendingKey.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            SaplingSpendingKey.decodeConsumingFromBytes(saplingSpendingKey.second.sliceArray(1 until saplingSpendingKey.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(scriptExprHash.second.sliceArray(1 until scriptExprHash.second.size), scriptExprHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(scriptExprHash.second.sliceArray(1 until scriptExprHash.second.size).toMutableList(), scriptExprHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519EncryptedSeed.decodeFromBytes(scriptExprHash.second.sliceArray(1 until scriptExprHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Ed25519EncryptedSeed.decodeConsumingFromBytes(scriptExprHash.second.sliceArray(1 until scriptExprHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(secp256K1Element.second.sliceArray(1 until secp256K1Element.second.size), secp256K1Element.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(secp256K1Element.second.sliceArray(1 until secp256K1Element.second.size).toMutableList(), secp256K1Element.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1Element.decodeFromBytes(secp256K1Element.second.sliceArray(1 until secp256K1Element.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1Element.decodeConsumingFromBytes(secp256K1Element.second.sliceArray(1 until secp256K1Element.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(secp256K1EncryptedScalar.second.sliceArray(1 until secp256K1EncryptedScalar.second.size), secp256K1EncryptedScalar.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(secp256K1EncryptedScalar.second.sliceArray(1 until secp256K1EncryptedScalar.second.size).toMutableList(), secp256K1EncryptedScalar.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1EncryptedScalar.decodeFromBytes(secp256K1EncryptedScalar.second.sliceArray(1 until secp256K1EncryptedScalar.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1EncryptedScalar.decodeConsumingFromBytes(secp256K1EncryptedScalar.second.sliceArray(1 until secp256K1EncryptedScalar.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(secp256K1EncryptedSecretKey.second.sliceArray(1 until secp256K1EncryptedSecretKey.second.size), secp256K1EncryptedSecretKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(secp256K1EncryptedSecretKey.second.sliceArray(1 until secp256K1EncryptedSecretKey.second.size).toMutableList(), secp256K1EncryptedSecretKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1EncryptedSecretKey.decodeFromBytes(secp256K1EncryptedSecretKey.second.sliceArray(1 until secp256K1EncryptedSecretKey.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1EncryptedSecretKey.decodeConsumingFromBytes(secp256K1EncryptedSecretKey.second.sliceArray(1 until secp256K1EncryptedSecretKey.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(secp256K1PublicKey.second.sliceArray(1 until secp256K1PublicKey.second.size), secp256K1PublicKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(secp256K1PublicKey.second.sliceArray(1 until secp256K1PublicKey.second.size).toMutableList(), secp256K1PublicKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1PublicKey.decodeFromBytes(secp256K1PublicKey.second.sliceArray(1 until secp256K1PublicKey.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1PublicKey.decodeConsumingFromBytes(secp256K1PublicKey.second.sliceArray(1 until secp256K1PublicKey.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(secp256K1PublicKeyHash.second.sliceArray(1 until secp256K1PublicKeyHash.second.size), secp256K1PublicKeyHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(secp256K1PublicKeyHash.second.sliceArray(1 until secp256K1PublicKeyHash.second.size).toMutableList(), secp256K1PublicKeyHash.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1PublicKeyHash.decodeFromBytes(secp256K1PublicKeyHash.second.sliceArray(1 until secp256K1PublicKeyHash.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1PublicKeyHash.decodeConsumingFromBytes(secp256K1PublicKeyHash.second.sliceArray(1 until secp256K1PublicKeyHash.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(secp256K1Scalar.second.sliceArray(1 until secp256K1Scalar.second.size), secp256K1Scalar.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(secp256K1Scalar.second.sliceArray(1 until secp256K1Scalar.second.size).toMutableList(), secp256K1Scalar.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1Scalar.decodeFromBytes(secp256K1Scalar.second.sliceArray(1 until secp256K1Scalar.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1Scalar.decodeConsumingFromBytes(secp256K1Scalar.second.sliceArray(1 until secp256K1Scalar.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(secp256K1SecretKey.second.sliceArray(1 until secp256K1SecretKey.second.size), secp256K1SecretKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(secp256K1SecretKey.second.sliceArray(1 until secp256K1SecretKey.second.size).toMutableList(), secp256K1SecretKey.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1SecretKey.decodeFromBytes(secp256K1SecretKey.second.sliceArray(1 until secp256K1SecretKey.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1SecretKey.decodeConsumingFromBytes(secp256K1SecretKey.second.sliceArray(1 until secp256K1SecretKey.second.size).toMutableList(), encodedBytesCoder)
        }

        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decode(secp256K1Signature.second.sliceArray(1 until secp256K1Signature.second.size), secp256K1Signature.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            encodedBytesCoder.decodeConsuming(secp256K1Signature.second.sliceArray(1 until secp256K1Signature.second.size).toMutableList(), secp256K1Signature.first.kind)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1Signature.decodeFromBytes(secp256K1Signature.second.sliceArray(1 until secp256K1Signature.second.size), encodedBytesCoder)
        }
        assertFailsWith<IllegalArgumentException> {
            Secp256K1Signature.decodeConsumingFromBytes(secp256K1Signature.second.sliceArray(1 until secp256K1Signature.second.size).toMutableList(), encodedBytesCoder)
        }
    }

    private val blockHash: Pair<BlockHash, ByteArray>
        get() = BlockHash("BLxhnhJ8yRu5BjcK8STwLtDmSpWz2q5R5HRYir8kdexFjMVZiZ5") to "a4712e4241cd45194876e5e3637afd5eb0de95d43909ee8a0300004bb54697f4".asHexString().toByteArray()

    private val blockPayloadHash: Pair<BlockPayloadHash, ByteArray>
        get() = BlockPayloadHash("vh2rvgiNNmaBn3tW62KVqNLphShiC8X8Ao1GxC1gPfmTDz5SWWQG") to "9c39bccfb8b3fc02c94ee78f7c0d372def7a956a3e9bec3aec7906f2d07b7d90".asHexString().toByteArray()

    private val blockMetadataHash: Pair<BlockMetadataHash, ByteArray>
        get() = BlockMetadataHash("bm3LV5fYgzx7P7V9E2nuJeGKzeonnu1FbRB2Bk5SMwvWmzUcQZpc") to "64aa9d9b3acf43f64a1c975b6298cf47f5aa557c86bd8a07645f49f486f64398".asHexString().toByteArray()

    private val chainId: Pair<ChainId, ByteArray>
        get() = ChainId("NetXNy1E383jSrX") to "23a9f05a".asHexString().toByteArray()

    private val contextHash: Pair<ContextHash, ByteArray>
        get() = ContextHash("CoVd2vUg9onub8w2ooJR7PyinX7sfxYk8YWBzg77yvY2T64E4eKM") to "80cd3c83957a801c336209c2418b476906fab82de4cd199252b2d5693146c166".asHexString().toByteArray()

    private val contractHash: Pair<ContractHash, ByteArray>
        get() = ContractHash("KT1SLXd7g5YqT81PnH4R9K4hwz9kJpCwNkn2") to "c2c0cd60707bd74cb985672e45c4eedb333b5d74".asHexString().toByteArray()

    private val cryptoboxPublicKeyHash: Pair<CryptoboxPublicKeyHash, ByteArray>
        get() = CryptoboxPublicKeyHash("idtUTwDSLeHFCKR1JnCPSj68HXnLT5") to "daceb7e6e75e421edad81355b5f29c49".asHexString().toByteArray()

    private val ed25519BlindedPublicKeyHash: Pair<Ed25519BlindedPublicKeyHash, ByteArray>
        get() = Ed25519BlindedPublicKeyHash("btz1UNpWMmyMW9AK57KKSAfpeX8rLZhkY7Cp3") to "585bacaceb7e6dca94941d372992e8b576872aea".asHexString().toByteArray()

    private val ed25519EncryptedSeed: Pair<Ed25519EncryptedSeed, ByteArray>
        get() = Ed25519EncryptedSeed("edesk1NB3aVLiqAeWz37PeEamUce1ms21SDh4BL8meLkKwHibhDkW7QWNzZCHAfexwCoAtMZofdzHiectcbNrrj9") to "22d4dd44b2671a05cf265b7a81f3d94abe7dc2aa508d1c29bf2fe0728d90a09c6208d97d81359565517bf2614c95acd8d4ccb219ea3be014".asHexString().toByteArray()

    private val ed25519PublicKey: Pair<Ed25519PublicKey, ByteArray>
        get() = Ed25519PublicKey("edpkuhNbSQLppKWQkihBb1awHP85Z7iSTUBMjC4ZRtSkqSDHYUP7ty") to "8ad8ce61fdc72076be473f8b3959d1fff3975e6ce3f4bbe64542ef3a20017f10".asHexString().toByteArray()

    private val ed25519PublicKeyHash: Pair<Ed25519PublicKeyHash, ByteArray>
        get() = Ed25519PublicKeyHash("tz1RQyvRnGZUiUVVcJiiL7BwaNUxYaK9Gq99") to "3f5908d325f3d4107a09f745dc372a08bccf95b7".asHexString().toByteArray()

    private val ed25519SecretKey: Pair<Ed25519SecretKey, ByteArray>
        get() = Ed25519SecretKey("edskRwd9D8hzH6S9dBxrWg72EdmCZ16uSHLUsqqzzuYujgqUGdJKWfxSKgaWdyvvqdwbK9nxyJ4CNL3RnCjEgXG2abJeqHdNUy") to "95e395d4c312e2f6a30e8bd691cf306d812476fd26068d428142321ea3a81a50047323c689287e9f02db8518c660c41effe068784a270512365c243d9cbb77e1".asHexString().toByteArray()

    private val ed25519Seed: Pair<Ed25519Seed, ByteArray>
        get() = Ed25519Seed("edsk3DLLzqVRjzPwCAptTETpT5JZapg81FH8YbFKL4dYRMaQpFo3Mu") to "475edf55f34f1e4b8664fd3fa218122c2a02da1fe746e7829e09d1bc9dc83499".asHexString().toByteArray()

    private val ed25519Signature: Pair<Ed25519Signature, ByteArray>
        get() = Ed25519Signature("edsigu5CFQXdbDySf2ZVSGBXAW2cC2WtG7e9PU9M3ZjhdpPGQPWgsQxz7ZyziWJ6nPt9fLQdcaZB2ZEFWU9nY9xtQU8nGcNY1Tz") to "efea37c304317102ff02c82705e229ba601e5c141fcbfc063b52ea2ddfb367a3c63a564a97237d7e7504be043d45963ef9c1c724b666bcc1b0a59de1c2a72193".asHexString().toByteArray()
    
    private val genericSignature: Pair<GenericSignature, ByteArray>
        get() = GenericSignature("sigVUsPZfih9CzgbD52wGbjGaCWeRkAipf7QwZAwtNZCT65rmHN1au2LVv5JUzffK8mnzaedVfk2jyMa775GjvcxGfknRZB2") to "39455b2e136efda425692cd38a49279d6597dd9db636723af217c9cae79aacd45e0fdff633611cbdb51bf034282d6e16a1e8a81747808267c70361932d44bc71".asHexString().toByteArray()
    
    private val nonceHash: Pair<NonceHash, ByteArray>
        get() = NonceHash("nceVMmASYh4FF5zYPnHeoYsRcxG5pt1MkmAuKjTg9DcbQvLDiHeEp") to "956f922f940dc6796b80719ac54d5c96e8c6824065a792a8a861e45c205d7cdb".asHexString().toByteArray()

    private val operationHash: Pair<OperationHash, ByteArray>
        get() = OperationHash("ooqe8AAhnrPu9M9CZeDCjg6EDww3Y5zQGUvsujEN7dikvvprsXz") to "9da5593331db4e8f496ae7588cb76404a16e77be46a0cfbd0a59749771613b9d".asHexString().toByteArray()

    private val operationListHash: Pair<OperationListHash, ByteArray>
        get() = OperationListHash("LoxE21kHxDzUHqS3ZL16UK7DCCuxykB3F8qoVqUBVQmZbHnYxSVs") to "bda74d5a706ca60449fadafcc5e2689576924e3c7aa2cf8df58ec2adc8830da1".asHexString().toByteArray()

    private val operationListListHash: Pair<OperationListListHash, ByteArray>
        get() = OperationListListHash("LLoZidphsx8dm8Eg8H2yAw8NZ4TTGU7jLd3DzpGPXXqdgrGaiux2u") to "340f6f139868a69cdf189072acb485ecb710e5dca94c79df6a7f0facd5e1b398".asHexString().toByteArray()

    private val operationMetadataHash: Pair<OperationMetadataHash, ByteArray>
        get() = OperationMetadataHash("r4msXPFKwuDaQzGDz2pD3kSAjALi7LuNambGqMkxgtDuZmbaFVQ") to "f946c89e94f8096064f56c12b06625b95fee240f39b895b8436b48ce37dce51f".asHexString().toByteArray()

    private val operationMetadataListHash: Pair<OperationMetadataListHash, ByteArray>
        get() = OperationMetadataListHash("Lr2ktwv16TRusAF6KaoEW3r8Wh5M5UXjhvZmHzCvKd1Uj48tXwAF") to "bf4c31452ac6d183b8f34cc1af6595cd1ee11f94c6b18d49452db01c568a6a45".asHexString().toByteArray()

    private val operationMetadataListListHash: Pair<OperationMetadataListListHash, ByteArray>
        get() = OperationMetadataListListHash("LLr2xFh9AdU2jMjG54jHeVsSUzWqXMqTMCysR5Jp95GprPbBm7gJA") to "e386359b2374a4eadd4d35617671a5fe0da38e9d01b43c0938a895a4a5ed03f9".asHexString().toByteArray()

    private val p256EncryptedSecretKey: Pair<P256EncryptedSecretKey, ByteArray>
        get() = P256EncryptedSecretKey("p2esk21af5ekBhFu5HTUKxCmcspPb22JdBtmewiWrx9vSAreCSifUj4fkVQ1ucqidGR2cYUU2Je9Nq718qXebC6u") to "544cad98a610235587dcc6f3e8a3ce5939ceb13b3350559361ba1cf58614a80698a1210f1b039e943fbb5a0803140b7732d31faa40abf2ec".asHexString().toByteArray()

    private val p256PublicKey: Pair<P256PublicKey, ByteArray>
        get() = P256PublicKey("p2pk6oXJn3Da7CWkBauEP6zDFnJee5RbSKd9ajNDcrJUu6DVY41UsA7") to "181872df40aa53eb52a1a353ee9de50d0f987758911f666100d61d08bc655810e2".asHexString().toByteArray()

    private val p256PublicKeyHash: Pair<P256PublicKeyHash, ByteArray>
        get() = P256PublicKeyHash("tz3YAMXU4qbPHVCR1hx2Q1qQkqppbjMpXL4z") to "81cc4cf88857d7a9ce867e361647639bbbf062c0".asHexString().toByteArray()

    private val p256SecretKey: Pair<P256SecretKey, ByteArray>
        get() = P256SecretKey("p2sk3hW82MtpmcJtziQqwyaMs48Ray9RuieSKgu7ePm9VXAHLnhgzB") to "b2b9db2841a31995522cb6010ba097302897a12c104beab1d02ce7c537c4ac04".asHexString().toByteArray()

    private val p256Signature: Pair<P256Signature, ByteArray>
        get() = P256Signature("p2sigYCKtMeoHN7UjSrr75D7va7GZjUqJxMiZPL5tJdXjn8RPVspt8bZ4R8jwx8EhAMJ31zikGu4cLw19xa7ofqvLT82ANBC8o") to "534388e4213a518591622321aac51ff7ca36071c0b7872b5f5f2932862fdd6a6c597c329204d26531b0c4e48ac2bc53383134d049332ccab761875539cee6580".asHexString().toByteArray()

    private val protocolHash: Pair<ProtocolHash, ByteArray>
        get() = ProtocolHash("Pt2qYBgju8RgUx1gXu1VFAi3FjzCdgMkdkLRwm77gdwAbxbaWpk") to "ace3a899b148831c682105949cdea6cb4c033b3fc152883c7091ef8391dea87a".asHexString().toByteArray()

    private val saplingAddress: Pair<SaplingAddress, ByteArray>
        get() = SaplingAddress("zet12WGVjXxsaWaLJVjyhCp8Pt4oew3noTbS55znq9pwVScY2UFCfq5xbbDoB8ByBtyxP") to "0cf5167bb9fe405489e0a9af41ddb213556d62109f6e069a9e54e50018015ab56c7ae197076daa551393f1".asHexString().toByteArray()

    private val saplingSpendingKey: Pair<SaplingSpendingKey, ByteArray>
        get() = SaplingSpendingKey("sask453Y2atFNQUwtPjN5M7Bzddnz29Q2tm2K6NWz2yPMTCGek9nVrUHK4GjH4F4bbvt5z5GpEuHmU4HoF9XrJsMGA8G6ogkhezKiFTcpTVBgkP6YoAr3rq7HgsFWGmaFTZkrUGB9u1rpRcCzK2ED2NGBxXnRJPEVqFSEjdKeGVnxyQ65DLNBwTA1CnfcCF3gAVMJnxEWPGwZCRZvRSx7DgoXDEBHz13tK9cK26Fwaod3Fjay") to "af453ddd281f2bf058c9b18d4a774bd3b98d51c1b2c02412deb526798f3d198510e4ef7e6c16cdc31ce3a8cbb21e643d3cfb356af15b7cd1b1eb9d861dd71f55e91a7baf9e081708bdf74245c807a2411d4dfb6aed8d5f31e940a750efedd2273449d5351c74c690b10ebf2ca13429500c44315ef9af69df029005423850b6e7e9201e35c2dace8a753f8ff6898757d92fc7e79cceca737caab16838d256f04c94012769ece782f8b6".asHexString().toByteArray()

    private val scriptExprHash: Pair<ScriptExprHash, ByteArray>
        get() = ScriptExprHash("exprvB6vfUhdorBq2p6HB6L36TkkwBEfEAwRmWxfuKXFtSbE5AWCxB") to "da7d5ea272aeafc6cf403ccca11a3d59524480ec4842bf27d8722f34f5759d6b".asHexString().toByteArray()

    private val secp256K1Element: Pair<Secp256K1Element, ByteArray>
        get() = Secp256K1Element("GSpE16ugxznfKSd2ckNWbUNCdXfUYauPPfYyRRugcg71LgFBkNKAv6") to "a99a2d68d47c292ade55906ecece24e17af3451a957a5f0bb1f9ff86e7d9357988".asHexString().toByteArray()

    private val secp256K1EncryptedScalar: Pair<Secp256K1EncryptedScalar, ByteArray>
        get() = Secp256K1EncryptedScalar("seesk8Fm9iE86ti3wJXSbCouKQn6cCP7WF3XUxgaVNue1rf9neZ2mG2NbC5ovihUzRqGGnY2Dr8yHP8koo3ELWHRad5Fx") to "ae64e486a6b1a594eed395112540771c419c4f8c748402b6c530f54fa602c325e07a041503dcf7611e35bde59415617265e9b243ceb0b6b512b83346".asHexString().toByteArray()

    private val secp256K1EncryptedSecretKey: Pair<Secp256K1EncryptedSecretKey, ByteArray>
        get() = Secp256K1EncryptedSecretKey("spesk1njTvUaKspW9sYcP942BwdEQkbbUdqHZuZSHirCdKGzhwsoXYmf8KhwaJeq6f55msU22JZsiKEASWnKLkgB") to "760186ea7117c2a74315582145260e5831fb0998e41c4bf1b36c2dc97d5f353df40489b0c0a104d8e2ca44c55c998a41a361c0f4793b1c72".asHexString().toByteArray()

    private val secp256K1PublicKey: Pair<Secp256K1PublicKey, ByteArray>
        get() = Secp256K1PublicKey("sppkE7zASd9wbU7WbBT37nCcMAQgfhBDsW6cEZYGz7huH78zEceGERh") to "c5d6449779370187578fd775dc3e55cbaa300426c7d258c76ce665bcb98da66e7c".asHexString().toByteArray()

    private val secp256K1PublicKeyHash: Pair<Secp256K1PublicKeyHash, ByteArray>
        get() = Secp256K1PublicKeyHash("tz2SyKS6VjvfEmmRayzYaKSNBiVUAV3wzYU7") to "cca60dcb06a41c1fa052c74fe22233cfd9a4b57c".asHexString().toByteArray()

    private val secp256K1Scalar: Pair<Secp256K1Scalar, ByteArray>
        get() = Secp256K1Scalar("SSp3BwHVZNR8323JnUBeHfRdnzXBHsLsq8omNGL5o5if26KU6rZik") to "c321155281c8298fc6700533ec10aad996a186f4e0c1b7d0c3e02c4d9ea4554a".asHexString().toByteArray()

    private val secp256K1SecretKey: Pair<Secp256K1SecretKey, ByteArray>
        get() = Secp256K1SecretKey("spsk3JQutvdG5STEELfJRrC8wmh7EEndU1Jnu9TfATnMUtVFgHXAba") to "f72790ef43bcba94e51ea2c6546d9a60d8a1fdd6ec47d94c73108705ac8a7fb9".asHexString().toByteArray()

    private val secp256K1Signature: Pair<Secp256K1Signature, ByteArray>
        get() = Secp256K1Signature("spsig1Xv7ujqGPuD4GAGoKm5M7xcmw1FcPVFKVfbAXCWDCXuYVHZdh6Wdz5p9ba31qxQF6WRmYrGgTGZ97G9QLxtZrCDEKPE3Eq") to "c78b2861c35f15d266d2450b3b783615b63cfc01f49aa25c8ca4d06f495f7e9e1931eb6b0294328707ed79834fcc14a7e1944e36d9958e5b2f036eb6cb7150dd".asHexString().toByteArray()

}