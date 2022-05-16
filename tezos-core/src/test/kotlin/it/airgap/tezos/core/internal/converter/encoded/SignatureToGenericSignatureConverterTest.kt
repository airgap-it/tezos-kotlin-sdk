package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.toGenericSignature
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.type.encoded.*
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals

class SignatureToGenericSignatureConverterTest {

    @MockK
    private lateinit var cryptoProvider: CryptoProvider

    private lateinit var tezos: Tezos

    private lateinit var signatureToGenericSignatureConverter: SignatureToGenericSignatureConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { cryptoProvider.sha256(any()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        tezos = mockTezos(cryptoProvider)
        signatureToGenericSignatureConverter = SignatureToGenericSignatureConverter(
            tezos.core().dependencyRegistry.signatureBytesCoder,
            tezos.core().dependencyRegistry.encodedBytesCoder,
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert SignatureEncoded to GenericSignature`() {
        signaturesWithGenerics.forEach {
            assertEquals(it.second, signatureToGenericSignatureConverter.convert(it.first))
            assertEquals(it.second, it.second.toGenericSignature(tezos))
            assertEquals(it.second, it.second.toGenericSignature(signatureToGenericSignatureConverter))
        }
    }

    private val signaturesWithGenerics: List<Pair<Signature, GenericSignature>>
        get() = listOf(
            Ed25519Signature("edsigtczTq2EC9VQNRRT53gzcs25DJFg1iZeTzQxY7jBtjradZb8qqZaqzAYSbVWvg1abvqFpQCV8TgqotDwckJiTJ9fJ2eYESb") to GenericSignature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"),
            Secp256K1Signature("spsig1PptyKUAGumWN9qs9aW2MafGp8kXekDAzEPCpoJUUPjVzQwmKdNBti5CA3nMTVcUaM3dcS2JSQwUNGtbYvHbSeU5eTTK6Z") to GenericSignature("sigg1DeFruoZoMyyr7BRwshfytuJxxagaUWfFt419AfVonZMHx94HowabLTgDGQ6YcVdJUsUAg1GnkGzBV33c6XRvyAQ3tby"),
            P256Signature("p2sigr37wjbaL4XCywZcB3YXdGf7Jz8ibYkTK6VjHXQrPyV98w5Y6S3XJMfV7fdrMfvwqwy2Pryf3eHztYaxCAXUivBupwUEDK") to GenericSignature("sigriu6eW2EbbAX1JqvKwzqmCKvbUVLTL36eisu7xR6m2rbPokn3zS55V4pUhL8EjNXKwWNnjii9LgX9u2Ta5HHeVZvj6kjP"),
            GenericSignature("sigfi6Rm6nNgPCM1yWcRaPemg9iwHoB1UmJHtQR3tup8KHhb53YvBpsv2WTkqJUJCJQvk8HL7ybHnqJXY9s1gEVtWxcBVX8L") to GenericSignature("sigfi6Rm6nNgPCM1yWcRaPemg9iwHoB1UmJHtQR3tup8KHhb53YvBpsv2WTkqJUJCJQvk8HL7ybHnqJXY9s1gEVtWxcBVX8L"),
        )
}