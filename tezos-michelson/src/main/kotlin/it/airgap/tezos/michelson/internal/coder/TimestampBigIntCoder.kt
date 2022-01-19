package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.type.BigInt
import java.time.Instant

internal class TimestampBigIntCoder : Coder<String, BigInt> {
    override fun encode(value: String): BigInt {
        val timestamp = dateToTimestamp(value)
        return BigInt.valueOf(timestamp)
    }

    override fun decode(value: BigInt): String {
        TODO("Not yet implemented")
    }

    private fun dateToTimestamp(value: String): Long {
        // replace with https://github.com/Kotlin/kotlinx-datetime once it's stable
        return Instant.parse(value).toEpochMilli()
    }
}