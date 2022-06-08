package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.number.TezosInteger
import it.airgap.tezos.core.type.number.TezosNatural

@InternalTezosSdkApi
public interface NumberUtilsContext {
    public fun BigInt.toTezosInteger(): TezosInteger = TezosInteger(toString(10))
    public fun BigInt.toTezosNatural(): TezosNatural = TezosNatural(toString(10))

    public fun String.asTezosInteger(): TezosInteger = asTezosIntegerOrNull() ?: failWithInvalidTezosInteger(this)
    public fun String.asTezosIntegerOrNull(): TezosInteger? = if (TezosInteger.isValid(this)) TezosInteger(this) else null

    public fun String.asTezosNatural(): TezosNatural = asTezosNaturalOrNull() ?: failWithInvalidTezosNatural(this)
    public fun String.asTezosNaturalOrNull(): TezosNatural? = if (TezosNatural.isValid(this)) TezosNatural(this) else null

    public fun TezosInteger.toBigInt(): BigInt = BigInt.valueOf(int)
    public fun TezosNatural.toBigInt(): BigInt = BigInt.valueOf(nat)

    public fun Byte.toBigInt(): BigInt = BigInt.valueOf(this)
    public fun UByte.toBigInt(): BigInt = BigInt.valueOf(this)

    public fun Short.toBigInt(): BigInt = BigInt.valueOf(this)
    public fun UShort.toBigInt(): BigInt = BigInt.valueOf(this)

    public fun Int.toBigInt(): BigInt = BigInt.valueOf(this)
    public fun UInt.toBigInt(): BigInt = BigInt.valueOf(this)

    public fun Long.toBigInt(): BigInt = BigInt.valueOf(this)
    public fun ULong.toBigInt(): BigInt = BigInt.valueOf(this)

    private fun failWithInvalidTezosInteger(string: String): Nothing = failWithIllegalArgument("$string is not a valid Tezos integer")
    private fun failWithInvalidTezosNatural(string: String): Nothing = failWithIllegalArgument("$string is not a valid Tezos natural number")
}