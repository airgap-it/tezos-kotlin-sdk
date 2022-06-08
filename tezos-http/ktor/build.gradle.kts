plugins {
    kotlin("plugin.serialization") version Build.Kotlin.version
}

dependencies {
    implementation(project(":tezos-rpc"))

    // Kotlin
    implementation(kotlin("stdlib"))

    implementation(Dependencies.kotlinxSerialization)

    // Ktor
    implementation(Dependencies.ktorCore)
    implementation(Dependencies.ktorCIO)
    implementation(Dependencies.ktorClientContentNegotiation)
    implementation(Dependencies.ktorSerializationKotlinxJson)
    implementation(Dependencies.ktorLoggingJvm)

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junit)
}