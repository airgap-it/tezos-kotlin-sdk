package it.airgap.tezos.operation.internal.signer

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.internal.operationModule
import it.airgap.tezos.operation.signer.sign
import it.airgap.tezos.operation.signer.verify
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class OperationSignerTest {

    private lateinit var tezos: Tezos

    private lateinit var operationSigner: OperationSigner
    private lateinit var operationEd25519Signer: OperationEd25519Signer
    private lateinit var operationSecp256K1Signer: OperationSecp256K1Signer
    private lateinit var operationP256Signer: OperationP256Signer

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()

        operationEd25519Signer = OperationEd25519Signer(
            tezos.dependencyRegistry.crypto,
            tezos.operationModule.dependencyRegistry.operationBytesCoder,
            tezos.coreModule.dependencyRegistry.encodedBytesCoder,
        )

        operationSecp256K1Signer = OperationSecp256K1Signer(
            tezos.dependencyRegistry.crypto,
            tezos.operationModule.dependencyRegistry.operationBytesCoder,
            tezos.coreModule.dependencyRegistry.encodedBytesCoder,
        )

        operationP256Signer = OperationP256Signer(
            tezos.dependencyRegistry.crypto,
            tezos.operationModule.dependencyRegistry.operationBytesCoder,
            tezos.coreModule.dependencyRegistry.encodedBytesCoder,
        )

        operationSigner = OperationSigner(operationEd25519Signer, operationSecp256K1Signer, operationP256Signer)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should sign Operation`() {
        operationsWithEd25519Signatures.forEach {
            val key = ed25519KeyPair.first

            val signedWithCurrentKey = Operation.Signed.from(it.first, it.second)
            val signedWithOtherKey = Operation.Signed.from(it.first, Ed25519Signature("edsigtsnqaaNExgKjVyp4J5pphV4GvmEWh9T5hXyfLn1Bs2rLubib9htT8hy575R733UrmTc65cgjuNYKXQxGS2dR9BW98kKw8m"))

            assertEquals(it.second, operationSigner.sign(it.first, key))
            assertEquals(it.second, operationEd25519Signer.sign(it.first, key))
            assertEquals(it.second, operationSigner.sign(signedWithCurrentKey, key))
            assertEquals(it.second, operationEd25519Signer.sign(signedWithCurrentKey, key))
            assertEquals(signedWithCurrentKey, it.first.sign(key, tezos))
            assertEquals(signedWithCurrentKey, it.first.sign(key, operationSigner))
            assertEquals(signedWithCurrentKey, signedWithCurrentKey.sign(key, tezos))
            assertEquals(signedWithCurrentKey, signedWithCurrentKey.sign(key, operationSigner))
            assertEquals(signedWithCurrentKey, signedWithOtherKey.sign(key, tezos))
            assertEquals(signedWithCurrentKey, signedWithOtherKey.sign(key, operationSigner))
            assertEquals(it.second, key.sign(it.first, tezos))
            assertEquals(it.second, key.sign(it.first, operationSigner))
            assertEquals(it.second, key.sign(it.first, operationEd25519Signer))
        }

        operationsWithSecp256K1Signatures.forEach {
            val key = secp256K1KeyPair.first

            val signedWithCurrentKey = Operation.Signed.from(it.first, it.second)
            val signedWithOtherKey = Operation.Signed.from(it.first, Secp256K1Signature("spsig19ZLL6dzEryZ8sPp7ggaqK6p7P6oC4Zc24BTFYNZykuiuVA9KuNzfuoNETzPpqEzN16cLiHMfrfys8TTDV1ycyDgPL8wvm"))

            assertEquals(it.second, operationSigner.sign(it.first, key))
            assertEquals(it.second, operationSecp256K1Signer.sign(it.first, key))
            assertEquals(it.second, operationSigner.sign(signedWithCurrentKey, key))
            assertEquals(it.second, operationSecp256K1Signer.sign(signedWithCurrentKey, key))
            assertEquals(signedWithCurrentKey, it.first.sign(key, tezos))
            assertEquals(signedWithCurrentKey, it.first.sign(key, operationSigner))
            assertEquals(signedWithCurrentKey, signedWithCurrentKey.sign(key, tezos))
            assertEquals(signedWithCurrentKey, signedWithCurrentKey.sign(key, operationSigner))
            assertEquals(signedWithCurrentKey, signedWithOtherKey.sign(key, tezos))
            assertEquals(signedWithCurrentKey, signedWithOtherKey.sign(key, operationSigner))
            assertEquals(it.second, key.sign(it.first, tezos))
            assertEquals(it.second, key.sign(it.first, operationSigner))
            assertEquals(it.second, key.sign(it.first, operationSecp256K1Signer))
        }

        operationsWithP256Signatures.forEach {
            val key = p256KeyPair.first

            val signedWithCurrentKey = Operation.Signed.from(it.first, it.second)
            val signedWithOtherKey = Operation.Signed.from(it.first, P256Signature("p2sigTrmxRGhckRaai4vVSDBeRwUfuPhHzJZmQtnX9MxaUsRBE9KMgNe1nwA2BWWZdH8qUtcE5nr8H5XjD8VtcJsabqGpNDRgx"))

            assertEquals(it.second, operationSigner.sign(it.first, key))
            assertEquals(it.second, operationP256Signer.sign(it.first, key))
            assertEquals(it.second, operationSigner.sign(signedWithCurrentKey, key))
            assertEquals(it.second, operationP256Signer.sign(signedWithCurrentKey, key))
            assertEquals(signedWithCurrentKey, it.first.sign(key, tezos))
            assertEquals(signedWithCurrentKey, it.first.sign(key, operationSigner))
            assertEquals(signedWithCurrentKey, signedWithCurrentKey.sign(key, tezos))
            assertEquals(signedWithCurrentKey, signedWithCurrentKey.sign(key, operationSigner))
            assertEquals(signedWithCurrentKey, signedWithOtherKey.sign(key, tezos))
            assertEquals(signedWithCurrentKey, signedWithOtherKey.sign(key, operationSigner))
            assertEquals(it.second, key.sign(it.first, tezos))
            assertEquals(it.second, key.sign(it.first, operationSigner))
            assertEquals(it.second, key.sign(it.first, operationP256Signer))
        }
    }

    @Test
    fun `should verify Operation signature`() {
        operationsWithEd25519Signatures.forEach {
            val key = ed25519KeyPair.second

            val signedWithCurrentKey = Operation.Signed.from(it.first, it.second)
            val signedWithOtherKey = Operation.Signed.from(it.first, Ed25519Signature("edsigtsnqaaNExgKjVyp4J5pphV4GvmEWh9T5hXyfLn1Bs2rLubib9htT8hy575R733UrmTc65cgjuNYKXQxGS2dR9BW98kKw8m"))
            val signedWithSecp256K1Key = Operation.Signed.from(it.first, Secp256K1Signature("spsig19ZLL6dzEryZ8sPp7ggaqK6p7P6oC4Zc24BTFYNZykuiuVA9KuNzfuoNETzPpqEzN16cLiHMfrfys8TTDV1ycyDgPL8wvm"))
            val signedWithP256Key = Operation.Signed.from(it.first, P256Signature("p2sigTrmxRGhckRaai4vVSDBeRwUfuPhHzJZmQtnX9MxaUsRBE9KMgNe1nwA2BWWZdH8qUtcE5nr8H5XjD8VtcJsabqGpNDRgx"))

            assertTrue(operationSigner.verify(signedWithCurrentKey, signedWithCurrentKey.signature, key), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(signedWithCurrentKey.verify(key, tezos), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(signedWithCurrentKey.verify(key, operationSigner), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(key.verify(signedWithCurrentKey, tezos), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(key.verify(signedWithCurrentKey, operationSigner), "Expected operation signed with matching secret key to be verified positively.")

            assertFalse(operationSigner.verify(signedWithOtherKey, signedWithOtherKey.signature, key), "Expected operation signed with other key to be verified negatively.")
            assertFalse(signedWithOtherKey.verify(key, tezos), "Expected operation signed with other key to be verified negatively.")
            assertFalse(signedWithOtherKey.verify(key, operationSigner), "Expected operation signed with other key to be verified negatively.")
            assertFalse(key.verify(signedWithOtherKey, tezos), "Expected operation signed with other key to be verified negatively.")
            assertFalse(key.verify(signedWithOtherKey, operationSigner), "Expected operation signed with other key to be verified negatively.")

            assertFalse(operationSigner.verify(signedWithSecp256K1Key, signedWithSecp256K1Key.signature, key), "Expected operation signed with secp256K1 key to be verified negatively.")
            assertFalse(signedWithSecp256K1Key.verify(key, tezos), "Expected operation signed with secp256K1 key to be verified negatively.")
            assertFalse(signedWithSecp256K1Key.verify(key, operationSigner), "Expected operation signed with secp256K1 key to be verified negatively.")
            assertFalse(key.verify(signedWithSecp256K1Key, tezos), "Expected operation signed with secp256K1 key to be verified negatively.")
            assertFalse(key.verify(signedWithSecp256K1Key, operationSigner), "Expected operation signed with secp256K1 key to be verified negatively.")

            assertFalse(operationSigner.verify(signedWithP256Key, signedWithP256Key.signature, key), "Expected operation signed with P256 key to be verified negatively.")
            assertFalse(signedWithP256Key.verify(key, tezos), "Expected operation signed with P256 key to be verified negatively.")
            assertFalse(signedWithP256Key.verify(key, operationSigner), "Expected operation signed with P256 key to be verified negatively.")
            assertFalse(key.verify(signedWithP256Key, tezos), "Expected operation signed with P256 key to be verified negatively.")
            assertFalse(key.verify(signedWithP256Key, operationSigner), "Expected operation signed with P256 key to be verified negatively.")
        }

        operationsWithSecp256K1Signatures.forEach {
            val key = secp256K1KeyPair.second

            val signedWithCurrentKey = Operation.Signed.from(it.first, it.second)
            val signedWithOtherKey = Operation.Signed.from(it.first, Secp256K1Signature("spsig19ZLL6dzEryZ8sPp7ggaqK6p7P6oC4Zc24BTFYNZykuiuVA9KuNzfuoNETzPpqEzN16cLiHMfrfys8TTDV1ycyDgPL8wvm"))
            val signedWithEd25519Key = Operation.Signed.from(it.first, Ed25519Signature("edsigtsnqaaNExgKjVyp4J5pphV4GvmEWh9T5hXyfLn1Bs2rLubib9htT8hy575R733UrmTc65cgjuNYKXQxGS2dR9BW98kKw8m"))
            val signedWithP256Key = Operation.Signed.from(it.first, P256Signature("p2sigTrmxRGhckRaai4vVSDBeRwUfuPhHzJZmQtnX9MxaUsRBE9KMgNe1nwA2BWWZdH8qUtcE5nr8H5XjD8VtcJsabqGpNDRgx"))

            assertTrue(operationSigner.verify(signedWithCurrentKey, signedWithCurrentKey.signature, key), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(signedWithCurrentKey.verify(key, tezos), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(signedWithCurrentKey.verify(key, operationSigner), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(key.verify(signedWithCurrentKey, tezos), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(key.verify(signedWithCurrentKey, operationSigner), "Expected operation signed with matching secret key to be verified positively.")

            assertFalse(operationSigner.verify(signedWithOtherKey, signedWithOtherKey.signature, key), "Expected operation signed with other key to be verified negatively.")
            assertFalse(signedWithOtherKey.verify(key, tezos), "Expected operation signed with other key to be verified negatively.")
            assertFalse(signedWithOtherKey.verify(key, operationSigner), "Expected operation signed with other key to be verified negatively.")
            assertFalse(key.verify(signedWithOtherKey, tezos), "Expected operation signed with other key to be verified negatively.")
            assertFalse(key.verify(signedWithOtherKey, operationSigner), "Expected operation signed with other key to be verified negatively.")

            assertFalse(operationSigner.verify(signedWithEd25519Key, signedWithEd25519Key.signature, key), "Expected operation signed with Ed25519 key to be verified negatively.")
            assertFalse(signedWithEd25519Key.verify(key, tezos), "Expected operation signed with Ed25519 key to be verified negatively.")
            assertFalse(signedWithEd25519Key.verify(key, operationSigner), "Expected operation signed with Ed25519 key to be verified negatively.")
            assertFalse(key.verify(signedWithEd25519Key, tezos), "Expected operation signed with Ed25519 key to be verified negatively.")
            assertFalse(key.verify(signedWithEd25519Key, operationSigner), "Expected operation signed with Ed25519 key to be verified negatively.")

            assertFalse(operationSigner.verify(signedWithP256Key, signedWithP256Key.signature, key), "Expected operation signed with P256 key to be verified negatively.")
            assertFalse(signedWithP256Key.verify(key, tezos), "Expected operation signed with P256 key to be verified negatively.")
            assertFalse(signedWithP256Key.verify(key, operationSigner), "Expected operation signed with P256 key to be verified negatively.")
            assertFalse(key.verify(signedWithP256Key, tezos), "Expected operation signed with P256 key to be verified negatively.")
            assertFalse(key.verify(signedWithP256Key, operationSigner), "Expected operation signed with P256 key to be verified negatively.")
        }

        operationsWithP256Signatures.forEach {
            val key = p256KeyPair.second

            val signedWithCurrentKey = Operation.Signed.from(it.first, it.second)
            val signedWithOtherKey = Operation.Signed.from(it.first, P256Signature("p2sigTrmxRGhckRaai4vVSDBeRwUfuPhHzJZmQtnX9MxaUsRBE9KMgNe1nwA2BWWZdH8qUtcE5nr8H5XjD8VtcJsabqGpNDRgx"))
            val signedWithEd25519Key = Operation.Signed.from(it.first, Ed25519Signature("edsigtsnqaaNExgKjVyp4J5pphV4GvmEWh9T5hXyfLn1Bs2rLubib9htT8hy575R733UrmTc65cgjuNYKXQxGS2dR9BW98kKw8m"))
            val signedWithSecp256K1Key = Operation.Signed.from(it.first, Secp256K1Signature("spsig19ZLL6dzEryZ8sPp7ggaqK6p7P6oC4Zc24BTFYNZykuiuVA9KuNzfuoNETzPpqEzN16cLiHMfrfys8TTDV1ycyDgPL8wvm"))

            assertTrue(operationSigner.verify(signedWithCurrentKey, signedWithCurrentKey.signature, key), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(signedWithCurrentKey.verify(key, tezos), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(signedWithCurrentKey.verify(key, operationSigner), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(key.verify(signedWithCurrentKey, tezos), "Expected operation signed with matching secret key to be verified positively.")
            assertTrue(key.verify(signedWithCurrentKey, operationSigner), "Expected operation signed with matching secret key to be verified positively.")

            assertFalse(operationSigner.verify(signedWithOtherKey, signedWithOtherKey.signature, key), "Expected operation signed with other key to be verified negatively.")
            assertFalse(signedWithOtherKey.verify(key, tezos), "Expected operation signed with other key to be verified negatively.")
            assertFalse(signedWithOtherKey.verify(key, operationSigner), "Expected operation signed with other key to be verified negatively.")
            assertFalse(key.verify(signedWithOtherKey, tezos), "Expected operation signed with other key to be verified negatively.")
            assertFalse(key.verify(signedWithOtherKey, operationSigner), "Expected operation signed with other key to be verified negatively.")

            assertFalse(operationSigner.verify(signedWithEd25519Key, signedWithEd25519Key.signature, key), "Expected operation signed with Ed25519 key to be verified negatively.")
            assertFalse(signedWithEd25519Key.verify(key, tezos), "Expected operation signed with Ed25519 key to be verified negatively.")
            assertFalse(signedWithEd25519Key.verify(key, operationSigner), "Expected operation signed with Ed25519 key to be verified negatively.")
            assertFalse(key.verify(signedWithEd25519Key, tezos), "Expected operation signed with Ed25519 key to be verified negatively.")
            assertFalse(key.verify(signedWithEd25519Key, operationSigner), "Expected operation signed with Ed25519 key to be verified negatively.")

            assertFalse(operationSigner.verify(signedWithSecp256K1Key, signedWithSecp256K1Key.signature, key), "Expected operation signed with secp256K1 key to be verified negatively.")
            assertFalse(signedWithSecp256K1Key.verify(key, tezos), "Expected operation signed with secp256K1 key to be verified negatively.")
            assertFalse(signedWithSecp256K1Key.verify(key, operationSigner), "Expected operation signed with secp256K1 key to be verified negatively.")
            assertFalse(key.verify(signedWithSecp256K1Key, tezos), "Expected operation signed with secp256K1 key to be verified negatively.")
            assertFalse(key.verify(signedWithSecp256K1Key, operationSigner), "Expected operation signed with secp256K1 key to be verified negatively.")
        }
    }

    private val operationsWithEd25519Signatures: List<Pair<Operation.Unsigned, Ed25519Signature>>
        get() = listOf(
            Operation.Unsigned(
                BlockHash("BLjg4HU2BwnCgJfRutxJX5rHACzLDxRJes1MXqbXXdxvHWdK3Te"),
                emptyList(),
            ) to Ed25519Signature("edsigtfLuR4pGGfJwYgWZbWi9JGzjLA8ThhThxqFGC8V6u4WTdS4fM7VFQKoN9jPDLKiAW75PtG1bykpnRa6ozr8m12iKGYCxNd"),
            Operation.Unsigned(
                BlockHash("BLjg4HU2BwnCgJfRutxJX5rHACzLDxRJes1MXqbXXdxvHWdK3Te"),
                listOf(OperationContent.SeedNonceRevelation(1, "6cdaf9367e551995a670a5c642a9396290f8c9d17e6bc3c1555bfaa910d92214".asHexString())),
            ) to Ed25519Signature("edsigtyP4ZD5NtBBkAkrmXQZg84xt9uCiHBpjqZj2HE65d4V9dkDapSVJ6jvaA4gEEgksVJzqSxdv2rnMyBzPoAfBQwNEqt8Y1x"),
            Operation.Unsigned(
                BlockHash("BLjg4HU2BwnCgJfRutxJX5rHACzLDxRJes1MXqbXXdxvHWdK3Te"),
                listOf(
                    OperationContent.SeedNonceRevelation(1, "9d15bcdc0194b327d3cb0dcd05242bc6ff1635da635e38ed7a62b8c413ce6833".asHexString()),
                    OperationContent.SeedNonceRevelation(2, "921ed0115c7cc1b5dcd07ad66ce4d9b2b0186c93c27a80d70b66b4e309add170".asHexString()),
                ),
            ) to Ed25519Signature("edsigu5i46oiR9Ye45rUJnPNLkEWkLvvGG5uzHCzPuoNFemNAguHBFn5hXiBivnHHdSzGqsMBc8c5cxAUr8Ue6FUVufbM3hECdU"),
        )

    private val operationsWithSecp256K1Signatures: List<Pair<Operation.Unsigned, Secp256K1Signature>>
        get() = listOf(
            Operation.Unsigned(
                BlockHash("BLjg4HU2BwnCgJfRutxJX5rHACzLDxRJes1MXqbXXdxvHWdK3Te"),
                emptyList(),
            ) to Secp256K1Signature("spsig1LPnrCkaRypLUz3UYdxQGVpxfSAxWwSV2HpaitKWvqRN6CDqqLJwWNn1S9kEWT2ZLrWq7m2361YVMN4LNkc9FVPdxBjYZi"),
            Operation.Unsigned(
                BlockHash("BLjg4HU2BwnCgJfRutxJX5rHACzLDxRJes1MXqbXXdxvHWdK3Te"),
                listOf(OperationContent.SeedNonceRevelation(1, "6cdaf9367e551995a670a5c642a9396290f8c9d17e6bc3c1555bfaa910d92214".asHexString())),
            ) to Secp256K1Signature("spsig1SC5sFkHG4YssRxQJQ5onZ8GNvfQDqk5cz1e6fdPhCNva3baoPCiE9fk6JcyUedEDFAEeMBgC7L6LeYBhFHpVrxjs96iuB"),
            Operation.Unsigned(
                BlockHash("BLjg4HU2BwnCgJfRutxJX5rHACzLDxRJes1MXqbXXdxvHWdK3Te"),
                listOf(
                    OperationContent.SeedNonceRevelation(1, "9d15bcdc0194b327d3cb0dcd05242bc6ff1635da635e38ed7a62b8c413ce6833".asHexString()),
                    OperationContent.SeedNonceRevelation(2, "921ed0115c7cc1b5dcd07ad66ce4d9b2b0186c93c27a80d70b66b4e309add170".asHexString()),
                ),
            ) to Secp256K1Signature("spsig1XFTLzrozPJ7Kc9aVNwK4hjpub7cWu8a95LmSKNucsPZjrgq3QRcQWtvo1fbBzpeWPK56XaUiJRN6B59kzueT6LCqTWK8R"),
        )

    private val operationsWithP256Signatures: List<Pair<Operation.Unsigned, P256Signature>>
        get() = listOf(
            Operation.Unsigned(
                BlockHash("BLjg4HU2BwnCgJfRutxJX5rHACzLDxRJes1MXqbXXdxvHWdK3Te"),
                emptyList(),
            ) to P256Signature("p2sigY5tNCTjyR3w2rbgBHnkcEChmtk43Gt6BKqwX2TsNdpVojk3QgRy9Wf3TMkAyRnagy4LrhC4AfVDFBQK87sqBipsNkCt5N"),
            Operation.Unsigned(
                BlockHash("BLjg4HU2BwnCgJfRutxJX5rHACzLDxRJes1MXqbXXdxvHWdK3Te"),
                listOf(OperationContent.SeedNonceRevelation(1, "6cdaf9367e551995a670a5c642a9396290f8c9d17e6bc3c1555bfaa910d92214".asHexString())),
            ) to P256Signature("p2sigUMZVy7WyyvYawCt8oW4eMvXCTWtmU6PCfsTbKmAUXuHFCcH8ER7ZwtNqsnwYER9DRKXfao9xhUFfYdxZPFFDi4J7nckvt"),
            Operation.Unsigned(
                BlockHash("BLjg4HU2BwnCgJfRutxJX5rHACzLDxRJes1MXqbXXdxvHWdK3Te"),
                listOf(
                    OperationContent.SeedNonceRevelation(1, "9d15bcdc0194b327d3cb0dcd05242bc6ff1635da635e38ed7a62b8c413ce6833".asHexString()),
                    OperationContent.SeedNonceRevelation(2, "921ed0115c7cc1b5dcd07ad66ce4d9b2b0186c93c27a80d70b66b4e309add170".asHexString()),
                ),
            ) to P256Signature("p2sigrjm1STjRF4ygPiPzd4L34MzCErExERsH79jWwJTdYqdaYbYA29UfE1y8f78268B2xNdT3gzR5tXR7G21DCYyYkGnFe3Dm"),
        )

    private val ed25519KeyPair: Pair<Ed25519SecretKey, Ed25519PublicKey>
        get() = Pair(Ed25519SecretKey("edskRv7VyXGVZb8EsrR7D9XKUbbAQNQGtALP6QeB16ZCD7SmmJpzyeneJVg3Mq56YLbxRA1kSdAXiswwPiaVfR3NHGMCXCziuZ"), Ed25519PublicKey("edpkttZKC51wemRqL2QxwpMnEKxWnbd35pq47Y6xsCHp5M1f7LN8NP"))

    private val secp256K1KeyPair: Pair<Secp256K1SecretKey, Secp256K1PublicKey>
        get() = Pair(Secp256K1SecretKey("spsk1SsrWCpufeXkNruaG9L3Mf9dRyd4D8HsM8ftqseN1fne3x9LNk"), Secp256K1PublicKey("sppk7ZpH5qAjTDZn1o1TW7z2QbQZUcMHRn2wtV4rRfz15eLQrvPkt6k"))

    private val p256KeyPair: Pair<P256SecretKey, P256PublicKey>
        get() = Pair(P256SecretKey("p2sk2rVhhi5EfEdhJ3wQGsdc4ZEN3i7Z8f73Bn1xp1JKjETNyJ85oW"), P256PublicKey("p2pk67fo5oy6byruqDtzVixbM7L3cVBDRMcFhA33XD5w2HF4fRXDJhw"))
}