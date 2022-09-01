# Tezos Kotlin SDK

[![stable](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?label=stable&sort=semver)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![latest](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?color=orange&include_prereleases&label=latest)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![release](https://img.shields.io/jitpack/v/github/airgap-it/tezos-kotlin-sdk)](https://jitpack.io/#airgap-it/tezos-kotlin-sdk)
[![license](https://img.shields.io/github/license/airgap-it/tezos-kotlin-sdk)](https://github.com/airgap-it/tezos-kotlin-sdk/blob/master/LICENSE)

A Kotlin library to interact with the Tezos blockchain.

## Use Cases

The Tezos Kotlin SDK ships with tools that can be used to:

*General*
  - convert a Base58 encoded string to bytes and vice versa

*Michelson/Micheline*
  - parse or create a Micheline JSON string
  - pack and unpack Micheline
  - convert Micheline to typed Michelson and vice versa

*Operations*
- create an unsigned or signed Tezos operation
- forge and unforge an operation
- sign an operation and verify the signature

*RPC*
- interact with a Tezos node
- estimate the operation fee

*Contract*
- read a contract's storage
- read BigMaps
- prepare contract calls

## Modules Overview

The Tezos Kotlin SDK is a multi-module project. It has been designed to allow its users to use only the required minimum of functionality that meets their needs, thus optimizing the amount of redundant and unwanted code and dependencies.
The library modules can be divided into 3 categories:
- Core
- Plugin
- Default Provider

### Core Modules
The core modules are the basis for other modules. They are required for the SDK to work as expected.

| Core Module                                                                       | Description                                                          | Module Dependencies |
|-----------------------------------------------------------------------------------|----------------------------------------------------------------------|---------------------|
| [:tezos-core](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-core) | Provides base Tezos types and actions that can be performed on them. | ✖️                  |

### Plugin Modules
The plugin modules are optional and come with additional functionality. They should be registered in the appropriate core components before use.

| Plugin Module                                                                               | Description                                                                                                                                                                                                   | Module Dependencies                                                                                                                                                                                                                                                                                                                                                            |
|---------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [:tezos-michelson](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-michelson) | Provides [Michelson](https://tezos.gitlab.io/active/michelson.html) and [Micheline](https://tezos.gitlab.io/shell/micheline.html) types and actions, e.g. `pack`/`unpack`.                                    | [:tezos-core](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-core)                                                                                                                                                                                                                                                                                              |
| [:tezos-operation](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-operation) | Provides Tezos Operation structures as defined in [the P2P message format](https://tezos.gitlab.io/shell/p2p_api.html) and actions that can be performed on them, e.g. `forge`/`unforge` and `sign`/`verify`. | [:tezos-core](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-core) <br /> [:tezos-michelson](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-michelson)                                                                                                                                                                                           |
| [:tezos-rpc](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-rpc)             | Provides a Tezos RPC client which should be used to interact with Tezos nodes.                                                                                                                                | [:tezos-core](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-core) <br /> [:tezos-michelson](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-michelson) <br /> [:tezos-operation](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-operation)                                                                                        |
| [:tezos-contract](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-contract)   | Provides a Tezos contract handler which should be used to interact with Tezos contracts.                                                                                                                      | [:tezos-core](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-core) <br /> [:tezos-michelson](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-michelson) <br /> [:tezos-operation](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-operation) <br /> [:tezos-rpc](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-rpc) |

### Default Provider Modules
The default provider modules are optional and come with default implementations of various components that can be provided externally.

| Default Provider Module                                                                                         | Description                                                                                   | Module Dependencies                                                               |
|-----------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| [:tezos-crypto:bouncycastle](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-crypto/bouncycastle) | Uses [BouncyCastle](https://www.bouncycastle.org/) to satisfy the cryptographic requirements. | [:tezos-core](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-core) |
| [:tezos-http:ktor](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-http/ktor)                     | Uses [Ktor](https://ktor.io/) to provide an HTTP client.                                      | [:tezos-rpc](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-rpc)   |

## Setup

To add the Tezos Kotlin SDK into your project:

1. Make sure the [JitPack](https://jitpack.io/) repository is included in your root `build.gradle` file:

#### Groovy
  ```groovy
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
  ```

#### Kotlin
  ```kotlin
  allprojects {
    repositories {
      ...
      maven("https://jitpack.io")
    }
  }
  ```

2. Add the dependencies:

#### Groovy
  ```groovy
  dependencies {
    def tezos_version = "0.0.2"

    // REQUIRED, :tezos-core
    implementation "com.github.airgap-it.tezos-kotlin-sdk:tezos-core:$tezos_version"

    // optional modules
    implementation "com.github.airgap-it.tezos-kotlin-sdk:tezos-michelson:$tezos_version"
    implementation "com.github.airgap-it.tezos-kotlin-sdk:tezos-operation:$tezos_version"
    implementation "com.github.airgap-it.tezos-kotlin-sdk:tezos-rpc:$tezos_version"
    implementation "com.github.airgap-it.tezos-kotlin-sdk:tezos-contract:$tezos_version"

    implementation "com.github.airgap-it.tezos-kotlin-sdk:tezos-crypto-bouncycastle:$tezos_version"
    implementation "com.github.airgap-it.tezos-kotlin-sdk:tezos-http-ktor:$tezos_version"
    
    ---

    // alternatively, all modules
    implementation "com.github.airgap-it:tezos-kotlin-sdk:$tezos_version"
  }
  ```

#### Kotlin

  ```kotlin
  dependencies {
    val tezosVersion = "0.0.2"

    // REQUIRED, :tezos-core
    implementation("com.github.airgap-it.tezos-kotlin-sdk:tezos-core:$tezosVersion")

    // optional modules
    implementation("com.github.airgap-it.tezos-kotlin-sdk:tezos-michelson:$tezosVersion")
    implementation("com.github.airgap-it.tezos-kotlin-sdk:tezos-operation:$tezosVersion")
    implementation("com.github.airgap-it.tezos-kotlin-sdk:tezos-rpc:$tezosVersion")
    implementation("com.github.airgap-it.tezos-kotlin-sdk:tezos-contract:$tezosVersion")

    implementation("com.github.airgap-it.tezos-kotlin-sdk:tezos-crypto-bouncycastle:$tezosVersion")
    implementation("com.github.airgap-it.tezos-kotlin-sdk:tezos-http-ktor:$tezosVersion")
    
    ---

    // alternatively, all modules
    implementation("com.github.airgap-it:tezos-kotlin-sdk:$tezosVersion")
}
  ```

### ProGuard and R8

The Tezos Kotlin SDK internally uses various libraries that may require custom ProGuard rules. If you are using ProGuard or R8, please follow the guides listed below to make sure your project works correctly with mentioned modules after obfuscation:

| Module           | Rules                                                                                              |
|------------------|----------------------------------------------------------------------------------------------------|
| :tezos-michelson | [ProGuard rules for Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization#android) |
| :tezos-rpc       | [ProGuard rules for Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization#android) |
| :tezos-http:ktor | [ProGuard rules for Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization#android) |

## Documentation

A high level guide on how to use the library can be found in the [docs](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/docs).
To try out the library in action, see the [samples](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/samples).

The code documentation can be build from the root of the project with the `buildDocs` Gradle task:
```shell
$ ./gradlew buildDocs 
> # output: build/docs
```
