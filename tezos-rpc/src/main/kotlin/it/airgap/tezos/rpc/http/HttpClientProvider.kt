package it.airgap.tezos.rpc.http

public typealias HttpHeader = Pair<String, String>
public typealias HttpParameter = Pair<String, String>

public interface HttpClientProvider {

    public suspend fun delete(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
    ): Unit

    public suspend fun get(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
    ): String /* raw JSON */

    public suspend fun patch(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        body: String?, /* raw JSON */
    ): String /* raw JSON */

    public suspend fun post(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        body: String?, /* raw JSON */
    ): String /* raw JSON */

    public suspend fun put(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        body: String?, /* raw JSON */
    ): String /* raw JSON */
}