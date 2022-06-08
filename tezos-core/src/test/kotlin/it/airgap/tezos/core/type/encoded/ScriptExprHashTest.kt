package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class ScriptExprHashTest {

    @Test
    fun `should recognize valid and invalid ScriptExprHash strings`() {
        validStrings.forEach {
            assertTrue(ScriptExprHash.isValid(it), "Expected `$it` to be recognized as valid ScriptExprHash string.")
        }

        invalidStrings.forEach {
            assertFalse(ScriptExprHash.isValid(it), "Expected `$it` to be recognized as invalid ScriptExprHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid ScriptExprHash bytes`() {
        validBytes.forEach {
            assertTrue(ScriptExprHash.isValid(it), "Expected `$it` to be recognized as valid ScriptExprHash.")
            assertTrue(ScriptExprHash.isValid(it.toList()), "Expected `$it` to be recognized as valid ScriptExprHash.")
        }

        invalidBytes.forEach {
            assertFalse(ScriptExprHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid ScriptExprHash.")
            assertFalse(ScriptExprHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid ScriptExprHash.")
        }
    }

    @Test
    fun `should create ScriptExprHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, ScriptExprHash.createValue(it).base58)
            assertEquals(it, ScriptExprHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create ScriptExprHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { ScriptExprHash.createValue(it) }
            assertNull(ScriptExprHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "expruaAHDAGFn4aixExCNeVRt8FBnfYvzQpqXnbKHmyYmPt5os7z2Y",
            "expruMm9b21RC1Bd4cDmPUj7H7gMGQBo52yJ2ioM45eQMNxA5JjDKJ",
            "exprtpM9KMgkBqDucEytmwQFgFL8efJUEbfge6bNXrm3CGUk6xxY29",
            "exprvRaecNYofDS9rqrmPxc6ApA6QVPgt5yfVAEp8ahxoCMieQRLfr",
            "expru4q5HzbVcRyqj4WY58e2wkz8m4vmU55S3PMaymPMFFY9ZNhPgC",
            "exprv9xCWx9QDnxTBDipw2pN5Fjnp9PxtMF7EUaCL48ssXin1KhQVL",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "uaAHDAGFn4aixExCNeVRt8FBnfYvzQpqXnbKHmyYmPt5os7z2Y",
            "expruMm9b21RC1Bd4cDmPUj7H7gMGQBo52yJ2ioM45eQMNx",
            "exprtpM9KMgkBqDucEytmwQFgFL8efJUEbfge6bNXrm3CGUk6xxY29eMj",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "8b2635c2c542c6c72fe78d39db36ee900ed34906b61b6addb3493c549c1e01e3",
            "0d2c401b8b2635c2c542c6c72fe78d39db36ee900ed34906b61b6addb3493c549c1e01e3",
            "6eff089d0dd9280d094a640fc96aa2fb3f62f2eb1be1b00a6fb1e45742a13df4",
            "0d2c401b6eff089d0dd9280d094a640fc96aa2fb3f62f2eb1be1b00a6fb1e45742a13df4",
            "27aacc7cfe298466d8b2c21c727ec6f5cc35485bf5147fbd38ee2d7cdcff4be8",
            "0d2c401b27aacc7cfe298466d8b2c21c727ec6f5cc35485bf5147fbd38ee2d7cdcff4be8",
            "fb5d1fae186ac57f5e3c70877c35eb0fdc96ed483b23631a22af4efe44f08b76",
            "0d2c401bfb5d1fae186ac57f5e3c70877c35eb0fdc96ed483b23631a22af4efe44f08b76",
            "488ca1afa54b5074465fc811b8a3d0f5e991020f9e408a21e8b2a21b37124f23",
            "0d2c401b488ca1afa54b5074465fc811b8a3d0f5e991020f9e408a21e8b2a21b37124f23",
            "d7e0a192296ea7b0b75388cf9deee5f5d2f91c7a61a9cbb39a651fc5f4f484d4",
            "0d2c401bd7e0a192296ea7b0b75388cf9deee5f5d2f91c7a61a9cbb39a651fc5f4f484d4",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "c9c062f064e49a3adec834e4aea9cf97086a3e4ab53fa2ddc8cc896813",
            "0d2c401b594ae3acefed609f1cd9bc71d1a8b36e0cac5b6de25e62bf278d725b335e",
            "9f196f705cd26abab8d0d0d215edcea314c3340bba233bd8a4060eaf1e71a8510f6d",
        ).map { it.asHexString().toByteArray() }
}
