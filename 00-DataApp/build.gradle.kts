plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.compose") version "1.6.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
    implementation("com.github.librepdf:openpdf:1.3.30")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}