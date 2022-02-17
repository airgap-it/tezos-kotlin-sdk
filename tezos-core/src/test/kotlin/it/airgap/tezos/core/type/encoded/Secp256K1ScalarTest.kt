package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class Secp256K1ScalarTest {

    @Test
    fun `should recognize valid and invalid Secp256K1Scalar strings`() {
        validStrings.forEach {
            assertTrue(Secp256K1Scalar.isValid(it), "Expected `$it` to be recognized as valid Secp256K1Scalar string.")
        }

        invalidStrings.forEach {
            assertFalse(Secp256K1Scalar.isValid(it), "Expected `$it` to be recognized as invalid Secp256K1Scalar string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Secp256K1Scalar bytes`() {
        validBytes.forEach {
            assertTrue(Secp256K1Scalar.isValid(it), "Expected `$it` to be recognized as valid Secp256K1Scalar.")
            assertTrue(Secp256K1Scalar.isValid(it.toList()), "Expected `$it` to be recognized as valid Secp256K1Scalar.")
        }

        invalidBytes.forEach {
            assertFalse(Secp256K1Scalar.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1Scalar.")
            assertFalse(Secp256K1Scalar.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1Scalar.")
        }
    }

    @Test
    fun `should create Secp256K1Scalar from valid string`() {
        validStrings.forEach {
            assertEquals(it, Secp256K1Scalar.createValue(it).base58)
            assertEquals(it, Secp256K1Scalar.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Secp256K1Scalar from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Secp256K1Scalar.createValue(it) }
            assertNull(Secp256K1Scalar.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "SSp31ykiTveHRWL9dT8Q6iykpsxVzVhyfJEzwMfqQMgnyUV5HdXRg",
            "SSp2kexXGENKfo2Wa4qaLHc3pU9g45mN3a4fewJHVDddHaVcWMwNJ",
            "SSp1kefPFJT1c67LZBiYQHbNDzt1cid8SdSCWvbopZnkvcYMvmsFm",
            "SSp2oeNK7ayEcy8uiV7h5zNgVEr9egq9DdcLbA3eXsKyEqaHBQTUk",
            "SSp39P9tQuKC7fTpSGuiNZvdJXch8GAcVppacL3WjBakj1SA7y9dj",
            "SSp1tanJe5oGrM4MHwztXPnygucV8h5rVBNm1Vr4YN693bpZ8vxt3",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "31ykiTveHRWL9dT8Q6iykpsxVzVhyfJEzwMfqQMgnyUV5HdXRg",
            "SSp2kexXGENKfo2Wa4qaLHc3pU9g45mN3a4fewJHVDddHaVcW",
            "SSp1kefPFJT1c67LZBiYQHbNDzt1cid8SdSCWvbopZnkvcYMvmsFm2oeN",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "ac8509c265391359b58855677d88219bd298fa2c990745665af963f498357eaa",
            "26f888ac8509c265391359b58855677d88219bd298fa2c990745665af963f498357eaa",
            "89b9782cd3f7e79a2a58c038aee9348ace55cee3548e6cacb83b992b9e653894",
            "26f88889b9782cd3f7e79a2a58c038aee9348ace55cee3548e6cacb83b992b9e653894",
            "06044ec249f3fdb1ef62635fcebf154b84cbde5f20ef995a28696589f9702e67",
            "26f88806044ec249f3fdb1ef62635fcebf154b84cbde5f20ef995a28696589f9702e67",
            "9083656cda2ccfa308dcf748069ed00f21f4064e1ecf656a6a913f323951df52",
            "26f8889083656cda2ccfa308dcf748069ed00f21f4064e1ecf656a6a913f323951df52",
            "bd54812ad1e2130dd0c8d332ede1b845ffa633876d4b56e3375f886acd67a296",
            "26f888bd54812ad1e2130dd0c8d332ede1b845ffa633876d4b56e3375f886acd67a296",
            "1807a658e984ea6272cc551dce41a9276357923d4a3ca702ff48037df46ad5c8",
            "26f8881807a658e984ea6272cc551dce41a9276357923d4a3ca702ff48037df46ad5c8",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "ac8509c265391359b58855677d88219bd298fa2c990745665af963f4",
            "89b9782cd3f7e79a2a58c038aee9348ace55cee3548e6cacb83b992b9e653894b978",
            "16f8881807a658e984ea6272cc551dce41a9276357923d4a3ca702ff48037df46ad5c8",
        ).map { it.asHexString().toByteArray() }
}