plugins {
    val kotlinVersion = "1.7.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    `maven-publish`
    id("net.mamoe.mirai-console") version "2.15.0-core-pkgsso-19"
    id("com.github.gmazzo.buildconfig") version "3.1.0"
}

group = "top.mrxiaom.mirai"
version = "0.1.4"

buildConfig {
    className("BuildConstants")
    packageName("top.mrxiaom.mirai.kawaii")
    useKotlinOutput()

    buildConfigField("String", "VERSION", "\"${project.version}\"")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenLocal()
    maven("https://repo.huaweicloud.com/repository/maven/")
    mavenCentral()
    maven("https://repo.mirai.mamoe.net/snapshots/")
}

tasks {
    processResources {
        from("LICENSE")
    }
}

dependencies {
    implementation(platform("net.mamoe:mirai-bom:2.15.0-core-pkgsso-19"))
    compileOnly("net.mamoe:mirai-core")
    compileOnly("net.mamoe:mirai-core-utils")

    compileOnly("org.apache.httpcomponents:httpclient:4.5.14")
}

publishing {
    publications {
        register("JitPack", MavenPublication::class) {
            groupId = "com.github.MrXiaoM"
            artifactId = "KawaiiMiku"
            version = project.version.toString()

            from(components.named("kotlin").get())
        }
    }

    repositories {
        mavenLocal()
    }
}