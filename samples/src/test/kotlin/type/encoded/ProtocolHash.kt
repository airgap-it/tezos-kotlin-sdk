package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class ProtocolHashSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val protocolHash = "Prs3cYwqfDby4srSMpv8JxuEC2UoosAYagQ3K69NCa6E8fZZQtx"
            assertTrue(ProtocolHash.isValid(protocolHash))

            val unknownHash = "rs3cYwqfDby4srSMpv8JxuEC2UoosAYagQ3K69NCa6E8fZZQtx"
            assertFalse(ProtocolHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val protocolHash = ProtocolHash("Prs3cYwqfDby4srSMpv8JxuEC2UoosAYagQ3K69NCa6E8fZZQtx")
            assertContentEquals(hexToBytes("12f5af2941855df5042141bc210ebb10d7fee174557b390c00923866ba988e1a"), protocolHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val protocolHash = hexToBytes("12f5af2941855df5042141bc210ebb10d7fee174557b390c00923866ba988e1a")
            assertEquals(ProtocolHash("Prs3cYwqfDby4srSMpv8JxuEC2UoosAYagQ3K69NCa6E8fZZQtx"), ProtocolHash.decodeFromBytes(protocolHash))
        }
    }
}
            