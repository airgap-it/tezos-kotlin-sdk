package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public interface UtilsContext :
    ArrayUtilsContext,
    BytesUtilsContext,
    CodingUtilsContext,
    CollectionUtilsContext,
    ErrorUtilsContext,
    MapUtilsContext,
    NumberUtilsContext,
    StringUtilsContext