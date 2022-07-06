package io.mjmoore.gradle

import org.gradle.api.GradleException
import org.gradle.api.Project

class VersionNotFoundException(project: Project)
    : GradleException("Could not find version information in ${project.buildFile.absolutePath}")
