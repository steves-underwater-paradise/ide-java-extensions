plugins {
    kotlin("jvm")
    id("maven-publish")
}

group = "io.github.enbrain.jdtls.spongepowered.mixin"
version = "0.1.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

repositories {
    maven(url = "https://download.eclipse.org/releases/2025-03/202501101000/")
    maven(url = "https://download.eclipse.org/jdtls/snapshots/repository/latest/")
    maven(url = "https://repo.eclipse.org/content/groups/releases/")
    maven(url = "https://repo.maven.apache.org/maven2/")
}

dependencies {
    val ow2_asm_version: String by project
    api("org-ow2-asm-asm:asm:${ow2_asm_version}")
    val jdt_core_version: String by project
    api("org.eclipse.jdt:org.eclipse.jdt.core:${jdt_core_version}")
    val jdt_ls_core_version: String by project
    api("org.eclipse.jdt.ls:org.eclipse.jdt.ls.core:${jdt_ls_core_version}")
}
