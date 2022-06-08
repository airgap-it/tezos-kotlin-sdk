package it.airgap.tezos.core.internal.module

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.DependencyRegistry

public interface TezosModule {
    public interface Builder<T : TezosModule> {
        @InternalTezosSdkApi
        public fun build(dependencyRegistry: DependencyRegistry, moduleRegistry: ModuleRegistry): T
    }
}
