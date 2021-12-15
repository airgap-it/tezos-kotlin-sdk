import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Build.Kotlin.version
    `maven-publish`
    jacoco
    id("org.barfuin.gradle.jacocolog") version "2.0.0"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")
    apply(plugin = "jacoco")

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

    tasks.jacocoTestReport {
        dependsOn(tasks.test)

        reports {
            xml.required.set(true)
            csv.required.set(true)
            html.required.set(true)
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven")
        }
    }
}

// -- jars --

tasks.register<Copy>("copyJars") {
    dependsOn(tasks.assemble)

    subprojects.forEach {
        sync {
            from("${it.buildDir}/libs/${it.name}-${it.version}.jar")
            into("$buildDir/libs/subprojects")
        }
    }
}

tasks.register<Zip>("zipJars") {
    dependsOn("copyJars")

    archiveFileName.set("jars.zip")
    destinationDirectory.set(layout.projectDirectory)

    from("$buildDir/libs/subprojects")
}

// -- tests --

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test, subprojects.map { it.tasks.jacocoTestReport })

    additionalSourceDirs.setFrom(subprojects.map { it.sourceSets.main.get().allSource.srcDirs })
    sourceDirectories.setFrom(files(subprojects.map { it.sourceSets.main.get().allSource.srcDirs }))
    classDirectories.setFrom(files(subprojects.map { it.sourceSets.main.get().output }))
    executionData.setFrom(project.fileTree(".") { include("**/build/jacoco/test.exec") } )

    reports {
        xml.required.set(true)
        csv.required.set(true)
        html.required.set(true)
    }
}

tasks.register<Copy>("copyTestReports") {
    dependsOn(tasks.test)

    subprojects.forEach {
        sync {
            from("${it.buildDir}/reports/tests")
            into("$buildDir/reports/tests/subprojects/${it.name}")
        }
        sync {
            from("${it.buildDir}/reports/jacoco/test")
            into("$buildDir/reports/tests/subprojects/${it.name}/coverage")
        }
    }
}

tasks.register<Zip>("zipTestReports") {
    dependsOn("copyTestReports")

    archiveFileName.set("test-reports.zip")
    destinationDirectory.set(layout.projectDirectory)

    from("$buildDir/reports/tests/subprojects")
    from("$buildDir/reports/jacoco/test") {
        into("coverage")
    }
}