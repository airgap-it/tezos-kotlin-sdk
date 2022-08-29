# Tezos Kotlin SDK: Core

[![stable](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?label=stable&sort=semver)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![latest](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?color=orange&include_prereleases&label=latest)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![release](https://img.shields.io/jitpack/v/github/airgap-it/tezos-kotlin-sdk)](https://jitpack.io/#airgap-it/tezos-kotlin-sdk)
[![license](https://img.shields.io/github/license/airgap-it/tezos-kotlin-sdk)](https://github.com/airgap-it/tezos-kotlin-sdk/blob/master/LICENSE)

`:tezos-core` provides base Tezos types and actions that can be performed on them.

## Setup

To add `:tezos-core` into your project:

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

2. Add the dependency:

#### Groovy
  ```groovy
  dependencies {
    def tezos_version = "0.0.1"

    implementation "com.github.airgap-it.tezos-kotlin-sdk:core:$tezos_version"
  }
  ```

#### Kotlin

  ```kotlin
  dependencies {
    val tezosVersion = "0.0.1"

    implementation("com.github.airgap-it.tezos-kotlin-sdk:core:$tezosVersion")
}
  ```

## Usage

See the [docs](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/docs) or [samples](https://github.com/airgap-it/tezos-kotlin-sdk/tree/main/samples) to learn how to use the library.