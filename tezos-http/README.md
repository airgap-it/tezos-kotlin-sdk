# Tezos Kotlin SDK: HTTP

[![stable](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?label=stable&sort=semver)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![latest](https://img.shields.io/github/v/tag/airgap-it/tezos-kotlin-sdk?color=orange&include_prereleases&label=latest)](https://github.com/airgap-it/tezos-kotlin-sdk/releases)
[![release](https://img.shields.io/jitpack/v/github/airgap-it/tezos-kotlin-sdk)](https://jitpack.io/#airgap-it/tezos-kotlin-sdk)
[![license](https://img.shields.io/github/license/airgap-it/tezos-kotlin-sdk)](https://github.com/airgap-it/tezos-kotlin-sdk/blob/master/LICENSE)

Some Tezos Kotlin SDK modules rely on the ability to send HTTP requests. To keep the developer in control over what HTTP client should be used in their project, 
those modules do not ship with predefined implementations. Instead, it is left to the developer to decide what best fits their needs and register it in the SDK. 

The submodules of `tezos-http` come with their own implementations that satisfy the cryptographic requirements and can be used to easily set up the SDK if there is no need for too much customization.

| Module             | Description                                              | Module Dependencies |
|--------------------|----------------------------------------------------------|---------------------|
| `:tezos-http:ktor` | Uses [Ktor](https://ktor.io/) to provide an HTTP client. | `:tezos-rpc`        |