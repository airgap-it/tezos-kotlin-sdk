package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class OperationMetadataListListHashTest {

    @Test
    fun `should recognize valid and invalid OperationMetadataListListHash strings`() {
        validStrings.forEach {
            assertTrue(OperationMetadataListListHash.isValid(it), "Expected `$it` to be recognized as valid OperationMetadataListListHash string.")
        }

        invalidStrings.forEach {
            assertFalse(OperationMetadataListListHash.isValid(it), "Expected `$it` to be recognized as invalid OperationMetadataListListHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid OperationMetadataListListHash bytes`() {
        validBytes.forEach {
            assertTrue(OperationMetadataListListHash.isValid(it), "Expected `$it` to be recognized as valid OperationMetadataListListHash.")
            assertTrue(OperationMetadataListListHash.isValid(it.toList()), "Expected `$it` to be recognized as valid OperationMetadataListListHash.")
        }

        invalidBytes.forEach {
            assertFalse(OperationMetadataListListHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationMetadataListListHash.")
            assertFalse(OperationMetadataListListHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationMetadataListListHash.")
        }
    }

    @Test
    fun `should create OperationMetadataListListHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, OperationMetadataListListHash.createValue(it).base58)
            assertEquals(it, OperationMetadataListListHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create OperationMetadataListListHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { OperationMetadataListListHash.createValue(it) }
            assertNull(OperationMetadataListListHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "LLr1peCfquFwwYuAvWcDyWoNJYqbY4wrXbtKdNjo84UsJKkW1FkN4",
            "LLr2gzjvyMiTDCUBSQy14mpNQyTV4xzsyLJY4m56cY7uURwtXGVds",
            "LLr2zFGBRA32gwRCgv3tiZz2VkmopncZEKDqfxs5en6NU73CsprR2",
            "LLr2HwjGjAuyB7DevzHWwdXNTZx8i6wkdD8ftP5jYtxTF4j2nZAXP",
            "LLr2cVRqcLmVavsim4teh7K8xkjrrWcXnzvcmBZmUcc6iNC4wXLp2",
            "LLr2ZUZHUNW15gk3N2joo5JtJb8UR5uAB5VRoG5veXzcbN23zPfin",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "1peCfquFwwYuAvWcDyWoNJYqbY4wrXbtKdNjo84UsJKkW1FkN4",
            "LLr2gzjvyMiTDCUBSQy14mpNQyTV4xzsyLJY4m56cY7uURwtX",
            "LLr2zFGBRA32gwRCgv3tiZz2VkmopncZEKDqfxs5en6NU73CsprR22Hwj",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "4e8b5cb531d764dd7b8052e23ceab7058a21bc3e27dcc1f5ef2e6e5e8009d737",
            "1d9fb64e8b5cb531d764dd7b8052e23ceab7058a21bc3e27dcc1f5ef2e6e5e8009d737",
            "c0e12bbbcf1ecf141c76b03d92329cfd895f25aafafb6001460775ee4f76cbaa",
            "1d9fb6c0e12bbbcf1ecf141c76b03d92329cfd895f25aafafb6001460775ee4f76cbaa",
            "e80c74735df66f0139a6dc9048d485ec4c1f75c63b37600a9381c7731cf363e7",
            "1d9fb6e80c74735df66f0139a6dc9048d485ec4c1f75c63b37600a9381c7731cf363e7",
            "8c89910162431f79939dcdd5ef845ccd287a3f0480c32e067373dd04f405814c",
            "1d9fb68c89910162431f79939dcdd5ef845ccd287a3f0480c32e067373dd04f405814c",
            "b6a649a29f132f0d8f4b7c9f131e4aa5c9cf1ebcd1e77b8505b46fe1002afbda",
            "1d9fb6b6a649a29f132f0d8f4b7c9f131e4aa5c9cf1ebcd1e77b8505b46fe1002afbda",
            "afcdb6d8422d69841305a499dd90111f398e99a5c9be3ec23d51c0951d029be2",
            "1d9fb6afcdb6d8422d69841305a499dd90111f398e99a5c9be3ec23d51c0951d029be2",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "4e8b5cb531d764dd7b8052e23ceab7058a21bc3e27dcc1f5ef2e6e5e",
            "c0e12bbbcf1ecf141c76b03d92329cfd895f25aafafb6001460775ee4f76cbaae12b",
            "2d9fb6afcdb6d8422d69841305a499dd90111f398e99a5c9be3ec23d51c0951d029be2",
        ).map { it.asHexString().toByteArray() }
}
