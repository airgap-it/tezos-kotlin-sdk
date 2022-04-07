package it.airgap.tezos.rpc.shell

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.shell.chains.GetBlocksResponse
import it.airgap.tezos.rpc.shell.chains.GetChainIdResponse
import it.airgap.tezos.rpc.shell.chains.GetIsBootstrappedResponse
import it.airgap.tezos.rpc.shell.injection.InjectOperationResponse
import it.airgap.tezos.rpc.shell.injection.Injection

// https://tezos.gitlab.io/shell/rpc.html
internal class ShellSimplifiedRpcClient(
    private val chains: Chains,
    private val injection: Injection,
) : ShellSimplifiedRpc {

    // -- /chains --

    override suspend fun getBlocks(chainId: String, length: UInt?, head: BlockHash?, minDate: String?, headers: List<HttpHeader>): GetBlocksResponse =
        chains.chainId(chainId).blocks.get(length, head, minDate, headers)

    override suspend fun getChainId(chainId: String, headers: List<HttpHeader>): GetChainIdResponse =
        chains.chainId(chainId).chainId.get(headers)

    override suspend fun isBootstrapped(chainId: String, headers: List<HttpHeader>): GetIsBootstrappedResponse =
        chains.chainId(chainId).isBootstrapped.get(headers)

    // -- /injection --

    override suspend fun injectOperation(
        data: HexString,
        async: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
    ): InjectOperationResponse =
        injection.operation.post(data, async, chain, headers)
}