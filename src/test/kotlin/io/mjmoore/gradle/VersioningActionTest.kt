package io.mjmoore.gradle

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Project
import org.gradle.api.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter

class VersioningActionTest {

    companion object {

        @TempDir
        @JvmStatic
        private lateinit var tempDir: Path
    }

    private lateinit var testBuildFile: Path

    private val testProject = mockk<Project> {
        every { name } returns "test"
    }

    private val task = mockk<Task> {
        every { project } returns testProject
    }

    private val version = Version(1, 0, 0)
    private val action = VersioningAction("test", version, Version::bumpMajor)

    @BeforeEach
    fun setup() {
        testBuildFile = tempDir
            .resolve("build.gradle.kts" + System.nanoTime())
            .let { Files.createFile(it) }


        every { testProject.buildFile } returns testBuildFile.toFile()

    }

    @Test
    fun `Version is updated when present in build file`() {

        testBuildFile.bufferedWriter()
            .use { it.write("version = \"1.0.0\"") }

        action.execute(task)

        testBuildFile.bufferedReader()
            .use { it.readText() }
            .let { assertThat(it).isEqualTo("version = \"2.0.0\"") }
    }

    @Test
    fun `No processing occurs when version is not found in build file`() {

        action.execute(task)

        testBuildFile.bufferedReader()
            .use { it.readText() }
            .let { assertThat(it).isEmpty() }
    }
}
