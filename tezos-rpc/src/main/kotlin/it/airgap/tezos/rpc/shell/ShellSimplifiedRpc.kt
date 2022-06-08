package it.airgap.tezos.rpc.shell

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants
import it.airgap.tezos.rpc.shell.chains.GetBlocksResponse
import it.airgap.tezos.rpc.shell.chains.GetBootstrappedStatusResponse
import it.airgap.tezos.rpc.shell.chains.GetChainIdResponse
import it.airgap.tezos.rpc.shell.injection.InjectOperationResponse

/**
 * Tezos protocol-independent RPCs.
 *
 * See [RPCs - Reference](https://tezos.gitlab.io/shell/rpc.html) for more details.
 */
public interface ShellSimplifiedRpc {

    // -- /chains --

    /**
     * Lists block hashes from [chainId], up to the last checkpoint, sorted with decreasing fitness.
     * Without arguments, it returns the head of the chain.
     * Optional arguments allow to return the list of predecessors of a given block or of a set of blocks.
     *
     * Optional query arguments:
     * * [length]  = `<uint>` : The requested number of predecessors to return (per request; see next argument).
     * * [head]    = `<block_hash>` : An empty argument requests blocks starting with the current head.
     *               A non-empty list allows to request one or more specific fragments of the chain.
     * * [minDate] = `<date>` : When [minDate] is provided, blocks with a timestamp before [minDate] are filtered out.
     *               However, if the `length` parameter is also provided, then up to that number of predecessors will be returned regardless of their date.
     *
     * [`GET /chains/<chain_id>/blocks?[length=<uint>]&(head=<block_hash>)*&[min_date=<date>]`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-blocks)
     */
    public suspend fun getBlocks(
        chainId: String = Constants.Chain.MAIN,
        length: UInt? = null,
        head: BlockHash? = null,
        minDate: String? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlocksResponse

    /**
     * Lists block hashes from [chainId], up to the last checkpoint, sorted with decreasing fitness.
     * Without arguments, it returns the head of the chain.
     * Optional arguments allow to return the list of predecessors of a given block or of a set of blocks.
     *
     * Optional query arguments:
     * * [length]  = `<uint>` : The requested number of predecessors to return (per request; see next argument).
     * * [head]    = `<block_hash>` : An empty argument requests blocks starting with the current head.
     *               A non-empty list allows to request one or more specific fragments of the chain.
     * * [minDate] = `<date>` : When [minDate] is provided, blocks with a timestamp before [minDate] are filtered out.
     *               However, if the `length` parameter is also provided, then up to that number of predecessors will be returned regardless of their date.
     *
     * [`GET /chains/<chain_id>/blocks?[length=<uint>]&(head=<block_hash>)*&[min_date=<date>]`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-blocks)
     */
    public suspend fun getBlocks(
        chainId: ChainId,
        length: UInt? = null,
        head: BlockHash? = null,
        minDate: String? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlocksResponse = getBlocks(chainId.base58, length, head, minDate, headers)

    /**
     * The chain unique identifier.
     *
     * [`GET /chains/<chain_id>/chain_id`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-chain-id)
     */
    public suspend fun getChainId(chainId: String = Constants.Chain.MAIN, headers: List<HttpHeader> = emptyList()): GetChainIdResponse

    /**
     * The chain unique identifier.
     *
     * [`GET /chains/<chain_id>/chain_id`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-chain-id)
     */
    public suspend fun getChainId(chainId: ChainId, headers: List<HttpHeader> = emptyList()): GetChainIdResponse = getChainId(chainId.base58, headers)

    /**
     * The bootstrap status of a chain.
     *
     * [`GET /chains/<chain_id>/is_bootstrapped`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-is-bootstrapped)
     */
    public suspend fun isBootstrapped(chainId: String = Constants.Chain.MAIN, headers: List<HttpHeader> = emptyList()): GetBootstrappedStatusResponse

    /**
     * The bootstrap status of a chain.
     *
     * [`GET /chains/<chain_id>/is_bootstrapped`](https://tezos.gitlab.io/shell/rpc.html#get-chains-chain-id-is-bootstrapped)
     */
    public suspend fun isBootstrapped(chainId: ChainId, headers: List<HttpHeader> = emptyList()): GetBootstrappedStatusResponse = isBootstrapped(chainId.base58, headers)

    // -- /injection --

    /**
     * Inject an operation in node and broadcast it. Returns the ID of the operation.
     * The `signedOperationContents` should be constructed using contextual RPCs from the latest block and signed by the client.
     * The injection of the operation will apply it on the current mempool context.
     *
     * This context may change at each operation injection or operation reception from peers.
     *
     * By default, the RPC will wait for the operation to be (pre-)validated before returning.
     * However, if ?async is true, the function returns immediately.
     * The optional ?chain parameter can be used to specify whether to inject on the test chain or the main chain.
     *
     * Optional query arguments:
     * * [async]
     * * [chain] = `<chain_id>
     *
     * [`POST /injection/operation?[async]&[chain=<chain_id>]`](https://tezos.gitlab.io/shell/rpc.html#post-injection-operation)
     */
    public suspend fun injectOperation(data: String, async: Boolean? = null, chain: ChainId? = null, headers: List<HttpHeader> = emptyList()): InjectOperationResponse
}