package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class BlockPayloadHashTest {

    @Test
    fun `should recognize valid and invalid BlockPayloadHash strings`() {
        validStrings.forEach {
            assertTrue(BlockPayloadHash.isValid(it), "Expected `$it` to be recognized as valid BlockPayloadHash string.")
        }

        invalidStrings.forEach {
            assertFalse(BlockPayloadHash.isValid(it), "Expected `$it` to be recognized as invalid BlockPayloadHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid BlockPayloadHash bytes`() {
        validBytes.forEach {
            assertTrue(BlockPayloadHash.isValid(it), "Expected `$it` to be recognized as valid BlockPayloadHash.")
            assertTrue(BlockPayloadHash.isValid(it.toList()), "Expected `$it` to be recognized as valid BlockPayloadHash.")
        }

        invalidBytes.forEach {
            assertFalse(BlockPayloadHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid BlockPayloadHash.")
            assertFalse(BlockPayloadHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid BlockPayloadHash.")
        }
    }

    @Test
    fun `should create BlockPayloadHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, BlockPayloadHash.createValue(it).base58)
            assertEquals(it, BlockPayloadHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create BlockPayloadHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { BlockPayloadHash.createValue(it) }
            assertNull(BlockPayloadHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "vh3bENMimKSukoKTQZd5HQHxcTGBKF36WuQDYAfZgYeVSk7dHjj2",
            "vh3LDcYTufVo4Sv1AcJ3AR5Tve6x3iEtdHvAur8EX6Vn5ZYDdtJC",
            "vh2639CwKTmG3PVUgFC13c8SydRNtwZk2YtAKyNH2uUFe2RWSKGj",
            "vh2YTvpCfQHkkjheRA1bzn6TdLSbUZNZBB14ZfjpSmqwrp1TzLMu",
            "vh2GRP91n9QbsqwBQFtL48jNtJE8qzqDcKvWJ8ahG6S5GJUnEMLV",
            "vh1hVUMGc5prP3hiBy57bTmVB68BL3Vma5K7phio7eKwawxzJvKD",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "3bENMimKSukoKTQZd5HQHxcTGBKF36WuQDYAfZgYeVSk7dHjj2",
            "vh3LDcYTufVo4Sv1AcJ3AR5Tve6x3iEtdHvAur8EX6Vn5ZYD",
            "vh2639CwKTmG3PVUgFC13c8SydRNtwZk2YtAKyNH2uUFe2RWSKGjYTvp",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "fc48b1afd48171ce852439b1bb327519b2526ce65c9c7be8350b8f5c2bc1d9b7",
            "016af2fc48b1afd48171ce852439b1bb327519b2526ce65c9c7be8350b8f5c2bc1d9b7",
            "da31eef335cc7213cff719a5f2f4edf736f171b61fb10f7cc9ca24075668d9bb",
            "016af2da31eef335cc7213cff719a5f2f4edf736f171b61fb10f7cc9ca24075668d9bb",
            "364ce0dcb02b1b3a2469d6a2ce0ef25553f14868531a7839d78d02229d21e2de",
            "016af2364ce0dcb02b1b3a2469d6a2ce0ef25553f14868531a7839d78d02229d21e2de",
            "724e8d0021e8d47b91284df31854974bef4cc825e0b5802e0f270ae3789532f3",
            "016af2724e8d0021e8d47b91284df31854974bef4cc825e0b5802e0f270ae3789532f3",
            "4de090713ba6fb2e909739996baa43b7f473e9865ef2696a06eef0dcda5bb660",
            "016af24de090713ba6fb2e909739996baa43b7f473e9865ef2696a06eef0dcda5bb660",
            "031b55e155a757885984c41588fd1401872f47032b481611d4ce161af62702b0",
            "016af2031b55e155a757885984c41588fd1401872f47032b481611d4ce161af62702b0",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "fc48b1afd48171ce852439b1bb327519b2526ce65c9c7be8350b8f5c",
            "da31eef335cc7213cff719a5f2f4edf736f171b61fb10f7cc9ca24075668d9bb31ee",
            "116af2031b55e155a757885984c41588fd1401872f47032b481611d4ce161af62702b0",
        ).map { it.asHexString().toByteArray() }
}
