package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.*
import it.airgap.tezos.core.internal.coder.tez.MutezBytesCoder
import it.airgap.tezos.core.internal.coder.tez.NanotezBytesCoder
import it.airgap.tezos.core.internal.coder.tez.TezBytesCoder
import it.airgap.tezos.core.internal.coder.timestamp.TimestampBigIntCoder
import it.airgap.tezos.core.internal.coder.zarith.ZarithIntegerBytesCoder
import it.airgap.tezos.core.internal.coder.zarith.ZarithNaturalBytesCoder
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.converter.encoded.*
import it.airgap.tezos.core.internal.delegate.lazyWeak
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.core.type.tez.Tez
import it.airgap.tezos.core.type.zarith.ZarithInteger
import it.airgap.tezos.core.type.zarith.ZarithNatural

@InternalTezosSdkApi
public class CoreDependencyRegistry internal constructor(global: DependencyRegistry) {

    // -- coder --

    public val encodedBytesCoder: EncodedBytesCoder by lazy { EncodedBytesCoder(global.base58Check) }

    public val addressBytesCoder: ConsumingBytesCoder<Address> by lazy { AddressBytesCoder(implicitAddressBytesCoder, encodedBytesCoder) }
    public val implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress> by lazy { ImplicitAddressBytesCoder(encodedBytesCoder) }

    public val publicKeyBytesCoder: ConsumingBytesCoder<PublicKey> by lazy { PublicKeyBytesCoder(encodedBytesCoder) }
    public val signatureBytesCoder: ConsumingBytesCoder<Signature> by lazy { SignatureBytesCoder(encodedBytesCoder) }

    public val zarithNaturalBytesCoder: ConsumingBytesCoder<ZarithNatural> by lazy { Static.zarithNaturalBytesCoder }
    public val zarithIntegerBytesCoder: ConsumingBytesCoder<ZarithInteger> by lazy { Static.zarithIntegerBytesCoder }

    public val tezBytesCoder: ConsumingBytesCoder<Tez> by lazy { Static.tezBytesCoder }
    public val mutezBytesCoder: ConsumingBytesCoder<Mutez> by lazy { Static.mutezBytesCoder }
    public val nanotezBytesCoder: ConsumingBytesCoder<Nanotez> by lazy { Static.nanotezBytesCoder }

    public val timestampBigIntCoder: Coder<Timestamp, BigInt> by lazy { Static.timestampBigIntCoder }

    // -- converter --

    public val bytesToAddressConverter: Converter<ByteArray, Address> by lazy { BytesToAddressConverter(global.base58Check) }
    public val stringToAddressConverter: Converter<String, Address> by lazy { Static.stringToAddressConverter }

    public val bytesToImplicitAddressConverter: Converter<ByteArray, ImplicitAddress> by lazy { BytesToImplicitAddressConverter(global.base58Check) }
    public val stringToImplicitAddressConverter: Converter<String, ImplicitAddress> by lazy { Static.stringToImplicitAddressConverter }

    public val bytesToPublicKeyConverter: Converter<ByteArray, PublicKey> by lazy { BytesToPublicKeyConverter(global.base58Check) }
    public val stringToPublicKeyConverter: Converter<String, PublicKey> by lazy { Static.stringToPublicKeyConverter }

    public val bytesToPublicKeyHashConverter: Converter<ByteArray, PublicKeyHash> by lazy { BytesToPublicKeyHashConverter(global.base58Check) }
    public val stringToPublicKeyHashConverter: Converter<String, PublicKeyHash> by lazy { Static.stringToPublicKeyHashConverter }

    public val bytesToBlindedPublicKeyHashConverter: Converter<ByteArray, BlindedPublicKeyHash> by lazy { BytesToBlindedPublicKeyHashConverter(global.base58Check) }
    public val stringToBlindedPublicKeyHashConverter: Converter<String, BlindedPublicKeyHash> by lazy { Static.stringToBlindedPublicKeyHashConverter }

    public val bytesToSignatureConverter: Converter<ByteArray, Signature> by lazy { BytesToSignatureConverter(global.base58Check) }
    public val stringToSignatureConverter: Converter<String, Signature> by lazy { Static.stringToSignatureConverter }

    public val signatureToGenericSignatureConverter: Converter<Signature, GenericSignature> by lazy { SignatureToGenericSignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    public val genericSignatureToEd25519SignatureConverter: Converter<GenericSignature, Ed25519Signature> by lazy { GenericSignatureToEd25519SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    public val genericSignatureToSecp256K1SignatureConverter: Converter<GenericSignature, Secp256K1Signature> by lazy { GenericSignatureToSecp256K1SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    public val genericSignatureToP256SignatureConverter: Converter<GenericSignature, P256Signature> by lazy { GenericSignatureToP256SignatureConverter(signatureBytesCoder, encodedBytesCoder) }

    private object Static {

        // -- coder --

        val zarithNaturalBytesCoder: ConsumingBytesCoder<ZarithNatural> by lazyWeak { ZarithNaturalBytesCoder() }
        val zarithIntegerBytesCoder: ConsumingBytesCoder<ZarithInteger> by lazyWeak { ZarithIntegerBytesCoder(zarithNaturalBytesCoder) }

        val tezBytesCoder: ConsumingBytesCoder<Tez> by lazyWeak { TezBytesCoder(zarithNaturalBytesCoder) }
        val mutezBytesCoder: ConsumingBytesCoder<Mutez> by lazyWeak { MutezBytesCoder(zarithNaturalBytesCoder) }
        val nanotezBytesCoder: ConsumingBytesCoder<Nanotez> by lazyWeak { NanotezBytesCoder(zarithNaturalBytesCoder) }

        val timestampBigIntCoder: Coder<Timestamp, BigInt> by lazyWeak { TimestampBigIntCoder() }

        // -- converter --

        val stringToAddressConverter: Converter<String, Address> by lazyWeak { StringToAddressConverter() }
        val stringToImplicitAddressConverter: Converter<String, ImplicitAddress> by lazyWeak { StringToImplicitAddressConverter() }
        val stringToPublicKeyConverter: Converter<String, PublicKey> by lazyWeak { StringToPublicKeyConverter() }
        val stringToPublicKeyHashConverter: Converter<String, PublicKeyHash> by lazyWeak { StringToPublicKeyHashConverter() }
        val stringToBlindedPublicKeyHashConverter: Converter<String, BlindedPublicKeyHash> by lazyWeak { StringToBlindedPublicKeyHashConverter() }
        val stringToSignatureConverter: Converter<String, Signature> by lazyWeak { StringToSignatureConverter() }
    }
}