package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.comparator.isPrim
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.failWithIllegalArgument
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.fromStringOrNull
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.toCompactExpression
import it.airgap.tezos.michelson.internal.utils.*
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

internal class MichelineToMichelsonConverter(
    stringToMichelsonPrimConverter: Converter<String, Michelson.Prim>,
    michelineToCompactStringConverter: Converter<MichelineNode, String>,
) : Converter<MichelineNode, Michelson> {
    private val literalToMichelsonConverter: MichelineLiteralToMichelsonConverter = MichelineLiteralToMichelsonConverter(michelineToCompactStringConverter)
    private val primitiveApplicationToMichelsonConverter: MichelinePrimitiveApplicationToMichelsonConverter = MichelinePrimitiveApplicationToMichelsonConverter(
        stringToMichelsonPrimConverter,
        michelineToCompactStringConverter,
        this,
    )
    private val michelineSequenceToMichelsonConverter: MichelineSequenceToMichelsonConverter = MichelineSequenceToMichelsonConverter(michelineToCompactStringConverter, this)

    override fun convert(value: MichelineNode): Michelson =
        when (value) {
            is MichelineLiteral -> literalToMichelsonConverter.convert(value)
            is MichelinePrimitiveApplication -> primitiveApplicationToMichelsonConverter.convert(value)
            is MichelineSequence -> michelineSequenceToMichelsonConverter.convert(value)
        }
}

private class MichelineLiteralToMichelsonConverter(private val michelineToCompactStringConverter: Converter<MichelineNode, String>) : Converter<MichelineLiteral, Michelson> {
    override fun convert(value: MichelineLiteral): Michelson = with(value) {
        try {
            when (this) {
                is MichelineLiteral.Integer -> MichelsonData.IntConstant(int)
                is MichelineLiteral.String -> MichelsonData.StringConstant(string)
                is MichelineLiteral.Bytes -> MichelsonData.ByteSequenceConstant(bytes)
            }
        } catch (e: IllegalArgumentException) {
            failWithInvalidLiteral(this)
        }
    }

    private fun failWithInvalidLiteral(literal: MichelineLiteral): Nothing =
        failWithIllegalArgument("Invalid Micheline Literal: ${literal.toCompactExpression(michelineToCompactStringConverter)}.")
}

