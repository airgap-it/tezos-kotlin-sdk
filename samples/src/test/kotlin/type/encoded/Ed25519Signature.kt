package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.converter.encoded.fromGenericSignature
import it.airgap.tezos.core.converter.encoded.toGenericSignature
import it.airgap.tezos.core.type.encoded.Ed25519Signature
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Ed25519SignatureSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val signature = "edsigu3djbAZRFuEmkqCFkg58M6gMbRHS9TTUxCNw1ZFqBJmeKsrRPXWKVE7v3PbS6pTP4KXeJDSTHqwKK6ajFU13EysczvcvDp"
            assertTrue(Ed25519Signature.isValid(signature))

            val unknownSignature = "u3djbAZRFuEmkqCFkg58M6gMbRHS9TTUxCNw1ZFqBJmeKsrRPXWKVE7v3PbS6pTP4KXeJDSTHqwKK6ajFU13EysczvcvDp"
            assertFalse(Ed25519Signature.isValid(unknownSignature))
        }

        @Test
        fun fromGeneric() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val genericSignature = GenericSignature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u")
            assertEquals(Ed25519Signature("edsigtczTq2EC9VQNRRT53gzcs25DJFg1iZeTzQxY7jBtjradZb8qqZaqzAYSbVWvg1abvqFpQCV8TgqotDwckJiTJ9fJ2eYESb"), Ed25519Signature.fromGenericSignature(genericSignature))
        }

        @Test
        fun toGeneric() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val edSignature = Ed25519Signature("edsigtczTq2EC9VQNRRT53gzcs25DJFg1iZeTzQxY7jBtjradZb8qqZaqzAYSbVWvg1abvqFpQCV8TgqotDwckJiTJ9fJ2eYESb")
            assertEquals(GenericSignature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"), edSignature.toGenericSignature())
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val signature = Ed25519Signature("edsigu3djbAZRFuEmkqCFkg58M6gMbRHS9TTUxCNw1ZFqBJmeKsrRPXWKVE7v3PbS6pTP4KXeJDSTHqwKK6ajFU13EysczvcvDp")
            assertContentEquals(hexToBytes("e3fc85826d7b358d170e8dca55c9e43c102a2c2c5e936a9c4883e8a08a02255ef4f91bb0bbcad57c4163043d1d2dfb087265974a42cea0e7534dd6a9abecba4a"), signature.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val signature = hexToBytes("e3fc85826d7b358d170e8dca55c9e43c102a2c2c5e936a9c4883e8a08a02255ef4f91bb0bbcad57c4163043d1d2dfb087265974a42cea0e7534dd6a9abecba4a")
            assertEquals(Ed25519Signature("edsigu3djbAZRFuEmkqCFkg58M6gMbRHS9TTUxCNw1ZFqBJmeKsrRPXWKVE7v3PbS6pTP4KXeJDSTHqwKK6ajFU13EysczvcvDp"), Ed25519Signature.decodeFromBytes(signature))
        }
    }
}
            