package it.airgap.tezos.operation.internal.converter

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.internal.context.TezosOperationContext.failWithIllegalArgument

internal class TagToOperationContentKindConverter : Converter<UByte, OperationContent.Kind> {
    override fun convert(value: UByte): OperationContent.Kind =
        OperationContent.Kind.values.firstOrNull { it.tag == value } ?: failWithUnknownKind(value)
}

private fun failWithUnknownKind(kind: UByte): Nothing =
    failWithIllegalArgument("Unknown OperationContent kind: \"$kind\".")