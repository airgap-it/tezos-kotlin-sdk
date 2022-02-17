package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class SaplingAddressTest {

    @Test
    fun `should recognize valid and invalid SaplingAddress strings`() {
        validStrings.forEach {
            assertTrue(SaplingAddress.isValid(it), "Expected `$it` to be recognized as valid SaplingAddress string.")
        }

        invalidStrings.forEach {
            assertFalse(SaplingAddress.isValid(it), "Expected `$it` to be recognized as invalid SaplingAddress string.")
        }
    }

    @Test
    fun `should recognize valid and invalid SaplingAddress bytes`() {
        validBytes.forEach {
            assertTrue(SaplingAddress.isValid(it), "Expected `$it` to be recognized as valid SaplingAddress.")
            assertTrue(SaplingAddress.isValid(it.toList()), "Expected `$it` to be recognized as valid SaplingAddress.")
        }

        invalidBytes.forEach {
            assertFalse(SaplingAddress.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid SaplingAddress.")
            assertFalse(SaplingAddress.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid SaplingAddress.")
        }
    }

    @Test
    fun `should create SaplingAddress from valid string`() {
        validStrings.forEach {
            assertEquals(it, SaplingAddress.createValue(it).base58)
            assertEquals(it, SaplingAddress.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create SaplingAddress from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { SaplingAddress.createValue(it) }
            assertNull(SaplingAddress.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "zet14S8WoUwrHZRRypqjahGKkLDjQTZjDQcdonD2EAH881nL8pUGQAEAS8crZTAZMj5zL",
            "zet13W3LDUJmT2MQLL8kZng3VLLaEonB3r6RsddFMJQAgBF4aHaciuUBmtUgHCCsjUnMb",
            "zet13oRAhGmaYqAbTux6BZpDxPDyodsTQYD9qta8ZXFKW7FkBebJnBHwh2cgHiEuvmsfR",
            "zet12bpHaVFgkozWSLiNEKYg3gUYqBWnaFzC3kkMpzoRTLVNQBWugY35HKyhKcjPjxZ3U",
            "zet12p1RaoZ5yGqZPiCNSST1NupK1xm2NSp5e8N7n9GTKrmnxjz6zYhT21LPFiDE1YZCo",
            "zet14QNw3zvVLscFZdxoAC1TittoZuaHe5S87kETe6MN1tfdbFXpoitUKr1utC1m3y8Mc",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "4S8WoUwrHZRRypqjahGKkLDjQTZjDQcdonD2EAH881nL8pUGQAEAS8crZTAZMj5zL",
            "zet13W3LDUJmT2MQLL8kZng3VLLaEonB3r6RsddFMJQAgBF4aHaciuUBmtUgHCCsj",
            "zet13oRAhGmaYqAbTux6BZpDxPDyodsTQYD9qta8ZXFKW7FkBebJnBHwh2cgHiEuvmsfR12bp",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "f505ad33154bea864e92ac833e5ba168eea578e6974ff3396721683ea95e28cc5710c1ce7c491ef00a0c4e",
            "124728dff505ad33154bea864e92ac833e5ba168eea578e6974ff3396721683ea95e28cc5710c1ce7c491ef00a0c4e",
            "84cf8ed797048b816c476a621d9fb6aec2619c94cde4a461bce043e22bf469e998f1ef7e9ea903d4287019",
            "124728df84cf8ed797048b816c476a621d9fb6aec2619c94cde4a461bce043e22bf469e998f1ef7e9ea903d4287019",
            "a8dbf841db01e17876d4430d38e2fdb794c9bc53f3b7e49af4e5b92d864b58a16ff944334470249a3de2ce",
            "124728dfa8dbf841db01e17876d4430d38e2fdb794c9bc53f3b7e49af4e5b92d864b58a16ff944334470249a3de2ce",
            "18779d1dde13f76477124abc01d3a484f759f85f8c23b790a0edff06bcbc21a253ecb369a043c167c30172",
            "124728df18779d1dde13f76477124abc01d3a484f759f85f8c23b790a0edff06bcbc21a253ecb369a043c167c30172",
            "31c2a325e8cc6dbcce6aa0f79c4a22965c8501adf9028b43693071dac2c2b6411915ee9463761f5a36dd07",
            "124728df31c2a325e8cc6dbcce6aa0f79c4a22965c8501adf9028b43693071dac2c2b6411915ee9463761f5a36dd07",
            "f163868b22b679ed7d7d1c1aa533b453f55a0f4bc612cea92762fb5e90ece4e37117fe0faead924c91376f",
            "124728dff163868b22b679ed7d7d1c1aa533b453f55a0f4bc612cea92762fb5e90ece4e37117fe0faead924c91376f",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "f505ad33154bea864e92ac833e5ba168eea578e6974ff3396721683ea95e28cc5710c1ce7c49",
            "84cf8ed797048b816c476a621d9fb6aec2619c94cde4a461bce043e22bf469e998f1ef7e9ea903d4287019cf8e",
            "224728dff163868b22b679ed7d7d1c1aa533b453f55a0f4bc612cea92762fb5e90ece4e37117fe0faead924c91376f",
        ).map { it.asHexString().toByteArray() }
}

