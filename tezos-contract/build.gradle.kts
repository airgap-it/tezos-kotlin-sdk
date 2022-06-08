dependencies {
    implementation(project(":tezos-core"))
    implementation(project(":tezos-michelson"))
    implementation(project(":tezos-operation"))
    implementation(project(":tezos-rpc"))

    // Kotlin

    implementation(kotlin("stdlib"))
    implementation(Dependencies.kotlinxCoroutines)

    // Test
    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)

    testImplementation(Dependencies.kotlinxCoroutines)
}