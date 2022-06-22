[![CI-CD](https://github.com/secugrow/kotlin-archetype/actions/workflows/generate_archetype_output.yml/badge.svg?branch=main)](https://github.com/secugrow/kotlin-archetype/actions/workflows/generate_archetype_output.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.secugrow/secugrow-kotlin-archetype/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.secugrow/secugrow-kotlin-archetype)

![SeCuGrow Logo](/docs/pics/SeCuGrow_Logo_300x150.png)
# Let your Selenium Cucumber Project grow

### Looking for the documentation how to use it after generating an project from this archetype?
[follow this link](src/main/resources/archetype-resources/README.md)


## How to generate a ready to start project with this archetype

This archetype will generate you a Selenium Cucumber Skeleton for your projects with your choosen packagenames
A ready to use showcase ca be cloned/downloaded/forked from https://github.com/secugrow/generated-project.

### Installation
Before being able to make use of the `archetype:generate` command, the project has to be built from its root directory 
via `mvn install`. This will result in a jar being copied to your local maven-repo. 

OR

copy a jar from releases to your local maven repository in the correct path:

    <user dir, depens on your OS>/.m2/repository/io/secugrow/secugrow-kotlin-archetype/<version>/secugrow-kotlin-archetype-<version>.jar

OR

using a release archetype from a maven repository

You're all set - feel free to use the archetype.


## Use this archetype to generate a project

     mvn archetype:generate \  
        -DarchetypeArtifactId=secugrow-kotlin-archetype \
        -DarchetypeGroupId=io.secugrow \
        -DarchetypeVersion=1.3.0 \
        -DgroupId=<your-group-id> \
        -DartifactId=<your artifactid> \
        -DinteractiveMode=false


example

     mvn archetype:generate \  
        -DarchetypeArtifactId=secugrow-kotlin-archetype \
        -DarchetypeGroupId=io.secugrow\
        -DarchetypeVersion=1.3.0 \
        -DgroupId=io.secugrow.demo \
        -DartifactId=fromArchetype \
        -DinteractiveMode=false
