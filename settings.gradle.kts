include(":tezos-core")

include(":tezos-michelson")
include(":tezos-operation")
include(":tezos-rpc")
include(":tezos-contract")

include(":tezos-crypto:bouncycastle")
project(":tezos-crypto:bouncycastle").name = "tezos-crypto-bouncycastle"
