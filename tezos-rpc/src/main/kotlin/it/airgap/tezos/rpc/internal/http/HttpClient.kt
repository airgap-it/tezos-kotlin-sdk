package it.airgap.tezos.rpc.internal.http

import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.http.HttpParameter
import it.airgap.tezos.rpc.internal.utils.decodeFromString
import it.airgap.tezos.rpc.internal.utils.encodeToString
import kotlinx.serialization.json.Json

public class HttpClient(
    @PublishedApi internal val httpClientProvider: HttpClientProvider,
    @PublishedApi internal val json: Json,
) {
    @Throws(Exception::class)
    public suspend inline fun <reified Response : Any> delete(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader> = emptyList(),
        parameters: List<HttpParameter> = emptyList(),
        requestTimeout: Long? = null,
        connectionTimeout: Long? = null,
    ): Response = httpClientProvider.delete(baseUrl, endpoint, headers, parameters, requestTimeout, connectionTimeout).decodeAsResponse()

    @Throws(Exception::class)
    public suspend inline fun <reified Response : Any> get(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader> = emptyList(),
        parameters: List<HttpParameter> = emptyList(),
        requestTimeout: Long? = null,
        connectionTimeout: Long? = null,
    ): Response = httpClientProvider.get(baseUrl, endpoint, headers, parameters, requestTimeout, connectionTimeout).decodeAsResponse()

    @Throws(Exception::class)
    public suspend inline fun <reified Request : Any, reified Response : Any> patch(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader> = emptyList(),
        parameters: List<HttpParameter> = emptyList(),
        request: Request? = null,
        requestTimeout: Long? = null,
        connectionTimeout: Long? = null,
    ): Response = httpClientProvider.patch(baseUrl, endpoint, headers, parameters, request?.encodeAsRequest(), requestTimeout, connectionTimeout).decodeAsResponse()

    @Throws(Exception::class)
    public suspend inline fun <reified Request : Any, reified Response : Any> post(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader> = emptyList(),
        parameters: List<HttpParameter> = emptyList(),
        request: Request? = null,
        requestTimeout: Long? = null,
        connectionTimeout: Long? = null,
    ): Response = httpClientProvider.post(baseUrl, endpoint, headers, parameters, request?.encodeAsRequest(), requestTimeout, connectionTimeout).decodeAsResponse()

    @Throws(Exception::class)
    public suspend inline fun <reified Request : Any, reified Response : Any> put(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader> = emptyList(),
        parameters: List<HttpParameter> = emptyList(),
        request: Request? = null,
        requestTimeout: Long? = null,
        connectionTimeout: Long? = null,
    ): Response = httpClientProvider.put(baseUrl, endpoint, headers, parameters, request?.encodeAsRequest(), requestTimeout, connectionTimeout).decodeAsResponse()

    @PublishedApi
    internal inline fun <reified T : Any> T.encodeAsRequest(): String = json.encodeToString(this)

    @PublishedApi
    internal inline fun <reified T : Any> String.decodeAsResponse(): T =
        when (T::class) {
            Unit::class -> Unit as T
            else -> json.decodeFromString(this)
        }
}