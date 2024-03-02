# Ford-Fulkerson Algorithm for Maximum Flow

For a directed graph without circuits (each arc has an associated capacity), the program determines the maximum flow that can be sent from a source vertex s to a destination vertex t using the Ford-Fulkerson algorithm.

Input and output file paths are given as parameters on the command line, the first being the input file path and the second being the output file path.

### The input file

The input file contains on the first line 2 numbers separated by space V E where V represents the number of vertices of the graph and E represents the number of arcs of the graph. The following E lines contain 3 numbers separated by space, each representing an arc: x y c. x is the source node of the arc, y is the destination node, and c is the capacity of the arc. Indexing of vertices starts at 0. The source vertex is 0 and the destination vertex is (V - 1).

### The output file

The output file will contain a single line with the maximum flow value.