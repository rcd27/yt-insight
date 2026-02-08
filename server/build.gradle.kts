import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    kotlin("plugin.serialization") version "2.3.0"
}

group = "com.github.rcd27"
version = "1.0.0"
application {
    mainClass.set("com.github.rcd27.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation("io.ktor:ktor-server-core:3.3.3")
    implementation("io.ktor:ktor-server-content-negotiation:3.3.3")
    implementation("io.ktor:ktor-server-core:3.3.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.3")
    implementation("io.ktor:ktor-server-content-negotiation:3.3.3")
    implementation("io.ktor:ktor-server-core:3.3.3")

    implementation("com.github.pgreze:kotlin-process:1.5.1")

    /** Networking **/
    implementation("io.ktor:ktor-client-core:3.4.0")
    implementation("io.ktor:ktor-client-cio:3.4.0")
    implementation("io.ktor:ktor-client-content-negotiation:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.10.0")
    implementation("io.ktor:ktor-client-logging:3.4.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.3")

    /** Environment variables **/
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-Xcontext-parameters"))
}