package it.airgap.tezos.rpc.shell

import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.shell.data.*
import it.airgap.tezos.rpc.type.*

internal class ShellRpcClient(
    private val nodeUrl: String,
    private val httpClient: HttpClient,
    private val encodedBytesCoder: EncodedBytesCoder,
) : ShellRpc {

    // ==== RPC (https://tezos.gitlab.io/shell/rpc.html) ====

    // -- /chains --

    override suspend fun setBootstrapped(
        chainId: String,
        bootstrapped: Boolean,
        headers: List<HttpHeader>,
    ): SetBootstrappedResponse =
        httpClient.patch(nodeUrl, "/chains/$chainId", headers, request = SetBootstrappedRequest(bootstrapped))

    override suspend fun getBlocks(
        chainId: String,
        length: UInt?,
        head: BlockHash?,
        minDate: String?,
        headers: List<HttpHeader>,
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

    override suspend fun getChainId(chainId: String, headers: List<HttpHeader>): GetChainIdResponse =
        httpClient.get<GetChainIdTransitionalResponse>(nodeUrl, "/chains/$chainId/chain_id", headers).toFinal()

    override suspend fun getInvalidBlocks(chainId: String, headers: List<HttpHeader>): GetInvalidBlocksResponse =
        httpClient.get<GetInvalidBlocksTransitionalResponse>(nodeUrl, "/chains/$chainId/invalid_blocks", headers).toFinal()

    override suspend fun getInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader>): GetInvalidBlockResponse =
        httpClient.get<GetInvalidBlockTransitionalResponse>(nodeUrl, "/chains/$chainId/invalid_blocks/${blockHash.base58}", headers).toFinal()

    override suspend fun deleteInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader>): DeleteInvalidBlockResponse =
        httpClient.delete(nodeUrl, "/chains/${chainId}/invalid_blocks/${blockHash.base58}", headers)

    override suspend fun isBootstrapped(chainId: String, headers: List<HttpHeader>): IsBootstrappedResponse =
        httpClient.get(nodeUrl, "/chains/${chainId}/is_bootstrapped", headers)

    override suspend fun getCaboose(chainId: String, headers: List<HttpHeader>): GetCabooseResponse =
        httpClient.get<GetCabooseTransitionalResponse>(nodeUrl, "/chains/${chainId}/levels/caboose", headers).toFinal()

    override suspend fun getCheckpoint(chainId: String, headers: List<HttpHeader>): GetCheckpointResponse =
        httpClient.get<GetCheckpointTransitionalResponse>(nodeUrl, "/chains/${chainId}/levels/checkpoint", headers).toFinal()

    override suspend fun getSavepoint(chainId: String, headers: List<HttpHeader>): GetSavepointResponse =
        httpClient.get<GetSavepointTransitionalResponse>(nodeUrl, "/chains/${chainId}/levels/savepoint", headers).toFinal()

    // -- /config --

    override suspend fun getHistoryMode(headers: List<HttpHeader>): GetHistoryModeResponse =
        httpClient.get(nodeUrl, "/config/history_mode", headers)

    override suspend fun setLogging(activeSinks: String, headers: List<HttpHeader>): SetLoggingResponse =
        httpClient.put(nodeUrl, "/config/logging", headers, request = SetLoggingRequest(RpcUnistring.PlainUtf8(activeSinks)))

    override suspend fun getUserActivatedProtocolOverrides(headers: List<HttpHeader>): GetUserActivatedProtocolOverridesResponse =
        httpClient.get<GetUserActivatedProtocolOverridesTransitionalResponse>(nodeUrl, "/config/network/user_activated_protocol_overrides", headers).toFinal()

    override suspend fun getUserActivatedUpgrades(headers: List<HttpHeader>): GetUserActivatedUpgradesResponse =
        httpClient.get<GetUserActivatedUpgradesTransitionalResponse>(nodeUrl, "/config/network/user_activated_upgrades", headers).toFinal()

    // -- /injection --

    override suspend fun injectBlock(
        data: HexString,
        operations: List<List<RpcOperation>>,
        async: Boolean?,
        force: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
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

    override suspend fun injectOperation(
        data: HexString,
        async: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
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

    override suspend fun injectProtocol(
        expectedEnvVersion: UShort,
        components: List<RpcProtocolComponents>,
        async: Boolean?,
        headers: List<HttpHeader>,
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

    // ==== converters ====

    // -- /chains --

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

    // -- /config --

    private fun GetUserActivatedProtocolOverridesTransitionalResponse.toFinal(): GetUserActivatedProtocolOverridesResponse =
        GetUserActivatedProtocolOverridesResponse(overrides.map { it.toFinal() })

    private fun GetUserActivatedUpgradesTransitionalResponse.toFinal(): GetUserActivatedUpgradesResponse =
        GetUserActivatedUpgradesResponse(upgrades.map { it.toFinal() })

    private fun RpcUserActivatedProtocolOverride<RpcProtocolHash>.toFinal(): RpcUserActivatedProtocolOverride<ProtocolHash> =
        RpcUserActivatedProtocolOverride(replacedProtocol.toEncodedProtocolHash(), replacementProtocol.toEncodedProtocolHash())

    private fun RpcUserActivatedUpgrade<RpcProtocolHash>.toFinal(): RpcUserActivatedUpgrade<ProtocolHash> =
        RpcUserActivatedUpgrade(level, replacementProtocol.toEncodedProtocolHash())

    // -- /injection --

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