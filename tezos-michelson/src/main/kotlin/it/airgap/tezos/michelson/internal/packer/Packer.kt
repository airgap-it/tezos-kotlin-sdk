package it.airgap.tezos.michelson.internal.packer

import it.airgap.tezos.michelson.micheline.MichelineNode

internal interface Packer<T> {
    fun pack(value: T, schema: MichelineNode?): ByteArray
    fun unpack(bytes: ByteArray, schema: MichelineNode?): T
}