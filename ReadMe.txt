Program has been implemented in java language. 
To execute the code, type following commands in command terminal 

1) Compile source code using below command -
javac PageRankSequential.java

2) Run executable class file using below command -
java PageRankSequential pagerank.input.1000.15 pagerank_output.txt 10 0.85

Program takes 4 arguments to run in following format -
java PageRankSequential [input file name] [output file name] [iteration count] [damping factor]
where,
1. input file name : file containing the adjacency matrix as input for program should be placed in the same folder as class file.
2. output file name: file where top 10 url is written after sorting calculated page rank values.
3. iteration count : specifies number of iterations for calculating page rank. Higher the number of iterations, more accurate the result would be. It should be greater than 0.
4. damping factor  : the constant value usually 0.85. It should be between 0 to 1.

On execution this program will output top 10 urls to specified output file and print all sorted page rank values to console along with total turnaround time. 
