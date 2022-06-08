package it.airgap.tezos.michelson.micheline

import it.airgap.tezos.michelson.Michelson
import kotlinx.serialization.Serializable

/**
 * Micheline primitive application as defined in [the documentation](https://tezos.gitlab.io/shell/micheline.html#bnf-grammar).
 */
@Serializable
public data class MichelinePrimitiveApplication internal constructor(
    public val prim: Primitive,
    public val args: List<MichelineNode> = emptyList(),
    public val annots: List<Annotation> = emptyList(),
) : MichelineNode() {

    @Serializable
    @JvmInline public value class Primitive(public val value: String) {
        init {
            require(isValid(value)) { "Invalid primitive." }
        }

        public companion object {
            public fun isValid(value: String): Boolean = value.matches(Regex("[a-zA-Z_0-9]+"))
        }
    }

    @Serializable
    @JvmInline public value class Annotation(public val value: String) {
        init {
            require(isValid(value)) { "Invalid annotation." }
        }

        public companion object {
            public fun isValid(value: String): Boolean = value.matches(Regex("[@:\$&%!?][_0-9a-zA-Z\\\\.%@]*"))
        }
    }

    public companion object {}
}

internal fun MichelinePrimitiveApplication(
    prim: String,
    args: List<MichelineNode> = emptyList(),
    annots: List<String> = emptyList(),
): MichelinePrimitiveApplication =
    MichelinePrimitiveApplication(MichelinePrimitiveApplication.Primitive(prim), args, annots.map { MichelinePrimitiveApplication.Annotation(it) })

public fun <T : Michelson.Prim> MichelinePrimitiveApplication(
    prim: T,
    args: List<MichelineNode> = emptyList(),
    annots: List<String> = emptyList(),
): MichelinePrimitiveApplication =
    MichelinePrimitiveApplication(prim.name, args, annots)