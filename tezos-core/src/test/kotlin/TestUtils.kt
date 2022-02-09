
// -- extensions --

fun <T> String.inRange(range: ClosedRange<T>): Boolean where T : Comparable<T> {
    val maxNumericIfNegative = range.start.toString().removePrefix("-")
    val maxNumericIfPositive = range.endInclusive.toString()

    val (abs, max) = if (startsWith("-")) {
        Pair(removePrefix("-"), maxNumericIfNegative)
    } else {
        Pair(this, maxNumericIfPositive)
    }

    return if (abs.length == max.length) abs <= max
    else abs.length < max.length
}
