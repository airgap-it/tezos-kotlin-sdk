object Dependencies {
    private object Version {
        const val kotlinxSerialization = "1.3.1"

        const val tuweni = "2.0.0"

        // Test
        const val junit = "5.+"
        const val mockk = "1.12.2"
    }

    const val kotlinxSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlinxSerialization}"

    const val tuweni = "org.apache.tuweni:tuweni-crypto:${Version.tuweni}"

    // Test
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Build.Kotlin.version}"

    const val junit = "org.junit.jupiter:junit-jupiter:${Version.junit}"
    const val mockk = "io.mockk:mockk:${Version.mockk}"
}