package it.airgap.tezos.rpc.shell.chains

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants

public interface Chains {
    public val main: Chain
        get() = invoke(Constants.Chain.MAIN)

    public val test: Chain
        get() = invoke(Constants.Chain.TEST)

    public operator fun invoke(chainId: String): Chain
    public operator fun invoke(chainId: ChainId): Chain = invoke(chainId.base58)

    public interface Chain {
        public suspend fun patch(bootstrapped: Boolean, headers: List<HttpHeader> = emptyList()): SetBootstrappedResponse

        public val blocks: Blocks
        public val chainId: ChainId
        public val invalidBlocks: InvalidBlocks
        public val isBootstrapped: IsBootstrapped
        public val levels: Levels

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

        public interface ChainId {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetChainIdResponse
        }

        public interface InvalidBlocks {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetInvalidBlocksResponse

            public operator fun invoke(blockHash: String): Block
            public operator fun invoke(blockHash: BlockHash): Block = invoke(blockHash.base58)

            public interface Block {
                public suspend fun get(headers: List<HttpHeader> = emptyList()): GetInvalidBlockResponse
                public suspend fun delete(headers: List<HttpHeader> = emptyList()): DeleteInvalidBlockResponse
            }
        }

        public interface IsBootstrapped {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetIsBootstrappedResponse
        }

        public interface Levels {
            public val caboose: Caboose
            public val checkpoint: Checkpoint
            public val savepoint: Savepoint

            public interface Caboose {
                public suspend fun get(headers: List<HttpHeader> = emptyList()): GetCabooseResponse
            }

            public interface Checkpoint {
                public suspend fun get(headers: List<HttpHeader> = emptyList()): GetCheckpointResponse
            }

            public interface Savepoint {
                public suspend fun get(headers: List<HttpHeader> = emptyList()): GetSavepointResponse
            }
        }
    }
}