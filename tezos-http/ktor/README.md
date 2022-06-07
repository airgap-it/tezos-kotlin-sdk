# Tezos Kotlin SDK: HTTP Ktor

[![stable](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?label=stable&sort=semver)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![latest](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?color=orange&include_prereleases&label=latest)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![release](https://img.shields.io/jitpack/v/github/airgap-it/tezos-kotlin-sdk)](https://jitpack.io/#airgap-it/tezos-kotlin-sdk)
[![license](https://img.shields.io/github/license/airgap-it/tezos-kotlin-sdk)](https://github.com/airgap-it/tezos-kotlin-sdk/blob/master/LICENSE)

`:tezos-http:ktor` provides an implementation of `HttpClientProvider` (`:tezos-rpc`) which uses [Ktor](https://ktor.io/) to satisfy the interface requirements.

## Setup

To add `:tezos-http-ktor` into your project:

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
    def tezos_version = "0.0.1"

    implementation "com.github.airgap-it.tezos:tezos-http-ktor:$tezos_version"

    // dependencies
    implementation "com.github.airgap-it.tezos-kotlin-sdk:core:$tezos_version"
  }
  ```

#### Kotlin

  ```kotlin
  dependencies {
    val tezosVersion = "0.0.1"

    implementation("com.github.airgap-it.tezos:tezos-http-ktor:$tezosVersion")
    
    // dependencies
    implementation("com.github.airgap-it.tezos-kotlin-sdk:core:$tezosVersion")
}
  ```

### ProGuard and R8
Tezos Kotlin SDK internally uses various libraries that may require custom ProGuard rules. If you are using ProGuard or R8, please follow the guides listed below to make sure your project works correctly after obfuscation:

- [ProGuard rules for Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization#android)
- [`:tezos-rpc`](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/tezos-rpc#proguard-and-r8)

## Usage

To use `KtorHttpClientProvider` in your `Tezos` context, register it while creating an instance:

```kotlin
import it.airgap.tezos.http.ktor.KtorHttpClientProvider
import it.airgap.tezos.rpc.RpcModule

val tezos = Tezos {
    install(RpcModule) {
        httpClientProvider = KtorHttpClientProvider()
    }
}
```