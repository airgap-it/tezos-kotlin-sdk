object Dependencies {
    private object Version {
        // Test
        const val junit = "5.+"
    }

    // Test
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Build.Kotlin.version}"

    const val junit = "org.junit.jupiter:junit-jupiter:${Version.junit}"
}