# SeCuton - Selenium Cucumber Skeleton Generator


## How to generate a skeleton with this archetype


This archetype will generate you a SeCuton Skeleton for your projects with your choosen packagenames
An ready to use showcase ca be cloned/downloaded/forked from https://github.com/boris779/SeCuton

Thx to Adi Musilek (https://github.com/adimusilek) for support


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
