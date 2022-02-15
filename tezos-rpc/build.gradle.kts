dependencies {
    implementation(project(":tezos-core"))

    // Kotlin

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(Dependencies.kotlinxSerialization)

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)
}