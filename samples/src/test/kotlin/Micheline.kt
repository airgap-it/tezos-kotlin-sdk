@file:Suppress("UNUSED_VARIABLE")

import _utils.SamplesBase
import _utils.toJson
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.coder.toJsonString
import it.airgap.tezos.michelson.converter.toMichelson
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline
import org.junit.Test
import kotlin.test.assertEquals

class MichelineSamples {

    class Usage : SamplesBase() {

        @Test
        fun create() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val intLiteral = micheline { int(10) }
            assertEquals("""
                {
                    "int": "10"
                }
            """.trimIndent().toJson(),
                intLiteral.toJsonString().toJson(),
            )

            val stringLiteral = micheline { string("string") }
            assertEquals("""
                {
                    "string": "string"
                }
            """.trimIndent().toJson(),
                stringLiteral.toJsonString().toJson(),
            )

            val bytesLiteral = micheline { bytes(byteArrayOf(10)) }
            assertEquals("""
                {
                    "bytes": "0a"
                }
            """.trimIndent().toJson(),
                bytesLiteral.toJsonString().toJson(),
            )

            val prim = micheline {
                pair {
                    arg {
                        option {
                            arg { nat annots "%nat" }
                        }
                    }
                    arg {
                        or {
                            lhs { unit }
                            rhs {
                                map {
                                    key { string }
                                    value {
                                        pair {
                                            arg { bytes annots "%bytes" }
                                            arg { address annots "%address" }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            assertEquals("""
                {
                    "prim": "pair",
                    "args": [
                        {
                            "prim": "option",
                            "args": [
                                {
                                    "prim": "nat",
                                    "annots": [
                                        "%nat"
                                    ]
                                }
                            ]
                        },
                        {
                            "prim": "or",
                            "args": [
                                {
                                    "prim": "unit"
                                },
                                {
                                    "prim": "map",
                                    "args": [
                                        {
                                            "prim": "string"
                                        },
                                        {
                                            "prim": "pair",
                                            "args": [
                                                {
                                                    "prim": "bytes",
                                                    "annots": [
                                                        "%bytes"
                                                    ]
                                                },
                                                {
                                                    "prim": "address",
                                                    "annots": [
                                                        "%address"
                                                    ]
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ] 
                        }
                    ]
                }
            """.trimIndent().toJson(),
                prim.toJsonString().toJson(),
            )

            val sequence = micheline {
                sequence {
                    int(1)
                    int(2)
                    int(3)
                }
            }
            assertEquals("""
                [
                    {
                        "int": "1"
                    },
                    {
                        "int": "2"
                    },
                    {
                        "int": "3"
                    }
                ]
            """.trimIndent().toJson(),
                bytesLiteral.toJsonString().toJson(),
            )
        }

        @Test
        fun toMichelson() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val intLiteral = micheline { int(10) }
            assertEquals(MichelsonData.IntConstant(10), intLiteral.toMichelson())

            val stringLiteral = micheline { string("string") }
            assertEquals(MichelsonData.StringConstant("string"), stringLiteral.toMichelson())

            val bytesLiteral = micheline { bytes(byteArrayOf(10)) }
            assertEquals(MichelsonData.ByteSequenceConstant("0x0a"), bytesLiteral.toMichelson())

            val prim = micheline {
                pair {
                    arg {
                        option {
                            arg { nat annots "%nat" }
                        }
                    }
                    arg {
                        or {
                            lhs { unit }
                            rhs {
                                map {
                                    key { string }
                                    value {
                                        pair {
                                            arg { bytes annots "%bytes" }
                                            arg { address annots "%address" }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            assertEquals(
                MichelsonType.Pair(
                    MichelsonComparableType.Option(
                        MichelsonComparableType.Nat(metadata = MichelsonType.Metadata(fieldName = Michelson.Annotation.Field("%nat")))
                    ),
                    MichelsonType.Or(
                        MichelsonComparableType.Unit(),
                        MichelsonType.Map(
                            MichelsonComparableType.String(),
                            MichelsonComparableType.Pair(
                                MichelsonComparableType.Bytes(metadata = MichelsonType.Metadata(fieldName = Michelson.Annotation.Field("%bytes"))),
                                MichelsonComparableType.Address(metadata = MichelsonType.Metadata(fieldName = Michelson.Annotation.Field("%address"))),
                            )
                        )
                    )
                ),
                prim.toMichelson(),
            )
        }
    }
}