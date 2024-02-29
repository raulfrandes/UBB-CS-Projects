# Prüfer decoding
Input and output file paths are given as parameters on the command line, the first being the input file path and the second being the output file path.

### The input file
The input file contains two lines. The first line contains the number of values ​​from the Prüfer encoding of the tree, M. The second line contains M space-separated values, representing the Prüfer encoding. Node indexing of the coded tree starts from 0.

## The output file
The output file will contain on the first line a number N, the number of nodes in the tree. The second line will contains N values. The value at position i represents the parent of node i. For the root of the tree, instead of parent the value -1 is displayed.