package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class P256PublicKeyTest {

    @Test
    fun `should recognize valid and invalid P256PublicKey strings`() {
        validStrings.forEach {
            assertTrue(P256PublicKey.isValid(it), "Expected `$it` to be recognized as valid P256PublicKey string.")
        }

        invalidStrings.forEach {
            assertFalse(P256PublicKey.isValid(it), "Expected `$it` to be recognized as invalid P256PublicKey string.")
        }
    }

    @Test
    fun `should recognize valid and invalid P256PublicKey bytes`() {
        validBytes.forEach {
            assertTrue(P256PublicKey.isValid(it), "Expected `$it` to be recognized as valid P256PublicKey.")
            assertTrue(P256PublicKey.isValid(it.toList()), "Expected `$it` to be recognized as valid P256PublicKey.")
        }

        invalidBytes.forEach {
            assertFalse(P256PublicKey.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid P256PublicKey.")
            assertFalse(P256PublicKey.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid P256PublicKey.")
        }
    }

    @Test
    fun `should create P256PublicKey from valid string`() {
        validStrings.forEach {
            assertEquals(it, P256PublicKey.createValue(it).base58)
            assertEquals(it, P256PublicKey.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create P256PublicKey from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { P256PublicKey.createValue(it) }
            assertNull(P256PublicKey.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "p2pkDkL6thzTwyPjpmMotSqeKy1MAftLrseqTALwBhHwUtXRmFV983f",
            "p2pk7FwiACD46ZkwEK2mJeAMrCp4NHjpQSyrg62TGakJMKjcXgZJKRc",
            "p2pkE59PJs6YZ5Ao6FNHRFeMfvsyrjD8VmikqYhiZcJLnQuDfDao2o9",
            "p2pkDjJwyckTnU2htHvsH73NbifkWpAJU4R93gViuRiCaMcUpp1CYeh",
            "p2pk8qH7BqkEdd9yU9RF3pmTV17ERjh7ASuiTxpatb35R8YkPosxmQr",
            "p2pk8zggNeLrhNau9LSu9Tr6HmUr9D6ggjJURiPJqP9hxgMqpEUmADY",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "DkL6thzTwyPjpmMotSqeKy1MAftLrseqTALwBhHwUtXRmFV983f",
            "p2pk7FwiACD46ZkwEK2mJeAMrCp4NHjpQSyrg62TGakJMKjcXgZ",
            "p2pkE59PJs6YZ5Ao6FNHRFeMfvsyrjD8VmikqYhiZcJLnQuDfDao2o9kDjJ",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "e7508872e2de3e3059abfacec6024bdab363a12948a231a20a3691179cffce7c61",
            "03b28b7fe7508872e2de3e3059abfacec6024bdab363a12948a231a20a3691179cffce7c61",
            "25aff51f272ce1572b1f78fc3ba4c80bc568ba292de8d4fdb7b002db62dffe6e11",
            "03b28b7f25aff51f272ce1572b1f78fc3ba4c80bc568ba292de8d4fdb7b002db62dffe6e11",
            "f0fe6cb6042c0b12aaa6a3188c026e464476d899ee4ed242af46211ff2941c9ac3",
            "03b28b7ff0fe6cb6042c0b12aaa6a3188c026e464476d899ee4ed242af46211ff2941c9ac3",
            "e6ca379e6ffea1682fffdce68d176755f9d0bfa9f8e78807fa2810b3f64a3339f8",
            "03b28b7fe6ca379e6ffea1682fffdce68d176755f9d0bfa9f8e78807fa2810b3f64a3339f8",
            "54ac587507bc8a40fe39dffc086cba71f6628395d7d1b47b68df06d15095a01646",
            "03b28b7f54ac587507bc8a40fe39dffc086cba71f6628395d7d1b47b68df06d15095a01646",
            "598322479ac272add7b4c82f1da07e86b9e38bc7d3fca36dc4e15c5e587a1a34c2",
            "03b28b7f598322479ac272add7b4c82f1da07e86b9e38bc7d3fca36dc4e15c5e587a1a34c2",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "e7508872e2de3e3059abfacec6024bdab363a12948a231a20a369117",
            "25aff51f272ce1572b1f78fc3ba4c80bc568ba292de8d4fdb7b002db62dffe6e11aff5",
            "13b28b7f598322479ac272add7b4c82f1da07e86b9e38bc7d3fca36dc4e15c5e587a1a34c2",
        ).map { it.asHexString().toByteArray() }
}