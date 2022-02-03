package it.airgap.tezos.core.internal.di

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

    // -- converter --

    override val bytesToAddressConverter: BytesToAddressConverter by lazy { BytesToAddressConverter(base58Check) }
    override val stringToAddressConverter: StringToAddressConverter = StringToAddressConverter()

    override val bytesToImplicitAddressConverter: BytesToImplicitAddressConverter by lazy { BytesToImplicitAddressConverter(base58Check) }
    override val stringToImplicitAddressConverter: StringToImplicitAddressConverter = StringToImplicitAddressConverter()

    override val bytesToPublicKeyEncodedConverter: BytesToPublicKeyEncodedConverter by lazy { BytesToPublicKeyEncodedConverter(base58Check) }
    override val stringToPublicKeyEncodedConverter: StringToPublicKeyEncodedConverter = StringToPublicKeyEncodedConverter()

    override val bytesToSignatureEncodedConverter: BytesToSignatureEncodedConverter by lazy { BytesToSignatureEncodedConverter(base58Check) }
    override val stringToSignatureEncodedConverter: StringToSignatureEncodedConverter = StringToSignatureEncodedConverter()
}