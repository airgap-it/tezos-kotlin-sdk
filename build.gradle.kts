import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Build.Kotlin.version
    `maven-publish`
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")

    group = Project.group
    version = Project.version

    java {
        sourceCompatibility = Build.Java.compatibility
        targetCompatibility = Build.Java.compatibility
    }

    kotlin {
        explicitApiWarning()
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = Build.Kotlin.jvmTarget
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven")
        }
    }
}

// Jars
tasks.register<Copy>("copyJars") {
    subprojects.forEach {
        from("${it.buildDir}/libs/${it.name}-${it.version}.jar")
        into("$buildDir/libs/subprojects")
    }
}

tasks.register<Zip>("zipJars") {
    dependsOn("copyJars")

    archiveFileName.set("jars.zip")
    destinationDirectory.set(layout.projectDirectory)

    from("$buildDir/libs/subprojects")
}

// Tests
tasks.register<Copy>("copyTestReports") {
    subprojects.forEach {
        from("${it.buildDir}/reports/tests")
        rename { filename -> "${it.name}-$filename" }
        into("$buildDir/reports/tests/subprojects")
    }
}

tasks.register<Zip>("zipTestReports") {
    dependsOn("copyTestReports")

    archiveFileName.set("test-reports.zip")
    destinationDirectory.set(layout.projectDirectory)

    from("$buildDir/reports/tests/subprojects")
}