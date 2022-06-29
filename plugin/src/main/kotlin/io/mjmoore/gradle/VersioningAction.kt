package io.mjmoore.gradle

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task

class VersioningAction(
    val name: String,
    private val version: Version,
    val bumpVersion: (Version) -> Version,
) : Action<Task> {

    companion object {
        private val regex = Regex("version\\s+=\\s+\"\\d+\\.\\d+\\.\\d+\"")
    }

    override fun execute(task: Task) = updateProject(task.project)

    private fun updateProject(project: Project): Unit =
        bumpVersion(version)
            .also { newVersion ->
                project.buildFile.bufferedReader()
                    .use { it.readText() }
                    .replace(regex, "version = \"$newVersion\"")
                    .let { project.buildFile.writeText(it) }
            }
            .let { println("Updated ${project.name} to version $it") }
}
