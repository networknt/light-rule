import org.jetbrains.kotlin.org.fusesource.jansi.AnsiRenderer.test

plugins {
    application
    kotlin("jvm") version "1.3.10"
}

application {
    mainClassName = "rule.CliKt"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    compile(kotlin("stdlib"))
    testCompile("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

repositories {
    jcenter()
}
