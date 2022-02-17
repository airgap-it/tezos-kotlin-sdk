package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class OperationHashTest {

    @Test
    fun `should recognize valid and invalid OperationHash strings`() {
        validStrings.forEach {
            assertTrue(OperationHash.isValid(it), "Expected `$it` to be recognized as valid OperationHash string.")
        }

        invalidStrings.forEach {
            assertFalse(OperationHash.isValid(it), "Expected `$it` to be recognized as invalid OperationHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid OperationHash bytes`() {
        validBytes.forEach {
            assertTrue(OperationHash.isValid(it), "Expected `$it` to be recognized as valid OperationHash.")
            assertTrue(OperationHash.isValid(it.toList()), "Expected `$it` to be recognized as valid OperationHash.")
        }

        invalidBytes.forEach {
            assertFalse(OperationHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationHash.")
            assertFalse(OperationHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationHash.")
        }
    }

    @Test
    fun `should create OperationHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, OperationHash.createValue(it).base58)
            assertEquals(it, OperationHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create OperationHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { OperationHash.createValue(it) }
            assertNull(OperationHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "opPqf1Cuq1QtogG8qgi46Xn3MCiBvJTZ3yVEbTrgfW7c1bi65Px",
            "oojHuV9UhbAKGGAfJzZZHzS2XSFyuuXY49kHSvxfTARkESZN8vV",
            "oopsq3kgARNFAgcLtVpx7AXrR3gX9ru4f8t6Lwdk42SrF5CGM8p",
            "op9moiNdkR4bLWk8E8cZYr31E4UZzud14qxzLrr2udyaSqDbzxE",
            "opZm8tGu7XZJui1potc2gNZ1N8V9QaUFMpDwp2c8vgHFz8yH2Sy",
            "ooCc9WG6MUdPRedAfwMcjzs5vqSvJkSAJc1d91D2N4nZ6QyM1i8",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "pPqf1Cuq1Qt",
            "oojHuV9UhbAKGGAfJzZZHzS2XSFyuuXY49kHSvxfTARkESZ",
            "oopsq3kgARNFAgcLtVpx7AXrR3gX9ru4f8t6Lwdk42SrF5CGM8pmoiN",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "e6c1dbf057fe8269b1f7693673e4ba8c3154a308ac4af5fcb49cf4f62345baa8",
            "0574e6c1dbf057fe8269b1f7693673e4ba8c3154a308ac4af5fcb49cf4f62345baa8",
            "8f3b0aa6e04625708e8f8497bac563773f0f7ee693ce09a1afd0bc581ca802bb",
            "05748f3b0aa6e04625708e8f8497bac563773f0f7ee693ce09a1afd0bc581ca802bb",
            "9be96bdb188e7cecd1e45d5bd6bfa3f70cb456fa66c06ff49ff0f32f7d9990a8",
            "05749be96bdb188e7cecd1e45d5bd6bfa3f70cb456fa66c06ff49ff0f32f7d9990a8",
            "c6d15d8f23dcbae77431977b5fb16500aa7a59f3c2c36bc2fe47bd9f37d9b1aa",
            "0574c6d15d8f23dcbae77431977b5fb16500aa7a59f3c2c36bc2fe47bd9f37d9b1aa",
            "fd495c0f3e3e4f9d847e810a99c316c463959762527546d3e606dcb48a0fee4d",
            "0574fd495c0f3e3e4f9d847e810a99c316c463959762527546d3e606dcb48a0fee4d",
            "498e33c5f8a80ac9e3dd45f29e8d6af964b75305c446a253ed8d0084b565cfb0",
            "0574498e33c5f8a80ac9e3dd45f29e8d6af964b75305c446a253ed8d0084b565cfb0",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "e6c1dbf057fe8269b1f7693673e4ba8c3154a308ac4af5fcb49cf4f623",
            "8f3b0aa6e04625708e8f8497bac563773f0f7ee693ce09a1afd0bc581ca802bb3b0a",
            "1574498e33c5f8a80ac9e3dd45f29e8d6af964b75305c446a253ed8d0084b565cfb0",
        ).map { it.asHexString().toByteArray() }
}