# SeCuGrow - Let your Selenium Cucumber Project grow

![SeCuGrow Logo](/docs/pics/SeCuGrow_Logo_800x400.png)

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

<user dir, depens on your OS>/.m2/repository/at/co/boris/secuton-archetype/<version>secuton-archetype-<version>.jar

You're all set - feel free to use the archetype.




Thx to Adi Musilek (https://github.com/adimusilek) for support

## Use this archetype to generate a project


     mvn archetype:generate \  
        -DarchetypeArtifactId=secuton-archetype \
        -DarchetypeGroupId=at.co.boris \
        -DarchetypeVersion=1.0.0 \
        -DgroupId=<your-group-id> \
        -DartifactId=<your artifactid> \
        -DinteractiveMode=false



example

     mvn archetype:generate \  
        -DarchetypeArtifactId=secuton-archetype \
        -DarchetypeGroupId=at.co.boris \
        -DarchetypeVersion=1.0 \
        -DgroupId=at.some.test \
        -DartifactId=testArchetype \
        -DinteractiveMode=false
