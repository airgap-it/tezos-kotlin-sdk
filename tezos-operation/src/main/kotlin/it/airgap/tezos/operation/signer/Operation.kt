package it.airgap.tezos.operation.signer

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.signer.Signer
import it.airgap.tezos.core.type.encoded.PublicKey
import it.airgap.tezos.core.type.encoded.SecretKey
import it.airgap.tezos.core.type.encoded.Signature
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.internal.operation

// -- Operation --

public fun <T : Operation> T.sign(key: SecretKey, tezos: Tezos = Tezos.Default): Operation.Signed =
    sign(key, tezos.operation().dependencyRegistry.operationSigner)

public fun Operation.Signed.verify(key: PublicKey, tezos: Tezos = Tezos.Default): Boolean =
    verify(key, tezos.operation().dependencyRegistry.operationSigner)

@InternalTezosSdkApi
public fun <T : Operation> T.sign(key: SecretKey, operationSigner: Signer<Operation, SecretKey, PublicKey, Signature>): Operation.Signed =
    Operation.Signed(branch, contents, operationSigner.sign(this, key))

@InternalTezosSdkApi
public fun Operation.Signed.verify(key: PublicKey, operationSigner: Signer<Operation, SecretKey, PublicKey, Signature>): Boolean =
    operationSigner.verify(this, signature, key)