import org.apache.commons.io.FileUtils

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


Path projectPath = Paths.get(request.outputDirectory, request.artifactId)
Files.move(
        projectPath.resolve("gitignore"),
        Paths.get(projectPath.toAbsolutePath().toString(), ".gitignore"),
        StandardCopyOption.REPLACE_EXISTING)

Properties properties = request.properties
println " -> properties: $properties"

def packageName = properties.get("package").toString()
println " -> package: $packageName"

def a11y = properties.get("a11y").toString()
println " -> a11y: $a11y"

def packagePath = packageName.replace(".", "/").toString()
println " -> converted to packagePath: $packagePath"

def packageBasePath = Paths.get(projectPath.toString(), "/src/test/kotlin/", packagePath)

if (Boolean.valueOf(a11y)) {
    def pathToFile = Paths.get(packageBasePath.toString(), "/step_definitions/AbstractStepDefs_noa11y.kt")
    println " -> deleting file: $pathToFile"
    def exists = Files.deleteIfExists(pathToFile.toAbsolutePath())
    println " -> file was deleted $exists"
} else {
    println " -> a11y-value $a11y\n" +
            "\t -> TODO delete AbstractStepDefs.kt in $packagePath/step_definitions and rename AbstractStepDefs_noa11y.kt to AbstractStepDefs.kt"

}
