pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

        maven{
            url = uri("https://repo.eclipse.org/content/repositories/paho-snapshots/")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Mqtt Example"
include(":app")
 