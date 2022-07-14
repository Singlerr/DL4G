
import io.github.singlerr.types.BackendType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    kotlin("jvm") version "1.7.10"
    id("io.github.singlerr.dl4g")
}



repositories {
    mavenCentral()
}



dependencies {

}

dl4j{

    nd4jEnvironment{
        backend = BackendType.CPU
        cudaVersion = "11.4"
        version = "1.0.0-M1.1"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {

    kotlinOptions.jvmTarget = "1.8"
}