package it.airgap.tezos.contract.entrypoint

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.contract.internal.converter.EntrypointArgumentToMichelineConverter
import it.airgap.tezos.contract.internal.entrypoint.MetaContractEntrypoint
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.zarith.ZarithNatural
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.internal.converter.MichelineToCompactStringConverter
import it.airgap.tezos.michelson.internal.converter.MichelsonToMichelineConverter
import it.airgap.tezos.michelson.internal.converter.StringToMichelsonPrimConverter
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.rpc.TezosRpc
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.internal.cache.Cached
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ContractEntrypointTest {

    private lateinit var michelsonToMichelineConverter: MichelsonToMichelineConverter
    private lateinit var michelineToCompactStringConverter: MichelineToCompactStringConverter
    private lateinit var stringToMichelinePrimConverter: StringToMichelsonPrimConverter

    private lateinit var entrypointArgumentToMichelineConverter: EntrypointArgumentToMichelineConverter

    @MockK
    private lateinit var blockRpc: Block

    @MockK
    private lateinit var tezosRpc: TezosRpc

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        michelsonToMichelineConverter = MichelsonToMichelineConverter()

        michelineToCompactStringConverter = MichelineToCompactStringConverter()
        stringToMichelinePrimConverter = StringToMichelsonPrimConverter()

        entrypointArgumentToMichelineConverter = EntrypointArgumentToMichelineConverter(michelineToCompactStringConverter, stringToMichelinePrimConverter)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should create unsigned operation with raw arguments`() {
        val source = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")
        val branch = BlockHash("BLkKavdWXZdfEXmup3FQKCrPJXTrVGfrbLhgBE3UU3SvyztZHHh")
        val counter = "1"

        coEvery { blockRpc.header.get(any()).header.hash } returns branch
        coEvery { blockRpc.context.contracts(any()).counter.get(any()).counter } returns counter
        coEvery { tezosRpc.minFee(any<String>(), any(), any(), any()) } answers { secondArg() }

        entrypointTestCases.forEach { (name, contractAddress, type, value, _) ->
            val contractEntrypoint = ContractEntrypoint(
                name,
                contractAddress,
                blockRpc,
                tezosRpc,
                Cached { MetaContractEntrypoint(type, entrypointArgumentToMichelineConverter) },
            )

            val actual = runBlocking { contractEntrypoint.call(value, source) }
            val expected = Operation.Unsigned(
                branch,
                listOf(
                    OperationContent.Transaction(
                        source = source,
                        fee = Mutez(0U),
                        counter = ZarithNatural(counter),
                        gasLimit = ZarithNatural(0U),
                        storageLimit = ZarithNatural(0U),
                        amount = Mutez(0U),
                        destination = contractAddress,
                        parameters = Parameters(
                            Entrypoint.fromString(name),
                            value,
                        ),
                    )
                )
            )

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should create unsigned operation with named arguments`() {
        val source = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")
        val branch = BlockHash("BLkKavdWXZdfEXmup3FQKCrPJXTrVGfrbLhgBE3UU3SvyztZHHh")
        val counter = "1"

        coEvery { blockRpc.header.get(any()).header.hash } returns branch
        coEvery { blockRpc.context.contracts(any()).counter.get(any()).counter } returns counter
        coEvery { tezosRpc.minFee(any<String>(), any(), any(), any()) } answers { secondArg() }

        entrypointTestCases.forEach { (name, contractAddress, type, value, namedValue) ->
            val contractEntrypoint = ContractEntrypoint(
                name,
                contractAddress,
                blockRpc,
                tezosRpc,
                Cached { MetaContractEntrypoint(type, entrypointArgumentToMichelineConverter) },
            )

            val actual = runBlocking { contractEntrypoint.call(namedValue, source) }
            val expected = Operation.Unsigned(
                branch,
                listOf(
                    OperationContent.Transaction(
                        source = source,
                        fee = Mutez(0U),
                        counter = ZarithNatural(counter),
                        gasLimit = ZarithNatural(0U),
                        storageLimit = ZarithNatural(0U),
                        amount = Mutez(0U),
                        destination = contractAddress,
                        parameters = Parameters(
                            Entrypoint.fromString(name),
                            value,
                        ),
                    )
                )
            )

            assertEquals(expected, actual)
        }
    }

    private val entrypointTestCases: List<EntrypointTestCase>
        get() = listOf(
            EntrypointTestCase(
                name = "test_entrypoint1",
                contractAddress = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc"),
                type = micheline(michelsonToMichelineConverter) {
                    pair {
                        arg {
                            pair {
                                arg {
                                    option {
                                        arg { address }
                                        annots("%address")
                                    }
                                }
                                arg {
                                    pair {
                                        arg { bytes annots "%label" }
                                        arg { address annots "%owner" }
                                    }
                                }
                            }
                        }
                        arg {
                            option {
                                arg {
                                    pair {
                                        arg { bytes annots "%parent" }
                                        arg { nat annots "%ttl" }
                                    }
                                }
                            }
                        }
                    }
                },
                value = micheline(michelsonToMichelineConverter) {
                    Pair {
                        arg {
                            Pair {
                                arg {
                                    Some {
                                        arg { string("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb") }
                                    }
                                }
                                arg {
                                    Pair {
                                        arg { bytes("0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c") }
                                        arg { string("tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq") }
                                    }
                                }
                            }
                        }
                        arg {
                            Some {
                                arg {
                                    Pair {
                                        arg { bytes("0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87") }
                                        arg { int(1) }
                                    }
                                }
                            }
                        }
                    }
                },
                namedValue = ContractEntrypointArgument.Object(
                    ContractEntrypointArgument.Value(MichelineLiteral.String("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb"), "%address"),
                    ContractEntrypointArgument.Value(MichelineLiteral.Bytes("0x0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c"), "%label"),
                    ContractEntrypointArgument.Value(MichelineLiteral.String("tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq"), "%owner"),
                    ContractEntrypointArgument.Value(MichelineLiteral.Bytes("0x0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87"), "%parent"),
                    ContractEntrypointArgument.Value(MichelineLiteral.Integer(1), "%ttl"),
                )
            ),
            EntrypointTestCase(
                name = "test_entrypoint2",
                contractAddress = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc"),
                type = micheline(michelsonToMichelineConverter) {
                    pair {
                        arg {
                            pair {
                                arg {
                                    pair {
                                        arg {
                                            option {
                                                arg { address }
                                                annots("%address")
                                            }
                                        }
                                        arg {
                                            map {
                                                key { string }
                                                value {
                                                    or {
                                                        lhs {
                                                            or {
                                                                lhs {
                                                                    or {
                                                                        lhs {
                                                                            or {
                                                                                lhs { address annots "%address" }
                                                                                rhs { bool annots "%bool" }
                                                                            }
                                                                        }
                                                                        rhs {
                                                                            or {
                                                                                lhs { bytes annots "%bytes" }
                                                                                rhs { int annots "%int" }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                rhs {
                                                                    or {
                                                                        lhs {
                                                                            or {
                                                                                lhs { key annots "%key" }
                                                                                rhs { keyHash annots "%key_hash" }
                                                                            }
                                                                        }
                                                                        rhs {
                                                                            or {
                                                                                lhs { nat annots "%nat" }
                                                                                rhs { signature annots "%signature" }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        rhs {
                                                            or {
                                                                lhs {
                                                                    or {
                                                                        lhs { string annots "%string" }
                                                                        rhs { mutez annots "%tez" }
                                                                    }
                                                                }
                                                                rhs { timestamp annots "%timestamp" }
                                                            }
                                                        }
                                                    }
                                                }
                                                annots("%data")
                                            }
                                        }
                                    }
                                }
                                arg {
                                    pair {
                                        arg { bytes annots "%label" }
                                        arg { address annots "%owner" }
                                    }
                                }
                            }
                        }
                        arg {
                            option {
                                arg {
                                    pair {
                                        arg { bytes annots "%parent" }
                                        arg { nat annots "%ttl" }
                                    }
                                }
                            }
                        }
                    }
                },
                value = micheline(michelsonToMichelineConverter) {
                    Pair {
                        arg {
                            Pair {
                                arg {
                                    Pair {
                                        arg{
                                            Some {
                                                arg { string("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb") }
                                            }
                                        }
                                        arg {
                                            sequence {
                                                Elt {
                                                    key { string("key1") }
                                                    value {
                                                        Left {
                                                            arg {
                                                                Left {
                                                                    arg {
                                                                        Left {
                                                                            arg {
                                                                                Right {
                                                                                    arg { True }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                Elt {
                                                    key { string("key2") }
                                                    value {
                                                        Left {
                                                            arg {
                                                                Right {
                                                                    arg {
                                                                        Right {
                                                                            arg {
                                                                                Left {
                                                                                    arg { int(1) }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                arg {
                                    Pair {
                                        arg { bytes("0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c") }
                                        arg { string("tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq") }
                                    }
                                }
                            }
                        }
                        arg {
                            Some {
                                arg {
                                    Pair {
                                        arg { bytes("0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87") }
                                        arg { int(2) }
                                    }
                                }
                            }
                        }
                    }
                },
                namedValue = ContractEntrypointArgument.Object(
                    ContractEntrypointArgument.Value(MichelineLiteral.String("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb"), "%address"),
                    ContractEntrypointArgument.Map(
                        ContractEntrypointArgument.Value(MichelineLiteral.String("key1")) to ContractEntrypointArgument.Value(MichelinePrimitiveApplication(MichelsonData.True), "%bool"),
                        ContractEntrypointArgument.Value(MichelineLiteral.String("key2")) to ContractEntrypointArgument.Value(MichelineLiteral.Integer(1), "%nat"),
                        name = "%data",
                    ),
                    ContractEntrypointArgument.Value(MichelineLiteral.Bytes("0x0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c"), "%label"),
                    ContractEntrypointArgument.Value(MichelineLiteral.String("tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq"), "%owner"),
                    ContractEntrypointArgument.Value(MichelineLiteral.Bytes("0x0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87"), "%parent"),
                    ContractEntrypointArgument.Value(MichelineLiteral.Integer(2), "%ttl"),
                )
            )
        )

    private data class EntrypointTestCase(
        val name: String,
        val contractAddress: ContractHash,
        val type: MichelineNode,
        val value: MichelineNode,
        val namedValue: ContractEntrypointArgument,
    )
}