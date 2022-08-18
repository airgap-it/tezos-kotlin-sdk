package it.airgap.tezos.rpc.converter

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.number.TezosNatural
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.operation.contract.Script
import it.airgap.tezos.operation.header.BlockHeader
import it.airgap.tezos.operation.header.LiquidityBakingToggleVote
import it.airgap.tezos.operation.inlined.InlinedEndorsement
import it.airgap.tezos.operation.inlined.InlinedPreendorsement
import it.airgap.tezos.rpc.internal.context.TezosRpcContext.asHexString
import it.airgap.tezos.rpc.internal.utils.placeholder
import it.airgap.tezos.rpc.type.block.RpcBlockHeader
import it.airgap.tezos.rpc.type.block.RpcLiquidityBakingToggleVote
import it.airgap.tezos.rpc.type.operation.RpcInlinedEndorsement
import it.airgap.tezos.rpc.type.operation.RpcInlinedPreendorsement
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation
import org.junit.Test
import kotlin.test.assertEquals

class OperationTest {

    @Test
    fun `converts Operation to RpcRunnableOperation`() {
        val chainId = ChainId("NetXnHfVqm9iesp")
        operationsWithRpcRunnable(chainId).forEach { (operation, rpcRunnableOperation) ->
            assertEquals(rpcRunnableOperation, operation.asRunnable(chainId))
        }
    }

    @Test
    fun `converts RpcRunnableOperation to Operation`() {
        val chainId = ChainId("NetXnHfVqm9iesp")
        operationsWithRpcRunnable(chainId).forEach { (operation, rpcRunnableOperation) ->
            assertEquals(operation, rpcRunnableOperation.asOperation())
        }
    }

    @Test
    fun `converts OperationContent to RpcOperationContent`() {
        operationContentsWithRpc.forEach { (operationContent, rpcOperationContent) ->
            assertEquals(rpcOperationContent, operationContent.asRpc())
        }
    }

    @Test
    fun `converts RpcOperationContent to OperationContent`() {
        operationContentsWithRpc.forEach { (operationContent, rpcOperationContent) ->
            assertEquals(operationContent, rpcOperationContent.asOperationContent())
        }
    }

    private fun operationsWithRpcRunnable(chainId: ChainId): List<Pair<Operation, RpcRunnableOperation>> =
        listOf(
            Operation.Unsigned(
                branch = BlockHash("BLJNdTWekHnVbsbgxwuXAdhzEg84w1Jn73nVjdzbSnxZikJSsGm"),
                contents = listOf(),
            ) to RpcRunnableOperation(
                chainId = chainId,
                branch = BlockHash("BLJNdTWekHnVbsbgxwuXAdhzEg84w1Jn73nVjdzbSnxZikJSsGm"),
                contents = listOf(),
                signature = Signature.placeholder,
            ),
            Operation.Unsigned(
                branch = BlockHash("BLVb91yM3Es88TFcrRNK36TTLnDnwbNoUVnKfUTc8KhJKJCL4SD"),
                contents = listOf(
                    OperationContent.FailingNoop("219a2eff641e0100".asHexString()),
                ),
            ) to RpcRunnableOperation(
                chainId = chainId,
                branch = BlockHash("BLVb91yM3Es88TFcrRNK36TTLnDnwbNoUVnKfUTc8KhJKJCL4SD"),
                contents = listOf(
                    RpcOperationContent.FailingNoop("219a2eff641e0100"),
                ),
                signature = Signature.placeholder,
            ),
        )

