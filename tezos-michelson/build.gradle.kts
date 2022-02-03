plugins {
    kotlin("plugin.serialization") version Build.Kotlin.version
}

dependencies {
    implementation(project(":tezos-core"))

    // Kotlin
    implementation(kotlin("stdlib"))
    implementation(Dependencies.kotlinxSerialization)

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)
}