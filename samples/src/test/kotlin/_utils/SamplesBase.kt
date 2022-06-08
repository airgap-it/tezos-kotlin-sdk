package _utils

import org.junit.Before

abstract class SamplesBase {

    @Before
    fun setup() {
        reset()
    }

    @Suppress("LocalVariableName")
    private fun reset() {
        val TezosStatic = Class.forName("it.airgap.tezos.core.Tezos\$Static")

        val INSTANCE = TezosStatic.getDeclaredField("INSTANCE").also { it.trySetAccessible() }.get(null)
        val defaultTezos_delegate = INSTANCE::class.java.getDeclaredField("defaultTezos\$delegate").also { it.trySetAccessible() }.get(null)
        val value_field = defaultTezos_delegate::class.java.getDeclaredField("value").also { it.trySetAccessible() }
        value_field.set(defaultTezos_delegate, null)
    }
}