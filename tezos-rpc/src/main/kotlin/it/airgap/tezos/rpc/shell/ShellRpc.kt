package it.airgap.tezos.rpc.shell

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants
import it.airgap.tezos.rpc.shell.chains.*
import it.airgap.tezos.rpc.shell.config.GetHistoryModeResponse
import it.airgap.tezos.rpc.shell.config.GetUserActivatedProtocolOverridesResponse
import it.airgap.tezos.rpc.shell.config.GetUserActivatedUpgradesResponse
import it.airgap.tezos.rpc.shell.config.SetLoggingResponse
import it.airgap.tezos.rpc.shell.injection.InjectBlockResponse
import it.airgap.tezos.rpc.shell.injection.InjectOperationResponse
import it.airgap.tezos.rpc.shell.injection.InjectProtocolResponse
import it.airgap.tezos.rpc.shell.monitor.*
import it.airgap.tezos.rpc.shell.network.*
import it.airgap.tezos.rpc.type.RpcAcl
import it.airgap.tezos.rpc.type.operation.RpcInjectableOperation
import it.airgap.tezos.rpc.type.p2p.RpcPeerState
import it.airgap.tezos.rpc.type.protocol.RpcProtocolComponent

// https://tezos.gitlab.io/shell/rpc.html
public interface ShellRpc {

    // -- /chains --

    public suspend fun setBootstrapped(chainId: String = Constants.Chain.MAIN, bootstrapped: Boolean, headers: List<HttpHeader> = emptyList()): SetBootstrappedResponse
    public suspend fun setBootstrapped(chainId: ChainId, bootstrapped: Boolean, headers: List<HttpHeader> = emptyList()): SetBootstrappedResponse =
        setBootstrapped(chainId.base58, bootstrapped, headers)

    public suspend fun getBlocks(chainId: String = Constants.Chain.MAIN, length: UInt? = null, head: BlockHash? = null, minDate: String? = null, headers: List<HttpHeader> = emptyList()): GetBlocksResponse
    public suspend fun getBlocks(chainId: ChainId, length: UInt? = null, head: BlockHash? = null, minDate: String? = null, headers: List<HttpHeader> = emptyList()): GetBlocksResponse =
        getBlocks(chainId.base58, length, head, minDate, headers)

    public suspend fun getChainId(chainId: String = Constants.Chain.MAIN, headers: List<HttpHeader> = emptyList()): GetChainIdResponse
    public suspend fun getChainId(chainId: ChainId, headers: List<HttpHeader> = emptyList()): GetChainIdResponse =
        getChainId(chainId.base58, headers)

    public suspend fun getInvalidBlocks(chainId: String = Constants.Chain.MAIN, headers: List<HttpHeader> = emptyList()): GetInvalidBlocksResponse
    public suspend fun getInvalidBlocks(chainId: ChainId, headers: List<HttpHeader> = emptyList()): GetInvalidBlocksResponse =
        getInvalidBlocks(chainId.base58, headers)

    public suspend fun getInvalidBlock(chainId: String = Constants.Chain.MAIN, blockHash: BlockHash, headers: List<HttpHeader> = emptyList()): GetInvalidBlockResponse
    public suspend fun getInvalidBlock(chainId: ChainId, blockHash: BlockHash, headers: List<HttpHeader> = emptyList()): GetInvalidBlockResponse =
        getInvalidBlock(chainId.base58, blockHash, headers)

    public suspend fun deleteInvalidBlock(chainId: String = Constants.Chain.MAIN, blockHash: BlockHash, headers: List<HttpHeader> = emptyList()): DeleteInvalidBlockResponse
    public suspend fun deleteInvalidBlock(chainId: ChainId, blockHash: BlockHash, headers: List<HttpHeader> = emptyList()): DeleteInvalidBlockResponse =
        deleteInvalidBlock(chainId.base58, blockHash, headers)

    public suspend fun isBootstrapped(chainId: String = Constants.Chain.MAIN, headers: List<HttpHeader> = emptyList()): GetIsBootstrappedResponse
    public suspend fun isBootstrapped(chainId: ChainId, headers: List<HttpHeader> = emptyList()): GetIsBootstrappedResponse =
        isBootstrapped(chainId.base58, headers)

    public suspend fun getCaboose(chainId: String = Constants.Chain.MAIN, headers: List<HttpHeader> = emptyList()): GetCabooseResponse
    public suspend fun getCaboose(chainId: ChainId, headers: List<HttpHeader> = emptyList()): GetCabooseResponse =
        getCaboose(chainId.base58, headers)

