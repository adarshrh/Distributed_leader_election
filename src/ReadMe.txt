Group Members:
Adarsh Raghupti       axh190002
Akash Akki            apa190001
####Implementation of LCR algorithm

NOTE: Process with lowest process ID is selected as Leader.

###### Steps to run the code via command line
* Unzip the source code files
* Run the following command from unzipped location to compile the code
javac <path of unzipped folder location>/*.java
* Run following command to execute the code
java <path of unzipped folder location>/ElectionCoordinator <path of input file> <path of output file>
Ex: java ElectionCoordinator ./input/input.dat ./output/testout.dat

###### Steps to run the code in IntelliJ IDE
* Create an empty java project
* Unzip the source code files and paste it under the location "Java Project Name"/src folder
* Open the ElectionCoordinator.java and provide input file path, output file path in command line argument option in run configuration.
* Run the ElectionCoordinator.java program


###### Sample input:
10
1
5
11
13
7
6
25
30
34
29
##### Sample Output: NOTE: Process with lowest process ID is selected as Leader.
Thread/Process ID: 5 Leader ID: 1
Thread/Process ID: 11 Leader ID: 1
Thread/Process ID: 13 Leader ID: 1
Thread/Process ID: 7 Leader ID: 1
Thread/Process ID: 6 Leader ID: 1
Thread/Process ID: 25 Leader ID: 1
Thread/Process ID: 30 Leader ID: 1
Thread/Process ID: 34 Leader ID: 1
Thread/Process ID: 29 Leader ID: 1
Thread/Process ID: 1 Leader ID: 1