package it.airgap.tezos.core.type

import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * Timestamp representations supported in Tezos
 */
public sealed interface Timestamp {

    /**
     * Converts this [Timestamp] to the [RFC 3339][Timestamp.Rfc3339] format.
     */
    public fun toRfc3339(): Rfc3339

    /**
     * Converts this [Timestamp] to [milliseconds][Timestamp.Millis].
     */
    public fun toMillis(): Millis

    /**
     * The RFC 3339 timestamp representation.
     *
     * @property dateString The RFC 3339 formatted date.
     */
    @JvmInline
    public value class Rfc3339(public val dateString: String) : Timestamp {
        init {
            require(isValid(dateString)) { "Invalid RFC 3339 date string." }
        }

        /**
         * Converts this [Timestamp.Rfc3339] to the [RFC 3339][Timestamp.Rfc3339] format.
         */
        override fun toRfc3339(): Rfc3339 = this

        /**
         * Converts this [Timestamp.Rfc3339] to [milliseconds][Timestamp.Millis].
         */
        override fun toMillis(): Millis {
            // replace with https://github.com/Kotlin/kotlinx-datetime once it's stable
            val long = Instant.parse(dateString).toEpochMilli()

            return Millis(long)
        }

        public companion object {
            /**
             * Checks if [string] is a valid [Timestamp.Rfc3339] value.
             */
            public fun isValid(string: String): Boolean =
                Regex("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?(Z|[\\+-]\\d{2}:\\d{2})?$").matches(string)
        }
    }

    /**
     * Timestamp in milliseconds.
     *
     * @property long An integer that represents the number of seconds elapsed since January 1st 1970.
     */
    @JvmInline
    public value class Millis(public val long: Long) : Timestamp {

        /**
         * Converts this [Timestamp.Millis] to the [RFC 3339][Timestamp.Rfc3339] format.
         */
        override fun toRfc3339(): Rfc3339 {
            // replace with https://github.com/Kotlin/kotlinx-datetime once it's stable
            val instant = Instant.ofEpochMilli(long)
            val dateString = DateTimeFormatter.ISO_INSTANT.format(instant)

            return Rfc3339(dateString)
        }

        /**
         * Converts this [Timestamp.Millis] to [milliseconds][Timestamp.Millis].
         */
        override fun toMillis(): Millis = this

        public companion object {}
    }

    public companion object {
        /**
         * Creates [Timestamp] from [String][string].
         *
         * @throws IllegalArgumentException if [string] is an invalid [Timestamp] value.
         */
        public fun fromString(string: String): Timestamp = fromStringOrNull(string) ?: failWithInvalidRfc3339(string)

        /**
         * Creates [Timestamp] from [String][string] or returns `null` if the value is not a valid [Timestamp].
         */
        public fun fromStringOrNull(string: String): Timestamp? = if (Rfc3339.isValid(string)) Rfc3339(string) else null

        /**
         * Creates [Timestamp] from [Long][millis].
         */
        public fun fromMillis(millis: Long): Timestamp = Millis(millis)
    }
}

private fun failWithInvalidRfc3339(string: String): Nothing = failWithIllegalArgument("$string is not a valid RFC 3339 date string.")