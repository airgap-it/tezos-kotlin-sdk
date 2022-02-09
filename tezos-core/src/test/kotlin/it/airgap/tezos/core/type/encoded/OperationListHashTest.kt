package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class OperationListHashTest {

    @Test
    fun `should recognize valid and invalid OperationListHash strings`() {
        validStrings.forEach {
            assertTrue(OperationListHash.isValid(it), "Expected `$it` to be recognized as valid OperationListHash string.")
        }

        invalidStrings.forEach {
            assertFalse(OperationListHash.isValid(it), "Expected `$it` to be recognized as invalid OperationListHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid OperationListHash bytes`() {
        validBytes.forEach {
            assertTrue(OperationListHash.isValid(it), "Expected `$it` to be recognized as valid OperationListHash.")
            assertTrue(OperationListHash.isValid(it.toList()), "Expected `$it` to be recognized as valid OperationListHash.")
        }

        invalidBytes.forEach {
            assertFalse(OperationListHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationListHash.")
            assertFalse(OperationListHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationListHash.")
        }
    }

    @Test
    fun `should create OperationListHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, OperationListHash.createValue(it).base58)
            assertEquals(it, OperationListHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create OperationListHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { OperationListHash.createValue(it) }
            assertNull(OperationListHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "LowHCVByofcwwJMQj7fMre6Hg78HAP6q1dL84RaY1AXFrra4dRK3",
            "Lox2L8jANW9Z1k6mg6RFXkqmz7SvAKKEN8uSeAeaqfbmxuuCBiN4",
            "Lowu3ZKxMWVxKSAbwBLUC9KDirWhyhQRnxHYi5nXgDaReXaphisg",
            "LowP2jQMr6TsdBd5nd7mq4eEZKcVqzwH9rfsds53bxsioRwMEo6z",
            "Low76fZHRqkEfcBjuVrU8pP4YrsrD7gxdSgFZDE1sLGLX71e2XE3",
            "LowbQjznqSzQYonzRPjB6SLCen6ZmWktM2xHMLasb2zHTwBvHvJ7",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "wHCVByofcwwJMQj7fMre6Hg78HAP6q1dL84RaY1AXFrra4dRK3",
            "Lox2L8jANW9Z1k6mg6RFXkqmz7SvAKKEN8uSeAeaqfbmxuuC",
            "Lowu3ZKxMWVxKSAbwBLUC9KDirWhyhQRnxHYi5nXgDaReXaphisgP2jQ",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "412de6f9f71aa23cbf5439a4692208b9289065d23d78c2d08e44248a4873c468",
            "85e9412de6f9f71aa23cbf5439a4692208b9289065d23d78c2d08e44248a4873c468",
            "a31d8db7006de9162fb69e128e59b5419dfc1a76846c6b622456bc25d82934ac",
            "85e9a31d8db7006de9162fb69e128e59b5419dfc1a76846c6b622456bc25d82934ac",
            "9292795e347bc4b2d323975206effe4045ad87cfc0c1406b91e59f95044123e8",
            "85e99292795e347bc4b2d323975206effe4045ad87cfc0c1406b91e59f95044123e8",
            "4e6bd046fd7d7a763830316c225da30722923f516b3acfb01e27ea4341fb4fcc",
            "85e94e6bd046fd7d7a763830316c225da30722923f516b3acfb01e27ea4341fb4fcc",
            "2a3ec4a52e18c864787be4ccd020a2577331ea009e1bd23869d48679ecf83e39",
            "85e92a3ec4a52e18c864787be4ccd020a2577331ea009e1bd23869d48679ecf83e39",
            "6a87c0d35eacdfd3d358aa8d2170c27e0f1bd9f688a9990b9bdebb49e39d8009",
            "85e96a87c0d35eacdfd3d358aa8d2170c27e0f1bd9f688a9990b9bdebb49e39d8009",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "412de6f9f71aa23cbf5439a4692208b9289065d23d78c2d08e44248a48",
            "a31d8db7006de9162fb69e128e59b5419dfc1a76846c6b622456bc25d82934ac1d8d",
            "15e96a87c0d35eacdfd3d358aa8d2170c27e0f1bd9f688a9990b9bdebb49e39d8009",
        ).map { it.asHexString().toByteArray() }
}