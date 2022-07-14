package io.github.singlerr.utils

fun createDL4JArtifactName(shape: String, moduleName: String): String = shape.replace("{moduleName}", moduleName)
fun createDependencyNotation(group: String, artifactName: String, version: String): String =
    group.plus(":").plus(artifactName).plus(":").plus(version)

fun createND4JArtifactName(shape: String, cudaVersion: String): String = shape.replace("{cudaVersion}", cudaVersion)