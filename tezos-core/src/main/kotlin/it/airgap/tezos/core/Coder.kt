package it.airgap.tezos.core

import it.airgap.tezos.core.internal.coder.*
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.zarith.ZarithInteger
import it.airgap.tezos.core.type.zarith.ZarithNatural

// -- BlockHash <-> ByteArray --

public fun BlockHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun BlockHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): BlockHash = encodedBytesCoder.decode(bytes, BlockHash)

public fun BlockHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): BlockHash = encodedBytesCoder.decodeConsuming(bytes, BlockHash)

// -- BlockMetadataHash <-> ByteArray --

public fun BlockMetadataHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun BlockMetadataHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): BlockMetadataHash = encodedBytesCoder.decode(bytes, BlockMetadataHash)

public fun BlockMetadataHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): BlockMetadataHash = encodedBytesCoder.decodeConsuming(bytes, BlockMetadataHash)

// -- BlockPayloadHash <-> ByteArray --

public fun BlockPayloadHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun BlockPayloadHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): BlockPayloadHash = encodedBytesCoder.decode(bytes, BlockPayloadHash)

public fun BlockPayloadHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): BlockPayloadHash = encodedBytesCoder.decodeConsuming(bytes, BlockPayloadHash)

// -- ChainId <-> ByteArray --

public fun ChainId.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun ChainId.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ChainId = encodedBytesCoder.decode(bytes, ChainId)

public fun ChainId.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ChainId = encodedBytesCoder.decodeConsuming(bytes, ChainId)

// -- ContextHash <-> ByteArray --

public fun ContextHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun ContextHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ContextHash = encodedBytesCoder.decode(bytes, ContextHash)

public fun ContextHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ContextHash = encodedBytesCoder.decodeConsuming(bytes, ContextHash)

// -- ContractHash <-> ByteArray --

public fun ContractHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun ContractHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ContractHash = encodedBytesCoder.decode(bytes, ContractHash)

public fun ContractHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ContractHash = encodedBytesCoder.decodeConsuming(bytes, ContractHash)

// -- CryptoboxPublicKeyHash <-> ByteArray --

public fun CryptoboxPublicKeyHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun CryptoboxPublicKeyHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): CryptoboxPublicKeyHash = encodedBytesCoder.decode(bytes, CryptoboxPublicKeyHash)

public fun CryptoboxPublicKeyHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): CryptoboxPublicKeyHash = encodedBytesCoder.decodeConsuming(bytes, CryptoboxPublicKeyHash)

// -- Ed25519EncryptedSeed <-> ByteArray --

public fun Ed25519EncryptedSeed.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Ed25519EncryptedSeed.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519EncryptedSeed = encodedBytesCoder.decode(bytes, Ed25519EncryptedSeed)

public fun Ed25519EncryptedSeed.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519EncryptedSeed = encodedBytesCoder.decodeConsuming(bytes, Ed25519EncryptedSeed)

// -- Ed25519PublicKey <-> ByteArray --

public fun Ed25519PublicKey.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Ed25519PublicKey.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519PublicKey = encodedBytesCoder.decode(bytes, Ed25519PublicKey)

public fun Ed25519PublicKey.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519PublicKey = encodedBytesCoder.decodeConsuming(bytes, Ed25519PublicKey)

// -- Ed25519PublicKeyHash <-> ByteArray --

public fun Ed25519PublicKeyHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Ed25519PublicKeyHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519PublicKeyHash = encodedBytesCoder.decode(bytes, Ed25519PublicKeyHash)

public fun Ed25519PublicKeyHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519PublicKeyHash = encodedBytesCoder.decodeConsuming(bytes, Ed25519PublicKeyHash)

// -- Ed25519SecretKey <-> ByteArray --

public fun Ed25519SecretKey.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Ed25519SecretKey.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519SecretKey = encodedBytesCoder.decode(bytes, Ed25519SecretKey)

public fun Ed25519SecretKey.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519SecretKey = encodedBytesCoder.decodeConsuming(bytes, Ed25519SecretKey)

// -- Ed25519Seed <-> ByteArray --

public fun Ed25519Seed.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Ed25519Seed.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519Seed = encodedBytesCoder.decode(bytes, Ed25519Seed)

public fun Ed25519Seed.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519Seed = encodedBytesCoder.decodeConsuming(bytes, Ed25519Seed)

// -- Ed25519Signature <-> ByteArray --

public fun Ed25519Signature.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Ed25519Signature.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519Signature = encodedBytesCoder.decode(bytes, Ed25519Signature)

public fun Ed25519Signature.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Ed25519Signature = encodedBytesCoder.decodeConsuming(bytes, Ed25519Signature)

// -- GenericSignature <-> ByteArray --

