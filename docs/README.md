# Configuration

Some of the library's public methods take an additional (and optional) `Tezos` context as an argument.
Its purpose is to hold a unique library configuration and serve it internally wherever it may be required.

The context can be created with its builder method:
```kotlin
import it.airgap.tezos.core.Tezos

val tezos = Tezos { /* ... */ }
```
and then used across the library's features:
```kotlin
import it.airgap.tezos.core.converter.encoded.Address

val address = Address("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e", tezos = tezos)
```

See the rest of the examples to better understand how to configure the `Tezos` context.

## Default Context

A newly created `Tezos` context instance can be set as the library's default with the `isDefault` flag.
The default context instance will always be used implicitly, if an explicit instance isn't passed in:

```kotlin
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.Address

Tezos {
    isDefault = true /* this instance can be then referenced as Tezos.Default */
    /* ... */
}

val address = Address("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e", /* tezos = Tezos.Default */)
```

## Modules

Optionally register and configure [modules](https://github.com/airgap-it/tezos-kotlin-sdk#modules-overview) with `use`:

```kotlin
import it.airgap.tezos.contract.ContractModule
import it.airgap.tezos.core.CoreModule
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.michelson.MichelsonModule
import it.airgap.tezos.operation.OperationModule
import it.airgap.tezos.rpc.RpcModule

Tezos {
    /* ... */
    
    use(CoreModule)
    use(MichelsonModule)
    use(OperationModule)
    use(RpcModule) {
        httpClientProvider = /* ... */
    }
    use(ContractModule)
}
```

If a module is not registered by the user, it'll get so lazily upon the first usage of a feature that it provides.

## Feature Providers

Some of the library's components require features that can't be found in the language standard library and, therefore,
must be satisfied with external providers. To make the library flexible and give control its users over which providers to choose,
the Tezos Kotlin SDK exposes a few interfaces whose implementations can be used in the library.
Those interfaces are:
- [:tezos-core] `it.airgap.tezos.core.crypto.CryptoProvider` (Tezos cryptography methods provider)
- [:tezos-rpc] `it.airgap.tezos.rpc.http.HttpClientProvider` (HTTP client provider)

For those who don't want to implement their own providers, the SDK also ships with implementations built
on top of libraries of our choice. Those predefined implementations can be found in `tezos-crypto` and `tezos-http`.

```kotlin
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.rpc.RpcModule

Tezos {
    /* ... */
    cryptoProvider = MyCryptoProvider()
    
    use(RpcModule) {
        httpClientProvider = MyHttpClientProvider()
    }
}
```

## Implicit Configuration

If no `Tezos` instance has been set explicitly as the library's default, the library will try to initialize a new instance
and set it as `Tezos.Default` on its own. To achieve it, the library will try to load the external feature providers from the classpath.
Because of that, it is possible to use `Tezos.Default` and methods that rely on `Tezos.Default` without prior configuration. 
⚠️ **This feature, however, should only be considered as a useful simplification for testing and debug purposes.
It is highly advisable to ALWAYS create your own context and set the feature providers explicitly in the PRODUCTION environment.** ⚠️  

## Example

The following example creates a new `Tezos` context configured with `BouncyCastleCryptoProvider` (from `:tezos-crypto-bouncycastle`) 
as the `CryptoProvider` implementation and `KtorHttpClientProvider` (from `:tezos-http-ktor`) as the `HttpClientProvider`implementation,
and sets the context as the library's default, so it can be implicitly used as `Tezos.Default`.

```kotlin
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import it.airgap.tezos.http.ktor.KtorHttpClientProvider
import it.airgap.tezos.rpc.RpcModule

Tezos {
    isDefault = true
    cryptoProvider = BouncyCastleCryptoProvider()

    use(RpcModule) {
        httpClientProvider = KtorHttpClientProvider()
    }
}
```
