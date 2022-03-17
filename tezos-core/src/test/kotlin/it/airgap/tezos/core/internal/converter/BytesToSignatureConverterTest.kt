package it.airgap.tezos.core.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.fromBytes
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.type.encoded.*
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BytesToSignatureConverterTest {

    @MockK
    private lateinit var crypto: Crypto

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var bytesToSignatureConverter: BytesToSignatureConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)
        bytesToSignatureConverter = BytesToSignatureConverter(base58Check)

        every { dependencyRegistry.bytesToSignatureConverter } returns bytesToSignatureConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert bytes to SignatureEncoded`() {
        signaturesWithBytes.forEach {
            assertEquals(it.first, bytesToSignatureConverter.convert(it.second).encoded)
            assertEquals(it.first, SignatureEncoded.fromBytes(it.second))
            assertEquals(it.first, SignatureEncoded.fromBytes(it.second, bytesToSignatureConverter))
        }
    }

    @Test
    fun `should fail to convert invalid bytes to SignatureEncoded`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { bytesToSignatureConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { SignatureEncoded.fromBytes(it) }
            assertFailsWith<IllegalArgumentException> { SignatureEncoded.fromBytes(it, bytesToSignatureConverter) }
        }
    }

    private val signaturesWithBytes: List<Pair<SignatureEncoded, ByteArray>>
        get() = listOf(
            Ed25519Signature("edsigu69u68mAZ4KhBDPWshZVCDhSgrzkc5Mgihxakzh7FceNgwdQqQYQM3pjN3b4gjrLVo5Dxre41mzCDt8JFtFytWLLnMmHfz") to "09f5cd8612f73faf982337ad4eb8cfd182e53751e7085ddb319842b6bffd1667ff5b00a9c80c243b16d47e94a5eb4941744f48d5bcf2d7cdb1269e545b0477955cd4021349".asHexString().toByteArray(),
            Secp256K1Signature("spsig1UdNgjqzaZxkbNyqVo3wm4Stoc4RXf6nybVGzANnKNHxTYBY8XGB2pgiVp5BYaYkLkmyaxMCq5xCsqCMDaFQWCeFBG2NNM") to "0d7365133fae67effa0c4429d50280b536259ddf1502b64cfde023d202947c16f7c6fbdd50e68e089be729f04fa51c8df1182adc9be9b2d1829013dd04d3b8971b51a99567".asHexString().toByteArray(),
            P256Signature("p2sign2yJ9dujTvBpkFXAeY8SWEAmtE2fCdVqcPvonSKp7phjDxX9QzENKXJxFZ9ZiEXWtcF13CuEmG8ZihkXiSUPtZVHniizN") to "36f02c34bd0a924744384ed45e0cfd833e2d36b7082fa5711b9eb31529fd4fa567b8b57279ac4c79debbbc6454d0b5b75ec84c3c2f57c5e21d14066de95df6f7032d2815".asHexString().toByteArray(),
            GenericSignature("sigorP5a3d9pUue323PENRLsrLzEMwTJfKudbjCTUBG7qAuELEt8nzm8FLC2kRwWfuUfFXfKu2PpfJPsT6RZcDA2AWnLTQQ9") to "04822bc5b0cc9d7f9d314b0ffd990e6f0b3d558ee9c0996ac2fe717b59304936e0a1f5fba90dce024854b95a5e091c702a6aca773ceebb8bdfb3655f2d75e02ab6439d".asHexString().toByteArray(),
            GenericSignature("sigorP5a3d9pUue323PENRLsrLzEMwTJfKudbjCTUBG7qAuELEt8nzm8FLC2kRwWfuUfFXfKu2PpfJPsT6RZcDA2AWnLTQQ9") to "c5b0cc9d7f9d314b0ffd990e6f0b3d558ee9c0996ac2fe717b59304936e0a1f5fba90dce024854b95a5e091c702a6aca773ceebb8bdfb3655f2d75e02ab6439d".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "09f5cd8612f73faf982337ad4eb8cfd182e53751e7085ddb319842b6bffd1667ff5b00a9c80c243b16d47e94a5eb4941744f48d5bcf2d7cdb1269e545b0477",
            "0d73d50280b536259ddf1502b64cfde023d202947c16f7c6fbdd50e68e089be729f04fa51c8df1182adc9be9b2d1829013dd04d3b8971b51a99567",
            "04822bc5b0cc9d7f9d314b0ffd990e6f0b3d558ee9c0996ac2fe717b59304936e0a1f5fba90dce024854b95a5e091c702a6aca773ceebb8bdfb3655f2d75e02ab6439d5fba73",
        ).map { it.asHexString().toByteArray() }
}