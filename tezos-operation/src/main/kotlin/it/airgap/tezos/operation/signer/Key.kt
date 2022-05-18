package it.airgap.tezos.operation.signer

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.signer.Signer
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.internal.operationModule

// -- SecretKeyEncoded --

public fun SecretKey.sign(operation: Operation, tezos: Tezos = Tezos.Default): Signature =
    sign(operation, tezos.operationModule.dependencyRegistry.operationSigner)

@InternalTezosSdkApi
public fun SecretKey.sign(operation: Operation, operationSigner: Signer<Operation, SecretKey, PublicKey, Signature>): Signature =
    operationSigner.sign(operation, this)

// -- PublicKeyEncoded --

public fun <T : PublicKey> T.verify(operation: Operation.Signed, tezos: Tezos = Tezos.Default): Boolean =
    verify(operation, tezos.operationModule.dependencyRegistry.operationSigner)

@InternalTezosSdkApi
public fun <T : PublicKey> T.verify(operation: Operation.Signed, operationSigner: Signer<Operation, SecretKey, PublicKey, Signature>): Boolean =
    operationSigner.verify(operation, operation.signature, this)

// -- Ed25519SecretKey --

public fun Ed25519SecretKey.sign(operation: Operation, tezos: Tezos = Tezos.Default): Ed25519Signature =
    sign(operation, tezos.operationModule.dependencyRegistry.operationEd25519Signer)

@InternalTezosSdkApi
public fun Ed25519SecretKey.sign(operation: Operation, operationEd25519Signer: Signer<Operation, Ed25519SecretKey, Ed25519PublicKey, Ed25519Signature>): Ed25519Signature =
    operationEd25519Signer.sign(operation, this)

// -- Secp256K1SecretKey --

public fun Secp256K1SecretKey.sign(operation: Operation, tezos: Tezos = Tezos.Default): Secp256K1Signature =
    sign(operation, tezos.operationModule.dependencyRegistry.operationSecp256K1Signer)

@InternalTezosSdkApi
public fun Secp256K1SecretKey.sign(operation: Operation, operationSecp256K1Signer: Signer<Operation, Secp256K1SecretKey, Secp256K1PublicKey, Secp256K1Signature>): Secp256K1Signature =
    operationSecp256K1Signer.sign(operation, this)

// -- Ed25519SecretKey --

public fun P256SecretKey.sign(operation: Operation, tezos: Tezos = Tezos.Default): P256Signature =
    sign(operation, tezos.operationModule.dependencyRegistry.operationP256Signer)

@InternalTezosSdkApi
public fun P256SecretKey.sign(operation: Operation, operationP256Signer: Signer<Operation, P256SecretKey, P256PublicKey, P256Signature>): P256Signature =
    operationP256Signer.sign(operation, this)