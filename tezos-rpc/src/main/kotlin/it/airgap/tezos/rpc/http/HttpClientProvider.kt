package it.airgap.tezos.rpc.http

/**
 * A key value HTTP header.
 */
public typealias HttpHeader = Pair<String, String?>

/**
 * A key value HTTP parameter.
 */
public typealias HttpParameter = Pair<String, String?>

/**
 * External HTTP client provider.
 *
 * Use this interface to register a custom HTTP client implementation in [it.airgap.tezos.core.Tezos].
 * See:
 *  - `tezos-http` for ready-to-use implementations
 *  - `samples/src/test/kotlin/Basic/BasicSamples#configuration` to learn how to register a custom provider.
*/
public interface HttpClientProvider {

    /**
     * Call DELETE HTTP method on specified [baseUrl] and [endpoint] with [headers] and [parameters].
     *
     * The method returns a JSON response encoded as [String].
     */
    public suspend fun delete(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        requestTimeout: Long? = null,
        connectionTimeout: Long? = null,
    ): String

    /**
     * Call GET HTTP method on specified [baseUrl] and [endpoint] with [headers] and [parameters].
     *
     * The method returns a JSON response encoded as [String].
     */
    public suspend fun get(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        requestTimeout: Long? = null,
        connectionTimeout: Long? = null,
    ): String /* raw JSON */

    /**
     * Call PATCH HTTP method on specified [baseUrl] and [endpoint] with [headers], [parameters] and [body].
     *
     * The [body] is a serialized JSON in [String] representation.
     * The method returns a JSON response encoded as [String].
     */
    public suspend fun patch(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        body: String?, /* raw JSON */
        requestTimeout: Long? = null,
        connectionTimeout: Long? = null,
    ): String /* raw JSON */

    /**
     * Call POST HTTP method on specified [baseUrl] and [endpoint] with [headers], [parameters] and [body].
     *
     * The [body] is a serialized JSON in [String] representation.
     * The method returns a JSON response encoded as [String].
     */
    public suspend fun post(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        body: String?, /* raw JSON */
        requestTimeout: Long? = null,
        connectionTimeout: Long? = null,
    ): String /* raw JSON */

    /**
     * Call PUT HTTP method on specified [baseUrl] and [endpoint] with [headers], [parameters] and [body].
     *
     * The [body] is a serialized JSON in [String] representation.
     * The method returns a JSON response encoded as [String].
     */
    public suspend fun put(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        body: String?, /* raw JSON */
        requestTimeout: Long? = null,
        connectionTimeout: Long? = null,
    ): String /* raw JSON */
}