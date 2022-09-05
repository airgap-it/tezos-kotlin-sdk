# Tezos Kotlin SDK: Michelson

[![stable](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?label=stable&sort=semver)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![latest](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?color=orange&include_prereleases&label=latest)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![release](https://img.shields.io/jitpack/v/github/airgap-it/tezos-kotlin-sdk)](https://jitpack.io/#airgap-it/tezos-kotlin-sdk)
[![license](https://img.shields.io/github/license/airgap-it/tezos-kotlin-sdk)](https://github.com/airgap-it/tezos-kotlin-sdk/blob/master/LICENSE)

`:tezos-michelson` provides [Michelson](https://tezos.gitlab.io/active/michelson.html) and [Micheline](https://tezos.gitlab.io/shell/micheline.html) types and actions, e.g. `pack`/`unpack`.

## Setup

To add `:tezos-michelson` into your project:

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

    implementation "com.github.airgap-it.tezos-kotlin-sdk:tezos-michelson:$tezos_version"

    // dependencies
    implementation "com.github.airgap-it.tezos-kotlin-sdk:tezos-core:$tezos_version"
  }
  ```

#### Kotlin

  ```kotlin
  dependencies {
    val tezosVersion = "0.0.1"

    implementation("com.github.airgap-it.tezos-kotlin-sdk:tezos-michelson:$tezosVersion")

    // dependencies
    implementation("com.github.airgap-it.tezos-kotlin-sdk:tezos-core:$tezosVersion")
}
  ```

### ProGuard and R8
Tezos Kotlin SDK internally uses various libraries that may require custom ProGuard rules. If you are using ProGuard or R8, please follow the guides listed below to make sure your project works correctly after obfuscation:

- [ProGuard rules for Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization#android)

## Usage

See the [docs](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/docs) or [samples](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/samples) to learn how to use the library.