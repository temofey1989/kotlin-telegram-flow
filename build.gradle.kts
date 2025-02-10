import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.10"
    id("org.jmailen.kotlinter") version "5.0.1"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

val emojiVersion: String by project
val jacksonVersion: String by project
val kotestVersion: String by project
val kotlinBoostVersion: String by project
val kotlinCoroutinesVersion: String by project
val mockkVersion: String by project
val retrofitVersion: String by project
val telegramBotVersion: String by project

dependencies {
    api(platform("io.justdevit.kotlin:boost-bom:$kotlinBoostVersion"))

    // Kotlin
    implementation(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$kotlinCoroutinesVersion")

    // Logging
    api("io.justdevit.kotlin:boost-logging-slf4j")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")

    // Serialization
    api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    // Other
    api("com.squareup.retrofit2:retrofit:$retrofitVersion") {
        exclude(group = "com.squareup.okhttp3", module = "okhttp")
    }
    api("org.kodein.emoji:emoji-kt:$emojiVersion")
    api("io.justdevit.kotlin:boost-commons")
    api("io.justdevit.kotlin:boost-eventbus")
    api("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:$telegramBotVersion")

    // Testing
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
}

java.sourceCompatibility = JavaVersion.VERSION_21

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
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
