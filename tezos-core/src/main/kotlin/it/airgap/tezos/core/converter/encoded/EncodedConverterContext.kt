package it.airgap.tezos.core.converter.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public interface EncodedConverterContext :
    AddressConverterContext,
    ImplicitAddressConverterContext,
    BlindedPublicKeyHashConverterContext,
    Ed25519SignatureConverterContext,
    P256SignatureConverterContext,
    PublicKeyConverterContext,
    PublicKeyHashConverterContext,
    Secp256K1SignatureConverterContext,
    SignatureConverterContext