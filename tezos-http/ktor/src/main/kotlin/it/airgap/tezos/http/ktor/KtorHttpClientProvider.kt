package it.airgap.tezos.http.ktor

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.http.HttpParameter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * [HttpClientProvider] implementation that uses [Ktor](https://ktor.io/) to satisfy the interface requirements.
 *
 * @property engineFactory [Ktor HttpClientEngineFactory][HttpClientEngineFactory] that the underlying [Ktor HttpClient][HttpClient] should be configured with.
 * @property logger An optional logging configuration.
 */
public class KtorHttpClientProvider(
    private val engineFactory: HttpClientEngineFactory<*>,
    private val logger: KtorLogger? = null,
) : HttpClientProvider {
    private val json: Json by lazy { Json.Default }
    private val ktor: HttpClient by lazy {
        HttpClient(engineFactory) {
            install(HttpTimeout)

            install(ContentNegotiation) {
                json(json)
            }

            logger?.let {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            it.log(message)
                        }
                    }
                    level = when (it.level) {
                        KtorLogger.LogLevel.All -> LogLevel.ALL
                        KtorLogger.LogLevel.Headers -> LogLevel.HEADERS
                        KtorLogger.LogLevel.Body -> LogLevel.BODY
                        KtorLogger.LogLevel.Info -> LogLevel.INFO
                        KtorLogger.LogLevel.None -> LogLevel.NONE
                    }
                }
            }
        }
    }

    /**
     * Call DELETE HTTP method on specified [baseUrl] and [endpoint] with [headers] and [parameters].
     *
     * The method returns a JSON response encoded as [String].
     */
    override suspend fun delete(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
    ): String = request(HttpMethod.Delete, baseUrl, endpoint, headers, parameters)

    /**
     * Call GET HTTP method on specified [baseUrl] and [endpoint] with [headers] and [parameters].
     *
     * The method returns a JSON response encoded as [String].
     */
    override suspend fun get(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
    ): String = request(HttpMethod.Get, baseUrl, endpoint, headers, parameters)

    /**
     * Call PATCH HTTP method on specified [baseUrl] and [endpoint] with [headers], [parameters] and [body].
     *
     * The [body] is a serialized JSON in [String] representation.
     * The method returns a JSON response encoded as [String].
     */
    override suspend fun patch(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        body: String?,
    ): String = request(HttpMethod.Patch, baseUrl, endpoint, headers, parameters) {
        body?.let { setBodyAsText(it) }
    }

    /**
     * Call POST HTTP method on specified [baseUrl] and [endpoint] with [headers], [parameters] and [body].
     *
     * The [body] is a serialized JSON in [String] representation.
     * The method returns a JSON response encoded as [String].
     */
    override suspend fun post(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        body: String?,
    ): String = request(HttpMethod.Post, baseUrl, endpoint, headers, parameters) {
        body?.let { setBodyAsText(it) }
    }

    /**
     * Call PUT HTTP method on specified [baseUrl] and [endpoint] with [headers], [parameters] and [body].
     *
     * The [body] is a serialized JSON in [String] representation.
     * The method returns a JSON response encoded as [String].
     */
    override suspend fun put(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        body: String?,
    ): String = request(HttpMethod.Put, baseUrl, endpoint, headers, parameters) {
        body?.let { setBodyAsText(it) }
    }

    private suspend fun request(
        method: HttpMethod,
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        block: HttpRequestBuilder.() -> Unit = {},
    ): String {
        val response = ktor.request {
            this.method = method

            url(listOf(baseUrl, endpoint).combineToUrl())

            headers(headers)
            parameters(parameters)

            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)

            block(this)
        }

        return response.bodyAsText()
    }

    private fun HttpRequestBuilder.setBodyAsText(body: String) {
        val jsonElement = json.decodeFromString<JsonElement>(body)
        setBody(jsonElement)
    }

    private fun HttpRequestBuilder.headers(headers: List<HttpHeader>) {
        headers.forEach { header(it.first, it.second) }
    }

    private fun HttpRequestBuilder.parameters(parameters: List<HttpParameter>) {
        parameters.forEach { parameter(it.first, it.second) }
    }

    private fun List<String>.combineToUrl(): String = joinToString("/") { it.trimStart('/') }.trimEnd('/')
}

/**
 * Creates a new [KtorHttpClientProvider] instance with a default [CIO HttpClientEngineFactory][CIO] and optional [logger].
 */
public fun KtorHttpClientProvider(logger: KtorLogger? = null): KtorHttpClientProvider = KtorHttpClientProvider(CIO, logger)