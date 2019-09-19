plugins {
    kotlin("multiplatform") version "1.3.50"
}
repositories {
    jcenter()
    maven("https://dl.bintray.com/kotlin/ktor")
    mavenCentral()
}
val ktor_version = "1.1.3"
val logback_version = "1.2.3"

kotlin {
    jvm()
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
                implementation("io.ktor:ktor-server-netty:$ktor_version")
                implementation("io.ktor:ktor-html-builder:$ktor_version")
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
            }
        }
        val jsTest by getting  {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

//val jvmJar by creating {
//    dependsOn(jsBrowserWebpack)
//    from(File(jsBrowserWebpack.entry.name, jsBrowserWebpack.outputPath))
//}
//
//task run(type: JavaExec, dependsOn(jvmJar)) {
//    group = "application"
//    main = "sample.SampleJvmKt"
//    classpath(configurations.jvmRuntimeClasspath, jvmJar)
//    args = []
//}