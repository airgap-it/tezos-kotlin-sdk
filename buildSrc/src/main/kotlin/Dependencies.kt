object Dependencies {
    private object Version {
        const val kotlinxSerialization = "1.3.2"
        const val kotlinxCoroutines = "1.6.1"

        const val bouncyCastle = "1.70"

        // Test
        const val junit = "5.+"
        const val mockk = "1.12.2"
    }

    const val kotlinxSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlinxSerialization}"
    const val kotlinxCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.kotlinxCoroutines}"

    const val bouncyCastle = "org.bouncycastle:bcprov-jdk15on:${Version.bouncyCastle}"

    // Test
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Build.Kotlin.version}"

    const val junit = "org.junit.jupiter:junit-jupiter:${Version.junit}"
    const val mockk = "io.mockk:mockk:${Version.mockk}"
}