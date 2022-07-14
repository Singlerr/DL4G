package io.github.singlerr

import org.gradle.api.provider.Property

interface Environment {
    val name: Property<String>
}