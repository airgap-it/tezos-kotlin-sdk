package it.airgap.tezos.rpc.shell.chains

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants

public interface Chains {
    public val main: Chain
        get() = chainId(Constants.Chain.MAIN)

    public val test: Chain
        get() = chainId(Constants.Chain.TEST)

    public fun chainId(chainId: String): Chain
    public fun chainId(chainId: ChainId): Chain = chainId(chainId.base58)

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

            // TODO: replace ActiveRpc with proper Block service

            public val head: Block
                get() = blockId(Constants.Block.HEAD)

            public fun blockId(blockId: String): Block
            public fun blockId(blockId: BlockHash): Block = blockId(blockId.base58)
        }

        public interface ChainId {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetChainIdResponse
        }

        public interface InvalidBlocks {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetInvalidBlocksResponse

            public fun blockHash(blockHash: String): Block
            public fun blockHash(blockHash: BlockHash): Block = blockHash(blockHash.base58)

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