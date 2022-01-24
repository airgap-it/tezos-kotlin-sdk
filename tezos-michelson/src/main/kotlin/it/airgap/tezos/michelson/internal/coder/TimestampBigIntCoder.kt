package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import java.time.DateTimeException
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

internal class TimestampBigIntCoder : Coder<String, BigInt> {
    override fun encode(value: String): BigInt {
        val timestamp = dateToTimestamp(value)
        return BigInt.valueOf(timestamp)
    }

    override fun decode(value: BigInt): String {
        try {
            val timestamp = value.toLongExact()
            return timestampToDate(timestamp)
        } catch (e: ArithmeticException) {
            failWithInvalidTimestamp(value)
        }
    }

    private fun dateToTimestamp(value: String): Long {
        // replace with https://github.com/Kotlin/kotlinx-datetime once it's stable
        try {
            return Instant.parse(value).toEpochMilli()
        } catch (e: DateTimeParseException) {
            failWithInvalidTimestamp(value)
        }
    }

    private fun timestampToDate(value: Long): String {
        // replace with https://github.com/Kotlin/kotlinx-datetime once it's stable
        try {
            val instant = Instant.ofEpochMilli(value)
            return DateTimeFormatter.ISO_INSTANT.format(instant)
        } catch (e: DateTimeException) {
            failWithInvalidTimestamp(value)
        }
    }

    private fun failWithInvalidTimestamp(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos timestamp.")
    private fun failWithInvalidTimestamp(value: BigInt): Nothing = failWithInvalidTimestamp(value.toString())
    private fun failWithInvalidTimestamp(value: Long): Nothing = failWithInvalidTimestamp(value.toString())
}