package it.airgap.tezos.michelson.internal.coder

internal interface Coder<T, S> {
    fun encode(value: T): S
    fun decode(value: S): T
}