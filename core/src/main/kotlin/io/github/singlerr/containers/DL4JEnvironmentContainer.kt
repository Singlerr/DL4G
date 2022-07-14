package io.github.singlerr.containers

import io.github.singlerr.environments.DL4JEnvironment

class DL4JEnvironmentContainer {
    val dl4jEnvironments = ArrayList<DL4JEnvironment>()
    var version: String? = null
    var artifactNameShape = "deeplearning4j-{moduleName}"
    fun dl4jModule(action: DL4JEnvironment.() -> Unit) {
        val env = DL4JEnvironment()
        action(env)
        dl4jEnvironments.add(env)
    }

}