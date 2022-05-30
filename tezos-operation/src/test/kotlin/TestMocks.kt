import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.CoreModule
import it.airgap.tezos.core.internal.TezosCoreModule
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.padStartEven
import it.airgap.tezos.core.internal.utils.splitAt
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.michelson.internal.MichelsonModule
import it.airgap.tezos.michelson.internal.TezosMichelsonModule
import it.airgap.tezos.operation.internal.OperationModule
import it.airgap.tezos.operation.internal.TezosOperationModule
import org.bouncycastle.crypto.digests.Blake2bDigest
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.params.*
import org.bouncycastle.crypto.signers.ECDSASigner
import org.bouncycastle.crypto.signers.Ed25519Signer
import org.bouncycastle.crypto.signers.HMacDSAKCalculator
import org.bouncycastle.jce.ECNamedCurveTable
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.min

// -- Tezos --

internal fun mockTezos(cryptoProvider: CryptoProvider? = null): Tezos =
    mockkClass(Tezos::class).also {
        val cryptoProvider = cryptoProvider ?: mockk<CryptoProvider>(relaxed = true).also {
            every { it.sha256(any()) } answers {
                val messageDigest = MessageDigest.getInstance("SHA-256")
                messageDigest.digest(firstArg())
            }

            every { it.blake2b(any(), any()) } answers {
                val message = firstArg<ByteArray>()
                val size = secondArg<Int>()
                val blake2bDigest = Blake2bDigest(min(size * 8, 512))

                ByteArray(blake2bDigest.digestSize).let {
                    blake2bDigest.update(message, 0, message.size)
                    blake2bDigest.doFinal(it, 0)

                    it.sliceArray(0 until size)
                }
            }

            every { it.signEd25519(any(), any()) } answers {
                val message = firstArg<ByteArray>()
                val key = secondArg<ByteArray>()

                val signer = Ed25519Signer().apply { init(true, Ed25519PrivateKeyParameters(key.sliceArray(0 until 32))) }
                val signature = signer.run {
                    update(message, 0, message.size)
                    generateSignature()
                }

                signature
            }

            every { it.verifyEd25519(any(), any(), any()) } answers {
                val message = firstArg<ByteArray>()
                val signature = secondArg<ByteArray>()
                val key = thirdArg<ByteArray>()

                val signer = Ed25519Signer().apply { init(false, Ed25519PublicKeyParameters(key)) }
                val verified = signer.run {
                    update(message, 0, message.size)
                    verifySignature(signature)
                }

                verified
            }

            every { it.signSecp256K1(any(), any()) } answers {
                val message = firstArg<ByteArray>()
                val key = secondArg<ByteArray>()

                val spec = ECNamedCurveTable.getParameterSpec("secp256K1")
                val domainParameters = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
                val d = BigInteger(key)

                val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest())).apply {
                    init(true, ECPrivateKeyParameters(d, domainParameters))
                }
                val signature = signer.generateSignature(message).let { (r, s) ->
                    val s = if (s <= spec.n.shiftRight(1)) s else spec.n - s
                    r.toString(16) + s.toString(16)
                }

                signature.asHexString().toByteArray()
            }

            every { it.verifySecp256K1(any(), any(), any()) } answers {
                val message = firstArg<ByteArray>()
                val signature = secondArg<ByteArray>()
                val key = thirdArg<ByteArray>()

                val spec = ECNamedCurveTable.getParameterSpec("secp256K1")
                val domainParameters = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
                val q = spec.curve.decodePoint(key)

                val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest())).apply {
                    init(false, ECPublicKeyParameters(q, domainParameters))
                }
                val (r, s) = signature.splitAt(32).let { (r, s) ->
                    r.toHexString().asString() to s.toHexString().asString()
                }
                val verified = signer.verifySignature(message, BigInteger(r, 16), BigInteger(s, 16))

                verified
            }

            every { it.signP256(any(), any()) } answers {
                val message = firstArg<ByteArray>()
                val key = secondArg<ByteArray>()

                val spec = ECNamedCurveTable.getParameterSpec("secp256r1")
                val domainParameters = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
                val d = BigInteger(key)

                val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest())).apply {
                    init(true, ECPrivateKeyParameters(d, domainParameters))
                }
                val signature = signer.generateSignature(message).let { (r, s) ->
                    val s = if (s <= spec.n.shiftRight(1)) s else spec.n - s
                    r.toString(16).padStartEven('0') + s.toString(16).padStartEven('0')
                }

                signature.asHexString().toByteArray()
            }

            every { it.verifyP256(any(), any(), any()) } answers {
                val message = firstArg<ByteArray>()
                val signature = secondArg<ByteArray>()
                val key = thirdArg<ByteArray>()

                val spec = ECNamedCurveTable.getParameterSpec("secp256r1")
                val domainParameters = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
                val q = spec.curve.decodePoint(key)

                val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest())).apply {
                    init(false, ECPublicKeyParameters(q, domainParameters))
                }
                val (r, s) = signature.splitAt(32).let { (r, s) ->
                    r.toHexString().asString() to s.toHexString().asString()
                }
                val verified = signer.verifySignature(message, BigInteger(r, 16), BigInteger(s, 16))

                verified
            }
        }

        val dependencyRegistry = DependencyRegistry(cryptoProvider)
        val moduleRegistry = ModuleRegistry(
            builders = mapOf(
                TezosCoreModule::class to CoreModule,
                TezosMichelsonModule::class to MichelsonModule,
                TezosOperationModule::class to OperationModule,
            ),
        )

        every { it.dependencyRegistry } returns dependencyRegistry
        every { it.moduleRegistry } returns moduleRegistry
    }