public fun GenericSignature.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun GenericSignature.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): GenericSignature = encodedBytesCoder.decode(bytes, GenericSignature)

public fun GenericSignature.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): GenericSignature = encodedBytesCoder.decodeConsuming(bytes, GenericSignature)

// -- NonceHash <-> ByteArray --

public fun NonceHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun NonceHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): NonceHash = encodedBytesCoder.decode(bytes, NonceHash)

public fun NonceHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): NonceHash = encodedBytesCoder.decodeConsuming(bytes, NonceHash)

// -- OperationHash <-> ByteArray --

public fun OperationHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun OperationHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationHash = encodedBytesCoder.decode(bytes, OperationHash)

public fun OperationHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationHash = encodedBytesCoder.decodeConsuming(bytes, OperationHash)

// -- OperationListHash <-> ByteArray --

public fun OperationListHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun OperationListHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationListHash = encodedBytesCoder.decode(bytes, OperationListHash)

public fun OperationListHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationListHash = encodedBytesCoder.decodeConsuming(bytes, OperationListHash)

// -- OperationListListHash <-> ByteArray --

public fun OperationListListHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun OperationListListHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationListListHash = encodedBytesCoder.decode(bytes, OperationListListHash)

public fun OperationListListHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationListListHash = encodedBytesCoder.decodeConsuming(bytes, OperationListListHash)

// -- OperationMetadataHash <-> ByteArray --

public fun OperationMetadataHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun OperationMetadataHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationMetadataHash = encodedBytesCoder.decode(bytes, OperationMetadataHash)

public fun OperationMetadataHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationMetadataHash = encodedBytesCoder.decodeConsuming(bytes, OperationMetadataHash)

// -- OperationMetadataListHash <-> ByteArray --

public fun OperationMetadataListHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun OperationMetadataListHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationMetadataListHash = encodedBytesCoder.decode(bytes, OperationMetadataListHash)

public fun OperationMetadataListHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationMetadataListHash = encodedBytesCoder.decodeConsuming(bytes, OperationMetadataListHash)

// -- OperationMetadataListListHash <-> ByteArray --

public fun OperationMetadataListListHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun OperationMetadataListListHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationMetadataListListHash = encodedBytesCoder.decode(bytes, OperationMetadataListListHash)

public fun OperationMetadataListListHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): OperationMetadataListListHash = encodedBytesCoder.decodeConsuming(bytes, OperationMetadataListListHash)

// -- P256EncryptedSecretKey <-> ByteArray --

public fun P256EncryptedSecretKey.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun P256EncryptedSecretKey.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): P256EncryptedSecretKey = encodedBytesCoder.decode(bytes, P256EncryptedSecretKey)

public fun P256EncryptedSecretKey.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): P256EncryptedSecretKey = encodedBytesCoder.decodeConsuming(bytes, P256EncryptedSecretKey)

// -- P256PublicKey <-> ByteArray --

public fun P256PublicKey.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun P256PublicKey.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): P256PublicKey = encodedBytesCoder.decode(bytes, P256PublicKey)

public fun P256PublicKey.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): P256PublicKey = encodedBytesCoder.decodeConsuming(bytes, P256PublicKey)

// -- P256PublicKeyHash <-> ByteArray --

public fun P256PublicKeyHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun P256PublicKeyHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): P256PublicKeyHash = encodedBytesCoder.decode(bytes, P256PublicKeyHash)

public fun P256PublicKeyHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): P256PublicKeyHash = encodedBytesCoder.decodeConsuming(bytes, P256PublicKeyHash)

// -- P256SecretKey <-> ByteArray --

public fun P256SecretKey.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun P256SecretKey.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): P256SecretKey = encodedBytesCoder.decode(bytes, P256SecretKey)

public fun P256SecretKey.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): P256SecretKey = encodedBytesCoder.decodeConsuming(bytes, P256SecretKey)

// -- P256Signature <-> ByteArray --

public fun P256Signature.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun P256Signature.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): P256Signature = encodedBytesCoder.decode(bytes, P256Signature)

public fun P256Signature.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): P256Signature = encodedBytesCoder.decodeConsuming(bytes, P256Signature)

// -- ProtocolHash <-> ByteArray --

public fun ProtocolHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun ProtocolHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ProtocolHash = encodedBytesCoder.decode(bytes, ProtocolHash)

public fun ProtocolHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ProtocolHash = encodedBytesCoder.decodeConsuming(bytes, ProtocolHash)

// -- SaplingAddress <-> ByteArray --

public fun SaplingAddress.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun SaplingAddress.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): SaplingAddress = encodedBytesCoder.decode(bytes, SaplingAddress)

