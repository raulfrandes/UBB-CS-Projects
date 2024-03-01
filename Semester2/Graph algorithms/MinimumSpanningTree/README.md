# Kruskal’s Minimum Spanning Tree (MST) Algorithm

For a weighted undirected graph, the program finds a minimum spanning tree using Kruskal's algorithm. The given graph is connected.

Input and output file paths are given as parameters on the command line, the first being the input file path and the second being the output file path.

### The input file

The input file contains on the first line 2 numbers separated by space: V E. V represents the number of nodes of the graph. E represents the number of edges of the graph. The following E lines will contain 3 numbers separated by space, representing one edge each: x y w. x and y are the nodes connected by the edge, and w is the weight associated with the edge. Indexing of vertices starts from 0.

### The output file

The output file contains on the first line a number C representing the cost associated with the minimum spanning tree. On the second line it will contain a number N representing the number of edges of the tree. On the next N lines the edges of the tree will be described, through the two nodes they connect.