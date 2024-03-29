package it.airgap.tezos.rpc.shell.chains

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.BlockClient
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient

internal class ChainsClient(parentUrl: String, private val httpClient: HttpClient) : Chains {
    private val baseUrl: String = /* /chains */ "$parentUrl/chains"

    override operator fun invoke(chainId: String): Chains.Chain = ChainsChainClient(baseUrl, chainId, httpClient)
}

private class ChainsChainClient(parentUrl: String, chainId: String, private val httpClient: HttpClient) : Chains.Chain {
    private val baseUrl: String = /* /chains/<chain_id> */ "$parentUrl/$chainId"

    override suspend fun patch(
        bootstrapped: Boolean,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ) = httpClient.patch<SetBootstrappedRequest, SetBootstrappedResponse>(
        baseUrl,
        "/",
        headers,
        request = SetBootstrappedRequest(bootstrapped),
        requestTimeout = requestTimeout,
        connectionTimeout = connectionTimeout,
    )

    override val blocks: Chains.Chain.Blocks by lazy { ChainsChainBlocksClient(baseUrl, httpClient) }
    override val chainId: Chains.Chain.ChainId by lazy { ChainsChainChainIdClient(baseUrl, httpClient) }
    override val invalidBlocks: Chains.Chain.InvalidBlocks by lazy { ChainsChainInvalidBlocksClient(baseUrl, httpClient) }
    override val isBootstrapped: Chains.Chain.IsBootstrapped by lazy { ChainsChainIsBootstrappedClient(baseUrl, httpClient) }
    override val levels: Chains.Chain.Levels by lazy { ChainsChainLevelsClient(baseUrl, httpClient) }
}

private class ChainsChainBlocksClient(parentUrl: String, private val httpClient: HttpClient) : Chains.Chain.Blocks {
    private val baseUrl: String = /* /chains/<chain_id>/blocks */ "$parentUrl/blocks"

    override suspend fun get(
        length: UInt?,
        head: BlockHash?,
        minDate: String?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBlocksResponse =
        httpClient.get(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                length?.let { add("length" to length.toString()) }
                head?.let { add("head" to head.base58) }
                minDate?.let { add("min_date" to minDate) }
            },
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )

    override operator fun invoke(blockId: String): Block = BlockClient(baseUrl, blockId, httpClient)
}

private class ChainsChainChainIdClient(parentUrl: String, private val httpClient: HttpClient) : Chains.Chain.ChainId {
    private val baseUrl: String = /* /chains/<chain_id>/chain_id */ "$parentUrl/chain_id"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetChainIdResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class ChainsChainInvalidBlocksClient(parentUrl: String, private val httpClient: HttpClient) : Chains.Chain.InvalidBlocks {
    private val baseUrl: String = /* /chains/<chain_id>/invalid_blocks */ "$parentUrl/invalid_blocks"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetInvalidBlocksResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)

    override operator fun invoke(blockHash: BlockHash): Chains.Chain.InvalidBlocks.Block = ChainsInvalidBlockClient(baseUrl, blockHash, httpClient)
}

private class ChainsInvalidBlockClient(parentUrl: String, blockHash: BlockHash, private val httpClient: HttpClient) : Chains.Chain.InvalidBlocks.Block {
    private val baseUrl: String = /* /chains/<chain_id>/invalid_blocks/<block_hash> */  "$parentUrl/${blockHash.base58}"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetInvalidBlockResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
    override suspend fun delete(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): DeleteInvalidBlockResponse = httpClient.delete(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class ChainsChainIsBootstrappedClient(parentUrl: String, private val httpClient: HttpClient) : Chains.Chain.IsBootstrapped {
    private val baseUrl: String = /* /chains/<chain_id>/is_bootstrapped */ "$parentUrl/is_bootstrapped"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBootstrappedStatusResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)

}

private class ChainsChainLevelsClient(parentUrl: String, private val httpClient: HttpClient) : Chains.Chain.Levels {
    private val baseUrl: String = /* /chains/<chain_id>/levels */ "$parentUrl/levels"

    override val caboose: Chains.Chain.Levels.Caboose by lazy { ChainsChainLevelCabooseClient(baseUrl, httpClient) }
    override val checkpoint: Chains.Chain.Levels.Checkpoint by lazy { ChainsChainLevelCheckpointClient(baseUrl, httpClient) }
    override val savepoint: Chains.Chain.Levels.Savepoint by lazy { ChainsChainLevelSavepointClient(baseUrl, httpClient) }
}

private class ChainsChainLevelCabooseClient(parentUrl: String, private val httpClient: HttpClient) : Chains.Chain.Levels.Caboose {
    private val baseUrl: String = /* /chains/<chain_id>/levels/caboose */ "$parentUrl/caboose"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetCabooseResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class ChainsChainLevelCheckpointClient(parentUrl: String, private val httpClient: HttpClient) : Chains.Chain.Levels.Checkpoint {
    private val baseUrl: String = /* /chains/<chain_id>/levels/checkpoint */ "$parentUrl/checkpoint"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetCheckpointResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class ChainsChainLevelSavepointClient(parentUrl: String, private val httpClient: HttpClient) : Chains.Chain.Levels.Savepoint {
    private val baseUrl: String = /* /chains/<chain_id>/levels/savepoint */ "$parentUrl/savepoint"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetSavepointResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}