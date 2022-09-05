@file:Suppress("UNUSED_VARIABLE")

import _utils.SamplesBase
import _utils.TEZOS_NODE
import _utils.printlnOnce
import _utils.url
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import it.airgap.tezos.http.ktor.KtorHttpClientProvider
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.signer.signWith
import it.airgap.tezos.operation.signer.verifyWith
import it.airgap.tezos.rpc.RpcModule
import it.airgap.tezos.rpc.TezosRpc
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.http.HttpParameter
import kotlinx.coroutines.runBlocking
import org.junit.Test

class BasicSamples : SamplesBase() {

    @Test
    fun explicit() {
        val default = Tezos {
            isDefault = true // the newly created instance will be registered as `Tezos.Default`
            cryptoProvider = BouncyCastleCryptoProvider()
        }

        val other = Tezos {
            /* isDefault = false */ // can be omitted, it is `false` by default
            cryptoProvider = CustomCryptoProvider()
        }

        val operation = Operation(branch = BlockHash("BMdhifZkcb5i9D6FnBi19SSBjft3sYaeKDAsEBgbsRLPTihQQJU"))

        val ed25519KeyPair = Pair(
            Ed25519SecretKey("edskRv7VyXGVZb8EsrR7D9XKUbbAQNQGtALP6QeB16ZCD7SmmJpzyeneJVg3Mq56YLbxRA1kSdAXiswwPiaVfR3NHGMCXCziuZ"),
            Ed25519PublicKey("edpkttZKC51wemRqL2QxwpMnEKxWnbd35pq47Y6xsCHp5M1f7LN8NP"),
        )

        // If the `tezos` parameter is omitted, `Tezos.Default` will be used.
        operation.signWith(ed25519KeyPair.first, /* tezos = Tezos.Default (== default) */).also { it.verifyWith(ed25519KeyPair.second, /* tezos = Tezos.Default (== default) */) }

        // `other` will be used as the Tezos context provider.
        operation.signWith(ed25519KeyPair.first, tezos = other).also { it.verifyWith(ed25519KeyPair.second, tezos = other) }
    }

    @Test
    fun implicit() {
        val operation = Operation(branch = BlockHash("BMdhifZkcb5i9D6FnBi19SSBjft3sYaeKDAsEBgbsRLPTihQQJU"))

        val ed25519KeyPair = Pair(
            Ed25519SecretKey("edskRv7VyXGVZb8EsrR7D9XKUbbAQNQGtALP6QeB16ZCD7SmmJpzyeneJVg3Mq56YLbxRA1kSdAXiswwPiaVfR3NHGMCXCziuZ"),
            Ed25519PublicKey("edpkttZKC51wemRqL2QxwpMnEKxWnbd35pq47Y6xsCHp5M1f7LN8NP"),
        )

        //
        // The `tezos` parameter can be omitted, `Tezos.Default` will be used.
        //
        // If no `Tezos` instance has been registered as default, a default instance will be constructed with providers dynamically loaded from dependencies at runtime.
        // The method call will result in an error if the expected dependencies cannot be found in the project.
        //
        // WARNING: The Tezos Kotlin SDK uses `java.util.ServiceLoader` to load the providers. It is recommended that you only use this feature for testing
        // or in a development environment. Production code should always register its providers explicitly to ensure the correct implementation is being used.
        //
        operation.signWith(ed25519KeyPair.first).also { it.verifyWith(ed25519KeyPair.second) }
    }

    @Test
    fun configuration() {
        val tezos = Tezos {
            cryptoProvider = CustomCryptoProvider()
            use(RpcModule) {
                httpClientProvider = CustomHttpClientProvider()
            }
        }

        val tezosRpc = TezosRpc(TEZOS_NODE, tezos)
        val branch = runBlocking { tezosRpc.getBlock().block.hash }

        val operation = Operation(branch = branch)

        val ed25519KeyPair = Pair(
            Ed25519SecretKey("edskRv7VyXGVZb8EsrR7D9XKUbbAQNQGtALP6QeB16ZCD7SmmJpzyeneJVg3Mq56YLbxRA1kSdAXiswwPiaVfR3NHGMCXCziuZ"),
            Ed25519PublicKey("edpkttZKC51wemRqL2QxwpMnEKxWnbd35pq47Y6xsCHp5M1f7LN8NP"),
        )

        operation.signWith(ed25519KeyPair.first, tezos).also { it.verifyWith(ed25519KeyPair.second, tezos) }

        val secp256K1KeyPair = Pair(
            Secp256K1SecretKey("spsk1SsrWCpufeXkNruaG9L3Mf9dRyd4D8HsM8ftqseN1fne3x9LNk"),
            Secp256K1PublicKey("sppk7ZpH5qAjTDZn1o1TW7z2QbQZUcMHRn2wtV4rRfz15eLQrvPkt6k"),
        )

        operation.signWith(secp256K1KeyPair.first, tezos).also { it.verifyWith(secp256K1KeyPair.second, tezos) }

        val p256KeyPair = Pair(
            P256SecretKey("p2sk2rVhhi5EfEdhJ3wQGsdc4ZEN3i7Z8f73Bn1xp1JKjETNyJ85oW"),
            P256PublicKey("p2pk67fo5oy6byruqDtzVixbM7L3cVBDRMcFhA33XD5w2HF4fRXDJhw"),
        )

        operation.signWith(p256KeyPair.first, tezos).also { it.verifyWith(p256KeyPair.second, tezos) }
    }

