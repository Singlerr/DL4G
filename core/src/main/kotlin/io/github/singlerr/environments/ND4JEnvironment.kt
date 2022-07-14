package io.github.singlerr.environments

import io.github.singlerr.types.BackendType

class ND4JEnvironment {
    var backend: BackendType? = null
    var version: String? = null
    var artifactNameShape: String = "nd4j-cuda-{cudaVersion}"
    var cudaVersion: String? = null
}