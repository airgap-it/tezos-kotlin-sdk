package it.airgap.tezos.michelson.micheline

import it.airgap.tezos.michelson.MichelsonData
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MichelinePrimitiveApplicationTest {

    @Test
    fun `creates MichelinePrimitiveApplication or fails if prim or annots are invalid`() {
        assertEquals(
            MichelinePrimitiveApplication(
                MichelinePrimitiveApplication.Primitive("prim"),
                emptyList(),
                emptyList(),
            ),
            MichelinePrimitiveApplication("prim"),
        )

        assertEquals(
            MichelinePrimitiveApplication(
                MichelinePrimitiveApplication.Primitive("prim"),
                listOf(MichelineLiteral.Integer(0)),
                emptyList(),
            ),
            MichelinePrimitiveApplication("prim", args = listOf(MichelineLiteral.Integer(0))),
        )

        assertEquals(
            MichelinePrimitiveApplication(
                MichelinePrimitiveApplication.Primitive("prim"),
                emptyList(),
                listOf(MichelinePrimitiveApplication.Annotation("%annot")),
            ),
            MichelinePrimitiveApplication("prim", annots = listOf("%annot")),
        )

        assertEquals(
            MichelinePrimitiveApplication(
                MichelinePrimitiveApplication.Primitive("prim"),
                listOf(MichelineLiteral.Integer(0)),
                listOf(MichelinePrimitiveApplication.Annotation("%annot")),
            ),
            MichelinePrimitiveApplication("prim", listOf(MichelineLiteral.Integer(0)), listOf("%annot")),
        )

        assertEquals(
            MichelinePrimitiveApplication(
                MichelinePrimitiveApplication.Primitive(MichelsonData.Unit.name),
                emptyList(),
                listOf(MichelinePrimitiveApplication.Annotation("%annot"))
            ),
            MichelinePrimitiveApplication(MichelsonData.Unit, annots = listOf("%annot")),
        )

        assertFailsWith<IllegalArgumentException> { MichelinePrimitiveApplication("@prim") }
        assertFailsWith<IllegalArgumentException> { MichelinePrimitiveApplication("prim", annots = listOf("annot")) }
    }

    @Test
    fun `recognizes valid and invalid Primitives`() {
        val valid = listOf(
            "prim",
            "prim1",
            "prim1_1",
            "prim1_1_2",
            "Prim",
            "Prim1",
            "Prim1_1",
            "Prim1_1_2",
            "PRIM",
            "PRIM1",
            "PRIM1_1",
            "PRIM1_1_2",
        )

        val invaid = listOf(
            "",
            "@prim",
            "-10",
        )

        valid.forEach {
            assertTrue(MichelinePrimitiveApplication.Primitive.isValid(it), "Expected `$it` to be recognized as valid Micheline Primitive.")
        }

        invaid.forEach {
            assertFalse(MichelinePrimitiveApplication.Primitive.isValid(it), "Expected `$it` to be recognized as invalid Micheline Primitive.")
        }
    }

    @Test
    fun `recognizes valid and invalid Annotations`() {
        val valid = listOf(
            "@annot",
            "@annot_1",
            "@annot.1",
            "@annot\\1",
            "@annot%1",
            "@annot@1",
            ":annot",
            "\$annot",
            "&annot",
            "%annot",
            "!annot",
            "?annot",
        )

        val invaid = listOf(
            "",
            "annot",
            "<annot",
            "%annot!",
            "-10",
        )

        valid.forEach {
            assertTrue(MichelinePrimitiveApplication.Annotation.isValid(it), "Expected `$it` to be recognized as valid Micheline Annotation.")
        }

        invaid.forEach {
            assertFalse(MichelinePrimitiveApplication.Annotation.isValid(it), "Expected `$it` to be recognized as invalid Micheline Annotation.")
        }
    }
}