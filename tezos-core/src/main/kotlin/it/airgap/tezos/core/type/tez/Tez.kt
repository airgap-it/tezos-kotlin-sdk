package it.airgap.tezos.core.type.tez

@JvmInline
public value class Tez(public val value: String) {
    public constructor(value: Byte) : this(value.toString())
    public constructor(value: Short) : this(value.toString())
    public constructor(value: Int) : this(value.toString())
    public constructor(value: Long) : this(value.toString())
}

@JvmInline
public value class Mutez(public val value: String) {
    public constructor(value: Byte) : this(value.toString())
    public constructor(value: Short) : this(value.toString())
    public constructor(value: Int) : this(value.toString())
    public constructor(value: Long) : this(value.toString())
}

@JvmInline
public value class Nanotez(public val value: String) {
    public constructor(value: Byte) : this(value.toString())
    public constructor(value: Short) : this(value.toString())
    public constructor(value: Int) : this(value.toString())
    public constructor(value: Long) : this(value.toString())
}