package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.type.encoded.Ed25519Signature
import it.airgap.tezos.core.type.encoded.SignatureEncoded

internal val SignatureEncoded.Companion.placeholder
    get() = Ed25519Signature("edsigtXomBKi5CTRf5cjATJWSyaRvhfYNHqSUGrn4SdbYRcGwQrUGjzEfQDTuqHhuA8b2d8NarZjz8TRf65WkpQmo423BtomS8Q")

internal val SignatureEncoded.isPlaceholder: Boolean
    get() = base58 == SignatureEncoded.placeholder.base58