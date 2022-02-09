package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class OperationMetadataHashTest {

    @Test
    fun `should recognize valid and invalid OperationMetadataHash strings`() {
        validStrings.forEach {
            assertTrue(OperationMetadataHash.isValid(it), "Expected `$it` to be recognized as valid OperationMetadataHash string.")
        }

        invalidStrings.forEach {
            assertFalse(OperationMetadataHash.isValid(it), "Expected `$it` to be recognized as invalid OperationMetadataHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid OperationMetadataHash bytes`() {
        validBytes.forEach {
            assertTrue(OperationMetadataHash.isValid(it), "Expected `$it` to be recognized as valid OperationMetadataHash.")
            assertTrue(OperationMetadataHash.isValid(it.toList()), "Expected `$it` to be recognized as valid OperationMetadataHash.")
        }

        invalidBytes.forEach {
            assertFalse(OperationMetadataHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationMetadataHash.")
            assertFalse(OperationMetadataHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationMetadataHash.")
        }
    }

    @Test
    fun `should create OperationMetadataHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, OperationMetadataHash.createValue(it).base58)
            assertEquals(it, OperationMetadataHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create OperationMetadataHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { OperationMetadataHash.createValue(it) }
            assertNull(OperationMetadataHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "r4i1ESNUVsFtL7NsjdyAbhp5W1tGf3Xx6Bo5pZLz3bPGMX54r51",
            "r2wHMyxnPMdvoG8mHWhWzHQ4Z5s6G7hFpvjP8fSDs8EtML3SCwW",
            "r3PGwXGNnuu3f6damJRbshjZAZKGHJbtTtyaMS2yan7PUJ58HUz",
            "r3ZiFK142iQpn47ZdaNbsJV85siztF4ZZuEWareeN14pco2y2jA",
            "r4YtEximEQ7ge8L5SUASQXxTgbr8d6ccirbCKTVFPEPMyJJ1NPG",
            "r3DYbCgpiscTmDL5jJTGdCXgfRrQv49PVJjWJdFRkPH3UQJFThL",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "4i1ESNUVsFtL7NsjdyAbhp5W1tGf3Xx6Bo5pZLz3bPGMX54",
            "r2wHMyxnPMdvoG8mHWhWzHQ4Z5s6G7hFpvjP8fSDs8EtML3",
            "r3PGwXGNnuu3f6damJRbshjZAZKGHJbtTtyaMS2yan7PUJ58HUziFK1",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "f07eea2763830ac9a1693e5bc6a77a4bc22df2c718c5846f0a35e021cd662213",
            "05b7f07eea2763830ac9a1693e5bc6a77a4bc22df2c718c5846f0a35e021cd662213",
            "0740cd342d6b273c68acd182bbc45c4c0e73c318c48372c36ac8f0ac4f396a82",
            "05b70740cd342d6b273c68acd182bbc45c4c0e73c318c48372c36ac8f0ac4f396a82",
            "4245d654e76fb502e45edd9912f90733dd1977f84c56f039c35a6986e9906f7e",
            "05b74245d654e76fb502e45edd9912f90733dd1977f84c56f039c35a6986e9906f7e",
            "59f8416e7165a3a4a85d7533bb5aa41a030ba78db6e828fd02185a0217168508",
            "05b759f8416e7165a3a4a85d7533bb5aa41a030ba78db6e828fd02185a0217168508",
            "dbc9563a3e650860fba4f1e5dd3d4dfff3ed9d0a0a3ea421673ad03e65c7a0a9",
            "05b7dbc9563a3e650860fba4f1e5dd3d4dfff3ed9d0a0a3ea421673ad03e65c7a0a9",
            "2c2de2c02795e51d467ed0de97a7e2afe2d62eef81e97b497a84be099b3de299",
            "05b72c2de2c02795e51d467ed0de97a7e2afe2d62eef81e97b497a84be099b3de299",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "f07eea2763830ac9a1693e5bc6a77a4bc22df2c718c5846f0a35e021cd",
            "0740cd342d6b273c68acd182bbc45c4c0e73c318c48372c36ac8f0ac4f396a8240cd",
            "15b72c2de2c02795e51d467ed0de97a7e2afe2d62eef81e97b497a84be099b3de299",
        ).map { it.asHexString().toByteArray() }
}
