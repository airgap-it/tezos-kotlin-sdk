dependencies {
    implementation(project(":tezos-core"))

    // Kotlin
    implementation(kotlin("stdlib"))

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)
}