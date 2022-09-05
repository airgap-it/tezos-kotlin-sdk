@file:Suppress("UNUSED_VARIABLE")

import _utils.TEZOS_NODE
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.core.converter.encoded.ImplicitAddress
import it.airgap.tezos.core.converter.encoded.Signature
import it.airgap.tezos.core.type.encoded.Ed25519PublicKey
import it.airgap.tezos.core.type.encoded.Ed25519SecretKey
import it.airgap.tezos.core.type.number.TezosNatural
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import it.airgap.tezos.http.ktor.KtorHttpClientProvider
import it.airgap.tezos.http.ktor.KtorLogger
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.coder.forgeToString
import it.airgap.tezos.operation.signer.sign
import it.airgap.tezos.operation.signer.signWith
import it.airgap.tezos.operation.signer.verify
import it.airgap.tezos.operation.signer.verifyWith
import it.airgap.tezos.rpc.RpcModule
import it.airgap.tezos.rpc.TezosRpc
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class OperationSamples {

    @Test
    fun create() {
        Tezos {
            isDefault = true
            cryptoProvider = BouncyCastleCryptoProvider()

            use(RpcModule) {
                httpClientProvider = KtorHttpClientProvider(object : KtorLogger() {
                    override fun log(message: String) {
                        println(message)
                    }
                })
            }
        }

        val tezosRpc = TezosRpc(TEZOS_NODE)

        val branch = runBlocking { tezosRpc.getBlock().block.hash }
        val counter = runBlocking { tezosRpc.getCounter(contractId = Address("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c")).counter!! }

        // Transfer 1000 mutez from tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c to tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy
        // with default fee (0) and limits (gas = 0, storage = 0).

        val unsigned = Operation(
            OperationContent.Transaction(
                source = ImplicitAddress("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"),
                counter = TezosNatural(counter),
                amount = Mutez(1000),
                destination = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"),
            ),
            branch = branch,
        )

        val signed = Operation(
            OperationContent.Transaction(
                source = ImplicitAddress("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"),
                counter = TezosNatural(counter),
                amount = Mutez(1000),
                destination = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"),
            ),
            branch = branch,
            signature = Signature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"),
        )
    }

    @Test
    fun sign() {
        Tezos {
            isDefault = true
            cryptoProvider = BouncyCastleCryptoProvider()

            use(RpcModule) {
                httpClientProvider = KtorHttpClientProvider(object : KtorLogger() {
                    override fun log(message: String) {
                        println(message)
                    }
                })
            }
        }

        val tezosRpc = TezosRpc(TEZOS_NODE)

        val branch = runBlocking { tezosRpc.getBlock().block.hash }
        val counter = runBlocking { tezosRpc.getCounter(contractId = Address("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c")).counter!! }

        val secretKey = Ed25519SecretKey("edskSA787kkcN7ZF9imyuArKxSYApt6YmQ2oNwKKH9PxFAux8fzrFmE6tzBecnbTRCW1jqN7S8crRoSbrczRxy3TwnycPCJKNr")
        val publicKey = Ed25519PublicKey("edpkvVEnevzUZS5x1MsyTpAzsa87rY7h9N7vFaQdeM2BmXcRmZxriH")

        // Transfer 1000 mutez from tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c to tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy
        // with default fee (0) and limits (gas = 0, storage = 0).

        val unsigned = Operation(
            OperationContent.Transaction(
                source = ImplicitAddress("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"),
                counter = TezosNatural(counter),
                amount = Mutez(1000),
                destination = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"),
            ),
            branch = branch,
        )

        val signed = unsigned.signWith(secretKey)
        val signature = secretKey.sign(unsigned)

        assertEquals(
            Operation.Signed(
                unsigned.branch,
                unsigned.contents,
                signature,
            ),
            signed,
        )

        assertTrue(signed.verifyWith(publicKey))
        assertTrue(publicKey.verify(signed))
    }

    @Test
    fun estimateFee() {
        assertFails { // expected to fail with `empty_implicit_contract` (the test account is empty)
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()

                use(RpcModule) {
                    httpClientProvider = KtorHttpClientProvider(object : KtorLogger() {
                        override fun log(message: String) {
                            println(message)
                        }
                    })
                }
            }

            val tezosRpc = TezosRpc(TEZOS_NODE)

            val branch = runBlocking { tezosRpc.getBlock().block.hash }
            val counter = runBlocking { tezosRpc.getCounter(contractId = Address("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c")).counter!! }

            // Transfer 1000 mutez from tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c to tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy
            // with default fee (0) and limits (gas = 0, storage = 0).

            val operation = Operation(
                OperationContent.Transaction(
                    source = ImplicitAddress("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"),
                    counter = TezosNatural(counter),
                    amount = Mutez(1000),
                    destination = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"),
                ),
                branch = branch,
            )

            // Estimate min fee for the operation
            val operationWithFee = runBlocking { tezosRpc.minFee(operation = operation) }
        }
    }

    @Test
    fun injectOperation() {
        assertFails { // expected to fail with `empty_implicit_contract` (the test account is empty)
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()

                use(RpcModule) {
                    httpClientProvider = KtorHttpClientProvider(object : KtorLogger() {
                        override fun log(message: String) {
                            println(message)
                        }
                    })
                }
            }

            val tezosRpc = TezosRpc(TEZOS_NODE)

            val branch = runBlocking { tezosRpc.getBlock().block.hash }
            val counter = runBlocking { tezosRpc.getCounter(contractId = Address("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c")).counter!! }

            // Transfer 1000 mutez from tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c to tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy
            // with default fee (0) and limits (gas = 0, storage = 0).

            val unsigned = Operation(
                OperationContent.Transaction(
                    source = ImplicitAddress("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"),
                    counter = TezosNatural(counter),
                    amount = Mutez(1000),
                    destination = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"),
                ),
                branch = branch,
            )

            // Estimate min fee for the operation
            val unsignedWithFee = runBlocking { tezosRpc.minFee(operation = unsigned) }

            val secretKey = Ed25519SecretKey("edskSA787kkcN7ZF9imyuArKxSYApt6YmQ2oNwKKH9PxFAux8fzrFmE6tzBecnbTRCW1jqN7S8crRoSbrczRxy3TwnycPCJKNr")
            val signedWithFee = unsignedWithFee.signWith(secretKey)

            val forged = signedWithFee.forgeToString()

            runBlocking { tezosRpc.injectOperation(forged) }
        }
    }
}