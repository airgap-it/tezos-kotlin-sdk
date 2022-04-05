package it.airgap.tezos.core.type

import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import java.time.Instant
import java.time.format.DateTimeFormatter

public sealed interface Timestamp {

    public fun toRfc3339(): Rfc3339
    public fun toMillis(): Millis

    @JvmInline
    public value class Rfc3339(public val dateString: String) : Timestamp {
        init {
            require(isValid(dateString)) { "Invalid RFC 3339 date string." }
        }

        override fun toRfc3339(): Rfc3339 = this
        override fun toMillis(): Millis {
            // replace with https://github.com/Kotlin/kotlinx-datetime once it's stable
            val long = Instant.parse(dateString).toEpochMilli()

            return Millis(long)
        }

        public companion object {
            public fun isValid(string: String): Boolean =
                Regex("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?(Z|[\\+-]\\d{2}:\\d{2})?$").matches(string)
        }
    }

    @JvmInline
    public value class Millis(public val long: Long) : Timestamp {

        override fun toRfc3339(): Rfc3339 {
            // replace with https://github.com/Kotlin/kotlinx-datetime once it's stable
            val instant = Instant.ofEpochMilli(long)
            val dateString = DateTimeFormatter.ISO_INSTANT.format(instant)

            return Rfc3339(dateString)
        }

        override fun toMillis(): Millis = this

        public companion object {}
    }

    public companion object {
        public fun fromString(string: String): Timestamp = fromStringOrNull(string) ?: failWithInvalidRfc3339(string)
        public fun fromStringOrNull(string: String): Timestamp? = if (Rfc3339.isValid(string)) Rfc3339(string) else null

        public fun fromMillis(millis: Long): Timestamp = Millis(millis)
    }
}

private fun failWithInvalidRfc3339(string: String): Nothing =
    failWithIllegalArgument("$string is not a valid RFC 3339 date string.")