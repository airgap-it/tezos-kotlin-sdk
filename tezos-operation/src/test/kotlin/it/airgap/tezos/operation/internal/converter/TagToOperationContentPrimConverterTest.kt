package it.airgap.tezos.operation.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.fromTagOrNull
import it.airgap.tezos.operation.internal.di.ScopedDependencyRegistry
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TagToOperationContentPrimConverterTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var tagToOperationContentKindConverter: TagToOperationContentKindConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        tagToOperationContentKindConverter = TagToOperationContentKindConverter()

        every { dependencyRegistry.tagToOperationContentKindConverter } returns tagToOperationContentKindConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert tag to Michelson GrammarType`() {
        val valuesWithExpected = OperationContent.Kind.values.map {
            it.tag to it
        }

        valuesWithExpected.forEach {
            assertEquals(it.second, tagToOperationContentKindConverter.convert(it.first))
            assertEquals(it.second, OperationContent.Kind.fromTagOrNull(it.first))
            assertEquals(it.second, OperationContent.Kind.fromTagOrNull(it.first, tagToOperationContentKindConverter))
        }
    }

    @Test
    fun `should fail to convert unknown string to Michelson GrammarType`() {
        val tags = OperationContent.Kind.values.map { it.tag.toUInt() }.toSet()
        val unknownTags = (UByte.MIN_VALUE until UByte.MAX_VALUE).toSet().minus(tags).map { it.toUByte() }

        unknownTags.forEach {
            assertFailsWith<IllegalArgumentException> { tagToOperationContentKindConverter.convert(it) }
            assertNull(OperationContent.Kind.fromTagOrNull(it))
            assertNull(OperationContent.Kind.fromTagOrNull(it, tagToOperationContentKindConverter))
        }
    }
}