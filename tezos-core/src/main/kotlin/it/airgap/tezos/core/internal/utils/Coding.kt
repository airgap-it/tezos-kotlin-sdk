package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.TezosCoreContext.asInt16Encoded
import it.airgap.tezos.core.internal.context.TezosCoreContext.asInt32Encoded
import it.airgap.tezos.core.internal.context.TezosCoreContext.asInt64Encoded
import it.airgap.tezos.core.internal.context.TezosCoreContext.consumeAt
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.HexString

@InternalTezosSdkApi
public interface CodingUtilsContext {

    // -- String <-> ByteArray

    public fun String.encodeToBytes(): ByteArray = toByteArray(charset = Charsets.UTF_8)
    public fun MutableList<Byte>.decodeConsumingString(size: Int): String = consumeAt(0 until size).toByteArray().decodeToString()

    public fun HexString.encodeToBytes(): ByteArray = toByteArray()
    public fun MutableList<Byte>.decodeConsumingHexString(size: Int): HexString = consumeAt(0 until size).toHexString()

    // -- Number <-> ByteArray

    public fun UByte.encodeToBytes(): ByteArray = byteArrayOf(toByte())
    public fun MutableList<Byte>.decodeConsumingUInt8(): UByte? = consumeAt(0)?.toUByte()

    public fun UShort.encodeToBytes(): ByteArray = BigInt.valueOf(toLong()).toHexString().toByteArray().asInt16Encoded()
    public fun MutableList<Byte>.decodeConsumingUInt16(): UShort = BigInt.valueOf(consumeAt(0 until 2).toByteArray()).toShortExact().toUShort()


    public fun Int.encodeToBytes(): ByteArray = BigInt.valueOf(this).toByteArray().asInt32Encoded(fillValue = if (this >= 0) 0U else 255U)
    public fun MutableList<Byte>.decodeConsumingInt32(): Int = BigInt.valueOf(consumeAt(0 until 4).toByteArray()).toIntExact()


    public fun Long.encodeToBytes(): ByteArray = BigInt.valueOf(this).toByteArray().asInt64Encoded(fillValue = if (this >= 0) 0U else 255U)
    public fun MutableList<Byte>.decodeConsumingInt64(): Long = BigInt.valueOf(consumeAt(0 until 8).toByteArray()).toLongExact()


    // -- Boolean <-> ByteArray --

    public fun Boolean.encodeToBytes(): ByteArray = if (this) byteArrayOf((255).toByte()) else byteArrayOf(0)
    public fun MutableList<Byte>.decodeConsumingBoolean(): Boolean? =
        when (consumeAt(0)?.toUByte()) {
            (255).toUByte() -> true
            (0).toUByte() -> false
            else -> null
        }

    // -- List <-> ByteArray --

    public fun <T> List<T>.encodeToBytes(encoder: (T) -> ByteArray): ByteArray = fold(byteArrayOf()) { acc, t -> acc + encoder(t) }
    public fun <T> MutableList<Byte>.decodeConsumingList(decoded: List<T> = emptyList(), decoder: (MutableList<Byte>) -> T): List<T> =
        decodeTailrecConsumingListFromBytes(this, decoded, decoder)
}

// -- private --

private tailrec fun <T> decodeTailrecConsumingListFromBytes(
    value: MutableList<Byte>,
    decoded: List<T> = emptyList(),
    decoder: (MutableList<Byte>) -> T,
): List<T> = withTezosContext {
    if (value.isEmpty()) return decoded
    val item = decoder(value)

    return decodeTailrecConsumingListFromBytes(value, decoded + item, decoder)
}