package it.airgap.tezos.operation.contract

public sealed interface Entrypoint {
    public val value: String

    public sealed interface Kind {
        public val tag: ByteArray

        public companion object {
            internal val values: List<Kind>
                get() = listOf(
                    Default,
                    Root,
                    Do,
                    SetDelegate,
                    RemoveDelegate,
                    Named,
                )
        }
    }

    public object Default : Entrypoint, Kind {
        override val tag: ByteArray = byteArrayOf(0)
        override val value: String = "default"
    }

    public object Root : Entrypoint, Kind {
        override val tag: ByteArray = byteArrayOf(1)
        override val value: String = "root"
    }

    public object Do : Entrypoint, Kind {
        override val tag: ByteArray = byteArrayOf(2)
        override val value: String = "do"
    }

    public object SetDelegate : Entrypoint, Kind {
        override val tag: ByteArray = byteArrayOf(3)
        override val value: String = "set_delegate"
    }

    public object RemoveDelegate : Entrypoint, Kind {
        override val tag: ByteArray = byteArrayOf(4)
        override val value: String = "remove_delegate"
    }

    public data class Named(override val value: String) : Entrypoint {

        public companion object : Kind {
            override val tag: ByteArray = byteArrayOf((255).toByte())
        }
    }

    public companion object {
        public fun fromString(value: String): Entrypoint =
            when (value) {
                Default.value -> Default
                Root.value -> Root
                Do.value -> Do
                SetDelegate.value -> SetDelegate
                RemoveDelegate.value -> RemoveDelegate
                else -> Named(value)
            }
    }
}
