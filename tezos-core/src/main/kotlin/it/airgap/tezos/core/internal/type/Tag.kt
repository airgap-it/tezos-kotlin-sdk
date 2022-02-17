package it.airgap.tezos.core.internal.type

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.type.encoded.Encoded

@InternalTezosSdkApi
public interface BytesTag {
    public val value: ByteArray

    public operator fun plus(bytes: ByteArray): ByteArray = value + bytes
}

@InternalTezosSdkApi
public interface EncodedTag<out K : Encoded.Kind<*>> : BytesTag {
    public val kind: K
}