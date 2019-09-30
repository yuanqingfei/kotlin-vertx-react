import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import java.io.File

//version = "1.0.0"

plugins {
//    id("io.vertx.vertx-plugin") version "0.8.0"
    kotlin("multiplatform") version "1.3.50"
}
repositories {
    jcenter()
    mavenCentral()
}
val rxkotlin_version = "2.4.0"
val vertx_version = "3.8.1"
val logback_version = "1.2.3"
val koin_version = "2.0.1"


//vertx {
//    vertxVersion = "${vertx_version}"
//    mainVerticle = "sample.MainVerticle"
//}

kotlin {
    jvm{
        val main by compilations.getting {
            kotlinOptions {
                // Setup the Kotlin compiler options for the 'main' compilation:
                jvmTarget = "1.8"
            }

            compileKotlinTask // get the Kotlin task 'compileKotlinJvm'
            output // get the main compilation output
        }

        compilations["test"].runtimeDependencyFiles
    }
    js {
        browser {
        }
    }
    sourceSets {
        val commonMain by getting {
            languageSettings.apply {
                languageVersion = "1.3" // possible values: '1.0', '1.1', '1.2', '1.3'
                apiVersion = "1.3" // possible values: '1.0', '1.1', '1.2', '1.3'
                //enableLanguageFeature("InlineClasses") // language feature name
                //useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes") // annotation FQ-name
                progressiveMode = true // false by default
            }
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                // Vertx + Kotlin
                implementation("io.vertx:vertx-lang-kotlin:$vertx_version")

                // Vertx + Coroutine
                implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertx_version")

                // KOIN for DI
                implementation("org.koin:koin-core:$koin_version")

                // Vertx Web
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")
                implementation("io.vertx:vertx-web:$vertx_version")

                // Vertx Config
                implementation("io.vertx:vertx-config:$vertx_version")

                // JDBC
                implementation("org.jetbrains.exposed:exposed:0.17.4")

                // H2
                implementation("com.h2database:h2:1.4.197")

                // Log
                implementation("ch.qos.logback:logback-classic:$logback_version")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation(npm("react", "^16.8.1"))
                implementation(npm("react-dom", "^16.8.1"))
                implementation(npm("kotlinx-html-js"))
                implementation(npm("material-components-web"))
            }
        }
        val jsTest by getting  {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

tasks{
    val jsBrowserWebpack by getting(KotlinWebpack::class)
    val jvmJar by getting(Jar::class){
        dependsOn(jsBrowserWebpack)
        println("parent: " + jsBrowserWebpack.entry.name)
        println("file: " + jsBrowserWebpack.destinationDirectory?.canonicalPath)
//        from(File(jsBrowserWebpack.destinationDirectory?.canonicalPath, jsBrowserWebpack.entry.name))
        from(File(jsBrowserWebpack.entry.name, jsBrowserWebpack.destinationDirectory?.canonicalPath))
    }
    val jvmRuntimeClasspath by configurations.getting
//    println(jvmRuntimeClasspat)
    val run by creating(JavaExec::class){
        dependsOn(jvmJar)
        group = "application"
        main = "sample.MainKt"
        classpath(jvmRuntimeClasspath, jvmJar)
        args = mutableListOf<String>()
    }


}



