package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class Ed25519SeedTest {

    @Test
    fun `should recognize valid and invalid Ed25519Seed strings`() {
        validStrings.forEach {
            assertTrue(Ed25519Seed.isValid(it), "Expected `$it` to be recognized as valid Ed25519Seed string.")
        }

        invalidStrings.forEach {
            assertFalse(Ed25519Seed.isValid(it), "Expected `$it` to be recognized as invalid Ed25519Seed string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Ed25519Seed bytes`() {
        validBytes.forEach {
            assertTrue(Ed25519Seed.isValid(it), "Expected `$it` to be recognized as valid Ed25519Seed.")
            assertTrue(Ed25519Seed.isValid(it.toList()), "Expected `$it` to be recognized as valid Ed25519Seed.")
        }

        invalidBytes.forEach {
            assertFalse(Ed25519Seed.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519Seed.")
            assertFalse(Ed25519Seed.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519Seed.")
        }
    }

    @Test
    fun `should create Ed25519Seed from valid string`() {
        validStrings.forEach {
            assertEquals(it, Ed25519Seed.createValue(it).base58)
            assertEquals(it, Ed25519Seed.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Ed25519Seed from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Ed25519Seed.createValue(it) }
            assertNull(Ed25519Seed.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "edsk3Wds6y1bvPeDCk4gjfBAPXYFBhb7HdY4tfid5rePH8YmJ5TA5H",
            "edsk375t8YmMHy3e6Xupdb6ukAMJdcdNJMC9Dd6EF6gfzqtzUpHpSm",
            "edsk3JoBrLtURS4oFAiVbd1jqCJPezhTztZwbervdyCvHrDQkxdazq",
            "edsk47ZXFskCyYY9td5hw9DAvvj8vfmzEaT61DQxkwE9ZYGSKKCX4m",
            "edsk35hvjFUbPwiNBSdMpB2uJ1QXd2nitGHXTCxrpqNAJzyXC8iJGM",
            "edsk32pBLoRT7bFGWB5xCc55sgexFXediSbqH4tL94NJauKZZGukdC",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "3Wds6y1bvPeDCk4gjfBAPXYFBhb7HdY4tfid5rePH8YmJ5TA5H",
            "edsk375t8YmMHy3e6Xupdb6ukAMJdcdNJMC9Dd6EF6gfzqtzUp",
            "edsk3JoBrLtURS4oFAiVbd1jqCJPezhTztZwbervdyCvHrDQkxdazqk47Z",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "6ea832a6a2b34176f254baeaa81f5988f4fb6e27fa749851f3e649989c8a76bc",
            "0d0f3a076ea832a6a2b34176f254baeaa81f5988f4fb6e27fa749851f3e649989c8a76bc",
            "392e3ec7ba8fa417e670ded5291bd1838f8bff945ed285e0a50f26967936a47e",
            "0d0f3a07392e3ec7ba8fa417e670ded5291bd1838f8bff945ed285e0a50f26967936a47e",
            "53c648b9de13a4e80c2319cbe2da37261507ea8e50aad63ef61173705dec5cf1",
            "0d0f3a0753c648b9de13a4e80c2319cbe2da37261507ea8e50aad63ef61173705dec5cf1",
            "bdf574d60af40cb7c827e6774778b25dc125b7ce7f28c36d0d20f0188f00e91d",
            "0d0f3a07bdf574d60af40cb7c827e6774778b25dc125b7ce7f28c36d0d20f0188f00e91d",
            "360cedfc5d02e2715739e7e2da63a26a0c32697431a32a3ade3d3aa01a2e1102",
            "0d0f3a07360cedfc5d02e2715739e7e2da63a26a0c32697431a32a3ade3d3aa01a2e1102",
            "2f7bbfc1fe82c9eff1845d58ba6017d345efb69ac53d28a93919865a745d8eec",
            "0d0f3a072f7bbfc1fe82c9eff1845d58ba6017d345efb69ac53d28a93919865a745d8eec",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "6ea832a6a2b34176f254baeaa81f5988f4fb6e27fa749851f3e649",
            "392e3ec7ba8fa417e670ded5291bd1838f8bff945ed285e0a50f26967936a47e2e3e",
            "1d0f3a072f7bbfc1fe82c9eff1845d58ba6017d345efb69ac53d28a93919865a745d8eec",
        ).map { it.asHexString().toByteArray() }
}