package it.airgap.tezos.operation.signer

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.signer.Signer
import it.airgap.tezos.core.type.encoded.PublicKey
import it.airgap.tezos.core.type.encoded.SecretKey
import it.airgap.tezos.core.type.encoded.Signature
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.internal.context.withTezosContext
import it.airgap.tezos.operation.internal.operationModule

/**
 * Signs this [operation][Operation] with the [key].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun <T : Operation> T.signWith(key: SecretKey, tezos: Tezos = Tezos.Default): Operation.Signed = withTezosContext {
    sign(key, tezos.operationModule.dependencyRegistry.operationSigner)
}

/**
 * Verifies this [operation][Operation.Signed] with the [key].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Operation.Signed.verifyWith(key: PublicKey, tezos: Tezos = Tezos.Default): Boolean = withTezosContext {
    verify(key, tezos.operationModule.dependencyRegistry.operationSigner)
}

@InternalTezosSdkApi
public interface OperationSignerContext {
    public fun <T : Operation> T.sign(key: SecretKey, operationSigner: Signer<Operation, SecretKey, PublicKey, Signature>): Operation.Signed =
        Operation.Signed(branch, contents, operationSigner.sign(this, key))

    public fun Operation.Signed.verify(key: PublicKey, operationSigner: Signer<Operation, SecretKey, PublicKey, Signature>): Boolean =
        operationSigner.verify(this, signature, key)
}