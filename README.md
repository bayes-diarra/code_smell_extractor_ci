# code_smell_extractor_ci
Code smells and Test smells extractor for projects on github using PMD and tsDetect.

## Installations to do
- Git : Git is a free and open source distributed version control system designed to handle everything from small to very large projects with speed and efficiency.(https://git-scm.com/downloads)
- PMD : An extensible cross-language static code analyzer.(https://pmd.github.io/)
Once pmd is downloaded, follow the instructions to install it:
    - On Linux:
    Install pmd in `$HOME/` and rename the parent folder just to pmd
    - On the windows:
    install pmd in `C:\` and rename the parent folder just to pmd
## No need install
- tsDetect : tsDetect is implemented as an open-source, command line-based tool that is available as a standalone Java jar file.(https://testsmells.org/pages/testsmelldetector-architecture.html) 
## Code smells that are analysing
There are more than five(5), you can modify the code to add some. 
- duplicatedCode
- GodClass
- GodMethod 
- CyclomaticComplexity 
- DataClass

## Test smells that are analysing 
- Assertion Roulette
- Conditional Test Logic
- Constructor Initialization
- Default Test
- Duplicate Assert
- Eager Test
- Empty Test
- Exception Handling
- General Fixture
- Ignored Test
- Lazy Test
- Magic Number Test
- Mystery Guest
- Redundant Print
- Redundant Assertion
- Resource Optimism
- Sensitive Equality
- Sleepy Test
- Unknown Test
## To run the program
- Windows : `java -jar .\extractor.jar -w -pmd|-tsd your_data_file_path`
- Linux : `java -jar ./extractor.jar -l -pmd|-tsd your_data_file_path`
- `-pmd` : to run PMD analysis
- `-tsd` : to run tsDetect analysis
## Enjoy it!
