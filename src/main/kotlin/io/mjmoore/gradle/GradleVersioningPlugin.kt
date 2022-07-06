package io.mjmoore.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleVersioningPlugin : Plugin<Project> {

    companion object {
        private const val group = "versioning"

        const val majorRelease = "majorRelease"
        const val minorRelease = "minorRelease"
        const val patchRelease = "patchRelease"

    }

    override fun apply(project: Project) = project.afterEvaluate {
        Version.from(project.version)
            ?.let { generateActions(it) }
            ?.let { registerTasks(project, it) }
            ?: throw VersionNotFoundException(project)
    }

    private fun generateActions(version: Version): List<VersioningAction> =
        listOf(
            VersioningAction(majorRelease, version, Version::bumpMajor),
            VersioningAction(minorRelease, version, Version::bumpMinor),
            VersioningAction(patchRelease, version, Version::bumpPatch)
        )

    private fun registerTasks(project: Project, actions: List<VersioningAction>): Unit  =
        actions.forEach { action ->
            project.tasks.run {
                register(action.name) {
                    it.group = group
                    it.actions = listOf(action)
                }
            }
        }
}

