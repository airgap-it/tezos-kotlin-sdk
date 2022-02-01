package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.coder.*
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.delegate.lazyWeak
import kotlin.reflect.KClass

internal class CoreDependencyRegistry(private val cryptoProvider: CryptoProvider) : DependencyRegistry {

    // -- scoped --

    private val _scoped: MutableMap<String, DependencyRegistry> = mutableMapOf()
    override val scoped: Map<String, DependencyRegistry>
        get() = _scoped

    override fun addScoped(scoped: DependencyRegistry) {
        val key = scoped::class.simpleName ?: return
        _scoped[key] = scoped
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : DependencyRegistry> findScoped(targetClass: KClass<T>): T? {
        val key = targetClass.simpleName ?: return null
        return scoped[key] as T?
    }

    // -- base58 --

    override val base58: Base58 by lazyWeak { Base58() }
    override val base58Check: Base58Check by lazyWeak { Base58Check(base58, crypto) }

    // -- crypto --

    override val crypto: Crypto by lazyWeak { Crypto(cryptoProvider) }

    // -- coder --

    override val base58BytesCoder: Base58BytesCoder by lazy { Base58BytesCoder(base58Check) }
    override val addressBytesCoder: AddressBytesCoder by lazy { AddressBytesCoder(keyHashBytesCoder, base58BytesCoder) }
    override val keyBytesCoder: KeyBytesCoder by lazy { KeyBytesCoder(base58BytesCoder) }
    override val keyHashBytesCoder: KeyHashBytesCoder by lazy { KeyHashBytesCoder(base58BytesCoder) }
    override val signatureBytesCoder: SignatureBytesCoder by lazy { SignatureBytesCoder(base58BytesCoder) }
    override val zarithNaturalNumberBytesCoder: ZarithNaturalNumberBytesCoder = ZarithNaturalNumberBytesCoder()
    override val zarithIntegerBytesCoder: ZarithIntegerBytesCoder by lazy { ZarithIntegerBytesCoder(zarithNaturalNumberBytesCoder) }

    override val timestampBigIntCoder: TimestampBigIntCoder = TimestampBigIntCoder()
}