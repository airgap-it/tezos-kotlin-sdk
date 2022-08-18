package it.airgap.tezos.contract.storage

import io.mockk.*
import io.mockk.impl.annotations.MockK
import it.airgap.tezos.contract.internal.converter.MichelineToStorageEntryConverter
import it.airgap.tezos.contract.internal.storage.MetaContractStorage
import it.airgap.tezos.contract.internal.storage.MetaContractStorageEntry
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.GetContractNormalizedStorageResponse
import it.airgap.tezos.rpc.internal.cache.Cached
import kotlinx.coroutines.runBlocking
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ContractStorageTest {

    private lateinit var tezos: Tezos
    private lateinit var michelineToStorageEntryConverter: MichelineToStorageEntryConverter

    @MockK
    private lateinit var blockRpc: Block

    @MockK
    private lateinit var contractRpc: Block.Context.Contracts.Contract

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        michelineToStorageEntryConverter = MichelineToStorageEntryConverter(
            tezos.dependencyRegistry.crypto,
            blockRpc,
            tezos.coreModule.dependencyRegistry.encodedBytesCoder,
            tezos.michelsonModule.dependencyRegistry.michelinePacker,
            tezos.michelsonModule.dependencyRegistry.michelineToCompactStringConverter,
            tezos.michelsonModule.dependencyRegistry.stringToMichelsonPrimConverter,
            tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter,
            tezos.michelsonModule.dependencyRegistry.michelineNormalizer,
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should create ContractStorageEntry from storage values`() {
        every { blockRpc.context.bigMaps } returns mockk()

        storageTestCases.forEach { (type, value, expected) ->
            coEvery { contractRpc.storage.normalized.post(any(), any()) } returns GetContractNormalizedStorageResponse(value)

            val contractStorage = ContractStorage(
                Cached { MetaContractStorage(type, michelineToStorageEntryConverter) },
                contractRpc,
                tezos.michelsonModule.dependencyRegistry.michelineNormalizer,
            )

            val actual = runBlocking { contractStorage.get() }

            assertEquals(expected, actual)
        }
    }

    private val storageTestCases: List<StorageTestCase>
        get() = listOf(
            StorageTestCase {
                val type = micheline(tezos) {
                    pair {
                        arg {
                            bigMap {
                                key { address }
                                value { nat annots ":balance" }
                                annots("%ledger")
                            }
                        }
                        arg {
                            pair {
                                arg {
                                    bigMap {
                                        key {
                                            pair {
                                                arg { address annots ":owner" }
                                                arg { address annots ":spender" }
                                            }
                                        }
                                        value { nat }
                                        annots("%approvals")
                                    }
                                }
                                arg {
                                    pair {
                                        arg { address annots "%admin" }
                                        arg {
                                            pair {
                                                arg { bool annots "%paused" }
                                                arg { nat annots "%totalSupply" }
                                            }
                                        }
                                        annots("%fields")
                                    }
                                }
                            }
                        }

                    }
                }

                val value = micheline(tezos) {
                    Pair {
                        arg { int(51296) }
                        arg {
                            Pair {
                                arg { int(51297) }
                                arg {
                                    Pair {
                                        arg { bytes("0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c") }
                                        arg {
                                            Pair {
                                                arg { True }
                                                arg { int(20) }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                val entry = ContractStorageEntry.Object(
                    value,
                    MetaContractStorageEntry.Basic(type),
                    listOf(
                        ContractStorageEntry.BigMap(
                            "51296",
                            value.args[0],
                            MetaContractStorageEntry.BigMap(
                                type.args[0],
                                mockk(),
                                mockk(),
                                mockk(),
                                mockk(),
                                mockk(),
                                mockk(),
                            ),
                            mockk(),
                            mockk()
                        ),
                        ContractStorageEntry.BigMap(
                            "51297",
                            value.args[1].args[0],
                            MetaContractStorageEntry.BigMap(
                                type.args[1].args[0],
                                mockk(),
                                mockk(),
                                mockk(),
                                mockk(),
                                mockk(),
                                mockk(),
                            ),
                            mockk(),
                            mockk(),
                        ),
                        ContractStorageEntry.Object(
                            value.args[1].args[1],
                            MetaContractStorageEntry.Basic(type.args[1].args[1]),
                            listOf(
                                ContractStorageEntry.Value(
                                    value.args[1].args[1].args[0],
                                    MetaContractStorageEntry.Basic(type.args[1].args[1].args[0]),
                                ),
                                ContractStorageEntry.Value(
                                    value.args[1].args[1].args[1].args[0],
                                     MetaContractStorageEntry.Basic(type.args[1].args[1].args[1].args[0]),
                                ),
                                ContractStorageEntry.Value(
                                    value.args[1].args[1].args[1].args[1],
                                     MetaContractStorageEntry.Basic(type.args[1].args[1].args[1].args[1]),
                                ),
                            ),
                        ),
                    ),
                )

                Triple(type, value, entry)
            }
        )

    private data class StorageTestCase(
        val type: Micheline,
        val value: Micheline,
        val entry: ContractStorageEntry,
    )

    private fun StorageTestCase(builder: () -> Triple<Micheline, Micheline, ContractStorageEntry>): StorageTestCase {
        val (type, value, entry) = builder()
        return StorageTestCase(type, value, entry)
    }

    private val Micheline.args: List<Micheline>
        get() = if (this is MichelinePrimitiveApplication) args else emptyList()
}