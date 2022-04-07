package it.airgap.tezos.rpc.shell

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.shell.chains.*
import it.airgap.tezos.rpc.shell.config.*
import it.airgap.tezos.rpc.shell.injection.InjectBlockResponse
import it.airgap.tezos.rpc.shell.injection.InjectOperationResponse
import it.airgap.tezos.rpc.shell.injection.InjectProtocolResponse
import it.airgap.tezos.rpc.shell.injection.Injection
import it.airgap.tezos.rpc.shell.monitor.*
import it.airgap.tezos.rpc.shell.network.*
import it.airgap.tezos.rpc.type.RpcAcl
import it.airgap.tezos.rpc.type.operation.RpcInjectableOperation
import it.airgap.tezos.rpc.type.p2p.RpcPeerState
import it.airgap.tezos.rpc.type.protocol.RpcProtocolComponent

// https://tezos.gitlab.io/shell/rpc.html
internal class ShellRpcClient(
    private val chains: Chains,
    private val config: Config,
    private val injection: Injection,
    private val monitor: Monitor,
    private val network: Network,
) : ShellRpc {

    // -- /chains --

    override suspend fun setBootstrapped(chainId: String, bootstrapped: Boolean, headers: List<HttpHeader>): SetBootstrappedResponse =
        chains.chainId(chainId).patch(bootstrapped, headers)

    override suspend fun getBlocks(chainId: String, length: UInt?, head: BlockHash?, minDate: String?, headers: List<HttpHeader>): GetBlocksResponse =
        chains.chainId(chainId).blocks.get(length, head, minDate, headers)

    override suspend fun getChainId(chainId: String, headers: List<HttpHeader>): GetChainIdResponse =
        chains.chainId(chainId).chainId.get(headers)

    override suspend fun getInvalidBlocks(chainId: String, headers: List<HttpHeader>): GetInvalidBlocksResponse =
        chains.chainId(chainId).invalidBlocks.get(headers)

    override suspend fun getInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader>): GetInvalidBlockResponse =
        chains.chainId(chainId).invalidBlocks.blockHash(blockHash).get(headers)

    override suspend fun deleteInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader>): DeleteInvalidBlockResponse =
        chains.chainId(chainId).invalidBlocks.blockHash(blockHash).delete(headers)

    override suspend fun isBootstrapped(chainId: String, headers: List<HttpHeader>): GetIsBootstrappedResponse =
        chains.chainId(chainId).isBootstrapped.get(headers)

    override suspend fun getCaboose(chainId: String, headers: List<HttpHeader>): GetCabooseResponse =
        chains.chainId(chainId).levels.caboose.get(headers)

    override suspend fun getCheckpoint(chainId: String, headers: List<HttpHeader>): GetCheckpointResponse =
        chains.chainId(chainId).levels.checkpoint.get(headers)

    override suspend fun getSavepoint(chainId: String, headers: List<HttpHeader>): GetSavepointResponse =
        chains.chainId(chainId).levels.savepoint.get(headers)

    // -- /config --

    override suspend fun getHistoryMode(headers: List<HttpHeader>): GetHistoryModeResponse =
        config.historyMode.get(headers)

    override suspend fun setLogging(activeSinks: String, headers: List<HttpHeader>): SetLoggingResponse =
        config.logging.put(activeSinks, headers)

    override suspend fun getUserActivatedProtocolOverrides(headers: List<HttpHeader>): GetUserActivatedProtocolOverridesResponse =
        config.network.userActivatedProtocolOverrides.get(headers)

    override suspend fun getUserActivatedUpgrades(headers: List<HttpHeader>): GetUserActivatedUpgradesResponse =
        config.network.userActivatedUpgrades.get(headers)

    // -- /injection --

    override suspend fun injectBlock(
        data: HexString,
        operations: List<List<RpcInjectableOperation>>,
        async: Boolean?,
        force: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
    ): InjectBlockResponse =
        injection.block.post(data, operations, async, force, chain, headers)

    override suspend fun injectOperation(
        data: HexString,
        async: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
    ): InjectOperationResponse =
        injection.operation.post(data, async, chain, headers)

    override suspend fun injectProtocol(
        expectedEnvVersion: UShort,
        components: List<RpcProtocolComponent>,
        async: Boolean?,
        headers: List<HttpHeader>,
    ): InjectProtocolResponse =
        injection.protocol.post(expectedEnvVersion, components, async, headers)

    // -- /monitor --

    override suspend fun monitorActiveChains(headers: List<HttpHeader>): MonitorActiveChainsResponse =
        monitor.activeChains.get(headers)

    override suspend fun monitorBootstrapped(headers: List<HttpHeader>): MonitorBootstrappedResponse =
        monitor.bootstrapped.get(headers)

    override suspend fun monitorHeads(chainId: String, nextProtocol: ProtocolHash?, headers: List<HttpHeader>): MonitorHeadResponse =
        monitor.heads.chainId(chainId).get(nextProtocol, headers)

    override suspend fun monitorProtocols(headers: List<HttpHeader>): MonitorProtocolsResponse =
        monitor.protocols.get(headers)

    override suspend fun monitorValidBlocks(protocol: ProtocolHash?, nextProtocol: ProtocolHash?, chain: String?, headers: List<HttpHeader>): MonitorValidBlocksResponse =
        monitor.validBlocks.get(protocol, nextProtocol, chain, headers)

    // -- /network --

    override suspend fun getConnections(headers: List<HttpHeader>): GetConnectionsResponse =
        network.connections.get(headers)

    override suspend fun getConnection(peerId: CryptoboxPublicKeyHash, headers: List<HttpHeader>): GetConnectionResponse =
        network.connections.peerId(peerId).get(headers)

    override suspend fun closeConnection(peerId: CryptoboxPublicKeyHash, wait: Boolean?, headers: List<HttpHeader>): CloseConnectionResponse =
        network.connections.peerId(peerId).delete(wait, headers)

    override suspend fun clearGreylist(headers: List<HttpHeader>): ClearGreylistResponse =
        network.greylist.delete(headers)

    override suspend fun getGreylistedIPs(headers: List<HttpHeader>): GetGreylistedIPsResponse =
        network.greylist.ips.get(headers)

    override suspend fun getLastGreylistedPeers(headers: List<HttpHeader>): GetLastGreylistedPeersResponse =
        network.greylist.peers.get(headers)

    override suspend fun getLogs(headers: List<HttpHeader>): GetLogResponse =
        network.log.get(headers)

    override suspend fun getPeers(filter: RpcPeerState?, headers: List<HttpHeader>): GetPeersResponse =
        network.peers.get(filter, headers)

    override suspend fun getPeer(peerId: CryptoboxPublicKeyHash, headers: List<HttpHeader>): GetPeerResponse =
        network.peers.peerId(peerId).get(headers)

    override suspend fun changePeerPermissions(peerId: CryptoboxPublicKeyHash, acl: RpcAcl, headers: List<HttpHeader>): ChangePeerPermissionResponse =
        network.peers.peerId(peerId).patch(acl, headers)

    override suspend fun isPeerBanned(peerId: CryptoboxPublicKeyHash, headers: List<HttpHeader>): GetPeerBannedStatusResponse =
        network.peers.peerId(peerId).banned.get(headers)

    override suspend fun getPeerEvents(peerId: CryptoboxPublicKeyHash, monitor: Boolean?, headers: List<HttpHeader>): GetPeerEventsResponse =
        network.peers.peerId(peerId).log.get(monitor, headers)
}