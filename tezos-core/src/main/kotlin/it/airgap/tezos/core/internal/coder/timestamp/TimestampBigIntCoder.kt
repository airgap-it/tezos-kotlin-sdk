package it.airgap.tezos.core.internal.coder.timestamp

import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.Timestamp

internal class TimestampBigIntCoder : Coder<Timestamp, BigInt> {
    override fun encode(value: Timestamp): BigInt {
        val long = value.toMillis().long
        return BigInt.valueOf(long)
    }

    override fun decode(value: BigInt): Timestamp {
        try {
            val long = value.toLongExact()
            return Timestamp.Millis(long).toRfc3339()
        } catch (e: ArithmeticException) {
            failWithInvalidTimestamp(value)
        }
    }

    private fun failWithInvalidTimestamp(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos timestamp.")
    private fun failWithInvalidTimestamp(value: BigInt): Nothing = failWithInvalidTimestamp(value.toString())
}