    private val operationContentsWithRpc: List<Pair<OperationContent, RpcOperationContent>>
        get() = listOf(
            OperationContent.SeedNonceRevelation(
                1,
                "6cdaf9367e551995a670a5c642a9396290f8c9d17e6bc3c1555bfaa910d92214".asHexString(),
            ) to RpcOperationContent.SeedNonceRevelation(
                1,
                "6cdaf9367e551995a670a5c642a9396290f8c9d17e6bc3c1555bfaa910d92214",
            ),
            OperationContent.DoubleEndorsementEvidence(
                InlinedEndorsement(
                    BlockHash("BLT3XKN3vFqWnWfuuLenQiyVgEgKcJttnGGdCcQbmE95xz9y7S5"),
                    OperationContent.Endorsement(
                        1U,
                        1,
                        1,
                        BlockPayloadHash("vh2cHpyeaHQhF7g3RFh8usyYmTTpt882UsRyXECuBwPiB3TcsKNd"),
                    ),
                    GenericSignature("sigdV5DNZRBLBDDEkbWcqefBuMZevanVyjotoazkkLbk7jXR8oZUmnxt6n3hkQtTe9WbLEkcCUWw1Ey7Ybby5z35nHKqpndn"),
                ),
                InlinedEndorsement(
                    BlockHash("BLZS5mP4BufHrZfvzrvw1ReWnj1L2zcQ4mM6Jywoaxe4mHbiCNn"),
                    OperationContent.Endorsement(
                        2U,
                        2,
                        2,
                        BlockPayloadHash("vh2rXj5TAG8p1HKiMyaWDdYrRL2rTBPyFLkVorgzEEBqqd4sgsXG"),
                    ),
                    GenericSignature("sigff9imsFxGwyQ8nEpXUR8ZFwTqZWjMJAgKGwub6Mn9Cnu4VvBppTRt84VPp1fRwqpx8JTrLHg76guTGzkm9ETKwFNCzniY"),
                ),
            ) to RpcOperationContent.DoubleEndorsementEvidence(
                RpcInlinedEndorsement(
                    BlockHash("BLT3XKN3vFqWnWfuuLenQiyVgEgKcJttnGGdCcQbmE95xz9y7S5"),
                    RpcOperationContent.Endorsement(
                        1U,
                        1,
                        1,
                        BlockPayloadHash("vh2cHpyeaHQhF7g3RFh8usyYmTTpt882UsRyXECuBwPiB3TcsKNd"),
                    ),
                    GenericSignature("sigdV5DNZRBLBDDEkbWcqefBuMZevanVyjotoazkkLbk7jXR8oZUmnxt6n3hkQtTe9WbLEkcCUWw1Ey7Ybby5z35nHKqpndn"),
                ),
                RpcInlinedEndorsement(
                    BlockHash("BLZS5mP4BufHrZfvzrvw1ReWnj1L2zcQ4mM6Jywoaxe4mHbiCNn"),
                    RpcOperationContent.Endorsement(
                        2U,
                        2,
                        2,
                        BlockPayloadHash("vh2rXj5TAG8p1HKiMyaWDdYrRL2rTBPyFLkVorgzEEBqqd4sgsXG"),
                    ),
                    GenericSignature("sigff9imsFxGwyQ8nEpXUR8ZFwTqZWjMJAgKGwub6Mn9Cnu4VvBppTRt84VPp1fRwqpx8JTrLHg76guTGzkm9ETKwFNCzniY"),
                ),
            ),
            OperationContent.DoubleBakingEvidence(
                BlockHeader(
                    1,
                    1U,
                    BlockHash("BKsP8FYgikDmqbUiVxfgXVjWuay5LQZY6LP4EvcsFK8uuqj4wQD"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.001Z"),
                    1U,
                    OperationListListHash("LLoaLP6mc6nVzG2Rp3fSrHFvvGpUvkbHCjLASVduN7GzQAKnPctrR"),
                    emptyList(),
                    ContextHash("CoWKSZnE72uMLBeh3Fmj3LSXjJmeCEmYBMxAig15g3LPjTP4rHmR"),
                    BlockPayloadHash("vh2cJrNF6FCXo1bfnM9hj66NDQSGQCBxTtqkxkMLzkTeeDnZjrvD"),
                    1,
                    "d4d34b5686c98ae1".asHexString(),
                    null,
                    LiquidityBakingToggleVote.On,
                    GenericSignature("sigiaEd9dHEGKgccx3JBBDw4eb6WVxGH3MvyziYbQqWQRMmyecdo5VuSkYWkgZvcQXshB4vV2qkTb6AxbKruaNPfnMg4u2EA"),
                ),
                BlockHeader(
                    2,
                    2U,
                    BlockHash("BMaBxGyVhtTiMKd7KA8HXJnbTK4e1TzffNc94G18op55HGQYVRk"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.002Z"),
                    2U,
                    OperationListListHash("LLoaNF9sd5z2SZtSmUopYNX6qs77QAUJqrnd5ei378H4bcJhQcPt5"),
                    emptyList(),
                    ContextHash("CoVj5HxwnPHpC1SgCC6pgqVPgw2vqFEqaC2bF5STqcbyX6giVrGn"),
                    BlockPayloadHash("vh2MHqgJtw8v7CDrZKYWtLmqGJtjzkRvs9yUeHNQqdgDJyCYm21q"),
                    2,
                    "336ebf95efce0475".asHexString(),
                    NonceHash("nceUeUCJRZ4M7FCSBsAUZU6dmxePdH7irje9Gfj9zWwCdfWd5B4Ee"),
                    LiquidityBakingToggleVote.Off,
                    GenericSignature("sigRsUhHqaFVBeV4qzyCZ6Y9TvoKajyNwyPQQCW3SbgPYY99MrpTqR2FopjzZEHMWoJG7LaTaHu7bnieKQRKqCRLA7hB7Ekp"),
                ),
            ) to RpcOperationContent.DoubleBakingEvidence(
                RpcBlockHeader(
                    1,
                    1U,
                    BlockHash("BKsP8FYgikDmqbUiVxfgXVjWuay5LQZY6LP4EvcsFK8uuqj4wQD"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.001Z"),
                    1U,
                    OperationListListHash("LLoaLP6mc6nVzG2Rp3fSrHFvvGpUvkbHCjLASVduN7GzQAKnPctrR"),
                    emptyList(),
                    ContextHash("CoWKSZnE72uMLBeh3Fmj3LSXjJmeCEmYBMxAig15g3LPjTP4rHmR"),
                    BlockPayloadHash("vh2cJrNF6FCXo1bfnM9hj66NDQSGQCBxTtqkxkMLzkTeeDnZjrvD"),
                    1,
                    "d4d34b5686c98ae1",
                    null,
                    RpcLiquidityBakingToggleVote.On,
                    GenericSignature("sigiaEd9dHEGKgccx3JBBDw4eb6WVxGH3MvyziYbQqWQRMmyecdo5VuSkYWkgZvcQXshB4vV2qkTb6AxbKruaNPfnMg4u2EA"),
                ),
                RpcBlockHeader(
                    2,
                    2U,
                    BlockHash("BMaBxGyVhtTiMKd7KA8HXJnbTK4e1TzffNc94G18op55HGQYVRk"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.002Z"),
                    2U,
                    OperationListListHash("LLoaNF9sd5z2SZtSmUopYNX6qs77QAUJqrnd5ei378H4bcJhQcPt5"),
                    emptyList(),
                    ContextHash("CoVj5HxwnPHpC1SgCC6pgqVPgw2vqFEqaC2bF5STqcbyX6giVrGn"),
                    BlockPayloadHash("vh2MHqgJtw8v7CDrZKYWtLmqGJtjzkRvs9yUeHNQqdgDJyCYm21q"),
                    2,
                    "336ebf95efce0475",
                    NonceHash("nceUeUCJRZ4M7FCSBsAUZU6dmxePdH7irje9Gfj9zWwCdfWd5B4Ee"),
                    RpcLiquidityBakingToggleVote.Off,
                    GenericSignature("sigRsUhHqaFVBeV4qzyCZ6Y9TvoKajyNwyPQQCW3SbgPYY99MrpTqR2FopjzZEHMWoJG7LaTaHu7bnieKQRKqCRLA7hB7Ekp"),
                ),
            ),
            OperationContent.DoubleBakingEvidence(
                BlockHeader(
                    1,
                    1U,
                    BlockHash("BKsP8FYgikDmqbUiVxfgXVjWuay5LQZY6LP4EvcsFK8uuqj4wQD"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.001Z"),
                    1U,
                    OperationListListHash("LLoaLP6mc6nVzG2Rp3fSrHFvvGpUvkbHCjLASVduN7GzQAKnPctrR"),
                    listOf("00000001000000000100000001".asHexString()),
                    ContextHash("CoWKSZnE72uMLBeh3Fmj3LSXjJmeCEmYBMxAig15g3LPjTP4rHmR"),
                    BlockPayloadHash("vh2cJrNF6FCXo1bfnM9hj66NDQSGQCBxTtqkxkMLzkTeeDnZjrvD"),
                    1,
                    "d4d34b5686c98ae1".asHexString(),
                    null,
                    LiquidityBakingToggleVote.On,
                    GenericSignature("sigiaEd9dHEGKgccx3JBBDw4eb6WVxGH3MvyziYbQqWQRMmyecdo5VuSkYWkgZvcQXshB4vV2qkTb6AxbKruaNPfnMg4u2EA"),
                ),
                BlockHeader(
                    2,
                    2U,
                    BlockHash("BMaBxGyVhtTiMKd7KA8HXJnbTK4e1TzffNc94G18op55HGQYVRk"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.002Z"),
                    2U,
                    OperationListListHash("LLoaNF9sd5z2SZtSmUopYNX6qs77QAUJqrnd5ei378H4bcJhQcPt5"),
                    listOf("00000002ff000000020000000200000002".asHexString(), "00000002000000000200000002".asHexString()),
                    ContextHash("CoVj5HxwnPHpC1SgCC6pgqVPgw2vqFEqaC2bF5STqcbyX6giVrGn"),
                    BlockPayloadHash("vh2MHqgJtw8v7CDrZKYWtLmqGJtjzkRvs9yUeHNQqdgDJyCYm21q"),
                    2,
                    "336ebf95efce0475".asHexString(),
                    NonceHash("nceUeUCJRZ4M7FCSBsAUZU6dmxePdH7irje9Gfj9zWwCdfWd5B4Ee"),
                    LiquidityBakingToggleVote.Pass,
                    GenericSignature("sigRsUhHqaFVBeV4qzyCZ6Y9TvoKajyNwyPQQCW3SbgPYY99MrpTqR2FopjzZEHMWoJG7LaTaHu7bnieKQRKqCRLA7hB7Ekp"),
                ),
            ) to RpcOperationContent.DoubleBakingEvidence(
                RpcBlockHeader(
                    1,
                    1U,
                    BlockHash("BKsP8FYgikDmqbUiVxfgXVjWuay5LQZY6LP4EvcsFK8uuqj4wQD"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.001Z"),
                    1U,
                    OperationListListHash("LLoaLP6mc6nVzG2Rp3fSrHFvvGpUvkbHCjLASVduN7GzQAKnPctrR"),
                    listOf("00000001000000000100000001"),
                    ContextHash("CoWKSZnE72uMLBeh3Fmj3LSXjJmeCEmYBMxAig15g3LPjTP4rHmR"),
                    BlockPayloadHash("vh2cJrNF6FCXo1bfnM9hj66NDQSGQCBxTtqkxkMLzkTeeDnZjrvD"),
                    1,
                    "d4d34b5686c98ae1",
                    null,
                    RpcLiquidityBakingToggleVote.On,
                    GenericSignature("sigiaEd9dHEGKgccx3JBBDw4eb6WVxGH3MvyziYbQqWQRMmyecdo5VuSkYWkgZvcQXshB4vV2qkTb6AxbKruaNPfnMg4u2EA"),
                ),
                RpcBlockHeader(
                    2,
                    2U,
                    BlockHash("BMaBxGyVhtTiMKd7KA8HXJnbTK4e1TzffNc94G18op55HGQYVRk"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.002Z"),
                    2U,
                    OperationListListHash("LLoaNF9sd5z2SZtSmUopYNX6qs77QAUJqrnd5ei378H4bcJhQcPt5"),
                    listOf("00000002ff000000020000000200000002", "00000002000000000200000002"),
                    ContextHash("CoVj5HxwnPHpC1SgCC6pgqVPgw2vqFEqaC2bF5STqcbyX6giVrGn"),
                    BlockPayloadHash("vh2MHqgJtw8v7CDrZKYWtLmqGJtjzkRvs9yUeHNQqdgDJyCYm21q"),
                    2,
                    "336ebf95efce0475",
                    NonceHash("nceUeUCJRZ4M7FCSBsAUZU6dmxePdH7irje9Gfj9zWwCdfWd5B4Ee"),
                    RpcLiquidityBakingToggleVote.Pass,
                    GenericSignature("sigRsUhHqaFVBeV4qzyCZ6Y9TvoKajyNwyPQQCW3SbgPYY99MrpTqR2FopjzZEHMWoJG7LaTaHu7bnieKQRKqCRLA7hB7Ekp"),
                ),
            ),
            OperationContent.ActivateAccount(
                Ed25519PublicKeyHash("tz1PokEhtiBGCmekQrcN87pCDmqy99TjaLuN"),
                "7b27ba02550e6834b50173c8c506de42d901c606".asHexString(),
            ) to RpcOperationContent.ActivateAccount(
                Ed25519PublicKeyHash("tz1PokEhtiBGCmekQrcN87pCDmqy99TjaLuN"),
                "7b27ba02550e6834b50173c8c506de42d901c606",
            ),
            OperationContent.Proposals(
                Ed25519PublicKeyHash("tz1QVzD6eV73LhtzhNKs94fKbvTg7VjKjEcE"),
                1,
                listOf(ProtocolHash("PtYnGfhwjiRjtA7VZriogYL6nwFgaAL9ZuVWE6UahXCMn6BoJPv")),
            ) to RpcOperationContent.Proposals(
                Ed25519PublicKeyHash("tz1QVzD6eV73LhtzhNKs94fKbvTg7VjKjEcE"),
                1,
                listOf(ProtocolHash("PtYnGfhwjiRjtA7VZriogYL6nwFgaAL9ZuVWE6UahXCMn6BoJPv")),
            ),
            OperationContent.Proposals(
                Ed25519PublicKeyHash("tz1MRrtJC9sk1o1D57LPWev6DDVjMgra5pXb"),
                1,
                listOf(ProtocolHash("PtARwRL7jEGtzoCCWDBXe6ZJ4ZJiWtDgBC2a5WwnHYYyKPdmwrb"), ProtocolHash("Ps6NdX1CpeF3kHV5CVQZMLZKZwAN8NYN9crdL3GzEg4uNg7f3DY")),
            ) to RpcOperationContent.Proposals(
                Ed25519PublicKeyHash("tz1MRrtJC9sk1o1D57LPWev6DDVjMgra5pXb"),
                1,
                listOf(ProtocolHash("PtARwRL7jEGtzoCCWDBXe6ZJ4ZJiWtDgBC2a5WwnHYYyKPdmwrb"), ProtocolHash("Ps6NdX1CpeF3kHV5CVQZMLZKZwAN8NYN9crdL3GzEg4uNg7f3DY")),
            ),
            OperationContent.Ballot(
                Ed25519PublicKeyHash("tz1eNhmMTYsti2quW46a5CBJbs4Fde4KGg4F"),
                1,
                ProtocolHash("PsjL76mH8vo3fTfUN4qKrdkPvRfXw7KJPWf87isNAxzh1vqdFQv"),
                OperationContent.Ballot.Type.Yay,
            ) to RpcOperationContent.Ballot(
                Ed25519PublicKeyHash("tz1eNhmMTYsti2quW46a5CBJbs4Fde4KGg4F"),
                1,
                ProtocolHash("PsjL76mH8vo3fTfUN4qKrdkPvRfXw7KJPWf87isNAxzh1vqdFQv"),
                RpcOperationContent.Ballot.Type.Yay,
            ),
            OperationContent.Ballot(
                Ed25519PublicKeyHash("tz1eNhmMTYsti2quW46a5CBJbs4Fde4KGg4F"),
                1,
                ProtocolHash("PsjL76mH8vo3fTfUN4qKrdkPvRfXw7KJPWf87isNAxzh1vqdFQv"),
                OperationContent.Ballot.Type.Nay,
            ) to RpcOperationContent.Ballot(
                Ed25519PublicKeyHash("tz1eNhmMTYsti2quW46a5CBJbs4Fde4KGg4F"),
                1,
                ProtocolHash("PsjL76mH8vo3fTfUN4qKrdkPvRfXw7KJPWf87isNAxzh1vqdFQv"),
                RpcOperationContent.Ballot.Type.Nay,
            ),
            OperationContent.Ballot(
                Ed25519PublicKeyHash("tz1eNhmMTYsti2quW46a5CBJbs4Fde4KGg4F"),
                1,
                ProtocolHash("PsjL76mH8vo3fTfUN4qKrdkPvRfXw7KJPWf87isNAxzh1vqdFQv"),
                OperationContent.Ballot.Type.Pass,
            ) to RpcOperationContent.Ballot(
                Ed25519PublicKeyHash("tz1eNhmMTYsti2quW46a5CBJbs4Fde4KGg4F"),
                1,
                ProtocolHash("PsjL76mH8vo3fTfUN4qKrdkPvRfXw7KJPWf87isNAxzh1vqdFQv"),
                RpcOperationContent.Ballot.Type.Pass,
            ),
            OperationContent.DoublePreendorsementEvidence(
                InlinedPreendorsement(
                    BlockHash("BLT3XKN3vFqWnWfuuLenQiyVgEgKcJttnGGdCcQbmE95xz9y7S5"),
                    OperationContent.Preendorsement(
                        1U,
                        1,
                        1,
                        BlockPayloadHash("vh2cHpyeaHQhF7g3RFh8usyYmTTpt882UsRyXECuBwPiB3TcsKNd"),
                    ),
                    GenericSignature("sigdV5DNZRBLBDDEkbWcqefBuMZevanVyjotoazkkLbk7jXR8oZUmnxt6n3hkQtTe9WbLEkcCUWw1Ey7Ybby5z35nHKqpndn"),
                ),
                InlinedPreendorsement(
                    BlockHash("BLZS5mP4BufHrZfvzrvw1ReWnj1L2zcQ4mM6Jywoaxe4mHbiCNn"),
                    OperationContent.Preendorsement(
                        2U,
                        2,
                        2,
                        BlockPayloadHash("vh2rXj5TAG8p1HKiMyaWDdYrRL2rTBPyFLkVorgzEEBqqd4sgsXG"),
                    ),
                    GenericSignature("sigff9imsFxGwyQ8nEpXUR8ZFwTqZWjMJAgKGwub6Mn9Cnu4VvBppTRt84VPp1fRwqpx8JTrLHg76guTGzkm9ETKwFNCzniY"),
                ),
            ) to RpcOperationContent.DoublePreendorsementEvidence(
                RpcInlinedPreendorsement(
                    BlockHash("BLT3XKN3vFqWnWfuuLenQiyVgEgKcJttnGGdCcQbmE95xz9y7S5"),
                    RpcOperationContent.Preendorsement(
                        1U,
                        1,
                        1,
                        BlockPayloadHash("vh2cHpyeaHQhF7g3RFh8usyYmTTpt882UsRyXECuBwPiB3TcsKNd"),
                    ),
                    GenericSignature("sigdV5DNZRBLBDDEkbWcqefBuMZevanVyjotoazkkLbk7jXR8oZUmnxt6n3hkQtTe9WbLEkcCUWw1Ey7Ybby5z35nHKqpndn"),
                ),
                RpcInlinedPreendorsement(
                    BlockHash("BLZS5mP4BufHrZfvzrvw1ReWnj1L2zcQ4mM6Jywoaxe4mHbiCNn"),
                    RpcOperationContent.Preendorsement(
                        2U,
                        2,
                        2,
                        BlockPayloadHash("vh2rXj5TAG8p1HKiMyaWDdYrRL2rTBPyFLkVorgzEEBqqd4sgsXG"),
                    ),
                    GenericSignature("sigff9imsFxGwyQ8nEpXUR8ZFwTqZWjMJAgKGwub6Mn9Cnu4VvBppTRt84VPp1fRwqpx8JTrLHg76guTGzkm9ETKwFNCzniY"),
                ),
            ),
            OperationContent.FailingNoop("cc7e647be422e432a3291ec8a2ee6f5e2210c51825b753758a99e266a0c65b15".asHexString()) to RpcOperationContent.FailingNoop("cc7e647be422e432a3291ec8a2ee6f5e2210c51825b753758a99e266a0c65b15"),
            OperationContent.Preendorsement(
                1U,
                1,
                1,
                BlockPayloadHash("vh2KDvhtt44Lyq187SnZjSDyRH1LNXbMj3T9G57miWK9QvqH3fhv"),
            ) to RpcOperationContent.Preendorsement(
                1U,
                1,
                1,
                BlockPayloadHash("vh2KDvhtt44Lyq187SnZjSDyRH1LNXbMj3T9G57miWK9QvqH3fhv"),
            ),
            OperationContent.Endorsement(
                1U,
                1,
                1,
                BlockPayloadHash("vh2WtVuY9PK3mDsnfdzA6iXc4pocgUff8hgamWwXw19r5kDYHVS5"),
            ) to RpcOperationContent.Endorsement(
                1U,
                1,
                1,
                BlockPayloadHash("vh2WtVuY9PK3mDsnfdzA6iXc4pocgUff8hgamWwXw19r5kDYHVS5"),
            ),
            OperationContent.Reveal(
                Ed25519PublicKeyHash("tz1SZ2CmbQB7MMXgcMSmyyVXpya1rkb9UGUE"),
                Mutez("135675"),
                TezosNatural("154"),
                TezosNatural("23675"),
                TezosNatural("34152"),
                Ed25519PublicKey("edpkuaARNJPQygG82X1xed6Z2kDutT8XjL3Fmv1XPBbca1uARirj55"),
            ) to RpcOperationContent.Reveal(
                Ed25519PublicKeyHash("tz1SZ2CmbQB7MMXgcMSmyyVXpya1rkb9UGUE"),
                Mutez("135675"),
                "154",
                "23675",
                "34152",
                Ed25519PublicKey("edpkuaARNJPQygG82X1xed6Z2kDutT8XjL3Fmv1XPBbca1uARirj55"),
            ),
            OperationContent.Transaction(
                Ed25519PublicKeyHash("tz1i8xLzLPQHknc5jmeFc3qxijar2HLG2W4Z"),
                Mutez("135675"),
                TezosNatural("154"),
                TezosNatural("23675"),
                TezosNatural("34152"),
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1YbTdYqmpLatAqLb1sm67qqXMXyRB3UYiz"),
            ) to RpcOperationContent.Transaction(
                Ed25519PublicKeyHash("tz1i8xLzLPQHknc5jmeFc3qxijar2HLG2W4Z"),
                Mutez("135675"),
                "154",
                "23675",
                "34152",
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1YbTdYqmpLatAqLb1sm67qqXMXyRB3UYiz"),
            ),
            OperationContent.Transaction(
                Ed25519PublicKeyHash("tz1i8xLzLPQHknc5jmeFc3qxijar2HLG2W4Z"),
                Mutez("135675"),
                TezosNatural("154"),
                TezosNatural("23675"),
                TezosNatural("34152"),
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1YbTdYqmpLatAqLb1sm67qqXMXyRB3UYiz"),
                Parameters(Entrypoint.Default, MichelineSequence()),
            ) to RpcOperationContent.Transaction(
                Ed25519PublicKeyHash("tz1i8xLzLPQHknc5jmeFc3qxijar2HLG2W4Z"),
                Mutez("135675"),
                "154",
                "23675",
                "34152",
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1YbTdYqmpLatAqLb1sm67qqXMXyRB3UYiz"),
                Parameters(Entrypoint.Default, MichelineSequence()),
            ),
            OperationContent.Origination(
                Ed25519PublicKeyHash("tz1LdF7qHCJg8Efa6Cx4LZrRPkvbh61H8tZq"),
                Mutez("135675"),
                TezosNatural("154"),
                TezosNatural("23675"),
                TezosNatural("34152"),
                Mutez("763243"),
                null,
                Script(MichelineSequence(), MichelineSequence())
            ) to RpcOperationContent.Origination(
                Ed25519PublicKeyHash("tz1LdF7qHCJg8Efa6Cx4LZrRPkvbh61H8tZq"),
                Mutez("135675"),
                "154",
                "23675",
                "34152",
                Mutez("763243"),
                null,
                Script(MichelineSequence(), MichelineSequence())
            ),
            OperationContent.Origination(
                Ed25519PublicKeyHash("tz1LdF7qHCJg8Efa6Cx4LZrRPkvbh61H8tZq"),
                Mutez("135675"),
                TezosNatural("154"),
                TezosNatural("23675"),
                TezosNatural("34152"),
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1RY8er4ybXszZBbhtQDrYhA5AYY3VQXiKn"),
                Script(MichelineSequence(), MichelineSequence())
            ) to RpcOperationContent.Origination(
                Ed25519PublicKeyHash("tz1LdF7qHCJg8Efa6Cx4LZrRPkvbh61H8tZq"),
                Mutez("135675"),
                "154",
                "23675",
                "34152",
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1RY8er4ybXszZBbhtQDrYhA5AYY3VQXiKn"),
                Script(MichelineSequence(), MichelineSequence())
            ),
            OperationContent.Delegation(
                Ed25519PublicKeyHash("tz1QVAraV1JDRsPikcqJVE4VccvW7vDWCJHy"),
                Mutez("135675"),
                TezosNatural("154"),
                TezosNatural("23675"),
                TezosNatural("34152"),
            ) to RpcOperationContent.Delegation(
                Ed25519PublicKeyHash("tz1QVAraV1JDRsPikcqJVE4VccvW7vDWCJHy"),
                Mutez("135675"),
                "154",
                "23675",
                "34152",
            ),
            OperationContent.Delegation(
                Ed25519PublicKeyHash("tz1QVAraV1JDRsPikcqJVE4VccvW7vDWCJHy"),
                Mutez("135675"),
                TezosNatural("154"),
                TezosNatural("23675"),
                TezosNatural("34152"),
                Ed25519PublicKeyHash("tz1dStZpfk5bWsvYvuktDJgDEbpuqDc7ipvi"),
            ) to RpcOperationContent.Delegation(
                Ed25519PublicKeyHash("tz1QVAraV1JDRsPikcqJVE4VccvW7vDWCJHy"),
                Mutez("135675"),
                "154",
                "23675",
                "34152",
                Ed25519PublicKeyHash("tz1dStZpfk5bWsvYvuktDJgDEbpuqDc7ipvi"),
            ),
            OperationContent.RegisterGlobalConstant(
                Ed25519PublicKeyHash("tz1brHnNaHcpxqHDhqwmAXDq1i4F2A4Xaepz"),
                Mutez("135675"),
                TezosNatural("154"),
                TezosNatural("23675"),
                TezosNatural("34152"),
                MichelineSequence(),
            ) to RpcOperationContent.RegisterGlobalConstant(
                Ed25519PublicKeyHash("tz1brHnNaHcpxqHDhqwmAXDq1i4F2A4Xaepz"),
                Mutez("135675"),
                "154",
                "23675",
                "34152",
                MichelineSequence(),
            ),
            OperationContent.SetDepositsLimit(
                Ed25519PublicKeyHash("tz1gxabEuUaCKk15qUKnhASJJoXhm9A7DVLM"),
                Mutez("135675"),
                TezosNatural("154"),
                TezosNatural("23675"),
                TezosNatural("34152"),
            ) to RpcOperationContent.SetDepositsLimit(
                Ed25519PublicKeyHash("tz1gxabEuUaCKk15qUKnhASJJoXhm9A7DVLM"),
                Mutez("135675"),
                "154",
                "23675",
                "34152",
            ),
            OperationContent.SetDepositsLimit(
                Ed25519PublicKeyHash("tz1gxabEuUaCKk15qUKnhASJJoXhm9A7DVLM"),
                Mutez("135675"),
                TezosNatural("154"),
                TezosNatural("23675"),
                TezosNatural("34152"),
                Mutez("634"),
            ) to RpcOperationContent.SetDepositsLimit(
                Ed25519PublicKeyHash("tz1gxabEuUaCKk15qUKnhASJJoXhm9A7DVLM"),
                Mutez("135675"),
                "154",
                "23675",
                "34152",
                Mutez("634"),
            ),
        )
}