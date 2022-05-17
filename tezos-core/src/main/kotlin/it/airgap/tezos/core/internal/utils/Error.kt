package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.module.TezosModule
import kotlin.reflect.KClass

@InternalTezosSdkApi
public fun failWithIllegalArgument(message: String? = null): Nothing = throw IllegalArgumentException(message)

@InternalTezosSdkApi
public fun failWithDependencyNotFound(name: String, module: String): Nothing =
    error("Failed to find $name implementation in the classpath. Consider adding a `$module` dependency or use a manually created Tezos instance.")

@InternalTezosSdkApi
public fun failWithModuleNotFound(module: KClass<out TezosModule>): Nothing = error("${module.qualifiedName} not found.")