package it.airgap.tezos.core

public object Tezos {
    
    public enum class Prefix(
        public val value: String,
        public val base58Bytes: ByteArray,
        public val dataLength: Int,
        public val base58Length: Int,
    ) {

        /* 32 */
        BlockHash("B", byteArrayOf(1, 52), dataLength = 32, base58Length = 51), /* B(51) */
        OperationHash("o", byteArrayOf(5, 116), dataLength = 32, base58Length = 51), /* o(51) */
        OperationListHash("Lo", byteArrayOf((133).toByte(), (233).toByte()), dataLength = 32, base58Length = 52), /* Lo(52) */
        OperationListListHash("LLo", byteArrayOf(29, (159).toByte(), 109), dataLength = 32, base58Length = 53), /* LLo(53) */
        ProtocolHash("P", byteArrayOf(2, (170).toByte()), dataLength = 32, base58Length = 51), /* P(51) */
        ContextHash("Co", byteArrayOf(79, (199).toByte()), dataLength = 32, base58Length = 52), /* Co(52) */
        BlockMetadataHash("bm", byteArrayOf((234).toByte(), (249).toByte()), dataLength = 32, base58Length = 52), /* bm(52) */
        OperationMetadataHash("r", byteArrayOf(5, (183).toByte()), dataLength = 32, base58Length = 51), /* r(51) */
        OperationMetadataListHash("Lr", byteArrayOf((134).toByte(), 39), dataLength = 32, base58Length = 52), /* Lr(52) */
        OperationMetadataListListHash("LLr", byteArrayOf(29, (159).toByte(), (182).toByte()), dataLength = 32, base58Length = 53), /* LLr(53) */

        /* 20 */
        Ed25519PublicKeyHash("tz1", byteArrayOf(6, (161).toByte(), (159).toByte()), dataLength = 20, base58Length = 36), /* tz1(36) */
        Secp256K1PublicKeyHash("tz2", byteArrayOf(6, (161).toByte(), (161).toByte()), dataLength = 20, base58Length = 36), /* tz2(36) */
        P256PublicKeyHash("tz3", byteArrayOf(6, (161).toByte(), (164).toByte()), dataLength = 20, base58Length = 36), /* tz3(36) */
        ContractHash("KT1", byteArrayOf(2, 90, 121), dataLength = 20, base58Length = 36), /* KT1(36) */

        /* 16 */
        CryptoboxPublicKeyHash("id", byteArrayOf((153).toByte(), (103).toByte()), dataLength = 16, base58Length = 30), /* id(30) */

        /* 32 */
        Ed25519Seed("edsk", byteArrayOf(13, 15, 58, 7), dataLength = 32, base58Length = 54), /* edsk(54) */
        Ed25519PublicKey("edpk", byteArrayOf(13, 15, 37, (217).toByte()), dataLength = 32, base58Length = 54), /* edpk(54) */
        Secp256K1SecretKey("spsk", byteArrayOf(17, (162).toByte(), (224).toByte(), (201).toByte()), dataLength = 32, base58Length = 54), /* spsk(54) */
        P256SecretKey("p2sk", byteArrayOf(16, 81, (238).toByte(), (189).toByte()), dataLength = 32, base58Length = 54), /* p2sk(54) */

        /* 56 */
        Ed25519EncryptedSeed("edesk", byteArrayOf(7, 90, 60, (179).toByte(), 41), dataLength = 56, base58Length = 88), /* edesk(88) */
        Secp256K1EncryptedSecretKey("spesk", byteArrayOf(9, (237).toByte(), (241).toByte(), (174).toByte(), (150).toByte()), dataLength = 56, base58Length = 88), /* spesk(88) */
        P256EncryptedSecretKey("p2esk", byteArrayOf(9, 48, 57, 115, (171).toByte()), dataLength = 56, base58Length = 88), /* p2esk(88) */

        /* 60 */
        Secp256K1EncryptedScalar("seesk", byteArrayOf(1, (131).toByte(), 36, 86, (248).toByte()), dataLength = 60, base58Length = 93), /* seesk(93) */

        /* 33 */
        Secp256K1PublicKey("sppk", byteArrayOf(3, (254).toByte(), (226).toByte(), 86), dataLength = 33, base58Length = 55), /* sppk(55) */
        P256PublicKey("p2pk", byteArrayOf(3, (178).toByte(), (139).toByte(), 127), dataLength = 33, base58Length = 55), /* p2pk(55) */
        Secp256K1Element("GSp", byteArrayOf(5, 92, 0), dataLength = 33, base58Length = 54), /* GSp(54) */

        /* 32 */
        Secp256K1Scalar("SSp", byteArrayOf(38, (248).toByte(), (136).toByte()), dataLength = 32, base58Length = 53), /* SSp(53) */

        /* 64 */
        Ed25519SecretKey("edsk", byteArrayOf(43, (246).toByte(), 78, 7), dataLength = 64, base58Length = 98), /* edsk(98) */
        Ed25519Signature("edsig", byteArrayOf(9, (245).toByte(), (205).toByte(), (134).toByte(), 18), dataLength = 64, base58Length = 99), /* edsig(99) */
        Secp256K1Signature("spsig1", byteArrayOf(13, 115.toByte(), 101, 19, 63), dataLength = 64, base58Length = 99), /* spsig1(99) */
        P256Signature("p2sig", byteArrayOf(54, (240).toByte(), 44, 52), dataLength = 64, base58Length = 98), /* p2sig(98) */
        GenericSignature("sig", byteArrayOf(4, (130).toByte(), 43), dataLength = 64, base58Length = 96), /* sig(96) */

        /* 4 */
        ChainId("Net", byteArrayOf(87, 82, 0), dataLength = 4, base58Length = 15), /* Net(15) */

        /* 169 */
        SaplingSpendingKey("sask", byteArrayOf(11, (237).toByte(), 20, 92), dataLength = 169, base58Length = 241), /* sask(241) */

        /* 43 */
        SaplingAddress("zet1", byteArrayOf(18, 71, 40, (223).toByte()), dataLength = 43, base58Length = 69); /* zet1(69) */

        public operator fun plus(string: String): String = value + string
        public operator fun plus(bytes: ByteArray): ByteArray = base58Bytes + bytes

        public companion object {
            public fun recognize(string: String): Prefix? =
                values().find { string.length == it.base58Length && string.startsWith(it.value) }

            public fun recognize(bytes: ByteArray): Prefix? =
                values().find { bytes.size == it.base58Bytes.size + it.dataLength && bytes.sliceArray(0 until it.base58Bytes.size).contentEquals(it.base58Bytes) }
        }
    }
}