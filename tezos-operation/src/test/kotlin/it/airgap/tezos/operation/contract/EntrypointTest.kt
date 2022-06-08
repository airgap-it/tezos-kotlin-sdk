package it.airgap.tezos.operation.contract

import org.junit.Test
import kotlin.test.assertEquals

class EntrypointTest {

    @Test
    fun `should create Entrypoint from String`() {
        assertEquals(Entrypoint.Default, Entrypoint("default"))
        assertEquals(Entrypoint.Root, Entrypoint("root"))
        assertEquals(Entrypoint.Do, Entrypoint("do"))
        assertEquals(Entrypoint.SetDelegate, Entrypoint("set_delegate"))
        assertEquals(Entrypoint.RemoveDelegate, Entrypoint("remove_delegate"))
        assertEquals(Entrypoint.Named("other"), Entrypoint("other"))
    }
}