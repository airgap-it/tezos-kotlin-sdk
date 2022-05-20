package it.airgap.tezos.rpc.type.limits

import it.airgap.tezos.core.internal.type.BigInt

private const val LIMIT_PER_OPERATION_GAS = 1040000U
private const val LIMIT_PER_OPERATION_STORAGE = 60000U

private const val LIMIT_PER_BLOCK_GAS = 5200000U

// -- Limits --

public data class Limits internal constructor(
    internal val perOperation: OperationLimits,
    internal val perBlock: BlockLimits,
) {

    public constructor() : this(LIMIT_PER_OPERATION_GAS.toString(), LIMIT_PER_BLOCK_GAS.toString(), LIMIT_PER_OPERATION_STORAGE.toString())

    public constructor(
        gasPerOperation: UByte = LIMIT_PER_OPERATION_GAS.toUByte(),
        gasPerBlock: UByte = LIMIT_PER_BLOCK_GAS.toUByte(),
        storagePerOperation: UByte = LIMIT_PER_OPERATION_STORAGE.toUByte(),
    ) : this(gasPerOperation.toString(), gasPerBlock.toString(), storagePerOperation.toString())

    public constructor(
        gasPerOperation: UShort = LIMIT_PER_OPERATION_GAS.toUShort(),
        gasPerBlock: UShort = LIMIT_PER_BLOCK_GAS.toUShort(),
        storagePerOperation: UShort = LIMIT_PER_OPERATION_STORAGE.toUShort(),
    ) : this(gasPerOperation.toString(), gasPerBlock.toString(), storagePerOperation.toString())

    public constructor(
        gasPerOperation: UInt = LIMIT_PER_OPERATION_GAS,
        gasPerBlock: UInt = LIMIT_PER_BLOCK_GAS,
        storagePerOperation: UInt = LIMIT_PER_OPERATION_STORAGE,
    ) : this(gasPerOperation.toString(), gasPerBlock.toString(), storagePerOperation.toString())

    public constructor(
        gasPerOperation: ULong = LIMIT_PER_OPERATION_GAS.toULong(),
        gasPerBlock: ULong = LIMIT_PER_BLOCK_GAS.toULong(),
        storagePerOperation: ULong = LIMIT_PER_OPERATION_STORAGE.toULong(),
    ) : this(gasPerOperation.toString(), gasPerBlock.toString(), storagePerOperation.toString())

    public constructor(
        gasPerOperation: String = LIMIT_PER_OPERATION_GAS.toString(),
        gasPerBlock: String = LIMIT_PER_BLOCK_GAS.toString(),
        storagePerOperation: String = LIMIT_PER_OPERATION_STORAGE.toString(),
    ) : this(
        OperationLimits(
            BigInt.valueOf(gasPerOperation),
            BigInt.valueOf(storagePerOperation),
        ),
        BlockLimits(
            BigInt.valueOf(gasPerBlock),
        ),
    )
}

// -- OperationLimits --

public data class OperationLimits internal constructor(
    internal val gasBigInt: BigInt,
    internal val storageBigInt: BigInt,
) {
    public constructor(gas: String, storage: String) : this(BigInt.valueOf(gas), BigInt.valueOf(storage))

    public val gas: String
        get() = gasBigInt.toString()

    public val storage: String
        get() = storageBigInt.toString()

    internal operator fun plus(other: OperationLimits): OperationLimits =
        OperationLimits(gasBigInt + other.gasBigInt, storageBigInt + other.storageBigInt)

    public companion object {
        internal val zero: OperationLimits
            get() = OperationLimits(BigInt.zero, BigInt.zero)
    }
}

// -- BlockLimits

public data class BlockLimits constructor(val gas: BigInt) {
    public companion object {}
}