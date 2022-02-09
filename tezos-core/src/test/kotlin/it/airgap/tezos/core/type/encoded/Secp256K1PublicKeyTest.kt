package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class Secp256K1PublicKeyTest {

    @Test
    fun `should recognize valid and invalid Secp256K1PublicKey strings`() {
        validStrings.forEach {
            assertTrue(Secp256K1PublicKey.isValid(it), "Expected `$it` to be recognized as valid Secp256K1PublicKey string.")
        }

        invalidStrings.forEach {
            assertFalse(Secp256K1PublicKey.isValid(it), "Expected `$it` to be recognized as invalid Secp256K1PublicKey string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Secp256K1PublicKey bytes`() {
        validBytes.forEach {
            assertTrue(Secp256K1PublicKey.isValid(it), "Expected `$it` to be recognized as valid Secp256K1PublicKey.")
            assertTrue(Secp256K1PublicKey.isValid(it.toList()), "Expected `$it` to be recognized as valid Secp256K1PublicKey.")
        }

        invalidBytes.forEach {
            assertFalse(Secp256K1PublicKey.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1PublicKey.")
            assertFalse(Secp256K1PublicKey.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1PublicKey.")
        }
    }

    @Test
    fun `should create Secp256K1PublicKey from valid string`() {
        validStrings.forEach {
            assertEquals(it, Secp256K1PublicKey.createValue(it).base58)
            assertEquals(it, Secp256K1PublicKey.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Secp256K1PublicKey from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Secp256K1PublicKey.createValue(it) }
            assertNull(Secp256K1PublicKey.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "sppkDN74FpFyXiHUe7MZS7rwDzzwb2esc21355LEcSExN67KdNnAfqA",
            "sppkBk5YQL8aGbSJpGdUGYMXofh26nuTtrc7zG83ieu7KZy3gSE4knL",
            "sppk9mVsk2aTRiTFsQZWYwPzxrsbQ8yhxewsnkBoq2o2Dqz5RdFZNTr",
            "sppkBXaNfz3S7VT5mfcqUwEA75Wykz14Mr5Nxd2JG5FS6od3ngK227W",
            "sppk9jCCzMUMJMLrQLdSz9nzzahwSxfssqQQTztMN7rYoeYi6oVtYPD",
            "sppkEKxembvxmqB1WV6zPsxdC3N9kqhRgCd2R3cK9tsY8kdBtrW2dV5",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "DN74FpFyXiHUe7MZS7rwDzzwb2esc21355LEcSExN67KdNnAfqA",
            "sppkBk5YQL8aGbSJpGdUGYMXofh26nuTtrc7zG83ieu7KZy3gSE",
            "sppk9mVsk2aTRiTFsQZWYwPzxrsbQ8yhxewsnkBoq2o2Dqz5RdFZNTrkBXa",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "af434abab1bec238352d02e45bc4f2657908512d0680c397f9efbcca837589b8ad",
            "03fee256af434abab1bec238352d02e45bc4f2657908512d0680c397f9efbcca837589b8ad",
            "7ee46b6c019c06f1024df20b45acdc94a87df5657b7fbda25ea526b43301080f09",
            "03fee2567ee46b6c019c06f1024df20b45acdc94a87df5657b7fbda25ea526b43301080f09",
            "43f29ee6ee5f834b9d77510b08328798833992c83e60e4d61336ef7fb2cfeabae2",
            "03fee25643f29ee6ee5f834b9d77510b08328798833992c83e60e4d61336ef7fb2cfeabae2",
            "7875d666b341dcb38a87e53143c3629dd0df8d6eb7fa8d5670a80ed8807072d755",
            "03fee2567875d666b341dcb38a87e53143c3629dd0df8d6eb7fa8d5670a80ed8807072d755",
            "42c31c5e3903fef158070e1aa95a3438ce731a311d10ae29459b0bea9eb17dc36e",
            "03fee25642c31c5e3903fef158070e1aa95a3438ce731a311d10ae29459b0bea9eb17dc36e",
            "cbff3055bce6819258a5a47baf79ec4206ed3629c4f9bdf1a80132a6e0fa5105f0",
            "03fee256cbff3055bce6819258a5a47baf79ec4206ed3629c4f9bdf1a80132a6e0fa5105f0",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "af434abab1bec238352d02e45bc4f2657908512d0680c397f9efbcca",
            "7ee46b6c019c06f1024df20b45acdc94a87df5657b7fbda25ea526b43301080f09e46b",
            "13fee256cbff3055bce6819258a5a47baf79ec4206ed3629c4f9bdf1a80132a6e0fa5105f0",
        ).map { it.asHexString().toByteArray() }
}