    public suspend fun getCheckpoint(chainId: String = Constants.Chain.MAIN, headers: List<HttpHeader> = emptyList()): GetCheckpointResponse
    public suspend fun getCheckpoint(chainId: ChainId, headers: List<HttpHeader> = emptyList()): GetCheckpointResponse =
        getCheckpoint(chainId.base58, headers)

    public suspend fun getSavepoint(chainId: String = Constants.Chain.MAIN, headers: List<HttpHeader> = emptyList()): GetSavepointResponse
    public suspend fun getSavepoint(chainId: ChainId, headers: List<HttpHeader> = emptyList()): GetSavepointResponse =
        getSavepoint(chainId.base58, headers)

    // -- /config --

    public suspend fun getHistoryMode(headers: List<HttpHeader> = emptyList()): GetHistoryModeResponse
    public suspend fun setLogging(activeSinks: String, headers: List<HttpHeader> = emptyList()): SetLoggingResponse

    public suspend fun getUserActivatedProtocolOverrides(headers: List<HttpHeader> = emptyList()): GetUserActivatedProtocolOverridesResponse
    public suspend fun getUserActivatedUpgrades(headers: List<HttpHeader> = emptyList()): GetUserActivatedUpgradesResponse

    // -- /injection --

    public suspend fun injectBlock(data: HexString, operations: List<List<RpcInjectableOperation>>, async: Boolean? = null, force: Boolean? = null, chain: ChainId? = null, headers: List<HttpHeader> = emptyList()): InjectBlockResponse
    public suspend fun injectOperation(data: HexString, async: Boolean? = null, chain: ChainId? = null, headers: List<HttpHeader> = emptyList()): InjectOperationResponse
    public suspend fun injectProtocol(expectedEnvVersion: UShort, components: List<RpcProtocolComponent>, async: Boolean? = null, headers: List<HttpHeader> = emptyList()): InjectProtocolResponse

    // -- /monitor --

    public suspend fun monitorActiveChains(headers: List<HttpHeader> = emptyList()): MonitorActiveChainsResponse
    public suspend fun monitorBootstrapped(headers: List<HttpHeader> = emptyList()): MonitorBootstrappedResponse
    public suspend fun monitorHeads(chainId: String = Constants.Chain.MAIN, nextProtocol: ProtocolHash? = null, headers: List<HttpHeader> = emptyList()): MonitorHeadResponse
    public suspend fun monitorProtocols(headers: List<HttpHeader> = emptyList()): MonitorProtocolsResponse

    public suspend fun monitorValidBlocks(protocol: ProtocolHash? = null, nextProtocol: ProtocolHash? = null, chain: String? = null, headers: List<HttpHeader> = emptyList()): MonitorValidBlocksResponse
    public suspend fun monitorValidBlocks(protocol: ProtocolHash? = null, nextProtocol: ProtocolHash? = null, chain: ChainId? = null, headers: List<HttpHeader> = emptyList()): MonitorValidBlocksResponse =
        monitorValidBlocks(protocol, nextProtocol, chain?.base58, headers)

    // -- /network --

    public suspend fun getConnections(headers: List<HttpHeader> = emptyList()): GetConnectionsResponse
    public suspend fun getConnection(peerId: CryptoboxPublicKeyHash, headers: List<HttpHeader> = emptyList()): GetConnectionResponse
    public suspend fun closeConnection(peerId: CryptoboxPublicKeyHash, wait: Boolean? = null, headers: List<HttpHeader> = emptyList()): CloseConnectionResponse

    public suspend fun clearGreylist(headers: List<HttpHeader> = emptyList()): ClearGreylistResponse
    public suspend fun getGreylistedIPs(headers: List<HttpHeader> = emptyList()): GetGreylistedIPsResponse
    public suspend fun getLastGreylistedPeers(headers: List<HttpHeader> = emptyList()): GetLastGreylistedPeersResponse

    public suspend fun getLogs(headers: List<HttpHeader> = emptyList()): GetLogResponse

    public suspend fun getPeers(filter: RpcPeerState? = null, headers: List<HttpHeader> = emptyList()): GetPeersResponse
    public suspend fun getPeer(peerId: CryptoboxPublicKeyHash, headers: List<HttpHeader> = emptyList()): GetPeerResponse
    public suspend fun changePeerPermissions(peerId: CryptoboxPublicKeyHash, acl: RpcAcl, headers: List<HttpHeader> = emptyList()): ChangePeerPermissionResponse
    public suspend fun isPeerBanned(peerId: CryptoboxPublicKeyHash, headers: List<HttpHeader> = emptyList()): GetPeerBannedStatusResponse
    public suspend fun getPeerEvents(peerId: CryptoboxPublicKeyHash, monitor: Boolean? = null, headers: List<HttpHeader> = emptyList()): GetPeerEventsResponse
}