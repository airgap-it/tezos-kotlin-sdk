package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class Ed25519EncryptedSeedTest {

    @Test
    fun `should recognize valid and invalid Ed25519EncryptedSeed strings`() {
        validStrings.forEach {
            assertTrue(Ed25519EncryptedSeed.isValid(it), "Expected `$it` to be recognized as valid Ed25519EncryptedSeed string.")
        }

        invalidStrings.forEach {
            assertFalse(Ed25519EncryptedSeed.isValid(it), "Expected `$it` to be recognized as invalid Ed25519EncryptedSeed string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Ed25519EncryptedSeed bytes`() {
        validBytes.forEach {
            assertTrue(Ed25519EncryptedSeed.isValid(it), "Expected `$it` to be recognized as valid Ed25519EncryptedSeed.")
            assertTrue(Ed25519EncryptedSeed.isValid(it.toList()), "Expected `$it` to be recognized as valid Ed25519EncryptedSeed.")
        }

        invalidBytes.forEach {
            assertFalse(Ed25519EncryptedSeed.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519EncryptedSeed.")
            assertFalse(Ed25519EncryptedSeed.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519EncryptedSeed.")
        }
    }

    @Test
    fun `should create Ed25519EncryptedSeed from valid string`() {
        validStrings.forEach {
            assertEquals(it, Ed25519EncryptedSeed.createValue(it).base58)
            assertEquals(it, Ed25519EncryptedSeed.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Ed25519EncryptedSeed from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Ed25519EncryptedSeed.createValue(it) }
            assertNull(Ed25519EncryptedSeed.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "edesk1ToDdQUemaRoaJTzKufqrd5vYLksg5ebPhZtCRMrNtCxLZHfQrVwUrYhaJNmJj4FMSibL1wT9axjST6GgmB",
            "edesk1jXP6UVVbNhef2CZE1mxvMeUkNEDinBQL73NFeTQmTVCw3bvmb5EBYoDXLiqP62VgnaV6Too29CTK1EVjZX",
            "edesk1iJFSWcsx61c2ufFGajPVwKXHGrmKWvDW2cni5fxhMztKpyLyXJXNHLFnXqvFCLnVbuyXaDQCURRfLNyi5P",
            "edesk1Q4tQMZKHwGf96G6prrCXvYCN1iayBsyApHUNE4Sacn8GgL9eMu3gGX8qwiJS7299yEiQTGJD3YAF2VGs7d",
            "edesk1xQKjCHkZnSS4sodFLEd4evMFz9gCF13cViAi9iBLzduEAyjui7xFVKFJuz7X92NvrRN9CmWmUP9mAjpwjG",
            "edesk1GdBPJMLEZJjYsZV5srwUeLXEvS9eK8Nicrchznb8H8UbJbfz7EVXVsKLJw9aq4HRtp6GyeyXu3cQaDo4He",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "1ToDdQUemaRoaJTzKufqrd5vYLksg5ebPhZtCRMrNtCxLZHfQrVwUrYhaJNmJj4FMSibL1wT9axjST6GgmB",
            "edesk1jXP6UVVbNhef2CZE1mxvMeUkNEDinBQL73NFeTQmTVCw3bvmb5EBYoDXLiqP62VgnaV6Too29CTK1E",
            "edesk1iJFSWcsx61c2ufFGajPVwKXHGrmKWvDW2cni5fxhMztKpyLyXJXNHLFnXqvFCLnVbuyXaDQCURRfLNyi5Psk1Q",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "4290dd1892e3584146dcd88823dde08c5e8e9d48f2700feb80ce111780dfe34a36d17b3b1aa362c7ed6e24cb65bece8932506df6dbc8137f",
            "075a3cb3294290dd1892e3584146dcd88823dde08c5e8e9d48f2700feb80ce111780dfe34a36d17b3b1aa362c7ed6e24cb65bece8932506df6dbc8137f",
            "9b501defdebf9d751b30f0e688389afe3c2b0bf5587231c9e9d7d848aeae0580f40dd274aa5bb7a68c459c6871f2468803bad273ada2c90b",
            "075a3cb3299b501defdebf9d751b30f0e688389afe3c2b0bf5587231c9e9d7d848aeae0580f40dd274aa5bb7a68c459c6871f2468803bad273ada2c90b",
            "94646eba8428a8fae30ab4351192dc6a89838a5585e4a9981c031d0b44890b3de61a699384317d9548b00fa8e27be02f1285304065540fcd",
            "075a3cb32994646eba8428a8fae30ab4351192dc6a89838a5585e4a9981c031d0b44890b3de61a699384317d9548b00fa8e27be02f1285304065540fcd",
            "2d84b1e4c17e32fdafcd29c199cec30430f6201d84c2b8e16c27f48cd15c95dc5255c5a45b1b7fb02fafb2ccef7c5a842bad203f72fbe4c1",
            "075a3cb3292d84b1e4c17e32fdafcd29c199cec30430f6201d84c2b8e16c27f48cd15c95dc5255c5a45b1b7fb02fafb2ccef7c5a842bad203f72fbe4c1",
            "e3fc340f698940392fd62c824353094199204f1b53da4d39e53ad6e3928071d90a6207092aaf197627f91005b388fb6574b3d67251e7814e",
            "075a3cb329e3fc340f698940392fd62c824353094199204f1b53da4d39e53ad6e3928071d90a6207092aaf197627f91005b388fb6574b3d67251e7814e",
            "03842959add2f04649ebff4a01eb238807dc35649e71438f7e4b701aed0e48c27c3ee4970af04650ca45bec91b5ab2a906eedb753ab0f5be",
            "075a3cb32903842959add2f04649ebff4a01eb238807dc35649e71438f7e4b701aed0e48c27c3ee4970af04650ca45bec91b5ab2a906eedb753ab0f5be",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "4290dd1892e3584146dcd88823dde08c5e8e9d48f2700feb80ce111780dfe34a36d17b3b1aa362c7ed6e24cb65bece893250",
            "9b501defdebf9d751b30f0e688389afe3c2b0bf5587231c9e9d7d848aeae0580f40dd274aa5bb7a68c459c6871f2468803bad273ada2c90b501d",
            "175a3cb32903842959add2f04649ebff4a01eb238807dc35649e71438f7e4b701aed0e48c27c3ee4970af04650ca45bec91b5ab2a906eedb753ab0f5be",
        ).map { it.asHexString().toByteArray() }
}
