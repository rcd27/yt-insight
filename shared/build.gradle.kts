
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    kotlin("plugin.serialization") version "2.1.0"
}

kotlin {
    jvm()
    
    js {
        outputModuleName = "shared"
        browser()
        binaries.library()
        generateTypeScriptDefinitions()
        compilerOptions {
            target = "es2015"
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            implementation("io.github.oshai:kotlin-logging:7.0.3")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation("com.github.pgreze:kotlin-process:1.5.1")
        }
    }
}

