package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

internal object MichelineToStringConverter : Converter<MichelineNode, String> {
    override fun convert(value: MichelineNode): String =
        when (value) {
            is MichelineLiteral -> literalToString(value)
            is MichelinePrimitiveApplication -> primitiveApplicationToString(value)
            is MichelineSequence -> sequenceToString(value)
        }

    private fun literalToString(literal: MichelineLiteral): String = with(literal) {
        when (this) {
            is MichelineLiteral.Integer -> int
            is MichelineLiteral.String -> string
            is MichelineLiteral.Bytes -> bytes
        }
    }

    private fun primitiveApplicationToString(primitiveApplication: MichelinePrimitiveApplication): String = with(primitiveApplication) {
        val prim = prim.value
        val annots = annots.joinToString(" ") { it.value }
        val args = args.joinToString(" ") { if (it is MichelinePrimitiveApplication) "(${convert(it)})" else convert(it) }

        when {
            annots.isBlank() && args.isBlank() -> prim
            args.isBlank() -> "$prim $annots"
            annots.isBlank() -> "$prim $args"
            else -> "$prim $annots $args"
        }
    }

    private fun sequenceToString(sequence: MichelineSequence): String = with(sequence) {
        when {
            nodes.isEmpty() -> "{ }"
            else -> nodes.joinToString(" ; ", prefix = "{ ", postfix = " }") { convert(it) }
        }
    }
}

internal object MichelineToCompactStringConverter : Converter<MichelineNode, String> {
    override fun convert(value: MichelineNode): String =
        when (value) {
            is MichelineLiteral -> literalToCompatString(value)
            is MichelinePrimitiveApplication -> primitiveApplicationToCompatString(value)
            is MichelineSequence -> sequenceToCompatString(value)
        }

    private fun literalToCompatString(literal: MichelineLiteral): String = with(literal) {
        when (this) {
            is MichelineLiteral.Integer -> int
            is MichelineLiteral.String -> string
            is MichelineLiteral.Bytes -> bytes
        }
    }

    private fun primitiveApplicationToCompatString(primitiveApplication: MichelinePrimitiveApplication): String = with(primitiveApplication) {
        val prim = prim.value
        val annots = annotationsToCompatString(annots)
        val args = argumentsToCompatString(args)

        when {
            annots.isBlank() && args.isBlank() -> prim
            args.isBlank() -> "$prim $annots"
            annots.isBlank() -> "$prim $args"
            else -> "$prim $annots $args"
        }
    }

    private fun annotationsToCompatString(annotations: List<MichelinePrimitiveApplication.Annotation>): String {
        val separator = when (annotations.size) {
            2 -> " "
            else -> " ... "
        }

        return listOfNotNull(
            annotations.firstOrNull(),
            if (annotations.size > 1) annotations.lastOrNull() else null,
        ).joinToString(separator) { it.value }
    }

    private fun argumentsToCompatString(arguments: List<MichelineNode>): String {
        val separator = when (arguments.size) {
            2 -> " "
            else -> " ... "
        }

        return listOfNotNull(
            arguments.firstOrNull(),
            if (arguments.size > 1) arguments.lastOrNull() else null,
        ).joinToString(separator) {
            if (it is MichelinePrimitiveApplication) {
                if (it.args.isNotEmpty() || it.annots.isNotEmpty()) "(${it.prim.value} ...)" else "(${it.prim.value})"
            } else convert(it)
        }
    }

    private fun sequenceToCompatString(sequence: MichelineSequence): String = with(sequence) {
        when {
            nodes.isEmpty() -> "{ }"
            nodes.size == 2 -> nodes.joinToString(" ; ", prefix = "{ ", postfix = " }") { convert(it) }
            else -> listOfNotNull(
                nodes.first(),
                if (nodes.size > 1) nodes.lastOrNull() else null,
            ).joinToString(" ; ... ; ", prefix = "{ ", postfix = " }") { convert(it) }
        }
    }
}