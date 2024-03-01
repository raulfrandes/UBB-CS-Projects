# Huffman Decoding

When constructing the tree used to establish Huffman codes, priority when choosing nodes is frequency, and in case of equality, the node in whose subgraph finds the character with the lowest ASCII code is chosen.

Input and output file paths are given as parameters on the command line, the first being the input file path and the second being the output file path.

### The input file

The input file will contain the alphabet used in Huffman encoding and the encoded text. On the first line you will find a number N representing the number of characters in the alphabet. On the following N lines find the alphabet described. Each line contains a ch character, a space, and a fr number (no other white spaces). fr is the frequency of the character ch in the encoded text. The alphabet will be displayed in lexicographic order of characters (depending on the ASCII code). The next line contains a string of 0's and 1's representing the Huffman encoding of the text in the file input.

### The output file

The output file contains the decoded text