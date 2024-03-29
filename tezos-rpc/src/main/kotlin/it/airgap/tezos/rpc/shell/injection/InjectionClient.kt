package it.airgap.tezos.rpc.shell.injection

import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.type.operation.RpcInjectableOperation
import it.airgap.tezos.rpc.type.protocol.RpcProtocolComponent

internal class InjectionClient(parentUrl: String, private val httpClient: HttpClient) : Injection {
    private val baseUrl: String = /* /injection */ "$parentUrl/injection"

    override val block: Injection.Block by lazy { InjectionBlockClient(baseUrl, httpClient) }
    override val operation: Injection.Operation by lazy { InjectionOperationClient(baseUrl, httpClient) }
    override val protocol: Injection.Protocol by lazy { InjectionProtocolClient(baseUrl, httpClient) }
}

private class InjectionBlockClient(parentUrl: String, private val httpClient: HttpClient) : Injection.Block {
    private val baseUrl: String = /* /injection/block */ "$parentUrl/block"

    override suspend fun post(
        data: String,
        operations: List<List<RpcInjectableOperation>>,
        async: Boolean?,
        force: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): InjectBlockResponse =
        httpClient.post(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
                force?.takeIf { it }?.let { add("force" to null) }
                chain?.let { add("chain" to chain.base58) }
            },
            request = InjectBlockRequest(data, operations),
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )
}

private class InjectionOperationClient(parentUrl: String, private val httpClient: HttpClient) : Injection.Operation {
    private val baseUrl: String = /* /injection/operation */ "$parentUrl/operation"

    override suspend fun post(
        data: String,
        async: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): InjectOperationResponse =
        httpClient.post(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
                chain?.let { add("chain" to chain.base58) }
            },
            request = InjectOperationRequest(data),
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )
}

private class InjectionProtocolClient(parentUrl: String, private val httpClient: HttpClient) : Injection.Protocol {
    private val baseUrl: String = /* /injection/protocol */ "$parentUrl/protocol"

    override suspend fun post(
        expectedEnvVersion: UShort,
        components: List<RpcProtocolComponent>,
        async: Boolean?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): InjectProtocolResponse =
        httpClient.post(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
            },
            request = InjectProtocolRequest(expectedEnvVersion, components),
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )
}
