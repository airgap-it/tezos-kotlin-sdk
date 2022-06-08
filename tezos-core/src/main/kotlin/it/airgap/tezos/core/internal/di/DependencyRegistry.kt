package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.context.TezosCoreContext.lazyWeak
import it.airgap.tezos.core.internal.crypto.Crypto

@InternalTezosSdkApi
public class DependencyRegistry(cryptoProvider: CryptoProvider) {

    // -- base58 --

    public val base58: Base58 by lazyWeak { Base58() }
    public val base58Check: Base58Check by lazyWeak { Base58Check(base58, crypto) }

    // -- crypto --

    public val crypto: Crypto by lazyWeak { Crypto(cryptoProvider) }
}