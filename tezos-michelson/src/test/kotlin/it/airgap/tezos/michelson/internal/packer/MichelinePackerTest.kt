package it.airgap.tezos.michelson.internal.packer

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.asHexString
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline
import it.airgap.tezos.michelson.normalizer.normalized
import it.airgap.tezos.michelson.packer.packToBytes
import it.airgap.tezos.michelson.packer.packToString
import it.airgap.tezos.michelson.packer.unpackFromBytes
import it.airgap.tezos.michelson.packer.unpackFromString
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MichelinePackerTest {

    private lateinit var tezos: Tezos
    private lateinit var michelinePacker: MichelinePacker

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        michelinePacker = MichelinePacker(
            tezos.michelsonModule.dependencyRegistry.michelineBytesCoder,
            tezos.michelsonModule.dependencyRegistry.michelinePrimitiveApplicationNormalizer,
            tezos.michelsonModule.dependencyRegistry.stringToMichelsonPrimConverter,
            tezos.michelsonModule.dependencyRegistry.michelineToCompactStringConverter,
            tezos.coreModule.dependencyRegistry.encodedBytesCoder,
            tezos.coreModule.dependencyRegistry.addressBytesCoder,
            tezos.coreModule.dependencyRegistry.publicKeyBytesCoder,
            tezos.coreModule.dependencyRegistry.implicitAddressBytesCoder,
            tezos.coreModule.dependencyRegistry.signatureBytesCoder,
            tezos.coreModule.dependencyRegistry.timestampBigIntCoder,
            tezos.coreModule.dependencyRegistry.stringToAddressConverter,
            tezos.coreModule.dependencyRegistry.stringToImplicitAddressConverter,
            tezos.coreModule.dependencyRegistry.stringToPublicKeyConverter,
            tezos.coreModule.dependencyRegistry.stringToSignatureConverter,
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should pack Micheline to bytes`() = withTezosContext {
        listOf(
            packedWithIntegersAndSchemas,
            packedWithStringsAndSchemas,
            packedWithBytesAndSchemas,
            packedWithPrimitiveApplicationsAndSchemas,
            packedWithSequencesAndSchemas,
            listOf(
                "050707030b0707030b030b".asHexString().toByteArray() to listOf(
                    MichelineSequence(
                        MichelinePrimitiveApplication("Unit"),
                        MichelinePrimitiveApplication("Unit"),
                        MichelinePrimitiveApplication("Unit"),
                    ) to micheline(tezos) {
                        pair {
                            arg { unit }
                            arg { unit }
                            arg { unit }
                        }
                    },
                ),
                "050a00000016000094a0ba27169ed8d97c1f476de6156c2482dbfb3d".asHexString().toByteArray() to listOf(
                    MichelineLiteral.Bytes("0x000094a0ba27169ed8d97c1f476de6156c2482dbfb3d") to micheline(tezos) { address }
                ),
                "050a00000004ef6a66af".asHexString().toByteArray() to listOf(
                    MichelineLiteral.Bytes("0xef6a66af") to micheline(tezos) { chainId }
                ),
                "050a000000150094a0ba27169ed8d97c1f476de6156c2482dbfb3d".asHexString().toByteArray() to listOf(
                    MichelineLiteral.Bytes("0x0094a0ba27169ed8d97c1f476de6156c2482dbfb3d") to micheline(tezos) { keyHash }
                ),
                "050a00000021005a9847101250e9cea9e714a8fd945e5131aeb5c021e027b1420db0cdd971c862".asHexString().toByteArray() to listOf(
                    MichelineLiteral.Bytes("0x005a9847101250e9cea9e714a8fd945e5131aeb5c021e027b1420db0cdd971c862") to micheline(tezos) { key }
                ),
                "050a000000402050b812a7d784193c030c8c72d892c2e167765fa5506c10bca5073b963fb9b861251f349c52cde2b76af285235c0a8bdc7eb2b26646ddc759b7da425d64588d".asHexString().toByteArray() to listOf(
                    MichelineLiteral.String("edsigtc2yNWbqq5WxGjJMrBDFR6WjQHWztGaXPsKk5ZoPST6XBmPfkykxxoEcmvHnyNzQAdzih6dE6yMwGi5smEmARDhjfwaqwZ") to micheline(tezos) { signature }
                ),
                "050a000000409dbc33d2b25d37d5a965789bef6e65f65d2048922e00127a79741137a84915e2a61e5f43bdaab7c401e5a74301118dd2aa048c39c7808a994cbc1e8ca819e73a".asHexString().toByteArray() to listOf(
                    MichelineLiteral.String("spsig1SSsjUEotAhXLxnpxhYYTDbTz2cWbj2LqvFsdeEq5ggRetuAWYLa1GjPPcEJcHL8CqpGt2DeYq9onWuMiYraGNEA4P3UKC") to micheline(tezos) { signature }
                ),
                "050a00000040d5dd7160f806328604d8fc6413a17386eb5b3825dbb68b4b7c38ca314c624955efd6bfafb3e04aa696d7fbee0bb32d94288c4d582edcd0d9f07142fa1dd7048f".asHexString().toByteArray() to listOf(
                    MichelineLiteral.String("p2sigqHLPRghCJr9E3B1ihWfjPJCLsADCL9JqoxESYLqQ2UzBSRvcUbjQ1s1ztpa5mNcrKAnEFAd7MDNrTrm1iDT7bA3Lkkxne") to micheline(tezos) { signature }
                ),
                "050a00000040cdedf48569b5ff605f3d4d00219d70a6b4aa46b090c5ceceeacfb3d5abb614e2ae2d01ce7b0b2fb17bee11434d3b379bcfef01126b2778646ac7bb11c5eb5a0b".asHexString().toByteArray() to listOf(
                    MichelineLiteral.String("sigpvu5AhSNvaGmWMj4jZFWfZ73aCWtHqLEXCb5uRCrPkXzP5AuJeuptXKS3NMQHg7h8nd3iDVYNwf2fFNaWskUA7DjxJKAL") to micheline(tezos) { signature }
                ),
                "050a000000402050b812a7d784193c030c8c72d892c2e167765fa5506c10bca5073b963fb9b861251f349c52cde2b76af285235c0a8bdc7eb2b26646ddc759b7da425d64588d".asHexString().toByteArray() to listOf(
                    MichelineLiteral.Bytes("0x2050b812a7d784193c030c8c72d892c2e167765fa5506c10bca5073b963fb9b861251f349c52cde2b76af285235c0a8bdc7eb2b26646ddc759b7da425d64588d") to micheline(tezos) { signature }
                ),
                "0500aff8aff1ce5f".asHexString().toByteArray() to listOf(
                    MichelineLiteral.Integer("1642675437103") to micheline(tezos) { timestamp }
                ),
            ),
        ).flatten().forEach { (packed, michelineWithSchemas) ->
            michelineWithSchemas.forEach {
                assertContentEquals(packed, michelinePacker.pack(it.first, it.second))
                assertContentEquals(packed, it.first.packToBytes(it.second, tezos))
                assertContentEquals(packed, it.first.packToBytes(it.second, michelinePacker))
                assertEquals(packed.toHexString().asString(withPrefix = false), it.first.packToString(it.second, withHexPrefix = false, tezos))
                assertEquals(packed.toHexString().asString(withPrefix = false), it.first.packToString(it.second, withHexPrefix = false, michelinePacker))
                assertEquals(packed.toHexString().asString(withPrefix = true), it.first.packToString(it.second, withHexPrefix = true, tezos))
                assertEquals(packed.toHexString().asString(withPrefix = true), it.first.packToString(it.second, withHexPrefix = true, michelinePacker))
            }
        }
    }

    @Test
    fun `should fail to pack Micheline Primitive Application if prim is unknown`() = withTezosContext {
        invalidPrimitiveApplicationsWithSchema.forEach {
            assertFailsWith<IllegalArgumentException> { michelinePacker.pack(it.first, it.second) }
            assertFailsWith<IllegalArgumentException> { it.first.packToBytes(it.second, tezos) }
            assertFailsWith<IllegalArgumentException> { it.first.packToBytes(it.second, michelinePacker) }
            assertFailsWith<IllegalArgumentException> { it.first.packToString(it.second, tezos = tezos) }
            assertFailsWith<IllegalArgumentException> { it.first.packToString(it.second, michelinePacker = michelinePacker) }
        }
    }

    @Test
    fun `should unpack Micheline from bytes`() = withTezosContext {
        listOf(
            packedWithIntegersAndSchemas,
            packedWithStringsAndSchemas,
            packedWithBytesAndSchemas,
            packedWithPrimitiveApplicationsAndSchemas,
            packedWithSequencesAndSchemas,
            listOf(
                "050a000000402050b812a7d784193c030c8c72d892c2e167765fa5506c10bca5073b963fb9b861251f349c52cde2b76af285235c0a8bdc7eb2b26646ddc759b7da425d64588d".asHexString().toByteArray() to listOf(
                    MichelineLiteral.String("sigSDWFTNwZ9KvDYK4i9N4ToEDdTS4udhw2Ba1MDoQS75ZswT5G5qssjzhjKfzsup3j6X6maqsGMQAXv3g76RCEAddoyfkRa") to micheline(tezos) { signature }
                ),
                "050a000000409dbc33d2b25d37d5a965789bef6e65f65d2048922e00127a79741137a84915e2a61e5f43bdaab7c401e5a74301118dd2aa048c39c7808a994cbc1e8ca819e73a".asHexString().toByteArray() to listOf(
                    MichelineLiteral.String("sigidCQQdZQpjNxno4zYzPoJv65CpwXfPeNM8Yrr5XGnkiDqFMKyGC49FXhEfDYtGG885894N27h15hECFCgB5M9h3Up8u51") to micheline(tezos) { signature }
                ),
                "050a00000040d5dd7160f806328604d8fc6413a17386eb5b3825dbb68b4b7c38ca314c624955efd6bfafb3e04aa696d7fbee0bb32d94288c4d582edcd0d9f07142fa1dd7048f".asHexString().toByteArray() to listOf(
                    MichelineLiteral.String("sigqy7YLb96qv6m6vFTyv8wsqQxUVywEitdNBP48tQ6p2hduA9J6YeAjgbhht3rLB3Xh9GDAvgmrG4V5AqH7mFgKThJthxa3") to micheline(tezos) { signature }
                ),
                "050a00000040cdedf48569b5ff605f3d4d00219d70a6b4aa46b090c5ceceeacfb3d5abb614e2ae2d01ce7b0b2fb17bee11434d3b379bcfef01126b2778646ac7bb11c5eb5a0b".asHexString().toByteArray() to listOf(
                    MichelineLiteral.String("sigpvu5AhSNvaGmWMj4jZFWfZ73aCWtHqLEXCb5uRCrPkXzP5AuJeuptXKS3NMQHg7h8nd3iDVYNwf2fFNaWskUA7DjxJKAL") to micheline(tezos) { signature }
                ),
            ),
        ).flatten().forEach { (packed, michelineWithSchemas) ->
            michelineWithSchemas.forEach {
                assertEquals(it.first.normalized(tezos), michelinePacker.unpack(packed, it.second))
                assertEquals(it.first.normalized(tezos), Micheline.unpackFromBytes(packed, it.second, tezos))
                assertEquals(it.first.normalized(tezos), Micheline.unpackFromBytes(packed, it.second, michelinePacker))
                assertEquals(it.first.normalized(tezos), Micheline.unpackFromString(packed.toHexString().asString(withPrefix = false), it.second, tezos))
                assertEquals(it.first.normalized(tezos), Micheline.unpackFromString(packed.toHexString().asString(withPrefix = false), it.second, michelinePacker))
                assertEquals(it.first.normalized(tezos), Micheline.unpackFromString(packed.toHexString().asString(withPrefix = true), it.second, tezos))
                assertEquals(it.first.normalized(tezos), Micheline.unpackFromString(packed.toHexString().asString(withPrefix = true), it.second, michelinePacker))
            }
        }
    }

    @Test
    fun `should fail to unpack Micheline if packed value or schema is invalid`() = withTezosContext {
        (invalidPackedWithSchema + packedWithInvalidSchema).forEach {
            assertFailsWith<IllegalArgumentException> { michelinePacker.unpack(it.first.asHexString().toByteArray(), it.second) }
            assertFailsWith<IllegalArgumentException> { Micheline.unpackFromBytes(it.first.asHexString().toByteArray(), it.second, tezos) }
            assertFailsWith<IllegalArgumentException> { Micheline.unpackFromBytes(it.first.asHexString().toByteArray(), it.second, michelinePacker) }
            assertFailsWith<IllegalArgumentException> { Micheline.unpackFromString(it.first, it.second, tezos) }
            assertFailsWith<IllegalArgumentException> { Micheline.unpackFromString(it.first, it.second, michelinePacker) }
        }
    }

    private val packedWithIntegersAndSchemas: List<Pair<ByteArray, List<Pair<MichelineLiteral.Integer, Micheline?>>>>
        get() = listOf(
            "0500c384efcfc7dac2f5849995afab9fa7c48b8fa4c0d9b5ca908dc70d".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer("-41547452475632687683489977342365486797893454355756867843") to null,
                MichelineLiteral.Integer("-41547452475632687683489977342365486797893454355756867843") to micheline(tezos) { int },
            ),
            "0500fc90d3c2e6a3b9c4c0b7fbf3b3b6d802".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer("-54576326575686358562454576456764") to null,
                MichelineLiteral.Integer("-54576326575686358562454576456764") to micheline(tezos) { int },
            ),
            "0500c8a8dd9df89cb998be01".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(-6852352674543413768) to null,
                MichelineLiteral.Integer(-6852352674543413768) to micheline(tezos) { int },
            ),
            "0500f9b1e2fee2c308".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(-18756523543673) to null,
                MichelineLiteral.Integer(-18756523543673) to micheline(tezos) { int },
            ),
            "0500c002".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(-128) to null,
                MichelineLiteral.Integer(-128) to micheline(tezos) { int },
            ),
            "0500ff01".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(-127) to null,
                MichelineLiteral.Integer(-127) to micheline(tezos) { int },
            ),
            "0500c001".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(-64) to null,
                MichelineLiteral.Integer(-64) to micheline(tezos) { int },
            ),
            "05006a".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(-42) to null,
                MichelineLiteral.Integer(-42) to micheline(tezos) { int },
            ),
            "05004a".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(-10) to null,
                MichelineLiteral.Integer(-10) to micheline(tezos) { int },
            ),
            "050041".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(-1) to null,
                MichelineLiteral.Integer(-1) to micheline(tezos) { int },
            ),
            "050000".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(0) to null,
                MichelineLiteral.Integer(0) to micheline(tezos) { int },
            ),
            "050001".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(1) to null,
                MichelineLiteral.Integer(1) to micheline(tezos) { int },
            ),
            "05000a".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(10) to null,
                MichelineLiteral.Integer(10) to micheline(tezos) { int },
            ),
            "05002a".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(42) to null,
                MichelineLiteral.Integer(42) to micheline(tezos) { int },
            ),
            "05008001".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(64) to null,
                MichelineLiteral.Integer(64) to micheline(tezos) { int },
            ),
            "0500bf01".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(127) to null,
                MichelineLiteral.Integer(127) to micheline(tezos) { int },
            ),
            "05008002".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(128) to null,
                MichelineLiteral.Integer(128) to micheline(tezos) { int },
            ),
            "0500b9b1e2fee2c308".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(18756523543673) to null,
                MichelineLiteral.Integer(18756523543673) to micheline(tezos) { int },
            ),
            "050088a8dd9df89cb998be01".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(6852352674543413768) to null,
                MichelineLiteral.Integer(6852352674543413768) to micheline(tezos) { int },
            ),
            "0500bc90d3c2e6a3b9c4c0b7fbf3b3b6d802".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer("54576326575686358562454576456764") to null,
                MichelineLiteral.Integer("54576326575686358562454576456764") to micheline(tezos) { int },
            ),
            "05008384efcfc7dac2f5849995afab9fa7c48b8fa4c0d9b5ca908dc70d".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer("41547452475632687683489977342365486797893454355756867843") to null,
                MichelineLiteral.Integer("41547452475632687683489977342365486797893454355756867843") to micheline(tezos) { int },
            ),
            "05002a".asHexString().toByteArray() to listOf(
                MichelineLiteral.Integer(42) to micheline(tezos) {
                    bigMap {
                        key { unit }
                        value { unit }
                    }
                },
            ),
        )

    private val packedWithStringsAndSchemas: List<Pair<ByteArray, List<Pair<MichelineLiteral.String, Micheline?>>>>
        get() = listOf(
            "050100000000".asHexString().toByteArray() to listOf(
                MichelineLiteral.String("") to null,
                MichelineLiteral.String("") to micheline(tezos) { string },
            ),
            "05010000000161".asHexString().toByteArray() to listOf(
                MichelineLiteral.String("a") to null,
                MichelineLiteral.String("a") to micheline(tezos) { string },
            ),
            "050100000003616263".asHexString().toByteArray() to listOf(
                MichelineLiteral.String("abc") to null,
                MichelineLiteral.String("abc") to micheline(tezos) { string },
            ),
            "050100000024747a315a734b4d6f6f47504a6135486f525435445156356a31526b5263536979706e594e".asHexString().toByteArray() to listOf(
                MichelineLiteral.String("tz1ZsKMooGPJa5HoRT5DQV5j1RkRcSiypnYN") to null,
                MichelineLiteral.String("tz1ZsKMooGPJa5HoRT5DQV5j1RkRcSiypnYN") to micheline(tezos) { string },
            ),
            "050a00000016000094a0ba27169ed8d97c1f476de6156c2482dbfb3d".asHexString().toByteArray() to listOf(
                MichelineLiteral.String("tz1ZBuF2dQ7E1b32bK3g1Qsah4pvWqpM4b4A") to micheline(tezos) { address }
            ),
            "050a00000004ef6a66af".asHexString().toByteArray() to listOf(
                MichelineLiteral.String("NetXy3eo3jtuwuc") to micheline(tezos) { chainId }
            ),
            "050a000000150094a0ba27169ed8d97c1f476de6156c2482dbfb3d".asHexString().toByteArray() to listOf(
                MichelineLiteral.String("tz1ZBuF2dQ7E1b32bK3g1Qsah4pvWqpM4b4A") to micheline(tezos) { keyHash }
            ),
            "050a00000021005a9847101250e9cea9e714a8fd945e5131aeb5c021e027b1420db0cdd971c862".asHexString().toByteArray() to listOf(
                MichelineLiteral.String("edpkuL84TEk6s2C9JCywmBS4Mztumq6iUVxNtBHvuZG95VPvFw1yCR") to micheline(tezos) { key }
            ),
            "0500aff8aff1ce5f".asHexString().toByteArray() to listOf(
                MichelineLiteral.String("2022-01-20T10:43:57.103Z") to micheline(tezos) { timestamp }
            ),
        )

    private val packedWithBytesAndSchemas: List<Pair<ByteArray, List<Pair<MichelineLiteral.Bytes, Micheline?>>>>
        get() = listOf(
            "050a00000000".asHexString().toByteArray() to listOf(
                MichelineLiteral.Bytes("0x") to null,
                MichelineLiteral.Bytes("0x") to micheline(tezos) { bytes },
            ),
            "050a0000000100".asHexString().toByteArray() to listOf(
                MichelineLiteral.Bytes("0x00") to null,
                MichelineLiteral.Bytes("0x00") to micheline(tezos) { bytes },
            ),
            "050a000000049434dc98".asHexString().toByteArray() to listOf(
                MichelineLiteral.Bytes("0x9434dc98") to null,
                MichelineLiteral.Bytes("0x9434dc98") to micheline(tezos) { bytes },
            ),
            "050a000000047b1ea2cb".asHexString().toByteArray() to listOf(
                MichelineLiteral.Bytes("0x7b1ea2cb") to null,
                MichelineLiteral.Bytes("0x7b1ea2cb") to micheline(tezos) { bytes },
            ),
            "050a00000004e40476d7".asHexString().toByteArray() to listOf(
                MichelineLiteral.Bytes("0xe40476d7") to null,
                MichelineLiteral.Bytes("0xe40476d7") to micheline(tezos) { bytes },
            ),
            "050a00000006c47320abdd31".asHexString().toByteArray() to listOf(
                MichelineLiteral.Bytes("0xc47320abdd31") to null,
                MichelineLiteral.Bytes("0xc47320abdd31") to micheline(tezos) { bytes },
            ),
            "050a000000065786dac9eaf4".asHexString().toByteArray() to listOf(
                MichelineLiteral.Bytes("0x5786dac9eaf4") to null,
                MichelineLiteral.Bytes("0x5786dac9eaf4") to micheline(tezos) { bytes },
            ),
        )

    private val packedWithPrimitiveApplicationsAndSchemas: List<Pair<ByteArray, List<Pair<MichelinePrimitiveApplication, Micheline?>>>>
        get() = listOf(
            "050500036c".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "parameter",
                    args = listOf(MichelinePrimitiveApplication("unit"))
                ) to null
            ),
            "050600036c0000000a25706172616d65746572".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "parameter",
                    args = listOf(MichelinePrimitiveApplication("unit")),
                    annots = listOf("%parameter"),
                ) to null
            ),
            "050501036c".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "storage",
                    args = listOf(MichelinePrimitiveApplication("unit"))
                ) to null
            ),
            "050502034f".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "code",
                    args = listOf(MichelinePrimitiveApplication("UNIT"))
                ) to null
            ),
            "050303".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "False",
                ) to null,
                MichelinePrimitiveApplication(
                    prim = "False",
                ) to micheline(tezos) { bool },
            ),
            "050704030b030b".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "Elt",
                    args = listOf(
                        MichelinePrimitiveApplication("Unit"),
                        MichelinePrimitiveApplication("Unit"),
                    ),
                ) to null,
            ),
            "050505030b".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "Left",
                    args = listOf(MichelinePrimitiveApplication("Unit")),
                ) to null,
                MichelinePrimitiveApplication(
                    prim = "Left",
                    args = listOf(MichelinePrimitiveApplication("Unit")),
                )  to micheline(tezos) {
                   or {
                       lhs { unit }
                       rhs { bool }
                   }
                },
            ),
            "0505050707030b0707030b030b".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "Left",
                    args = listOf(
                        MichelinePrimitiveApplication(
                            prim = "Pair",
                            args = listOf(
                                MichelinePrimitiveApplication("Unit"),
                                MichelinePrimitiveApplication("Unit"),
                                MichelinePrimitiveApplication("Unit"),
                            ),
                        )
                    ),
                )  to micheline(tezos) {
                    or {
                        lhs {
                            pair {
                                arg { unit }
                                arg { unit }
                                arg { unit }
                            }
                        }
                        rhs { bool }
                    }
                },
            ),
            "050306".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "None",
                ) to null,
                MichelinePrimitiveApplication(
                    prim = "None",
                ) to micheline(tezos) {
                    option {
                        arg { unit }
                    }
                },
                MichelinePrimitiveApplication(
                    prim = "None",
                ) to micheline(tezos) {
                    option {
                        arg {
                            pair {
                                arg { unit }
                                arg { unit }
                                arg { unit }
                            }
                        }
                    }
                },
            ),
            "050303".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "False",
                ) to null,
                MichelinePrimitiveApplication(
                    prim = "False",
                ) to micheline(tezos) { bool },
            ),
            "050707030b030b".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "Pair",
                    args = listOf(
                        MichelinePrimitiveApplication("Unit"),
                        MichelinePrimitiveApplication("Unit"),
                    ),
                ) to null,
                MichelinePrimitiveApplication(
                    prim = "Pair",
                    args = listOf(
                        MichelinePrimitiveApplication("Unit"),
                        MichelinePrimitiveApplication("Unit"),
                    ),
                ) to micheline(tezos) {
                    pair {
                        arg { unit }
                        arg { unit }
                    }
                },
            ),
            "050707030b0707030b030b".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "Pair",
                    args = listOf(
                        MichelinePrimitiveApplication("Unit"),
                        MichelinePrimitiveApplication("Unit"),
                        MichelinePrimitiveApplication("Unit"),
                    ),
                ) to micheline(tezos) {
                    pair {
                        arg { unit }
                        arg { unit }
                        arg { unit }
                    }
                },
            ),
            "050508030b".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "Right",
                    args = listOf(MichelinePrimitiveApplication("Unit")),
                ) to null,
                MichelinePrimitiveApplication(
                    prim = "Right",
                    args = listOf(MichelinePrimitiveApplication("Unit")),
                )  to micheline(tezos) {
                    or {
                        lhs { bool }
                        rhs { unit }
                    }
                },
            ),
            "0505080707030b0707030b030b".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "Right",
                    args = listOf(
                        MichelinePrimitiveApplication(
                            prim = "Pair",
                            args = listOf(
                                MichelinePrimitiveApplication("Unit"),
                                MichelinePrimitiveApplication("Unit"),
                                MichelinePrimitiveApplication("Unit"),
                            ),
                        )
                    ),
                )  to micheline(tezos) {
                    or {
                        lhs { bool }
                        rhs {
                            pair {
                                arg { unit }
                                arg { unit }
                                arg { unit }
                            }
                        }
                    }
                },
            ),
            "050509030b".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "Some",
                    args = listOf(MichelinePrimitiveApplication("Unit")),
                ) to null,
                MichelinePrimitiveApplication(
                    prim = "Some",
                    args = listOf(MichelinePrimitiveApplication("Unit")),
                ) to micheline(tezos) {
                    option {
                        arg { unit }
                    }
                },
            ),
            "0505090707030b0707030b030b".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "Some",
                    args = listOf(
                        MichelinePrimitiveApplication(
                            prim = "Pair",
                            args = listOf(
                                MichelinePrimitiveApplication("Unit"),
                                MichelinePrimitiveApplication("Unit"),
                                MichelinePrimitiveApplication("Unit"),
                            ),
                        )
                    ),
                ) to micheline(tezos) {
                    option {
                        arg {
                            pair {
                                arg { unit }
                                arg { unit }
                                arg { unit }
                            }
                        }
                    }
                },
            ),
            "05030a".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "True",
                ) to null,
                MichelinePrimitiveApplication(
                    prim = "True",
                ) to micheline(tezos) { bool },
            ),
            "05030b".asHexString().toByteArray() to listOf(
                MichelinePrimitiveApplication(
                    prim = "Unit",
                ) to null,
                MichelinePrimitiveApplication(
                    prim = "Unit",
                ) to micheline(tezos) { unit },
            ),
        )

    private val packedWithSequencesAndSchemas: List<Pair<ByteArray, List<Pair<MichelineSequence, Micheline?>>>>
        get() = listOf(
            "050200000000".asHexString().toByteArray() to listOf(
                MichelineSequence(listOf()) to null,
                MichelineSequence(listOf()) to micheline(tezos) { sequence {  } },
            ),
            "0502000000020000".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelineLiteral.Integer(0),
                ) to null,
                MichelineSequence(
                    MichelineLiteral.Integer(0),
                ) to micheline(tezos) { sequence { int } },
            ),
            "05020000000a00000100000003616263".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelineLiteral.Integer(0),
                    MichelineLiteral.String("abc"),
                ) to null,
                MichelineSequence(
                    MichelineLiteral.Integer(0),
                    MichelineLiteral.String("abc"),
                ) to micheline(tezos) { sequence { int; string } },
            ),
            "050200000006030b030b030b".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                ) to micheline(tezos) {
                    list {
                        arg { unit }
                    }
                },
                MichelineSequence(
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                ) to micheline(tezos) {
                    set {
                        arg { unit }
                    }
                },
            ),
            "0502000000060704030b030b".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "Elt",
                        args = listOf(
                            MichelinePrimitiveApplication("Unit"),
                            MichelinePrimitiveApplication("Unit"),
                        ),
                    ),
                ) to micheline(tezos) {
                    map {
                        key { unit }
                        value { unit }
                    }
                },
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "Elt",
                        args = listOf(
                            MichelinePrimitiveApplication("Unit"),
                            MichelinePrimitiveApplication("Unit"),
                        ),
                    ),
                ) to micheline(tezos) {
                    bigMap {
                        key { unit }
                        value { unit }
                    }
                },
            ),
            "050200000002030c".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication("PACK"),
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication("PACK"),
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "0502000000070200000002030c".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelineSequence(
                        MichelinePrimitiveApplication("PACK"),
                    ),
                ) to null,
                MichelineSequence(
                    MichelineSequence(
                        MichelinePrimitiveApplication("PACK"),
                    ),
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "050200000009051f0200000002034f".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "DIP",
                        args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
                    ),
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "DIP",
                        args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
                    ),
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "050200000010072c0200000002034f0200000002034f".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "IF",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "IF",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    ),
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "050200000010072d0200000002034f0200000002034f".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "IF_CONS",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "IF_CONS",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    ),
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "050200000010072e0200000002034f0200000002034f".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "IF_LEFT",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "IF_LEFT",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    ),
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "050200000010072f0200000002034f0200000002034f".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "IF_NONE",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "IF_NONE",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    ),
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "05020000001509310000000b036c036c0200000002034f00000000".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "LAMBDA",
                        args = listOf(
                            MichelinePrimitiveApplication("unit"),
                            MichelinePrimitiveApplication("unit"),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "LAMBDA",
                        args = listOf(
                            MichelinePrimitiveApplication("unit"),
                            MichelinePrimitiveApplication("unit"),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "05020000000905340200000002034f".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "LOOP",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "LOOP",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    ),
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "05020000000905380200000002034f".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "MAP",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "MAP",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    ),
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "05020000000905520200000002034f".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "ITER",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "ITER",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    ),
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "05020000000905530200000002034f".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "LOOP_LEFT",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "LOOP_LEFT",
                        args = listOf(
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    ),
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "05020000000f0743075e036c036c0200000002034f".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "PUSH",
                        args = listOf(
                            MichelinePrimitiveApplication(
                                prim = "lambda",
                                args = listOf(
                                    MichelinePrimitiveApplication("unit"),
                                    MichelinePrimitiveApplication("unit"),
                                ),
                            ),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "PUSH",
                        args = listOf(
                            MichelinePrimitiveApplication(
                                prim = "lambda",
                                args = listOf(
                                    MichelinePrimitiveApplication("unit"),
                                    MichelinePrimitiveApplication("unit"),
                                ),
                            ),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
            "050200000015091d0000000b036c036c0200000002034f00000000".asHexString().toByteArray() to listOf(
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "CREATE_CONTRACT",
                        args = listOf(
                            MichelinePrimitiveApplication("unit"),
                            MichelinePrimitiveApplication("unit"),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to null,
                MichelineSequence(
                    MichelinePrimitiveApplication(
                        prim = "CREATE_CONTRACT",
                        args = listOf(
                            MichelinePrimitiveApplication("unit"),
                            MichelinePrimitiveApplication("unit"),
                            MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                        ),
                    )
                ) to micheline(tezos) {
                    lambda {
                        parameter { unit }
                        returnType { unit }
                    }
                },
            ),
        )

    private val invalidPrimitiveApplicationsWithSchema: List<Pair<MichelinePrimitiveApplication, Micheline?>>
        get() = listOf(
            MichelinePrimitiveApplication("unknown") to null,
        )

    private val invalidPackedWithSchema: List<Pair<String, Micheline?>>
        get() = listOf(
            "00" to null,
            "00020000000f0743075e036c036c0200000002034f" to micheline(tezos) {
                lambda {
                    parameter { unit }
                    returnType { unit }
                }
            },
            "050743075e036c036c0200000002034f" to micheline(tezos) {
                lambda {
                    parameter { unit }
                    returnType { unit }
                }
            }
        )

    private val packedWithInvalidSchema: List<Pair<String, Micheline?>>
        get() = listOf(
            "050100000000" to micheline(tezos) { int(1) },
            "0500aff8aff1ce5f" to micheline(tezos) {
                option {
                    arg { unit }
                }
            },
            "050041" to micheline(tezos) {
                option {
                    arg { unit }
                }
            },
            "0500aff8aff1ce5f" to micheline(tezos) {
                pair {
                    arg { unit }
                    arg { unit }
                }
            },
            "050041" to micheline(tezos) {
                pair {
                    arg { unit }
                    arg { unit }
                }
            },
        )
}