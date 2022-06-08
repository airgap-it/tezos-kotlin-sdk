package it.airgap.tezos.rpc.converter

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.BlockPayloadHash
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.inlined.InlinedEndorsement
import it.airgap.tezos.operation.inlined.InlinedPreendorsement
import it.airgap.tezos.rpc.type.operation.RpcInlinedEndorsement
import it.airgap.tezos.rpc.type.operation.RpcInlinedPreendorsement
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import org.junit.Test
import kotlin.test.assertEquals

class InlinedTest {

    @Test
    fun `converts InlinedEndorsement to RpcInlinedEndorsement`() {
        inlinedEndorsementsWithRpc.forEach { (inlinedEndorsement, rpcInlinedEndorsement) ->
            assertEquals(rpcInlinedEndorsement, inlinedEndorsement.asRpc())
        }
    }

    @Test
    fun `converts RpcInlinedEndorsement to InlinedEndorsement`() {
        inlinedEndorsementsWithRpc.forEach { (inlinedEndorsement, rpcInlinedEndorsement) ->
            assertEquals(inlinedEndorsement, rpcInlinedEndorsement.asInlinedEndorsement())
        }
    }

    @Test
    fun `converts InlinedPreendorsement to RpcInlinedPreendorsement`() {
        inlinedPreendorsementsWithRpc.forEach { (inlinedPreendorsement, rpcInlinedPreendorsement) ->
            assertEquals(rpcInlinedPreendorsement, inlinedPreendorsement.asRpc())
        }
    }

    @Test
    fun `converts RpcInlinedPreendorsement to InlinedPreendorsement`() {
        inlinedPreendorsementsWithRpc.forEach { (inlinedPreendorsement, rpcInlinedPreendorsement) ->
            assertEquals(inlinedPreendorsement, rpcInlinedPreendorsement.asInlinedPreendorsement())
        }
    }

    private val inlinedEndorsementsWithRpc: List<Pair<InlinedEndorsement, RpcInlinedEndorsement>>
        get() = listOf(
            InlinedEndorsement(
                BlockHash("BLT3XKN3vFqWnWfuuLenQiyVgEgKcJttnGGdCcQbmE95xz9y7S5"),
                OperationContent.Endorsement(
                    1U,
                    1,
                    1,
                    BlockPayloadHash("vh2cHpyeaHQhF7g3RFh8usyYmTTpt882UsRyXECuBwPiB3TcsKNd"),
                ),
                GenericSignature("sigdV5DNZRBLBDDEkbWcqefBuMZevanVyjotoazkkLbk7jXR8oZUmnxt6n3hkQtTe9WbLEkcCUWw1Ey7Ybby5z35nHKqpndn"),
            ) to RpcInlinedEndorsement(
                BlockHash("BLT3XKN3vFqWnWfuuLenQiyVgEgKcJttnGGdCcQbmE95xz9y7S5"),
                RpcOperationContent.Endorsement(
                    1U,
                    1,
                    1,
                    BlockPayloadHash("vh2cHpyeaHQhF7g3RFh8usyYmTTpt882UsRyXECuBwPiB3TcsKNd"),
                ),
                GenericSignature("sigdV5DNZRBLBDDEkbWcqefBuMZevanVyjotoazkkLbk7jXR8oZUmnxt6n3hkQtTe9WbLEkcCUWw1Ey7Ybby5z35nHKqpndn"),
            ),
        )

    private val inlinedPreendorsementsWithRpc: List<Pair<InlinedPreendorsement, RpcInlinedPreendorsement>>
        get() = listOf(
            InlinedPreendorsement(
                BlockHash("BLT3XKN3vFqWnWfuuLenQiyVgEgKcJttnGGdCcQbmE95xz9y7S5"),
                OperationContent.Preendorsement(
                    1U,
                    1,
                    1,
                    BlockPayloadHash("vh2cHpyeaHQhF7g3RFh8usyYmTTpt882UsRyXECuBwPiB3TcsKNd"),
                ),
                GenericSignature("sigdV5DNZRBLBDDEkbWcqefBuMZevanVyjotoazkkLbk7jXR8oZUmnxt6n3hkQtTe9WbLEkcCUWw1Ey7Ybby5z35nHKqpndn"),
            ) to RpcInlinedPreendorsement(
                BlockHash("BLT3XKN3vFqWnWfuuLenQiyVgEgKcJttnGGdCcQbmE95xz9y7S5"),
                RpcOperationContent.Preendorsement(
                    1U,
                    1,
                    1,
                    BlockPayloadHash("vh2cHpyeaHQhF7g3RFh8usyYmTTpt882UsRyXECuBwPiB3TcsKNd"),
                ),
                GenericSignature("sigdV5DNZRBLBDDEkbWcqefBuMZevanVyjotoazkkLbk7jXR8oZUmnxt6n3hkQtTe9WbLEkcCUWw1Ey7Ybby5z35nHKqpndn"),
            )
        )
}