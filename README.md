<p align="center">
  
  <h2 align="center">Schema Similarity</h2>

  <p align="center">
    This is a tool that implements a master thesis "A text similarity-based approach for extracting JSON conceptual schemas". This process is formed by ten algorithms whose six of then are working in this software.
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

- Clone the repo: `git clone https://github.com/twbs/bootstrap.git`
- Needs Maven Project
- Needs install two external JARs that are not included in Maven Repo:
- Ws4j:`mvn install:install-file -Dfile=/home/fhabiana/Downloads/libs/ws4j-1.0.1.jar -DgroupId=de.sciss -DartifactId=ws4j -Dversion=1.0.1 -Dpackaging=jar -DgeneratePom=true`
- Jawjaw: `mvn install:install-file -Dfile=/home/fhabiana/Downloads/libs/jawjaw-1.0.2.jar -DgroupId=de.sciss -DartifactId=jawjaw -Dversion=1.0.2 -Dpackaging=jar -DgeneratePom=true`

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

AAA

## Creators

**Fhabiana Thieli Machado**

- <fsantos@inf.ufsm.br>

**Renata Padilha**

- <rpadilha@inf.ufsm.br>


## Copyright and license

Code and documentation copyright 2017-2018.
