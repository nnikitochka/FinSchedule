plugins {
    id("java")
    id("net.kyori.blossom") version "2.2.0"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "ru.nnedition.finschedule"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    // БД
    implementation(libs.sqlite.jdbc)
    implementation(libs.hikaricp)
    implementation(libs.jdbi.core)
    implementation(libs.jdbi.sqlobject)
    implementation(libs.jdbi.caffeine)
    implementation(libs.caffeine)

    // Телеграм апи
    implementation(libs.telegram.longpolling)
    implementation(libs.telegram.client)

    // Утилки
    implementation(libs.gson)
    implementation(libs.jsoup)

    // Конфиги
    implementation(libs.guava)
    implementation(libs.snakeyaml)

    // Логирование
    implementation(libs.slf4j.api)
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
        }
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "$group.FinSchedule"
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
        archiveFileName.set("FinSchedule.jar")
    }
}