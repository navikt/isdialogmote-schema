buildscript {
    configurations.classpath {
        resolutionStrategy {
            // Avro 1.11+ compiler generates java.util.UUID for logicalType uuid; 1.10 keeps String (consumer API)
            force("org.apache.avro:avro-compiler:1.10.2", "org.apache.avro:avro:1.10.2")
        }
    }
}

plugins {
    `java-library`
    `maven-publish`
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
}

object Versions {
    const val avroVersion = "1.11.4"
    const val junitVersion = "5.11.4"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.avro:avro:${Versions.avroVersion}")
    testImplementation(platform("org.junit:junit-bom:${Versions.junitVersion}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}

tasks.test {
    useJUnitPlatform()
}

group = "no.nav.syfo.dialogmote.avro"

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
