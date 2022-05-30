# Tezos Kotlin SDK: Crypto

Tezos Kotlin SDK depends on cryptography that is more advanced than what is provided in the standard library. This requirement must be then met either through custom implementations or by 3rd party libraries. 
To keep the developer in control over such a delicate matter, the core and plugin modules do not ship with predefined implementations of required algorithms. 
Instead, it is left to the developer to decide what best fits their needs and register it in the SDK. 

The submodules of `tezos-crypto` come with their own implementations that satisfy the cryptographic requirements and can be used to easily set up the SDK if there is no need for too much customization.

| Module                       | Description                                                                                                                    | Module Dependencies |
|------------------------------|--------------------------------------------------------------------------------------------------------------------------------|---------------------|
| `:tezos-crypto:bouncycastle` | Uses [BouncyCastle](https://www.bouncycastle.org/) to provide implementations of cryptographic algorithms required in the SDK. | `:tezos-core`       |