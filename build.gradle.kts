import org.gradle.internal.impldep.aQute.bnd.build.Run
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

tasks.withType(KotlinCompile::class).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

//vertx {
//    vertxVersion = "${vertx_version}"
//    mainVerticle = "sample.MainVerticle"
//}

kotlin {
    jvm(
    )
    js {
        browser {
        }
    }
    sourceSets {
        val commonMain by getting {
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

                // Vertx
                implementation("io.vertx:vertx-web:${vertx_version}")

                // Vertx + Kotlin
                implementation("io.vertx:vertx-lang-kotlin:$vertx_version")

                // RxJava 2 + Kotlin
                implementation("io.reactivex.rxjava2:rxkotlin:${rxkotlin_version}")

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
            }
        }
        val jsTest by getting  {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

//tasks.named<Jar>("jvmJar") {
//    dependsOn(jsBrowserWebpack)
//    from(File(jsBrowserWebpack.entry.name, jsBrowserWebpack.outputPath))
//}

//tasks.named("myRun")(type: JavaExec::class) {
//    group = "application"
//    main = "sample.SampleJvmKt"
//    classpath(configurations.jvmRuntimeClasspath, jvmJar)
//    args = []
//}

