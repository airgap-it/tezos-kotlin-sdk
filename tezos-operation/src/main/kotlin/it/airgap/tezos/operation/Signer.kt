package it.airgap.tezos.operation

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.operation.internal.di.operation
import it.airgap.tezos.operation.internal.signer.OperationEd25519Signer
import it.airgap.tezos.operation.internal.signer.OperationP256Signer
import it.airgap.tezos.operation.internal.signer.OperationSecp256K1Signer
import it.airgap.tezos.operation.internal.signer.OperationSigner

// -- Operation --

public fun <T : Operation> T.sign(
    key: SecretKeyEncoded,
    operationSigner: OperationSigner = TezosSdk.instance.dependencyRegistry.operation().operationSigner,
): Operation.Signed = Operation.Signed(branch, contents, operationSigner.sign(this, key))

public fun Operation.Signed.verify(
    key: PublicKeyEncoded,
    operationSigner: OperationSigner = TezosSdk.instance.dependencyRegistry.operation().operationSigner,
): Boolean = operationSigner.verify(this, signature, key)

// -- SecretKeyEncoded --

public fun SecretKeyEncoded.sign(
    operation: Operation,
    operationSigner: OperationSigner = TezosSdk.instance.dependencyRegistry.operation().operationSigner,
): SignatureEncoded = operationSigner.sign(operation, this)

// -- PublicKeyEncoded --

public fun <T : PublicKeyEncoded> T.verify(
    operation: Operation.Signed,
    operationSigner: OperationSigner = TezosSdk.instance.dependencyRegistry.operation().operationSigner,
): Boolean = operationSigner.verify(operation, operation.signature, this)

// -- Ed25519SecretKey --

public fun Ed25519SecretKey.sign(
    operation: Operation,
    operationEd25519Signer: OperationEd25519Signer = TezosSdk.instance.dependencyRegistry.operation().operationEd25519Signer,
): Ed25519Signature = operationEd25519Signer.sign(operation, this)

// -- Secp256K1SecretKey --

public fun Secp256K1SecretKey.sign(
    operation: Operation,
    operationSecp256K1Signer: OperationSecp256K1Signer = TezosSdk.instance.dependencyRegistry.operation().operationSecp256K1Signer,
): Secp256K1Signature = operationSecp256K1Signer.sign(operation, this)

// -- Ed25519SecretKey --

public fun P256SecretKey.sign(
    operation: Operation,
    operationP256Signer: OperationP256Signer = TezosSdk.instance.dependencyRegistry.operation().operationP256Signer,
): P256Signature = operationP256Signer.sign(operation, this)