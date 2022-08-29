@file:Suppress("UNUSED_VARIABLE")

import _utils.TEZOS_NODE
import it.airgap.tezos.contract.Contract
import it.airgap.tezos.contract.converter.bigMapEntry
import it.airgap.tezos.contract.converter.objectEntry
import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.core.converter.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.number.TezosNatural
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import it.airgap.tezos.http.ktor.KtorHttpClientProvider
import it.airgap.tezos.http.ktor.KtorLogger
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.coder.fromJsonString
import it.airgap.tezos.michelson.converter.toMichelson
import it.airgap.tezos.michelson.converter.tryAs
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.rpc.RpcModule
import it.airgap.tezos.rpc.TezosRpc
import it.airgap.tezos.rpc.type.limits.OperationLimits
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ContractSamples {

    @Test
    fun create() {
        Tezos {
            isDefault = true
            cryptoProvider = BouncyCastleCryptoProvider()
        }

        val tezosDomains = Contract(TEZOS_NODE, ContractHash("KT1GFYUFQRT4RsNbtG2NU23woUyMp5tx9gx2"))
    }

    @Test
    fun getStorage() {
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

        val tezosDomains = Contract(TEZOS_NODE, ContractHash("KT1VmuKWcqSJ35RwPigAhLvu43Y11jED37fy"))
        val storage = runBlocking { tezosDomains.storage.get() }

        assertTrue(storage is ContractStorageEntry.Object)

        val actions = storage["%actions"]
        assertTrue(actions is ContractStorageEntry.BigMap)
        assertEquals("17073", actions.id)

        val store = storage["%store"]
        assertTrue(store is ContractStorageEntry.Object)

        val records = store["%records"]
        assertTrue(records is ContractStorageEntry.BigMap)
        assertEquals("17077", records.id)

        val reverseRecords = store["%reverse_records"]
        assertTrue(reverseRecords is ContractStorageEntry.BigMap)
        assertEquals("17078", reverseRecords.id)
    }

    @Test
    fun getValueFromBigMap() {
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

        val tezosDomains = Contract(TEZOS_NODE, ContractHash("KT1VmuKWcqSJ35RwPigAhLvu43Y11jED37fy"))
        val storage = runBlocking { tezosDomains.storage.get() }

        val store = storage.objectEntry["%store"]

        val record = runBlocking { store.objectEntry["%records"].bigMapEntry.get { bytes("aabbcc.jak".encodeToByteArray()) } }
        val address = record.objectEntry["%address"]?.value?.toMichelson()
            .tryAs<MichelsonData.Some>().value
            .tryAs<MichelsonData.StringConstant>()

        assertEquals("tz1Yj8M1R1VnDr8NsKRsJckgWxe1gj8QqZNS", address.value)

        val reverseRecord = runBlocking { store.objectEntry["%reverse_records"].bigMapEntry.get { string("tz1Yj8M1R1VnDr8NsKRsJckgWxe1gj8QqZNS") } }
        val name = reverseRecord.objectEntry["%name"]?.value?.toMichelson()
            .tryAs<MichelsonData.Some>().value
            .tryAs<MichelsonData.ByteSequenceConstant>()

        assertEquals("aabbcc.jak", name.toByteArray().decodeToString())
    }

    @Test
    fun callEntrypoint() {
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

        val tezosDomains = Contract(TEZOS_NODE, ContractHash("KT1VmuKWcqSJ35RwPigAhLvu43Y11jED37fy"))
        val transfer = tezosDomains.entrypoint("transfer")
        val operation = runBlocking {
            transfer(
                source = ImplicitAddress("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"),
                // The `fee` and `limits` values can be omitted.
                // If **both** omitted, the entrypoint handler will estimate the minimum fee for the resulting operation.
                fee = Mutez(505),
                limits = OperationLimits(
                    gas = "1521",
                    storage = "100",
                ),
            ) {
                sequence {
                    `object` {
                        value("%from_") { string("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e") }
                        sequence("%txs") {
                            `object` {
                                value("%to_") { string("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e") }
                                value("%token_id") { int(0) }
                                value("%amount") { int(100) }
                            }
                        }
                    }
                }
            }
        }

        val tezosRpc = TezosRpc(TEZOS_NODE)
        val branch = runBlocking { tezosRpc.getBlock().block.hash }
        val counter = runBlocking { tezosRpc.getCounter(contractId = Address("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")).counter!! }

        assertEquals(
            Operation.Unsigned(
                branch = branch,
                contents = listOf(
                    OperationContent.Transaction(
                        source = ImplicitAddress("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"),
                        fee = Mutez(505),
                        counter = TezosNatural(counter),
                        gasLimit = TezosNatural(1521U),
                        storageLimit = TezosNatural(100U),
                        amount = Mutez(0),
                        destination = ContractHash("KT1VmuKWcqSJ35RwPigAhLvu43Y11jED37fy"),
                        parameters = Parameters(
                            entrypoint = Entrypoint("transfer"),
                            value = Micheline.fromJsonString("""
                                [
                                    {
                                        "prim": "Pair",
                                        "args": [
                                            {
                                                "string": "tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"
                                            },
                                            [
                                                {
                                                    "prim": "Pair",
                                                    "args": [
                                                        {
                                                            "string": "tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"
                                                        },
                                                        {
                                                            "prim": "Pair",
                                                            "args": [
                                                                {
                                                                    "int": "0"
                                                                },
                                                                {
                                                                    "int": "100"
                                                                }
                                                            ]
                                                        }
                                                    ]
                                                }
                                            ]
                                        ]
                                    }
                                ]
                            """.trimIndent())
                        ),
                    ),
                ),
            ),
            operation,
        )
    }
}