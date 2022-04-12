package it.airgap.tezos.rpc.shell

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants
import it.airgap.tezos.rpc.shell.chains.GetBlocksResponse
import it.airgap.tezos.rpc.shell.chains.GetBootstrappedStatusResponse
import it.airgap.tezos.rpc.shell.chains.GetChainIdResponse
import it.airgap.tezos.rpc.shell.injection.InjectOperationResponse

// https://tezos.gitlab.io/shell/rpc.html
public interface ShellSimplifiedRpc {

    // -- /chains --

    public suspend fun getBlocks(chainId: String = Constants.Chain.MAIN, length: UInt? = null, head: BlockHash? = null, minDate: String? = null, headers: List<HttpHeader> = emptyList()): GetBlocksResponse
    public suspend fun getBlocks(chainId: ChainId, length: UInt? = null, head: BlockHash? = null, minDate: String? = null, headers: List<HttpHeader> = emptyList()): GetBlocksResponse = getBlocks(chainId.base58, length, head, minDate, headers)

    public suspend fun getChainId(chainId: String = Constants.Chain.MAIN, headers: List<HttpHeader> = emptyList()): GetChainIdResponse
    public suspend fun getChainId(chainId: ChainId, headers: List<HttpHeader> = emptyList()): GetChainIdResponse = getChainId(chainId.base58, headers)

    public suspend fun isBootstrapped(chainId: String = Constants.Chain.MAIN, headers: List<HttpHeader> = emptyList()): GetBootstrappedStatusResponse
    public suspend fun isBootstrapped(chainId: ChainId, headers: List<HttpHeader> = emptyList()): GetBootstrappedStatusResponse = isBootstrapped(chainId.base58, headers)

    // -- /injection --

    public suspend fun injectOperation(data: HexString, async: Boolean? = null, chain: ChainId? = null, headers: List<HttpHeader> = emptyList()): InjectOperationResponse
}