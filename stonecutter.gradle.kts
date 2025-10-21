plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.21.8-fabric"

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) { 
    group = "project"
    ofTask("build")
}

// Feels like this is very much not where I'm meant to put this but...
tasks.register("datagenAll") {
    group = "project"

    doLast {
        exec {
            workingDir = project.rootDir
            executable = "./datagenAll.sh"
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.fabricmc.net/")
    }
}