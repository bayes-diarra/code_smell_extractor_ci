# code_smell_extractor_ci
Code smells extractor for projects on github using PMD.

## Installations to do
- Git : Git is a free and open source distributed version control system designed to handle everything from small to very large projects with speed and efficiency.(https://git-scm.com/downloads)
- PMD : An extensible cross-language static code analyzer.(https://pmd.github.io/)

## Code smells that are using for analisis 
- duplicatedCode
- GodClass
- GodMethod 
- CyclomaticComplexity 
- ExcessiveParameterList 
- DataClass

## Système d'exploitation 
 - Linux 
## To run the program
You must be on linux and you must modify the variables in src / main / java / utilities / Utility.java:
- PATH_PMD = "[your path] / pmd / bin /", the directory where you installed PMD.
- PATH_REPOSITORY = "[your path] / gitRepository /", the directory in which the commits will be temporarily saved to perform the analysis.
