package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class BlockHashTest {

    @Test
    fun `should recognize valid and invalid BlockHash strings`() {
        validStrings.forEach {
            assertTrue(BlockHash.isValid(it), "Expected `$it` to be recognized as valid BlockHash string.")
        }

        invalidStrings.forEach {
            assertFalse(BlockHash.isValid(it), "Expected `$it` to be recognized as invalid BlockHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid BlockHash bytes`() {
        validBytes.forEach {
            assertTrue(BlockHash.isValid(it), "Expected `$it` to be recognized as valid BlockHash.")
            assertTrue(BlockHash.isValid(it.toList()), "Expected `$it` to be recognized as valid BlockHash.")
        }

        invalidBytes.forEach {
            assertFalse(BlockHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid BlockHash.")
            assertFalse(BlockHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid BlockHash.")
        }
    }

    @Test
    fun `should create BlockHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, BlockHash.createValue(it).base58)
            assertEquals(it, BlockHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create BlockHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { BlockHash.createValue(it) }
            assertNull(BlockHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "BLZuCPKnM5PYfs6cs48nKzDHY9HWWMfrcvcPoQzpeL26wh41L5y",
            "BKqXxNcP3SkyKmqX9UAU62WuZgzpxdpqc7eACt9SVo3w3p1KZXr",
            "BM4a1QFShy91pg5Cct6Yyx3RpGjhaXD8szdLnLDsp34crqtVxfz",
            "BLGNbibeMYuRUrgQC58wN56BLJCGPrPPCdsDt89RAXDA2Ps25z7",
            "BLKF7ECXSEL2WCnYvuuY5XpA7JiNCbCfmwv9UKViUCPL7cPA4oH",
            "BM9tckCh6x371XaMkhabZMud3noKzn6S6xfjtrMjhwtm8c7QMz2",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "LZuCPKnM5PYfs6cs48nKzDHY9HWWMfrcvcPoQzpeL26wh41L5y",
            "BKqXxNcP3SkyKmqX9UAU62WuZgzpxdpqc7eACt9SVo3w3p1",
            "BM4a1QFShy91pg5Cct6Yyx3RpGjhaXD8szdLnLDsp34crqtVxfzNbib",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "70aa17b1327728a490ff3cb0279355a795310a60ecc075a091c57bd5a096815a",
            "013470aa17b1327728a490ff3cb0279355a795310a60ecc075a091c57bd5a096815a",
            "10777a0e1b8dd6415d5f748de15605940b5238e424a4b4eb559155fd1aa2056b",
            "013410777a0e1b8dd6415d5f748de15605940b5238e424a4b4eb559155fd1aa2056b",
            "b1c2e02cbe3f1bf531fa546e88581dba97c42c0c19a6611fb2136c9c9f06945c",
            "0134b1c2e02cbe3f1bf531fa546e88581dba97c42c0c19a6611fb2136c9c9f06945c",
            "48ddb12b2b32a924f6fb09c131dcd0bfad479a9fd33ffc007265cdc48403ec27",
            "013448ddb12b2b32a924f6fb09c131dcd0bfad479a9fd33ffc007265cdc48403ec27",
            "4f6273e928e89b4abdd998782fe513970da5e3b915657f11c20f0ed148ff5294",
            "01344f6273e928e89b4abdd998782fe513970da5e3b915657f11c20f0ed148ff5294",
            "bdd7c6caf678104b0b50f84facade5f21c34aad1fb5d228d8afb4fc875603049",
            "0134bdd7c6caf678104b0b50f84facade5f21c34aad1fb5d228d8afb4fc875603049",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "70aa17b1327728a490ff3cb0279355a795310a60ecc075a091c57bd5a0",
            "10777a0e1b8dd6415d5f748de15605940b5238e424a4b4eb559155fd1aa2056b777a",
            "1134bdd7c6caf678104b0b50f84facade5f21c34aad1fb5d228d8afb4fc875603049",
        ).map { it.asHexString().toByteArray() }
}