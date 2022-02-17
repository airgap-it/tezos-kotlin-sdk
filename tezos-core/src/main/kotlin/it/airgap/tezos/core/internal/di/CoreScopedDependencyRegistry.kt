package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.*
import it.airgap.tezos.core.internal.converter.*

internal class CoreScopedDependencyRegistry(dependencyRegistry: DependencyRegistry) : ScopedDependencyRegistry, DependencyRegistry by dependencyRegistry {

    // -- coder --

    override val encodedBytesCoder: EncodedBytesCoder by lazy { EncodedBytesCoder(base58Check) }

    override val addressBytesCoder: AddressBytesCoder by lazy { AddressBytesCoder(implicitAddressBytesCoder, encodedBytesCoder) }
    override val implicitAddressBytesCoder: ImplicitAddressBytesCoder by lazy { ImplicitAddressBytesCoder(encodedBytesCoder) }

    override val publicKeyBytesCoder: PublicKeyBytesCoder by lazy { PublicKeyBytesCoder(encodedBytesCoder) }
    override val signatureBytesCoder: SignatureBytesCoder by lazy { SignatureBytesCoder(encodedBytesCoder) }

    override val zarithNaturalBytesCoder: ZarithNaturalBytesCoder = ZarithNaturalBytesCoder()
    override val zarithIntegerBytesCoder: ZarithIntegerBytesCoder by lazy { ZarithIntegerBytesCoder(zarithNaturalBytesCoder) }

    override val timestampBigIntCoder: TimestampBigIntCoder = TimestampBigIntCoder()

    // -- converter --

    override val bytesToAddressConverter: BytesToAddressConverter by lazy { BytesToAddressConverter(base58Check) }
    override val stringToAddressConverter: StringToAddressConverter = StringToAddressConverter()

    override val bytesToImplicitAddressConverter: BytesToImplicitAddressConverter by lazy { BytesToImplicitAddressConverter(base58Check) }
    override val stringToImplicitAddressConverter: StringToImplicitAddressConverter = StringToImplicitAddressConverter()

    override val bytesToPublicKeyConverter: BytesToPublicKeyConverter by lazy { BytesToPublicKeyConverter(base58Check) }
    override val stringToPublicKeyConverter: StringToPublicKeyConverter = StringToPublicKeyConverter()

    override val bytesToSignatureConverter: BytesToSignatureConverter by lazy { BytesToSignatureConverter(base58Check) }
    override val stringToSignatureConverter: StringToSignatureConverter = StringToSignatureConverter()

    override val signatureToGenericSignatureConverter: SignatureToGenericSignatureConverter by lazy { SignatureToGenericSignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    override val genericSignatureToEd25519SignatureConverter: GenericSignatureToEd25519SignatureConverter by lazy { GenericSignatureToEd25519SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    override val genericSignatureToSecp256K1SignatureConverter: GenericSignatureToSecp256K1SignatureConverter by lazy { GenericSignatureToSecp256K1SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
    override val genericSignatureToP256SignatureConverter: GenericSignatureToP256SignatureConverter by lazy { GenericSignatureToP256SignatureConverter(signatureBytesCoder, encodedBytesCoder) }
}

@InternalTezosSdkApi
public fun DependencyRegistry.core(): ScopedDependencyRegistry =
    if (this is ScopedDependencyRegistry) this
    else findScoped<CoreScopedDependencyRegistry>() ?: CoreScopedDependencyRegistry(this).also { addScoped(it) }