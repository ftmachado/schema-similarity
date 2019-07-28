<p>
  
  <h2>Schema Similarity</h2>

  <p>
    This is a tool that implements a master thesis "A text similarity-based approach for extracting JSON conceptual schemas". This process is formed by ten algorithms whose six of then are working in this software.
  </p>
  <p>
    The project uses similarity measures to identify equivalent fields in JSON documents. Due to the availability of libraries, the Java language was used. It is executed via console, generating the output files in a directory called `output`.
  </p>
</p>

<br>

## Table of contents

- [Quick-start](#quick-start)
- [What's included](#whats-included)
- [Documentation](#documentation)
- [Creators](#creators)
- [Copyright and license](#copyright-and-license)

## Quick Start

Several quick start options are available:

- Clone the repo: `git clone https://github.com/ftmachado/schema-similarity.git`
- Needs Maven Project
- Needs install two external JARs that are not included in Maven Repo:
- Ws4j:`mvn install:install-file -Dfile=/home/fhabiana/Downloads/libs/ws4j-1.0.1.jar -DgroupId=de.sciss -DartifactId=ws4j -Dversion=1.0.1 -Dpackaging=jar -DgeneratePom=true`
- Jawjaw: `mvn install:install-file -Dfile=/home/fhabiana/Downloads/libs/jawjaw-1.0.2.jar -DgroupId=de.sciss -DartifactId=jawjaw -Dversion=1.0.2 -Dpackaging=jar -DgeneratePom=true`
- If you have already install the external libraries, or are making some error in pom.xml, change in the pom.xml the system path of your .jar. For instance, in Windows I use: `<systemPath>C:\Users\Thieli\.m2\repository\de\sciss\ws4j\1.0.1\ws4j-1.0.1.jar</systemPath>` and `<systemPath>C:\Users\Thieli\.m2\repository\de\sciss\jawjaw\1.0.2\jawjaw-1.0.2.jar</systemPath>` for Linux I use: `<systemPath>${project.basedir}/src/main/resources/libs/ws4j-1.0.1.jar</systemPath>`.

## Run

- Make sure you have Apache Maven and Java Development Kit installed
- In pom.xml set file paths for dependency ws4j and jawjaw (its are commented!)
- Then build Maven Project
- Execute the line command: `java -jar schema-similarity-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Whats included

Within the download you'll find the following directories and files. You'll see something like this:

```
schema-similarity/
└── src/
    ├── main/
    │   ├── java
    │   │   ├── br.ufsm.ppgcc
    │   │   │   ├── Algoritmos.java
    │   │   │   ├── Main.java
    │   │   │   ├── Util.java
└── src/
    ├── main/
    │   ├── resources
        │   ├── artefatos/
        │   ├── json
└── pom.xml
```

## Documentation

The flow chart below represents the implementation

![Flow Chart](https://github.com/ftmachado/schema-similarity/blob/master/fluxograma.png)


## Creators

*Fhabiana Thieli Machado* - <fsantos@inf.ufsm.br>

*Renata Padilha* - <rpadilha@inf.ufsm.br>


## Copyright and license

Code and documentation copyright 2017-2018.
