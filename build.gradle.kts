import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinter)
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    api(platform(libs.boost.bom))
    api(libs.bundles.kotlin.coroutines)
    api(libs.bundles.logging)
    api(libs.bundles.serialization)
    api(libs.bundles.other) {
        exclude(group = "com.squareup.okhttp3", module = "okhttp")
    }
    testImplementation(libs.bundles.testing)
}

java.sourceCompatibility = JavaVersion.VERSION_21

tasks {
    formatKotlin {
    }

    lintKotlin {
    }

    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(java.sourceCompatibility.majorVersion)
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xcontext-parameters",
            )
        }
    }

    test {
        useJUnitPlatform()
        testLogging {
            events(PASSED, FAILED, SKIPPED)
        }
    }
}
