# BostonMetro
Built using Java
This project finds the shortest path from one station to another in Boston using Dijkstra's algorithm. It take 3 command line arguement, the first is the bostonmetro.csv file, the 2nd is the name of your current station, and the final argument is the name of the station your trying to get to. It prints out the route and estimate time it will take a arrive.
I used the JDK and Princeton libraries in this project. The JAR file for the Princeton is include in the lib directory.

If using Eclipse, this video will help you import the JAR file
https://www.youtube.com/watch?v=UtzAf8tyuAM&t=39s

To compile in terminal
First cd to the src directory

javac -cp ../lib/algs4.jar *.java MetroGraph.java Platform.java ShortestPath.java

To Run on Mac/Linux:
java -cp ../lib/algs4.jar:. ShortestPath ../bostonmetro.csv Ruggles Andrew

On Windows:
java -cp ../lib/algs4.jar;. ShortestPath ../bostonmetro.csv Ruggles Andrew

You can change the final two arguements, its up to you.
Before changing, look at the bostonmetro.csv file and look at the name of the stations.
