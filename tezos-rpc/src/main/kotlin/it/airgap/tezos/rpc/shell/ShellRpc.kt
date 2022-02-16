package it.airgap.tezos.rpc.shell

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.shell.data.*
import it.airgap.tezos.rpc.type.RpcOperation
import it.airgap.tezos.rpc.type.RpcProtocolComponent

// https://tezos.gitlab.io/shell/rpc.html
public interface ShellRpc {

    // -- /chains --

    public suspend fun setBootstrapped(chainId: String, bootstrapped: Boolean, headers: List<HttpHeader> = emptyList()): SetBootstrappedResponse
    public suspend fun getBlocks(chainId: String, length: UInt? = null, head: BlockHash? = null, minDate: String? = null, headers: List<HttpHeader> = emptyList()): GetBlocksResponse
    public suspend fun getChainId(chainId: String, headers: List<HttpHeader> = emptyList()): GetChainIdResponse
    public suspend fun getInvalidBlocks(chainId: String, headers: List<HttpHeader> = emptyList()): GetInvalidBlocksResponse
    public suspend fun getInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader> = emptyList()): GetInvalidBlockResponse
    public suspend fun deleteInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader> = emptyList()): DeleteInvalidBlockResponse
    public suspend fun isBootstrapped(chainId: String, headers: List<HttpHeader> = emptyList()): IsBootstrappedResponse
    public suspend fun getCaboose(chainId: String, headers: List<HttpHeader> = emptyList()): GetCabooseResponse
    public suspend fun getCheckpoint(chainId: String, headers: List<HttpHeader> = emptyList()): GetCheckpointResponse
    public suspend fun getSavepoint(chainId: String, headers: List<HttpHeader> = emptyList()): GetSavepointResponse

    // -- /config --

    public suspend fun getHistoryMode(headers: List<HttpHeader> = emptyList()): GetHistoryModeResponse
    public suspend fun setLogging(activeSinks: String, headers: List<HttpHeader> = emptyList()): SetLoggingResponse
    public suspend fun getUserActivatedProtocolOverrides(headers: List<HttpHeader> = emptyList()): GetUserActivatedProtocolOverridesResponse
    public suspend fun getUserActivatedUpgrades(headers: List<HttpHeader> = emptyList()): GetUserActivatedUpgradesResponse

    // -- /injection --

    public suspend fun injectBlock(data: HexString, operations: List<List<RpcOperation>>, async: Boolean? = null, force: Boolean? = null, chain: ChainId? = null, headers: List<HttpHeader> = emptyList()): InjectBlockResponse
    public suspend fun injectOperation(data: HexString, async: Boolean? = null, chain: ChainId? = null, headers: List<HttpHeader> = emptyList()): InjectOperationResponse
    public suspend fun injectProtocol(expectedEnvVersion: UShort, components: List<RpcProtocolComponent>, async: Boolean? = null, headers: List<HttpHeader> = emptyList()): InjectProtocolResponse

    // -- /monitor --

    public suspend fun monitorActiveChains(headers: List<HttpHeader> = emptyList()): MonitorActiveChainsResponse
    public suspend fun monitorBootstrapped(headers: List<HttpHeader> = emptyList()): MonitorBootstrappedResponse
    public suspend fun monitorHeads(chainId: ChainId, nextProtocol: ProtocolHash? = null, headers: List<HttpHeader> = emptyList()): MonitorHeadsResponse
    public suspend fun monitorProtocols(headers: List<HttpHeader> = emptyList()): MonitorProtocolsResponse
    public suspend fun monitorValidBlocks(protocol: ProtocolHash? = null, nextProtocol: ProtocolHash? = null, chain: ChainId? = null, headers: List<HttpHeader> = emptyList()): MonitorValidBlocksResponse
}