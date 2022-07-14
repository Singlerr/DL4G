package io.github.singlerr

import io.github.singlerr.extension.DL4JEnvironmentExtension
import io.github.singlerr.types.BackendType
import io.github.singlerr.utils.createDL4JArtifactName
import io.github.singlerr.utils.createDependencyNotation
import io.github.singlerr.utils.createND4JArtifactName
import org.gradle.api.Plugin
import org.gradle.api.Project

const val separator = "-"
const val dl4jGroup = "org.deeplearning4j"
const val nd4jGroup = "org.nd4j"

class DL4GPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val dl4jEnvironment = target.extensions.create("dl4j", DL4JEnvironmentExtension::class.java)

        val config = dl4jEnvironment.configuration
        val dl4jArtifactNameShape = dl4jEnvironment.dl4jEnvironments.artifactNameShape
        val nd4jArtifactNameShape = dl4jEnvironment.nd4jEnvironment.artifactNameShape

        val dl4jDependencies = dl4jEnvironment.dl4jEnvironments.dl4jEnvironments
        val nd4jDependency = dl4jEnvironment.nd4jEnvironment

        if(dl4jDependencies.isEmpty())
            return

        dl4jDependencies.forEach {
            target.dependencies.add(
                config, createDependencyNotation(
                    dl4jGroup,
                    createDL4JArtifactName(dl4jArtifactNameShape, it.module!!.moduleName),
                    it.version ?: dl4jEnvironment.dl4jEnvironments.version!!
                )
            )
        }

        if (nd4jDependency.backend == BackendType.CPU) {
            target.dependencies.add(
                config,
                createDependencyNotation(nd4jGroup, "nd4j-native-platform", nd4jDependency.version!!)
            )
        } else {
            target.dependencies.add(
                config,
                createDependencyNotation(
                    nd4jGroup,
                    createND4JArtifactName(nd4jArtifactNameShape, nd4jDependency.cudaVersion!!),
                    nd4jDependency.version!!
                )
            )
            target.dependencies.add(
                config,
                createDependencyNotation(
                    dl4jGroup,
                    createND4JArtifactName("deeplearning4j-cuda-{cudaVersion}", nd4jDependency.cudaVersion!!),
                    nd4jDependency.version!!
                )
            )
        }
    }

}