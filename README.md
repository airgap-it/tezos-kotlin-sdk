# Tezos Kotlin SDK

A Kotlin library to interact with the Tezos blockchain.

### 🚧🚧🚧 The project is still a work in progress, use at your own risk. 🚧🚧🚧

## Modules Overview

Tezos Kotlin SDK is a multi-module project. It has been designed to allow its users to use only the required minimum of functionality that meets their needs, thus optimizing the amount of redundant and unwanted code and dependencies.
The library modules can be divided into 3 categories:
- Core
- Plugin
- Default Provider

### Core Modules
The core modules are the basis for other modules. They are required for the SDK to work as expected.

| Core Module   | Description                                                          | Module Dependencies |
|---------------|----------------------------------------------------------------------|---------------------|
| `:tezos-core` | Provides base Tezos types and actions that can be performed on them. | ✖️                  |

### Plugin Modules
The plugin modules are optional and come with additional functionality. They should be registered in the appropriate core components before use.

| Plugin Module      | Description                                                                                                                                                                                                   | Module Dependencies |
|--------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------|
| `:tezos-michelson` | Provides [Michelson](https://tezos.gitlab.io/active/michelson.html) and [Micheline](https://tezos.gitlab.io/shell/micheline.html) types and actions, e.g. `pack`/`unpack`.                                    | `:tezos-core`       |
| `:tezos-operation` | Provides Tezos Operation structures as defined in [the P2P message format](https://tezos.gitlab.io/shell/p2p_api.html) and actions that can be performed on them, e.g. `forge`/`unforge` and `sign`/`verify`. | `:tezos-core`       |
| `:tezos-rpc`       | Provides a Tezos RPC client which should be used to interact with Tezos nodes.                                                                                                                                | `:tezos-core`       |

### Default Provider Modules
The default provider modules are optional and come with default implementations of various components that can be provided externally.

| Default Provider Module | Description                                                                                                                                                                                 | Module Dependencies |
|-------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------|
| `:tezos-crypto:default` | Provides default and recommended implementations of cryptographic algorithms required in the SDK. Uses [BouncyCastle](https://www.bouncycastle.org/) to satisfy the interface requirements. | `:tezos-core`       |

## Setup

🚧🚧🚧 TBD 🚧🚧🚧

### ProGuard and R8
Tezos Kotlin SDK internally uses various libraries that may require custom ProGuard rules. If you are using ProGuard or R8, please follow the guides listed below to make sure your project works correctly with mentioned modules after obfuscation:

| Module             | Rules                                                                                              |
|--------------------|----------------------------------------------------------------------------------------------------|
| `:tezos-michelson` | [ProGuard rules for Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization#android) |

## Usage
🚧🚧🚧 TBD 🚧🚧🚧
