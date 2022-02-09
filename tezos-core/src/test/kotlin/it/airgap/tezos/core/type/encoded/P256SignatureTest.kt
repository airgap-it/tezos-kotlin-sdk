package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class P256SignatureTest {

    @Test
    fun `should recognize valid and invalid P256Signature strings`() {
        validStrings.forEach {
            assertTrue(P256Signature.isValid(it), "Expected `$it` to be recognized as valid P256Signature string.")
        }

        invalidStrings.forEach {
            assertFalse(P256Signature.isValid(it), "Expected `$it` to be recognized as invalid P256Signature string.")
        }
    }

    @Test
    fun `should recognize valid and invalid P256Signature bytes`() {
        validBytes.forEach {
            assertTrue(P256Signature.isValid(it), "Expected `$it` to be recognized as valid P256Signature.")
            assertTrue(P256Signature.isValid(it.toList()), "Expected `$it` to be recognized as valid P256Signature.")
        }

        invalidBytes.forEach {
            assertFalse(P256Signature.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid P256Signature.")
            assertFalse(P256Signature.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid P256Signature.")
        }
    }

    @Test
    fun `should create P256Signature from valid string`() {
        validStrings.forEach {
            assertEquals(it, P256Signature.createValue(it).base58)
            assertEquals(it, P256Signature.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create P256Signature from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { P256Signature.createValue(it) }
            assertNull(P256Signature.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "p2sigfMsDC6iWrvntDFQi8ycvcT6AjfePLPZGiPfwoBeiiQFgM8kibj49e3mHp22wXt2SEaEcH7hKbMgpR91CPprotwwiurnC7",
            "p2sigRHbu3NDmEYeVeUTts7y4TKcFod8cNZ3Nt4EzkfQs7QvEJrUjuz35C6nC5PQHt6jXpVDnLgnwNZv8jcDh7jhQfMfwuqyff",
            "p2sigVCqsGG6NquJpTbMvHhCF3q6w3pPW4pxGaL75DGMtk4YXkPao1zsWu79jrXmP2x1mkDLtbrRLZC19zsWE8NHMunVEA48FL",
            "p2sigmrDdBcP779dyfp1UbN51yDxJBz38PPbE8eqtX1n31zaduB1dtzojnBc29sN8NC8mpewPDeZRfTMckH2fkpHjQwnxN8nWy",
            "p2sigUTx4KievFL4xHckRz3tWDGwKNTWmT246eRkEZtiSAY2sm9YV8bvysoep8Z9BN9C8YKwuUqKxp8joQBgCRrYNePH7HngeT",
            "p2siggawxcbMBGYAJKTkrFGmSdZf3Sm721s2azhWv8krVJf8RvChSC1UrMwpipSqCse5MiRKMeqDgaTWDehi1Xncsve7ZXe1Xo",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "fMsDC6iWrvntDFQi8ycvcT6AjfePLPZGiPfwoBeiiQFgM8kibj49e3mHp22wXt2SEaEcH7hKbMgpR91CPprotwwiurnC7",
            "p2sigRHbu3NDmEYeVeUTts7y4TKcFod8cNZ3Nt4EzkfQs7QvEJrUjuz35C6nC5PQHt6jXpVDnLgnwNZv8jcDh7jhQfMfwu",
            "p2sigVCqsGG6NquJpTbMvHhCF3q6w3pPW4pxGaL75DGMtk4YXkPao1zsWu79jrXmP2x1mkDLtbrRLZC19zsWE8NHMunVEA48FLigmr",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "8a06b71374f2f0b9c8591937c111752af7b584a535e9f606b62e784f6be4723d036452cec86cc91464235a5620c0f334989f52ff556d2599946a44a01b64c15c",
            "36f02c348a06b71374f2f0b9c8591937c111752af7b584a535e9f606b62e784f6be4723d036452cec86cc91464235a5620c0f334989f52ff556d2599946a44a01b64c15c",
            "1e7435de125896300a350bf1e13ef71915ee7934332c260d10a0dddd7e3e3b3f9a580ee3d4f44acc5d5cd8cf0eb746c165f24c2acd56982fabba3b34a7f40913",
            "36f02c341e7435de125896300a350bf1e13ef71915ee7934332c260d10a0dddd7e3e3b3f9a580ee3d4f44acc5d5cd8cf0eb746c165f24c2acd56982fabba3b34a7f40913",
            "3c66b136ec9581e4e96dab07f9e0b5059a3b6bed294be3bebba64b6aaafc1d66c29fc0f9fce985b8306d54d7d674890317d08d3c1bdb177eab8c4fad80781e11",
            "36f02c343c66b136ec9581e4e96dab07f9e0b5059a3b6bed294be3bebba64b6aaafc1d66c29fc0f9fce985b8306d54d7d674890317d08d3c1bdb177eab8c4fad80781e11",
            "bb9fcbc648ebe62ba3f5350681fd9d74235e7bd3f7c3da8f6c2127a2197cc2b6e664c62a6c9cc3778e39ee873e720e9ff6e51b72e676b89ef796d88f14f34c11",
            "36f02c34bb9fcbc648ebe62ba3f5350681fd9d74235e7bd3f7c3da8f6c2127a2197cc2b6e664c62a6c9cc3778e39ee873e720e9ff6e51b72e676b89ef796d88f14f34c11",
            "36bf97655085780bb57375c1dfe4b86f68697ad4f51aafada07fcffcb0ad84278448ebc06cd31122fa83ed6d54268097f3a17d22930a3ca19cb01a43204293c2",
            "36f02c3436bf97655085780bb57375c1dfe4b86f68697ad4f51aafada07fcffcb0ad84278448ebc06cd31122fa83ed6d54268097f3a17d22930a3ca19cb01a43204293c2",
            "9364d27c59ebed702ca59dfef15eff9a6a06d6f22e3bd2b47e26f9b05ab9966844973fddfcc53fa75edb74cb1c5e750a319365c62d34b95f70248d7496b902ad",
            "36f02c349364d27c59ebed702ca59dfef15eff9a6a06d6f22e3bd2b47e26f9b05ab9966844973fddfcc53fa75edb74cb1c5e750a319365c62d34b95f70248d7496b902ad",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "8a06b71374f2f0b9c8591937c111752af7b584a535e9f606b62e784f6be4723d036452cec86cc91464235a5620c0f334989f52ff556d2599946a44",
            "1e7435de125896300a350bf1e13ef71915ee7934332c260d10a0dddd7e3e3b3f9a580ee3d4f44acc5d5cd8cf0eb746c165f24c2acd56982fabba3b34a7f409137435",
            "16f02c349364d27c59ebed702ca59dfef15eff9a6a06d6f22e3bd2b47e26f9b05ab9966844973fddfcc53fa75edb74cb1c5e750a319365c62d34b95f70248d7496b902ad",
        ).map { it.asHexString().toByteArray() }
}