private class MichelinePrimitiveApplicationToMichelsonConverter(
    private val stringToMichelsonPrimConverter: Converter<String, Michelson.Prim>,
    private val michelineToCompactStringConverter: Converter<MichelineNode, String>,
    private val toMichelsonConverter: Converter<MichelineNode, Michelson>,
) : Converter<MichelinePrimitiveApplication, Michelson> {
    override fun convert(value: MichelinePrimitiveApplication): Michelson = with(value) {
        val prim = Michelson.Prim.fromStringOrNull(prim.value, stringToMichelsonPrimConverter) ?: failWithUnknownPrimitiveApplication(this)
        try {
            when (prim) {
                is MichelsonData.Prim -> fromDataPrimitiveApplication(prim, this)
                is MichelsonType.Prim -> fromTypePrimitiveApplication(prim, this)
            }
        } catch (e: IllegalArgumentException) {
            failWithInvalidPrimitiveApplication(this)
        } catch (e: NoSuchElementException) {
            failWithInvalidPrimitiveApplication(this)
        }
    }

    private fun fromDataPrimitiveApplication(
        prim: MichelsonData.Prim,
        primitiveApplication: MichelinePrimitiveApplication,
    ): MichelsonData = with(primitiveApplication) {
        when (prim) {
            MichelsonData.Unit -> MichelsonData.Unit
            MichelsonData.True -> MichelsonData.True
            MichelsonData.False -> MichelsonData.False
            MichelsonData.Pair -> {
                val values = args.convertToExpected<MichelsonData>()
                require(values.size > 1)

                MichelsonData.Pair(values)
            }
            MichelsonData.Left -> {
                val value = args.first().convertToExpected<MichelsonData>()

                MichelsonData.Left(value)
            }
            MichelsonData.Right -> {
                val value = args.first().convertToExpected<MichelsonData>()

                MichelsonData.Right(value)
            }
            MichelsonData.Some -> {
                val value = args.first().convertToExpected<MichelsonData>()

                MichelsonData.Some(value)
            }
            MichelsonData.None -> MichelsonData.None
            is MichelsonInstruction.Prim -> fromInstructionPrimitiveApplication(prim, this)
            else -> failWithUnknownPrimitiveApplication(primitiveApplication)
        }
    }

    private fun fromInstructionPrimitiveApplication(
        prim: MichelsonInstruction.Prim,
        primitiveApplication: MichelinePrimitiveApplication,
    ): MichelsonInstruction = with(primitiveApplication) {
        when (prim) {
            MichelsonInstruction.Drop -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.Drop(n)
            }
            MichelsonInstruction.Dup -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()
                val metadata = MichelsonInstruction.Dup.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Dup(n, metadata)
            }
            MichelsonInstruction.Swap -> MichelsonInstruction.Swap
            MichelsonInstruction.Dig -> {
                val n = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.Dig(n)
            }
            MichelsonInstruction.Dug -> {
                val n = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.Dug(n)
            }
            MichelsonInstruction.Push -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val value = args.second().convertToExpected<MichelsonData>()
                val metadata = MichelsonInstruction.Push.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Push(type, value, metadata)
            }
            MichelsonInstruction.Some -> {
                val metadata = MichelsonInstruction.Some.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Some(metadata)
            }
            MichelsonInstruction.None -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val metadata = MichelsonInstruction.None.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.None(type, metadata)
            }
            MichelsonInstruction.Unit -> {
                val metadata = MichelsonInstruction.Unit.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Unit(metadata)
            }
            MichelsonInstruction.Never -> MichelsonInstruction.Never
            MichelsonInstruction.IfNone -> {
                val ifBranch = args.first().convertToExpected<MichelsonInstruction.Sequence>()
                val elseBranch = args.second().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.IfNone(ifBranch, elseBranch)
            }
            MichelsonInstruction.Pair -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()
                val metadata = MichelsonInstruction.Pair.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Pair(n, metadata)
            }
            MichelsonInstruction.Car -> {
                val metadata = MichelsonInstruction.Car.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Car(metadata)
            }
            MichelsonInstruction.Cdr -> {
                val metadata = MichelsonInstruction.Cdr.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Cdr(metadata)
            }
            MichelsonInstruction.Unpair -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()
                val metadata = MichelsonInstruction.Unpair.Metadata(
                    firstVariableName = michelsonAnnotations.firstInstanceOfOrNull(),
                    secondVariableName = michelsonAnnotations.secondInstanceOfOrNull(),
                )

                MichelsonInstruction.Unpair(n, metadata)
            }
            MichelsonInstruction.Left -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val metadata = MichelsonInstruction.Left.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Left(type, metadata)
            }
            MichelsonInstruction.Right -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val metadata = MichelsonInstruction.Right.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Right(type, metadata)
            }
            MichelsonInstruction.IfLeft -> {
                val ifBranch = args.first().convertToExpected<MichelsonInstruction.Sequence>()
                val elseBranch = args.second().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.IfLeft(ifBranch, elseBranch)
            }
            MichelsonInstruction.Nil -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val metadata = MichelsonInstruction.Nil.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Nil(type, metadata)
            }
            MichelsonInstruction.Cons -> {
                val metadata = MichelsonInstruction.Cons.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Cons(metadata)
            }
            MichelsonInstruction.IfCons -> {
                val ifBranch = args.first().convertToExpected<MichelsonInstruction.Sequence>()
                val elseBranch = args.second().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.IfCons(ifBranch, elseBranch)
            }
            MichelsonInstruction.Size -> {
                val metadata = MichelsonInstruction.Size.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Size(metadata)
            }
            MichelsonInstruction.EmptySet -> {
                val type = args.first().convertToExpected<MichelsonComparableType>()
                val metadata = MichelsonInstruction.EmptySet.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.EmptySet(type, metadata)
            }
            MichelsonInstruction.EmptyMap -> {
                val keyType = args.first().convertToExpected<MichelsonComparableType>()
                val valueType = args.second().convertToExpected<MichelsonType>()
                val metadata = MichelsonInstruction.EmptyMap.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.EmptyMap(keyType, valueType, metadata)
            }
            MichelsonInstruction.EmptyBigMap -> {
                val keyType = args.first().convertToExpected<MichelsonComparableType>()
                val valueType = args.second().convertToExpected<MichelsonType>()
                val metadata = MichelsonInstruction.EmptyBigMap.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.EmptyBigMap(keyType, valueType, metadata)
            }
            MichelsonInstruction.Map -> {
                val expression = args.first().convertToExpected<MichelsonInstruction.Sequence>()
                val metadata = MichelsonInstruction.Map.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Map(expression, metadata)
            }
            MichelsonInstruction.Iter -> {
                val expression = args.first().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.Iter(expression)
            }
            MichelsonInstruction.Mem -> {
                val metadata = MichelsonInstruction.Mem.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Mem(metadata)
            }
            MichelsonInstruction.Get -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()
                val metadata = MichelsonInstruction.Get.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Get(n, metadata)
            }
            MichelsonInstruction.Update -> {
                val n = args.firstOrNull()?.convertToExpected<MichelsonData.NaturalNumberConstant>()
                val metadata = MichelsonInstruction.Update.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Update(n, metadata)
            }
            MichelsonInstruction.GetAndUpdate -> MichelsonInstruction.GetAndUpdate
            MichelsonInstruction.If -> {
                val ifBranch = args.first().convertToExpected<MichelsonInstruction.Sequence>()
                val elseBranch = args.second().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.If(ifBranch, elseBranch)
            }
            MichelsonInstruction.Loop -> {
                val body = args.first().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.Loop(body)
            }
            MichelsonInstruction.LoopLeft -> {
                val body = args.first().convertToExpected<MichelsonInstruction.Sequence>()

                MichelsonInstruction.LoopLeft(body)
            }
            MichelsonInstruction.Lambda -> {
                val parameterType = args.first().convertToExpected<MichelsonType>()
                val returnType = args.second().convertToExpected<MichelsonType>()
                val body = args.third().convertToExpected<MichelsonInstruction.Sequence>()
                val metadata = MichelsonInstruction.Lambda.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Lambda(parameterType, returnType, body, metadata)
            }
            MichelsonInstruction.Exec -> {
                val metadata = MichelsonInstruction.Exec.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Exec(metadata)
            }
            MichelsonInstruction.Apply -> MichelsonInstruction.Apply
            MichelsonInstruction.Dip -> {
                val (n, instruction) = if (args.size == 1) {
                    val instruction = args.first().convertToExpected<MichelsonInstruction.Sequence>()
                    Pair(null, instruction)
                } else {
                    val n = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()
                    val instruction = args.second().convertToExpected<MichelsonInstruction.Sequence>()
                    Pair(n, instruction)
                }

                MichelsonInstruction.Dip(instruction, n)
            }
            MichelsonInstruction.Failwith -> MichelsonInstruction.Failwith
            MichelsonInstruction.Cast -> {
                val metadata = MichelsonInstruction.Cast.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Cast(metadata)
            }
            MichelsonInstruction.Rename -> {
                val metadata = MichelsonInstruction.Rename.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Rename(metadata)
            }
            MichelsonInstruction.Concat -> {
                val metadata = MichelsonInstruction.Concat.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Concat(metadata)
            }
            MichelsonInstruction.Slice -> MichelsonInstruction.Slice
            MichelsonInstruction.Pack -> MichelsonInstruction.Pack
            MichelsonInstruction.Unpack -> {
                val type = args.first().convertToExpected<MichelsonType>()

                MichelsonInstruction.Unpack(type)
            }
            MichelsonInstruction.Add -> {
                val metadata = MichelsonInstruction.Add.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Add(metadata)
            }
            MichelsonInstruction.Sub -> {
                val metadata = MichelsonInstruction.Sub.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Sub(metadata)
            }
            MichelsonInstruction.Mul -> {
                val metadata = MichelsonInstruction.Mul.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Mul(metadata)
            }
            MichelsonInstruction.Ediv -> {
                val metadata = MichelsonInstruction.Ediv.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Ediv(metadata)
            }
            MichelsonInstruction.Abs -> {
                val metadata = MichelsonInstruction.Abs.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Abs(metadata)
            }
            MichelsonInstruction.Isnat -> {
                val metadata = MichelsonInstruction.Isnat.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Isnat(metadata)
            }
            MichelsonInstruction.Int -> {
                val metadata = MichelsonInstruction.Int.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Int(metadata)
            }
            MichelsonInstruction.Neg -> {
                val metadata = MichelsonInstruction.Neg.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Neg(metadata)
            }
            MichelsonInstruction.Lsl -> {
                val metadata = MichelsonInstruction.Lsl.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Lsl(metadata)
            }
            MichelsonInstruction.Lsr -> {
                val metadata = MichelsonInstruction.Lsr.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Lsr(metadata)
            }
            MichelsonInstruction.Or -> {
                val metadata = MichelsonInstruction.Or.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Or(metadata)
            }
            MichelsonInstruction.And -> {
                val metadata = MichelsonInstruction.And.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.And(metadata)
            }
            MichelsonInstruction.Xor -> {
                val metadata = MichelsonInstruction.Xor.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Xor(metadata)
            }
            MichelsonInstruction.Not -> {
                val metadata = MichelsonInstruction.Not.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Not(metadata)
            }
            MichelsonInstruction.Compare -> {
                val metadata = MichelsonInstruction.Compare.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Compare(metadata)
            }
            MichelsonInstruction.Eq -> {
                val metadata = MichelsonInstruction.Eq.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Eq(metadata)
            }
            MichelsonInstruction.Neq -> {
                val metadata = MichelsonInstruction.Neq.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Neq(metadata)
            }
            MichelsonInstruction.Lt -> {
                val metadata = MichelsonInstruction.Lt.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Lt(metadata)
            }
            MichelsonInstruction.Gt -> {
                val metadata = MichelsonInstruction.Gt.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Gt(metadata)
            }
            MichelsonInstruction.Le -> {
                val metadata = MichelsonInstruction.Le.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Le(metadata)
            }
            MichelsonInstruction.Ge -> {
                val metadata = MichelsonInstruction.Ge.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Ge(metadata)
            }
            MichelsonInstruction.Self -> {
                val metadata = MichelsonInstruction.Self.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Self(metadata)
            }
            MichelsonInstruction.SelfAddress -> {
                val metadata = MichelsonInstruction.SelfAddress.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.SelfAddress(metadata)
            }
            MichelsonInstruction.Contract -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val metadata = MichelsonInstruction.Contract.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Contract(type, metadata)
            }
            MichelsonInstruction.TransferTokens -> MichelsonInstruction.TransferTokens
            MichelsonInstruction.SetDelegate -> {
                val metadata = MichelsonInstruction.SetDelegate.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.SetDelegate(metadata)
            }
            MichelsonInstruction.CreateContract -> {
                val parameterType = args.first().convertToExpected<MichelsonType>()
                val storageType = args.second().convertToExpected<MichelsonType>()
                val code = args.third().convertToExpected<MichelsonInstruction.Sequence>()
                val metadata = MichelsonInstruction.CreateContract.Metadata(
                    firstVariableName = michelsonAnnotations.firstInstanceOfOrNull(),
                    secondVariableName = michelsonAnnotations.secondInstanceOfOrNull(),
                )

                MichelsonInstruction.CreateContract(parameterType, storageType, code, metadata)
            }
            MichelsonInstruction.ImplicitAccount -> {
                val metadata = MichelsonInstruction.ImplicitAccount.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.ImplicitAccount(metadata)
            }
            MichelsonInstruction.VotingPower -> MichelsonInstruction.VotingPower
            MichelsonInstruction.Now -> {
                val metadata = MichelsonInstruction.Now.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Now(metadata)
            }
            MichelsonInstruction.Level -> {
                val metadata = MichelsonInstruction.Level.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Level(metadata)
            }
            MichelsonInstruction.Amount -> {
                val metadata = MichelsonInstruction.Amount.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Amount(metadata)
            }
            MichelsonInstruction.Balance -> {
                val metadata = MichelsonInstruction.Balance.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Balance(metadata)
            }
            MichelsonInstruction.CheckSignature -> {
                val metadata = MichelsonInstruction.CheckSignature.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.CheckSignature(metadata)
            }
            MichelsonInstruction.Blake2B -> {
                val metadata = MichelsonInstruction.Blake2B.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Blake2B(metadata)
            }
            MichelsonInstruction.Keccak -> {
                val metadata = MichelsonInstruction.Keccak.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Keccak(metadata)
            }
            MichelsonInstruction.Sha3 -> {
                val metadata = MichelsonInstruction.Sha3.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Sha3(metadata)
            }
            MichelsonInstruction.Sha256 -> {
                val metadata = MichelsonInstruction.Sha256.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Sha256(metadata)
            }
            MichelsonInstruction.Sha512 -> {
                val metadata = MichelsonInstruction.Sha512.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Sha512(metadata)
            }
            MichelsonInstruction.HashKey -> {
                val metadata = MichelsonInstruction.HashKey.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.HashKey(metadata)
            }
            MichelsonInstruction.Source -> {
                val metadata = MichelsonInstruction.Source.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Source(metadata)
            }
            MichelsonInstruction.Sender -> {
                val metadata = MichelsonInstruction.Sender.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Sender(metadata)
            }
            MichelsonInstruction.Address -> {
                val metadata = MichelsonInstruction.Address.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.Address(metadata)
            }
            MichelsonInstruction.ChainId -> {
                val metadata = MichelsonInstruction.ChainId.Metadata(
                    variableName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonInstruction.ChainId(metadata)
            }
            MichelsonInstruction.TotalVotingPower -> MichelsonInstruction.TotalVotingPower
            MichelsonInstruction.PairingCheck -> MichelsonInstruction.PairingCheck
            MichelsonInstruction.SaplingEmptyState -> {
                val memoSize = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()

                MichelsonInstruction.SaplingEmptyState(memoSize)
            }
            MichelsonInstruction.SaplingVerifyUpdate -> MichelsonInstruction.SaplingVerifyUpdate
            MichelsonInstruction.Ticket -> MichelsonInstruction.Ticket
            MichelsonInstruction.ReadTicket -> MichelsonInstruction.ReadTicket
            MichelsonInstruction.SplitTicket -> MichelsonInstruction.SplitTicket
            MichelsonInstruction.JoinTickets -> MichelsonInstruction.JoinTickets
            MichelsonInstruction.OpenChest -> MichelsonInstruction.OpenChest
        }
    }

    private fun fromTypePrimitiveApplication(
        prim: MichelsonType.Prim,
        primitiveApplication: MichelinePrimitiveApplication,
    ): MichelsonType = with(primitiveApplication) {
        when (prim) {
            MichelsonType.Parameter -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Parameter(type, metadata)
            }
            MichelsonType.Storage -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Storage(type, metadata)
            }
            MichelsonType.Code -> {
                val code = args.first().convertToExpected<MichelsonInstruction>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Code(code, metadata)
            }
            MichelsonType.Option -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                if (type is MichelsonComparableType) MichelsonComparableType.Option(type, metadata)
                else MichelsonType.Option(type, metadata)
            }
            MichelsonType.List -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.List(type, metadata)
            }
            MichelsonType.Set -> {
                val type = args.first().convertToExpected<MichelsonComparableType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Set(type, metadata)
            }
            MichelsonType.Operation -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Operation(metadata)
            }
            MichelsonType.Contract -> {
                val type = args.first().convertToExpected<MichelsonType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Contract(type, metadata)
            }
            MichelsonType.Ticket -> {
                val type = args.first().convertToExpected<MichelsonComparableType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Ticket(type, metadata)
            }
            MichelsonType.Pair -> {
                val types = args.convertToExpected<MichelsonType>()
                require(types.size > 1)
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                @Suppress("UNCHECKED_CAST")
                if (types.allIsInstance<MichelsonComparableType>()) MichelsonComparableType.Pair(types as List<MichelsonComparableType>, metadata)
                else MichelsonType.Pair(types, metadata)
            }
            MichelsonType.Or -> {
                val lhs = args.first().convertToExpected<MichelsonType>()
                val rhs = args.second().convertToExpected<MichelsonType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                if (lhs is MichelsonComparableType && rhs is MichelsonComparableType) MichelsonComparableType.Or(lhs, rhs, metadata)
                else MichelsonType.Or(lhs, rhs, metadata)
            }
            MichelsonType.Lambda -> {
                val parameterType = args.first().convertToExpected<MichelsonType>()
                val returnType = args.second().convertToExpected<MichelsonType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Lambda(parameterType, returnType, metadata)
            }
            MichelsonType.Map -> {
                val keyType = args.first().convertToExpected<MichelsonComparableType>()
                val valueType = args.second().convertToExpected<MichelsonType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Map(keyType, valueType, metadata)
            }
            MichelsonType.BigMap -> {
                val keyType = args.first().convertToExpected<MichelsonComparableType>()
                val valueType = args.second().convertToExpected<MichelsonType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.BigMap(keyType, valueType, metadata)
            }
            MichelsonType.Bls12_381G1 -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Bls12_381G1(metadata)
            }
            MichelsonType.Bls12_381G2 -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Bls12_381G2(metadata)
            }
            MichelsonType.Bls12_381Fr -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Bls12_381Fr(metadata)
            }
            MichelsonType.SaplingTransaction -> {
                val memoSize = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.SaplingTransaction(memoSize, metadata)
            }
            MichelsonType.SaplingState -> {
                val memoSize = args.first().convertToExpected<MichelsonData.NaturalNumberConstant>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.SaplingState(memoSize, metadata)
            }
            MichelsonType.Chest -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.Chest(metadata)
            }
            MichelsonType.ChestKey -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonType.ChestKey(metadata)
            }
            is MichelsonComparableType.Prim -> fromComparableTypePrimitiveApplication(prim, primitiveApplication)
        }
    }

    private fun fromComparableTypePrimitiveApplication(
        prim: MichelsonComparableType.Prim,
        primitiveApplication: MichelinePrimitiveApplication,
    ): MichelsonComparableType = with(primitiveApplication) {
        when (prim) {
            MichelsonComparableType.Unit -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Unit(metadata)
            }
            MichelsonComparableType.Never -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Never(metadata)
            }
            MichelsonComparableType.Bool -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Bool(metadata)
            }
            MichelsonComparableType.Int -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Int(metadata)
            }
            MichelsonComparableType.Nat -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Nat(metadata)
            }
            MichelsonComparableType.String -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.String(metadata)
            }
            MichelsonComparableType.ChainId -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.ChainId(metadata)
            }
            MichelsonComparableType.Bytes -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Bytes(metadata)
            }
            MichelsonComparableType.Mutez -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Mutez(metadata)
            }
            MichelsonComparableType.KeyHash -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.KeyHash(metadata)
            }
            MichelsonComparableType.Key -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Key(metadata)
            }
            MichelsonComparableType.Signature -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Signature(metadata)
            }
            MichelsonComparableType.Timestamp -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Timestamp(metadata)
            }
            MichelsonComparableType.Address -> {
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Address(metadata)
            }
            MichelsonComparableType.Option -> {
                val type = args.first().convertToExpected<MichelsonComparableType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Option(type, metadata)
            }
            MichelsonComparableType.Or -> {
                val lhs = args.first().convertToExpected<MichelsonComparableType>()
                val rhs = args.second().convertToExpected<MichelsonComparableType>()
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Or(lhs, rhs, metadata)
            }
            MichelsonComparableType.Pair -> {
                val types = args.convertToExpected<MichelsonComparableType>()
                require(types.size > 1)
                val metadata = MichelsonType.Metadata(
                    typeName = michelsonAnnotations.firstInstanceOfOrNull(),
                    fieldName = michelsonAnnotations.firstInstanceOfOrNull(),
                )

                MichelsonComparableType.Pair(types, metadata)
            }
        }
    }

    private inline fun <reified T : Michelson> MichelineNode.convertToExpected(): T =
        when (T::class) {
            MichelsonData.NaturalNumberConstant::class -> toMichelsonConverter.convert(this)
                .also { require(it is MichelsonData.IntConstant) }
                .run { this as MichelsonData.IntConstant }
                .run { MichelsonData.NaturalNumberConstant(value) } as T
            else -> toMichelsonConverter.convert(this).also { require(it is T) } as T
        }

    private inline fun <reified T : Michelson> List<MichelineNode>.convertToExpected(): List<T> =
        map { it.convertToExpected() }

    private val MichelinePrimitiveApplication.michelsonAnnotations: List<Michelson.Annotation>
        get() = annots.mapNotNull { it.asMichelsonAnnotationOrNull() }

    private fun MichelinePrimitiveApplication.Annotation.asMichelsonAnnotationOrNull(): Michelson.Annotation? =
        when {
            Michelson.Annotation.Type.isValid(value) -> Michelson.Annotation.Type(value)
            Michelson.Annotation.Variable.isValid(value) -> Michelson.Annotation.Variable(value)
            Michelson.Annotation.Field.isValid(value) -> Michelson.Annotation.Field(value)
            else -> null
        }

    private fun failWithUnknownPrimitiveApplication(primitiveApplication: MichelinePrimitiveApplication): Nothing =
        failWithIllegalArgument("Unknown Micheline Primitive Application: ${primitiveApplication.toCompactExpression(michelineToCompactStringConverter)}.")

    private fun failWithInvalidPrimitiveApplication(primitiveApplication: MichelinePrimitiveApplication): Nothing =
        failWithIllegalArgument("Invalid Micheline Primitive Application: ${primitiveApplication.toCompactExpression(michelineToCompactStringConverter)}.")
}

