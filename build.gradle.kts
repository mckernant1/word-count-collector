import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    val kotlinVersion = "1.4.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "com.github.mckernant1"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "com.github.mckernant1.marketeer.collector.CollectorRunnerKt"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    implementation("org.jsoup:jsoup:1.13.1")

    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("org.litote.kmongo:kmongo:4.1.2")

    implementation(platform("software.amazon.awssdk:bom:2.15.+"))
    implementation("software.amazon.awssdk:s3")

    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

tasks.withType<ShadowJar>() {
    manifest {
        attributes["Main-Class"] = "CollectorRunnerKt"
    }
}
