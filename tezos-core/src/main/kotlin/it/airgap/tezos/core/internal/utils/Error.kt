package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.module.TezosModule

@InternalTezosSdkApi
public interface ErrorUtilsContext {
    public fun failWithIllegalArgument(message: String? = null): Nothing = throw IllegalArgumentException(message)

    public fun failWithDependencyNotFound(name: String, module: String): Nothing =
        error("Failed to find $name implementation in the classpath. Consider adding a `$module` dependency or use a manually created Tezos instance.")
}

public inline fun <reified T : TezosModule> ErrorUtilsContext.failWithModuleNotFound(): Nothing = error("${T::class.qualifiedName} not found.")