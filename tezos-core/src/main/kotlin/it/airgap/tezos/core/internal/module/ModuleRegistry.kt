package it.airgap.tezos.core.internal.module

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import kotlin.reflect.KClass

@InternalTezosSdkApi
public class ModuleRegistry(modules: List<TezosModule>) {
    private val modules: MutableMap<String, TezosModule> = mutableMapOf()

    init {
        modules.forEach { registerModule(it) }
    }

    @InternalTezosSdkApi
    public fun registerModule(module: TezosModule) {
        val key = module::class.qualifiedName ?: return
        modules[key] = module
    }

    @InternalTezosSdkApi
    public inline fun <reified T : TezosModule> findModule(): T? = findModule(T::class)

    @PublishedApi
    @Suppress("UNCHECKED_CAST")
    internal fun <T : TezosModule> findModule(targetClass: KClass<T>): T? {
        val key = targetClass.qualifiedName ?: return null
        return modules[key] as T?
    }
}