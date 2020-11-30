Group Members:
Adarsh Raghupti       axh190002
Akash Akki            apa190001
####Implementation of Asynchronous Bellman Ford algorithm


###### Steps to run the code via command line
* Unzip the source code files
* Run the following command from unzipped location to compile the code

javac <path of unzipped folder location>/*.java

* Run following command to execute the code

java <path of unzipped folder location>/project3.ElectionCoordinator <path of input file> <path of output file>


###### Steps to run the code in IntelliJ IDE
* Create an empty java project
* Unzip the source code files and paste it under the location "Java Project Name"/src folder
* Open the project3.ElectionCoordinator.java and provide input file path, output file path in command line argument option in run configuration.
* Run the project3.ElectionCoordinator.java program



###### Sample input:
9 0
0 4 -1 -1 -1 -1 -1 8 -1
4 0 8 -1 -1 -1 -1 11 -1
-1 8 0 7 -1 4 -1 -1 2
-1 -1 7 0 9 14 -1 -1 -1
-1 -1 -1 9 0 10 -1 -1 -1
-1 -1 4 14 10 0 2 -1 -1
-1 -1 -1 -1 -1 2 0 1 6
8 11 -1 -1 -1 -1 1 0 7
-1 -1 2 -1 -1 -1 6 7 0

##### Sample Output:
My id:8 parentId:2 Distance to root:14
My id:2 parentId:1 Distance to root:12
My id:1 parentId:0 Distance to root:4
My id:4 parentId:5 Distance to root:21
My id:3 parentId:2 Distance to root:19
My id:5 parentId:6 Distance to root:11
My id:6 parentId:7 Distance to root:9
My id:7 parentId:0 Distance to root:8
My id:0 parentId:null Distance to root:0
Total msg sent:80