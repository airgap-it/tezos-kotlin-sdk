package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class NonceHashTest {

    @Test
    fun `should recognize valid and invalid NonceHash strings`() {
        validStrings.forEach {
            assertTrue(NonceHash.isValid(it), "Expected `$it` to be recognized as valid NonceHash string.")
        }

        invalidStrings.forEach {
            assertFalse(NonceHash.isValid(it), "Expected `$it` to be recognized as invalid NonceHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid NonceHash bytes`() {
        validBytes.forEach {
            assertTrue(NonceHash.isValid(it), "Expected `$it` to be recognized as valid NonceHash.")
            assertTrue(NonceHash.isValid(it.toList()), "Expected `$it` to be recognized as valid NonceHash.")
        }

        invalidBytes.forEach {
            assertFalse(NonceHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid NonceHash.")
            assertFalse(NonceHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid NonceHash.")
        }
    }

    @Test
    fun `should create NonceHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, NonceHash.createValue(it).base58)
            assertEquals(it, NonceHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create NonceHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { NonceHash.createValue(it) }
            assertNull(NonceHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "nceVuHLBtMnAnhWgWBUn4sHVkPV4zJ4hYH7VrPkLWmkcB4BLEDk2F",
            "nceUvz1x7XCEKbg6JPnAMGACfNY852BAaJHHkdX7UxtsYerjrebji",
            "nceVaqQTWaobeacLsAv4in8d92Fn13ufJwhaY7sHEo8b5YZDppzyf",
            "nceUqDyppZ2jWmjZGj6UDHoqmysQKtex98KjPHsamDeTJgJc5KSqs",
            "nceV2BbJ7kpkW4Z7pYRK1dcYxNTj5ALDT3cfU69HNSw6cvRukSMec",
            "nceUWA6TZexe2Mn6nSyJGJyvMofZExnBTgru7XA1US76nbC6V6Znb",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "VuHLBtMnAnhWgWBUn4sHVkPV4zJ4hYH7VrPkLWmkcB4BLEDk2F",
            "nceUvz1x7XCEKbg6JPnAMGACfNY852BAaJHHkdX7UxtsYerjr",
            "nceVaqQTWaobeacLsAv4in8d92Fn13ufJwhaY7sHEo8b5YZDppzyfUqDy",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "dd01936b0ec2b8deaa03b1d9e20ab4ebef24fde8a1cb1710d88a6be013a30235",
            "45dca9dd01936b0ec2b8deaa03b1d9e20ab4ebef24fde8a1cb1710d88a6be013a30235",
            "5d2c6864bcc9901474d8dff92c416e8767f686560faaa0dc86f26b80dd72a875",
            "45dca95d2c6864bcc9901474d8dff92c416e8767f686560faaa0dc86f26b80dd72a875",
            "b31eb65e85fbf657eae784110a557b18c0903d890d8721b1aad8dca2eba23261",
            "45dca9b31eb65e85fbf657eae784110a557b18c0903d890d8721b1aad8dca2eba23261",
            "5018ac61fb2767b7da0dbe093e008566a21ac9bb7e41826db461cd8d4eab85ba",
            "45dca95018ac61fb2767b7da0dbe093e008566a21ac9bb7e41826db461cd8d4eab85ba",
            "68facf0dc33de53a174e2823b49ab2e45366053fd7d25a35b7be6a164bc9cee4",
            "45dca968facf0dc33de53a174e2823b49ab2e45366053fd7d25a35b7be6a164bc9cee4",
            "24cd6ca1c508665b54399d0cb8743c455946345a79b5e1d6d7c7a24fc55edc55",
            "45dca924cd6ca1c508665b54399d0cb8743c455946345a79b5e1d6d7c7a24fc55edc55",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "dd01936b0ec2b8deaa03b1d9e20ab4ebef24fde8a1cb1710d88a6be0",
            "5d2c6864bcc9901474d8dff92c416e8767f686560faaa0dc86f26b80dd72a8752c68",
            "15dca924cd6ca1c508665b54399d0cb8743c455946345a79b5e1d6d7c7a24fc55edc55",
        ).map { it.asHexString().toByteArray() }
}