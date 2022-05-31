package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Address
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
            val contractHash = "KT1SLXd7g5YqT81PnH4R9K4hwz9kJpCwNkn2"
            assertTrue(Address.isValid(contractHash))

            val unknownHash = "SLXd7g5YqT81PnH4R9K4hwz9kJpCwNkn2"
            assertFalse(Address.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val contractHash = ContractHash("KT1SLXd7g5YqT81PnH4R9K4hwz9kJpCwNkn2")
            assertContentEquals(hexToBytes("c2c0cd60707bd74cb985672e45c4eedb333b5d74"), contractHash.encodeToBytes())
            assertContentEquals(hexToBytes("c2c0cd60707bd74cb985672e45c4eedb333b5d74"), contractHash.encodeToBytes(tezos))
        }

        @Test
        fun fromBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val contractHash = hexToBytes("c2c0cd60707bd74cb985672e45c4eedb333b5d74")
            assertEquals(ContractHash("KT1SLXd7g5YqT81PnH4R9K4hwz9kJpCwNkn2"), ContractHash.decodeFromBytes(contractHash))
            assertEquals(ContractHash("KT1SLXd7g5YqT81PnH4R9K4hwz9kJpCwNkn2"), ContractHash.decodeFromBytes(contractHash, tezos))
        }
    }
}
