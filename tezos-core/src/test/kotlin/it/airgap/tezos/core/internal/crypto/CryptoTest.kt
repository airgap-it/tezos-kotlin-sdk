package it.airgap.tezos.core.internal.crypto

import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.utils.asHexString
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals

class CryptoTest {
    @MockK
    private lateinit var cryptoProvider: CryptoProvider

    private lateinit var crypto: Crypto

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        crypto = Crypto(cryptoProvider)
    }

    @Test
    fun `creates SHA-256 hash from HexString message`() {
        every { cryptoProvider.hash256(any()) } answers { "SHA-256".toByteArray() + firstArg<ByteArray>() }

        val message = "9434dc98".asHexString()
        val hash = crypto.hashSha256(message)

        assertContentEquals("SHA-256".toByteArray() + message.toByteArray(), hash)
        verify { cryptoProvider.hash256(message.toByteArray()) }

        confirmVerified(cryptoProvider)
    }

    @Test
    fun `creates SHA-256 hash from ByteArray message`() {
        every { cryptoProvider.hash256(any()) } answers { "SHA-256".toByteArray() + firstArg<ByteArray>() }

        val message = "9434dc98".asHexString().toByteArray()
        val hash = crypto.hashSha256(message)

        assertContentEquals("SHA-256".toByteArray() + message, hash)
        verify { cryptoProvider.hash256(message) }

        confirmVerified(cryptoProvider)
    }
}