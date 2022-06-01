package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class ScriptExprHashSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val scriptExprHash = "expruYPgMhqSpMcF7WzfgnPY21fUsgXTQ6RG6LVVtuMjqJXqaN2DEp"
            assertTrue(ScriptExprHash.isValid(scriptExprHash))

            val unknownHash = "uYPgMhqSpMcF7WzfgnPY21fUsgXTQ6RG6LVVtuMjqJXqaN2DEp"
            assertFalse(ScriptExprHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val scriptExprHash = ScriptExprHash("expruYPgMhqSpMcF7WzfgnPY21fUsgXTQ6RG6LVVtuMjqJXqaN2DEp")
            assertContentEquals(hexToBytes("8721f015bc9c716788ac8ae4ad0af55af6bccd1d34c2a648d57021cce1e66ad2"), scriptExprHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val scriptExprHash = hexToBytes("8721f015bc9c716788ac8ae4ad0af55af6bccd1d34c2a648d57021cce1e66ad2")
            assertEquals(ScriptExprHash("expruYPgMhqSpMcF7WzfgnPY21fUsgXTQ6RG6LVVtuMjqJXqaN2DEp"), ScriptExprHash.decodeFromBytes(scriptExprHash))
        }
    }
}
            