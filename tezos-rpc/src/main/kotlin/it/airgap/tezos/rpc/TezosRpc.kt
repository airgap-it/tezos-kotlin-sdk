package it.airgap.tezos.rpc

import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.data.shell.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.type.*

public class TezosRpc internal constructor(
    private val nodeUrl: String,
    private val httpClient: HttpClient,
    private val encodedBytesCoder: EncodedBytesCoder,
) {

    // ==== shell (https://tezos.gitlab.io/shell/rpc.html) ====

    // -- /chains --

    public suspend fun setBootstrapped(
        chainId: String,
        bootstrapped: Boolean,
        headers: List<HttpHeader> = emptyList(),
    ): SetBootstrappedResponse =
        httpClient.patch(nodeUrl, "/chains/$chainId", headers, request = SetBootstrappedRequest(bootstrapped))

    public suspend fun getBlocks(
        chainId: String,
        length: UInt? = null,
        head: BlockHash? = null,
        minDate: String? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlocksResponse =
        httpClient.get<GetBlocksTransitionalResponse>(
            nodeUrl,
            "/chains/$chainId",
            headers,
            parameters = buildList {
                length?.let { add("length" to length.toString()) }
                head?.let { add("head" to head.base58) }
                minDate?.let { add("min_date" to minDate) }
            }
        ).toFinal()

    public suspend fun getChainId(chainId: String, headers: List<HttpHeader> = emptyList()): GetChainIdResponse =
        httpClient.get<GetChainIdTransitionalResponse>(nodeUrl, "/chains/$chainId/chain_id", headers).toFinal()

    public suspend fun getInvalidBlocks(chainId: String, headers: List<HttpHeader> = emptyList()): GetInvalidBlocksResponse =
        httpClient.get<GetInvalidBlocksTransitionalResponse>(nodeUrl, "/chains/$chainId/invalid_blocks", headers).toFinal()

    public suspend fun getInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader> = emptyList()): GetInvalidBlockResponse =
        httpClient.get<GetInvalidBlockTransitionalResponse>(nodeUrl, "/chains/$chainId/invalid_blocks/${blockHash.base58}", headers).toFinal()

    public suspend fun deleteInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader> = emptyList()): DeleteInvalidBlockResponse =
        httpClient.delete(nodeUrl, "/chains/${chainId}/invalid_blocks/${blockHash.base58}", headers)

    public suspend fun isBootstrapped(chainId: String, headers: List<HttpHeader> = emptyList()): IsBootstrappedResponse =
        httpClient.get(nodeUrl, "/chains/${chainId}/is_bootstrapped", headers)

    public suspend fun getCaboose(chainId: String, headers: List<HttpHeader> = emptyList()): GetCabooseResponse =
        httpClient.get<GetCabooseTransitionalResponse>(nodeUrl, "/chains/${chainId}/levels/caboose", headers).toFinal()

    public suspend fun getCheckpoint(chainId: String, headers: List<HttpHeader> = emptyList()): GetCheckpointResponse =
        httpClient.get<GetCheckpointTransitionalResponse>(nodeUrl, "/chains/${chainId}/levels/checkpoint", headers).toFinal()

    public suspend fun getSavepoint(chainId: String, headers: List<HttpHeader> = emptyList()): GetSavepointResponse =
        httpClient.get<GetSavepointTransitionalResponse>(nodeUrl, "/chains/${chainId}/levels/savepoint", headers).toFinal()

    // -- /config --

    public suspend fun getHistoryMode(headers: List<HttpHeader> = emptyList()): GetHistoryModeResponse =
        httpClient.get(nodeUrl, "/config/history_mode", headers)

    public suspend fun setLogging(activeSinks: String, headers: List<HttpHeader> = emptyList()): SetLoggingResponse =
        httpClient.put(nodeUrl, "/config/logging", headers, request = SetLoggingRequest(RpcUnistring.PlainUtf8(activeSinks)))

    public suspend fun getUserActivatedProtocolOverrides(headers: List<HttpHeader> = emptyList()): GetUserActivatedProtocolOverridesResponse =
        httpClient.get<GetUserActivatedProtocolOverridesTransitionalResponse>(nodeUrl, "/config/network/user_activated_protocol_overrides", headers).toFinal()

    public suspend fun getUserActivatedUpgrades(headers: List<HttpHeader> = emptyList()): GetUserActivatedUpgradesResponse =
        httpClient.get<GetUserActivatedUpgradesTransitionalResponse>(nodeUrl, "/config/network/user_activated_upgrades", headers).toFinal()

    // -- /injection --

    public suspend fun injectBlock(
        data: HexString,
        operations: List<List<RpcOperation>>,
        async: Boolean? = null,
        force: Boolean? = null,
        chain: ChainId? = null,
        headers: List<HttpHeader> = emptyList(),
    ): InjectBlockResponse =
        httpClient.post<InjectBlockRequest, InjectBlockTransitionalResponse>(
            nodeUrl,
            "/injection/block",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
                force?.takeIf { it }?.let { add("force" to null) }
                chain?.let { add("chain" to chain.base58) }
            },
            request = InjectBlockRequest(data, operations),
        ).toFinal()

    public suspend fun injectOperation(
        data: HexString,
        async: Boolean? = null,
        chain: ChainId? = null,
        headers: List<HttpHeader> = emptyList(),
    ): InjectOperationResponse =
        httpClient.post<InjectOperationRequest, InjectOperationTransitionalResponse>(
            nodeUrl,
            "/injection/operation",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
                chain?.let { add("chain" to chain.base58) }
            },
            request = InjectOperationRequest(data),
        ).toFinal()

    public suspend fun injectProtocol(
        expectedEnvVersion: UShort,
        components: List<RpcProtocolComponents>,
        async: Boolean? = null,
        headers: List<HttpHeader> = emptyList(),
    ): InjectProtocolResponse =
        httpClient.post<InjectProtocolRequest, InjectProtocolTransitionalResponse>(
            nodeUrl,
            "/injection/protocol",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
            },
            request = InjectProtocolRequest(expectedEnvVersion, components),
        ).toFinal()

    // ==== active (https://tezos.gitlab.io/active/rpc.html) ====

    // ==== converters ====

    // -- shell /chains --

    private fun GetBlocksTransitionalResponse.toFinal(): GetBlocksResponse =
        GetBlocksResponse(
            blocks.map { inner ->
                inner.map { it.toEncodedBlockHash() }
            }
        )

    private fun GetChainIdTransitionalResponse.toFinal(): GetChainIdResponse =
        GetChainIdResponse(chainId.toEncodedChainId())

    private fun GetInvalidBlocksTransitionalResponse.toFinal(): GetInvalidBlocksResponse =
        GetInvalidBlocksResponse(blocks.map { it.toFinal() })

    private fun GetInvalidBlockTransitionalResponse.toFinal(): GetInvalidBlockResponse =
        GetInvalidBlockResponse(block.toFinal())

    private fun RpcInvalidBlock<RpcBlockHash>.toFinal(): RpcInvalidBlock<BlockHash> =
        RpcInvalidBlock(block.toEncodedBlockHash(), level, errors)

    private fun GetCabooseTransitionalResponse.toFinal(): GetCabooseResponse =
        GetCabooseResponse(blockHash.toEncodedBlockHash(), level)

    private fun GetCheckpointTransitionalResponse.toFinal(): GetCheckpointResponse =
        GetCheckpointResponse(blockHash.toEncodedBlockHash(), level)

    private fun GetSavepointTransitionalResponse.toFinal(): GetSavepointResponse =
        GetSavepointResponse(blockHash.toEncodedBlockHash(), level)

    // -- shell /config --

    private fun GetUserActivatedProtocolOverridesTransitionalResponse.toFinal(): GetUserActivatedProtocolOverridesResponse =
        GetUserActivatedProtocolOverridesResponse(overrides.map { it.toFinal() })

    private fun GetUserActivatedUpgradesTransitionalResponse.toFinal(): GetUserActivatedUpgradesResponse =
        GetUserActivatedUpgradesResponse(upgrades.map { it.toFinal() })

    private fun RpcUserActivatedProtocolOverride<RpcProtocolHash>.toFinal(): RpcUserActivatedProtocolOverride<ProtocolHash> =
        RpcUserActivatedProtocolOverride(replacedProtocol.toEncodedProtocolHash(), replacementProtocol.toEncodedProtocolHash())

    private fun RpcUserActivatedUpgrade<RpcProtocolHash>.toFinal(): RpcUserActivatedUpgrade<ProtocolHash> =
        RpcUserActivatedUpgrade(level, replacementProtocol.toEncodedProtocolHash())

    // -- shell /injection --

    private fun InjectBlockTransitionalResponse.toFinal(): InjectBlockResponse =
        InjectBlockResponse(hash.toEncodedBlockHash())

    private fun InjectOperationTransitionalResponse.toFinal(): InjectOperationResponse =
        InjectOperationResponse(hash.toEncodedOperationHash())

    private fun InjectProtocolTransitionalResponse.toFinal(): InjectProtocolResponse =
        InjectProtocolResponse(hash.toEncodedProtocolHash())

    // -- common --

    private fun RpcBlockHash.toEncodedBlockHash(): BlockHash = toEncoded(BlockHash)
    private fun RpcChainId.toEncodedChainId(): ChainId = toEncoded(ChainId)
    private fun RpcOperationHash.toEncodedOperationHash(): OperationHash = toEncoded(OperationHash)
    private fun RpcProtocolHash.toEncodedProtocolHash(): ProtocolHash = toEncoded(ProtocolHash)

    private fun <E : Encoded<E>, K : Encoded.Kind<E>> RpcUnistring.toEncoded(kind: K): E =
        when (this) {
            is RpcUnistring.PlainUtf8 -> kind.createValue(string)
            is RpcUnistring.InvalidUtf8 -> encodedBytesCoder.decode(invalidUtf8String, kind)
        }
}