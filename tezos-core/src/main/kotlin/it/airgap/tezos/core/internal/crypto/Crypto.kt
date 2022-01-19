package it.airgap.tezos.core.internal.crypto

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.type.HexString

public class Crypto(private val provider: CryptoProvider)  {
    public fun hashSha256(message: HexString): ByteArray = hashSha256(message.toByteArray())
    public fun hashSha256(message: ByteArray): ByteArray = provider.hash256(message)
}