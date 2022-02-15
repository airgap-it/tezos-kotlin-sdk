package it.airgap.tezos.rpc.internal.network

import it.airgap.tezos.rpc.internal.utils.decodeFromString
import it.airgap.tezos.rpc.internal.utils.encodeToString
import it.airgap.tezos.rpc.network.HttpClientProvider
import it.airgap.tezos.rpc.network.HttpHeader
import it.airgap.tezos.rpc.network.HttpParameter
import kotlinx.serialization.json.Json

public class HttpClient(
    @PublishedApi internal val httpClientProvider: HttpClientProvider,
    @PublishedApi internal val json: Json,
) {
    @Throws(Exception::class)
    public suspend fun delete(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader> = emptyList(),
        parameters: List<HttpParameter> = emptyList(),
    ): Unit = httpClientProvider.delete(baseUrl, endpoint, headers, parameters)

    @Throws(Exception::class)
    public suspend inline fun <reified Response : Any> get(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader> = emptyList(),
        parameters: List<HttpParameter> = emptyList(),
    ): Response = httpClientProvider.get(baseUrl, endpoint, headers, parameters).decodeAsResponse()

    @Throws(Exception::class)
    public suspend inline fun <reified Request : Any, reified Response : Any> patch(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader> = emptyList(),
        parameters: List<HttpParameter> = emptyList(),
        request: Request? = null,
    ): Response = httpClientProvider.patch(baseUrl, endpoint, headers, parameters, request?.encodeAsRequest()).decodeAsResponse()

    @Throws(Exception::class)
    public suspend inline fun <reified Request : Any, reified Response : Any> post(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader> = emptyList(),
        parameters: List<HttpParameter> = emptyList(),
        request: Request? = null,
    ): Response = httpClientProvider.post(baseUrl, endpoint, headers, parameters, request?.encodeAsRequest()).decodeAsResponse()

    @Throws(Exception::class)
    public suspend inline fun <reified Request : Any, reified Response : Any> put(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader> = emptyList(),
        parameters: List<HttpParameter> = emptyList(),
        request: Request? = null,
    ): Response = httpClientProvider.put(baseUrl, endpoint, headers, parameters, request?.encodeAsRequest()).decodeAsResponse()

    @PublishedApi
    internal inline fun <reified T : Any> T.encodeAsRequest(): String = json.encodeToString(this)

    @PublishedApi
    internal inline fun <reified T : Any> String.decodeAsResponse(): T = json.decodeFromString(this)
}