
val kotlin_version: String by project
val logback_version: String by project
val postgres_version: String by project
val h2_version: String by project
val exposed_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
    //id("com.google.gms.google-services") version "4.4.2" apply false
}

group = "com.example"
version = "0.0.1"


application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("net.sourceforge.tess4j:tess4j:5.7.0")
    implementation("com.google.cloud:google-cloud-document-ai:2.62.0") // Use the latest version
    implementation("io.grpc:grpc-netty-shaded:1.70.0") // Make sure to use compatible versions
    implementation("io.grpc:grpc-stub:1.70.0")
    implementation("com.google.protobuf:protobuf-java:3.21.12")
    implementation("org.openpnp:opencv:4.5.1-2")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    testImplementation("io.ktor:ktor-client-content-negotiation-jvm")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.google.firebase:firebase-dataconnect:16.0.0-beta01")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    //implementation("com.google.firebase:firebase-analytics")
    //implementation("com.google.firebase:firebase-auth:23.0.0") // Optional
    implementation("com.h2database:h2:$h2_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")

    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7") // Or OkHttp, Java, etc.
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")

    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
