package it.airgap.tezos.core.internal.type

public interface ByteTag {
    public val value: Byte?

    public operator fun plus(bytes: ByteArray): ByteArray = listOfNotNull(value).toByteArray() + bytes
}