plugins {
    application
    kotlin("jvm") version "1.3.10"
}

application {
    mainClassName = "rule.CliKt"
}

dependencies {
    compile(kotlin("stdlib"))
}

repositories {
    jcenter()
}
