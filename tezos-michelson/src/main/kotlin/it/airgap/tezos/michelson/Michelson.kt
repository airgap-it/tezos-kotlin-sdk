package it.airgap.tezos.michelson

/**
 * Tezos Michelson types as defined in [the documentation](https://tezos.gitlab.io/active/michelson.html#full-grammar).
 *
 * See also: [Michelson Reference](https://tezos.gitlab.io/michelson-reference/).
 */
public sealed interface Michelson {
    public val annotations: List<Annotation>
        get() = emptyList()

    public sealed interface Prim {
        public val name: String
        public val tag: ByteArray

        public companion object {
            public val values: List<Prim>
                get() = MichelsonData.Prim.values + MichelsonType.Prim.values
        }
    }

    /**
     * Michelson annotations as defined in [the documentation](https://tezos.gitlab.io/active/michelson.html#annotations).
     */
    public sealed interface Annotation {
        public val value: String
        public fun matches(string: String): Boolean

        @JvmInline
        public value class Type(override val value: String) : Annotation {
            init {
                require(isValid(value))
            }

            override fun matches(string: String): Boolean = value.removePrefix(PREFIX) == string.removePrefix(PREFIX)

            public companion object {
                public const val PREFIX: String = ":"

                public fun isValid(value: String): Boolean = value.startsWith(PREFIX)
            }
        }

        @JvmInline
        public value class Variable(override val value: String) : Annotation {
            init {
                require(isValid(value))
            }

            override fun matches(string: String): Boolean = value.removePrefix(PREFIX) == string.removePrefix(PREFIX)

            public companion object {
                public const val PREFIX: String = "@"

                public fun isValid(value: String): Boolean = value.startsWith(PREFIX)
            }
        }

        @JvmInline
        public value class Field(override val value: String) : Annotation {
            init {
                require(isValid(value))
            }

            override fun matches(string: String): Boolean = value.removePrefix(PREFIX) == string.removePrefix(PREFIX)

            public companion object {
                public const val PREFIX: String = "%"

                public fun isValid(value: String): Boolean = value.startsWith(PREFIX)
            }
        }
    }

    public companion object {}
}