    class CustomCryptoProvider : CryptoProvider {
        private val delegateCryptoProvider: CryptoProvider = BouncyCastleCryptoProvider()

        override fun sha256(message: ByteArray): ByteArray {
            printlnOnce("CustomCryptoProvider: sha256")
            return delegateCryptoProvider.sha256(message)
        }

        override fun blake2b(message: ByteArray, size: Int): ByteArray {
            printlnOnce("CustomCryptoProvider: blake2b")
            return delegateCryptoProvider.blake2b(message, size)
        }

        override fun signEd25519(message: ByteArray, secretKey: ByteArray): ByteArray {
            printlnOnce("CustomCryptoProvider: signEd25519")
            return delegateCryptoProvider.signEd25519(message, secretKey)
        }

        override fun verifyEd25519(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean {
            printlnOnce("CustomCryptoProvider: verifyEd25519")
            return delegateCryptoProvider.verifyEd25519(message, signature, publicKey)
        }

        override fun signSecp256K1(message: ByteArray, secretKey: ByteArray): ByteArray {
            printlnOnce("CustomCryptoProvider: signSecp256K1")
            return delegateCryptoProvider.signSecp256K1(message, secretKey)
        }

        override fun verifySecp256K1(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean {
            printlnOnce("CustomCryptoProvider: verifySecp256K1")
            return delegateCryptoProvider.verifySecp256K1(message, signature, publicKey)
        }

        override fun signP256(message: ByteArray, secretKey: ByteArray): ByteArray {
            printlnOnce("CustomCryptoProvider: signP256")
            return delegateCryptoProvider.signP256(message, secretKey)
        }

        override fun verifyP256(message: ByteArray, signature: ByteArray, publicKey: ByteArray): Boolean {
            printlnOnce("CustomCryptoProvider: verifyP256")
            return delegateCryptoProvider.verifyP256(message, signature, publicKey)
        }
    }

    class CustomHttpClientProvider : HttpClientProvider {
        private val delegateHttpClientProvider: HttpClientProvider = KtorHttpClientProvider()

        override suspend fun delete(
            baseUrl: String,
            endpoint: String,
            headers: List<HttpHeader>,
            parameters: List<HttpParameter>,
        ): String {
            printlnOnce("CustomHttpClientProvider: DELETE ${url(baseUrl, endpoint)}")
            return delegateHttpClientProvider.delete(baseUrl, endpoint, headers, parameters)
        }

        override suspend fun get(
            baseUrl: String,
            endpoint: String,
            headers: List<HttpHeader>,
            parameters: List<HttpParameter>,
        ): String {
            printlnOnce("CustomHttpClientProvider: GET ${url(baseUrl, endpoint)}")
            return delegateHttpClientProvider.get(baseUrl, endpoint, headers, parameters)
        }

        override suspend fun patch(
            baseUrl: String,
            endpoint: String,
            headers: List<HttpHeader>,
            parameters: List<HttpParameter>,
            body: String?,
        ): String {
            printlnOnce("CustomHttpClientProvider: PATH ${url(baseUrl, endpoint)}")
            return delegateHttpClientProvider.patch(baseUrl, endpoint, headers, parameters, body)
        }

        override suspend fun post(
            baseUrl: String,
            endpoint: String,
            headers: List<HttpHeader>,
            parameters: List<HttpParameter>,
            body: String?,
        ): String {
            printlnOnce("CustomHttpClientProvider: POST ${url(baseUrl, endpoint)}")
            return delegateHttpClientProvider.post(baseUrl, endpoint, headers, parameters, body)
        }

        override suspend fun put(
            baseUrl: String,
            endpoint: String,
            headers: List<HttpHeader>,
            parameters: List<HttpParameter>,
            body: String?,
        ): String {
            printlnOnce("CustomHttpClientProvider: PUT ${url(baseUrl, endpoint)}")
            return delegateHttpClientProvider.put(baseUrl, endpoint, headers, parameters, body)
        }

    }
}