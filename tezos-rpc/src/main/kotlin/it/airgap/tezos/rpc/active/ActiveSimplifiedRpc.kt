package it.airgap.tezos.rpc.active

import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.active.block.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import it.airgap.tezos.rpc.type.operation.RpcApplicableOperation
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

/**
 * Tezos protocol specific RPCs.
 *
 * See [RPCs - Reference](https://tezos.gitlab.io/active/rpc.html) for more details.
 */
public interface ActiveSimplifiedRpc {

    // -- ../<block_id> --

    /**
     * All the information about a block. The associated metadata may not be present depending on the history mode and block's distance from the head.
     *
     * [`GET ../<block_id>`](https://tezos.gitlab.io/active/rpc.html#get-block-id)
     */
    public suspend fun getBlock(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockResponse

    /**
     * All the information about a block. The associated metadata may not be present depending on the history mode and block's distance from the head.
     *
     * [`GET ../<block_id>`](https://tezos.gitlab.io/active/rpc.html#get-block-id)
     */
    public suspend fun getBlock(
        chainId: ChainId,
        blockId: Int,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockResponse = getBlock(chainId.base58, blockId.toString(), headers)

    /**
     * All the information about a block. The associated metadata may not be present depending on the history mode and block's distance from the head.
     *
     * [`GET ../<block_id>`](https://tezos.gitlab.io/active/rpc.html#get-block-id)
     */
    public suspend fun getBlock(
        chainId: ChainId,
        blockId: BlockHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockResponse = getBlock(chainId.base58, blockId.base58, headers)

    // -- ../<block_id>/context/big_maps --

    /**
     * Get the (optionally paginated) list of values in a big map. Order of values is unspecified, but is guaranteed to be consistent.
     *
     * Optional query arguments:
     * * [offset] = `<uint>` : Skip the first [offset] values. Useful in combination with [length] for pagination.
     * * [length] = `<uint>` : Only retrieve [length] values. Useful in combination with [offset] for pagination.
     *
     * [`GET ../<block_id>/context/big_maps/<big_map_id>?[offset=<uint>]&[length=<uint>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-big-maps-big-map-id)
     */
    public suspend fun getBigMap(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        bigMapId: String,
        offset: UInt? = null,
        length: UInt? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapResponse

    /**
     * Get the (optionally paginated) list of values in a big map. Order of values is unspecified, but is guaranteed to be consistent.
     *
     * Optional query arguments:
     * * [offset] = `<uint>` : Skip the first [offset] values. Useful in combination with [length] for pagination.
     * * [length] = `<uint>` : Only retrieve [length] values. Useful in combination with [offset] for pagination.
     *
     * [`GET ../<block_id>/context/big_maps/<big_map_id>?[offset=<uint>]&[length=<uint>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-big-maps-big-map-id)
     */
    public suspend fun getBigMap(
        chainId: ChainId,
        blockId: Int,
        bigMapId: String,
        offset: UInt? = null,
        length: UInt? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapResponse = getBigMap(chainId.base58, blockId.toString(), bigMapId, offset, length, headers)

    /**
     * Get the (optionally paginated) list of values in a big map. Order of values is unspecified, but is guaranteed to be consistent.
     *
     * Optional query arguments:
     * * [offset] = `<uint>` : Skip the first [offset] values. Useful in combination with [length] for pagination.
     * * [length] = `<uint>` : Only retrieve [length] values. Useful in combination with [offset] for pagination.
     *
     * [`GET ../<block_id>/context/big_maps/<big_map_id>?[offset=<uint>]&[length=<uint>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-big-maps-big-map-id)
     */
    public suspend fun getBigMap(
        chainId: ChainId,
        blockId: BlockHash,
        bigMapId: String,
        offset: UInt? = null,
        length: UInt? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapResponse = getBigMap(chainId.base58, blockId.base58, bigMapId, offset, length, headers)

    /**
     * Access the value associated with a key in a big map.
     *
     * [`GET ../<block_id>/context/big_maps/<big_map_id>/<script_expr>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-big-maps-big-map-id-script-expr)
     */
    public suspend fun getBigMapValue(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValueResponse

    /**
     * Access the value associated with a key in a big map.
     *
     * [`GET ../<block_id>/context/big_maps/<big_map_id>/<script_expr>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-big-maps-big-map-id-script-expr)
     */
    public suspend fun getBigMapValue(
        chainId: ChainId,
        blockId: Int,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValueResponse = getBigMapValue(chainId.base58, blockId.toString(), bigMapId, key, headers)

    /**
     * Access the value associated with a key in a big map.
     *
     * [`GET ../<block_id>/context/big_maps/<big_map_id>/<script_expr>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-big-maps-big-map-id-script-expr)
     */
    public suspend fun getBigMapValue(
        chainId: ChainId,
        blockId: BlockHash,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValueResponse = getBigMapValue(chainId.base58, blockId.base58, bigMapId, key, headers)

    // -- ../<block_id>/context/constants --

    /**
     * All constants
     *
     * [`GET ../<block_id>/context/constants`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-constants)
     */
    public suspend fun getConstants(chainId: String = Constants.Chain.MAIN, blockId: String = Constants.Block.HEAD, headers: List<HttpHeader> = emptyList()): GetConstantsResponse

    /**
     * All constants
     *
     * [`GET ../<block_id>/context/constants`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-constants)
     */
    public suspend fun getConstants(chainId: ChainId, blockId: Int, headers: List<HttpHeader> = emptyList()): GetConstantsResponse = getConstants(chainId.base58, blockId.toString(), headers)

    /**
     * All constants
     *
     * [`GET ../<block_id>/context/constants`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-constants)
     */
    public suspend fun getConstants(chainId: ChainId, blockId: BlockHash, headers: List<HttpHeader> = emptyList()): GetConstantsResponse = getConstants(chainId.base58, blockId.base58, headers)

    // -- ../<block_id>/context/contracts --

    /**
     * Access the complete status of a contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts)
     */
    public suspend fun getContractDetails(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDetailsResponse

    /**
     * Access the complete status of a contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts)
     */
    public suspend fun getContractDetails(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDetailsResponse = getContractDetails(chainId, blockId.toString(), contractId, headers)

    /**
     * Access the complete status of a contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts)
     */
    public suspend fun getContractDetails(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDetailsResponse = getContractDetails(chainId, blockId.base58, contractId, headers)

    /**
     * Access the balance of a contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-balance)
     */
    public suspend fun getBalance(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractBalanceResponse

    /**
     * Access the balance of a contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-balance)
     */
    public suspend fun getBalance(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractBalanceResponse = getBalance(chainId, blockId.toString(), contractId, headers)

    /**
     * Access the balance of a contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-balance)
     */
    public suspend fun getBalance(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractBalanceResponse = getBalance(chainId, blockId.base58, contractId, headers)

    /**
     * Access the counter of a contract, if any.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/counter`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-counter)
     */
    public suspend fun getCounter(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractCounterResponse

    /**
     * Access the counter of a contract, if any.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/counter`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-counter)
     */
    public suspend fun getCounter(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractCounterResponse = getCounter(chainId, blockId.toString(), contractId, headers)

    /**
     * Access the counter of a contract, if any.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/counter`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-counter)
     */
    public suspend fun getCounter(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractCounterResponse = getCounter(chainId, blockId.base58, contractId, headers)

    /**
     * Access the delegate of a contract, if any.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/delegate`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-delegate)
     */
    public suspend fun getDelegate(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDelegateResponse

    /**
     * Access the delegate of a contract, if any.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/delegate`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-delegate)
     */
    public suspend fun getDelegate(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDelegateResponse = getDelegate(chainId, blockId.toString(), contractId, headers)

    /**
     * Access the delegate of a contract, if any.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/delegate`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-delegate)
     */
    public suspend fun getDelegate(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDelegateResponse = getDelegate(chainId, blockId.base58, contractId, headers)

    /**
     * Return the list of entrypoints of the contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/entrypoints`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-entrypoints)
     */
    public suspend fun getEntrypoints(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointsResponse

    /**
     * Return the list of entrypoints of the contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/entrypoints`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-entrypoints)
     */
    public suspend fun getEntrypoints(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointsResponse = getEntrypoints(chainId, blockId.toString(), contractId, headers)

    /**
     * Return the list of entrypoints of the contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/entrypoints`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-entrypoints)
     */
    public suspend fun getEntrypoints(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointsResponse = getEntrypoints(chainId, blockId.base58, contractId, headers)

    /**
     * Return the type of the given entrypoint of the contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/entrypoints/<string>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-entrypoints-string)
     */
    public suspend fun getEntrypoint(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        entrypoint: String,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointResponse

    /**
     * Return the type of the given entrypoint of the contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/entrypoints/<string>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-entrypoints-string)
     */
    public suspend fun getEntrypoint(
        chainId: String,
        blockId: Int,
        contractId: Address,
        entrypoint: String,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointResponse = getEntrypoint(chainId, blockId.toString(), contractId, entrypoint)

    /**
     * Return the type of the given entrypoint of the contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/entrypoints/<string>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-entrypoints-string)
     */
    public suspend fun getEntrypoint(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        entrypoint: String,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointResponse = getEntrypoint(chainId, blockId.base58, contractId, entrypoint)

    /**
     * Access the manager of a contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/manager-key`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-manager-key)
     */
    public suspend fun getManagerKey(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractManagerKeyResponse

    /**
     * Access the manager of a contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/manager-key`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-manager-key)
     */
    public suspend fun getManagerKey(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractManagerKeyResponse = getManagerKey(chainId, blockId.toString(), contractId, headers)

    /**
     * Access the manager of a contract.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/manager-key`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-manager-key)
     */
    public suspend fun getManagerKey(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractManagerKeyResponse = getManagerKey(chainId, blockId.base58, contractId, headers)

    /**
     * Access the script of the contract and normalize it using the requested [unparsingMode].
     *
     * `POST ../<block_id>/context/contracts/<contract_id>/script/normalized`
     */
    public suspend fun getScript(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedScriptResponse

    /**
     * Access the script of the contract and normalize it using the requested [unparsingMode].
     *
     * `POST ../<block_id>/context/contracts/<contract_id>/script/normalized`
     */
    public suspend fun getScript(
        chainId: String,
        blockId: Int,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedScriptResponse = getScript(chainId, blockId.toString(), contractId, unparsingMode, headers)

    /**
     * Access the script of the contract and normalize it using the requested [unparsingMode].
     *
     * `POST ../<block_id>/context/contracts/<contract_id>/script/normalized`
     */
    public suspend fun getScript(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedScriptResponse = getScript(chainId, blockId.base58, contractId, unparsingMode, headers)

    /**
     * Returns the root and a diff of a state starting from an optional offset which is zero by default.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/single_sapling_get_diff?[offset_commitment=<uint63>]&[offset_nullifier=<uint63>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-single-sapling-get-diff)
     */
    public suspend fun getSaplingStateDiff(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractSaplingStateDiffResponse

    /**
     * Returns the root and a diff of a state starting from an optional offset which is zero by default.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/single_sapling_get_diff?[offset_commitment=<uint63>]&[offset_nullifier=<uint63>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-single-sapling-get-diff)
     */
    public suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: Int,
        contractId: Address,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractSaplingStateDiffResponse = getSaplingStateDiff(chainId, blockId.toString(), contractId, commitmentOffset, nullifierOffset, headers)

    /**
     * Returns the root and a diff of a state starting from an optional offset which is zero by default.
     *
     * [`GET ../<block_id>/context/contracts/<contract_id>/single_sapling_get_diff?[offset_commitment=<uint63>]&[offset_nullifier=<uint63>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-single-sapling-get-diff)
     */
    public suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractSaplingStateDiffResponse = getSaplingStateDiff(chainId, blockId.base58, contractId, commitmentOffset, nullifierOffset, headers)

    /**
     * Access the data of the contract and normalize it using the requested [unparsingMode].
     *
     * `POST ../<block_id>/context/contracts/<contract_id>/storage/normalized`
     */
    public suspend fun getStorage(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedStorageResponse

    /**
     * Access the data of the contract and normalize it using the requested [unparsingMode].
     *
     * `POST ../<block_id>/context/contracts/<contract_id>/storage/normalized`
     */
    public suspend fun getStorage(
        chainId: String,
        blockId: Int,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedStorageResponse = getStorage(chainId, blockId.toString(), contractId, unparsingMode, headers)

    /**
     * Access the data of the contract and normalize it using the requested [unparsingMode].
     *
     * `POST ../<block_id>/context/contracts/<contract_id>/storage/normalized`
     */
    public suspend fun getStorage(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedStorageResponse = getStorage(chainId, blockId.base58, contractId, unparsingMode, headers)

    // -- ../<block_id>/context/delegates --

    /**
     * Everything about a delegate.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh)
     */
    public suspend fun getDelegateDetails(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDetailsResponse

    /**
     * Everything about a delegate.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh)
     */
    public suspend fun getDelegateDetails(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDetailsResponse = getDelegateDetails(chainId, blockId.toString(), delegateId, headers)

    /**
     * Everything about a delegate.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh)
     */
    public suspend fun getDelegateDetails(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDetailsResponse = getDelegateDetails(chainId, blockId.base58, delegateId, headers)

    /**
     * Returns the current amount of the frozen deposits (in mutez).
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/current_frozen_deposits`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-current-frozen-deposits)
     */
    public suspend fun getCurrentFrozenDeposits(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateCurrentFrozenDepositsResponse

    /**
     * Returns the current amount of the frozen deposits (in mutez).
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/current_frozen_deposits`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-current-frozen-deposits)
     */
    public suspend fun getCurrentFrozenDeposits(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateCurrentFrozenDepositsResponse = getCurrentFrozenDeposits(chainId, blockId.toString(), delegateId, headers)

    /**
     * Returns the current amount of the frozen deposits (in mutez).
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/current_frozen_deposits`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-current-frozen-deposits)
     */
    public suspend fun getCurrentFrozenDeposits(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateCurrentFrozenDepositsResponse = getCurrentFrozenDeposits(chainId, blockId.base58, delegateId, headers)

    /**
     * Tells whether the delegate is currently tagged as deactivated or not.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/deactivated`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-deactivated)
     */
    public suspend fun isDeactivated(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDeactivatedStatusResponse

    /**
     * Tells whether the delegate is currently tagged as deactivated or not.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/deactivated`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-deactivated)
     */
    public suspend fun isDeactivated(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDeactivatedStatusResponse = isDeactivated(chainId, blockId.toString(), delegateId, headers)

    /**
     * Tells whether the delegate is currently tagged as deactivated or not.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/deactivated`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-deactivated)
     */
    public suspend fun isDeactivated(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDeactivatedStatusResponse = isDeactivated(chainId, blockId.base58, delegateId, headers)

    /**
     * Returns the sum (in mutez) of all balances of all the contracts that delegate to a given delegate. This excludes the delegate's own balance and its frozen deposits.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/delegated_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-delegated-balance)
     */
    public suspend fun getDelegatedBalance(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedBalanceResponse

    /**
     * Returns the sum (in mutez) of all balances of all the contracts that delegate to a given delegate. This excludes the delegate's own balance and its frozen deposits.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/delegated_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-delegated-balance)
     */
    public suspend fun getDelegatedBalance(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedBalanceResponse = getDelegatedBalance(chainId, blockId.toString(), delegateId, headers)

    /**
     * Returns the sum (in mutez) of all balances of all the contracts that delegate to a given delegate. This excludes the delegate's own balance and its frozen deposits.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/delegated_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-delegated-balance)
     */
    public suspend fun getDelegatedBalance(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedBalanceResponse = getDelegatedBalance(chainId, blockId.base58, delegateId, headers)

    /**
     * Returns the list of contracts that delegate to a given delegate.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/delegated_contracts`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-delegated-contracts)
     */
    public suspend fun getDelegatedContracts(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedContractsResponse

    /**
     * Returns the list of contracts that delegate to a given delegate.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/delegated_contracts`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-delegated-contracts)
     */
    public suspend fun getDelegatedContracts(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedContractsResponse = getDelegatedContracts(chainId, blockId.toString(), delegateId, headers)

    /**
     * Returns the list of contracts that delegate to a given delegate.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/delegated_contracts`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-delegated-contracts)
     */
    public suspend fun getDelegatedContracts(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedContractsResponse = getDelegatedContracts(chainId, blockId.base58, delegateId, headers)

    /**
     * Returns the initial amount (that is, at the beginning of a cycle) of the frozen deposits (in mutez).
     * This amount is the same as the current amount of the frozen deposits, unless the delegate has been punished.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/frozen_deposits`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-frozen-deposits)
     */
    public suspend fun getFrozenDeposits(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsResponse

    /**
     * Returns the initial amount (that is, at the beginning of a cycle) of the frozen deposits (in mutez).
     * This amount is the same as the current amount of the frozen deposits, unless the delegate has been punished.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/frozen_deposits`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-frozen-deposits)
     */
    public suspend fun getFrozenDeposits(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsResponse = getFrozenDeposits(chainId, blockId.toString(), delegateId, headers)

    /**
     * Returns the initial amount (that is, at the beginning of a cycle) of the frozen deposits (in mutez).
     * This amount is the same as the current amount of the frozen deposits, unless the delegate has been punished.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/frozen_deposits`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-frozen-deposits)
     */
    public suspend fun getFrozenDeposits(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsResponse = getFrozenDeposits(chainId, blockId.base58, delegateId, headers)

    /**
     * Returns the frozen deposits limit for the given delegate or none if no limit is set.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/frozen_deposits_limit`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-frozen-deposits-limit)
     */
    public suspend fun getFrozenDepositsLimit(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsLimitResponse

    /**
     * Returns the frozen deposits limit for the given delegate or none if no limit is set.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/frozen_deposits_limit`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-frozen-deposits-limit)
     */
    public suspend fun getFrozenDepositsLimit(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsLimitResponse = getFrozenDepositsLimit(chainId, blockId.toString(), delegateId, headers)

    /**
     * Returns the frozen deposits limit for the given delegate or none if no limit is set.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/frozen_deposits_limit`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-frozen-deposits-limit)
     */
    public suspend fun getFrozenDepositsLimit(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsLimitResponse = getFrozenDepositsLimit(chainId, blockId.base58, delegateId, headers)

    /**
     * Returns the full balance (in mutez) of a given delegate, including the frozen deposits. It does not include its delegated balance.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/full_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-full-balance)
     */
    public suspend fun getFullBalance(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFullBalanceResponse

    /**
     * Returns the full balance (in mutez) of a given delegate, including the frozen deposits. It does not include its delegated balance.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/full_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-full-balance)
     */
    public suspend fun getFullBalance(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFullBalanceResponse = getFullBalance(chainId, blockId.toString(), delegateId, headers)

    /**
     * Returns the full balance (in mutez) of a given delegate, including the frozen deposits. It does not include its delegated balance.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/full_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-full-balance)
     */
    public suspend fun getFullBalance(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFullBalanceResponse = getFullBalance(chainId, blockId.base58, delegateId, headers)

    /**
     * Returns the cycle by the end of which the delegate might be deactivated if she fails to execute any delegate action.
     * A deactivated delegate might be reactivated (without loosing any stake) by simply re-registering as a delegate.
     * For deactivated delegates, this value contains the cycle at which they were deactivated.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/grace_period`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-grace-period)
     */
    public suspend fun getGracePeriod(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateGracePeriodResponse

    /**
     * Returns the cycle by the end of which the delegate might be deactivated if she fails to execute any delegate action.
     * A deactivated delegate might be reactivated (without loosing any stake) by simply re-registering as a delegate.
     * For deactivated delegates, this value contains the cycle at which they were deactivated.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/grace_period`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-grace-period)
     */
    public suspend fun getGracePeriod(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateGracePeriodResponse = getGracePeriod(chainId, blockId.toString(), delegateId, headers)

    /**
     * Returns the cycle by the end of which the delegate might be deactivated if she fails to execute any delegate action.
     * A deactivated delegate might be reactivated (without loosing any stake) by simply re-registering as a delegate.
     * For deactivated delegates, this value contains the cycle at which they were deactivated.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/grace_period`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-grace-period)
     */
    public suspend fun getGracePeriod(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateGracePeriodResponse = getGracePeriod(chainId, blockId.base58, delegateId, headers)

    /**
     * Returns cycle and level participation information. 
     * In particular this indicates, in the field `expected_cycle_activity`, the number of slots the delegate is expected to have in the cycle based on its active stake. 
     *
     * The field `minimal_cycle_activity` indicates the minimal endorsing slots in the cycle required to get endorsing rewards. It is computed based on `expected_cycle_activity.
     *
     * The fields `missed_slots` and `missed_levels` indicate the number of missed endorsing slots and missed levels (for endorsing) in the cycle so far.
     *
     * `missed_slots` indicates the number of missed endorsing slots in the cycle so far.
     *
     * The field `remaining_allowed_missed_slots` indicates the remaining amount of endorsing slots that can be missed in the cycle before forfeiting the rewards.
     *
     * Finally, `expected_endorsing_rewards` indicates the endorsing rewards that will be distributed at the end of the cycle if activity at that point will be greater than the minimal required;
     * if the activity is already known to be below the required minimum, then the rewards are zero.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/participation`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-participation)
     */
    public suspend fun getParticipation(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateParticipationResponse

    /**
     * Returns cycle and level participation information.
     * In particular this indicates, in the field `expected_cycle_activity`, the number of slots the delegate is expected to have in the cycle based on its active stake.
     *
     * The field `minimal_cycle_activity` indicates the minimal endorsing slots in the cycle required to get endorsing rewards. It is computed based on `expected_cycle_activity.
     *
     * The fields `missed_slots` and `missed_levels` indicate the number of missed endorsing slots and missed levels (for endorsing) in the cycle so far.
     *
     * `missed_slots` indicates the number of missed endorsing slots in the cycle so far.
     *
     * The field `remaining_allowed_missed_slots` indicates the remaining amount of endorsing slots that can be missed in the cycle before forfeiting the rewards.
     *
     * Finally, `expected_endorsing_rewards` indicates the endorsing rewards that will be distributed at the end of the cycle if activity at that point will be greater than the minimal required;
     * if the activity is already known to be below the required minimum, then the rewards are zero.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/participation`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-participation)
     */
    public suspend fun getParticipation(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateParticipationResponse = getParticipation(chainId, blockId.toString(), delegateId, headers)

    /**
     * Returns cycle and level participation information.
     * In particular this indicates, in the field `expected_cycle_activity`, the number of slots the delegate is expected to have in the cycle based on its active stake.
     *
     * The field `minimal_cycle_activity` indicates the minimal endorsing slots in the cycle required to get endorsing rewards. It is computed based on `expected_cycle_activity.
     *
     * The fields `missed_slots` and `missed_levels` indicate the number of missed endorsing slots and missed levels (for endorsing) in the cycle so far.
     *
     * `missed_slots` indicates the number of missed endorsing slots in the cycle so far.
     *
     * The field `remaining_allowed_missed_slots` indicates the remaining amount of endorsing slots that can be missed in the cycle before forfeiting the rewards.
     *
     * Finally, `expected_endorsing_rewards` indicates the endorsing rewards that will be distributed at the end of the cycle if activity at that point will be greater than the minimal required;
     * if the activity is already known to be below the required minimum, then the rewards are zero.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/participation`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-participation)
     */
    public suspend fun getParticipation(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateParticipationResponse = getParticipation(chainId, blockId.base58, delegateId, headers)

    /**
     * Returns the total amount of tokens (in mutez) delegated to a given delegate.
     * This includes the balances of all the contracts that delegate to it, but also the balance of the delegate itself and its frozen deposits.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/staking_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-staking-balance)
     */
    public suspend fun getStakingBalance(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateStakingBalanceResponse

    /**
     * Returns the total amount of tokens (in mutez) delegated to a given delegate.
     * This includes the balances of all the contracts that delegate to it, but also the balance of the delegate itself and its frozen deposits.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/staking_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-staking-balance)
     */
    public suspend fun getStakingBalance(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateStakingBalanceResponse = getStakingBalance(chainId, blockId.toString(), delegateId, headers)

    /**
     * Returns the total amount of tokens (in mutez) delegated to a given delegate.
     * This includes the balances of all the contracts that delegate to it, but also the balance of the delegate itself and its frozen deposits.
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/staking_balance`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-staking-balance)
     */
    public suspend fun getStakingBalance(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateStakingBalanceResponse = getStakingBalance(chainId, blockId.base58, delegateId, headers)

    /**
     * The number of rolls in the vote listings for a given delegate
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/voting_power`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-voting-power)
     */
    public suspend fun getVotingPower(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateVotingPowerResponse

    /**
     * The number of rolls in the vote listings for a given delegate
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/voting_power`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-voting-power)
     */
    public suspend fun getVotingPower(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateVotingPowerResponse = getVotingPower(chainId, blockId.toString(), delegateId, headers)

    /**
     * The number of rolls in the vote listings for a given delegate
     *
     * [`GET ../<block_id>/context/delegates/<pkh>/voting_power`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-delegates-pkh-voting-power)
     */
    public suspend fun getVotingPower(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateVotingPowerResponse = getVotingPower(chainId, blockId.base58, delegateId, headers)

    // -- ../<block_id>/context/sapling --

    /**
     * Returns the root and a diff of a state starting from an optional offset which is zero by default.
     *
     * [`GET ../<block_id>/context/sapling/<sapling_state_id>/get_diff?[offset_commitment=<uint63>]&[offset_nullifier=<uint63>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-sapling-sapling-state-id-get-diff)
     */
    public suspend fun getSaplingStateDiff(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        stateId: String,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetSaplingStateDiffResponse

    /**
     * Returns the root and a diff of a state starting from an optional offset which is zero by default.
     *
     * [`GET ../<block_id>/context/sapling/<sapling_state_id>/get_diff?[offset_commitment=<uint63>]&[offset_nullifier=<uint63>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-sapling-sapling-state-id-get-diff)
     */
    public suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: Int,
        stateId: String,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetSaplingStateDiffResponse = getSaplingStateDiff(chainId, blockId.toString(), stateId, commitmentOffset, nullifierOffset, headers)

    /**
     * Returns the root and a diff of a state starting from an optional offset which is zero by default.
     *
     * [`GET ../<block_id>/context/sapling/<sapling_state_id>/get_diff?[offset_commitment=<uint63>]&[offset_nullifier=<uint63>]`](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-sapling-sapling-state-id-get-diff)
     */
    public suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: BlockHash,
        stateId: String,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetSaplingStateDiffResponse = getSaplingStateDiff(chainId, blockId.base58, stateId, commitmentOffset, nullifierOffset, headers)

    // -- ../<block_id>/header --

    /**
     * The whole block header.
     *
     * [`GET ../<block_id>/header`](https://tezos.gitlab.io/active/rpc.html#get-block-id-header)
     */
    public suspend fun getBlockHeader(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockHeaderResponse

    /**
     * The whole block header.
     *
     * [`GET ../<block_id>/header`](https://tezos.gitlab.io/active/rpc.html#get-block-id-header)
     */
    public suspend fun getBlockHeader(
        chainId: String,
        blockId: Int,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockHeaderResponse = getBlockHeader(chainId, blockId.toString(), headers)

    /**
     * The whole block header.
     *
     * [`GET ../<block_id>/header`](https://tezos.gitlab.io/active/rpc.html#get-block-id-header)
     */
    public suspend fun getBlockHeader(
        chainId: String,
        blockId: BlockHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockHeaderResponse = getBlockHeader(chainId, blockId.base58, headers)

    // -- ../<block_id>/helpers --

    /**
     * Simulate the application of the operations with the context of the given block and return the result of each operation application.
     *
     * [`POST ../<block_id>/helpers/preapply/operations`](https://tezos.gitlab.io/active/rpc.html#post-block-id-helpers-preapply-operations)
     */
    public suspend fun preapplyOperations(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        operations: List<RpcApplicableOperation>,
        headers: List<HttpHeader> = emptyList(),
    ): PreapplyOperationsResponse

    /**
     * Simulate the application of the operations with the context of the given block and return the result of each operation application.
     *
     * [`POST ../<block_id>/helpers/preapply/operations`](https://tezos.gitlab.io/active/rpc.html#post-block-id-helpers-preapply-operations)
     */
    public suspend fun preapplyOperations(
        chainId: String,
        blockId: Int,
        operations: List<RpcApplicableOperation>,
        headers: List<HttpHeader> = emptyList(),
    ): PreapplyOperationsResponse = preapplyOperations(chainId, blockId.toString(), operations, headers)

    /**
     * Simulate the application of the operations with the context of the given block and return the result of each operation application.
     *
     * [`POST ../<block_id>/helpers/preapply/operations`](https://tezos.gitlab.io/active/rpc.html#post-block-id-helpers-preapply-operations)
     */
    public suspend fun preapplyOperations(
        chainId: String,
        blockId: BlockHash,
        operations: List<RpcApplicableOperation>,
        headers: List<HttpHeader> = emptyList(),
    ): PreapplyOperationsResponse = preapplyOperations(chainId, blockId.base58, operations, headers)

    /**
     * Run an operation without signature checks.
     *
     * `POST ../<block_id>/helpers/scripts/run_operation`
     */
    public suspend fun runOperation(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        operation: RpcRunnableOperation,
        headers: List<HttpHeader> = emptyList(),
    ): RunOperationResponse

    /**
     * Run an operation without signature checks.
     *
     * `POST ../<block_id>/helpers/scripts/run_operation`
     */
    public suspend fun runOperation(
        chainId: String,
        blockId: Int,
        operation: RpcRunnableOperation,
        headers: List<HttpHeader> = emptyList(),
    ): RunOperationResponse = runOperation(chainId, blockId.toString(), operation, headers)

    /**
     * Run an operation without signature checks.
     *
     * `POST ../<block_id>/helpers/scripts/run_operation`
     */
    public suspend fun runOperation(
        chainId: String,
        blockId: BlockHash,
        operation: RpcRunnableOperation,
        headers: List<HttpHeader> = emptyList(),
    ): RunOperationResponse = runOperation(chainId, blockId.base58, operation, headers)

    // -- ../<block_id>/operations --

    /**
     * All the operations included in the block.
     *
     * [`GET ../<block_id>/operations`](https://tezos.gitlab.io/active/rpc.html#get-block-id-operations)
     */
    public suspend fun getOperations(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockOperationsResponse

    /**
     * All the operations included in the block.
     *
     * [`GET ../<block_id>/operations`](https://tezos.gitlab.io/active/rpc.html#get-block-id-operations)
     */
    public suspend fun getOperations(
        chainId: String,
        blockId: Int,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockOperationsResponse = getOperations(chainId, blockId.toString(), headers)

    /**
     * All the operations included in the block.
     *
     * [`GET ../<block_id>/operations`](https://tezos.gitlab.io/active/rpc.html#get-block-id-operations)
     */
    public suspend fun getOperations(
        chainId: String,
        blockId: BlockHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockOperationsResponse = getOperations(chainId, blockId.base58, headers)

}