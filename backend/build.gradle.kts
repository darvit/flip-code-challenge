import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val exposedVersion: String by project
val flywayVersion: String by project
val hikariCpVersion: String by project
val kediatrVersion: String by project
val kLoggingVersion: String by project
val koinVersion: String by project
val kotlinVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project
val postgreSqlVersion: String by project
val mockkVersion: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
    kotlin("plugin.serialization") version "1.9.23"
}

group = "com.flip"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-serialization")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-default-headers")
    implementation("io.ktor:ktor-server-websockets")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-request-validation")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-swagger")
    implementation("io.ktor:ktor-server-openapi")
    implementation("io.klogging:slf4j-klogging:$kLoggingVersion")
    implementation("io.klogging:klogging-jvm:$kLoggingVersion")
    implementation("io.swagger.codegen.v3:swagger-codegen-generators:1.0.36")

    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    implementation("com.trendyol:kediatr-koin-starter:$kediatrVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-money:$exposedVersion")
    implementation("org.postgresql:postgresql:$postgreSqlVersion")
    implementation("com.zaxxer:HikariCP:$hikariCpVersion")
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("org.flywaydb:flyway-database-postgresql:$flywayVersion")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}