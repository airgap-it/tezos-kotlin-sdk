package it.airgap.tezos.operation.internal.coder

import it.airgap.tezos.core.decodeConsumingFromBytes
import it.airgap.tezos.core.encodeToBytes
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent

@InternalTezosSdkApi
public class OperationBytesCoder(
    private val operationContentsBytesCoder: OperationContentBytesCoder,
    private val encodedBytesCoder: EncodedBytesCoder,
) : ConsumingBytesCoder<Operation> {
    override fun encode(value: Operation): ByteArray = with(value) {
        val branchBytes = branch.encodeToBytes(encodedBytesCoder)
        val contentsBytes = encodeContents(contents)

        branchBytes + contentsBytes
    }

    override fun decode(value: ByteArray): Operation = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): Operation {
        val branch = BlockHash.decodeConsumingFromBytes(value, encodedBytesCoder)
        val contents = decodeContents(value)

        return Operation.Unsigned(branch, contents)
    }

    private fun encodeContents(contents: List<OperationContent>): ByteArray =
        contents.fold(byteArrayOf()) { acc, content -> acc + operationContentsBytesCoder.encode(content) }

    private tailrec fun decodeContents(value: MutableList<Byte>, decoded: List<OperationContent> = emptyList()): List<OperationContent> {
        if (value.isEmpty()) return decoded
        val content = operationContentsBytesCoder.decodeConsuming(value)

        return decodeContents(value, decoded + content)
    }
}
