package io.mjmoore.gradle

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

class VersionTest {

    companion object {
        @JvmStatic
        fun invalidVersions(): Stream<Arguments> = Stream.of(
            Arguments.of(Object()),
            Arguments.of(1),
            Arguments.of(1L),
            Arguments.of(Version(1, 0, 0)),
            Arguments.of(""),
            Arguments.of("test"),
            Arguments.of("a.b.c"),
            Arguments.of(" "),
            Arguments.of("-1.-1.-1"),
            Arguments.of("1"),
            Arguments.of("1.0"),
            Arguments.of("1.0.0.0"),
            Arguments.of("${Integer.MIN_VALUE}.${Integer.MIN_VALUE}.${Integer.MIN_VALUE}")
        )
    }

    @MethodSource("invalidVersions")
    @ParameterizedTest
    fun `Invalid strings do not create a version`(version: Any) {
        Version.from(version)
            .let { assertThat(it).isNull() }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "1.0.0",
        "0.0.0",
        "${Integer.MAX_VALUE}.${Integer.MAX_VALUE}.${Integer.MAX_VALUE}"
    ])
    fun `Valid strings create versions`(version: String) {
        Version.from(version)
            .let { assertThat(it).isNotNull }
    }

    @Test
    fun `Bumping version number`() {
        val version = Version(0, 0, 0)
        assertThat(version.bumpMajor().major).isEqualTo(1)
        assertThat(version.bumpMinor().minor).isEqualTo(1)
        assertThat(version.bumpPatch().patch).isEqualTo(1)

        assertThat(version.toString()).isEqualTo("0.0.0")
    }

    @Test
    fun `Bumping version resets to zero for less significant version parts`() {
        val initial = Version(0, 0, 0)

        val patchRelease = initial.bumpPatch()
        assertThat(patchRelease).isEqualTo(Version(0, 0, 1))

        val minorRelease = patchRelease.bumpMinor()
        assertThat(minorRelease).isEqualTo(Version(0, 1, 0))

        val majorRelease = minorRelease.bumpMajor()
        assertThat(majorRelease).isEqualTo(Version(1, 0, 0))

        val otherMajorRelease = patchRelease.bumpMajor()
        assertThat(otherMajorRelease).isEqualTo(Version(1, 0, 0))
    }
}
