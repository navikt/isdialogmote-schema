object Versions {
    const val avroVersion = "1.10.2"
}

repositories {
    mavenCentral()
    maven(url = "https://dl.bintray.com/gradle/gradle-plugins")
    maven(url = "http://packages.confluent.io/maven/")
}

plugins {
    `maven-publish`
    id("com.github.davidmc24.gradle.plugin.avro") version "1.2.1"
}

dependencies {
    implementation("org.apache.avro:avro:${Versions.avroVersion}")
}

group = "no.nav.syfo.dialogmote.avro"
version = "${project.version}"

val githubUser: String by project
val githubPassword: String by project

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/isdialogmote-schema")
            credentials {
                username = githubUser
                password = githubPassword
            }
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            pom {
                name.set("isdialogmote-schema")
                description.set("isdialogmote-schema")
                url.set("https://github.com/navikt/isdialogmote-schema")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/navikt/isdialogmote-schema.git")
                    developerConnection.set("scm:git:https://github.com/navikt/isdialogmote-schema.git")
                    url.set("https://github.com/navikt/isdialogmote-schema")
                }
            }
        }
    }
}
