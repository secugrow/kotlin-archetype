# Releasing a New Version of secugrow-kotlin-archetype

## Release Process

### 1. Prepare Release

The first step is to prepare the release using the Maven Release Plugin:

```bash
mvn release:prepare -Prelease
```

This command will:
- Check for uncommitted changes
- Check that there are no SNAPSHOT dependencies
- Update the version in the POM from `X.Y.Z-SNAPSHOT` to `X.Y.Z`
- Run the project tests against the modified POMs
- Commit the modified POMs
- Create a tag in SCM with the format `vX.Y.Z` (as configured in the POM)
- Update the version in the POM to the next development version (`X.Y.(Z+1)-SNAPSHOT`)
- Commit the modified POMs

You will be prompted to confirm or modify:
- The release version (default: current version without `-SNAPSHOT`)
- The SCM release tag (default: `vX.Y.Z`)
- The new development version (default: increment the minor version and add `-SNAPSHOT`)

### 2. Perform Release

After preparing the release, perform it with:

```bash
mvn release:perform -Prelease
```

This command will:
- Check out the release tag from SCM
- Build the project with the release profile activated
- Deploy the built artifacts to Maven Central via the central-publishing-maven-plugin

During this process:
- The release profile activates the following plugins:
  - `maven-source-plugin`: Creates a JAR with the source code
  - `maven-javadoc-plugin`: Generates and packages JavaDocs
  - `maven-gpg-plugin`: Signs all the artifacts with your GPG key
- The `central-publishing-maven-plugin` handles the actual publishing to Maven Central

### 3. Verify Release

After the release is performed:

1. Once published, your artifacts should be available in Maven Central after some time (usually within 30 minutes to 2 hours):
   - https://repo1.maven.org/maven2/io/secugrow/secugrow-kotlin-archetype/


### Cleaning Up After Failed Release

If the release process fails and you need to start over:

1. Delete the release tag locally and remotely:
   ```bash
   git tag -d vX.Y.Z
   git push origin :refs/tags/vX.Y.Z
   ```

2. Reset your local repository to the state before the release preparation:
   ```bash
   git reset --hard origin/main
   ```

3. Clean up the release backup files:
   ```bash
   mvn release:clean
   ```