package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class ContractHashTest {

    @Test
    fun `should recognize valid and invalid ContractHash strings`() {
        validStrings.forEach {
            assertTrue(ContractHash.isValid(it), "Expected `$it` to be recognized as valid ContractHash string.")
        }

        invalidStrings.forEach {
            assertFalse(ContractHash.isValid(it), "Expected `$it` to be recognized as invalid ContractHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid ContractHash bytes`() {
        validBytes.forEach {
            assertTrue(ContractHash.isValid(it), "Expected `$it` to be recognized as valid ContractHash.")
            assertTrue(ContractHash.isValid(it.toList()), "Expected `$it` to be recognized as valid ContractHash.")
        }

        invalidBytes.forEach {
            assertFalse(ContractHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid ContractHash.")
            assertFalse(ContractHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid ContractHash.")
        }
    }

    @Test
    fun `should create ContractHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, ContractHash.createValue(it).base58)
            assertEquals(it, ContractHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create ContractHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { ContractHash.createValue(it) }
            assertNull(ContractHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "KT1WPbAPYu5ZRDmhp4XLVUnSY2Ubv4jZaKtQ",
            "KT1Mnu669pGJgk1yu3EvJZM2s6ZAFaGUfSVo",
            "KT1B289KQSearRBdLsTkutjMGP15utSx3ypZ",
            "KT1MSJ1kBcYmez5xn86yKcVo7YyvkKnu2A6Z",
            "KT1EQq8EjKN58qH2yX9BZTL1eE84tzXodQX5",
            "KT1CnjrrwFMhiAQnJVVyyycz2cPV9Ye1M7Qv",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "WPbAPYu5ZRDmhp4XLVUnSY2Ubv4jZaKtQ",
            "KT1Mnu669pGJgk1yu3EvJZM2s6ZAFaGU",
            "KT1B289KQSearRBdLsTkutjMGP15utSx3ypZMSJ1",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "ef3581f561f0ca1ab14301d33f773cd96c856760",
            "025a79ef3581f561f0ca1ab14301d33f773cd96c856760",
            "90e4e80eb1fc9069bbbd2cf5229c4662457f0c44",
            "025a7990e4e80eb1fc9069bbbd2cf5229c4662457f0c44",
            "1abbd4240c1a552df70631a1497395c8b1b385a7",
            "025a791abbd4240c1a552df70631a1497395c8b1b385a7",
            "8cff4ee53779653d523340f1f0ba87f203cddc6c",
            "025a798cff4ee53779653d523340f1f0ba87f203cddc6c",
            "3fef9113e518bb8e2a6670c3d1b218105a58dd9b",
            "025a793fef9113e518bb8e2a6670c3d1b218105a58dd9b",
            "2e24123b57e870da12b91009d88d111691ba0e2d",
            "025a792e24123b57e870da12b91009d88d111691ba0e2d",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "ef3581f561f0ca1ab14301d33f773cd9",
            "90e4e80eb1fc9069bbbd2cf5229c4662457f0c44e4e8",
            "125a792e24123b57e870da12b91009d88d111691ba0e2d",
        ).map { it.asHexString().toByteArray() }
}