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
- `CryptoProvider` (Tezos cryptography methods provider, `:tezos-core`)
- `HttpClientProvider` (HTTP client provider, `:tezos-rpc`)

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

The Tezos Kotlin SDK comes with support for the **tz**, **KT** and **zet** addresses. The addresses, however, can't be used
as plain strings across the library. To improve typing and compile-time checks, a few wrapper address types have been
introduced:

**tz**, **KT**
- `Address` (sealed)
    - **tz**
      - `ImplicitAddress (: Address)` (sealed)
      - **tz1**
        - `Ed25519PublicKeyHash (: ImplicitAddress)`
      - **tz2**
        - `Secp256K1PublicKeyHash (: ImplicitAddress)`
      - **tz3**
        - `P256PublicKeyHash (: ImplicitAddress)`
    - **KT**
      - `OriginatedAddress (: Address)` (sealed)`
      - **KT1**
        - `ContractHash (: OriginatedAddress)`

**zet1**
- `SaplingAddress`

Different types may be required depending on the use case. 

## `Address` (type)

`Address` is the most general address type that covers all the **tz** and **KT** addresses. It is a sealed
interface which splits into `ImplicitAddress` and `OriginatedAddress` cases. 

To create an `Address` instance use the factory method:

```kotlin
import it.airgap.tezos.core.converter.encoded.Address

val tz1Address = Address("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val tz2Address = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy")
val tz3Address = Address("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B")
val kt1Address = Address("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7")
```

or use one of the actual types that implement the interface:

```kotlin
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKeyHash
import it.airgap.tezos.core.type.encoded.P256PublicKeyHash

val tz1Address: Address = Ed25519PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val tz2Address: Address = Secp256K1PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val tz3Address: Address = P256PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val kt1Address: Address = ContractHash("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7")
```

### `ImplicitAddress` (type)

`ImplicitAddress` is an address type that covers all the **tz** addresses. It is a sealed interface implemented
by `Ed25519PublicKeyHash` (**tz1**), `Secp256K1PublicKeyHash` (**tz2**) and `P256PublicKeyHash` (**tz3**). 

To create an `ImplicitAddress` instance use the factory method:

```kotlin
import it.airgap.tezos.core.converter.encoded.ImplicitAddress

