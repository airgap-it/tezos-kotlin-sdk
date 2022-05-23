package it.airgap.tezos.operation

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.GenericSignature
import org.junit.Test
import kotlin.test.assertEquals

class OperationTest {

    @Test
    fun `should create Operation without signature`() {
        assertEquals(
            Operation.Unsigned(
                branch = BlockHash("BLyKu3tnc9NCuiFfCqfeVGPCoZTyW63dYh2XAYxkM7fQYKCqsju"),
                contents = listOf(),
            ),
            Operation(branch = BlockHash("BLyKu3tnc9NCuiFfCqfeVGPCoZTyW63dYh2XAYxkM7fQYKCqsju"))
        )

        assertEquals(
            Operation.Unsigned(
                branch = BlockHash("BLyKu3tnc9NCuiFfCqfeVGPCoZTyW63dYh2XAYxkM7fQYKCqsju"),
                contents = listOf(),
            ),
            Operation(contents = listOf(), branch = BlockHash("BLyKu3tnc9NCuiFfCqfeVGPCoZTyW63dYh2XAYxkM7fQYKCqsju"))
        )
    }

    @Test
    fun `should create Operation with signature`() {
        assertEquals(
            Operation.Signed(
                branch = BlockHash("BLyKu3tnc9NCuiFfCqfeVGPCoZTyW63dYh2XAYxkM7fQYKCqsju"),
                contents = listOf(),
                signature = GenericSignature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"),
            ),
            Operation(
                branch = BlockHash("BLyKu3tnc9NCuiFfCqfeVGPCoZTyW63dYh2XAYxkM7fQYKCqsju"),
                signature = GenericSignature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"),
            )
        )

        assertEquals(
            Operation.Signed(
                branch = BlockHash("BLyKu3tnc9NCuiFfCqfeVGPCoZTyW63dYh2XAYxkM7fQYKCqsju"),
                contents = listOf(),
                signature = GenericSignature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"),
            ),
            Operation(
                contents = listOf(),
                branch = BlockHash("BLyKu3tnc9NCuiFfCqfeVGPCoZTyW63dYh2XAYxkM7fQYKCqsju"),
                signature = GenericSignature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"),
            )
        )
    }
}