# Bellman-Ford | Dijkstra
For a weighted directed graph and a source vertex, the program calculates the minimum cost from the source vertex to each accessible vertex in the graph using the Bellman-Ford and Dijkstra algorithms.

Input and output file paths are given as parameters on the command line, the first being the input file path and the second being the output file path.

### The input file:
The input file contains on the first line 3 numbers separated by space: V E S. V represents the number of vertices for the given graph. E represents the number of arcs in the graph. S is the source node from which the minimum costs will be calculated. The following E lines will contain 3 numbers separated by space, each representing an arc: x y w. x is the source node of the arc, y is the destination node, and w is the weight. The indexing of the vertices starts from 0.

### The output file:
The output file will contain a single line with N values ​​separated by spaces. The value on position i represents the cost of the minimum cost path from source vertex S to vertex i. If vertex i is unreachable from vertex S, then the string "INF" will be written at position i.