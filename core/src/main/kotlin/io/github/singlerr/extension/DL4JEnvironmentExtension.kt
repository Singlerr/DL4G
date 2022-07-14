package io.github.singlerr.extension

import io.github.singlerr.containers.DL4JEnvironmentContainer
import io.github.singlerr.environments.ND4JEnvironment


abstract class DL4JEnvironmentExtension {

    var configuration: String = "implementation"

    val dl4jEnvironments: DL4JEnvironmentContainer = DL4JEnvironmentContainer()

    val nd4jEnvironment: ND4JEnvironment = ND4JEnvironment()

    fun dl4jEnvironments(action: DL4JEnvironmentContainer.() -> Unit) {
        action(dl4jEnvironments)
    }

    fun nd4jEnvironment(action: ND4JEnvironment.() -> Unit) {
        action(nd4jEnvironment)
    }

    companion object {
        const val name = "dl4j"
    }
}

