package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.*
import it.airgap.tezos.core.internal.converter.*
import it.airgap.tezos.core.type.encoded.*

internal class CoreScopedDependencyRegistry(dependencyRegistry: DependencyRegistry) : ScopedDependencyRegistry, DependencyRegistry by dependencyRegistry {

    // -- coder --

    override val encodedBytesCoder: EncodedBytesCoder by lazy { EncodedBytesCoder(base58Check) }

    override val addressBytesCoder: AddressBytesCoder by lazy { AddressBytesCoder(implicitAddressBytesCoder, encodedBytesCoder) }
    override val implicitAddressBytesCoder: ImplicitAddressBytesCoder by lazy { ImplicitAddressBytesCoder(encodedBytesCoder) }

    override val publicKeyBytesCoder: PublicKeyBytesCoder by lazy { PublicKeyBytesCoder(encodedBytesCoder) }
    override val signatureBytesCoder: SignatureBytesCoder by lazy { SignatureBytesCoder(encodedBytesCoder) }

    override val zarithNaturalBytesCoder: ZarithNaturalBytesCoder = ZarithNaturalBytesCoder()
    override val zarithIntegerBytesCoder: ZarithIntegerBytesCoder by lazy { ZarithIntegerBytesCoder(zarithNaturalBytesCoder) }

    override val tezBytesCoder: TezBytesCoder by lazy { TezBytesCoder(zarithNaturalBytesCoder) }
    override val mutezBytesCoder: MutezBytesCoder by lazy { MutezBytesCoder(zarithNaturalBytesCoder) }
    override val nanotezBytesCoder: NanotezBytesCoder by lazy { NanotezBytesCoder(zarithNaturalBytesCoder) }

    override val timestampBigIntCoder: TimestampBigIntCoder = TimestampBigIntCoder()

    // -- converter --

    override val bytesToAddressConverter: Converter<ByteArray, Address> by lazy { BytesToAddressConverter(base58Check) }
    override val stringToAddressConverter: Converter<String, Address> by lazy { StringToAddressConverter() }

    override val bytesToImplicitAddressConverter: Converter<ByteArray, ImplicitAddress> by lazy { BytesToImplicitAddressConverter(base58Check) }
    override val stringToImplicitAddressConverter: Converter<String, ImplicitAddress> by lazy { StringToImplicitAddressConverter() }

    override val bytesToPublicKeyConverter: Converter<ByteArray, PublicKey> by lazy { BytesToPublicKeyConverter(base58Check) }
    override val stringToPublicKeyConverter: Converter<String, PublicKey> = StringToPublicKeyConverter()

    override val bytesToPublicKeyHashConverter: Converter<ByteArray, PublicKeyHash> by lazy { BytesToPublicKeyHashConverter(base58Check) }
    override val stringToPublicKeyHashConverter: Converter<String, PublicKeyHash> = StringToPublicKeyHashConverter()

    override val bytesToBlindedPublicKeyHashConverter: Converter<ByteArray, BlindedPublicKeyHash> by lazy { BytesToBlindedPublicKeyHashConverter(base58Check) }
    override val stringToBlindedPublicKeyHashConverter: Converter<String, BlindedPublicKeyHash> = StringToBlindedPublicKeyHashConverter()

    override val bytesToSignatureConverter: Converter<ByteArray, Signature> by lazy { BytesToSignatureConverter(base58Check) }
    override val stringToSignatureConverter: Converter<String, Signature> = StringToSignatureConverter()

    override val signatureToGenericSignatureConverter: Converter<Signature, GenericSignature> by lazy { SignatureToGenericSignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    override val genericSignatureToEd25519SignatureConverter: Converter<GenericSignature, Ed25519Signature> by lazy { GenericSignatureToEd25519SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    override val genericSignatureToSecp256K1SignatureConverter: Converter<GenericSignature, Secp256K1Signature> by lazy { GenericSignatureToSecp256K1SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    override val genericSignatureToP256SignatureConverter: Converter<GenericSignature, P256Signature> by lazy { GenericSignatureToP256SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
}

@InternalTezosSdkApi
public fun DependencyRegistry.core(): ScopedDependencyRegistry =
    if (this is ScopedDependencyRegistry) this
    else findScoped<CoreScopedDependencyRegistry>() ?: CoreScopedDependencyRegistry(this).also { addScoped(it) }