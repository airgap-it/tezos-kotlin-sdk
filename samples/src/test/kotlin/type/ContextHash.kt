package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.ContextHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class ContextHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val contextHash = "CoVd2vUg9onub8w2ooJR7PyinX7sfxYk8YWBzg77yvY2T64E4eKM"
            assertTrue(ContextHash.isValid(contextHash))

            val unknownHash = "Vd2vUg9onub8w2ooJR7PyinX7sfxYk8YWBzg77yvY2T64E4eKM"
            assertFalse(ContextHash.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val contextHash = ContextHash("CoVd2vUg9onub8w2ooJR7PyinX7sfxYk8YWBzg77yvY2T64E4eKM")
            assertContentEquals(hexToBytes("80cd3c83957a801c336209c2418b476906fab82de4cd199252b2d5693146c166"), contextHash.encodeToBytes())
            assertContentEquals(hexToBytes("80cd3c83957a801c336209c2418b476906fab82de4cd199252b2d5693146c166"), contextHash.encodeToBytes(tezos))
        }

        @Test
        fun fromBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val contextHash = hexToBytes("80cd3c83957a801c336209c2418b476906fab82de4cd199252b2d5693146c166")
            assertEquals(ContextHash("CoVd2vUg9onub8w2ooJR7PyinX7sfxYk8YWBzg77yvY2T64E4eKM"), ContextHash.decodeFromBytes(contextHash))
            assertEquals(ContextHash("CoVd2vUg9onub8w2ooJR7PyinX7sfxYk8YWBzg77yvY2T64E4eKM"), ContextHash.decodeFromBytes(contextHash, tezos))
        }
    }
}
