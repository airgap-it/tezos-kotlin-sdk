package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class Secp256K1EncryptedScalarTest {

    @Test
    fun `should recognize valid and invalid Secp256K1EncryptedScalar strings`() {
        validStrings.forEach {
            assertTrue(Secp256K1EncryptedScalar.isValid(it), "Expected `$it` to be recognized as valid Secp256K1EncryptedScalar string.")
        }

        invalidStrings.forEach {
            assertFalse(Secp256K1EncryptedScalar.isValid(it), "Expected `$it` to be recognized as invalid Secp256K1EncryptedScalar string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Secp256K1EncryptedScalar bytes`() {
        validBytes.forEach {
            assertTrue(Secp256K1EncryptedScalar.isValid(it), "Expected `$it` to be recognized as valid Secp256K1EncryptedScalar.")
            assertTrue(Secp256K1EncryptedScalar.isValid(it.toList()), "Expected `$it` to be recognized as valid Secp256K1EncryptedScalar.")
        }

        invalidBytes.forEach {
            assertFalse(Secp256K1EncryptedScalar.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1EncryptedScalar.")
            assertFalse(Secp256K1EncryptedScalar.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1EncryptedScalar.")
        }
    }

    @Test
    fun `should create Secp256K1EncryptedScalar from valid string`() {
        validStrings.forEach {
            assertEquals(it, Secp256K1EncryptedScalar.createValue(it).base58)
            assertEquals(it, Secp256K1EncryptedScalar.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Secp256K1EncryptedScalar from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Secp256K1EncryptedScalar.createValue(it) }
            assertNull(Secp256K1EncryptedScalar.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "seesk715koVQUPjhHPvaU1jxPSp4VSRWbRyQ1BmbpmAsNxLYmPGc2G8PQuvGFArkNcCfKGK155FBFMLqeBtmGNcJzHDq3",
            "seesk5BxfjKz5m4GZ6RhKRn1doVQ7f9mw6k5k19n9jrzAXpoHpAMncAf51a6Cgjb73GsKm5vTr71YVT1YAK77JoTfNzS9",
            "seesk89pKHJxXJuzMA1o2j8fQPBV7WNivNasZo2tCwnhcbzkmEUMobAzKign2fa7zBgWu9SVoEVpBpVHnP1Xr75RYhoKC",
            "seesk6CLgEGukjLgfkUXrhPZ7AZMzGCoaB3yqgdTDoA9nCjtvTirUo517JZH4cwh3nQUepTrgmpqTUM9yYmhdRNCUD1p1",
            "seesk95Scfgekcz15VicKKaXE9Enndku7Dwt5avwSoukCJe2hUUgZsUypCJyTNR8FA2DXArabd5R48g6C64sgHjxBgibK",
            "seesk7ZpdXF1uvGSbRX6UnZwf2a33zjKrCrkjJNURBBopJmb9oAZdCAs8riBdLVKfAF8anCYCxE1S7U3FpETRNJKZ5uKn",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "e",
            "seesk5BxfjKz5m4GZ6RhKRn1doVQ7f9mw6k5k19n9jrzAXpoHpAMncAf51a6Cgjb73GsKm5vTr71YVT1YAK77JoTf",
            "seesk89pKHJxXJuzMA1o2j8fQPBV7WNivNasZo2tCwnhcbzkmEUMobAzKign2fa7zBgWu9SVoEVpBpVHnP1Xr75RYhoKCsk6C",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "6fb7e30af81d1f24e0093cf0d3e2314238e311eae4cb75b3621fd4048874de4ed655f8995d4f515070aeb8b189c481b5c1c2c7ca4e634ec4a05b6a1d",
            "01832456f86fb7e30af81d1f24e0093cf0d3e2314238e311eae4cb75b3621fd4048874de4ed655f8995d4f515070aeb8b189c481b5c1c2c7ca4e634ec4a05b6a1d",
            "15109e4d8049251232e653f95d8ed5f201c20f12bda7f0cab845595193f9ecaa9825b59018dee120dbc444cc0d20f683af07c0e4bc8e386aeecb3c00",
            "01832456f815109e4d8049251232e653f95d8ed5f201c20f12bda7f0cab845595193f9ecaa9825b59018dee120dbc444cc0d20f683af07c0e4bc8e386aeecb3c00",
            "a94459fca46c92f6a00d02f39965d1aa55513d93a5525ccb47d068e409d5a8d50c7b30c1dbb5617d019ae5549e68c9e72e838e40a9d3a02a164ba454",
            "01832456f8a94459fca46c92f6a00d02f39965d1aa55513d93a5525ccb47d068e409d5a8d50c7b30c1dbb5617d019ae5549e68c9e72e838e40a9d3a02a164ba454",
            "4768bdb499b9ffbf877f3e1cb26775567aea39d31c313b5125c0363c61b0fa434f5472f375ba62230d7a04dd52bf9b44a7d518e7231936c0d3baf247",
            "01832456f84768bdb499b9ffbf877f3e1cb26775567aea39d31c313b5125c0363c61b0fa434f5472f375ba62230d7a04dd52bf9b44a7d518e7231936c0d3baf247",
            "d7830c079ebe813ba1fba2847d0adac0d6d19eb7779b14f36e7e75502cca7fa50521cc2b3c303f0f590612797352e194f49713a15f0663b18102d54d",
            "01832456f8d7830c079ebe813ba1fba2847d0adac0d6d19eb7779b14f36e7e75502cca7fa50521cc2b3c303f0f590612797352e194f49713a15f0663b18102d54d",
            "8bf38ca0f4f3f0bee763ed85086cb8f6b267ee116710759f8b62ea51c69fe20fbbc2a604b3e6de0f5036b5bebb1a474713e2d8debe63bb23795a65c7",
            "01832456f88bf38ca0f4f3f0bee763ed85086cb8f6b267ee116710759f8b62ea51c69fe20fbbc2a604b3e6de0f5036b5bebb1a474713e2d8debe63bb23795a65c7",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "6fb7e30af81d1f24e0093cf0d3e2314238e311eae4cb75b3621fd4048874de4ed655f8995d4f515070aeb8b189c481b5c1c2c7ca4e63",
            "15109e4d8049251232e653f95d8ed5f201c20f12bda7f0cab845595193f9ecaa9825b59018dee120dbc444cc0d20f683af07c0e4bc8e386aeecb3c00109e",
            "11832456f88bf38ca0f4f3f0bee763ed85086cb8f6b267ee116710759f8b62ea51c69fe20fbbc2a604b3e6de0f5036b5bebb1a474713e2d8debe63bb23795a65c7",
        ).map { it.asHexString().toByteArray() }
}