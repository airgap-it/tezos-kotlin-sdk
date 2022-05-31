dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":tezos-core"))
    implementation(project(":tezos-michelson"))
    implementation(project(":tezos-operation"))
    implementation(project(":tezos-rpc"))
    implementation(project(":tezos-contract"))

    implementation(project(":tezos-crypto:bouncycastle"))
    implementation(project(":tezos-http:ktor"))

    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junit)
}