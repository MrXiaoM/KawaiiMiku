plugins {
    val kotlinVersion = "1.7.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.15.0-RC"
}
allprojects {
    group = "top.mrxiaom.mirai"
    version = "0.1.0"

    apply(plugin = "java")
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    repositories {
        mavenLocal()
        maven("https://repo.huaweicloud.com/repository/maven/")
        mavenCentral()
    }
}
dependencies {
    compileOnly("net.mamoe:mirai-core:2.15.0-RC")
    compileOnly("net.mamoe:mirai-core-utils:2.15.0-RC")

    compileOnly("org.apache.httpcomponents.client5:httpclient5:5.2.1")
}