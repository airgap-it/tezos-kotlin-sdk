package it.airgap.tezos.rpc

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.rpc.data.*
import it.airgap.tezos.rpc.internal.http.HttpClient

public class TezosRpc internal constructor(private val nodeUrl: String, private val httpClient: HttpClient) {

    // ==== shell ====

    // -- /chains --

    public suspend fun setBootstrapped(chainId: String, bootstrapped: Boolean): SetBootstrappedResponse =
        httpClient.patch(nodeUrl, "/chains/$chainId", request = SetBootstrappedRequest(bootstrapped))

    public suspend fun getBlocks(chainId: String, length: UInt? = null, head: BlockHash? = null, minDate: String? = null): GetBlocksResponse =
        httpClient.get(
            nodeUrl,
            "/chains/$chainId",
            headers = buildList {
                length?.let { add("length" to length.toString()) }
                head?.let { add("head" to head.base58) }
                minDate?.let { add("min_date" to minDate) }
            }
        )

    public suspend fun getChainId(chainId: String): GetChainIdResponse =
        httpClient.get(nodeUrl, "chains/$chainId/chain_id")

    public suspend fun getInvalidBlocks(chainId: String): GetInvalidBlocksResponse =
        httpClient.get(nodeUrl, "chains/$chainId/invalid_blocks")

    public suspend fun getInvalidBlock(chainId: String, blockHash: BlockHash): GetInvalidBlockResponse =
        httpClient.get(nodeUrl, "chains/$chainId/invalid_blocks/${blockHash.base58}")

    public suspend fun deleteInvalidBlock(chainId: String, blockHash: BlockHash): DeleteInvalidBlockResponse =
        httpClient.delete(nodeUrl, "chains/${chainId}/invalid_blocks/${blockHash.base58}")

    public suspend fun isBootstrapped(chainId: String): IsBootstrappedResponse =
        httpClient.get(nodeUrl, "chains/${chainId}/is_bootstrapped")

    public suspend fun getCaboose(chainId: String): GetCabooseResponse =
        httpClient.get(nodeUrl, "chains/${chainId}/levels/caboose")

    public suspend fun getCheckpoint(chainId: String): GetCheckpointResponse =
        httpClient.get(nodeUrl, "chains/${chainId}/levels/checkpoint")

    public suspend fun getSavepoint(chainId: String): GetSavepointResponse =
        httpClient.get(nodeUrl, "chains/${chainId}/levels/savepoint")
}