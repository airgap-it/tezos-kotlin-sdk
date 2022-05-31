package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class ChainIdTest {

    @Test
    fun `should recognize valid and invalid ChainId strings`() {
        validStrings.forEach {
            assertTrue(ChainId.isValid(it), "Expected `$it` to be recognized as valid ChainId string.")
        }

        invalidStrings.forEach {
            assertFalse(ChainId.isValid(it), "Expected `$it` to be recognized as invalid ChainId string.")
        }
    }

    @Test
    fun `should recognize valid and invalid ChainId bytes`() {
        validBytes.forEach {
            assertTrue(ChainId.isValid(it), "Expected `$it` to be recognized as valid ChainId.")
            assertTrue(ChainId.isValid(it.toList()), "Expected `$it` to be recognized as valid ChainId.")
        }

        invalidBytes.forEach {
            assertFalse(ChainId.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid ChainId.")
            assertFalse(ChainId.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid ChainId.")
        }
    }

    @Test
    fun `should create ChainId from valid string`() {
        validStrings.forEach {
            assertEquals(it, ChainId.createValue(it).base58)
            assertEquals(it, ChainId.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create ChainId from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { ChainId.createValue(it) }
            assertNull(ChainId.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "NetXW6TG9Si5bTf",
            "NetXk5cuWSi7NCQ",
            "NetXVzr7bEZhFSm",
            "NetXunAT5v1V4H8",
            "NetXUDs9sNTabhH",
            "NetY1LaPG6YT1dw",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "XW6TG9Si5bTf",
            "NetXk5cuWSi",
            "NetXVzr7bEZhFSmXunA",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "4e482bcc",
            "5752004e482bcc",
            "a1e581ad",
            "575200a1e581ad",
            "4db43cfe",
            "5752004db43cfe",
            "dbe21baf",
            "575200dbe21baf",
            "4316b62a",
            "5752004316b62a",
            "fd1e0b63",
            "575200fd1e0b63",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "",
            "a1e581ade581",
            "175200fd1e0b63",
        ).map { it.asHexString().toByteArray() }
}