package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class BlockMetadataHashTest {

    @Test
    fun `should recognize valid and invalid BlockMetadataHash strings`() {
        validStrings.forEach {
            assertTrue(BlockMetadataHash.isValid(it), "Expected `$it` to be recognized as valid BlockMetadataHash string.")
        }

        invalidStrings.forEach {
            assertFalse(BlockMetadataHash.isValid(it), "Expected `$it` to be recognized as invalid BlockMetadataHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid BlockMetadataHash bytes`() {
        validBytes.forEach {
            assertTrue(BlockMetadataHash.isValid(it), "Expected `$it` to be recognized as valid BlockMetadataHash.")
            assertTrue(BlockMetadataHash.isValid(it.toList()), "Expected `$it` to be recognized as valid BlockMetadataHash.")
        }

        invalidBytes.forEach {
            assertFalse(BlockMetadataHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid BlockMetadataHash.")
            assertFalse(BlockMetadataHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid BlockMetadataHash.")
        }
    }

    @Test
    fun `should create BlockMetadataHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, BlockMetadataHash.createValue(it).base58)
            assertEquals(it, BlockMetadataHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create BlockMetadataHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { BlockMetadataHash.createValue(it) }
            assertNull(BlockMetadataHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "bm45XAJVF8Mry4hjaiD8413Jqy86rZaLbRzDzJBdnzmUgFwymm2L",
            "bm3wQn3cyzkGyAopPDqtmmGhR5VhC285qkKepWu34Hvc3e2gt5kg",
            "bm2sDoByzvAECDeEZRbHVkuqFTA5kshqjT5Fe6mrAig8CouKR1A5",
            "bm3NFV5kAMAQz1SRctRjLWjr8ZNcvNgtUct6FADsWER5bcoM6LTk",
            "bm31CXrD4xDwgoMLc3YUc8hkfmM9EQYHyEWt25t2fxqQvRgBZf8U",
            "bm4L9NHZA2poiytpRR9FkgedFmkuPVh5ZyYwDNWWJybgqyrWP1XA",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "45XAJVF8Mry4hjaiD8413Jqy86rZaLbRzDzJBdnzmUgFwymm2L",
            "bm3wQn3cyzkGyAopPDqtmmGhR5VhC285qkKepWu34Hvc3e2g",
            "bm2sDoByzvAECDeEZRbHVkuqFTA5kshqjT5Fe6mrAig8CouKR1A5NFV5",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "c66278bd29f690b0324b09dc4703d548c5ae1bb7c305c13cd3904fd119938558",
            "eaf9c66278bd29f690b0324b09dc4703d548c5ae1bb7c305c13cd3904fd119938558",
            "b3f8424d7889d720c23d2be8f179abbe88ac1802595d9371ceecbb8ce9800f20",
            "eaf9b3f8424d7889d720c23d2be8f179abbe88ac1802595d9371ceecbb8ce9800f20",
            "26c2e57aa6360a9453763b226d591a26cc9db8c46c218de711dd2c6245dbb592",
            "eaf926c2e57aa6360a9453763b226d591a26cc9db8c46c218de711dd2c6245dbb592",
            "68ace96e369908134ced52431c0447b4c99ec947ad0ebabe7fd9a89794eb5582",
            "eaf968ace96e369908134ced52431c0447b4c99ec947ad0ebabe7fd9a89794eb5582",
            "38e0754361bebfcb93d8228efa31896f743ccda4446861b06ba65bfd8c05699d",
            "eaf938e0754361bebfcb93d8228efa31896f743ccda4446861b06ba65bfd8c05699d",
            "e7973f340a6f06f370285ada635f8b43866380f20ded28f61fd880b84a1732ab",
            "eaf9e7973f340a6f06f370285ada635f8b43866380f20ded28f61fd880b84a1732ab",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "c66278bd29f690b0324b09dc4703d548c5ae1bb7c305c13cd3904fd119",
            "b3f8424d7889d720c23d2be8f179abbe88ac1802595d9371ceecbb8ce9800f20f842",
            "1af9e7973f340a6f06f370285ada635f8b43866380f20ded28f61fd880b84a1732ab",
        ).map { it.asHexString().toByteArray() }
}
