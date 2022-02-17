package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.coder.TimestampBigIntCoder
import it.airgap.tezos.core.internal.coder.ZarithIntegerBytesCoder
import it.airgap.tezos.core.internal.coder.ZarithNaturalBytesCoder
import it.airgap.tezos.core.internal.crypto.Crypto
import kotlin.reflect.KClass

@InternalTezosSdkApi
public interface DependencyRegistry {

    // -- scoped --

    public val scoped: Map<String, DependencyRegistry>
    public fun addScoped(scoped: DependencyRegistry)
    public fun <T : DependencyRegistry> findScoped(targetClass: KClass<T>): T?

    // -- base58 --

    public val base58: Base58
    public val base58Check: Base58Check

    // -- crypto --

    public val crypto: Crypto

    // -- coder --

    public val timestampBigIntCoder: TimestampBigIntCoder
}

public inline fun <reified T : DependencyRegistry> DependencyRegistry.findScoped(): T? = findScoped(T::class)