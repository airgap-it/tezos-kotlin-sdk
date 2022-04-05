package it.airgap.tezos.core.internal.type

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.type.encoded.Encoded
import it.airgap.tezos.core.type.encoded.MetaEncoded

@InternalTezosSdkApi
public interface BytesTag {
    public val value: ByteArray

    public operator fun plus(bytes: ByteArray): ByteArray = value + bytes
}

@InternalTezosSdkApi
public interface EncodedTag<out K : MetaEncoded.Kind<*>> : BytesTag {
    public val kind: K
}