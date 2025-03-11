import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
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

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    formatKotlin {
    }

    lintKotlin {
    }

    withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xcontext-receivers",
            )
            jvmTarget = JVM_21
        }
    }

    test {
        useJUnitPlatform()
        testLogging {
            events(PASSED, FAILED, SKIPPED)
        }
    }
}
