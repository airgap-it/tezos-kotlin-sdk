package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class Secp256K1SignatureTest {

    @Test
    fun `should recognize valid and invalid Secp256K1Signature strings`() {
        validStrings.forEach {
            assertTrue(Secp256K1Signature.isValid(it), "Expected `$it` to be recognized as valid Secp256K1Signature string.")
        }

        invalidStrings.forEach {
            assertFalse(Secp256K1Signature.isValid(it), "Expected `$it` to be recognized as invalid Secp256K1Signature string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Secp256K1Signature bytes`() {
        validBytes.forEach {
            assertTrue(Secp256K1Signature.isValid(it), "Expected `$it` to be recognized as valid Secp256K1Signature.")
            assertTrue(Secp256K1Signature.isValid(it.toList()), "Expected `$it` to be recognized as valid Secp256K1Signature.")
        }

        invalidBytes.forEach {
            assertFalse(Secp256K1Signature.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1Signature.")
            assertFalse(Secp256K1Signature.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1Signature.")
        }
    }

    @Test
    fun `should create Secp256K1Signature from valid string`() {
        validStrings.forEach {
            assertEquals(it, Secp256K1Signature.createValue(it).base58)
            assertEquals(it, Secp256K1Signature.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Secp256K1Signature from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Secp256K1Signature.createValue(it) }
            assertNull(Secp256K1Signature.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "spsig1eSQBRpFgcZBPYijWPmcyLByE7yo5dhfqThn59zyAQDhV5o6uagP58UFJAnZknCWyaKivVDvVDFEUoqGF4uRMhGfLKHWbC",
            "spsig1PQiZY3bDLQCdcCY8cmnoUumTZtTgyVh2H2dn8gPUfCGsysieGz6XE7RhN5hqrCz923AtrESkRYACxDoFh1EY727JrV2Tj",
            "spsig1SRbseN1UzjYMZzJtg8xhzqz2vGugum6KbFDimgyAEgHvu2wa2mJHUkEGwC3R3dbqdfhuaC3556PPDJkv89LkDuySZ8g8v",
            "spsig1B2bB7PsRV2umJGX4fnnwGuBnFQXqsibWafZN8pA34denpK7njU6a7vxvWX48a2YveAAu4E4Aj7nuUoMfAs46hQ6bZVFWo",
            "spsig1JDvXdCiFiA4dGEmcoj9sJod3AXedCbN6cdynf85i844dg277JZz7A3kdtJLEabqcyR6hR1xEhf8LzxWrTF8NhjFMWkXxq",
            "spsig1M2Z3DJj6U3Zex5DtkcBpf63KDNJKby5WBdTqMGDMLhdqcdvQThoCqXKcbQzNFXTy5Lxq8V6iyLwX6TLnEjW4VkQ27b5B9",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "eSQBRpFgcZBPYijWPmcyLByE7yo5dhfqThn59zyAQDhV5o6uagP58UFJAnZknCWyaKivVDvVDFEUoqGF4uRMhGfLKHWbC",
            "spsig1PQiZY3bDLQCdcCY8cmnoUumTZtTgyVh2H2dn8gPUfCGsysieGz6XE7RhN5hqrCz923AtrESkRYACxDoFh1EY727Jr",
            "spsig1SRbseN1UzjYMZzJtg8xhzqz2vGugum6KbFDimgyAEgHvu2wa2mJHUkEGwC3R3dbqdfhuaC3556PPDJkv89LkDuySZ8g8vig1B",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "f9654d99eca5d61c2cea794fbc116e7db884bcd1cefd58cc2258dbe2626513bb3ed001decda5d1e3cdd0479ae20c68dc90a9e4b1dcd9a9b8860d67ca11e657b9",
            "0d7365133ff9654d99eca5d61c2cea794fbc116e7db884bcd1cefd58cc2258dbe2626513bb3ed001decda5d1e3cdd0479ae20c68dc90a9e4b1dcd9a9b8860d67ca11e657b9",
            "86851c83673f81e273c11d4a9fd9dd5f9a74e24ddf925555cc323b8fd5905de83d35404beedf490f4a5443b6a2deb877198a99f343897d8213fea334bc948aa0",
            "0d7365133f86851c83673f81e273c11d4a9fd9dd5f9a74e24ddf925555cc323b8fd5905de83d35404beedf490f4a5443b6a2deb877198a99f343897d8213fea334bc948aa0",
            "9d913d86a23f4b5267e044a6ab04bcf5bbf9963e6e0b7d8eb1816230dfbbaa7dcda899f1f1f5016775065ba3355f4722cd5ab2765e613cc87bcbb13689062da8",
            "0d7365133f9d913d86a23f4b5267e044a6ab04bcf5bbf9963e6e0b7d8eb1816230dfbbaa7dcda899f1f1f5016775065ba3355f4722cd5ab2765e613cc87bcbb13689062da8",
            "27e177c7a58350464f4a34e9d73fb6514d2bacc98323283d374a2b493bbed65a44bce9e3c0bcd86dbc50421c6b06f3b0992c2756b89ea347ec47693a7ac000fd",
            "0d7365133f27e177c7a58350464f4a34e9d73fb6514d2bacc98323283d374a2b493bbed65a44bce9e3c0bcd86dbc50421c6b06f3b0992c2756b89ea347ec47693a7ac000fd",
            "5ee128d620a5a035215f9f3698495b1bf0c29405f0da536aff88093bb7d6ff0cc6de359e2a2008fba3013000cce2ea4ddacc31ccec53a6410ded9f968fafaf0e",
            "0d7365133f5ee128d620a5a035215f9f3698495b1bf0c29405f0da536aff88093bb7d6ff0cc6de359e2a2008fba3013000cce2ea4ddacc31ccec53a6410ded9f968fafaf0e",
            "744fd4d809543a8e64a13aa35bf7e4d65cbdd164cb7de3de32ddbb95cc3b44cf5d04b839d9106b15f13a89162976fcf623fc5f6b13307e2ae2aec62f9b0eb046",
            "0d7365133f744fd4d809543a8e64a13aa35bf7e4d65cbdd164cb7de3de32ddbb95cc3b44cf5d04b839d9106b15f13a89162976fcf623fc5f6b13307e2ae2aec62f9b0eb046",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "f9654d99eca5d61c2cea794fbc116e7db884bcd1cefd58cc2258dbe2626513bb3ed001decda5d1e3cdd0479ae20c68dc90a9e4b1dcd9a9b8860d",
            "86851c83673f81e273c11d4a9fd9dd5f9a74e24ddf925555cc323b8fd5905de83d35404beedf490f4a5443b6a2deb877198a99f343897d8213fea334bc948aa0851c",
            "1d7365133f744fd4d809543a8e64a13aa35bf7e4d65cbdd164cb7de3de32ddbb95cc3b44cf5d04b839d9106b15f13a89162976fcf623fc5f6b13307e2ae2aec62f9b0eb046",
        ).map { it.asHexString().toByteArray() }
}