private class MichelineSequenceToMichelsonConverter(
    private val michelineToCompactStringConverter: Converter<MichelineNode, String>,
    private val toMichelsonConverter: MichelineToMichelsonConverter,
) : Converter<MichelineSequence, Michelson> {
    @Suppress("UNCHECKED_CAST")
    override fun convert(value: MichelineSequence): Michelson {
        val michelsonValues = value.nodes.map {
            if (it.isPrim(MichelsonData.Elt)) it.convertToElt()
            else toMichelsonConverter.convert(it)
        }
        with(michelsonValues) {
            return when {
                isEmpty() -> MichelsonData.Sequence(emptyList())
                allIsInstance<MichelsonInstruction>() -> MichelsonInstruction.Sequence(this as List<MichelsonInstruction>)
                allIsInstance<MichelsonData.Elt>() -> MichelsonData.EltSequence(this as List<MichelsonData.Elt>)
                anyIsInstance<MichelsonData.Elt>() /* any but not all */ -> failWithInvalidSequence(value)
                allIsInstance<MichelsonData>() -> MichelsonData.Sequence(this as List<MichelsonData>)
                else -> failWithUnknownSequence(value)
            }
        }
    }

    private fun MichelinePrimitiveApplication.convertToElt(): MichelsonData.Elt {
        val key = args.first().convertToExpected<MichelsonData>()
        val value = args.second().convertToExpected<MichelsonData>()

        return MichelsonData.Elt(key, value)
    }

    private inline fun <reified T : Michelson> MichelineNode.convertToExpected(): T =
        when (T::class) {
            MichelsonData.NaturalNumberConstant::class -> toMichelsonConverter.convert(this)
                .also { require(it is MichelsonData.IntConstant) }
                .run { this as MichelsonData.IntConstant }
                .run { MichelsonData.NaturalNumberConstant(value) } as T
            else -> toMichelsonConverter.convert(this).also { require(it is T) } as T
        }

    private fun failWithUnknownSequence(sequence: MichelineSequence): Nothing =
        failWithIllegalArgument("Unknown Micheline Sequence: ${sequence.toCompactExpression(michelineToCompactStringConverter)}.")

    private fun failWithInvalidSequence(sequence: MichelineSequence): Nothing =
        failWithIllegalArgument("Invalid Micheline Sequence: ${sequence.toCompactExpression(michelineToCompactStringConverter)}.")
}
