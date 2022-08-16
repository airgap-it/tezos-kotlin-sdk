package it.airgap.tezos.rpc.shell.chains

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants

/**
 * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/chains`
 */
public interface Chains {
    public val main: Chain
        get() = invoke(Constants.Chain.MAIN)

    public val test: Chain
        get() = invoke(Constants.Chain.TEST)

    public operator fun invoke(chainId: String): Chain
    public operator fun invoke(chainId: ChainId): Chain = invoke(chainId.base58)

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/chains/<chain_id>`
     */
    public interface Chain {
        public suspend fun patch(bootstrapped: Boolean, headers: List<HttpHeader> = emptyList()): SetBootstrappedResponse

        public val blocks: Blocks
        public val chainId: ChainId
        public val invalidBlocks: InvalidBlocks
        public val isBootstrapped: IsBootstrapped
        public val levels: Levels

        /**
         * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/chains/<chain_id>/blocks`
         */
        public interface Blocks {

            public suspend fun get(
                length: UInt? = null,
                head: BlockHash? = null,
                minDate: String? = null,
                headers: List<HttpHeader> = emptyList(),
            ): GetBlocksResponse

            public val head: Block
                get() = invoke(Constants.Block.HEAD)

            public operator fun invoke(blockId: String): Block
            public operator fun invoke(blockId: BlockHash): Block = invoke(blockId.base58)
        }

        /**
         * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/chains/<chain_id>/chain_id`
         */
        public interface ChainId {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetChainIdResponse
        }

        /**
         * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/chains/<chain_id>/invalid_blocks`
         */
        public interface InvalidBlocks {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetInvalidBlocksResponse

            public operator fun invoke(blockHash: BlockHash): Block

            /**
             * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/chains/<chain_id>/invalid_blocks/<block_hash>`
             */
            public interface Block {
                public suspend fun get(headers: List<HttpHeader> = emptyList()): GetInvalidBlockResponse
                public suspend fun delete(headers: List<HttpHeader> = emptyList()): DeleteInvalidBlockResponse
            }
        }

        /**
         * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/chains/<chain_id>/is_bootstrapped`
         */
        public interface IsBootstrapped {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetBootstrappedStatusResponse
        }

        /**
         * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/chains/<chain_id>/levels`
         */
        public interface Levels {
            public val caboose: Caboose
            public val checkpoint: Checkpoint
            public val savepoint: Savepoint

            /**
             * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/chains/<chain_id>/levels/caboose`
             */
            public interface Caboose {
                public suspend fun get(headers: List<HttpHeader> = emptyList()): GetCabooseResponse
            }

            /**
             * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/chains/<chain_id>/levels/checkpoint`
             */
            public interface Checkpoint {
                public suspend fun get(headers: List<HttpHeader> = emptyList()): GetCheckpointResponse
            }

            /**
             * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/chains/<chain_id>/levels/savepoint`
             */
            public interface Savepoint {
                public suspend fun get(headers: List<HttpHeader> = emptyList()): GetSavepointResponse
            }
        }
    }
}