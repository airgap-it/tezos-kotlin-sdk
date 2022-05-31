package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class Ed25519SecretKeyTest {

    @Test
    fun `should recognize valid and invalid Ed25519SecretKey strings`() {
        validStrings.forEach {
            assertTrue(Ed25519SecretKey.isValid(it), "Expected `$it` to be recognized as valid Ed25519SecretKey string.")
        }

        invalidStrings.forEach {
            assertFalse(Ed25519SecretKey.isValid(it), "Expected `$it` to be recognized as invalid Ed25519SecretKey string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Ed25519SecretKey bytes`() {
        validBytes.forEach {
            assertTrue(Ed25519SecretKey.isValid(it), "Expected `$it` to be recognized as valid Ed25519SecretKey.")
            assertTrue(Ed25519SecretKey.isValid(it.toList()), "Expected `$it` to be recognized as valid Ed25519SecretKey.")
        }

        invalidBytes.forEach {
            assertFalse(Ed25519SecretKey.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519SecretKey.")
            assertFalse(Ed25519SecretKey.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519SecretKey.")
        }
    }

    @Test
    fun `should create Ed25519SecretKey from valid string`() {
        validStrings.forEach {
            assertEquals(it, Ed25519SecretKey.createValue(it).base58)
            assertEquals(it, Ed25519SecretKey.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Ed25519SecretKey from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Ed25519SecretKey.createValue(it) }
            assertNull(Ed25519SecretKey.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "edskRhKTQkgxb7CNTr31rzy3xdkyKaYX9hySAnZYJTPmUzPB7WU4NL7C8pmtQDgRqQ4jDw4Ugh6Y1UW5nvo7UYrRbyhVYK1YuR",
            "edskRtNjWaE2SwE33zREX1a6HndrShcE8GtFXVBKvgAYNNqJ4yTbp5YYY5EqZvWofP1AkscedNQTXdumgnMvKdtDx6HiEy7LQY",
            "edskS4dTKQ2E2QPe4ds7V8Gy2MHCBVofuf6szWLd6HYGDMdxyV1K7CuU1tjFsmyKvygqUw4Lt9mD2c6pfx9L8r9Z9N4GjbthjP",
            "edskS2AzJk7NUpVRPok5fdYR1dcUiNQ847Xpp7o6LuS6TVFuedyieCmTdPLHLqrsiqWtDgiC83BMv4WxxNoiKvnGuLAeMoxfFv",
            "edskRyCpUr7uPfczYUAqvYr2LxK5fvH5PBVzw8bUeFZkzjGjE6o2zWnanfWMcXNGaQmx4cEW92snUdQHCuzJqVyAduUQij5qCj",
            "edskS9ARrWZHjFUeqrtC2KwCyKdjGh3RwnUaqbtsyazffTYCHzTfyd6Jgbed1XRmMQykBy37h1KBUB3fXgdVhLW1VSr2mdrMX9",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "RhKTQkgxb7CNTr31rzy3xdkyKaYX9hySAnZYJTPmUzPB7WU4NL7C8pmtQDgRqQ4jDw4Ugh6Y1UW5nvo7UYrRbyhVYK1YuR",
            "edskRtNjWaE2SwE33zREX1a6HndrShcE8GtFXVBKvgAYNNqJ4yTbp5YYY5EqZvWofP1AkscedNQTXdumgnMvKdtDx6HiEy",
            "edskS4dTKQ2E2QPe4ds7V8Gy2MHCBVofuf6szWLd6HYGDMdxyV1K7CuU1tjFsmyKvygqUw4Lt9mD2c6pfx9L8r9Z9N4GjbthjPkS2A",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "288c40c041a15d87c2a44e5584a07e881235624c78aea6c41b7aa15311e56cbc18788a8f2e8e478457b16ea5ca5049b55a1391c7f05c9cf7795e92bba5904078",
            "2bf64e07288c40c041a15d87c2a44e5584a07e881235624c78aea6c41b7aa15311e56cbc18788a8f2e8e478457b16ea5ca5049b55a1391c7f05c9cf7795e92bba5904078",
            "7d0f32536190030f234815ad666375edc731c765e1d5aee0bb926c858ef7cb437578d69215a1f398f230012aba258bd78afbb39758a19d7996dfd8ec5df07443",
            "2bf64e077d0f32536190030f234815ad666375edc731c765e1d5aee0bb926c858ef7cb437578d69215a1f398f230012aba258bd78afbb39758a19d7996dfd8ec5df07443",
            "cb6f714abcfa36d975dd4e9e6b36baa4e8f7af5d1abd313a77352512ac4ba22ce14c1307e0384259a484ef3559c5b94dd1b868b84d276d0641cf1a8d2490e9ed",
            "2bf64e07cb6f714abcfa36d975dd4e9e6b36baa4e8f7af5d1abd313a77352512ac4ba22ce14c1307e0384259a484ef3559c5b94dd1b868b84d276d0641cf1a8d2490e9ed",
            "b8a90a7bc4f7dd9e57a97ec23ab1dc9b171fea5339af28938f344117ec1c4b67b18b50bf343c37bd926c59140f66e270f400f67d7612bb2e8b3b289186ba63f7",
            "2bf64e07b8a90a7bc4f7dd9e57a97ec23ab1dc9b171fea5339af28938f344117ec1c4b67b18b50bf343c37bd926c59140f66e270f400f67d7612bb2e8b3b289186ba63f7",
            "a1f884a7eb38573bd357d6df2c06d46864e687b1b8baaa33eb58fcfb95887d39a23847fadd8a06c0aac09b803b3a838267092d21ffe23643f43305f2bb292172",
            "2bf64e07a1f884a7eb38573bd357d6df2c06d46864e687b1b8baaa33eb58fcfb95887d39a23847fadd8a06c0aac09b803b3a838267092d21ffe23643f43305f2bb292172",
            "ee177d0c1330d2e31549d0e989be818e5503e32b4d6c9126ac4ddacc568113beea9448287876b7e9e3a94824f1b1c5e215f936e2ce4124b47063845fad100887",
            "2bf64e07ee177d0c1330d2e31549d0e989be818e5503e32b4d6c9126ac4ddacc568113beea9448287876b7e9e3a94824f1b1c5e215f936e2ce4124b47063845fad100887",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "288c40c041a15d87c2a44e5584a07e881235624c78aea6c41b7aa15311e56cbc18788a8f2e8e478457b16ea5ca5049b55a1391c7f05c9cf7795e92",
            "7d0f32536190030f234815ad666375edc731c765e1d5aee0bb926c858ef7cb437578d69215a1f398f230012aba258bd78afbb39758a19d7996dfd8ec5df074430f32",
            "1bf64e07ee177d0c1330d2e31549d0e989be818e5503e32b4d6c9126ac4ddacc568113beea9448287876b7e9e3a94824f1b1c5e215f936e2ce4124b47063845fad100887",
        ).map { it.asHexString().toByteArray() }
}