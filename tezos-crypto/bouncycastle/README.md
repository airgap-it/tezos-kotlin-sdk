# Tezos Kotlin SDK: Crypto BouncyCastle

[![stable](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?label=stable&sort=semver)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![latest](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?color=orange&include_prereleases&label=latest)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![release](https://img.shields.io/jitpack/v/github/airgap-it/tezos-kotlin-sdk)](https://jitpack.io/#airgap-it/tezos-kotlin-sdk)
[![license](https://img.shields.io/github/license/airgap-it/tezos-kotlin-sdk)](https://github.com/airgap-it/tezos-kotlin-sdk/blob/master/LICENSE)

`:tezos-crypto:bouncycastle` provides an implementation of `CryptoProvider` (`:tezos-core`) which uses [BouncyCastle](https://www.bouncycastle.org/) to satisfy the interface requirements.

## Setup

To add `:tezos-crypto-bouncycastle` into your project:

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

    implementation "com.github.airgap-it.tezos-kotlin-sdk:tezos-crypto-bouncycastle:$tezos_version"
    
    // dependencies
    implementation "com.github.airgap-it.tezos-kotlin-sdk:tezos-core:$tezos_version"
  }
  ```

#### Kotlin

  ```kotlin
  dependencies {
    val tezosVersion = "0.0.1"
    
    implementation("com.github.airgap-it.tezos-kotlin-sdk:tezos-crypto-bouncycastle:$tezosVersion")
    
    // dependencies
    implementation("com.github.airgap-it.tezos-kotlin-sdk:tezos-core:$tezosVersion")
}
  ```

## Usage

To use `BouncyCastleCryptoProvider` in your `Tezos` context, register it while creating an instance:

```kotlin
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider

val tezos = Tezos {
    cryptoProvider = BouncyCastleCryptoProvider()
}
```