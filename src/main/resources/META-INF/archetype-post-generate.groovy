import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


Path projectPath = Paths.get(request.outputDirectory, request.artifactId)
Files.move(
        projectPath.resolve("gitignore"),
        Paths.get(projectPath.toAbsolutePath().toString(), ".gitignore"),
        StandardCopyOption.REPLACE_EXISTING)


