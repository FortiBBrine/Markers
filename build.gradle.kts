plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    idea
}

group = "me.fortibrine"
version = "1.0"

allprojects {
    repositories {
        mavenCentral()

        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.codemc.org/repository/maven-public/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.panda-lang.org/releases")
        maven("https://jitpack.io")
    }
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.serialization")
        plugin("com.google.devtools.ksp")
    }

    tasks {
        withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
            targetCompatibility = "21"
            sourceCompatibility = "21"
        }

        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = "21"
            }
        }
    }

    dependencies {
        val libs = rootProject.libs

        compileOnly(libs.spigot)
    }
}