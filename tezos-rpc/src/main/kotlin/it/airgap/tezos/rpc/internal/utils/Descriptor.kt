package it.airgap.tezos.rpc.internal.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.ClassSerialDescriptorBuilder
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.descriptors.elementNames

@OptIn(ExperimentalSerializationApi::class)
internal fun ClassSerialDescriptorBuilder.elementsFrom(descriptor: SerialDescriptor) {
    with(descriptor) {
        elementNames.zip(elementDescriptors).forEach { (name, descriptor) ->
            element(name, descriptor)
        }
    }
}