val tz1ImplicitAddress = ImplicitAddress("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val tz2ImplicitAddress = ImplicitAddress("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy")
val tz3ImplicitAddress = ImplicitAddress("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B")
```

or use one of the actual types that implement the interface:

```kotlin
import it.airgap.tezos.core.converter.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKeyHash
import it.airgap.tezos.core.type.encoded.P256PublicKeyHash

val tz1ImplicitAddress: ImplicitAddress = Ed25519PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val tz2ImplicitAddress: ImplicitAddress = Secp256K1PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val tz3ImplicitAddress: ImplicitAddress = P256PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
```

`ImplicitAddress` can also be used as `Address`:

```kotlin
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.core.converter.encoded.ImplicitAddress

val tz1Address: Address = ImplicitAddress("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
val tz2Address: Address = ImplicitAddress("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy")
val tz3Address: Address = ImplicitAddress("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B")
```

### `OriginatedAddress` (type)

`OriginatedAddress` is an address type that covers all the **KT** addresses. It is a sealed interface implemented
by `ContractHsh` (**KT1**).

To create an `OriginatedAddress` instance use the factory method:

```kotlin
import it.airgap.tezos.core.converter.encoded.OriginatedAddress

val kt1OriginatedAddress = OriginatedAddress("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7")
```

or use one of the actual types that implement the interface:

```kotlin
import it.airgap.tezos.core.converter.encoded.OriginatedAddress
import it.airgap.tezos.core.type.encoded.ContractHash

val kt1OriginatedAddress: OriginatedAddress = ContractHash("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7")
```

`OriginatedAddress` can also be used as `Address`:

```kotlin
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.core.converter.encoded.OriginatedAddress

val kt1Address: Address = OriginatedAddress("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7")
```

# Base58 Encoded Data

Similarly to the addresses, the use of a plain string Base58 encoded data (i.e. string values whose data type can be 
recognized by their Base58 prefix, e.g. **sig**, **Net**, **expr**, etc.) is not supported. The data must be first wrapped
in its responding type before it can be used in the library, for example:

```kotlin
import it.airgap.tezos.core.type.encoded.*

val blockHash = BlockHash("BLrUSnmhoWczorTYG8utWTLcD8yup6MX1MCehXG8f8QWew8t1N8")
val chainId = ChainId("NetXPduhFKtb9SG")
val ed25519PublicKey = Ed25519PublicKey("edpktmJqEE79FtfdWse1gqnUey1vNBkB3zNV99Pi95SRAs8NMatczG")
```

# Micheline

The Tezos Kotlin SDK provides various features to make working with Micheline expressions easier. It can help developers
to deserialize and serialize expressions to a JSON string, pack and unpack it using an optional schema or convert it to
a better typed `Michelson` structure.

All features mentioned in this section are provided in the `:tezos-michelson` module.

## JSON Serialization

Micheline expressions can be deserialized and serialized to a JSON string with the `fromJsonString` and `toJsonString`
methods:

```kotlin
import it.airgap.tezos.michelson.coder.fromJsonString
import it.airgap.tezos.michelson.coder.toJsonString
import it.airgap.tezos.michelson.micheline.Micheline

val micheline = Micheline.fromJsonString("""{ "int": "10" }""") // deserializes to MichelineLiteral.Integer("10")
val json = micheline.toJsonString() // serializes to { "int": "10" }
```

## Create an Expression

Sometimes it may be useful to create a Micheline expression directly in the code, for example to define a packing schema
or provide contract call parameters. To address this need and simplify the process, the SDK introduces a tailored Micheline DSL.

The examples below present how to create Micheline expressions. Each example features a reference JSON to help you
understand what the final expression is going to look like. The examples are divided into DSL- and constructor-based parts.
The DSL-based parts show how to use the library's Micheline DSL, whilst the constructor-based parts show how to achieve
the same results by creating appropriate `Micheline` instances directly.

### Literals

#### Int

JSON
```json
{
  "int": "10"
}
```

DSL
```kotlin
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val int = micheline { int(10) }
```

Constructor
```kotlin
import it.airgap.tezos.michelson.micheline.MichelineLiteral

val int = MichelineLiteral.Integer(10)
```

#### String

JSON
```json
{
  "string": "value"
}
```

DSL
```kotlin
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val string = micheline { string("value") }
```

Constructor
```kotlin
import it.airgap.tezos.michelson.micheline.MichelineLiteral

val string = MichelineLiteral.String("value")
```

#### Bytes

JSON
```json
{
  "bytes": "0a"
}
```

DSL
```kotlin
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val bytes = micheline { bytes("0a") }
```

Constructor
```kotlin
import it.airgap.tezos.michelson.micheline.MichelineLiteral

val bytes = MichelineLiteral.Bytes("0a")
```

### Prims

#### Data

JSON
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

DSL
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

Constructor
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

JSON
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

DSL
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

Constructor
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

JSON
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

DSL
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

Constructor
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

JSON
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

DSL
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

Constructor
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

To serialize a Micheline expression to its optimized binary representation use `packToBytes` or `packToString`,
to deserialize a Micheline expression from the optimized binary representation run `unpackFromBytes` or `unpackFromString`:

```kotlin
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline
import it.airgap.tezos.michelson.packer.packToBytes
import it.airgap.tezos.michelson.packer.packToString
import it.airgap.tezos.michelson.packer.unpackFromBytes
import it.airgap.tezos.michelson.packer.unpackFromString

val micheline = micheline { string("tz1ZBuF2dQ7E1b32bK3g1Qsah4pvWqpM4b4A") }
val schema = micheline { address }

val packedBytes = micheline.packToBytes(schema) // = byteArrayOf(5, 10, 0, 0, 0, 22, 0, 0, -108, -96, -70, 39, 22, -98, -40, -39, 124, 31, 71, 109, -26, 21, 108, 36, -126, -37, -5, 61)
val unpackedFromBytes = Micheline.unpackFromBytes(packedBytes, schema) // = { "string": "tz1ZBuF2dQ7E1b32bK3g1Qsah4pvWqpM4b4A" }

val packedString = micheline.packToString(schema) // = "050a00000016000094a0ba27169ed8d97c1f476de6156c2482dbfb3d"
val unpackedFromString = Micheline.unpackFromString(packedString, schema) // = { "string": "tz1ZBuF2dQ7E1b32bK3g1Qsah4pvWqpM4b4A" }

```

# `Michelson` (type)

The `Michelson` type is the SDK representation of the Smart Contract language. It provides a much
more strictly typed interface than `Micheline` and it can be converted to a Micheline expression
or created out of it.

## Create Expression

`Michelson` splits into `MichelsonData`, `MichelsonInstruction`, `MichelsonType` and `MichelsonComparableType`
types, defined based on [the grammar specification](https://tezos.gitlab.io/active/michelson.html#full-grammar):

- `Michelson`
  -  `MichelsonData (: Michelson)`
    - ...
    - `MichelsonInstruction (: MichelsonData)`
      - ...
  - `MichelsonType (: Michelson)`
    - ...
    - `MichelsonComparableType (: MichelsonType)`
      - ...

The examples below present how to create Michelson expressions. Each example features a reference JSON to help you
understand what the final expression (in Micheline) is going to look like.

### MichelsonData

JSON
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

Expression
```kotlin
import it.airgap.tezos.michelson.MichelsonData

val michelson = MichelsonData.Pair(
    MichelsonData.Some(
        MichelsonData.IntConstant(10),
    ),
    MichelsonData.Left(
        MichelsonData.StringConstant("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"),
    ),
)

```

### MichelsonInstruction

JSON
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

Expression
```kotlin
import it.airgap.tezos.michelson.MichelsonInstruction

val michelson = MichelsonInstruction.Map(
    MichelsonInstruction.Sequence(
        MichelsonInstruction.Dig(2U),
        MichelsonInstruction.Dup(),
        MichelsonInstruction.Dug(3U),
        MichelsonInstruction.Swap,
    ),
)
```

### MichelsonType

JSON
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
          "prim": "chest_key",
          "annots": [
            "%chest_key"
          ]
        }
      ]
    }
  ]
}
```

```kotlin
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonType

val michelson = MichelsonType.Pair(
    MichelsonComparableType.Option(
        MichelsonComparableType.Nat(metadata = MichelsonType.Metadata(fieldName = Michelson.Annotation.Field("%nat")))
    ),
    MichelsonType.Or(
        MichelsonComparableType.Address(metadata = MichelsonType.Metadata(fieldName = Michelson.Annotation.Field("%address"))),
        MichelsonType.ChestKey(metadata = MichelsonType.Metadata(fieldName = Michelson.Annotation.Field("%chest_key"))),
    ),
)
```

### MichelsonComparableType

JSON
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
import it.airgap.tezos.michelson.MichelsonComparableType

val michelson = MichelsonComparableType.Pair(
    MichelsonComparableType.Option(
        MichelsonComparableType.Nat(metadata = MichelsonType.Metadata(fieldName = Michelson.Annotation.Field("%nat")))
    ),
    MichelsonComparableType.Or(
        MichelsonComparableType.Address(metadata = MichelsonType.Metadata(fieldName = Michelson.Annotation.Field("%address"))),
        MichelsonComparableType.Bytes(metadata = MichelsonType.Metadata(fieldName = Michelson.Annotation.Field("%bytes"))),
    ),
)
```

## Micheline Conversion

To convert values between the `Michelson` and `Micheline` types use the`toMicheline` and `toMichelson` methods:

```kotlin
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.converter.toMicheline
import it.airgap.tezos.michelson.converter.toMichelson

val pair = MichelsonType.Pair(
    MichelsonComparableType.Option(
        MichelsonComparableType.Nat()
    ),
    MichelsonType.Or(
        MichelsonComparableType.Unit(),
        MichelsonType.Map(
            MichelsonComparableType.String(),
            MichelsonComparableType.Pair(
                MichelsonComparableType.Bytes(),
                MichelsonComparableType.Address(),
            )
        )
    )
)

val micheline /*: Micheline */ = pair.toMicheline()
val michelson /*: Michelson */ = micheline.toMichelson()
```

## In the Micheline DSL

`Michelson` values can also be used within the Micheline DSL:

```kotlin
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val michelson = MichelsonType.Pair(
    MichelsonComparableType.Option(
        MichelsonComparableType.Nat()
    ),
    MichelsonType.Or(
        MichelsonComparableType.Unit(),
        MichelsonType.Map(
            MichelsonComparableType.String(),
            MichelsonComparableType.Pair(
                MichelsonComparableType.Bytes(),
                MichelsonComparableType.Address(),
            )
        )
    )
)

val micheline = micheline {
    michelson(michelson)
}
```

# Operation

The Tezos Kotlin SDK comes with means to create Tezos operations, forge and sign them.

All features mentioned in this section are provided in the `:tezos-operation` module.

## Create

To create an `Operation` instance use the factory method:

```kotlin
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.core.converter.encoded.ImplicitAddress
import it.airgap.tezos.core.converter.encoded.Signature
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent

val unsigned = Operation(
    OperationContent.Transaction(
        source = ImplicitAddress("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"),
        counter = TezosNatural(1U),
        amount = Mutez(1000),
        destination = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"),
    ),
    branch = BlockHash("BMdhifZkcb5i9D6FnBi19SSBjft3sYaeKDAsEBgbsRLPTihQQJU"),
)

val signed = Operation(
    OperationContent.Transaction(
        source = ImplicitAddress("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"),
        counter = TezosNatural(1U),
        amount = Mutez(1000),
        destination = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"),
    ),
    branch = BlockHash("BMdhifZkcb5i9D6FnBi19SSBjft3sYaeKDAsEBgbsRLPTihQQJU"),
    signature = Signature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"),
)
```

or explicitly use one of the `Operation` types constructor. `Operation` can be either `Operation.Unsigned` or `Operation.Signed`:

```kotlin
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.core.converter.encoded.ImplicitAddress
import it.airgap.tezos.core.converter.encoded.Signature
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent

val unsigned = Operation.Unsigned(
    BlockHash("BMdhifZkcb5i9D6FnBi19SSBjft3sYaeKDAsEBgbsRLPTihQQJU"),
    listOf(
        OperationContent.Transaction(
            source = ImplicitAddress("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"),
            counter = TezosNatural(1U),
            amount = Mutez(1000),
            destination = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"),
        ),
    ),
)

val signed = Operation.Signed(
    BlockHash("BMdhifZkcb5i9D6FnBi19SSBjft3sYaeKDAsEBgbsRLPTihQQJU"),
    listOf(
        OperationContent.Transaction(
            source = ImplicitAddress("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"),
            counter = TezosNatural(1U),
            amount = Mutez(1000),
            destination = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"),
        ),
    ),
    Signature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"),
)
```

## Forge

To forge an operation use the `forgeToBytes` or `forgeToString` methods, to unforge an operation use `unforgeFromBytes` 
or `unforgeFromString`.

```kotlin
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.core.converter.encoded.ImplicitAddress
import it.airgap.tezos.core.converter.encoded.Signature
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.coder.forgeToBytes
import it.airgap.tezos.operation.coder.forgeToString
import it.airgap.tezos.operation.coder.unforgeFromBytes
import it.airgap.tezos.operation.coder.unforgeFromString

val operation = Operation(
    OperationContent.Transaction(
        source = ImplicitAddress("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"),
        counter = TezosNatural(1U),
        amount = Mutez(1000),
        destination = Address("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"),
    ),
    branch = BlockHash("BMdhifZkcb5i9D6FnBi19SSBjft3sYaeKDAsEBgbsRLPTihQQJU"),
)

val forgedBytes = operation.forgeToBytes()
val unforgedFromBytes = Operation.unforgeFromBytes(forgedBytes)

val forgedString = operation.forgeToString()
val unforgedFromString = Operation.unforgeFromString(forgedString)
```

## Sign and Verify

Sign an operation with the `signWith` method:

```kotlin
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.Ed25519SecretKey
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.signer.signWith

val secretKey = Ed25519SecretKey("edskRv7VyXGVZb8EsrR7D9XKUbbAQNQGtALP6QeB16ZCD7SmmJpzyeneJVg3Mq56YLbxRA1kSdAXiswwPiaVfR3NHGMCXCziuZ")

val unsigned = Operation(branch = BlockHash("BMdhifZkcb5i9D6FnBi19SSBjft3sYaeKDAsEBgbsRLPTihQQJU"))
val signed = unsigned.signWith(secretKey)
```

*Note: If you sign `Operation.Signed`, the old signature will be discarded and replaced with the new one.*

You can also generate the signature only by calling `sign` on the secret key:

```kotlin
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.Ed25519SecretKey
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.signer.sign

val secretKey = Ed25519SecretKey("edskRv7VyXGVZb8EsrR7D9XKUbbAQNQGtALP6QeB16ZCD7SmmJpzyeneJVg3Mq56YLbxRA1kSdAXiswwPiaVfR3NHGMCXCziuZ")

val unsigned = Operation(branch = BlockHash("BMdhifZkcb5i9D6FnBi19SSBjft3sYaeKDAsEBgbsRLPTihQQJU"))
val signature = secretKey.sign(unsigned)
```

To verify the operation's signature call `verifyWith` on the signed operation or `verify` on the public key:

```kotlin
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.Ed25519PublicKey
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.signer.verify
import it.airgap.tezos.operation.signer.verifyWith

val publicKey = Ed25519PublicKey("edpkttZKC51wemRqL2QxwpMnEKxWnbd35pq47Y6xsCHp5M1f7LN8NP")

val signed = Operation.Signed(
    BlockHash("BMdhifZkcb5i9D6FnBi19SSBjft3sYaeKDAsEBgbsRLPTihQQJU"),
    listOf(),
    Signature("edsigtyqcyfipEAqFVexKahDjtQkzFgAkd9MroyYHnxxUVHAi4oSBJSezwiKaoNpH9NkY34cNuR4nrL6s8oPWVstQp9h2f7iGQF"),
)

val isSignatureValid1 = signed.verifyWith(publicKey)
val isSignatureValid2 = publicKey.verify(signed)
```

# RPC

You can use the Tezos Kotlin SDK to directly interact with a Tezos node. This means that the library provides the developers
with tools to easily make HTTP request to fetch the node's data or inject operations. Additionally, the SDK can estimate
the minimum fee that is required to inject an operation.

All features mentioned in this section are provided in the `:tezos-rpc` module.

## Interact With a Node

Create a `TezosRpc` instance and use its interface to make calls to a node of your choice:

```kotlin
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.rpc.TezosRpc

val tezosRpc = TezosRpc("https://testnet-tezos.giganode.io")

val block = /* suspend */ tezosRpc.getBlock()
val balance = /* suspend */ tezosRpc.getBalance(contractId = Address("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"))
```

More advanced users can also use the interface that has been designed to be a direct mapping of the actual HTTP paths, 
as defined in the [shell](https://tezos.gitlab.io/shell/rpc.html) and [active](https://tezos.gitlab.io/active/rpc.html)
RPC reference documentation:

```kotlin
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.rpc.TezosRpc

val tezosRpc = TezosRpc("https://testnet-tezos.giganode.io")

val block = /* suspend */ tezosRpc.chains.main.blocks.head.get()
val balance = /* suspend */ tezosRpc.chains.main.blocks.head.context.contracts(Address("tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c")).balance.get()
```

*Note: The RPC interface consists of `suspend` methods which can only be run from a coroutine.
If you're not familiar with coroutines in Kotlin, see [the official documentation](https://kotlinlang.org/docs/coroutines-overview.html).*

## Estimate the Operation Fee

You can estimate the minimum fee required for the operation to be injected with the `minFee` method
defined for `TezosRpc`:

```kotlin
import it.airgap.tezos.core.converter.encoded.Address
import it.airgap.tezos.rpc.TezosRpc

val tezosRpc = TezosRpc("https://testnet-tezos.giganode.io")

val branch = /* suspend */ tezosRpc.getBlock().block.hash
val operation = Operation(branch = branch)
val operationWithFee = /* suspend */ tezosRpc.minFee(operation = operation)
```

*Note: The RPC interface consists of `suspend` methods which can only be run from a coroutine.
If you're not familiar with coroutines in Kotlin, see [the official documentation](https://kotlinlang.org/docs/coroutines-overview.html).*

# Contract

The Tezos Kotlin SDK exposes a Smart Contract handler whose purpose is to simplify the interaction with contract's
storage and code. The handler ships with methods to read a contract's storage and prepare operations with parameters
matching the contract's code.

You can create the handler with the factory method:

```kotlin
import it.airgap.tezos.contract.Contract
import it.airgap.tezos.core.type.encoded.ContractHash

val contract = Contract("https://testnet-tezos.giganode.io", ContractHash("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7"))
```

All features mentioned in this section are provided in the `:tezos-contract` module.

## Read the Storage

To read the contract's storage with the handler, simply call:

```kotlin
import it.airgap.tezos.contract.Contract
import it.airgap.tezos.core.type.encoded.ContractHash

val contract = Contract("https://testnet-tezos.giganode.io", ContractHash("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7"))
val storageEntry = /* suspend */ contract.storage.get()
```

The returned `storageEntry` will be of the `ContractStorageEntry` type or `null` if the storage couldn't be read. 
The `ContractStorageEntry` type is meant to be a handler for the storage values which will allow you to access the data
even if you're not familiar with the exact contract's storage structure and just want to access a piece of data by its
annotation. The `ContractStorageEntry` value may be one of the following:
- `ContractStorageEntry.Value`\
Holds a raw value, for example a string literal.


- `ContractStorageEntry.Object`\
Holds an object-like structure, mapped from, for example, a `pair` primitive application.
The value's arguments are also `ContractStorageEntry` and can be referenced by their annotations, if any.


- `ContractStorageEntry.Sequence`\
Holds a list of `ContractStorageEntry` values, mapped from a Micheline sequence.


- `ContractStorageEntry.Map`\
Holds a map of `ContractStorageEntry` values where the keys are also of the `ContractStorageEntry` type.
Created from a sequence of `Elt` objects.


- `ContractStorageEntry.BigMap`\
Holds a BigMap id and can further fetch the BigMap value which is also mapped to the `ContractStorageEntry` handler.

To better understand how to read data from `ContractStorageEntry`, let's assume the contract's storage is of a type
defined as follows:
```json
{
  "prim": "pair",
  "args": [
    {
      "prim": "big_map",
      "args": [
        {
          "prim": "address"
        },
        {
          "prim": "nat",
          "annots": [
            ":balance"
          ]
        }
      ],
      "annots": [
        "%ledger"
      ]
    },
    {
      "prim": "pair",
      "args": [
        {
          "prim": "address",
          "annots": [
            "%admin"
          ]
        },
        {
          "prim": "pair",
          "args": [
            {
              "prim": "bool",
              "annots": [
                "%paused"
              ]
            },
            {
              "prim": "nat",
              "annots": [
                "%total_supply"
              ]
            }
          ]
        }
      ],
      "annots": ["%fields"]
    }
  ]
}
```

and the returned value in Micheline is:

```json
{
  "prim": "Pair",
  "args": [
    {
      "int": "51296"
    },
    {
      "prim": "Pair",
      "args": [
        {
          "bytes": "0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c"
        },
        {
          "prim": "Pair",
          "args": [
            {
              "prim": "True"
            },
            {
              "int": "20"
            }
          ]
        }
      ]
    }
  ]
}
```

The individual values from the storage could be read as below:
```kotlin
import it.airgap.tezos.contract.converter.objectEntry

val storageEntry = /* suspend */ contract.storage.get()
// WARNING: If `storageEntry` or `objectEntry["%fields]` are not of the `ContractStorageEntry.Object` type, the 2 following lines will fail at runtime. 
// Use `objectEntry` and other convenience casting properties (`valueEntry`, `sequenceEntry`, `mapEntry` and `bigMapEntry`) only if you're sure about the type.
// If you're unsure of what type to expect, it's always better to check every case with the `when` expression, double-check the type with `is` or safely cast it with `as?`.  
val objectEntry = storageEntry.objectEntry
val fields = objectEntry["%fields"].objectEntry

val ledger = objectEntry["%ledger"]?.value // = { "int": "51296" }
val admin = fields["%admin"]?.value // = { "bytes": "0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c" }
val paused = fields["%paused"]?.value // = { "prim": "True" }
val totalSupply = fields["%total_supply"]?.value // = { "int": "20" }
```

If you don't want to use the handler and process the storage value as raw Micheline, you can access it with the
`value` property defined for `ContractStorageEntry`:

```kotlin
val storageEntry = /* suspend */ contract.storage.get()
val micheline = storageEntry?.value
```

*Note: The `Contract` handler interface consists of `suspend` methods which can only be run from a coroutine.
If you're not familiar with coroutines in Kotlin, see [the official documentation](https://kotlinlang.org/docs/coroutines-overview.html).*

### Read a BigMap

When one of the contract's storage value is of the `ContractStorageEntry.BigMap` type, you can use that handler to further fetch the BigMap
data. The example below assumes `storageEntry` is `ContractStorageEntry.BigMap` and fetches the value stored under the
`"my_key"` key.

```kotlin
import it.airgap.tezos.contract.converter.bigMapEntry

val bigMapValue = storageEntry.bigMapEntry.get { string("my_key") }
```

## Prepare a Call

To prepare a contract call with the handler, first create an entrypoint handler:

```kotlin
import it.airgap.tezos.contract.Contract
import it.airgap.tezos.core.type.encoded.ContractHash

val contract = Contract("https://testnet-tezos.giganode.io", ContractHash("KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7"))

val default = contract.entrypoint()
val transfer = contract.entrypoint("transfer")
```

You can then invoke the entrypoint handler. The handler takes 2 required arguments, the source of the call and
parameters. The parameters can be provided as plain Micheline:

```kotlin
import it.airgap.tezos.core.type.encoded.ImplicitAddress

val parameters = micheline {
    sequence {
        Pair {
            arg { string("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e") }
            arg {
                sequence {
                    Pair {
                        arg { string("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e") }
                        arg {
                            Pair {
                                arg { int(0) }
                                arg { int(100) }
                            }
                        }
                    }
                }
            }
        }
    }
}
val unsigned = /* suspend */ transfer(parameters, ImplicitAddress("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"))
```

or with the custom contract entrypoint parameters DSL:

```kotlin
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline

val unsigned = /* suspend */ transfer(ImplicitAddress("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")) {
    sequence {
        `object` {
            value("%from_") { string("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e") }
            sequence("%txs") {
                `object` {
                    value("%to_") { string("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e") }
                    value("%token_id") { int(0) }
                    value("%amount") { int(100) }
                }
            }
        }
    }
}
```

The library will generate a Micheline value from the object description specified with the DSL based on the entrypoint's code.
The values will be placed based on their annotations, if **any is provided** or based on their order if **no annotation was specified for any value**.

By default, the entrypoint handler will also estimate the minimum fee for the operation it generates. However, you can override 
this behaviour by provided **both** the `fee` and `limits` arguments.

*Note: The `Contract` handler interface consists of `suspend` methods which can only be run from a coroutine.
If you're not familiar with coroutines in Kotlin, see [the official documentation](https://kotlinlang.org/docs/coroutines-overview.html).*