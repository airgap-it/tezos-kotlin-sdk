package it.airgap.tezos.operation.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.signer.Signer
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.michelson.internal.di.MichelsonDependencyRegistry
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.internal.coder.OperationBytesCoder
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.operation.internal.context.TezosOperationContext.lazyWeak
import it.airgap.tezos.operation.internal.converter.TagToOperationContentKindConverter
import it.airgap.tezos.operation.internal.signer.OperationEd25519Signer
import it.airgap.tezos.operation.internal.signer.OperationP256Signer
import it.airgap.tezos.operation.internal.signer.OperationSecp256K1Signer
import it.airgap.tezos.operation.internal.signer.OperationSigner

@InternalTezosSdkApi
public class OperationDependencyRegistry internal constructor(
    private val global: DependencyRegistry,
    private val core: CoreDependencyRegistry,
    private val michelson: MichelsonDependencyRegistry
) {

    // -- coder --

    public val operationBytesCoder: ConsumingBytesCoder<Operation> by lazy { OperationBytesCoder(operationContentBytesCoder, core.encodedBytesCoder) }
    public val operationContentBytesCoder: ConsumingBytesCoder<OperationContent> by lazy {
        OperationContentBytesCoder(
            core.encodedBytesCoder,
            core.addressBytesCoder,
            core.publicKeyBytesCoder,
            core.implicitAddressBytesCoder,
            core.signatureBytesCoder,
            core.tezosNaturalBytesCoder,
            core.mutezBytesCoder,
            michelson.michelineBytesCoder,
            core.timestampBigIntCoder,
            tagToOperationContentKindConverter,
        )
    }

    // -- converter --

    public val tagToOperationContentKindConverter: Converter<UByte, OperationContent.Kind> by lazy { Static.tagToOperationContentKindConverter }

    // -- signer --

    public val operationSigner: Signer<Operation, SecretKey, PublicKey, Signature> by lazy {
        OperationSigner(
            operationEd25519Signer,
            operationSecp256K1Signer,
            operationP256Signer,
            core.genericSignatureToEd25519SignatureConverter,
            core.genericSignatureToSecp256K1SignatureConverter,
            core.genericSignatureToP256SignatureConverter,
        )
    }
    public val operationEd25519Signer: Signer<Operation, Ed25519SecretKey, Ed25519PublicKey, Ed25519Signature> by lazy { OperationEd25519Signer(global.crypto, operationBytesCoder, core.encodedBytesCoder) }
    public val operationSecp256K1Signer: Signer<Operation, Secp256K1SecretKey, Secp256K1PublicKey, Secp256K1Signature> by lazy { OperationSecp256K1Signer(global.crypto, operationBytesCoder, core.encodedBytesCoder) }
    public val operationP256Signer: Signer<Operation, P256SecretKey, P256PublicKey, P256Signature> by lazy { OperationP256Signer(global.crypto, operationBytesCoder, core.encodedBytesCoder) }

    private object Static {
        val tagToOperationContentKindConverter: Converter<UByte, OperationContent.Kind> by lazyWeak { TagToOperationContentKindConverter() }
    }
}
