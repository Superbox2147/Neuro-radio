plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
}

group = "org.whatever"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("dev.kord:kord-core:0.15.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("dev.kord:kord-voice:0.15.0")
    implementation("dev.arbjerg:lavaplayer:2.2.2")
    implementation("me.xdrop:fuzzywuzzy:1.4.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}