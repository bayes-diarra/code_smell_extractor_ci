# code_smell_extractor_ci
Code smells extractor for projects on github

## Installations to do
Git : https://git-scm.com/downloads
PMD : https://pmd.github.io/

## Système d'exploitation 
 - Linux 
## To run the program
You must be on linux and you must modify the variables in src / main / java / utilities / Utility.java:
- PATH_PMD = "[your path] / pmd / bin /", the directory where you installed PMD
- PATH_REPOSITORY = "[your path] / gitRepository /", the directory in which the commits will be temporarily saved to perform the analysis
