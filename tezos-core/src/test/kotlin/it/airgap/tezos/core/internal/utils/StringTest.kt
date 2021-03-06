package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.context.TezosCoreContext.padStartEven
import org.junit.Test
import kotlin.test.assertEquals

class StringTest {
    @Test
    fun `prepends string with specified char to achieve even length`() {
        assertEquals(
            "",
            "".padStartEven('0'),
        )

        assertEquals(
            "1234",
            "1234".padStartEven('0'),
        )

        assertEquals(
            "012345",
            "12345".padStartEven('0'),
        )
    }

}