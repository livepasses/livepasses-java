plugins {
    `java-library`
    `maven-publish`
    signing
}

group = "com.livepasses"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.0")
    testImplementation("org.wiremock:wiremock:3.9.0")
    testImplementation("org.assertj:assertj-core:3.26.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("Livepasses Java SDK")
                description.set("Official Java SDK for the Livepasses API")
                url.set("https://github.com/livepasses/livepasses-java")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("livepasses")
                        name.set("Livepasses")
                        email.set("sdk@livepasses.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/livepasses/livepasses-java.git")
                    developerConnection.set("scm:git:ssh://github.com:livepasses/livepasses-java.git")
                    url.set("https://github.com/livepasses/livepasses-java")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

signing {
    val signingKey = System.getenv("GPG_PRIVATE_KEY")
    val signingPassword = System.getenv("GPG_PASSPHRASE")
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["maven"])
    isRequired = signingKey != null
}
