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

        val tezosDomains = Contract(TEZOS_NODE, ContractHash("KT1GFYUFQRT4RsNbtG2NU23woUyMp5tx9gx2"))
        val storage = runBlocking { tezosDomains.storage.get() }

        assertTrue(storage is ContractStorageEntry.Object)

        val actions = storage["%actions"]
        assertTrue(actions is ContractStorageEntry.BigMap)
        assertEquals("13806", actions.id)

        val store = storage["%store"]
        assertTrue(store is ContractStorageEntry.Object)

        val records = store["%records"]
        assertTrue(records is ContractStorageEntry.BigMap)
        assertEquals("13810", records.id)

        val reverseRecords = store["%reverse_records"]
        assertTrue(reverseRecords is ContractStorageEntry.BigMap)
        assertEquals("13811", reverseRecords.id)
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

        val tezosDomains = Contract(TEZOS_NODE, ContractHash("KT1GFYUFQRT4RsNbtG2NU23woUyMp5tx9gx2"))
        val storage = runBlocking { tezosDomains.storage.get() }

        val store = storage.objectEntry["%store"]

        val record = runBlocking { store.objectEntry["%records"].bigMapEntry.get { bytes("lab-void.ith".encodeToByteArray()) } }
        val address = record.objectEntry["%address"]?.value?.toMichelson()
            .tryAs<MichelsonData.Some>().value
            .tryAs<MichelsonData.StringConstant>()

        assertEquals("tz1UA6Neo7pu6qvUveZQq1ZWwV1YuNgoip4m", address.value)

        val reverseRecord = runBlocking { store.objectEntry["%reverse_records"].bigMapEntry.get { string("tz1UA6Neo7pu6qvUveZQq1ZWwV1YuNgoip4m") } }
        val name = reverseRecord.objectEntry["%name"]?.value?.toMichelson()
            .tryAs<MichelsonData.Some>().value
            .tryAs<MichelsonData.ByteSequenceConstant>()

        assertEquals("short-wing.ith", name.toByteArray().decodeToString())
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

        val tezosDomains = Contract(TEZOS_NODE, ContractHash("KT1GFYUFQRT4RsNbtG2NU23woUyMp5tx9gx2"))
        val transfer = tezosDomains.entrypoint("transfer")
        val operation = runBlocking {
            transfer(
                source = ImplicitAddress("tz1UA6Neo7pu6qvUveZQq1ZWwV1YuNgoip4m"),
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
                        value("%from_") { string("tz1UA6Neo7pu6qvUveZQq1ZWwV1YuNgoip4m") }
                        sequence("%txs") {
                            `object` {
                                value("%to_") { string("tz1UA6Neo7pu6qvUveZQq1ZWwV1YuNgoip4m") }
                                value("%token_id") { int(53) }
                                value("%amount") { int(100) }
                            }
                        }
                    }
                }
            }
        }

        val tezosRpc = TezosRpc(TEZOS_NODE)
        val branch = runBlocking { tezosRpc.getBlock().block.hash }
        val counter = runBlocking { tezosRpc.getCounter(contractId = Address("tz1UA6Neo7pu6qvUveZQq1ZWwV1YuNgoip4m")).counter!! }

        assertEquals(
            Operation.Unsigned(
                branch = branch,
                contents = listOf(
                    OperationContent.Transaction(
                        source = ImplicitAddress("tz1UA6Neo7pu6qvUveZQq1ZWwV1YuNgoip4m"),
                        fee = Mutez(505),
                        counter = TezosNatural(counter),
                        gasLimit = TezosNatural(1521U),
                        storageLimit = TezosNatural(100U),
                        amount = Mutez(0),
                        destination = ContractHash("KT1GFYUFQRT4RsNbtG2NU23woUyMp5tx9gx2"),
                        parameters = Parameters(
                            entrypoint = Entrypoint("transfer"),
                            value = Micheline.fromJsonString("""
                                [
                                    {
                                        "prim": "Pair",
                                        "args": [
                                            {
                                                "string": "tz1UA6Neo7pu6qvUveZQq1ZWwV1YuNgoip4m"
                                            },
                                            [
                                                {
                                                    "prim": "Pair",
                                                    "args": [
                                                        {
                                                            "string": "tz1UA6Neo7pu6qvUveZQq1ZWwV1YuNgoip4m"
                                                        },
                                                        {
                                                            "prim": "Pair",
                                                            "args": [
                                                                {
                                                                    "int": "53"
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