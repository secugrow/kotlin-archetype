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
def packageName = properties.get("package").toString()
def a11y = properties.get("a11y").toString()
def packagePath = packageName.replace(".", "/").toString()
def packageBasePath = Paths.get(projectPath.toString(), "/src/test/kotlin/", packagePath)

if (Boolean.valueOf(a11y)) {
    def pathToFile = Paths.get(packageBasePath.toString(), "/step_definitions/AbstractStepDefs_noa11y.kt")
    Files.deleteIfExists(pathToFile.toAbsolutePath())
} else {
    def pathToAbstractStepDefs_noa11y = Paths.get(packageBasePath.toString(), "/step_definitions/AbstractStepDefs_noa11y.kt")
    def pathToAbstractStepDefs = Paths.get(packageBasePath.toString(), "/step_definitions/AbstractStepDefs.kt")

    Files.deleteIfExists(pathToAbstractStepDefs.toAbsolutePath())
    def a11yPackage = Paths.get(packageBasePath.toString(), "a11y")

    FileUtils.deleteDirectory(a11yPackage.toAbsolutePath().toFile())

    pathToAbstractStepDefs_noa11y.toFile().renameTo(pathToAbstractStepDefs.toFile())
}
