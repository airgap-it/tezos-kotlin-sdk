package it.airgap.tezos.rpc.shell

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.shell.chains.GetBlocksResponse
import it.airgap.tezos.rpc.shell.chains.GetBootstrappedStatusResponse
import it.airgap.tezos.rpc.shell.chains.GetChainIdResponse
import it.airgap.tezos.rpc.shell.injection.InjectOperationResponse
import it.airgap.tezos.rpc.shell.injection.Injection

// https://tezos.gitlab.io/shell/rpc.html
internal class ShellSimplifiedRpcClient(
    private val chains: Chains,
    private val injection: Injection,
) : ShellSimplifiedRpc {

    // -- /chains --

    override suspend fun getBlocks(
        chainId: String,
        length: UInt?,
        head: BlockHash?,
        minDate: String?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBlocksResponse =
        chains(chainId).blocks.get(length, head, minDate, headers, requestTimeout, connectionTimeout)

    override suspend fun getChainId(
        chainId: String,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetChainIdResponse =
        chains(chainId).chainId.get(headers, requestTimeout, connectionTimeout)

    override suspend fun isBootstrapped(
        chainId: String,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBootstrappedStatusResponse =
        chains(chainId).isBootstrapped.get(headers, requestTimeout, connectionTimeout)

    // -- /injection --

    override suspend fun injectOperation(
        data: String,
        async: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): InjectOperationResponse =
        injection.operation.post(data, async, chain, headers, requestTimeout, connectionTimeout)
}