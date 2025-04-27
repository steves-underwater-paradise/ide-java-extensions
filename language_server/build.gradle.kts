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
    maven(url = "https://repo.maven.apache.org/maven2/")

    flatDir {
        dirs = setOf(rootProject.file("libraries/"))
    }
}

dependencies {
    val ow2_asm_version: String by project
    api("org.ow2.asm:asm:${ow2_asm_version}")
    val osgi_framework_version: String by project
    api(("org.osgi:org.osgi.framework:${osgi_framework_version}"))
    val eclipse_core_runtime_version: String by project
    api("blank:org.eclipse.core.runtime_${eclipse_core_runtime_version}")
    val eclipse_equinox_common_version: String by project
    api("blank:org.eclipse.equinox.common_${eclipse_equinox_common_version}")
    val eclipse_core_resources_version: String by project
    api("blank:org.eclipse.core.resources_${eclipse_core_resources_version}")
    val eclipse_text_version: String by project
    api("blank:org.eclipse.text_${eclipse_text_version}")
    val jdt_core_version: String by project
    api("blank:org.eclipse.jdt.core_${jdt_core_version}")
    val jdt_core_compiler_version: String by project
    api("blank:org.eclipse.jdt.core.compiler.batch_${jdt_core_compiler_version}")
    val jdt_ls_core_version: String by project
    api("blank:org.eclipse.jdt.ls.core_${jdt_ls_core_version}")
}