public fun SaplingAddress.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): SaplingAddress = encodedBytesCoder.decodeConsuming(bytes, SaplingAddress)

// -- SaplingSpendingKey <-> ByteArray --

public fun SaplingSpendingKey.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun SaplingSpendingKey.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): SaplingSpendingKey = encodedBytesCoder.decode(bytes, SaplingSpendingKey)

public fun SaplingSpendingKey.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): SaplingSpendingKey = encodedBytesCoder.decodeConsuming(bytes, SaplingSpendingKey)

// -- Secp256K1Element <-> ByteArray --

public fun Secp256K1Element.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Secp256K1Element.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1Element = encodedBytesCoder.decode(bytes, Secp256K1Element)

public fun Secp256K1Element.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1Element = encodedBytesCoder.decodeConsuming(bytes, Secp256K1Element)

// -- Secp256K1EncryptedScalar <-> ByteArray --

public fun Secp256K1EncryptedScalar.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Secp256K1EncryptedScalar.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1EncryptedScalar = encodedBytesCoder.decode(bytes, Secp256K1EncryptedScalar)

public fun Secp256K1EncryptedScalar.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1EncryptedScalar = encodedBytesCoder.decodeConsuming(bytes, Secp256K1EncryptedScalar)

// -- Secp256K1EncryptedSecretKey <-> ByteArray --

public fun Secp256K1EncryptedSecretKey.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Secp256K1EncryptedSecretKey.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1EncryptedSecretKey = encodedBytesCoder.decode(bytes, Secp256K1EncryptedSecretKey)

public fun Secp256K1EncryptedSecretKey.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1EncryptedSecretKey = encodedBytesCoder.decodeConsuming(bytes, Secp256K1EncryptedSecretKey)

// -- Secp256K1PublicKey <-> ByteArray --

public fun Secp256K1PublicKey.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Secp256K1PublicKey.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1PublicKey = encodedBytesCoder.decode(bytes, Secp256K1PublicKey)

public fun Secp256K1PublicKey.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1PublicKey = encodedBytesCoder.decodeConsuming(bytes, Secp256K1PublicKey)

// -- Secp256K1PublicKeyHash <-> ByteArray --

public fun Secp256K1PublicKeyHash.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Secp256K1PublicKeyHash.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1PublicKeyHash = encodedBytesCoder.decode(bytes, Secp256K1PublicKeyHash)

public fun Secp256K1PublicKeyHash.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1PublicKeyHash = encodedBytesCoder.decodeConsuming(bytes, Secp256K1PublicKeyHash)

// -- Secp256K1Scalar <-> ByteArray --

public fun Secp256K1Scalar.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Secp256K1Scalar.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1Scalar = encodedBytesCoder.decode(bytes, Secp256K1Scalar)

public fun Secp256K1Scalar.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1Scalar = encodedBytesCoder.decodeConsuming(bytes, Secp256K1Scalar)

// -- Secp256K1SecretKey <-> ByteArray --

public fun Secp256K1SecretKey.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Secp256K1SecretKey.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1SecretKey = encodedBytesCoder.decode(bytes, Secp256K1SecretKey)

public fun Secp256K1SecretKey.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1SecretKey = encodedBytesCoder.decodeConsuming(bytes, Secp256K1SecretKey)

// -- Secp256K1Signature <-> ByteArray --

public fun Secp256K1Signature.encodeToBytes(
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): ByteArray = encodedBytesCoder.encode(this)

public fun Secp256K1Signature.Companion.decodeFromBytes(
    bytes: ByteArray,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1Signature = encodedBytesCoder.decode(bytes, Secp256K1Signature)

public fun Secp256K1Signature.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    encodedBytesCoder: EncodedBytesCoder = TezosSdk.instance.dependencyRegistry.core().encodedBytesCoder,
): Secp256K1Signature = encodedBytesCoder.decodeConsuming(bytes, Secp256K1Signature)

// -- Address <-> ByteArray --

public fun Address<*>.encodeToBytes(
    addressBytesCoder: AddressBytesCoder = TezosSdk.instance.dependencyRegistry.core().addressBytesCoder,
): ByteArray = addressBytesCoder.encode(this)

public fun Address.Companion.decodeFromBytes(
    bytes: ByteArray,
    addressBytesCoder: AddressBytesCoder = TezosSdk.instance.dependencyRegistry.core().addressBytesCoder,
): Address<*> = addressBytesCoder.decode(bytes)

public fun Address.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    addressBytesCoder: AddressBytesCoder = TezosSdk.instance.dependencyRegistry.core().addressBytesCoder,
): Address<*> = addressBytesCoder.decodeConsuming(bytes)

// -- ImplicitAddress <-> ByteArray --

