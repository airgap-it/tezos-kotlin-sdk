package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.context.TezosCoreContext.getOrPutWeak
import org.junit.Test
import java.lang.ref.WeakReference
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class MapTest {

    @Test
    fun `gets value or puts default as WeakReference`() {
        val map = mutableMapOf<String, WeakReference<TestReference>>()

        var strongRefPutCounter = 0
        val strongRef = TestReference()

        val h1Strong = map.getOrPutWeak("strong") {
            strongRefPutCounter++
            strongRef
        }.hashCode()

        var weakRefPutCounter = 0
        val h1Weak = map.getOrPutWeak("weak") {
            weakRefPutCounter++
            TestReference()
        }.hashCode()

        System.gc()

        val h2Strong = map.getOrPutWeak("strong") {
            strongRefPutCounter++
            strongRef
        }.hashCode()

        val h2Weak = map.getOrPutWeak("weak") {
            weakRefPutCounter++
            TestReference()
        }.hashCode()

        assertEquals(h1Strong, h2Strong, "Expected strong reference hash codes to be the same.")
        assertNotEquals(h1Weak, h2Weak, "Expected weak reference hash codes to be not the same.")

        assertEquals(1, strongRefPutCounter, "Expected strong reference default value to be created once.")
        assertEquals(2, weakRefPutCounter, "Expected weak reference default value to be created twice.")
    }

    private class TestReference
}