package io.mjmoore.gradle

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int
) {
    companion object {

        private val versionRegex = Regex("^\\d+\\.\\d+\\.\\d+")

        fun from(version: Any): Version? =
            when (version) {
                is String ->
                    version.takeIf { it.matches(versionRegex) }
                        ?.split(".")
                        ?.map { it.toInt() }
                        ?.let { Version(it[0], it[1], it[2]) }
                else -> null
            }
    }

    override fun toString(): String = "$major.$minor.$patch"

    fun bumpMajor(): Version = copy(major = major + 1)
    fun bumpMinor(): Version = copy(minor = minor + 1)
    fun bumpPatch(): Version = copy(patch = patch + 1)
}
