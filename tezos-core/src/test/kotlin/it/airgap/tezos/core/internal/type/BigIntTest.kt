package it.airgap.tezos.core.internal.type

import it.airgap.tezos.core.internal.utils.toHexString
import java.math.BigInteger
import kotlin.random.Random
import kotlin.test.*

class BigIntTest {

    @Test
    fun `creates BigInt from Byte`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            assertEquals(it.toByte(), BigInt.valueOf(it.toByte()).toByte())
        }
    }

    @Test
    fun `creates BigInt from UByte`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            assertEquals(it.toUByte(), BigInt.valueOf(it.toUByte()).toUByte())
        }
    }

    @Test
    fun `creates BigInt from Short`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            assertEquals(it.toShort(), BigInt.valueOf(it.toShort()).toShort())
        }
    }

    @Test
    fun `creates BigInt from UShort`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            assertEquals(it.toUShort(), BigInt.valueOf(it.toUShort()).toUShort())
        }
    }

    @Test
    fun `creates BigInt from Int`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            assertEquals(it.toInt(), BigInt.valueOf(it.toInt()).toInt())
        }
    }

    @Test
    fun `creates BigInt from UInt`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            assertEquals(it.toUInt(), BigInt.valueOf(it.toUInt()).toUInt())
        }
    }

    @Test
    fun `creates BigInt from Long`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            assertEquals(it, BigInt.valueOf(it).toLong())
        }
    }

    @Test
    fun `creates BigInt from ULong`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            assertEquals(it.toULong(), BigInt.valueOf(it.toULong()).toULong())
        }
    }

    @Test
    fun `creates BigInt from ByteArray`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            assertEquals(it, BigInt.valueOf(BigInteger.valueOf(it).toByteArray()).toLong())
        }
    }

    @Test
    fun `creates BigInt from HexString`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            assertEquals(it, BigInt.valueOf(BigInteger.valueOf(it).toByteArray().toHexString()).toLong())
        }
    }

    @Test
    fun `creates BigInt from String`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            assertEquals(it, BigInt.valueOf(it.toString()).toLong())
            assertEquals(it, BigInt.valueOf(it.toString(2), 2).toLong())
            assertEquals(it, BigInt.valueOf(it.toString(3), 3).toLong())
            assertEquals(it, BigInt.valueOf(it.toString(10), 10).toLong())
            assertEquals(it, BigInt.valueOf(it.toString(16), 16).toLong())
        }
    }
    
    @Test
    fun `finds max of 2 BigInts`() {
        val a = BigInt.valueOf(2)
        val b = BigInt.valueOf(1)
        
        assertEquals(a, BigInt.max(a, b))
        assertEquals(a, BigInt.max(b, a))
    }

    @Test
    fun `finds min of 2 BigInts`() {
        val a = BigInt.valueOf(2)
        val b = BigInt.valueOf(1)

        assertEquals(b, BigInt.min(a, b))
        assertEquals(b, BigInt.min(b, a))
    }

    @Test
    fun `performs logical AND on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)

            jvmBigInt to bigInteger
        }

        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertEquals(
                bigInteger.and(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.and((0).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.and((0).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.and((0).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.and(0L).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.and(BigInteger.valueOf(0)).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.and(JvmBigInt(BigInteger.valueOf(0))).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.and(JvmBigInt(BigInteger.valueOf(0)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.and(MockBigInt(JvmBigInt(BigInteger.valueOf(0)))).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.and((10).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.and((10).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.and((10).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.and(10L).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.and(BigInteger.valueOf(10)).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.and(JvmBigInt(BigInteger.valueOf(10))).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.and(JvmBigInt(BigInteger.valueOf(10)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.and(MockBigInt(JvmBigInt(BigInteger.valueOf(10)))).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.and((54).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.and((54).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.and((54).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.and(54L).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.and(BigInteger.valueOf(54)).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.and(JvmBigInt(BigInteger.valueOf(54))).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.and(JvmBigInt(BigInteger.valueOf(54)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.and(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.and(MockBigInt(JvmBigInt(BigInteger.valueOf(54)))).toString(10),
            )
        }
    }

    @Test
    fun `performs logical OR on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)
            
            jvmBigInt to bigInteger
        }
        
        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertEquals(
                bigInteger.or(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.or((0).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.or((0).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.or((0).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.or(0L).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.or(BigInteger.valueOf(0)).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.or(JvmBigInt(BigInteger.valueOf(0))).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.or(JvmBigInt(BigInteger.valueOf(0)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.or(MockBigInt(JvmBigInt(BigInteger.valueOf(0)))).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.or((10).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.or((10).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.or((10).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.or(10L).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.or(BigInteger.valueOf(10)).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.or(JvmBigInt(BigInteger.valueOf(10))).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.or(JvmBigInt(BigInteger.valueOf(10)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.or(MockBigInt(JvmBigInt(BigInteger.valueOf(10)))).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.or((54).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.or((54).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.or((54).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.or(54L).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.or(BigInteger.valueOf(54)).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.or(JvmBigInt(BigInteger.valueOf(54))).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.or(JvmBigInt(BigInteger.valueOf(54)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.or(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.or(MockBigInt(JvmBigInt(BigInteger.valueOf(54)))).toString(10),
            )
        }
    }

    @Test
    fun `performs logical XOR on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)

            jvmBigInt to bigInteger
        }

        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertEquals(
                bigInteger.xor(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.xor((0).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.xor((0).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.xor((0).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.xor(0L).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.xor(BigInteger.valueOf(0)).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.xor(JvmBigInt(BigInteger.valueOf(0))).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.xor(JvmBigInt(BigInteger.valueOf(0)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.xor(MockBigInt(JvmBigInt(BigInteger.valueOf(0)))).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.xor((10).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.xor((10).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.xor((10).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.xor(10L).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.xor(BigInteger.valueOf(10)).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.xor(JvmBigInt(BigInteger.valueOf(10))).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.xor(JvmBigInt(BigInteger.valueOf(10)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.xor(MockBigInt(JvmBigInt(BigInteger.valueOf(10)))).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.xor((54).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.xor((54).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.xor((54).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.xor(54L).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.xor(BigInteger.valueOf(54)).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.xor(JvmBigInt(BigInteger.valueOf(54))).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.xor(JvmBigInt(BigInteger.valueOf(54)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.xor(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.xor(MockBigInt(JvmBigInt(BigInteger.valueOf(54)))).toString(10),
            )
        }
    }

    @Test
    fun `performs logical NOT on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)

            jvmBigInt to bigInteger
        }

        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertEquals(
                bigInteger.not().toString(10),
                jvmBigInt.not().toString(10),
            )
        }
    }

    @Test
    fun `performs logical SHR on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)

            jvmBigInt to bigInteger
        }

        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertEquals(
                bigInteger.shr(0).toString(10),
                jvmBigInt.shr(0).toString(10),
            )

            assertEquals(
                bigInteger.shr(1).toString(10),
                jvmBigInt.shr(1).toString(10),
            )

            assertEquals(
                bigInteger.shr(6).toString(10),
                jvmBigInt.shr(6).toString(10),
            )
        }
    }

    @Test
    fun `performs logical SHL on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)

            jvmBigInt to bigInteger
        }

        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertEquals(
                bigInteger.shl(0).toString(10),
                jvmBigInt.shl(0).toString(10),
            )

            assertEquals(
                bigInteger.shl(1).toString(10),
                jvmBigInt.shl(1).toString(10),
            )

            assertEquals(
                bigInteger.shl(6).toString(10),
                jvmBigInt.shl(6).toString(10),
            )
        }
    }

    @Test
    fun `performs arithmetical PLUS on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)

            jvmBigInt to bigInteger
        }

        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertEquals(
                bigInteger.plus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.plus((0).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.plus((0).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.plus((0).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.plus(0L).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.plus(BigInteger.valueOf(0)).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.plus(JvmBigInt(BigInteger.valueOf(0))).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.plus(JvmBigInt(BigInteger.valueOf(0)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.plus(MockBigInt(JvmBigInt(BigInteger.valueOf(0)))).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.plus((10).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.plus((10).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.plus((10).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.plus(10L).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.plus(BigInteger.valueOf(10)).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.plus(JvmBigInt(BigInteger.valueOf(10))).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.plus(JvmBigInt(BigInteger.valueOf(10)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.plus(MockBigInt(JvmBigInt(BigInteger.valueOf(10)))).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.plus((54).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.plus((54).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.plus((54).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.plus(54L).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.plus(BigInteger.valueOf(54)).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.plus(JvmBigInt(BigInteger.valueOf(54))).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.plus(JvmBigInt(BigInteger.valueOf(54)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.plus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.plus(MockBigInt(JvmBigInt(BigInteger.valueOf(54)))).toString(10),
            )
        }
    }

    @Test
    fun `performs arithmetical MINUS on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)

            jvmBigInt to bigInteger
        }

        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertEquals(
                bigInteger.minus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.minus((0).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.minus((0).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.minus((0).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.minus(0L).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.minus(BigInteger.valueOf(0)).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.minus(JvmBigInt(BigInteger.valueOf(0))).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.minus(JvmBigInt(BigInteger.valueOf(0)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.minus(MockBigInt(JvmBigInt(BigInteger.valueOf(0)))).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.minus((10).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.minus((10).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.minus((10).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.minus(10L).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.minus(BigInteger.valueOf(10)).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.minus(JvmBigInt(BigInteger.valueOf(10))).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.minus(JvmBigInt(BigInteger.valueOf(10)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.minus(MockBigInt(JvmBigInt(BigInteger.valueOf(10)))).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.minus((54).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.minus((54).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.minus((54).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.minus(54L).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.minus(BigInteger.valueOf(54)).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.minus(JvmBigInt(BigInteger.valueOf(54))).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.minus(JvmBigInt(BigInteger.valueOf(54)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.minus(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.minus(MockBigInt(JvmBigInt(BigInteger.valueOf(54)))).toString(10),
            )
        }
    }

    @Test
    fun `performs arithmetical TIMES on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)

            jvmBigInt to bigInteger
        }

        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertEquals(
                bigInteger.times(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.times((0).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.times((0).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.times((0).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.times(0L).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.times(BigInteger.valueOf(0)).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.times(JvmBigInt(BigInteger.valueOf(0))).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.times(JvmBigInt(BigInteger.valueOf(0)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(0)).toString(10),
                jvmBigInt.times(MockBigInt(JvmBigInt(BigInteger.valueOf(0)))).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.times((10).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.times((10).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.times((10).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.times(10L).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.times(BigInteger.valueOf(10)).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.times(JvmBigInt(BigInteger.valueOf(10))).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.times(JvmBigInt(BigInteger.valueOf(10)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.times(MockBigInt(JvmBigInt(BigInteger.valueOf(10)))).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.times((54).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.times((54).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.times((54).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.times(54L).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.times(BigInteger.valueOf(54)).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.times(JvmBigInt(BigInteger.valueOf(54))).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.times(JvmBigInt(BigInteger.valueOf(54)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.times(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.times(MockBigInt(JvmBigInt(BigInteger.valueOf(54)))).toString(10),
            )
        }
    }

    @Test
    fun `performs arithmetical DIV on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)

            jvmBigInt to bigInteger
        }

        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertFailsWith<ArithmeticException> {
                jvmBigInt.div((0).toByte())
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.div((0).toShort())
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.div((0).toInt())
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.div(0L)
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.div(BigInteger.valueOf(0))
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.div(JvmBigInt(BigInteger.valueOf(0)))
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.div(JvmBigInt(BigInteger.valueOf(0)) as BigInt)
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.div(MockBigInt(JvmBigInt(BigInteger.valueOf(0))))
            }

            assertEquals(
                bigInteger.div(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.div((10).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.div((10).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.div((10).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.div(10L).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.div(BigInteger.valueOf(10)).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.div(JvmBigInt(BigInteger.valueOf(10))).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.div(JvmBigInt(BigInteger.valueOf(10)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.div(MockBigInt(JvmBigInt(BigInteger.valueOf(10)))).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.div((54).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.div((54).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.div((54).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.div(54L).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.div(BigInteger.valueOf(54)).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.div(JvmBigInt(BigInteger.valueOf(54))).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.div(JvmBigInt(BigInteger.valueOf(54)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.div(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.div(MockBigInt(JvmBigInt(BigInteger.valueOf(54)))).toString(10),
            )
        }
    }

    @Test
    fun `performs arithmetical DIV with RoudingMode on JvmBigInt`() {
        val jvmBigInt_1 = JvmBigInt(BigInteger.valueOf(1))

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toByte(), roundingMode = BigInt.RoundingMode.Up)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toByte(), roundingMode = BigInt.RoundingMode.Down)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toByte(), roundingMode = BigInt.RoundingMode.Ceiling)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toByte(), roundingMode = BigInt.RoundingMode.Floor)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toShort(), roundingMode = BigInt.RoundingMode.Up)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toShort(), roundingMode = BigInt.RoundingMode.Down)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toShort(), roundingMode = BigInt.RoundingMode.Ceiling)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toShort(), roundingMode = BigInt.RoundingMode.Floor)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toInt(), roundingMode = BigInt.RoundingMode.Up)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toInt(), roundingMode = BigInt.RoundingMode.Down)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toInt(), roundingMode = BigInt.RoundingMode.Ceiling)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div((0).toInt(), roundingMode = BigInt.RoundingMode.Floor)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(0L, roundingMode = BigInt.RoundingMode.Up)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(0L, roundingMode = BigInt.RoundingMode.Down)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(0L, roundingMode = BigInt.RoundingMode.Ceiling)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(0L, roundingMode = BigInt.RoundingMode.Floor)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(BigInteger.valueOf(0), roundingMode = BigInt.RoundingMode.Up)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(BigInteger.valueOf(0), roundingMode = BigInt.RoundingMode.Down)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(BigInteger.valueOf(0), roundingMode = BigInt.RoundingMode.Ceiling)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(BigInteger.valueOf(0), roundingMode = BigInt.RoundingMode.Floor)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(0)), roundingMode = BigInt.RoundingMode.Up)
        }
        
        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(0)), roundingMode = BigInt.RoundingMode.Down)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(0)), roundingMode = BigInt.RoundingMode.Ceiling)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(0)), roundingMode = BigInt.RoundingMode.Floor)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(0)) as BigInt, roundingMode = BigInt.RoundingMode.Up)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(0)) as BigInt, roundingMode = BigInt.RoundingMode.Down)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(0)) as BigInt, roundingMode = BigInt.RoundingMode.Ceiling)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(0)) as BigInt, roundingMode = BigInt.RoundingMode.Floor)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(0))), roundingMode = BigInt.RoundingMode.Up)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(0))), roundingMode = BigInt.RoundingMode.Down)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(0))), roundingMode = BigInt.RoundingMode.Ceiling)
        }

        assertFailsWith<ArithmeticException> {
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(0))), roundingMode = BigInt.RoundingMode.Floor)
        }

        assertEquals(
            1,
            jvmBigInt_1.div((10).toByte(), roundingMode = BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((10).toByte(), roundingMode = BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div((10).toByte(), roundingMode = BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((10).toByte(), roundingMode = BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div((10).toShort(), roundingMode = BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((10).toShort(), roundingMode = BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div((10).toShort(), roundingMode = BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((10).toShort(), roundingMode = BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div((10).toInt(), roundingMode = BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((10).toInt(), roundingMode = BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div((10).toInt(), roundingMode = BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((10).toInt(), roundingMode = BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div(10L, roundingMode = BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(10L, roundingMode = BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div(10L, roundingMode = BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(10L, roundingMode = BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div(BigInteger.valueOf(10), BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(BigInteger.valueOf(10), BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div(BigInteger.valueOf(10), BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(BigInteger.valueOf(10), BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(10)), BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(10)), BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(10)), BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(10)), BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(10)) as BigInt, BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(10)) as BigInt, BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(10)) as BigInt, BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(10)) as BigInt, BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(10))), BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(10))), BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            1,
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(10))), BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(10))), BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div((-10).toByte(), roundingMode = BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((-10).toByte(), roundingMode = BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((-10).toByte(), roundingMode = BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div((-10).toByte(), roundingMode = BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div((-10).toShort(), roundingMode = BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((-10).toShort(), roundingMode = BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((-10).toShort(), roundingMode = BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div((-10).toShort(), roundingMode = BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div((-10).toInt(), roundingMode = BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((-10).toInt(), roundingMode = BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div((-10).toInt(), roundingMode = BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div((-10).toInt(), roundingMode = BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div(-10L, roundingMode = BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(-10L, roundingMode = BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(-10L, roundingMode = BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div(-10L, roundingMode = BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div(BigInteger.valueOf(-10), BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(BigInteger.valueOf(-10), BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(BigInteger.valueOf(-10), BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div(BigInteger.valueOf(-10), BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(-10)), BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(-10)), BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(-10)), BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(-10)), BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(-10)) as BigInt, BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(-10)) as BigInt, BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(-10)) as BigInt, BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div(JvmBigInt(BigInteger.valueOf(-10)) as BigInt, BigInt.RoundingMode.Floor).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(-10))), BigInt.RoundingMode.Up).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(-10))), BigInt.RoundingMode.Down).toInt(),
        )

        assertEquals(
            0,
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(-10))), BigInt.RoundingMode.Ceiling).toInt(),
        )

        assertEquals(
            -1,
            jvmBigInt_1.div(MockBigInt(JvmBigInt(BigInteger.valueOf(-10))), BigInt.RoundingMode.Floor).toInt(),
        )
    }

    @Test
    fun `performs arithmetical REM on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)

            jvmBigInt to bigInteger
        }

        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertFailsWith<ArithmeticException> {
                jvmBigInt.rem((0).toByte())
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.rem((0).toShort())
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.rem((0).toInt())
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.rem(0L)
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.rem(BigInteger.valueOf(0))
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.rem(JvmBigInt(BigInteger.valueOf(0)))
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.rem(JvmBigInt(BigInteger.valueOf(0)) as BigInt)
            }

            assertFailsWith<ArithmeticException> {
                jvmBigInt.rem(MockBigInt(JvmBigInt(BigInteger.valueOf(0))))
            }

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.rem((10).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.rem((10).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.rem((10).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.rem(10L).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.rem(BigInteger.valueOf(10)).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.rem(JvmBigInt(BigInteger.valueOf(10))).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.rem(JvmBigInt(BigInteger.valueOf(10)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(10)).toString(10),
                jvmBigInt.rem(MockBigInt(JvmBigInt(BigInteger.valueOf(10)))).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.rem((54).toByte()).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.rem((54).toShort()).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.rem((54).toInt()).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.rem(54L).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.rem(BigInteger.valueOf(54)).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.rem(JvmBigInt(BigInteger.valueOf(54))).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.rem(JvmBigInt(BigInteger.valueOf(54)) as BigInt).toString(10),
            )

            assertEquals(
                bigInteger.rem(BigInteger.valueOf(54)).toString(10),
                jvmBigInt.rem(MockBigInt(JvmBigInt(BigInteger.valueOf(54)))).toString(10),
            )
        }
    }

    @Test
    fun `performs logical ABS on JvmBigInt`() {
        val bigIntsWithBigIntegers = (1..3).map {
            val bigInteger = BigInteger.valueOf(Random.nextLong())
            val jvmBigInt = JvmBigInt(bigInteger)

            jvmBigInt to bigInteger
        }

        bigIntsWithBigIntegers.forEach { (jvmBigInt, bigInteger) ->
            assertEquals(
                bigInteger.abs().toString(10),
                jvmBigInt.abs().toString(10),
            )
        }
    }

    @Test
    fun `checks JvmBigInt equality`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val byte = it.toByte()
            val short = it.toShort()
            val int = it.toInt()
            val long = it

            val jvmByteBigInt = JvmBigInt(BigInteger.valueOf(byte.toLong()))
            val jvmShortBigInt = JvmBigInt(BigInteger.valueOf(short.toLong()))
            val jvmIntBigInt = JvmBigInt(BigInteger.valueOf(int.toLong()))
            val jvmLongBigInt = JvmBigInt(BigInteger.valueOf(long))

            assertEquals(jvmLongBigInt, JvmBigInt(BigInteger.valueOf(long)))

            assertTrue(jvmByteBigInt.equals(byte), "Expected $jvmByteBigInt to equal (Byte) $byte")
            assertFalse(jvmByteBigInt.equals(byte - 1), "Expected $jvmByteBigInt not to equal (Byte) ${byte - 1}")

            assertTrue(jvmShortBigInt.equals(short), "Expected $jvmShortBigInt to equal (Short) $short")
            assertFalse(jvmShortBigInt.equals(short - 1), "Expected $jvmShortBigInt not to equal (Short) ${short - 1}")

            assertTrue(jvmIntBigInt.equals(int), "Expected $jvmIntBigInt to equal (Int) $int")
            assertFalse(jvmIntBigInt.equals(int - 1), "Expected $jvmIntBigInt not to equal (Int) ${int - 1}")

            assertTrue(jvmLongBigInt.equals(long), "Expected $jvmLongBigInt to equal (Long) $long")
            assertFalse(jvmLongBigInt.equals(long - 1), "Expected $jvmLongBigInt not to equal (Long) ${long - 1}")

            assertFalse(jvmLongBigInt.equals(long.toDouble()), "Expected $jvmLongBigInt not to equal (Double) ${long.toDouble()}")
            assertFalse(jvmLongBigInt.equals(null), "Expected $jvmLongBigInt not to equal null")

            assertEquals(jvmLongBigInt.hashCode(), BigInteger.valueOf(long).hashCode())
        }
    }

    @Test
    fun `converts JvmBigInt to Byte`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val byte = it.toByte()
            val jvmBigInt = JvmBigInt(BigInteger.valueOf(byte.toLong()))

            assertEquals(byte, jvmBigInt.toByte())
        }
    }

    @Test
    fun `converts JvmBigInt to exact Byte value or fails in case of overflow`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val byte = it.toByte()

            val jvmByteBigInt = JvmBigInt(BigInteger.valueOf(byte.toLong()))
            val jvmLongBigInt = JvmBigInt(BigInteger.valueOf(it))

            assertEquals(byte, jvmByteBigInt.toByteExact())
            assertFailsWith<ArithmeticException> {
                jvmLongBigInt.toByteExact()
            }
        }
    }

    @Test
    fun `converts JvmBigInt to exact UByte value or fails in case of overflow`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val byte = it.toByte()

            val jvmByteBigInt = JvmBigInt(BigInteger.valueOf(byte.toLong()))
            val jvmLongBigInt = JvmBigInt(BigInteger.valueOf(it))

            assertEquals(byte.toUByte(), jvmByteBigInt.toUByteExact())
            assertFailsWith<ArithmeticException> {
                jvmLongBigInt.toUByteExact()
            }
        }
    }

    @Test
    fun `converts JvmBigInt to Short`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val short = it.toShort()
            val jvmBigInt = JvmBigInt(BigInteger.valueOf(short.toLong()))

            assertEquals(short, jvmBigInt.toShort())
        }
    }

    @Test
    fun `converts JvmBigInt to exact Short value or fails in case of overflow`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val short = it.toShort()

            val jvmShortBigInt = JvmBigInt(BigInteger.valueOf(short.toLong()))
            val jvmLongBigInt = JvmBigInt(BigInteger.valueOf(it))

            assertEquals(short, jvmShortBigInt.toShortExact())
            assertFailsWith<ArithmeticException> {
                jvmLongBigInt.toShortExact()
            }
        }
    }

    @Test
    fun `converts JvmBigInt to exact UShort value or fails in case of overflow`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val short = it.toShort()

            val jvmShortBigInt = JvmBigInt(BigInteger.valueOf(short.toLong()))
            val jvmLongBigInt = JvmBigInt(BigInteger.valueOf(it))

            assertEquals(short.toUShort(), jvmShortBigInt.toUShortExact())
            assertFailsWith<ArithmeticException> {
                jvmLongBigInt.toUShortExact()
            }
        }
    }

    @Test
    fun `converts JvmBigInt to Int`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val int = it.toInt()
            val jvmBigInt = JvmBigInt(BigInteger.valueOf(int.toLong()))

            assertEquals(int, jvmBigInt.toInt())
        }
    }

    @Test
    fun `converts JvmBigInt to exact Int value or fails in case of overflow`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val int = it.toInt()

            val jvmIntBigInt = JvmBigInt(BigInteger.valueOf(int.toLong()))
            val jvmLongBigInt = JvmBigInt(BigInteger.valueOf(it))

            assertEquals(int, jvmIntBigInt.toIntExact())
            assertFailsWith<ArithmeticException> {
                jvmLongBigInt.toIntExact()
            }
        }
    }

    @Test
    fun `converts JvmBigInt to exact UInt value or fails in case of overflow`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val int = it.toInt()

            val jvmIntBigInt = JvmBigInt(BigInteger.valueOf(int.toLong()))
            val jvmLongBigInt = JvmBigInt(BigInteger.valueOf(it))

            assertEquals(int.toUInt(), jvmIntBigInt.toUIntExact())
            assertFailsWith<ArithmeticException> {
                jvmLongBigInt.toUIntExact()
            }
        }
    }

    @Test
    fun `converts JvmBigInt to Long`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val jvmBigInt = JvmBigInt(BigInteger.valueOf(it))

            assertEquals(it, jvmBigInt.toLong())
        }
    }

    @Test
    fun `converts JvmBigInt to exact Long value or fails in case of overflow`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val jvmExactBigInt = JvmBigInt(BigInteger.valueOf(it))
            val jvmOverflownBigInt = JvmBigInt(BigInteger.valueOf(Long.MAX_VALUE)) + 1

            assertEquals(it, jvmExactBigInt.toLongExact())
            assertFailsWith<ArithmeticException> {
                jvmOverflownBigInt.toByteExact()
            }
        }
    }

    @Test
    fun `converts JvmBigInt to exact ULong value or fails in case of overflow`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val jvmExactBigInt = JvmBigInt(BigInteger.valueOf(it))
            val jvmOverflownBigInt = JvmBigInt(BigInteger.valueOf(Long.MAX_VALUE)) + 1

            assertEquals(it.toULong(), jvmExactBigInt.toULongExact())
            assertFailsWith<ArithmeticException> {
                jvmOverflownBigInt.toULongExact()
            }
        }
    }

    @Test
    fun `converts JvmBigInt to ByteArray`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val bigInteger = BigInteger.valueOf(it)
            val jvmBigInt = JvmBigInt(bigInteger)

            assertContentEquals(bigInteger.toByteArray(), jvmBigInt.toByteArray())
        }
    }

    @Test
    fun `converts JvmBigInt to String`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val bigInteger = BigInteger.valueOf(it)
            val jvmBigInt = JvmBigInt(bigInteger)

            assertEquals(bigInteger.toString(), jvmBigInt.toString())
            assertEquals(bigInteger.toString(2), jvmBigInt.toString(2))
            assertEquals(bigInteger.toString(3), jvmBigInt.toString(3))
            assertEquals(bigInteger.toString(10), jvmBigInt.toString(10))
            assertEquals(bigInteger.toString(16), jvmBigInt.toString(16))
        }
    }

    @Test
    fun `compares JvmBigInts`() {
        val longs = (1..3).map { Random.nextLong() }

        longs.forEach {
            val jvmBigInt = JvmBigInt(BigInteger.valueOf(it))

            assertTrue(jvmBigInt > JvmBigInt(BigInteger.valueOf(it - 1)))
            assertTrue(jvmBigInt >= JvmBigInt(BigInteger.valueOf(it - 1)))
            assertTrue(jvmBigInt >= JvmBigInt(BigInteger.valueOf(it)))

            assertTrue(jvmBigInt < JvmBigInt(BigInteger.valueOf(it + 1)))
            assertTrue(jvmBigInt <= JvmBigInt(BigInteger.valueOf(it + 1)))
            assertTrue(jvmBigInt <= JvmBigInt(BigInteger.valueOf(it)))

            assertTrue(jvmBigInt > MockBigInt(JvmBigInt(BigInteger.valueOf(it - 1))))
            assertTrue(jvmBigInt >= MockBigInt(JvmBigInt(BigInteger.valueOf(it - 1))))
            assertTrue(jvmBigInt >= MockBigInt(JvmBigInt(BigInteger.valueOf(it))))

            assertTrue(jvmBigInt < MockBigInt(JvmBigInt(BigInteger.valueOf(it + 1))))
            assertTrue(jvmBigInt <= MockBigInt(JvmBigInt(BigInteger.valueOf(it + 1))))
            assertTrue(jvmBigInt <= MockBigInt(JvmBigInt(BigInteger.valueOf(it))))
        }
    }
    
    private class MockBigInt(other: BigInt) : BigInt by other
}