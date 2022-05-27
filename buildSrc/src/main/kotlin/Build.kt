import org.gradle.api.JavaVersion

object Build {
    object Kotlin {
        const val version = "1.6.21"
        const val jvmTarget = "1.8"
    }

    object Java {
        val compatibility = JavaVersion.VERSION_1_8
    }
}