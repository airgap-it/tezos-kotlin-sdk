object Dependencies {
    private object Version {
        const val kotlinxSerialization = "1.3.3"
        const val kotlinxCoroutines = "1.6.1"

        const val ktor = "2.0.2"

        const val bouncyCastle = "1.70"

        // Test
        const val junit = "5.+"
        const val mockk = "1.12.2"
    }

    const val kotlinxSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlinxSerialization}"
    const val kotlinxCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.kotlinxCoroutines}"

    const val ktorCore = "io.ktor:ktor-client-core:${Version.ktor}"
    const val ktorCIO = "io.ktor:ktor-client-cio:${Version.ktor}"
    const val ktorClientContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Version.ktor}"
    const val ktorSerializationKotlinxJson = "io.ktor:ktor-serialization-kotlinx-json:${Version.ktor}"
    const val ktorLoggingJvm = "io.ktor:ktor-client-logging-jvm:${Version.ktor}"

    const val bouncyCastle = "org.bouncycastle:bcprov-jdk15on:${Version.bouncyCastle}"

    // Test
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Build.Kotlin.version}"

    const val junit = "org.junit.jupiter:junit-jupiter:${Version.junit}"
    const val mockk = "io.mockk:mockk:${Version.mockk}"
}