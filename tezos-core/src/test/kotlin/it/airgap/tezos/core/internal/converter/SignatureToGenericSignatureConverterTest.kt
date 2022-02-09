package it.airgap.tezos.core.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.core.toGenericSignature
import it.airgap.tezos.core.type.encoded.*
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals

class SignatureToGenericSignatureConverterTest {

    @MockK
    private lateinit var crypto: Crypto

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var signatureToGenericSignatureConverter: SignatureToGenericSignatureConverter

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

        signatureToGenericSignatureConverter = SignatureToGenericSignatureConverter(signatureBytesCoder, encodedBytesCoder)

        every { dependencyRegistry.signatureToGenericSignatureConverter } returns signatureToGenericSignatureConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert SignatureEncoded to GenericSignature`() {
        signaturesWithGenerics.forEach {
            assertEquals(it.second, signatureToGenericSignatureConverter.convert(it.first))
            assertEquals(it.second, it.second.toGenericSignature())
            assertEquals(it.second, it.second.toGenericSignature(signatureToGenericSignatureConverter))
        }
    }

    private val signaturesWithGenerics: List<Pair<SignatureEncoded<*>, GenericSignature>>
        get() = listOf(
            Ed25519Signature("edsigtczTq2EC9VQNRRT53gzcs25DJFg1iZeTzQxY7jBtjradZb8qqZaqzAYSbVWvg1abvqFpQCV8TgqotDwckJiTJ9fJ2eYESb") to GenericSignature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"),
            Secp256K1Signature("spsig1PptyKUAGumWN9qs9aW2MafGp8kXekDAzEPCpoJUUPjVzQwmKdNBti5CA3nMTVcUaM3dcS2JSQwUNGtbYvHbSeU5eTTK6Z") to GenericSignature("sigg1DeFruoZoMyyr7BRwshfytuJxxagaUWfFt419AfVonZMHx94HowabLTgDGQ6YcVdJUsUAg1GnkGzBV33c6XRvyAQ3tby"),
            P256Signature("p2sigr37wjbaL4XCywZcB3YXdGf7Jz8ibYkTK6VjHXQrPyV98w5Y6S3XJMfV7fdrMfvwqwy2Pryf3eHztYaxCAXUivBupwUEDK") to GenericSignature("sigriu6eW2EbbAX1JqvKwzqmCKvbUVLTL36eisu7xR6m2rbPokn3zS55V4pUhL8EjNXKwWNnjii9LgX9u2Ta5HHeVZvj6kjP"),
            GenericSignature("sigfi6Rm6nNgPCM1yWcRaPemg9iwHoB1UmJHtQR3tup8KHhb53YvBpsv2WTkqJUJCJQvk8HL7ybHnqJXY9s1gEVtWxcBVX8L") to GenericSignature("sigfi6Rm6nNgPCM1yWcRaPemg9iwHoB1UmJHtQR3tup8KHhb53YvBpsv2WTkqJUJCJQvk8HL7ybHnqJXY9s1gEVtWxcBVX8L"),
        )
}