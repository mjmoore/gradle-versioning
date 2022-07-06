# Gradle Versioning

A gradle plugin to allow versioning control as part of the build lifecycle:

```shell
$ ./gradlew clean build patchRelease publish
```

The plugin will rewrite the version information contained in the `build.gradle.kts` file.

## Usage

 * Declare the plugin as part of the plugin configuration:
 
 ```kotlin
 plugins {
     id("io.mjmoore.gradle.gradle-versioning") version "0.0.1"
 }
 ```
 
 * Run a release task:
 
 ```shell
 $ ./gradlew majorRelease
 
 > Task :majorRelease
 Updated gradle-versioning to version 1.0.0
 ```
 


## Tasks

The plugin provides three simple tasks in the group `versioning`:

 * `majorRelease`
 * `minorRelease`
 * `patchRelease`

### Limitations 

 * Versioning must be in the form `"\\d+.\\d+.\\d+"`, e.g. `"1.0.0"`
 * Groovy based build files are not supported.
 * `gradle.properties` based version configuration is not supported.
