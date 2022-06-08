package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class Secp256K1EncryptedSecretKeyTest {

    @Test
    fun `should recognize valid and invalid Secp256K1EncryptedSecretKey strings`() {
        validStrings.forEach {
            assertTrue(Secp256K1EncryptedSecretKey.isValid(it), "Expected `$it` to be recognized as valid Secp256K1EncryptedSecretKey string.")
        }

        invalidStrings.forEach {
            assertFalse(Secp256K1EncryptedSecretKey.isValid(it), "Expected `$it` to be recognized as invalid Secp256K1EncryptedSecretKey string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Secp256K1EncryptedSecretKey bytes`() {
        validBytes.forEach {
            assertTrue(Secp256K1EncryptedSecretKey.isValid(it), "Expected `$it` to be recognized as valid Secp256K1EncryptedSecretKey.")
            assertTrue(Secp256K1EncryptedSecretKey.isValid(it.toList()), "Expected `$it` to be recognized as valid Secp256K1EncryptedSecretKey.")
        }

        invalidBytes.forEach {
            assertFalse(Secp256K1EncryptedSecretKey.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1EncryptedSecretKey.")
            assertFalse(Secp256K1EncryptedSecretKey.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1EncryptedSecretKey.")
        }
    }

    @Test
    fun `should create Secp256K1EncryptedSecretKey from valid string`() {
        validStrings.forEach {
            assertEquals(it, Secp256K1EncryptedSecretKey.createValue(it).base58)
            assertEquals(it, Secp256K1EncryptedSecretKey.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Secp256K1EncryptedSecretKey from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Secp256K1EncryptedSecretKey.createValue(it) }
            assertNull(Secp256K1EncryptedSecretKey.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "spesk2D3bthnmSSESbkGycZK1Q5NxUVPc3m44CmxQ2ySrakWXMPhaxm8C5UfKGc2dwxdUFhSpr97R4mqe1NXq7MR",
            "spesk1c34nXKSWjMcwjgJF3uuTULiufM8Nz4kc5FoECNKqj5wGbenikXL5FsgGNDLVD3nFYHrwz5MLWjXbf4A3zu",
            "spesk1vtJjW6V6LVgcgbsqUaahVC7DWJUpbSp8TspmXdChQ8dEZypepv5uN1sjdFSLu6bJypCZdPEqdBtqEsVUAs",
            "spesk1oo6uWeqxPcw5phgvTF55eQebWF92s82wYtTzX6265GGhWsnGEd9yX23SSWL7crCKzX8kibTqb2KhKwRk2S",
            "spesk1YTLZUEKJk3jrcxUev7633YF4UjS9ZmXhf6CLaQwhVosFQkoyChrYvgTwwE3V4B1whaaQmpvTVLDc3p6jef",
            "spesk22JB9MgagVZwLo423d7yKNepCRqrhLVFiJyHbYajDtE7rDTWiQhUeWRR3YgqKDENNDzGFeH9WqHGdF3xKnT",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "2D3bthnmSSESbkGycZK1Q5NxUVPc3m44CmxQ2ySrakWXMPhaxm8C5UfKGc2dwxdUFhSpr97R4mqe1NXq7MR",
            "spesk1c34nXKSWjMcwjgJF3uuTULiufM8Nz4kc5FoECNKqj5wGbenikXL5FsgGNDLVD3nFYHrwz5MLWjXbf4",
            "spesk1vtJjW6V6LVgcgbsqUaahVC7DWJUpbSp8TspmXdChQ8dEZypepv5uN1sjdFSLu6bJypCZdPEqdBtqEsVUAssk1o",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "ff33d354378ce14e436c378fbda8c6075600298f71e70ed363998de484a75d12af6eb0e96bbe64c67804e510301ede1785466975099b759c",
            "09edf1ae96ff33d354378ce14e436c378fbda8c6075600298f71e70ed363998de484a75d12af6eb0e96bbe64c67804e510301ede1785466975099b759c",
            "39a53f9e3f6705e8c4ef72bcb5d629488ec438aad82c00c436548338060fb9a8d1c431df232ab8c99ac4dcceb738c94e3ccfeb885b4a7b31",
            "09edf1ae9639a53f9e3f6705e8c4ef72bcb5d629488ec438aad82c00c436548338060fb9a8d1c431df232ab8c99ac4dcceb738c94e3ccfeb885b4a7b31",
            "a402980d3a853ccd89abacf39cd794b55bb052bb7ec188c2398008316c7b038a3a59e11f1d28249b9fcd884402fd895ceb2ffbe9c8970754",
            "09edf1ae96a402980d3a853ccd89abacf39cd794b55bb052bb7ec188c2398008316c7b038a3a59e11f1d28249b9fcd884402fd895ceb2ffbe9c8970754",
            "7c00bc70a2a847c61ba50c69a10db7f7ef2195bb3d7106f1d8a45628aefa42bfa4c505bb32b9e603799400734cbc67cd4db29397dcc53626",
            "09edf1ae967c00bc70a2a847c61ba50c69a10db7f7ef2195bb3d7106f1d8a45628aefa42bfa4c505bb32b9e603799400734cbc67cd4db29397dcc53626",
            "256f5da86d5f2e3f47c1dee8d23f40a116eaa35ab8596f794fea20b2d01eae932ae3f351c699fcc8895b709a5c7bbe12f1bd0c2b36cad5aa",
            "09edf1ae96256f5da86d5f2e3f47c1dee8d23f40a116eaa35ab8596f794fea20b2d01eae932ae3f351c699fcc8895b709a5c7bbe12f1bd0c2b36cad5aa",
            "c28c226e6242c6f0ac25a5fc62d6086d6f9e9d6ee3038c5a4f7341c12477b10b1d119b0b5de27de6c2e0b13a2cf5208059912b2aaa97aa45",
            "09edf1ae96c28c226e6242c6f0ac25a5fc62d6086d6f9e9d6ee3038c5a4f7341c12477b10b1d119b0b5de27de6c2e0b13a2cf5208059912b2aaa97aa45",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "ff33d354378ce14e436c378fbda8c6075600298f71e70ed363998de484a75d12af6eb0e96bbe64c67804e510301ede178546",
            "39a53f9e3f6705e8c4ef72bcb5d629488ec438aad82c00c436548338060fb9a8d1c431df232ab8c99ac4dcceb738c94e3ccfeb885b4a7b31a53f",
            "19edf1ae96c28c226e6242c6f0ac25a5fc62d6086d6f9e9d6ee3038c5a4f7341c12477b10b1d119b0b5de27de6c2e0b13a2cf5208059912b2aaa97aa45",
        ).map { it.asHexString().toByteArray() }
}