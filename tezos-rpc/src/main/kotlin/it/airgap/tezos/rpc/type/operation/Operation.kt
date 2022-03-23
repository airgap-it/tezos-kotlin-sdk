package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcOperation --

@Serializable
public sealed class RpcOperation {

    public abstract val protocol: @Contextual ProtocolHash
    public abstract val chainId: @Contextual ChainId
    public abstract val hash: @Contextual OperationHash
    public abstract val branch: @Contextual BlockHash
    public abstract val signature: @Contextual SignatureEncoded

    @Serializable
    public data class WithContents(
        override val protocol: @Contextual ProtocolHash,
        @SerialName("chain_id") override val chainId: @Contextual ChainId,
        override val hash: @Contextual OperationHash,
        override val branch: @Contextual BlockHash,
        public val contents: List<RpcOperationContent>,
        override val signature: @Contextual SignatureEncoded,
    ) : RpcOperation()

    @Serializable
    public data class WithContentsAndResults(
        override val protocol: @Contextual ProtocolHash,
        @SerialName("chain_id") override val chainId: @Contextual ChainId,
        override val hash: @Contextual OperationHash,
        override val branch: @Contextual BlockHash,
        public val contents: List<RpcOperationContentWithResult>,
        override val signature: @Contextual SignatureEncoded,
    ) : RpcOperation()
}

// -- RpcInjectableOperation --

@Serializable
public data class RpcInjectableOperation(
    public val branch: @Contextual BlockHash,
    public val data: @Contextual HexString,
)