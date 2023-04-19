/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    java
    `maven-publish`
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repository.apache.org/content/repositories/snapshots/")
    }
maven {
    url = uri("https://packages.confluent.io/maven/")
}
      
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.apache.beam:beam-sdks-java-core:2.46.0")
    implementation("org.apache.beam:beam-sdks-java-io-neo4j:2.46.0")
    implementation("org.apache.beam:beam-sdks-java-io-google-cloud-platform:2.46.0")
    implementation("org.apache.beam:beam-sdks-java-extensions-python:2.46.0")
    implementation("org.apache.beam:beam-sdks-java-io-kafka:2.46.0")
    implementation("com.google.api-client:google-api-client:2.0.0")
    implementation("com.google.apis:google-api-services-bigquery:v2-rev20220924-2.0.0")
    implementation("com.google.http-client:google-http-client:1.42.3")
    implementation("com.google.apis:google-api-services-pubsub:v1-rev20220904-2.0.0")
    implementation("joda-time:joda-time:2.10.10")
    implementation("org.apache.kafka:kafka-clients:2.4.1")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("org.hamcrest:hamcrest-core:2.1")
    implementation("org.hamcrest:hamcrest-library:2.1")
    implementation("junit:junit:4.13.1")
    runtimeOnly("org.slf4j:slf4j-jdk14:1.7.30")
    runtimeOnly("org.apache.beam:beam-runners-direct-java:2.46.0")
    runtimeOnly("org.apache.beam:beam-runners-portability-java:2.46.0")
    testImplementation("org.mockito:mockito-core:3.7.7")
}

group = "org.example"
version = "0.1"
description = "word-count-beam"
java.sourceCompatibility = JavaVersion.VERSION_1_8

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

if (project.hasProperty("dataflow-runner")) {
    dependencies {
        runtimeOnly("org.apache.beam:beam-runners-google-cloud-dataflow-java:2.46.0")
    }
}
      
task("execute", JavaExec::class) {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set(System.getProperty("mainClass"))
}
      