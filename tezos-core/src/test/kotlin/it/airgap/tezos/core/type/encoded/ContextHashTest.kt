package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class ContextHashTest {

    @Test
    fun `should recognize valid and invalid ContextHash strings`() {
        validStrings.forEach {
            assertTrue(ContextHash.isValid(it), "Expected `$it` to be recognized as valid ContextHash string.")
        }

        invalidStrings.forEach {
            assertFalse(ContextHash.isValid(it), "Expected `$it` to be recognized as invalid ContextHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid ContextHash bytes`() {
        validBytes.forEach {
            assertTrue(ContextHash.isValid(it), "Expected `$it` to be recognized as valid ContextHash.")
            assertTrue(ContextHash.isValid(it.toList()), "Expected `$it` to be recognized as valid ContextHash.")
        }

        invalidBytes.forEach {
            assertFalse(ContextHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid ContextHash.")
            assertFalse(ContextHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid ContextHash.")
        }
    }

    @Test
    fun `should create ContextHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, ContextHash.createValue(it).base58)
            assertEquals(it, ContextHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create ContextHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { ContextHash.createValue(it) }
            assertNull(ContextHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "CoVvWUEWvAwGbzeLxktBFU6pPFNdJrJtsdUWPQf4LJR3RgiAFwPY",
            "CoUoSBVYuk5GPtVuYXVfK8aob38C1m5i5HhKZhCR4DHuPv4Z7ZKi",
            "CoWFn8hrWKT1JZViLHUr2cVtSyZsnDMu2Xh3N1dRztUu1sxVCWyJ",
            "CoWAibr87qsv8Ftx8Qyqv4FQ3Vv2PTSzWUdx4xG1bR2pDn8EDNpp",
            "CoV9yP6xR234qmncgFDbBVKjoRLxaAwarGQ54dDcBcEiguywg8us",
            "CoV2WHkHJwoeQPAxowGrrrGtwCYrDiY6ve7ki4pNzSyyt2V4GmBN",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "VvWUEWvAwGbzeLxktBFU6pPFNdJrJtsdUWPQf4LJR3RgiAFwPY",
            "CoUoSBVYuk5GPtVuYXVfK8aob38C1m5i5HhKZhCR4DHuPv4Z",
            "CoWFn8hrWKT1JZViLHUr2cVtSyZsnDMu2Xh3N1dRztUu1sxVCWyJAibr",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "a87b114ad3b5f6809dbd77537724abee563a4964fa23f67b2daaa4adc9018383",
            "4fc7a87b114ad3b5f6809dbd77537724abee563a4964fa23f67b2daaa4adc9018383",
            "14b8ee5541eaf65880066efd8875fa2f91a736bd44a86b2aa73e0ea2022188b6",
            "4fc714b8ee5541eaf65880066efd8875fa2f91a736bd44a86b2aa73e0ea2022188b6",
            "d43c5a61c01bd4ee06810aac6e5fc5588ccd34b88751fcfb62ffc417a5ab015d",
            "4fc7d43c5a61c01bd4ee06810aac6e5fc5588ccd34b88751fcfb62ffc417a5ab015d",
            "c8be8eedbba763e7aba25e7f6f9d4060e828d44c83bafa1ddde5d53e5492fedf",
            "4fc7c8be8eedbba763e7aba25e7f6f9d4060e828d44c83bafa1ddde5d53e5492fedf",
            "435b351300e9f4662570c4345f81766ec66d1ec45842faaaf61bc9293de324c0",
            "4fc7435b351300e9f4662570c4345f81766ec66d1ec45842faaaf61bc9293de324c0",
            "3266bb225a171f0c74ff6f6be4016494ca4379228b79cb68384f3508b35fc6bb",
            "4fc73266bb225a171f0c74ff6f6be4016494ca4379228b79cb68384f3508b35fc6bb",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "a87b114ad3b5f6809dbd77537724abee563a4964fa23f67b2daaa4adc9",
            "14b8ee5541eaf65880066efd8875fa2f91a736bd44a86b2aa73e0ea2022188b6b8ee",
            "1fc73266bb225a171f0c74ff6f6be4016494ca4379228b79cb68384f3508b35fc6bb",
        ).map { it.asHexString().toByteArray() }
}