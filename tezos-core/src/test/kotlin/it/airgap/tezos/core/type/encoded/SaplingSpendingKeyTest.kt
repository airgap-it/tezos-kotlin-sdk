package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class SaplingSpendingKeyTest {

    @Test
    fun `should recognize valid and invalid SaplingSpendingKey strings`() {
        validStrings.forEach {
            assertTrue(SaplingSpendingKey.isValid(it), "Expected `$it` to be recognized as valid SaplingSpendingKey string.")
        }

        invalidStrings.forEach {
            assertFalse(SaplingSpendingKey.isValid(it), "Expected `$it` to be recognized as invalid SaplingSpendingKey string.")
        }
    }

    @Test
    fun `should recognize valid and invalid SaplingSpendingKey bytes`() {
        validBytes.forEach {
            assertTrue(SaplingSpendingKey.isValid(it), "Expected `$it` to be recognized as valid SaplingSpendingKey.")
            assertTrue(SaplingSpendingKey.isValid(it.toList()), "Expected `$it` to be recognized as valid SaplingSpendingKey.")
        }

        invalidBytes.forEach {
            assertFalse(SaplingSpendingKey.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid SaplingSpendingKey.")
            assertFalse(SaplingSpendingKey.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid SaplingSpendingKey.")
        }
    }

    @Test
    fun `should create SaplingSpendingKey from valid string`() {
        validStrings.forEach {
            assertEquals(it, SaplingSpendingKey.createValue(it).base58)
            assertEquals(it, SaplingSpendingKey.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create SaplingSpendingKey from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { SaplingSpendingKey.createValue(it) }
            assertNull(SaplingSpendingKey.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "sask32oucSCf5mTFNVZAdUmJwYaFXvixXcZwD1MkKfHRExJDz8mUUQFeQr81VuyU5JQY67As9KQbyutg8HEnStwhw9fciJfdpUwWVj59hyBpg7mMRSfU84NCNAZQuzhDvwR381nsgdGHmkicH1Kh7Ea15Q4q49vyMjB1Ckui92bXfJL8kYfgTmFsfNZTcxtjVJc8d2RGHTdCroQuFiZPUYUrTc6qvgNiyFHtgqiwq7AM9nhGX",
            "sask2YvDzg86hVLPNK51AJ6ZNgpFkvXJx2GdwVSDbLgkifj9TdXW98MitJU4UmJRiku2Q7LEkBmtexG6dEa4Dk2JHyWSRTtiviPChQw1Vdu9E7QwUaptSWmMCzPGDJBLYkmkuWmC13hq8gSs2RFyj5kzxS2GdaekUhD4GUHXVoC8u5ayWMQWWT4fVuKFScZiAUzRGq4izQH2EbSoQo27ZrmHG9Tqxa1mPhJRBwHCSkLBionRL",
            "sask4DR42rXymf65Nv9ytbJvhMNEZ9SQmLQiEoZDek7djTW7Tvx9169HvBGieaGGwcqr78EEzd2WQ6KKFTndUrmizR4aw6sxq9PC5sW7jJGfG3gZQs6zV8ZySxuwmyCFSyyn66TUKr2LWewzSC446vojPxSbmT88XWRTXD8CKYSzhfTWE6yEuSWxnzGB6yvjH7AeffMXZby9QJ4H4oADeG568DN4tja3pqQSJJS9FhW6Ls4bW",
            "sask3LwvrjkBrVyiNBphv2Ww6PXcaWjV3RWhqCVmZecN1KPfyFUBaAPMpbD9DKtpoy6fuP1GqVEr2A9YD9aEGXPi2Ws4ACYUzmTMhND4zJParDE54izi8LNm6Jd2uEVqiF5jqTYZr7xBDGrrEgCyMQfnEYguXP3HKHY24NPce1GFd4hwxF4HJFxVjTJWTyAgGhsrd1K2rmVgVNeyknrcmMPP5utujVRKzncQWYxm92vBSUrMt",
            "sask2awbrt58qZR5i973ctTd6fDD4z22FD6UNEtPNEDE4GdmB6YAaHLuijagJuAbS3weQJRMVV4uhZzt6w2wY1FAxss1xd9W9Znbg4NBkGL8L7LRzwG512WtJGpoaBGqU918R5dgUEKJqyLPKPVwCKfKMWEgcKdsRBSdgnKjpFZa7wvbRCyv37WvMy6DaeTh8FhnTA2qWisGM3VDN42XrQHY3EEnGVNAXwKryUAM2WKUM3bWa",
            "sask4A51FeVvhJnfoFbjzArsaXkmdYFYeHnA7fbVhLZBdS4WnZFWmGVeELqqiKEFjcfhP6NXZWbPYSRWFGiQP1ZYQ1fKEuhWUx4ipuRrmy69mNf3NJJ9MFwuqcwJagw1SkE9nhUzXHsejtH8r8sA2pTHkNJJWjiYTuDjTQfE4xwz9htQuXGJipeQb333kGZVymoV2K58b9BMNXKDwgz2Sa3DHHk34H2tr8vFQJ8Y2PKJtgDVq",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "32oucSCf5mTFNVZAdUmJwYaFXvixXcZwD1MkKfHRExJDz8mUUQFeQr81VuyU5JQY67As9KQbyutg8HEnStwhw9fciJfdpUwWVj59hyBpg7mMRSfU84NCNAZQuzhDvwR381nsgdGHmkicH1Kh7Ea15Q4q49vyMjB1Ckui92bXfJL8kYfgTmFsfNZTcxtjVJc8d2RGHTdCroQuFiZPUYUrTc6qvgNiyFHtgqiwq7AM9nhGX",
            "sask2YvDzg86hVLPNK51AJ6ZNgpFkvXJx2GdwVSDbLgkifj9TdXW98MitJU4UmJRiku2Q7LEkBmtexG6dEa4Dk2JHyWSRTtiviPChQw1Vdu9E7QwUaptSWmMCzPGDJBLYkmkuWmC13hq8gSs2RFyj5kzxS2GdaekUhD4GUHXVoC8u5ayWMQWWT4fVuKFScZiAUzRGq4izQH2EbSoQo27ZrmHG9Tqxa1mPhJRBwHCSkLBi",
            "sask4DR42rXymf65Nv9ytbJvhMNEZ9SQmLQiEoZDek7djTW7Tvx9169HvBGieaGGwcqr78EEzd2WQ6KKFTndUrmizR4aw6sxq9PC5sW7jJGfG3gZQs6zV8ZySxuwmyCFSyyn66TUKr2LWewzSC446vojPxSbmT88XWRTXD8CKYSzhfTWE6yEuSWxnzGB6yvjH7AeffMXZby9QJ4H4oADeG568DN4tja3pqQSJJS9FhW6Ls4bWk3Lw",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "5257511f76c25187909feacf5ef4c57b2d7066aec8e7dd471ae4150db613ed65358d015741357c6153a341b23e2b1023d79ab951ff3153aa7a5ec720c910517fe802ff485605404f177598fa0b0125a4dcee7e6517d47d35baf9dcdc80aad9955ae8aa3dde5a0ba0a7c06d7dfcef7a199a252afd838d9af6347e945bf5203d79982644418d5165a9a3b260176c34b6fd3142238c0c775d523aeeb54c13fbd958b67905ecd109c5b9ef",
            "0bed145c5257511f76c25187909feacf5ef4c57b2d7066aec8e7dd471ae4150db613ed65358d015741357c6153a341b23e2b1023d79ab951ff3153aa7a5ec720c910517fe802ff485605404f177598fa0b0125a4dcee7e6517d47d35baf9dcdc80aad9955ae8aa3dde5a0ba0a7c06d7dfcef7a199a252afd838d9af6347e945bf5203d79982644418d5165a9a3b260176c34b6fd3142238c0c775d523aeeb54c13fbd958b67905ecd109c5b9ef",
            "274fac79a1d611de73e322e0c92d1bef0d05cecf7eaae4b7709a1f603bdb05981caf4a98e849eb0091bef10ca16f9666ba3f79b589ae227f4b4a1efa98ba08feaa28e35beedc6350b86fe8c5f8ce4f1e47a88c35565a8046382a3fb1d67a07c93306ec77b939cac7db4c3a08c660ed060412a53f94a535b7e1fbfad8e2b4540d08d696c0a78f625b3900437171b5cf20777ce18e5141aea21e5e10b5a36d9cf38bb39d450ef150ad3d",
            "0bed145c274fac79a1d611de73e322e0c92d1bef0d05cecf7eaae4b7709a1f603bdb05981caf4a98e849eb0091bef10ca16f9666ba3f79b589ae227f4b4a1efa98ba08feaa28e35beedc6350b86fe8c5f8ce4f1e47a88c35565a8046382a3fb1d67a07c93306ec77b939cac7db4c3a08c660ed060412a53f94a535b7e1fbfad8e2b4540d08d696c0a78f625b3900437171b5cf20777ce18e5141aea21e5e10b5a36d9cf38bb39d450ef150ad3d",
            "bc2f61894f30aa6a396cfbc88c220864b4411f27e8dc2c6f7a3dcd6b069c2df4fab6d824e8609c072e982f88a97ea615bed4fdce66c703f241e4b0eefd0a2f3e2204ab5e53199f4bd25db64cb8afa01bbfed80d2737e131e1f3b1fb19100c5ab5afd77778103ad55fd45bc5b00ad2f974850edd4d48d795ddb76f5f8a72cf0b2011c757cf19f60b212c1a35919f1d55ddc89df73e64e35981848df9a751c05b48164bb1df0eea4485e",
            "0bed145cbc2f61894f30aa6a396cfbc88c220864b4411f27e8dc2c6f7a3dcd6b069c2df4fab6d824e8609c072e982f88a97ea615bed4fdce66c703f241e4b0eefd0a2f3e2204ab5e53199f4bd25db64cb8afa01bbfed80d2737e131e1f3b1fb19100c5ab5afd77778103ad55fd45bc5b00ad2f974850edd4d48d795ddb76f5f8a72cf0b2011c757cf19f60b212c1a35919f1d55ddc89df73e64e35981848df9a751c05b48164bb1df0eea4485e",
            "6e5313b9fce5718557370b4c4d9d6e02c4a7db207c0408c970cb83d282d999acf95d4687acc453570ea82b8528ef6230b5aa0554c21be5b8fcb00ceba7e47d0a8b7bb3195a8102e482d3276cd874fe1c0d42a375a8c9756f5a911320978da8452315dbe134f948b0ff7c923bc9672fd2a73427ad33227529b17523cc929a7e50da96d52cf995ac441afdf7d24ded51fa384c277749a7f2ced9844e75b5c07c463f2a812b01862f4cd9",
            "0bed145c6e5313b9fce5718557370b4c4d9d6e02c4a7db207c0408c970cb83d282d999acf95d4687acc453570ea82b8528ef6230b5aa0554c21be5b8fcb00ceba7e47d0a8b7bb3195a8102e482d3276cd874fe1c0d42a375a8c9756f5a911320978da8452315dbe134f948b0ff7c923bc9672fd2a73427ad33227529b17523cc929a7e50da96d52cf995ac441afdf7d24ded51fa384c277749a7f2ced9844e75b5c07c463f2a812b01862f4cd9",
            "2a6ef45f43df2c2151f7ecd2f12bb3183e1b381b4727baa9fab120adcbe713d84ff3c5080fd361f58415e2a0b7069aa1acbd2fe81feccdc12f623ca480213375a2841d8180585c8261dff6e898da18f5dd6cc73be71f2659935c4ee0a2dc1d5a988bb99fb7a72355f288c629e3977b8193d64096d8a5e669e2282f1a692fc630e90ae77e7e05d6d78575e4c256d5e2fad844ce0f2a76930675d8032a8b98fe839029eca886027fd4e6",
            "0bed145c2a6ef45f43df2c2151f7ecd2f12bb3183e1b381b4727baa9fab120adcbe713d84ff3c5080fd361f58415e2a0b7069aa1acbd2fe81feccdc12f623ca480213375a2841d8180585c8261dff6e898da18f5dd6cc73be71f2659935c4ee0a2dc1d5a988bb99fb7a72355f288c629e3977b8193d64096d8a5e669e2282f1a692fc630e90ae77e7e05d6d78575e4c256d5e2fad844ce0f2a76930675d8032a8b98fe839029eca886027fd4e6",
            "b706020e001f73c4229aec12166e04c9268d34fa82cc0b8a771f88949ebada1c01e61b79b12ab2baa5576862447d6cc769554d7b8d037cf5e72c91ae8e111154f143676e344b70efb8c7c2a0f148cfeef6bc06b01438d13b3703ae621310783dbfadeac5ab7efc20c25a86b7ddd5b332b5b48464b33c63e25018273b480af8f5d47020c3485b8b7bef374797fec6e6ab8b90e99813713e60c87e6d7081d65df8382ad7b299f502fb13",
            "0bed145cb706020e001f73c4229aec12166e04c9268d34fa82cc0b8a771f88949ebada1c01e61b79b12ab2baa5576862447d6cc769554d7b8d037cf5e72c91ae8e111154f143676e344b70efb8c7c2a0f148cfeef6bc06b01438d13b3703ae621310783dbfadeac5ab7efc20c25a86b7ddd5b332b5b48464b33c63e25018273b480af8f5d47020c3485b8b7bef374797fec6e6ab8b90e99813713e60c87e6d7081d65df8382ad7b299f502fb13",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "5257511f76c25187909feacf5ef4c57b2d7066aec8e7dd471ae4150db613ed65358d015741357c6153a341b23e2b1023d79ab951ff3153aa7a5ec720c910517fe802ff485605404f177598fa0b0125a4dcee7e6517d47d35baf9dcdc80aad9955ae8aa3dde5a0ba0a7c06d7dfcef7a199a252afd838d9af6347e945bf5203d79982644418d5165a9a3b260176c34b6fd3142238c0c775d523aeeb54c13fbd958b67905ec",
            "274fac79a1d611de73e322e0c92d1bef0d05cecf7eaae4b7709a1f603bdb05981caf4a98e849eb0091bef10ca16f9666ba3f79b589ae227f4b4a1efa98ba08feaa28e35beedc6350b86fe8c5f8ce4f1e47a88c35565a8046382a3fb1d67a07c93306ec77b939cac7db4c3a08c660ed060412a53f94a535b7e1fbfad8e2b4540d08d696c0a78f625b3900437171b5cf20777ce18e5141aea21e5e10b5a36d9cf38bb39d450ef150ad3d4fac",
            "1bed145cb706020e001f73c4229aec12166e04c9268d34fa82cc0b8a771f88949ebada1c01e61b79b12ab2baa5576862447d6cc769554d7b8d037cf5e72c91ae8e111154f143676e344b70efb8c7c2a0f148cfeef6bc06b01438d13b3703ae621310783dbfadeac5ab7efc20c25a86b7ddd5b332b5b48464b33c63e25018273b480af8f5d47020c3485b8b7bef374797fec6e6ab8b90e99813713e60c87e6d7081d65df8382ad7b299f502fb13",
        ).map { it.asHexString().toByteArray() }
}