import java.util.zip.ZipFile

plugins {
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT"
    `maven-publish`
}

group = property("maven_group") as String
version = "${property("mod_version")}+fabric-mc${property("minecraft_version")}"

base {
    archivesName.set(property("archives_base_name") as String)
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
    flatDir {
        dirs("libs", "../../create-rei/CreateReiViewer-Fly/build/libs")
    }
}

val recipeViewer = ":CreateReiViewer:${property("createreiviewer_version")}+fabric-mc${property("minecraft_version")}"

loom {
    mods {
        create("createdeco") {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
    implementation("maven.modrinth:create-fly:${property("create_fabric_version")}")

    include(recipeViewer)
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(25)
    options.compilerArgs.addAll(listOf("-Xmaxerrs", "10000", "-Xmaxwarns", "1000"))
}

tasks.withType<AbstractCopyTask>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    val modMetadata = mapOf(
        "version" to project.version.toString(),
        "minecraft_dependency_version" to project.property("minecraft_dependency_version") as String,
        "fabric_loader_version" to project.property("fabric_loader_version") as String,
        "create_fabric_version_range" to project.property("create_fabric_version_range") as String,
    )
    inputs.properties(modMetadata)
    filesMatching("fabric.mod.json") {
        expand(modMetadata)
    }
}

tasks.jar {
    from("LICENSE")
    from("NOTICE")
}

tasks.named<Jar>("sourcesJar") {
    from("LICENSE")
    from("NOTICE")
}

val allowedJarPrefixes = listOf(
    "com/github/talrey/createdeco/",
    "assets/createdeco/",
    "data/createdeco/",
    "data/c/",
    "data/minecraft/",
)

val allowedJarFiles = listOf(
    "fabric.mod.json",
    "createdeco.mixins.json",
    "icon.png",
    "pack.mcmeta",
    "LICENSE",
    "NOTICE",
)

val foreignTag = Regex("^data/[a-z0-9_.-]+/tags/.+\\.json$")

fun checkNamespaces(archive: File): List<String> {
    val strays = mutableListOf<String>()
    val zip = ZipFile(archive)
    try {
        val entries = zip.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            if (entry.isDirectory) continue
            val name = entry.name
            if (name.startsWith("META-INF/")) continue
            if (name in allowedJarFiles) continue
            if (allowedJarPrefixes.any { name.startsWith(it) }) continue
            if (foreignTag.matches(name)) continue
            strays += name
        }
    } finally {
        zip.close()
    }
    return strays
}

val verifyNamespaces = tasks.register("verifyNamespaces") {
    dependsOn(tasks.jar, tasks.named("sourcesJar"))
    val jars = listOf(tasks.jar, tasks.named<Jar>("sourcesJar")).map { it.get().archiveFile }
    doLast {
        val problems = jars.flatMap { file ->
            val archive = file.get().asFile
            checkNamespaces(archive).map { "${archive.name}: $it" }
        }
        if (problems.isNotEmpty()) {
            throw GradleException("Foreign namespaces in the build output:\n" + problems.joinToString("\n"))
        }
    }
}

tasks.named("check") {
    dependsOn(verifyNamespaces)
}

tasks.register("printCompileClasspath") {
    val cp = sourceSets.main.get().compileClasspath
    val out = layout.projectDirectory.file(".classpath.txt")
    doLast {
        out.asFile.writeText(cp.files.joinToString("\n") { it.absolutePath } + "\n")
    }
}
