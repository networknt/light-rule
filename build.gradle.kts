import org.jetbrains.kotlin.org.fusesource.jansi.AnsiRenderer.test

plugins {
    application
    kotlin("jvm") version "1.3.10"
    maven
}

application {
    mainClassName = "rule.CliKt"
}

dependencies {
    compile(kotlin("stdlib"))
    testCompile("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

allprojects {
    group = "com.networknt"
    version = "1.0.0"

    repositories {
        mavenLocal() // mavenLocal must be added first.
        jcenter()
    }
}
