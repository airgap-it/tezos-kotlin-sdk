package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.*
import it.airgap.tezos.core.internal.coder.number.TezosIntegerBytesCoder
import it.airgap.tezos.core.internal.coder.number.TezosNaturalBytesCoder
import it.airgap.tezos.core.internal.coder.tez.MutezBytesCoder
import it.airgap.tezos.core.internal.coder.timestamp.TimestampBigIntCoder
import it.airgap.tezos.core.internal.context.TezosCoreContext.lazyWeak
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.converter.encoded.*
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.number.TezosInteger
import it.airgap.tezos.core.type.number.TezosNatural
import it.airgap.tezos.core.type.tez.Mutez

@InternalTezosSdkApi
public class CoreDependencyRegistry internal constructor(global: DependencyRegistry) {

    // -- coder --

    public val encodedBytesCoder: EncodedBytesCoder by lazy { EncodedBytesCoder(global.base58Check) }

    public val addressBytesCoder: ConsumingBytesCoder<Address> by lazy { AddressBytesCoder(implicitAddressBytesCoder, originatedAddressBytesCoder) }
    public val implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress> by lazy { ImplicitAddressBytesCoder(encodedBytesCoder) }
    public val originatedAddressBytesCoder: ConsumingBytesCoder<OriginatedAddress> by lazy { OriginatedAddressBytesCoder(encodedBytesCoder) }

    public val publicKeyBytesCoder: ConsumingBytesCoder<PublicKey> by lazy { PublicKeyBytesCoder(encodedBytesCoder) }
    public val signatureBytesCoder: ConsumingBytesCoder<Signature> by lazy { SignatureBytesCoder(encodedBytesCoder) }

    public val tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural> by lazy { Static.tezosNaturalBytesCoder }
    public val tezosIntegerBytesCoder: ConsumingBytesCoder<TezosInteger> by lazy { Static.tezosIntegerBytesCoder }

    public val mutezBytesCoder: ConsumingBytesCoder<Mutez> by lazy { Static.mutezBytesCoder }

    public val timestampBigIntCoder: Coder<Timestamp, BigInt> by lazy { Static.timestampBigIntCoder }

    // -- converter --

    public val stringToAddressConverter: Converter<String, Address> by lazy { Static.stringToAddressConverter }
    public val stringToImplicitAddressConverter: Converter<String, ImplicitAddress> by lazy { Static.stringToImplicitAddressConverter }
    public val stringToOriginatedAddressConverter: Converter<String, OriginatedAddress> by lazy { Static.stringToOriginatedAddressConverter }
    public val stringToPublicKeyConverter: Converter<String, PublicKey> by lazy { Static.stringToPublicKeyConverter }
    public val stringToPublicKeyHashConverter: Converter<String, PublicKeyHash> by lazy { Static.stringToPublicKeyHashConverter }
    public val stringToBlindedPublicKeyHashConverter: Converter<String, BlindedPublicKeyHash> by lazy { Static.stringToBlindedPublicKeyHashConverter }
    public val stringToSignatureConverter: Converter<String, Signature> by lazy { Static.stringToSignatureConverter }

    public val publicKeyToPublicKeyHashConverter: Converter<PublicKey, PublicKeyHash> by lazy { PublicKeyToPublicKeyHashConverter(global.crypto, encodedBytesCoder) }

    public val signatureToGenericSignatureConverter: Converter<Signature, GenericSignature> by lazy { SignatureToGenericSignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    public val genericSignatureToEd25519SignatureConverter: Converter<GenericSignature, Ed25519Signature> by lazy { GenericSignatureToEd25519SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    public val genericSignatureToSecp256K1SignatureConverter: Converter<GenericSignature, Secp256K1Signature> by lazy { GenericSignatureToSecp256K1SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    public val genericSignatureToP256SignatureConverter: Converter<GenericSignature, P256Signature> by lazy { GenericSignatureToP256SignatureConverter(signatureBytesCoder, encodedBytesCoder) }

    private object Static {

        // -- coder --

        val tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural> by lazyWeak { TezosNaturalBytesCoder() }
        val tezosIntegerBytesCoder: ConsumingBytesCoder<TezosInteger> by lazyWeak { TezosIntegerBytesCoder(tezosNaturalBytesCoder) }

        val mutezBytesCoder: ConsumingBytesCoder<Mutez> by lazyWeak { MutezBytesCoder(tezosNaturalBytesCoder) }

        val timestampBigIntCoder: Coder<Timestamp, BigInt> by lazyWeak { TimestampBigIntCoder() }

        // -- converter --

        val stringToAddressConverter: Converter<String, Address> by lazyWeak { StringToAddressConverter() }
        val stringToImplicitAddressConverter: Converter<String, ImplicitAddress> by lazyWeak { StringToImplicitAddressConverter(stringToPublicKeyHashConverter) }
        val stringToOriginatedAddressConverter: Converter<String, OriginatedAddress> by lazyWeak { StringToOriginatedAddressConverter() }
        val stringToPublicKeyConverter: Converter<String, PublicKey> by lazyWeak { StringToPublicKeyConverter() }
        val stringToPublicKeyHashConverter: Converter<String, PublicKeyHash> by lazyWeak { StringToPublicKeyHashConverter() }
        val stringToBlindedPublicKeyHashConverter: Converter<String, BlindedPublicKeyHash> by lazyWeak { StringToBlindedPublicKeyHashConverter() }
        val stringToSignatureConverter: Converter<String, Signature> by lazyWeak { StringToSignatureConverter() }
    }
}