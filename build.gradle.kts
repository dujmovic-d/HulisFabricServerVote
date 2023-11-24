@file:Suppress("UnstableApiUsage")

plugins {
    id("java")
    id("dev.architectury.loom") version("1.3-SNAPSHOT")
    id("architectury-plugin") version("3.4-SNAPSHOT")
    kotlin("jvm") version ("2.0.0-Beta1")
}

group = "dev.huli"
version = "1.0"

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    silentMojangMappingsLicense()

    mixin {
        defaultRefmapName.set("mixins.${project.name}.refmap.json")
    }
}
repositories {
    mavenCentral()
    maven("https://maven.impactdev.net/repository/development/")
    maven("https://maven.nucleoid.xyz")
}
dependencies {
    minecraft("com.mojang:minecraft:1.20.1")
    mappings("net.fabricmc:yarn:1.20.1+build.10")
    modImplementation("net.fabricmc:fabric-loader:0.14.22")

    modImplementation("net.fabricmc.fabric-api:fabric-api:0.88.1+1.20.1")
    modImplementation("com.cobblemon:fabric:1.4.0+1.20.1")


    //shadowCommon group: 'commons-io', name: 'commons-io', version: '2.6'
    compileOnly("net.luckperms:api:${rootProject.property("luckperms_version")}")
}