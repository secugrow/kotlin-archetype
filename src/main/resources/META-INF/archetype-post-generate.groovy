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

def packageName = properties.get("package")
println " -> package: $packageName"

def a11y = properties.get("a11y")
println " -> a11y: $a11y"

def packagePath = packageName.replace(".", "/")
println " -> packagePath: $packagePath"