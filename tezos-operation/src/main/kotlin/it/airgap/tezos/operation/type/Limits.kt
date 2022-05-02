package it.airgap.tezos.operation.type

import it.airgap.tezos.core.internal.type.BigInt

private const val LIMIT_PER_OPERATION_GAS = 1040000U
private const val LIMIT_PER_OPERATION_STORAGE = 60000U

private const val LIMIT_PER_BLOCK_GAS = 5200000U

// -- FeeLimits --

public data class FeeLimits internal constructor(
    internal val perOperation: FeeOperationLimits,
    internal val perBlock: FeeBlockLimits,
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
        FeeOperationLimits(
            BigInt.valueOf(gasPerOperation),
            BigInt.valueOf(storagePerOperation),
        ),
        FeeBlockLimits(
            BigInt.valueOf(gasPerBlock),
        ),
    )
}

public data class FeeOperationLimits(val gas: BigInt, val storage: BigInt) {
    public operator fun plus(other: FeeOperationLimits): FeeOperationLimits =
        FeeOperationLimits(gas + other.gas, storage + other.storage)

    public companion object {
        internal val zero: FeeOperationLimits
            get() = FeeOperationLimits(BigInt.zero, BigInt.zero)
    }
}
public data class FeeBlockLimits(val gas: BigInt) {
    public companion object {
        internal val zero: FeeBlockLimits
            get() = FeeBlockLimits(BigInt.zero)
    }
}