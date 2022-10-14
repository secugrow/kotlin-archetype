
import org.apache.commons.io.FileUtils
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

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
def resourcePath = Paths.get(projectPath.toString(), "/src/test/resources/")

println "-> packageBasePath: $packageBasePath"

def handle_a11y_marker(Path path, Boolean isA11y) {

    print "\n::: searching for a11y-Marker :::\n===========================================================\n"

    def nrOfDirectories = 0
    def nrOfFiles = 0

    try {
        Files.walkFileTree(path, new FileVisitor<Path>() {
            @Override
            FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE
            }

            @Override
            FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                def filename = file.fileName
                def fileAsString = Files.readString(file)
                if (fileAsString.contains("//a11y-start")) {
                    println "-> $filename contains a11y-marker"
                    switch (isA11y) {
                        case true:
                            println "  -> a11y is $isA11y...removing a11y markers only - NO a11y code gets deleted"
                            def replaced = fileAsString.replaceAll("(?s)\\/\\/a11y-start", "")
                                    .replaceAll("(?s)\\/\\/a11y-end", "")
                            Files.write(file, replaced.bytes)
                            break
                        case false:
                            println "  -> a11y is $isA11y...removing a11y code ¯\\_(ツ)_/¯"
                            def replaced = fileAsString.replaceAll("(?s)\\/\\/a11y-start.*?\\/\\/a11y-end", "")
                            Files.write(file, replaced.bytes)
                            break
                    }
                }
                nrOfFiles++
                return FileVisitResult.CONTINUE
            }

            @Override
            FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
                return FileVisitResult.CONTINUE
            }

            @Override
            FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
                nrOfDirectories++
                return FileVisitResult.CONTINUE
            }
        }).toFile()
    } catch (Exception e) {
        println "error occured: $e"
    }
    print "::: done - checked $nrOfFiles files in $nrOfDirectories directories :::\n===========================================================\n"
}


if (Boolean.valueOf(a11y)) {
    handle_a11y_marker(packageBasePath, true)
} else {
    def a11yPackage = Paths.get(packageBasePath.toString(), "a11y")
    FileUtils.deleteDirectory(a11yPackage.toAbsolutePath().toFile())

    handle_a11y_marker(packageBasePath, false)
}
