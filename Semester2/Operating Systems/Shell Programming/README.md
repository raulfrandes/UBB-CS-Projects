# Exercises:

1. Write a bash script that displays the first 3 lines and the last 3 lines of each text file in a directory given as an argument in the command line. If a text file has fewer than 6 lines, then its entire content will be displayed. (commands: find, file, grep, wc, head, tail)

2. Write a bash script that calculates the average number of lines in text files from a directory given as an argument. (commands: find, file, grep, wc)

3. Write a bash script that for each argument in the command line:
    - if it is a file, will display the name, number of lines, and number of characters (in this order)
    - if it is a directory, will display the name and how many files it contains (including in subdirectories).
(commands: find, wc)

4. Write a bash script that takes any number of filenames as arguments. The script will read two words from the keyboard and replace ALL occurrences of the first word with the second word in each file provided as an argument.
(commands: read, sed)

5. Write a bash script that takes triplets as arguments, each consisting of a filename, a word, and a number k. For each such triplet, the script will display all lines of the file that contain the specified word exactly k times.
(commands: shift, wc, sed, grep)