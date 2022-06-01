package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.core.converter.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class AddressSamples {

    class Usage {

        @Test
        fun isValid() {
            val tz1Address = "tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"
            assertTrue(Address.isValid(tz1Address))

            val tz2Address = "tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"
            assertTrue(Address.isValid(tz2Address))

            val tz3Address = "tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B"
            assertTrue(Address.isValid(tz3Address))

            val kt1Address = "KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7"
            assertTrue(Address.isValid(kt1Address))

            val unknownAddress = "tzhByhpsNVUsEHndaAaqk6ikEwpeCKQdUZe"
            assertFalse(Address.isValid(unknownAddress))
        }

        @Test
        fun create() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val tz1Address = Address("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
            assertEquals(Ed25519PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"), tz1Address)

            val tz2Address = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy")
            assertEquals(Secp256K1PublicKeyHash("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"), tz2Address)

            val tz3Address = Address("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B")
            assertEquals(P256PublicKeyHash("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B"), tz3Address)

            val kt1Address = Address("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7")
            assertEquals(ContractHash("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7"), kt1Address)
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val tz1Address = Address("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
            assertContentEquals(hexToBytes("0000d7a60d4e90e8a33ec835159191b14ce4452f12f8"), tz1Address.encodeToBytes())

            val tz2Address = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy")
            assertContentEquals(hexToBytes("00011a868789216112194b3bb003e8384c8f7217b288"), tz2Address.encodeToBytes())

            val tz3Address = Address("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B")
            assertContentEquals(hexToBytes("00021a78f4332a6fe15b979904c6c2e5f9521e1ffc4a"), tz3Address.encodeToBytes())

            val kt1Address = Address("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7")
            assertContentEquals(hexToBytes("016077cd98fd8aca94851b83a4c44203b705d2004b"), kt1Address.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val tz1Address = hexToBytes("0000d7a60d4e90e8a33ec835159191b14ce4452f12f8")
            assertEquals(Ed25519PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"), Address.decodeFromBytes(tz1Address))

            val tz2Address = hexToBytes("00011a868789216112194b3bb003e8384c8f7217b288")
            assertEquals(Secp256K1PublicKeyHash("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"), Address.decodeFromBytes(tz2Address))

            val tz3Address = hexToBytes("00021a78f4332a6fe15b979904c6c2e5f9521e1ffc4a")
            assertEquals(P256PublicKeyHash("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B"), Address.decodeFromBytes(tz3Address))

            val kt1Address = hexToBytes("016077cd98fd8aca94851b83a4c44203b705d2004b")
            assertEquals(ContractHash("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7"), Address.decodeFromBytes(kt1Address))
        }
    }
}

@RunWith(Enclosed::class)
class ImplicitAddressSamples {

    class Usage {

        @Test
        fun isValid() {
            val tz1Address = "tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"
            assertTrue(ImplicitAddress.isValid(tz1Address))

            val tz2Address = "tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"
            assertTrue(ImplicitAddress.isValid(tz2Address))

            val tz3Address = "tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B"
            assertTrue(ImplicitAddress.isValid(tz3Address))

            val kt1Address = "KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7"
            assertFalse(ImplicitAddress.isValid(kt1Address))

            val unknownAddress = "tzhByhpsNVUsEHndaAaqk6ikEwpeCKQdUZe"
            assertFalse(ImplicitAddress.isValid(unknownAddress))
        }

        @Test
        fun create() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val tz1Address = ImplicitAddress("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
            assertEquals(Ed25519PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"), tz1Address)

            val tz2Address = ImplicitAddress("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy")
            assertEquals(Secp256K1PublicKeyHash("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"), tz2Address)

            val tz3Address = ImplicitAddress("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B")
            assertEquals(P256PublicKeyHash("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B"), tz3Address)
        }
    }

    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val tz1Address = ImplicitAddress("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
            assertContentEquals(hexToBytes("00d7a60d4e90e8a33ec835159191b14ce4452f12f8"), tz1Address.encodeToBytes())

            val tz2Address = ImplicitAddress("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy")
            assertContentEquals(hexToBytes("011a868789216112194b3bb003e8384c8f7217b288"), tz2Address.encodeToBytes())

            val tz3Address = ImplicitAddress("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B")
            assertContentEquals(hexToBytes("021a78f4332a6fe15b979904c6c2e5f9521e1ffc4a"), tz3Address.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val tz1Address = hexToBytes("00d7a60d4e90e8a33ec835159191b14ce4452f12f8")
            assertEquals(Ed25519PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"), ImplicitAddress.decodeFromBytes(tz1Address))

            val tz2Address = hexToBytes("011a868789216112194b3bb003e8384c8f7217b288")
            assertEquals(Secp256K1PublicKeyHash("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"), ImplicitAddress.decodeFromBytes(tz2Address))

            val tz3Address = hexToBytes("021a78f4332a6fe15b979904c6c2e5f9521e1ffc4a")
            assertEquals(P256PublicKeyHash("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B"), ImplicitAddress.decodeFromBytes(tz3Address))
        }
    }
}