public fun ImplicitAddress<*>.encodeToBytes(
    implicitAddressBytesCoder: ImplicitAddressBytesCoder = TezosSdk.instance.dependencyRegistry.core().implicitAddressBytesCoder,
): ByteArray = implicitAddressBytesCoder.encode(this)

public fun ImplicitAddress.Companion.decodeFromBytes(
    bytes: ByteArray,
    implicitAddressBytesCoder: ImplicitAddressBytesCoder = TezosSdk.instance.dependencyRegistry.core().implicitAddressBytesCoder,
): ImplicitAddress<*> = implicitAddressBytesCoder.decode(bytes)

public fun ImplicitAddress.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    implicitAddressBytesCoder: ImplicitAddressBytesCoder = TezosSdk.instance.dependencyRegistry.core().implicitAddressBytesCoder,
): ImplicitAddress<*> = implicitAddressBytesCoder.decodeConsuming(bytes)

// -- PublicKeyEncoded <-> ByteArray --

public fun PublicKeyEncoded<*>.encodeToBytes(
    publicKeyBytesCoder: PublicKeyBytesCoder = TezosSdk.instance.dependencyRegistry.core().publicKeyBytesCoder,
): ByteArray = publicKeyBytesCoder.encode(this)

public fun PublicKeyEncoded.Companion.decodeFromBytes(
    bytes: ByteArray,
    publicKeyBytesCoder: PublicKeyBytesCoder = TezosSdk.instance.dependencyRegistry.core().publicKeyBytesCoder,
): PublicKeyEncoded<*> = publicKeyBytesCoder.decode(bytes)

public fun PublicKeyEncoded.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    publicKeyBytesCoder: PublicKeyBytesCoder = TezosSdk.instance.dependencyRegistry.core().publicKeyBytesCoder,
): PublicKeyEncoded<*> = publicKeyBytesCoder.decodeConsuming(bytes)

// -- SignatureEncoded <-> ByteArray --

public fun SignatureEncoded<*>.encodeToBytes(
    signatureBytesCoder: SignatureBytesCoder = TezosSdk.instance.dependencyRegistry.core().signatureBytesCoder,
): ByteArray = signatureBytesCoder.encode(this)

public fun SignatureEncoded.Companion.decodeFromBytes(
    bytes: ByteArray,
    signatureBytesCoder: SignatureBytesCoder = TezosSdk.instance.dependencyRegistry.core().signatureBytesCoder,
): SignatureEncoded<*> = signatureBytesCoder.decode(bytes)

public fun SignatureEncoded.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    signatureBytesCoder: SignatureBytesCoder = TezosSdk.instance.dependencyRegistry.core().signatureBytesCoder,
): SignatureEncoded<*> = signatureBytesCoder.decodeConsuming(bytes)

// -- ZarithInteger <-> ByteArray --

public fun ZarithInteger.encodeToBytes(
    zarithIntegerBytesCoder: ZarithIntegerBytesCoder = TezosSdk.instance.dependencyRegistry.core().zarithIntegerBytesCoder,
): ByteArray = zarithIntegerBytesCoder.encode(this)

public fun ZarithInteger.Companion.decodeFromBytes(
    bytes: ByteArray,
    zarithIntegerBytesCoder: ZarithIntegerBytesCoder = TezosSdk.instance.dependencyRegistry.core().zarithIntegerBytesCoder,
): ZarithInteger = zarithIntegerBytesCoder.decode(bytes)

public fun ZarithInteger.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    zarithIntegerBytesCoder: ZarithIntegerBytesCoder = TezosSdk.instance.dependencyRegistry.core().zarithIntegerBytesCoder,
): ZarithInteger = zarithIntegerBytesCoder.decodeConsuming(bytes)

// -- ZarithNatural <-> ByteArray --

public fun ZarithNatural.encodeToBytes(
    zarithNaturalBytesCoder: ZarithNaturalBytesCoder = TezosSdk.instance.dependencyRegistry.core().zarithNaturalBytesCoder,
): ByteArray = zarithNaturalBytesCoder.encode(this)

public fun ZarithNatural.Companion.decodeFromBytes(
    bytes: ByteArray,
    zarithNaturalBytesCoder: ZarithNaturalBytesCoder = TezosSdk.instance.dependencyRegistry.core().zarithNaturalBytesCoder,
): ZarithNatural = zarithNaturalBytesCoder.decode(bytes)

public fun ZarithNatural.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    zarithNaturalBytesCoder: ZarithNaturalBytesCoder = TezosSdk.instance.dependencyRegistry.core().zarithNaturalBytesCoder,
): ZarithNatural = zarithNaturalBytesCoder.decodeConsuming(bytes)
