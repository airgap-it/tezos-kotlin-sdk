package it.airgap.tezos.core.internal.coder.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.*
import mockTezos
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AddressBytesCoderTest {

    private lateinit var tezos: Tezos
    private lateinit var addressBytesCoder: AddressBytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        addressBytesCoder = AddressBytesCoder(
            tezos.coreModule.dependencyRegistry.implicitAddressBytesCoder,
            tezos.coreModule.dependencyRegistry.originatedAddressBytesCoder,
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode Address to bytes`() = withTezosContext {
        addressesWithBytes.forEach {
            assertContentEquals(it.second, addressBytesCoder.encode(it.first))
            assertContentEquals(it.second, it.first.encodeToBytes(tezos))
            assertContentEquals(it.second, it.first.encodeToBytes(addressBytesCoder))
        }
    }

    @Test
    fun `should decode Address from bytes`() = withTezosContext {
        addressesWithBytes.forEach {
            assertEquals(it.first, addressBytesCoder.decode(it.second))
            assertEquals(it.first, addressBytesCoder.decodeConsuming(it.second.toMutableList()))
            assertEquals(it.first, Address.decodeFromBytes(it.second, tezos))
            assertEquals(it.first, Address.decodeFromBytes(it.second, addressBytesCoder))
            assertEquals(it.first, Address.decodeConsumingFromBytes(it.second.toMutableList(), addressBytesCoder))
        }
    }

    @Test
    fun `should fail to decode Address from invalid bytes`() = withTezosContext {
        listOf(
            invalidBytes,
            listOf(
                "0000ec6575487c0f706d9f936a33a0dd5a2f7f822502a5".asHexString().toByteArray(),
                "00016cbe94a25b5de8b2aede4816d7e744c761d1c39e73".asHexString().toByteArray(),
                "000234f443872ae83d91d50747de6abf6a125d63112b26".asHexString().toByteArray(),
                "01f3ada5fa3d0003009b493ae4b1af6bf5e7cd6963d8".asHexString().toByteArray(),
            ),
        ).flatten().forEach {
            assertFailsWith<IllegalArgumentException> { addressBytesCoder.decode(it) }
            assertFailsWith<IllegalArgumentException> { Address.decodeFromBytes(it, tezos) }
            assertFailsWith<IllegalArgumentException> { Address.decodeFromBytes(it, addressBytesCoder) }
        }

        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { addressBytesCoder.decodeConsuming(it.toMutableList()) }
            assertFailsWith<IllegalArgumentException> { Address.decodeConsumingFromBytes(it.toMutableList(), addressBytesCoder) }
        }
    }

    private val addressesWithBytes: List<Pair<Address, ByteArray>>
        get() = listOf(
            Ed25519PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e") to "0000d7a60d4e90e8a33ec835159191b14ce4452f12f8".asHexString().toByteArray(),
            Ed25519PublicKeyHash("tz1VmzGTgLxb2qw8tXQvvM5QMRigMMoStTDR") to "00006f328fa5c2ea6fd8596b63eafeabf91a78d37ae0".asHexString().toByteArray(),
            Ed25519PublicKeyHash("tz1hByhpsNVUsEHndaAaqk6ikEwpeCKQdUZe") to "0000ec6575487c0f706d9f936a33a0dd5a2f7f822502".asHexString().toByteArray(),
            Secp256K1PublicKeyHash("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy") to "00011a868789216112194b3bb003e8384c8f7217b288".asHexString().toByteArray(),
            Secp256K1PublicKeyHash("tz2Mqgq7nrEXD51JkMe85w3H2WT3qMwz3Emt") to "0001945bf5622be703dcce774f9d940ed307b52905f5".asHexString().toByteArray(),
            Secp256K1PublicKeyHash("tz2JEDwptRu3P69Sv7UhVqzN3shFR2vSZzdk") to "00016cbe94a25b5de8b2aede4816d7e744c761d1c39e".asHexString().toByteArray(),
            P256PublicKeyHash("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B") to "00021a78f4332a6fe15b979904c6c2e5f9521e1ffc4a".asHexString().toByteArray(),
            P256PublicKeyHash("tz3imABPfVxJ33MAW9uFVnvV4qVSiU69AQut") to "0002f612a5590e21bcb4cabc85b30dbbe5f63533e0e7".asHexString().toByteArray(),
            P256PublicKeyHash("tz3RA3NP8Y8MnuJSzUZCvpyD3AsLZ6CG9h5L") to "000234f443872ae83d91d50747de6abf6a125d63112b".asHexString().toByteArray(),
            ContractHash("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7") to "016077cd98fd8aca94851b83a4c44203b705d2004b00".asHexString().toByteArray(),
            ContractHash("KT1PMSWmevCWDhSRqxkRTknQNyfdJetctoab") to "01a2049364703140f6e6f83b54e15021d63ed3700a00".asHexString().toByteArray(),
            ContractHash("KT1WoDngLZcTnKdW9KDray2BH8PbGW5BWPhT") to "01f3ada5fa3d0003009b493ae4b1af6bf5e7cd696300".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
            "0000d7a60d4e90e8a33ec835159191b14ce4452f12".asHexString().toByteArray(),
            "006f328fa5c2ea6fd8596b63eafeabf91a78d37ae0".asHexString().toByteArray(),
            "00011a868789216112194b3bb003e8384c8f7217b2".asHexString().toByteArray(),
            "00945bf5622be703dcce774f9d940ed307b52905f5".asHexString().toByteArray(),
            "00021a78f4332a6fe15b979904c6c2e5f9521e1ffc".asHexString().toByteArray(),
            "00f612a5590e21bcb4cabc85b30dbbe5f63533e0e7".asHexString().toByteArray(),
            "016077cd98fd8aca94851b83a4c44203b705d200".asHexString().toByteArray(),
            "a2049364703140f6e6f83b54e15021d63ed3700a".asHexString().toByteArray(),
        )
}