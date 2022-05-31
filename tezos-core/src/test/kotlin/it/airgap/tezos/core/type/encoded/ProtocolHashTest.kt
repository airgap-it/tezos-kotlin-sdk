package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class ProtocolHashTest {

    @Test
    fun `should recognize valid and invalid ProtocolHash strings`() {
        validStrings.forEach {
            assertTrue(ProtocolHash.isValid(it), "Expected `$it` to be recognized as valid ProtocolHash string.")
        }

        invalidStrings.forEach {
            assertFalse(ProtocolHash.isValid(it), "Expected `$it` to be recognized as invalid ProtocolHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid ProtocolHash bytes`() {
        validBytes.forEach {
            assertTrue(ProtocolHash.isValid(it), "Expected `$it` to be recognized as valid ProtocolHash.")
            assertTrue(ProtocolHash.isValid(it.toList()), "Expected `$it` to be recognized as valid ProtocolHash.")
        }

        invalidBytes.forEach {
            assertFalse(ProtocolHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid ProtocolHash.")
            assertFalse(ProtocolHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid ProtocolHash.")
        }
    }

    @Test
    fun `should create ProtocolHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, ProtocolHash.createValue(it).base58)
            assertEquals(it, ProtocolHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create ProtocolHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { ProtocolHash.createValue(it) }
            assertNull(ProtocolHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "ProaRka7UGQsRRiDywZqe9hbi7W3mSzADmQKZSFq7t27RZrT8k4",
            "PtaoNzQLMC1SQM3v5yZEf8nUgSnhmGGVBC7ndq6ndC6MNqaSysP",
            "Ps8EyXKPoEqTambrzceWgrNLhrFwn3ZRzoXDKP4aKNR8ZXKrkCj",
            "PsCeacsjPz5NyTSgdjuh2HwggA4m7UVo7aM9by5jWHBNfdEk2Lt",
            "PtGQf1exMC89Eb1j1Co8BiNepRCgkpwkcLhJKiPUanHSGTXGVyo",
            "PsAfJwAR2FzKvM8HwetjHJdcN7d6NkMTpivYQ8ZUPurLfH7Yqrr",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "roaRka7UGQsRRiDywZqe9hbi7W3mSzADmQKZSFq7t27RZrT8k4",
            "PtaoNzQLMC1SQM3v5yZEf8nUgSnhmGGVBC7ndq6ndC6MNqa",
            "Ps8EyXKPoEqTambrzceWgrNLhrFwn3ZRzoXDKP4aKNR8ZXKrkCjeacs",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "0b156276e5a50a4cc7608db89bd42319b1600c54d59599dde0772b407f118905",
            "02aa0b156276e5a50a4cc7608db89bd42319b1600c54d59599dde0772b407f118905",
            "f576f6c05481dcd4de9cb31e919f35bf1660df145a98d7f9b761f91bb8423615",
            "02aaf576f6c05481dcd4de9cb31e919f35bf1660df145a98d7f9b761f91bb8423615",
            "3576bd8d34a17d4ac1f98492532bf92f2ce15fba6c0a58b594e3ea6b4dd87371",
            "02aa3576bd8d34a17d4ac1f98492532bf92f2ce15fba6c0a58b594e3ea6b4dd87371",
            "3f786deb3b6ea830a7abf98dc0b62719e390aadf0a543688f0b680e7b7924704",
            "02aa3f786deb3b6ea830a7abf98dc0b62719e390aadf0a543688f0b680e7b7924704",
            "cbb432810eab310a681f2444ec83bb2732a532dc57e17ce683fee3dbec523b06",
            "02aacbb432810eab310a681f2444ec83bb2732a532dc57e17ce683fee3dbec523b06",
            "3af52ebcf186b5acfe5c130ad68ec9e31bfbfefaee9e20dc2dc38b73a83662bd",
            "02aa3af52ebcf186b5acfe5c130ad68ec9e31bfbfefaee9e20dc2dc38b73a83662bd",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "0b156276e5a50a4cc7608db89bd42319b1600c54d59599dde0772b407f",
            "f576f6c05481dcd4de9cb31e919f35bf1660df145a98d7f9b761f91bb842361576f6",
            "12aa3af52ebcf186b5acfe5c130ad68ec9e31bfbfefaee9e20dc2dc38b73a83662bd",
        ).map { it.asHexString().toByteArray() }
}