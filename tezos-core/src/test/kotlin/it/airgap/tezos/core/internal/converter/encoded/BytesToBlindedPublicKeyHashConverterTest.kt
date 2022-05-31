package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.fromBytes
import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.type.encoded.BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.Ed25519BlindedPublicKeyHash
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BytesToBlindedPublicKeyHashConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var bytesToBlindedPublicKeyHashConverter: BytesToBlindedPublicKeyHashConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        bytesToBlindedPublicKeyHashConverter = BytesToBlindedPublicKeyHashConverter(tezos.dependencyRegistry.base58Check)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert bytes to PublicKeyEncoded`() = withTezosContext {
        publicKeysWithBytes.forEach {
            assertEquals(it.first, bytesToBlindedPublicKeyHashConverter.convert(it.second))
            assertEquals(it.first, BlindedPublicKeyHash.fromBytes(it.second, tezos))
            assertEquals(it.first, BlindedPublicKeyHash.fromBytes(it.second, bytesToBlindedPublicKeyHashConverter))
        }
    }

    @Test
    fun `should fail to convert invalid bytes to PublicKeyEncoded`() = withTezosContext {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { bytesToBlindedPublicKeyHashConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { BlindedPublicKeyHash.fromBytes(it, tezos) }
            assertFailsWith<IllegalArgumentException> { BlindedPublicKeyHash.fromBytes(it, bytesToBlindedPublicKeyHashConverter) }
        }
    }

    private val publicKeysWithBytes: List<Pair<BlindedPublicKeyHash, ByteArray>>
        get() = listOf(
            Ed25519BlindedPublicKeyHash("btz1ecEEauoGbHrbyzVTVNZhK4B8YHUfVgkzj") to "c89625a38f46f6c36a047108df313915d74266e0".asHexString().toByteArray(),
            Ed25519BlindedPublicKeyHash("btz1cnHeSrGs8LzwS3tJ8GKXfmjgtN2dRfRMW") to "010231dfb48ce71bce920f9b6d96a2d3f1d27610161bde42".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "c89625a38f46f6c36a047108df313915d74266",
            "c89625a38f46f6c36a047108df313915d74266e0f4",
            "010231dfb48ce71bce920f9b6d96a2d3f1d27610161bde",
        ).map { it.asHexString().toByteArray() }
}