#!/bin/bash

# ./ex1.sh name_dir

DIR=$1
FILES=$(find $DIR -type f)

for FILE in $FILES
do
	if file $FILE | grep -q 'ASCII text$'
	then
		NL=$(wc -l <$FILE)
		if [ $NL -lt 6 ]
		then
			cat -n $FILE
		else
			echo '----- HEAD -----'
			head -3 $FILE
			echo '----- TAIL -----'
			tail -3 $FILE
			echo '----------------'
		fi
	fi
done

