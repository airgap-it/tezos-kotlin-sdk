package it.airgap.tezos.core.internal.module

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.DependencyRegistry
import kotlin.reflect.KClass

public interface TezosModule {
    public interface Builder<T : TezosModule> {

        @InternalTezosSdkApi
        public val moduleDependencies: Set<KClass<out TezosModule>>

        @InternalTezosSdkApi
        public fun build(dependencyRegistry: DependencyRegistry, modules: List<TezosModule>): T
    }
}
