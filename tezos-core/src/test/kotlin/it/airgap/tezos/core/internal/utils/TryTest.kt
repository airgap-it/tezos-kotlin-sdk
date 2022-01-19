package it.airgap.tezos.core.internal.utils

import org.junit.Test
import kotlin.test.assertTrue

class TryTest {

    @Test
    fun `executes block returning Result and returns flatten result if no exception was thrown`() {
        val result = runCatchingFlat { Result.success() }

        assertTrue(result.isSuccess, "Expected result to be a success")
    }

    @Test
    fun `executes block returning Result and returns failure result on error`() {
        val result = runCatchingFlat<Unit> { throw IllegalStateException() }

        assertTrue(result.isFailure, "Expected result to be a failure")
    }
}