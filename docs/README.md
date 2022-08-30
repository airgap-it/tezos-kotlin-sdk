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

```diff
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.Address

- Tezos {
-    isDefault = true
-    /* ... */
- }

val address = Address("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e", /* tezos = Tezos.Default */) // will still work
```

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

# Address

The Tezos Kotlin SDK supports 

## Tz Addresses

```kotlin
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.core.converter.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.*

val tz1Address = Address("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val tz2Address = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy")
val tz3Address = Address("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B")

val tz1ImplicitAddress = ImplicitAddress("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val tz2ImplicitAddress = ImplicitAddress("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy")
val tz3ImplicitAddress = ImplicitAddress("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B")

val ed25519PublicKeyHash = Ed25519PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val secp256K1PublicKeyHash = Secp256K1PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val p256PublicKeyHash = P256PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
```

## KT Addresses

```kotlin
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.core.type.encoded.*

val address = Address("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7")
val contractHash = ContractHash("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7")
```

## Zet Addresses (Sapling)

# Micheline

## From and To JSON

```kotlin
import it.airgap.tezos.michelson.coder.fromJsonString
import it.airgap.tezos.michelson.coder.toJsonString
import it.airgap.tezos.michelson.micheline.Micheline

val micheline = Micheline.fromJsonString("{ \"int\": \"10\" }") // deserializes to MichelineLiteral.Integer("10")
val json = micheline.toJsonString() // serializes to { "int": "10" }
```

## Create an Expression

### Literals

#### Int

```json
{
  "int": "10"
}
```

```kotlin
import it.airgap.tezos.michelson.micheline.MichelineLiteral

val int = MichelineLiteral.Integer(10)
```

```kotlin
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val int = micheline { int(10) }
```

#### String

```json
{
  "string": "value"
}
```

```kotlin
import it.airgap.tezos.michelson.micheline.MichelineLiteral

val string = MichelineLiteral.String("value")
```

```kotlin
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val string = micheline { string("value") }
```

#### Bytes

```json
{
  "bytes": "0a"
}
```

```kotlin
import it.airgap.tezos.michelson.micheline.MichelineLiteral

val bytes = MichelineLiteral.Bytes("0a")
```

```kotlin
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val bytes = micheline { bytes("0a") }
```

### Prims

#### Data

```json
{
  "prim": "Pair",
  "args": [
    {
      "prim": "Some",
      "args": [
        {
          "int": "10"
        }
      ]
    },
    {
      "prim": "Left",
      "args": [
        {
          "string": "tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"
        }
      ]
    }
  ]
}
```

```kotlin
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val pair = micheline {
    Pair {
        arg {
            Some {
                arg { int(10) }
            }
        }
        arg {
            Left {
                arg { string("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e") }
            }
        }
    }
}
```

```kotlin
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication

val pair = MichelinePrimitiveApplication(
    prim = MichelsonData.Pair,
    args = listOf(
        MichelinePrimitiveApplication(
            prim = MichelsonData.Some,
            args = listOf(
                MichelineLiteral.Integer(10),
            ),
        ),
        MichelinePrimitiveApplication(
            prim = MichelsonData.Left,
            args = listOf(
                MichelineLiteral.String("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"),
            ),
        ),
    ),
)
```

#### Instruction

```json
{
  "prim": "MAP",
  "args": [
    [
      {
        "prim": "DIG",
        "args": [
          {
            "int": "2"
          }
        ]
      },
      {
        "prim": "DUP"
      },
      {
        "prim": "DUG",
        "args": [
          {
            "int": "3"
          }
        ]
      },
      {
        "prim": "SWAP"
      }
    ]
  ]
}
```

```kotlin
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val pair = micheline {
    MAP {
        expression {
            DIG(2U)
            DUP
            DUG(3U)
            SWAP
        }
    }
}
```

```kotlin
import it.airgap.tezos.michelson.MichelsonInstruction
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication

val pair = MichelinePrimitiveApplication(
    prim = MichelsonInstruction.Map,
    args = listOf(
        MichelineSequence(
            MichelinePrimitiveApplication(
                prim = MichelsonInstruction.Dig,
                args = listOf(
                    MichelineLiteral.Integer(2),
                ),
            ),
            MichelinePrimitiveApplication(prim = MichelsonInstruction.Dup),
            MichelinePrimitiveApplication(
                prim = MichelsonInstruction.Dug,
                args = listOf(
                    MichelineLiteral.Integer(3),
                ),
            ),
            MichelinePrimitiveApplication(prim = MichelsonInstruction.Swap),
        ),
    ),
)
```

#### Types

```json
{
  "prim": "pair",
  "args": [
    {
      "prim": "option",
      "args": [
        {
          "prim": "nat",
          "annots": [
            "%nat"
          ]
        }
      ]
    },
    {
      "prim": "or",
      "args": [
        {
          "prim": "address",
          "annots": [
            "%address"
          ]
        },
        {
          "prim": "bytes",
          "annots": [
            "%bytes"
          ]
        }
      ]
    }
  ]
}
```

```kotlin
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val pair = micheline {
    pair {
        arg {
            option {
                arg { nat annots "%nat" }
            }
        }
        arg {
            or {
                lhs { address annots "%address" }
                rhs { bytes annots "%bytes" }
            }
        }
    }
}
```

```kotlin
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication

val pair = MichelinePrimitiveApplication(
    prim = MichelsonComparableType.Pair,
    args = listOf(
        MichelinePrimitiveApplication(
            prim = MichelsonComparableType.Option,
            args = listOf(
                MichelinePrimitiveApplication(
                    prim = MichelsonComparableType.Nat,
                    annots = listOf("%nat"),
                ),
            ),
        ),
        MichelinePrimitiveApplication(
            prim = MichelsonComparableType.Or,
            args = listOf(
                MichelinePrimitiveApplication(
                    prim = MichelsonComparableType.Address,
                    annots = listOf("%address"),
                ),
                MichelinePrimitiveApplication(
                    prim = MichelsonComparableType.Bytes,
                    annots = listOf("%bytes"),
                ),
            ),
        ),
    ),
)
```

### Sequences

```json
[
  {
    "prim": "DUP"
  },
  {
    "prim": "CDR"
  },
  {
    "prim": "SWAP"
  }
]
```

```kotlin
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val pair = micheline {
    sequence {
        DUP
        CDR
        SWAP
    }
}
```

```kotlin
import it.airgap.tezos.michelson.MichelsonInstruction
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication

val pair = MichelineSequence(
    MichelinePrimitiveApplication(prim = MichelsonInstruction.Dup),
    MichelinePrimitiveApplication(prim = MichelsonInstruction.Cdr),
    MichelinePrimitiveApplication(prim = MichelsonInstruction.Swap),
)
```

## Pack and Unpack

# Micheline and Michelson Types

# Operation

## Create

## Forge

## Sign

# RPC

## Interact With a Node

## Estimate the Operation Fee

# Contract

## Read Storage

## Read BigMap

## Prepare Call