import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.serialization") version Build.Kotlin.version
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

dependencies {
    implementation(project(":tezos-core"))

    // Kotlin

    implementation(kotlin("stdlib"))
    implementation(Dependencies.kotlinxSerialization)

    // Test

    testImplementation(Dependencies.kotlinTest)
    testImplementation(Dependencies.junit)
}