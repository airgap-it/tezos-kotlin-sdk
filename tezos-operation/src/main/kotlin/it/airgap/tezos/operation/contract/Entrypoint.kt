package it.airgap.tezos.operation.contract

public sealed interface Entrypoint {
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
    }

    public object Root : Entrypoint, Kind {
        override val tag: ByteArray = byteArrayOf(1)
    }

    public object Do : Entrypoint, Kind {
        override val tag: ByteArray = byteArrayOf(2)
    }

    public object SetDelegate : Entrypoint, Kind {
        override val tag: ByteArray = byteArrayOf(3)
    }

    public object RemoveDelegate : Entrypoint, Kind {
        override val tag: ByteArray = byteArrayOf(4)
    }

    public data class Named(public val value: String) : Entrypoint {

        public companion object : Kind {
            override val tag: ByteArray = byteArrayOf((255).toByte())
        }
    }

    public companion object {}
}
