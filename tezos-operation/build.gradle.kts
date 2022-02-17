dependencies {
    implementation(project(":tezos-core"))
    implementation(project(":tezos-michelson"))

    // Kotlin
    implementation(kotlin("stdlib"))

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)

    testImplementation(Dependencies.bouncyCastle)
}