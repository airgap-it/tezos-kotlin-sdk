package it.airgap.tezos.core.internal.type

import it.airgap.tezos.core.Tezos

public interface BytesTag {
    public val value: ByteArray

    public operator fun plus(bytes: ByteArray): ByteArray = value + bytes
}

public interface PrefixTag : BytesTag {
    public val prefix: Tezos.Prefix
}