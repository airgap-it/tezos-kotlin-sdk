package it.airgap.tezos.rpc.shell

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.shell.data.*
import it.airgap.tezos.rpc.type.RpcAcl
import it.airgap.tezos.rpc.type.RpcProtocolComponent
import it.airgap.tezos.rpc.type.operation.RpcInjectableOperation
import it.airgap.tezos.rpc.type.p2p.RpcPeerState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class ShellRpcClient(
    private val nodeUrl: String,
    private val httpClient: HttpClient,
    private val json: Json,
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
        httpClient.get(
            nodeUrl,
            "/chains/$chainId",
            headers,
            parameters = buildList {
                length?.let { add("length" to length.toString()) }
                head?.let { add("head" to head.base58) }
                minDate?.let { add("min_date" to minDate) }
            }
        )

    override suspend fun getChainId(chainId: String, headers: List<HttpHeader>): GetChainIdResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/chain_id", headers)

    override suspend fun getInvalidBlocks(chainId: String, headers: List<HttpHeader>): GetInvalidBlocksResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/invalid_blocks", headers)

    override suspend fun getInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader>): GetInvalidBlockResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/invalid_blocks/${blockHash.base58}", headers)

    override suspend fun deleteInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader>): DeleteInvalidBlockResponse =
        httpClient.delete(nodeUrl, "/chains/${chainId}/invalid_blocks/${blockHash.base58}", headers)

    override suspend fun isBootstrapped(chainId: String, headers: List<HttpHeader>): IsBootstrappedResponse =
        httpClient.get(nodeUrl, "/chains/${chainId}/is_bootstrapped", headers)

    override suspend fun getCaboose(chainId: String, headers: List<HttpHeader>): GetCabooseResponse =
        httpClient.get(nodeUrl, "/chains/${chainId}/levels/caboose", headers)

    override suspend fun getCheckpoint(chainId: String, headers: List<HttpHeader>): GetCheckpointResponse =
        httpClient.get(nodeUrl, "/chains/${chainId}/levels/checkpoint", headers)

    override suspend fun getSavepoint(chainId: String, headers: List<HttpHeader>): GetSavepointResponse =
        httpClient.get(nodeUrl, "/chains/${chainId}/levels/savepoint", headers)

    // -- /config --

    override suspend fun getHistoryMode(headers: List<HttpHeader>): GetHistoryModeResponse =
        httpClient.get(nodeUrl, "/config/history_mode", headers)

    override suspend fun setLogging(activeSinks: String, headers: List<HttpHeader>): SetLoggingResponse =
        httpClient.put(nodeUrl, "/config/logging", headers, request = SetLoggingRequest(activeSinks))

    override suspend fun getUserActivatedProtocolOverrides(headers: List<HttpHeader>): GetUserActivatedProtocolOverridesResponse =
        httpClient.get(nodeUrl, "/config/network/user_activated_protocol_overrides", headers)

    override suspend fun getUserActivatedUpgrades(headers: List<HttpHeader>): GetUserActivatedUpgradesResponse =
        httpClient.get(nodeUrl, "/config/network/user_activated_upgrades", headers)

    // -- /injection --

    override suspend fun injectBlock(
        data: HexString,
        operations: List<List<RpcInjectableOperation>>,
        async: Boolean?,
        force: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
    ): InjectBlockResponse =
        httpClient.post(
            nodeUrl,
            "/injection/block",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
                force?.takeIf { it }?.let { add("force" to null) }
                chain?.let { add("chain" to chain.base58) }
            },
            request = InjectBlockRequest(data, operations),
        )

    override suspend fun injectOperation(
        data: HexString,
        async: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
    ): InjectOperationResponse =
        httpClient.post(
            nodeUrl,
            "/injection/operation",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
                chain?.let { add("chain" to chain.base58) }
            },
            request = InjectOperationRequest(data),
        )

    override suspend fun injectProtocol(
        expectedEnvVersion: UShort,
        components: List<RpcProtocolComponent>,
        async: Boolean?,
        headers: List<HttpHeader>,
    ): InjectProtocolResponse =
        httpClient.post(
            nodeUrl,
            "/injection/protocol",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
            },
            request = InjectProtocolRequest(expectedEnvVersion, components),
        )

    // -- /monitor --

    override suspend fun monitorActiveChains(headers: List<HttpHeader>): MonitorActiveChainsResponse =
        httpClient.get(nodeUrl, "/monitor/active_chains", headers)

    override suspend fun monitorBootstrapped(headers: List<HttpHeader>): MonitorBootstrappedResponse =
        httpClient.get(nodeUrl, "/monitor/bootstrapped", headers)

    override suspend fun monitorHeads(
        chainId: ChainId,
        nextProtocol: ProtocolHash?,
        headers: List<HttpHeader>,
    ): MonitorHeadsResponse =
        httpClient.get(
            nodeUrl,
            "/monitor/heads/$chainId",
            headers,
            parameters = buildList {
                nextProtocol?.let { add("next_protocol" to it.base58) }
            },
        )

    override suspend fun monitorProtocols(headers: List<HttpHeader>): MonitorProtocolsResponse =
        httpClient.get(nodeUrl, "/monitor/protocols", headers)

    override suspend fun monitorValidBlocks(
        protocol: ProtocolHash?,
        nextProtocol: ProtocolHash?,
        chain: ChainId?,
        headers: List<HttpHeader>,
    ): MonitorValidBlocksResponse =
        httpClient.get(
            nodeUrl,
            "/monitor/valid_blocks",
            headers,
            parameters = buildList {
                protocol?.let { add("protocol" to it.base58) }
                nextProtocol?.let { add("next_protocol" to it.base58) }
                chain?.let { add("chain" to it.base58) }
            },
        )

    // -- /network --
    override suspend fun getConnections(headers: List<HttpHeader>): GetConnectionsResponse =
        httpClient.get(
            nodeUrl,
            "/network/connections",
            headers,
        )

    override suspend fun getConnection(
        peerId: CryptoboxPublicKeyHash,
        headers: List<HttpHeader>,
    ): GetConnectionResponse =
        httpClient.get(
            nodeUrl,
            "/network/connections/${peerId.base58}",
            headers,
        )

    override suspend fun closeConnection(peerId: CryptoboxPublicKeyHash, wait: Boolean?, headers: List<HttpHeader>): CloseConnectionResponse =
        httpClient.delete(
            nodeUrl,
            "/network/connections/${peerId.base58}",
            headers,
            parameters = buildList {
                wait?.takeIf { it }?.let { add("wait" to null) }
            },
        )

    override suspend fun clearGreylist(headers: List<HttpHeader>): ClearGreylistResponse =
        httpClient.delete(nodeUrl, "/network/greylist", headers, )

    override suspend fun getGreylistedIPs(headers: List<HttpHeader>): GetGreylistedIPsResponse =
        httpClient.get(
            nodeUrl,
            "/network/greylist/ips",
            headers,
        )


    override suspend fun getLastGreylistedPeers(headers: List<HttpHeader>): GetLastGreylistedPeersResponse =
        httpClient.get(
            nodeUrl,
            "/network/greylist/peers",
            headers,
        )

    override suspend fun getLogs(headers: List<HttpHeader>): GetLogResponse =
        httpClient.get(
            nodeUrl,
            "/network/log",
            headers,
        )

    override suspend fun getPeers(filter: RpcPeerState?, headers: List<HttpHeader>): GetPeersResponse =
        httpClient.get(
            nodeUrl,
            "/network/peers",
            headers,
            parameters = buildList {
                filter?.let { add("filter" to json.encodeToString(it)) }
            },
        )

    override suspend fun getPeer(peerId: CryptoboxPublicKeyHash, headers: List<HttpHeader>): GetPeerResponse =
        httpClient.get(
            nodeUrl,
            "/network/peers/${peerId.base58}",
            headers,
        )

    override suspend fun changePeerPermissions(
        peerId: CryptoboxPublicKeyHash,
        acl: RpcAcl,
        headers: List<HttpHeader>,
    ): ChangePeerPermissionResponse =
        httpClient.patch(
            nodeUrl,
            "/network/peers/${peerId.base58}",
            headers,
            request = ChangePeerPermissionRequest(acl),
        )

    override suspend fun isPeerBanned(peerId: CryptoboxPublicKeyHash, headers: List<HttpHeader>): BannedPeerResponse =
        httpClient.get(nodeUrl, "/network/peers/${peerId.base58}/banned", headers)

    override suspend fun getPeerEvents(
        peerId: CryptoboxPublicKeyHash,
        monitor: Boolean?,
        headers: List<HttpHeader>,
    ): GetPeerEventsResponse =
        httpClient.get(
            nodeUrl,
            "/network/peers/${peerId.base58}/log",
            headers,
            parameters = buildList {
                monitor?.takeIf { it }?.let { add("monitor" to null) }
            }
        )
}