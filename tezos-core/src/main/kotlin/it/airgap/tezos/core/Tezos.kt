package it.airgap.tezos.core

public object Tezos {
    
    public enum class Prefix(
        public val value: String,
        public val base58Bytes: ByteArray,
        public val encodedLength: Int,
    ) {

        /* 32 */
        BlockHash("B", byteArrayOf(1, 52), encodedLength = 32), /* B(51) */
        OperationHash("o", byteArrayOf(5, 116), encodedLength = 32), /* o(51) */
        OperationListHash("Lo", byteArrayOf((133).toByte(), (233).toByte()), encodedLength = 32), /* Lo(52) */
        OperationListListHash("LLo", byteArrayOf(29, (159).toByte(), 109), encodedLength = 32), /* LLo(53) */
        ProtocolHash("P", byteArrayOf(2, (170).toByte()), encodedLength = 32), /* P(51) */
        ContextHash("Co", byteArrayOf(79, (199).toByte()), encodedLength = 32), /* Co(52) */
        BlockMetadataHash("bm", byteArrayOf((234).toByte(), (249).toByte()), encodedLength = 32), /* bm(52) */
        OperationMetadataHash("r", byteArrayOf(5, (183).toByte()), encodedLength = 32), /* r(51) */
        OperationMetadataListHash("Lr", byteArrayOf((134).toByte(), 39), encodedLength = 32), /* Lr(52) */
        OperationMetadataListListHash("LLr", byteArrayOf(29, (159).toByte(), (182).toByte()), encodedLength = 32), /* LLr(53) */

        /* 20 */
        Ed25519PublicKeyHash("tz1", byteArrayOf(6, (161).toByte(), (159).toByte()), encodedLength = 20), /* tz1(36) */
        Secp256K1PublicKeyHash("tz2", byteArrayOf(6, (161).toByte(), (161).toByte()), encodedLength = 20), /* tz2(36) */
        P256PublicKeyHash("tz3", byteArrayOf(6, (161).toByte(), (164).toByte()), encodedLength = 20), /* tz3(36) */
        ContractHash("KT1", byteArrayOf(2, 90, 121), encodedLength = 20), /* KT1(36) */

        /* 16 */
        CryptoboxPublicKeyHash("id", byteArrayOf((153).toByte(), (103).toByte()), encodedLength = 16), /* id(30) */

        /* 32 */
        Ed25519Seed("edsk", byteArrayOf(13, 15, 58, 7), encodedLength = 32), /* edsk(54) */
        Ed25519PublicKey("edpk", byteArrayOf(13, 15, 37, (217).toByte()), encodedLength = 32), /* edpk(54) */
        Secp256K1SecretKey("spsk", byteArrayOf(17, (162).toByte(), (224).toByte(), (201).toByte()), encodedLength = 32), /* spsk(54) */
        P256SecretKey("p2sk", byteArrayOf(16, 81, (238).toByte(), (189).toByte()), encodedLength = 32), /* p2sk(54) */

        /* 56 */
        Ed25519EncryptedSeed("edesk", byteArrayOf(7, 90, 60, (179).toByte(), 41), encodedLength = 56), /* edesk(88) */
        Secp256K1EncryptedSecretKey("spesk", byteArrayOf(9, (237).toByte(), (241).toByte(), (174).toByte(), (150).toByte()), encodedLength = 56), /* spesk(88) */
        P256EncryptedSecretKey("p2esk", byteArrayOf(9, 48, 57, 115, (171).toByte()), encodedLength = 56), /* p2esk(88) */

        /* 60 */
        Secp256K1EncryptedScalar("seesk", byteArrayOf(1, (131).toByte(), 36, 86, (248).toByte()), encodedLength = 60), /* seesk(93) */

        /* 33 */
        Secp256K1PublicKey("sppk", byteArrayOf(3, (254).toByte(), (226).toByte(), 86), encodedLength = 33), /* sppk(55) */
        P256PublicKey("p2pk", byteArrayOf(3, (178).toByte(), (139).toByte(), 127), encodedLength = 33), /* p2pk(55) */
        Secp256K1Scalar("SSp", byteArrayOf(38, (248).toByte(), (136).toByte()), encodedLength = 33), /* SSp(53) */
        Secp256K1Element("GSp", byteArrayOf(5, 92, 0), encodedLength = 33), /* GSp(54) */

        /* 64 */
        Ed25519SecretKey("edsk", byteArrayOf(43, (246).toByte(), 78, 7), encodedLength = 64), /* edsk(98) */
        Ed25519Signature("edsig", byteArrayOf(9, (245).toByte(), (205).toByte(), (134).toByte(), 18), encodedLength = 64), /* edsig(99) */
        Secp256K1Signature("spsig1", byteArrayOf(13, 115.toByte(), 101, 19, 63), encodedLength = 64), /* spsig1(99) */
        P256Signature("p2sig", byteArrayOf(54, (240).toByte(), 44, 52), encodedLength = 64), /* p2sig(98) */
        GenericSignature("sig", byteArrayOf(4, (130).toByte(), 43), encodedLength = 64), /* sig(96) */

        /* 4 */
        ChainId("Net", byteArrayOf(87, 82, 0), encodedLength = 4), /* Net(15) */

        /* 169 */
        SaplingSpendingKey("sask", byteArrayOf(11, (237).toByte(), 20, 92), encodedLength = 169), /* sask(241) */

        /* 43 */
        SaplingAddress("zet1", byteArrayOf(18, 71, 40, (223).toByte()), encodedLength = 43); /* zet1(69) */

        public operator fun plus(string: String): String = value + string
        public operator fun plus(bytes: ByteArray): ByteArray = base58Bytes + bytes

        public companion object {
            public fun recognize(string: String): Prefix? = values().find { string.startsWith(it.value) }
            public fun recognize(bytes: ByteArray): Prefix? = values().find { bytes.sliceArray(0 until it.base58Bytes.size).contentEquals(it.base58Bytes) }
        }
    }
}