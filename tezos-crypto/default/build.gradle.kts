dependencies {
    implementation(project(":tezos-core"))

    // Kotlin
    implementation(kotlin("stdlib"))

    // Bouncy Castle
    implementation(Dependencies.bouncyCastle)

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junit)
}