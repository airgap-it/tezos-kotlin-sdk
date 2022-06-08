plugins {
    kotlin("plugin.serialization") version Build.Kotlin.version
}

dependencies {
    implementation(project(":tezos-core"))
    implementation(project(":tezos-michelson"))
    implementation(project(":tezos-operation"))

    // Kotlin

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation(Dependencies.kotlinxSerialization)

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)

    testImplementation(Dependencies.kotlinxCoroutines)
}