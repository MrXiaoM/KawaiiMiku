rootProject.name = "KawaiiMiku"

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven("https://repo.mirai.mamoe.net/snapshots/")
    }
}

include("tlv544_enc")
