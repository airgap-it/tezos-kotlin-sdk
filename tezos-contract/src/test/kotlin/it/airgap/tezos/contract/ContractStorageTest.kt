package it.airgap.tezos.contract

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.michelson.internal.converter.MichelineToCompactStringConverter
import it.airgap.tezos.michelson.internal.converter.MichelsonToMichelineConverter
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.GetContractNormalizedStorageResponse
import it.airgap.tezos.rpc.internal.cache.Cached
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ContractStorageTest {

    private lateinit var michelsonToMichelineConverter: MichelsonToMichelineConverter
    private lateinit var michelineToCompactStringConverter: MichelineToCompactStringConverter

    @MockK
    private lateinit var rpc: Block.Context.Contracts.Contract

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        michelsonToMichelineConverter = MichelsonToMichelineConverter()
        michelineToCompactStringConverter = MichelineToCompactStringConverter()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should create Map from storage values`() {
        storageWithMap.forEach { (type, value, expected) ->
            coEvery { rpc.storage.normalized.post(any(), any()) } returns GetContractNormalizedStorageResponse(value)

            val contractStorage = ContractStorage(
                Cached { MetaContractStorage(type, michelineToCompactStringConverter) },
                rpc,
            )

            val actual = runBlocking { contractStorage.get() }

            assertEquals(expected, actual)
        }
    }

    private val storageWithMap: List<StorageTestCase>
        get() = listOf(
            StorageTestCase {
                val type = micheline(michelsonToMichelineConverter) {
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

                val value = micheline(michelsonToMichelineConverter) {
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
                    type,
                    setOf(),
                    listOf(
                        ContractStorageEntry.BigMap(
                            "51296",
                            type.args[0],
                            setOf("%ledger"),
                        ),
                        ContractStorageEntry.Object(
                            value.args[1],
                            type.args[1],
                            setOf(),
                            listOf(
                                ContractStorageEntry.BigMap(
                                    "51297",
                                    type.args[1].args[0],
                                    setOf("%approvals"),
                                ),
                                ContractStorageEntry.Object(
                                    value.args[1].args[1],
                                    type.args[1].args[1],
                                    setOf("%fields"),
                                    listOf(
                                        ContractStorageEntry.Value(
                                            value.args[1].args[1].args[0],
                                            type.args[1].args[1].args[0],
                                            setOf("%admin")
                                        ),
                                        ContractStorageEntry.Object(
                                            value.args[1].args[1].args[1],
                                            type.args[1].args[1].args[1],
                                            setOf(),
                                            listOf(
                                                ContractStorageEntry.Value(
                                                    value.args[1].args[1].args[1].args[0],
                                                    type.args[1].args[1].args[1].args[0],
                                                    setOf("%paused"),
                                                ),
                                                ContractStorageEntry.Value(
                                                    value.args[1].args[1].args[1].args[1],
                                                    type.args[1].args[1].args[1].args[1],
                                                    setOf("%totalSupply"),
                                                ),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                )

                Triple(type, value, entry)
            }
        )

    private data class StorageTestCase(
        val type: MichelineNode,
        val value: MichelineNode,
        val entry: ContractStorageEntry,
    )

    private fun StorageTestCase(builder: () -> Triple<MichelineNode, MichelineNode, ContractStorageEntry>): StorageTestCase {
        val (type, value, entry) = builder()
        return StorageTestCase(type, value, entry)
    }

    private val MichelineNode.args: List<MichelineNode>
        get() = if (this is MichelinePrimitiveApplication) args else emptyList()
}