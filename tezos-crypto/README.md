# Tezos Kotlin SDK: Crypto

[![stable](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?label=stable&sort=semver)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![latest](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?color=orange&include_prereleases&label=latest)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![release](https://img.shields.io/jitpack/v/github/airgap-it/tezos-kotlin-sdk)](https://jitpack.io/#airgap-it/tezos-kotlin-sdk)
[![license](https://img.shields.io/github/license/airgap-it/tezos-kotlin-sdk)](https://github.com/airgap-it/tezos-kotlin-sdk/blob/master/LICENSE)

Tezos Kotlin SDK depends on cryptography that is more advanced than what is provided in the standard library. This requirement must be then met either through custom implementations or by 3rd party libraries. 
To keep the developer in control over such a delicate matter, the core and plugin modules do not ship with predefined implementations of required algorithms. 
Instead, it is left to the developer to decide what best fits their needs and register it in the SDK. 

The submodules of `tezos-crypto` come with their own implementations that satisfy the cryptographic requirements and can be used to easily set up the SDK if there is no need for too much customization.

| Module                       | Description                                                                                                                    | Module Dependencies |
|------------------------------|--------------------------------------------------------------------------------------------------------------------------------|---------------------|
| `:tezos-crypto:bouncycastle` | Uses [BouncyCastle](https://www.bouncycastle.org/) to provide implementations of cryptographic algorithms required in the SDK. | `:tezos-core`       |