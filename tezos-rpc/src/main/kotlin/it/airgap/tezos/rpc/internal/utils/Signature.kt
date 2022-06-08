package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.type.encoded.Ed25519Signature
import it.airgap.tezos.core.type.encoded.Signature

internal val Signature.Companion.placeholder
    get() = Ed25519Signature("edsigtXomBKi5CTRf5cjATJWSyaRvhfYNHqSUGrn4SdbYRcGwQrUGjzEfQDTuqHhuA8b2d8NarZjz8TRf65WkpQmo423BtomS8Q")

internal val Signature.isPlaceholder: Boolean
    get() = base58 == Signature.placeholder.base58