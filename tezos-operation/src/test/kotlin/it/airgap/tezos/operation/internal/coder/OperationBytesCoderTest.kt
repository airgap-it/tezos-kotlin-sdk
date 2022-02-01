package it.airgap.tezos.operation.internal.coder

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.coder.*
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.operation.*
import it.airgap.tezos.operation.internal.converter.TagToOperationContentKindConverter
import it.airgap.tezos.operation.internal.di.ScopedDependencyRegistry
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OperationBytesCoderTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    @MockK
    private lateinit var crypto: Crypto

    private lateinit var operationBytesCoder: OperationBytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)

        val base58BytesCoder = Base58BytesCoder(base58Check)
        val keyHashBytesCoder = KeyHashBytesCoder(base58BytesCoder)
        val keyBytesCoder = KeyBytesCoder(base58BytesCoder)
        val signatureBytesCoder = SignatureBytesCoder(base58BytesCoder)
        val addressBytesCoder = AddressBytesCoder(keyHashBytesCoder, base58BytesCoder)
        val zarithNaturalNumberBytesCoder = ZarithNaturalNumberBytesCoder()

        val tagToOperationContentKindConverter = TagToOperationContentKindConverter()

        val operationContentBytesCoder = OperationContentBytesCoder(
            base58BytesCoder,
            addressBytesCoder,
            keyBytesCoder,
            keyHashBytesCoder,
            signatureBytesCoder,
            zarithNaturalNumberBytesCoder,
            tagToOperationContentKindConverter,
        )

        operationBytesCoder = OperationBytesCoder(operationContentBytesCoder, base58BytesCoder)

        every { dependencyRegistry.operationBytesCoder } returns operationBytesCoder
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode Operation to bytes`() {
        listOf(
            operationsWithBytes,
            listOf(
                Operation(
                    "BLyKu3tnc9NCuiFfCqfeVGPCoZTyW63dYh2XAYxkM7fQYKCqsju",
                    contents = emptyList(),
                    signature = "siga9NgTU8rCDsojPqDciCQi9nPDhYKTBrg1SjHsxRfMgJByWJr4SrXpUhQEjxiJpR7sVbQwHoo2mdMF1CdLGSyAXW6JTyst"
                ) to "a5db12a8a7716fa5445bd374c8b3239c876dde8397efae0eb0dd223dc23a51c7".asHexString().toByteArray(),
            ),
        ).flatten().forEach {
            assertContentEquals(it.second, operationBytesCoder.encode(it.first))
            assertContentEquals(it.second, it.first.forgeToBytes())
            assertContentEquals(it.second, it.first.forgeToBytes(operationBytesCoder))
            assertEquals(it.second.toHexString().asString(withPrefix = false), it.first.forgeToString(withHexPrefix = false))
            assertEquals(it.second.toHexString().asString(withPrefix = false), it.first.forgeToString(operationBytesCoder, withHexPrefix = false))
            assertEquals(it.second.toHexString().asString(withPrefix = true), it.first.forgeToString(withHexPrefix = true))
            assertEquals(it.second.toHexString().asString(withPrefix = true), it.first.forgeToString(operationBytesCoder, withHexPrefix = true))
        }
    }

    @Test
    fun `should decode Operation from bytes`() {
        operationsWithBytes.forEach {
            assertEquals(it.first, operationBytesCoder.decode(it.second))
            assertEquals(it.first, operationBytesCoder.decodeConsuming(it.second.toMutableList()))
            assertEquals(it.first, Operation.unforgeFromBytes(it.second))
            assertEquals(it.first, Operation.unforgeFromBytes(it.second, operationBytesCoder))
            assertEquals(it.first, Operation.unforgeFromString(it.second.toHexString().asString()))
            assertEquals(it.first, Operation.unforgeFromString(it.second.toHexString().asString(), operationBytesCoder))
        }
    }

    @Test
    fun `should fail to decode Operation from invalid bytes`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> {
                operationBytesCoder.decode(it)
            }

            assertFailsWith<IllegalArgumentException> {
                operationBytesCoder.decodeConsuming(it.toMutableList())
            }

            assertFailsWith<IllegalArgumentException> {
                Operation.unforgeFromBytes(it)
            }

            assertFailsWith<IllegalArgumentException> {
                Operation.unforgeFromBytes(it, operationBytesCoder)
            }

            assertFailsWith<IllegalArgumentException> {
                Operation.unforgeFromString(it.toHexString().asString())
            }

            assertFailsWith<IllegalArgumentException> {
                Operation.unforgeFromString(it.toHexString().asString(), operationBytesCoder)
            }
        }
    }

    private val operationsWithBytes: List<Pair<Operation, ByteArray>>
        get() = listOf(
            Operation(
                "BLyKu3tnc9NCuiFfCqfeVGPCoZTyW63dYh2XAYxkM7fQYKCqsju",
                contents = emptyList(),
            ) to "a5db12a8a7716fa5445bd374c8b3239c876dde8397efae0eb0dd223dc23a51c7".asHexString().toByteArray(),
            Operation(
                OperationContent.SeedNonceRevelation(1, "6cdaf9367e551995a670a5c642a9396290f8c9d17e6bc3c1555bfaa910d92214"),
                branch = "BLjg4HU2BwnCgJfRutxJX5rHACzLDxRJes1MXqbXXdxvHWdK3Te",
            ) to "86db32fcecf30277eef3ef9f397118ed067957dd998979fd723ea0a0d50beead01000000016cdaf9367e551995a670a5c642a9396290f8c9d17e6bc3c1555bfaa910d92214".asHexString().toByteArray(),
            Operation(
                OperationContent.SeedNonceRevelation(1, "9d15bcdc0194b327d3cb0dcd05242bc6ff1635da635e38ed7a62b8c413ce6833"),
                OperationContent.SeedNonceRevelation(2, "921ed0115c7cc1b5dcd07ad66ce4d9b2b0186c93c27a80d70b66b4e309add170"),
                branch = "BLjg4HU2BwnCgJfRutxJX5rHACzLDxRJes1MXqbXXdxvHWdK3Te",
            ) to "86db32fcecf30277eef3ef9f397118ed067957dd998979fd723ea0a0d50beead01000000019d15bcdc0194b327d3cb0dcd05242bc6ff1635da635e38ed7a62b8c413ce68330100000002921ed0115c7cc1b5dcd07ad66ce4d9b2b0186c93c27a80d70b66b4e309add170".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "a5db12a8a7716fa5445bd374c8b3239c876dde8397efae0eb0dd223dc23a51".asHexString().toByteArray(),
            "86db32fcecf30277eef3ef9f397118ed067957dd998979fd723ea0a0d50beead00000000016cdaf9367e551995a670a5c642a9396290f8c9d17e6bc3c1555bfaa910d92214".asHexString().toByteArray(),
        )
}