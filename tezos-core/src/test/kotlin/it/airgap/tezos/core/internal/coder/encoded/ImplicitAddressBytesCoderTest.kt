package it.airgap.tezos.core.internal.coder.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.P256PublicKeyHash
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKeyHash
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ImplicitAddressBytesCoderTest {

    private lateinit var tezos: Tezos
    private lateinit var implicitAddressBytesCoder: ImplicitAddressBytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        implicitAddressBytesCoder = ImplicitAddressBytesCoder(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode ImplicitAddress to bytes`() = withTezosContext {
        keyHashesWithBytes.forEach {
            assertContentEquals(it.second, implicitAddressBytesCoder.encode(it.first))
            assertContentEquals(it.second, it.first.encodeToBytes(tezos))
            assertContentEquals(it.second, it.first.encodeToBytes(implicitAddressBytesCoder))
        }
    }

    @Test
    fun `should decode ImplicitAddress from bytes`() = withTezosContext {
        keyHashesWithBytes.forEach {
            assertEquals(it.first, implicitAddressBytesCoder.decode(it.second))
            assertEquals(it.first, implicitAddressBytesCoder.decodeConsuming(it.second.toMutableList()))
            assertEquals(it.first, ImplicitAddress.decodeFromBytes(it.second, implicitAddressBytesCoder))
            assertEquals(it.first, ImplicitAddress.decodeFromBytes(it.second, tezos))
            assertEquals(it.first, ImplicitAddress.decodeConsumingFromBytes(it.second.toMutableList(), implicitAddressBytesCoder))
        }
    }

    @Test
    fun `should fail to decode ImplicitAddress from invalid bytes`() = withTezosContext {
        listOf(
            invalidBytes,
            listOf(
                "00ec6575487c0f706d9f936a33a0dd5a2f7f822502a5".asHexString().toByteArray(),
                "016cbe94a25b5de8b2aede4816d7e744c761d1c39e73".asHexString().toByteArray(),
                "0234f443872ae83d91d50747de6abf6a125d63112b26".asHexString().toByteArray(),
            ),
        ).flatten().forEach {
            assertFailsWith<IllegalArgumentException> { implicitAddressBytesCoder.decode(it) }
            assertFailsWith<IllegalArgumentException> { ImplicitAddress.decodeFromBytes(it, tezos) }
            assertFailsWith<IllegalArgumentException> { ImplicitAddress.decodeFromBytes(it, implicitAddressBytesCoder) }
        }

        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { implicitAddressBytesCoder.decodeConsuming(it.toMutableList()) }
            assertFailsWith<IllegalArgumentException> { ImplicitAddress.decodeConsumingFromBytes(it.toMutableList(), implicitAddressBytesCoder) }
        }
    }

    private val keyHashesWithBytes: List<Pair<ImplicitAddress, ByteArray>>
        get() = listOf(
            Ed25519PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e") to "00d7a60d4e90e8a33ec835159191b14ce4452f12f8".asHexString().toByteArray(),
            Ed25519PublicKeyHash("tz1VmzGTgLxb2qw8tXQvvM5QMRigMMoStTDR") to "006f328fa5c2ea6fd8596b63eafeabf91a78d37ae0".asHexString().toByteArray(),
            Ed25519PublicKeyHash("tz1hByhpsNVUsEHndaAaqk6ikEwpeCKQdUZe") to "00ec6575487c0f706d9f936a33a0dd5a2f7f822502".asHexString().toByteArray(),
            Secp256K1PublicKeyHash("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy") to "011a868789216112194b3bb003e8384c8f7217b288".asHexString().toByteArray(),
            Secp256K1PublicKeyHash("tz2Mqgq7nrEXD51JkMe85w3H2WT3qMwz3Emt") to "01945bf5622be703dcce774f9d940ed307b52905f5".asHexString().toByteArray(),
            Secp256K1PublicKeyHash("tz2JEDwptRu3P69Sv7UhVqzN3shFR2vSZzdk") to "016cbe94a25b5de8b2aede4816d7e744c761d1c39e".asHexString().toByteArray(),
            P256PublicKeyHash("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B") to "021a78f4332a6fe15b979904c6c2e5f9521e1ffc4a".asHexString().toByteArray(),
            P256PublicKeyHash("tz3imABPfVxJ33MAW9uFVnvV4qVSiU69AQut") to "02f612a5590e21bcb4cabc85b30dbbe5f63533e0e7".asHexString().toByteArray(),
            P256PublicKeyHash("tz3RA3NP8Y8MnuJSzUZCvpyD3AsLZ6CG9h5L") to "0234f443872ae83d91d50747de6abf6a125d63112b".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
            "00d7a60d4e90e8a33ec835159191b14ce4452f12".asHexString().toByteArray(),
            "6f328fa5c2ea6fd8596b63eafeabf91a78d37ae0".asHexString().toByteArray(),
            "011a868789216112194b3bb003e8384c8f7217b2".asHexString().toByteArray(),
            "945bf5622be703dcce774f9d940ed307b52905f5".asHexString().toByteArray(),
            "021a78f4332a6fe15b979904c6c2e5f9521e1ffc".asHexString().toByteArray(),
            "f612a5590e21bcb4cabc85b30dbbe5f63533e0e7".asHexString().toByteArray(),
        )
}