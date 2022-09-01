package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.RandomHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class RandomHashSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val protocolHash = "rngHBUYKNskXPnCPLugKjBjb1pSsVT1MESLU9a2qsTymj5qHSgacH"
            assertTrue(RandomHash.isValid(protocolHash))

            val unknownHash = "rs3cYwqfDby4srSMpv8JxuEC2UoosAYagQ3K69NCa6E8fZZQtx"
            assertFalse(RandomHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val protocolHash = RandomHash("rngHBUYKNskXPnCPLugKjBjb1pSsVT1MESLU9a2qsTymj5qHSgacH")
            assertContentEquals(hexToBytes("b1510bf30a81451b44f5028ca5fa17483f6d47ab161f4ebe383c51dd7a3d1f0e"), protocolHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val protocolHash = hexToBytes("b1510bf30a81451b44f5028ca5fa17483f6d47ab161f4ebe383c51dd7a3d1f0e")
            assertEquals(RandomHash("rngHBUYKNskXPnCPLugKjBjb1pSsVT1MESLU9a2qsTymj5qHSgacH"), RandomHash.decodeFromBytes(protocolHash))
        }
    }
}
            