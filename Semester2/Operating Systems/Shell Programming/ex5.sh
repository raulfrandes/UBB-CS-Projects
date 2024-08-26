#!/bin/bash

#./ex5.sh file1 word1 2 file2 word2 3 ...

while [ ! $# -eq 0 ] #while there are arguments in the command line
do
	FILE=$1
	WORD=$2
	K=$3

	NL=$(wc -l <$FILE)
	for L in $(seq 1 $NL)
	do
		LINE = $(sed -n ''$L'p' $FILE)
		NA=$(echo $LINE | grep -o '\b'$WORD'\b' | wc -l)
		if [ $NA -eq $K ]
		then
			echo 'Line' $L':' $LINE
		fi
	done

	shift 3
done
