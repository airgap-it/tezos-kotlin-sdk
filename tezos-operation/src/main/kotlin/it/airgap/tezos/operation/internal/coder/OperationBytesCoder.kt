package it.airgap.tezos.operation.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coder.Base58BytesCoder
import it.airgap.tezos.core.internal.coder.BytesCoder
import it.airgap.tezos.core.internal.utils.consumeAt
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent

internal class OperationBytesCoder(
    private val operationContentsBytesCoder: OperationContentBytesCoder,
    private val base58BytesCoder: Base58BytesCoder,
) : BytesCoder<Operation> {
    override fun encode(value: Operation): ByteArray = with(value) {
        val branchBytes = base58BytesCoder.encode(branch, Tezos.Prefix.BlockHash)
        val contentsBytes = encodeContents(contents)

        branchBytes + contentsBytes
    }

    override fun decode(value: ByteArray): Operation = decodeConsuming(value.toMutableList())
    fun decodeConsuming(value: MutableList<Byte>): Operation {
        val branch = base58BytesCoder.decode(value.consumeAt(0 until Tezos.Prefix.BlockHash.dataLength).toByteArray(), Tezos.Prefix.BlockHash)
        val contents = decodeContents(value)

        return Operation(branch, contents)
    }

    private fun encodeContents(contents: List<OperationContent>): ByteArray =
        contents.fold(byteArrayOf()) { acc, content -> acc + operationContentsBytesCoder.encode(content) }

    private tailrec fun decodeContents(value: MutableList<Byte>, decoded: List<OperationContent> = emptyList()): List<OperationContent> {
        if (value.isEmpty()) return decoded
        val content = operationContentsBytesCoder.decodeConsuming(value)

        return decodeContents(value, decoded + content)
    }
}
