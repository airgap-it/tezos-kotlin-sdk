package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKey
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Secp256K1PublicKeySamples {

    class Usage {

        @Test
        fun isValid() {
            val publicKey = "sppkBbxstkHNxvWSWWmjeYKMrL1GavombMMLNbduQCpSUTDQ2k2RuE4"
            assertTrue(Secp256K1PublicKey.isValid(publicKey))

            val unknownKey = "BbxstkHNxvWSWWmjeYKMrL1GavombMMLNbduQCpSUTDQ2k2RuE4"
            assertFalse(Secp256K1PublicKey.isValid(unknownKey))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKey = Secp256K1PublicKey("sppkBbxstkHNxvWSWWmjeYKMrL1GavombMMLNbduQCpSUTDQ2k2RuE4")
            assertContentEquals(hexToBytes("7ab7b83157c4dba331e21227ad01681f2410ca724403280a74ea4541d83d903bc2"), publicKey.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKey = hexToBytes("7ab7b83157c4dba331e21227ad01681f2410ca724403280a74ea4541d83d903bc2")
            assertEquals(Secp256K1PublicKey("sppkBbxstkHNxvWSWWmjeYKMrL1GavombMMLNbduQCpSUTDQ2k2RuE4"), Secp256K1PublicKey.decodeFromBytes(publicKey))
        }
    }
}
            