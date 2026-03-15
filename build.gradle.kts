import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    val kotlinVersion = "2.3.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    application
    id("com.gradleup.shadow") version "9.3.2"
}

group = "com.github.mckernant1"
version = "1.0-SNAPSHOT"

application {
    mainClass = "com.github.mckernant1.marketeer.collector.CollectorRunnerKt"
}

repositories {
    mavenCentral()
    maven(uri("https://mvn.mckernant1.com/release"))
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")

    implementation("org.jsoup:jsoup:1.22.1")
    implementation("com.mckernant1.commons:metrics:0.1.6")
    implementation("com.mckernant1:kotlin-utils:0.3.21")

    implementation("org.jsoup:jsoup:1.15.3")

    implementation("org.slf4j:slf4j-simple:2.0.17")

    implementation(platform("software.amazon.awssdk:bom:2.42.13"))
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:cloudwatch")

    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.3")


}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }
    compileTestKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
