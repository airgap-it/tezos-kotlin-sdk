package it.airgap.tezos.rpc.shell.injection

import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.type.operation.RpcInjectableOperation
import it.airgap.tezos.rpc.type.protocol.RpcProtocolComponent

public interface Injection {
    public val block: Block
    public val operation: Operation
    public val protocol: Protocol

    public interface Block {
        public suspend fun post(
            data: String,
            operations: List<List<RpcInjectableOperation>>,
            async: Boolean? = null,
            force: Boolean? = null,
            chain: ChainId? = null,
            headers: List<HttpHeader> = emptyList(),
        ): InjectBlockResponse
    }

    public interface Operation {
        public suspend fun post(
            data: String,
            async: Boolean? = null,
            chain: ChainId? = null,
            headers: List<HttpHeader> = emptyList(),
        ): InjectOperationResponse
    }

    public interface Protocol {
        public suspend fun post(
            expectedEnvVersion: UShort,
            components: List<RpcProtocolComponent>,
            async: Boolean? = null,
            headers: List<HttpHeader> = emptyList(),
        ): InjectProtocolResponse
    }
}