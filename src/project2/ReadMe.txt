Group Members:
Adarsh Raghupti       axh190002
Akash Akki            apa190001
####Implementation of LCR algorithm


###### Steps to run the code via command line
* Unzip the source code files
* Run the following command from unzipped location to compile the code

javac <path of unzipped folder location>/*.java

* Run following command to execute the code

java <path of unzipped folder location>/project2.ElectionCoordinator <path of input file> <path of output file>


###### Steps to run the code in IntelliJ IDE
* Create an empty java project
* Unzip the source code files and paste it under the location "Java Project Name"/src folder
* Open the project2.ElectionCoordinator.java and provide input file path, output file path in command line argument option in run configuration.
* Run the project2.ElectionCoordinator.java program


###### Sample input:
4
8 5 4 9
0 1 0 0
1 0 1 0
0 1 0 1
0 0 1 0

##### Sample Output:
Found new maxID: Send EXPLORE to all neighbors except parent
Found new maxID: Send EXPLORE to all neighbors except parent
Found new maxID: Send NACK to old parent
Sending NACK from:5 to:8
Found new maxID: Send EXPLORE to all neighbors except parent
My ID:9 Leader ID:9
Found new maxID: Send EXPLORE to all neighbors except parent
My ID:4 Leader ID:9
My ID:5 Leader ID:9
My ID:8 Leader ID:9
Total msg sent:30