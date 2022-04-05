package it.airgap.tezos.core.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.fromGenericSignature
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.P256Signature
import it.airgap.tezos.core.type.encoded.SignatureEncoded
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals

class GenericSignatureToP256SignatureConverterTest {

    @MockK
    private lateinit var crypto: Crypto

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var genericSignatureToP256SignatureConverter: GenericSignatureToP256SignatureConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)
        val encodedBytesCoder = EncodedBytesCoder(base58Check)
        val signatureBytesCoder = SignatureBytesCoder(encodedBytesCoder)

        genericSignatureToP256SignatureConverter = GenericSignatureToP256SignatureConverter(signatureBytesCoder, encodedBytesCoder)

        every { dependencyRegistry.genericSignatureToP256SignatureConverter } returns genericSignatureToP256SignatureConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert GenericSignature to P256Signature`() {
        signaturesWithGenerics.forEach {
            assertEquals(it.first, genericSignatureToP256SignatureConverter.convert(it.second))
            assertEquals(it.first, P256Signature.fromGenericSignature(it.second))
            assertEquals(it.first, P256Signature.fromGenericSignature(it.second, genericSignatureToP256SignatureConverter))
        }
    }

    private val signaturesWithGenerics: List<Pair<SignatureEncoded, GenericSignature>>
        get() = listOf(
            P256Signature("p2sigr37wjbaL4XCywZcB3YXdGf7Jz8ibYkTK6VjHXQrPyV98w5Y6S3XJMfV7fdrMfvwqwy2Pryf3eHztYaxCAXUivBupwUEDK") to GenericSignature("sigriu6eW2EbbAX1JqvKwzqmCKvbUVLTL36eisu7xR6m2rbPokn3zS55V4pUhL8EjNXKwWNnjii9LgX9u2Ta5HHeVZvj6kjP"),
        )
}