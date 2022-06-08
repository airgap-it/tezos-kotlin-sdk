package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class Secp256K1SecretKeyTest {

    @Test
    fun `should recognize valid and invalid Secp256K1SecretKey strings`() {
        validStrings.forEach {
            assertTrue(Secp256K1SecretKey.isValid(it), "Expected `$it` to be recognized as valid Secp256K1SecretKey string.")
        }

        invalidStrings.forEach {
            assertFalse(Secp256K1SecretKey.isValid(it), "Expected `$it` to be recognized as invalid Secp256K1SecretKey string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Secp256K1SecretKey bytes`() {
        validBytes.forEach {
            assertTrue(Secp256K1SecretKey.isValid(it), "Expected `$it` to be recognized as valid Secp256K1SecretKey.")
            assertTrue(Secp256K1SecretKey.isValid(it.toList()), "Expected `$it` to be recognized as valid Secp256K1SecretKey.")
        }

        invalidBytes.forEach {
            assertFalse(Secp256K1SecretKey.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1SecretKey.")
            assertFalse(Secp256K1SecretKey.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1SecretKey.")
        }
    }

    @Test
    fun `should create Secp256K1SecretKey from valid string`() {
        validStrings.forEach {
            assertEquals(it, Secp256K1SecretKey.createValue(it).base58)
            assertEquals(it, Secp256K1SecretKey.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Secp256K1SecretKey from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Secp256K1SecretKey.createValue(it) }
            assertNull(Secp256K1SecretKey.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "spsk2WUw2TFXQq2CsrNhB7EfFzdhMyNvGoYgD4uGQ6e17MgoRDv1co",
            "spsk2M8xGDRpAottSyea7qs872HfDS9PnCvL7q3FWrPpPEZy3p3FZ4",
            "spsk33Mptu9maVJaPhy2LJyZAbtY9VoEPEEr2pvU2Xoc6NohPqBHUH",
            "spsk1WWS7ubrZqARvzcFhDZh3nA4XHUsViBJx7Q8TxDfhc8KL3JJPa",
            "spsk1gL497dPspmaxQd4wAUjxk31VrK76xFvUeuuUDoxDgrfoPnJNp",
            "spsk2H1HR1mwsGtTXmThpZRFowgJReJj2E4KqSS3zwE18BnoQaFkeQ",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "2WUw2TFXQq2CsrNhB7EfFzdhMyNvGoYgD4uGQ6e17MgoRDv1co",
            "spsk2M8xGDRpAottSyea7qs872HfDS9PnCvL7q3FWrPpPEZy3p",
            "spsk33Mptu9maVJaPhy2LJyZAbtY9VoEPEEr2pvU2Xoc6NohPqBHUHk1WW",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "8edd004205dbf5ea9f6a61bbb9817af38ddf81b4e67e564471954105c3701a87",
            "11a2e0c98edd004205dbf5ea9f6a61bbb9817af38ddf81b4e67e564471954105c3701a87",
            "79a542825546403eb2506c77195dcc90d8c71467ddeb2ec1b2d9fc6c05789a28",
            "11a2e0c979a542825546403eb2506c77195dcc90d8c71467ddeb2ec1b2d9fc6c05789a28",
            "d4f972387a2f055c2b0d41a04089a4ab0cfe8714414eb49c8434f66138ff73bb",
            "11a2e0c9d4f972387a2f055c2b0d41a04089a4ab0cfe8714414eb49c8434f66138ff73bb",
            "0b39d995c94de84536034532a44a242c3efa4fd861251e6a2f5e0d048bb6384b",
            "11a2e0c90b39d995c94de84536034532a44a242c3efa4fd861251e6a2f5e0d048bb6384b",
            "21869fea56150850dd305e4541c6d0d05fdde19ce33712f82978d7adabea6168",
            "11a2e0c921869fea56150850dd305e4541c6d0d05fdde19ce33712f82978d7adabea6168",
            "7043467f15b374c9160c153f7ae0c3a6663a077c051bcfb89be084441b192dfd",
            "11a2e0c97043467f15b374c9160c153f7ae0c3a6663a077c051bcfb89be084441b192dfd",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "8edd004205dbf5ea9f6a61bbb9817af38ddf81b4e67e5644719541",
            "79a542825546403eb2506c77195dcc90d8c71467ddeb2ec1b2d9fc6c05789a28a542",
            "21a2e0c97043467f15b374c9160c153f7ae0c3a6663a077c051bcfb89be084441b192dfd",
        ).map { it.asHexString().toByteArray() }
}