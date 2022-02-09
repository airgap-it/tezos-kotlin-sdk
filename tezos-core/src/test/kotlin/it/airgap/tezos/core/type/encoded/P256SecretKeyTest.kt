package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class P256SecretKeyTest {

    @Test
    fun `should recognize valid and invalid P256SecretKey strings`() {
        validStrings.forEach {
            assertTrue(P256SecretKey.isValid(it), "Expected `$it` to be recognized as valid P256SecretKey string.")
        }

        invalidStrings.forEach {
            assertFalse(P256SecretKey.isValid(it), "Expected `$it` to be recognized as invalid P256SecretKey string.")
        }
    }

    @Test
    fun `should recognize valid and invalid P256SecretKey bytes`() {
        validBytes.forEach {
            assertTrue(P256SecretKey.isValid(it), "Expected `$it` to be recognized as valid P256SecretKey.")
            assertTrue(P256SecretKey.isValid(it.toList()), "Expected `$it` to be recognized as valid P256SecretKey.")
        }

        invalidBytes.forEach {
            assertFalse(P256SecretKey.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid P256SecretKey.")
            assertFalse(P256SecretKey.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid P256SecretKey.")
        }
    }

    @Test
    fun `should create P256SecretKey from valid string`() {
        validStrings.forEach {
            assertEquals(it, P256SecretKey.createValue(it).base58)
            assertEquals(it, P256SecretKey.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create P256SecretKey from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { P256SecretKey.createValue(it) }
            assertNull(P256SecretKey.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "p2sk2Xoduh8dx6B3smV81NMV25cYpZJj7yYWMRARedzyJae8SB9auw",
            "p2sk3BZXZmp7uuxECt5pWpuKMqtBXxkmNTxdRz1P5tVLUU6Aap4E2U",
            "p2sk2taS6rt8ZAgB3WT1uqBDzun1pjnvgx57FCxfYAZ3FYxHwLUEt8",
            "p2sk3MLp3kREcTPpuPPJ2di77tz6cn5xEicsQUs9vCBaBup979LE1m",
            "p2sk2ZW9sHUuV7SzFQV1GaenfbpG2AbuPotBDWAJVUZNwMLerksdjK",
            "p2sk35y4vPHcfsBnj48twxMeBCAqUa2wZp6okNwm73BHMM3jhFSUzv",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "2Xoduh8dx6B3smV81NMV25cYpZJj7yYWMRARedzyJae8SB9auw",
            "p2sk3BZXZmp7uuxECt5pWpuKMqtBXxkmNTxdRz1P5tVLUU6Aap",
            "p2sk2taS6rt8ZAgB3WT1uqBDzun1pjnvgx57FCxfYAZ3FYxHwLUEt8k3ML",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "190266c4a6fa1ea1a0529a935be7d9153cd24edda038823a148a93782db0af31",
            "1051eebd190266c4a6fa1ea1a0529a935be7d9153cd24edda038823a148a93782db0af31",
            "6ebd9c4f4c31be11e621c5f737fa192858732243f0416a2d823107f01b7115a4",
            "1051eebd6ebd9c4f4c31be11e621c5f737fa192858732243f0416a2d823107f01b7115a4",
            "482cf0aa6668a15ab8fdc974966b3f08ce1a780f86ac7a46fdb97caf3fa30aa5",
            "1051eebd482cf0aa6668a15ab8fdc974966b3f08ce1a780f86ac7a46fdb97caf3fa30aa5",
            "84f2f70e8b5c6f30b848189e8b2169eec547e67a636b5ab8b103dd1964fa1690",
            "1051eebd84f2f70e8b5c6f30b848189e8b2169eec547e67a636b5ab8b103dd1964fa1690",
            "1cddbd5c7f354bf593c751a01634669726152f4e289eab86afdcb93a2417fe32",
            "1051eebd1cddbd5c7f354bf593c751a01634669726152f4e289eab86afdcb93a2417fe32",
            "6209dc2a453c4316b714d6c758c01bb867ba0700d310cfb79b65e036b6d5554a",
            "1051eebd6209dc2a453c4316b714d6c758c01bb867ba0700d310cfb79b65e036b6d5554a",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "190266c4a6fa1ea1a0529a935be7d9153cd24edda038823a148a93",
            "6ebd9c4f4c31be11e621c5f737fa192858732243f0416a2d823107f01b7115a4bd9c",
            "2051eebd6209dc2a453c4316b714d6c758c01bb867ba0700d310cfb79b65e036b6d5554a",
        ).map { it.asHexString().toByteArray() }
}
