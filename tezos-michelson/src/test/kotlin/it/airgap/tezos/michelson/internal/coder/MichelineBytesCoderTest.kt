package it.airgap.tezos.michelson.internal.coder

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.michelson.coder.decodeFromBytes
import it.airgap.tezos.michelson.coder.decodeFromString
import it.airgap.tezos.michelson.coder.encodeToBytes
import it.airgap.tezos.michelson.coder.encodeToString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.asHexString
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MichelineBytesCoderTest {

    private lateinit var tezos: Tezos

    private lateinit var michelineBytesCoder: MichelineBytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        michelineBytesCoder = MichelineBytesCoder(
            tezos.michelsonModule.dependencyRegistry.stringToMichelsonPrimConverter,
            tezos.michelsonModule.dependencyRegistry.tagToMichelsonPrimConverter,
            tezos.michelsonModule.dependencyRegistry.michelineToCompactStringConverter,
            tezos.coreModule.dependencyRegistry.tezosIntegerBytesCoder,
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode Micheline Node to bytes`() = withTezosContext {
        listOf(
            literalIntegersWithBytes,
            literalStringsWithBytes,
            literalBytesWithBytes,
            primitiveApplicationsWithBytes,
            sequencesWithBytes,
        ).flatten().forEach {
            assertContentEquals(it.second, michelineBytesCoder.encode(it.first))
            assertContentEquals(it.second, it.first.encodeToBytes(tezos))
            assertContentEquals(it.second, it.first.encodeToBytes(michelineBytesCoder))
            assertEquals(it.second.toHexString().asString(withPrefix = false), it.first.encodeToString(withHexPrefix = false, tezos))
            assertEquals(it.second.toHexString().asString(withPrefix = false), it.first.encodeToString(michelineBytesCoder, withHexPrefix = false))
            assertEquals(it.second.toHexString().asString(withPrefix = true), it.first.encodeToString(withHexPrefix = true, tezos))
            assertEquals(it.second.toHexString().asString(withPrefix = true), it.first.encodeToString(michelineBytesCoder, withHexPrefix = true))
        }
    }

    @Test
    fun `should fail to encode Micheline Primitive Application if prim is unknown`() = withTezosContext {
        unknownPrimitiveApplications.forEach {
            assertFailsWith<IllegalArgumentException> { michelineBytesCoder.encode(it) }
            assertFailsWith<IllegalArgumentException> { it.encodeToBytes(tezos) }
            assertFailsWith<IllegalArgumentException> { it.encodeToBytes(michelineBytesCoder) }
            assertFailsWith<IllegalArgumentException> { it.encodeToString(tezos = tezos) }
            assertFailsWith<IllegalArgumentException> { it.encodeToString(michelineBytesCoder) }
        }
    }

    @Test
    fun `should decode Micheline Node from bytes`() = withTezosContext {
        listOf(
            literalIntegersWithBytes,
            literalStringsWithBytes,
            literalBytesWithBytes,
            primitiveApplicationsWithBytes,
            sequencesWithBytes,
        ).flatten().forEach {
            assertEquals(it.first, michelineBytesCoder.decode(it.second))
            assertEquals(it.first, MichelineNode.decodeFromBytes(it.second, tezos))
            assertEquals(it.first, MichelineNode.decodeFromBytes(it.second, michelineBytesCoder))
            assertEquals(it.first, MichelineNode.decodeConsumingFromBytes(it.second.toMutableList(), michelineBytesCoder))
            assertEquals(it.first, MichelineNode.decodeFromString(it.second.toHexString().asString(withPrefix = false), tezos))
            assertEquals(it.first, MichelineNode.decodeFromString(it.second.toHexString().asString(withPrefix = false), michelineBytesCoder))
            assertEquals(it.first, MichelineNode.decodeFromString(it.second.toHexString().asString(withPrefix = true), tezos))
            assertEquals(it.first, MichelineNode.decodeFromString(it.second.toHexString().asString(withPrefix = true), michelineBytesCoder))
        }
    }

    @Test
    fun `should fail to decode Micheline Node if encoded value is invalid`() = withTezosContext {
        invalidEncodings.forEach {
            assertFailsWith<IllegalArgumentException> { michelineBytesCoder.decode(it.asHexString().toByteArray()) }
            assertFailsWith<IllegalArgumentException> { MichelineNode.decodeFromString(it, tezos) }
            assertFailsWith<IllegalArgumentException> { MichelineNode.decodeFromString(it, michelineBytesCoder) }
            assertFailsWith<IllegalArgumentException> { MichelineNode.decodeFromBytes(it.asHexString().toByteArray(), tezos) }
            assertFailsWith<IllegalArgumentException> { MichelineNode.decodeFromBytes(it.asHexString().toByteArray(), michelineBytesCoder) }
            assertFailsWith<IllegalArgumentException> { MichelineNode.decodeConsumingFromBytes(it.asHexString().toByteArray().toMutableList(), michelineBytesCoder) }
        }
    }

    private val literalIntegersWithBytes: List<Pair<MichelineLiteral.Integer, ByteArray>>
        get() = listOf(
            MichelineLiteral.Integer("-41547452475632687683489977342365486797893454355756867843") to "00c384efcfc7dac2f5849995afab9fa7c48b8fa4c0d9b5ca908dc70d".asHexString().toByteArray(),
            MichelineLiteral.Integer("-54576326575686358562454576456764") to "00fc90d3c2e6a3b9c4c0b7fbf3b3b6d802".asHexString().toByteArray(),
            MichelineLiteral.Integer(-6852352674543413768) to "00c8a8dd9df89cb998be01".asHexString().toByteArray(),
            MichelineLiteral.Integer(-18756523543673) to "00f9b1e2fee2c308".asHexString().toByteArray(),
            MichelineLiteral.Integer(-128) to "00c002".asHexString().toByteArray(),
            MichelineLiteral.Integer(-127) to "00ff01".asHexString().toByteArray(),
            MichelineLiteral.Integer(-64) to "00c001".asHexString().toByteArray(),
            MichelineLiteral.Integer(-42) to "006a".asHexString().toByteArray(),
            MichelineLiteral.Integer(-10) to "004a".asHexString().toByteArray(),
            MichelineLiteral.Integer(-1) to "0041".asHexString().toByteArray(),
            MichelineLiteral.Integer(0) to "0000".asHexString().toByteArray(),
            MichelineLiteral.Integer(1) to "0001".asHexString().toByteArray(),
            MichelineLiteral.Integer(10) to "000a".asHexString().toByteArray(),
            MichelineLiteral.Integer(42) to "002a".asHexString().toByteArray(),
            MichelineLiteral.Integer(64) to "008001".asHexString().toByteArray(),
            MichelineLiteral.Integer(127) to "00bf01".asHexString().toByteArray(),
            MichelineLiteral.Integer(128) to "008002".asHexString().toByteArray(),
            MichelineLiteral.Integer(18756523543673) to "00b9b1e2fee2c308".asHexString().toByteArray(),
            MichelineLiteral.Integer(6852352674543413768) to "0088a8dd9df89cb998be01".asHexString().toByteArray(),
            MichelineLiteral.Integer("54576326575686358562454576456764") to "00bc90d3c2e6a3b9c4c0b7fbf3b3b6d802".asHexString().toByteArray(),
            MichelineLiteral.Integer("41547452475632687683489977342365486797893454355756867843") to "008384efcfc7dac2f5849995afab9fa7c48b8fa4c0d9b5ca908dc70d".asHexString().toByteArray(),
        )

    private val literalStringsWithBytes: List<Pair<MichelineLiteral.String, ByteArray>>
        get() = listOf(
            MichelineLiteral.String("") to "0100000000".asHexString().toByteArray(),
            MichelineLiteral.String("a") to "010000000161".asHexString().toByteArray(),
            MichelineLiteral.String("abc") to "0100000003616263".asHexString().toByteArray(),
            MichelineLiteral.String("tz1ZsKMooGPJa5HoRT5DQV5j1RkRcSiypnYN") to "0100000024747a315a734b4d6f6f47504a6135486f525435445156356a31526b5263536979706e594e".asHexString().toByteArray(),
        )

    private val literalBytesWithBytes: List<Pair<MichelineLiteral.Bytes, ByteArray>>
        get() = listOf(
            MichelineLiteral.Bytes("0x") to "0a00000000".asHexString().toByteArray(),
            MichelineLiteral.Bytes("0x00") to "0a0000000100".asHexString().toByteArray(),
            MichelineLiteral.Bytes("0x9434dc98") to "0a000000049434dc98".asHexString().toByteArray(),
            MichelineLiteral.Bytes("0x7b1ea2cb") to "0a000000047b1ea2cb".asHexString().toByteArray(),
            MichelineLiteral.Bytes("0xe40476d7") to "0a00000004e40476d7".asHexString().toByteArray(),
            MichelineLiteral.Bytes("0xc47320abdd31") to "0a00000006c47320abdd31".asHexString().toByteArray(),
            MichelineLiteral.Bytes("0x5786dac9eaf4") to "0a000000065786dac9eaf4".asHexString().toByteArray(),
        )

    private val primitiveApplicationsWithBytes: List<Pair<MichelinePrimitiveApplication, ByteArray>>
        get() = listOf(
            MichelinePrimitiveApplication(
                prim = "parameter",
                args = listOf(MichelinePrimitiveApplication("unit"))
            ) to "0500036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "parameter",
                args = listOf(MichelinePrimitiveApplication("unit")),
                annots = listOf("%parameter"),
            ) to "0600036c0000000a25706172616d65746572".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "parameter",
                args = listOf(MichelinePrimitiveApplication("unit")),
                annots = listOf("%annot1", "@annot2"),
            ) to "0600036c0000000f25616e6e6f74312040616e6e6f7432".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "parameter",
                args = listOf(MichelinePrimitiveApplication("unit")),
                annots = listOf("@annot1", ":annot2", "\$annot3", "&annot4", "%annot5", "!annot6", "?annot7"),
            ) to "0600036c0000003740616e6e6f7431203a616e6e6f74322024616e6e6f74332026616e6e6f74342025616e6e6f74352021616e6e6f7436203f616e6e6f7437".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "storage",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to "0501036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "code",
                args = listOf(MichelinePrimitiveApplication("UNIT")),
            ) to "0502034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "False",
            ) to "0303".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "False",
                annots = listOf("%false"),
            ) to "0403000000062566616c7365".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "Elt",
                args = listOf(
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                ),
            ) to "0704030b030b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "Elt",
                args = listOf(
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                ),
                annots = listOf("%elt"),
            ) to "0804030b030b0000000425656c74".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "Left",
                args = listOf(MichelinePrimitiveApplication("Unit")),
            ) to "0505030b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "None",
            ) to "0306".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "Pair",
                args = listOf(
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                ),
            ) to "090700000006030b030b030b00000000".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "Pair",
                args = listOf(
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                ),
                annots = listOf("%pair"),
            ) to "090700000006030b030b030b000000052570616972".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "Right",
                args = listOf(MichelinePrimitiveApplication("Unit")),
            ) to "0508030b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "Some",
                args = listOf(MichelinePrimitiveApplication("Unit")),
            ) to "0509030b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "True",
            ) to "030a".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "Unit",
            ) to "030b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "PACK",
            ) to "030c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "UNPACK",
                listOf(MichelinePrimitiveApplication("unit"))
            ) to "050d036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "BLAKE2B",
            ) to "030e".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SHA256",
            ) to "030f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SHA512",
            ) to "0310".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "ABS",
            ) to "0311".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "ADD",
            ) to "0312".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "AMOUNT",
            ) to "0313".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "AND",
            ) to "0314".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "BALANCE",
            ) to "0315".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "CAR",
            ) to "0316".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "CDR",
            ) to "0317".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "CHECK_SIGNATURE",
            ) to "0318".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "COMPARE",
            ) to "0319".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "CONCAT",
            ) to "031a".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "CONS",
            ) to "031b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "CREATE_CONTRACT",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                ),
            ) to "091d0000000b036c036c0200000002034f00000000".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "IMPLICIT_ACCOUNT",
            ) to "031e".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "DIP",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
            ) to "051f0200000002034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "DIP",
                args = listOf(
                    MichelineLiteral.Integer(1),
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                ),
            ) to "071f00010200000002034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "DROP",
            ) to "0320".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "DROP",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to "05200001".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "DUP",
            ) to "0321".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "DUP",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to "05210001".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "EDIV",
            ) to "0322".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "EMPTY_MAP",
                args = listOf(
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                ),
            ) to "0723030b030b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "EMPTY_SET",
                args = listOf(MichelinePrimitiveApplication("Unit")),
            ) to "0524030b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "EQ",
            ) to "0325".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "EXEC",
            ) to "0326".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "FAILWITH",
            ) to "0327".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "GE",
            ) to "0328".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "GET",
            ) to "0329".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "GET",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to "05290001".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "GT",
            ) to "032a".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "HASH_KEY",
            ) to "032b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "IF",
                args = listOf(
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                ),
            ) to "072c0200000002034f0200000002034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "IF_CONS",
                args = listOf(
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                ),
            ) to "072d0200000002034f0200000002034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "IF_LEFT",
                args = listOf(
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                ),
            ) to "072e0200000002034f0200000002034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "IF_NONE",
                args = listOf(
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                ),
            ) to "072f0200000002034f0200000002034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "INT",
            ) to "0330".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "LAMBDA",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                ),
            ) to "09310000000b036c036c0200000002034f00000000".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "LE",
            ) to "0332".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "LEFT",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to "0533036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "LOOP",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
            ) to "05340200000002034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "LSL",
            ) to "0335".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "LSR",
            ) to "0336".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "LT",
            ) to "0337".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "MAP",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
            ) to "05380200000002034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "MEM",
            ) to "0339".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "MUL",
            ) to "033a".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "NEG",
            ) to "033b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "NEQ",
            ) to "033c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "NIL",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to "053d036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "NONE",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to "053e036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "NOT",
            ) to "033f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "NOW",
            ) to "0340".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "OR",
            ) to "0341".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "PAIR",
            ) to "0342".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "PAIR",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to "05420001".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "PUSH",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("Unit"),
                ),
            ) to "0743036c030b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "RIGHT",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to "0544036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SIZE",
            ) to "0345".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SOME",
            ) to "0346".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SOURCE",
            ) to "0347".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SENDER",
            ) to "0348".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SELF",
            ) to "0349".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SUB",
            ) to "034b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SWAP",
            ) to "034c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "TRANSFER_TOKENS",
            ) to "034d".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SET_DELEGATE",
            ) to "034e".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "UNIT",
            ) to "034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "UPDATE",
            ) to "0350".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "UPDATE",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to "05500001".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "XOR",
            ) to "0351".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "ITER",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
            ) to "05520200000002034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "LOOP_LEFT",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
            ) to "05530200000002034f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "ADDRESS",
            ) to "0354".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "CONTRACT",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to "0555036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "ISNAT",
            ) to "0356".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "CAST",
            ) to "0357".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "RENAME",
            ) to "0358".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "bool",
            ) to "0359".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "contract",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to "055a036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "int",
            ) to "035b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "key",
            ) to "035c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "key_hash",
            ) to "035d".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "lambda",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                ),
            ) to "075e036c036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "list",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to "055f036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "map",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                ),
            ) to "0760036c036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "big_map",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                ),
            ) to "0761036c036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "nat",
            ) to "0362".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "option",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to "0563036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "or",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                ),
            ) to "0764036c036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "pair",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                ),
            ) to "096500000006036c036c036c00000000".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "set",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to "0566036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "signature",
            ) to "0367".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "string",
            ) to "0368".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "bytes",
            ) to "0369".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "mutez",
            ) to "036a".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "timestamp",
            ) to "036b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "unit",
            ) to "036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "operation",
            ) to "036d".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "address",
            ) to "036e".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SLICE",
            ) to "036f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "DIG",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to "05700001".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "DUG",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to "05710001".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "EMPTY_BIG_MAP",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                ),
            ) to "0772036c036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "APPLY",
            ) to "0373".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "chain_id",
            ) to "0374".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "CHAIN_ID",
            ) to "0375".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "LEVEL",
            ) to "0376".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SELF_ADDRESS",
            ) to "0377".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "never",
            ) to "0378".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "NEVER",
            ) to "0379".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "UNPAIR",
            ) to "037a".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "UNPAIR",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to "057a0001".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "VOTING_POWER",
            ) to "037b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "TOTAL_VOTING_POWER",
            ) to "037c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "KECCAK",
            ) to "037d".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SHA3",
            ) to "037e".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "PAIRING_CHECK",
            ) to "037f".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "bls12_381_g1",
            ) to "0380".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "bls12_381_g2",
            ) to "0381".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "bls12_381_fr",
            ) to "0382".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "sapling_state",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to "05830001".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "sapling_transaction",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to "05840001".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SAPLING_EMPTY_STATE",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to "05850001".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SAPLING_VERIFY_UPDATE",
            ) to "0386".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "ticket",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to "0587036c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "TICKET",
            ) to "0388".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "READ_TICKET",
            ) to "0389".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "SPLIT_TICKET",
            ) to "038a".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "JOIN_TICKETS",
            ) to "038b".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "GET_AND_UPDATE",
            ) to "038c".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "chest",
            ) to "038d".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "chest_key",
            ) to "038e".asHexString().toByteArray(),
            MichelinePrimitiveApplication(
                prim = "OPEN_CHEST",
            ) to "038f".asHexString().toByteArray(),
        )

    private val sequencesWithBytes: List<Pair<MichelineSequence, ByteArray>>
        get() = listOf(
            MichelineSequence(listOf()) to "0200000000".asHexString().toByteArray(),
            MichelineSequence(
                listOf(
                    MichelineLiteral.Integer(0),
                )
            ) to "02000000020000".asHexString().toByteArray(),
            MichelineSequence(
                listOf(
                    MichelineLiteral.Integer(0),
                    MichelineLiteral.String("abc"),
                )
            ) to "020000000a00000100000003616263".asHexString().toByteArray(),
        )

    private val unknownPrimitiveApplications: List<MichelinePrimitiveApplication>
        get() = listOf(
            MichelinePrimitiveApplication("unknown"),
        )

    private val invalidEncodings: List<String>
        get() = listOf(
            "00",
            "01",
            "0100000001",
            "0a",
            "0a00000001",
            "0b8f",
            "058e",
            "077a0001",
            "095e036c036c",
        )
}