
plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.bukkitgradle)
}

dependencies {
    implementation(libs.coroutines)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)
    implementation(libs.litecommands)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.dao)
    implementation(libs.configurate)
    implementation(libs.sqlite)
}

tasks {
    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }

    bukkit {
        apiVersion = "1.21.1"

        meta {
            name.set(rootProject.name)
            version.set(rootProject.version.toString())
            main.set("me.fortibrine.markers.MarkersPlugin")
        }

        server {
            setCore("paper")
            eula = true
            onlineMode = true
            encoding = "UTF-8"
            javaArgs("-Xmx1G")
            bukkitArgs("nogui")
        }
    }
}