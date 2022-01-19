package it.airgap.tezos.crypto.tuweni

import it.airgap.tezos.core.crypto.CryptoProvider
import org.apache.tuweni.crypto.sodium.SHA256Hash

public class TuweniCryptoProvider : CryptoProvider {
    override fun hash256(message: ByteArray): ByteArray {
        val input = SHA256Hash.Input.fromBytes(message)
        val hash = SHA256Hash.hash(input)

        return hash.bytesArray().also {
            input.destroy()
            hash.destroy()
        }
    }
}