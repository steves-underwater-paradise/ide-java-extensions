rootProject.name = "jdtls.ext"

pluginManagement {
	repositories {
        mavenCentral()
        gradlePluginPortal()
	}

	val kotlin_jvm_plugin_version: String by settings
	val auto_include_plugin_version: String by settings
	plugins {
		kotlin("jvm").version(kotlin_jvm_plugin_version)
    	id("com.pablisco.gradle.auto.include").version(auto_include_plugin_version)
        id("maven-publish")
	}
}

// Gradle modules are automatically included using com.pablisco.gradle.auto.include
// See https://github.com/pablisco/auto-include/
