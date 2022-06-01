package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class ContractHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val contractHash = "KT1PcTwFybtnRmJ6JqMJ3GFY9zCorxXdpvDq"
            assertTrue(ContractHash.isValid(contractHash))

            val unknownHash = "PcTwFybtnRmJ6JqMJ3GFY9zCorxXdpvDq"
            assertFalse(ContractHash.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val contractHash = ContractHash("KT1PcTwFybtnRmJ6JqMJ3GFY9zCorxXdpvDq")
            assertContentEquals(hexToBytes("a4dc009c0db46b58873b90bceaaed08fbab6aee4"), contractHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val contractHash = hexToBytes("a4dc009c0db46b58873b90bceaaed08fbab6aee4")
            assertEquals(ContractHash("KT1PcTwFybtnRmJ6JqMJ3GFY9zCorxXdpvDq"), ContractHash.decodeFromBytes(contractHash))
        }
    }
}
            