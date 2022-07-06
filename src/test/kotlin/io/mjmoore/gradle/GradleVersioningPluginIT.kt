package io.mjmoore.gradle

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter
import kotlin.io.path.deleteIfExists

class GradleVersioningPluginIT {
    companion object {

        @TempDir
        @JvmStatic
        private lateinit var tempDir: Path
    }

    private lateinit var testBuildFile: Path
    private lateinit var testSettingsFile: Path

    @BeforeEach
    fun setup() {
        testBuildFile = tempDir
            .resolve("build.gradle.kts")
            .let { Files.createFile(it) }

        testSettingsFile = tempDir.resolve("settings.gradle.kts")
            .let { Files.createFile(it) }
    }

    @AfterEach
    fun tearDown() {
        testBuildFile.deleteIfExists()
        testSettingsFile.deleteIfExists()
    }

    @Test
    fun `Versioning action fails when no version information is found`() {

        testBuildFile.bufferedWriter().use { writer ->

            writer.write(
                """
                    plugins {
                      id("io.mjmoore.gradle.gradle-versioning")
                    }
                """.trimIndent()
            )
        }

        assertThrows<UnexpectedBuildFailure> {

            GradleRunner.create()
                .withProjectDir(tempDir.toFile())
                .withArguments(GradleVersioningPlugin.majorRelease)
                .withPluginClasspath()
                .build()
        }
    }

    @Test
    fun `Versioning action succeeds when version information is present`() {
        testBuildFile.bufferedWriter().use { writer ->
            writer.write(
                """
                    plugins {
                      id("io.mjmoore.gradle.gradle-versioning")
                    }
                    
                    version = "1.0.0"
                """.trimIndent()
            )
        }

        val result = GradleRunner.create()
            .withProjectDir(tempDir.toFile())
            .withArguments(GradleVersioningPlugin.minorRelease)
            .withPluginClasspath()
            .build()

        assertThat(result.task(":${GradleVersioningPlugin.minorRelease}")?.outcome)
            .isEqualTo(TaskOutcome.SUCCESS)

        testBuildFile.bufferedReader()
            .use { it.readText() }
            .let { assertThat(it).contains("version = \"1.1.0\"") }
    }


}
