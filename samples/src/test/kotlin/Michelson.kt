import _utils.toJson
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.coder.toJsonString
import it.airgap.tezos.michelson.converter.toMicheline
import org.junit.Test
import kotlin.test.assertEquals

class MichelsonSamples {
    class Usage {
        @Test
        fun toMicheline() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val intConstant = MichelsonData.IntConstant(10)
            assertEquals("""
                { 
                    "int": "10"
                }
            """.trimIndent().toJson(),
                intConstant.toMicheline().toJsonString().toJson(),
            )

            val stringConstant = MichelsonData.StringConstant("string")
            assertEquals("""
                { 
                    "string": "string"
                }
            """.trimIndent().toJson(),
                stringConstant.toMicheline().toJsonString().toJson(),
            )

            val bytesSequenceConstant = MichelsonData.ByteSequenceConstant("0x0a")
            assertEquals("""
                { 
                    "bytes": "0a"
                }
            """.trimIndent().toJson(),
                bytesSequenceConstant.toMicheline().toJsonString().toJson(),
            )

            val prim = MichelsonType.Pair(
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
            )
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
                prim.toMicheline().toJsonString().toJson(),
            )
        }
    }
}