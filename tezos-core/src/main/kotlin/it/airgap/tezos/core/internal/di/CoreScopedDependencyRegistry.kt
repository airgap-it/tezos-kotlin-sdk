package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.*
import it.airgap.tezos.core.internal.coder.encoded.*
import it.airgap.tezos.core.internal.coder.encoded.AddressBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.ImplicitAddressBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.PublicKeyBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.SignatureBytesCoder
import it.airgap.tezos.core.internal.coder.tez.MutezBytesCoder
import it.airgap.tezos.core.internal.coder.tez.NanotezBytesCoder
import it.airgap.tezos.core.internal.coder.tez.TezBytesCoder
import it.airgap.tezos.core.internal.coder.timestamp.TimestampBigIntCoder
import it.airgap.tezos.core.internal.coder.zarith.ZarithIntegerBytesCoder
import it.airgap.tezos.core.internal.coder.zarith.ZarithNaturalBytesCoder
import it.airgap.tezos.core.internal.converter.*
import it.airgap.tezos.core.internal.converter.encoded.*
import it.airgap.tezos.core.internal.converter.encoded.BytesToBlindedPublicKeyHashConverter
import it.airgap.tezos.core.internal.converter.encoded.BytesToPublicKeyHashConverter
import it.airgap.tezos.core.internal.converter.encoded.SignatureToGenericSignatureConverter
import it.airgap.tezos.core.internal.converter.encoded.StringToBlindedPublicKeyHashConverter
import it.airgap.tezos.core.internal.converter.encoded.StringToSignatureConverter
import it.airgap.tezos.core.internal.delegate.lazyWeak
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.core.type.tez.Tez
import it.airgap.tezos.core.type.zarith.ZarithInteger
import it.airgap.tezos.core.type.zarith.ZarithNatural

internal class CoreScopedDependencyRegistry(dependencyRegistry: DependencyRegistry) : ScopedDependencyRegistry, DependencyRegistry by dependencyRegistry {

    // -- coder --

    override val encodedBytesCoder: EncodedBytesCoder by lazy { EncodedBytesCoder(base58Check) }

    override val addressBytesCoder: ConsumingBytesCoder<Address> by lazy { AddressBytesCoder(implicitAddressBytesCoder, encodedBytesCoder) }
    override val implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress> by lazy { ImplicitAddressBytesCoder(encodedBytesCoder) }

    override val publicKeyBytesCoder: ConsumingBytesCoder<PublicKey> by lazy { PublicKeyBytesCoder(encodedBytesCoder) }
    override val signatureBytesCoder: ConsumingBytesCoder<Signature> by lazy { SignatureBytesCoder(encodedBytesCoder) }

    override val zarithNaturalBytesCoder: ConsumingBytesCoder<ZarithNatural> by lazy { Static.zarithNaturalBytesCoder }
    override val zarithIntegerBytesCoder: ConsumingBytesCoder<ZarithInteger> by lazy { Static.zarithIntegerBytesCoder }

    override val tezBytesCoder: ConsumingBytesCoder<Tez> by lazy { Static.tezBytesCoder }
    override val mutezBytesCoder: ConsumingBytesCoder<Mutez> by lazy { Static.mutezBytesCoder }
    override val nanotezBytesCoder: ConsumingBytesCoder<Nanotez> by lazy { Static.nanotezBytesCoder }

    override val timestampBigIntCoder: Coder<Timestamp, BigInt> by lazy { Static.timestampBigIntCoder }

    // -- converter --

    override val bytesToAddressConverter: Converter<ByteArray, Address> by lazy { BytesToAddressConverter(base58Check) }
    override val stringToAddressConverter: Converter<String, Address> by lazy { Static.stringToAddressConverter }

    override val bytesToImplicitAddressConverter: Converter<ByteArray, ImplicitAddress> by lazy { BytesToImplicitAddressConverter(base58Check) }
    override val stringToImplicitAddressConverter: Converter<String, ImplicitAddress> by lazy { Static.stringToImplicitAddressConverter }

    override val bytesToPublicKeyConverter: Converter<ByteArray, PublicKey> by lazy { BytesToPublicKeyConverter(base58Check) }
    override val stringToPublicKeyConverter: Converter<String, PublicKey> by lazy { Static.stringToPublicKeyConverter }

    override val bytesToPublicKeyHashConverter: Converter<ByteArray, PublicKeyHash> by lazy { BytesToPublicKeyHashConverter(base58Check) }
    override val stringToPublicKeyHashConverter: Converter<String, PublicKeyHash> by lazy { Static.stringToPublicKeyHashConverter }

    override val bytesToBlindedPublicKeyHashConverter: Converter<ByteArray, BlindedPublicKeyHash> by lazy { BytesToBlindedPublicKeyHashConverter(base58Check) }
    override val stringToBlindedPublicKeyHashConverter: Converter<String, BlindedPublicKeyHash> by lazy { Static.stringToBlindedPublicKeyHashConverter }

    override val bytesToSignatureConverter: Converter<ByteArray, Signature> by lazy { BytesToSignatureConverter(base58Check) }
    override val stringToSignatureConverter: Converter<String, Signature> by lazy { Static.stringToSignatureConverter }

    override val signatureToGenericSignatureConverter: Converter<Signature, GenericSignature> by lazy { SignatureToGenericSignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    override val genericSignatureToEd25519SignatureConverter: Converter<GenericSignature, Ed25519Signature> by lazy { GenericSignatureToEd25519SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    override val genericSignatureToSecp256K1SignatureConverter: Converter<GenericSignature, Secp256K1Signature> by lazy { GenericSignatureToSecp256K1SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    override val genericSignatureToP256SignatureConverter: Converter<GenericSignature, P256Signature> by lazy { GenericSignatureToP256SignatureConverter(signatureBytesCoder, encodedBytesCoder) }

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

@InternalTezosSdkApi
public fun DependencyRegistry.core(): ScopedDependencyRegistry =
    if (this is ScopedDependencyRegistry) this
    else findScoped<CoreScopedDependencyRegistry>() ?: CoreScopedDependencyRegistry(this).also { addScoped(it) }