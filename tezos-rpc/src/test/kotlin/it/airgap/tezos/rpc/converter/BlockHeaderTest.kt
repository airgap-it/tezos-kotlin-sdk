package it.airgap.tezos.rpc.converter

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.operation.header.BlockHeader
import it.airgap.tezos.operation.header.LiquidityBakingToggleVote
import it.airgap.tezos.rpc.internal.context.TezosRpcContext.asHexString
import it.airgap.tezos.rpc.type.block.RpcBlockHeader
import it.airgap.tezos.rpc.type.block.RpcLiquidityBakingToggleVote
import org.junit.Test
import kotlin.test.assertEquals

class BlockHeaderTest {

    @Test
    fun `converts BlockHeader to RpcBlockHeader`() {
        blockHeadersWithRpc.forEach { (blockHeader, rpcBlockHeader) ->
            assertEquals(rpcBlockHeader, blockHeader.asRpc())
        }
    }

    @Test
    fun `converts RpcBlockHeader to BlockHeader`() {
        blockHeadersWithRpc.forEach { (blockHeader, rpcBlockHeader) ->
            assertEquals(blockHeader, rpcBlockHeader.asBlockHeader())
        }
    }

    private val blockHeadersWithRpc: List<Pair<BlockHeader, RpcBlockHeader>>
        get() = listOf(
            BlockHeader(
                level = 376499,
                proto = 2U,
                predecessor = BlockHash("BLJNdTWekHnVbsbgxwuXAdhzEg84w1Jn73nVjdzbSnxZikJSsGm"),
                timestamp = Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                validationPass = 4U,
                operationsHash = OperationListListHash("LLoZzrsq6NfsHydhDCBqXJyBTosRR9R9xNXTjm4PCtZ2juiEv9VBD"),
                fitness = listOf(
                    "02".asHexString(),
                    "0005beb3".asHexString(),
                    "ffffffff".asHexString(),
                    "00000000".asHexString(),
                ),
                context = ContextHash("CoVuxLsBWaDZXA6jdcSuU6NW3pzrsC1Z6uggBb71e9PS8XAEgWrj"),
                payloadHash = BlockPayloadHash("vh3Uk8raNVcLYrfT4QeiqykTjPxQHyk2ZpgH8B2XJNXSURujDxt8"),
                payloadRound = 0,
                proofOfWorkNonce = "61fed54075090100".asHexString(),
                liquidityBakingToggleVote = LiquidityBakingToggleVote.Off,
                signature = GenericSignature("sigZ3uvQ5oa3pxSZkPjASKFYvHtph3S7VN8mbUXjSAUtpsaWe736Aa2B5Tr8VpeG3b78FNZJpDoSWTQiTYmeuw4WfniEbFrx"),
            ) to RpcBlockHeader(
                level = 376499,
                proto = 2U,
                predecessor = BlockHash("BLJNdTWekHnVbsbgxwuXAdhzEg84w1Jn73nVjdzbSnxZikJSsGm"),
                timestamp = Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                validationPass = 4U,
                operationsHash = OperationListListHash("LLoZzrsq6NfsHydhDCBqXJyBTosRR9R9xNXTjm4PCtZ2juiEv9VBD"),
                fitness = listOf(
                    "02",
                    "0005beb3",
                    "ffffffff",
                    "00000000",
                ),
                context = ContextHash("CoVuxLsBWaDZXA6jdcSuU6NW3pzrsC1Z6uggBb71e9PS8XAEgWrj"),
                payloadHash = BlockPayloadHash("vh3Uk8raNVcLYrfT4QeiqykTjPxQHyk2ZpgH8B2XJNXSURujDxt8"),
                payloadRound = 0,
                proofOfWorkNonce = "61fed54075090100",
                liquidityBakingToggleVote = RpcLiquidityBakingToggleVote.Off,
                signature = GenericSignature("sigZ3uvQ5oa3pxSZkPjASKFYvHtph3S7VN8mbUXjSAUtpsaWe736Aa2B5Tr8VpeG3b78FNZJpDoSWTQiTYmeuw4WfniEbFrx"),
            ),
            BlockHeader(
                level = 444051,
                proto = 2U,
                predecessor = BlockHash("BLznWUsgQuUMKXKgAcxUFZwq9Y9KpZevDVruQ8V1So3jjHF68WG"),
                timestamp = Timestamp.Rfc3339("2022-04-26T07:07:25Z"),
                validationPass = 4U,
                operationsHash = OperationListListHash("LLoas3tyzRCjGKfkaCok681RFTjHQSJ5dipgJZkVSALHxZiBs2xUB"),
                fitness = listOf(
                    "02".asHexString(),
                    "0006c693".asHexString(),
                    "fffffffe".asHexString(),
                    "00000000".asHexString(),
                ),
                context = ContextHash("CoW338xEFzvo23cA9zec83VVj3qNYEL6H5DghaDXbLmo9o7r4CMi"),
                payloadHash = BlockPayloadHash("vh2svwjdP6sYvyDWgbrofirgaxSJ1tRghv1eue9Nw7SxQ4cm9YNn"),
                payloadRound = 0,
                proofOfWorkNonce = "6e2037c91e600200".asHexString(),
                seedNonceHash = NonceHash("nceVuHLBtMnAnhWgWBUn4sHVkPV4zJ4hYH7VrPkLWmkcB4BLEDk2F"),
                liquidityBakingToggleVote = LiquidityBakingToggleVote.Off,
                signature = GenericSignature("sigdKuy9ifmD4bSmJW9bausXsN8y9jhzDBBMoXaNngyyV6miaXXD4X3srtiGvjD8Ahapgtbw3Zjp4kqcPnbpsDm1CAa9wKBN"),
            ) to RpcBlockHeader(
                level = 444051,
                proto = 2U,
                predecessor = BlockHash("BLznWUsgQuUMKXKgAcxUFZwq9Y9KpZevDVruQ8V1So3jjHF68WG"),
                timestamp = Timestamp.Rfc3339("2022-04-26T07:07:25Z"),
                validationPass = 4U,
                operationsHash = OperationListListHash("LLoas3tyzRCjGKfkaCok681RFTjHQSJ5dipgJZkVSALHxZiBs2xUB"),
                fitness = listOf(
                    "02",
                    "0006c693",
                    "fffffffe",
                    "00000000",
                ),
                context = ContextHash("CoW338xEFzvo23cA9zec83VVj3qNYEL6H5DghaDXbLmo9o7r4CMi"),
                payloadHash = BlockPayloadHash("vh2svwjdP6sYvyDWgbrofirgaxSJ1tRghv1eue9Nw7SxQ4cm9YNn"),
                payloadRound = 0,
                proofOfWorkNonce = "6e2037c91e600200",
                seedNonceHash = NonceHash("nceVuHLBtMnAnhWgWBUn4sHVkPV4zJ4hYH7VrPkLWmkcB4BLEDk2F"),
                liquidityBakingToggleVote = RpcLiquidityBakingToggleVote.Off,
                signature = GenericSignature("sigdKuy9ifmD4bSmJW9bausXsN8y9jhzDBBMoXaNngyyV6miaXXD4X3srtiGvjD8Ahapgtbw3Zjp4kqcPnbpsDm1CAa9wKBN"),
            )
        )
}