#!/bin/bash

# ./ex3.sh /etc numbers.txt vasile

for NAME in $*
do
	if [ -f $NAME ]
	then
		echo 'File:' $NAME
		echo 'Number of lines:' $(wc -l <$NAME)
		echo 'Number of characters:' $(wc -m <$NAME)
	elif [ -d $NAME ]
	then
		echo 'Directory:' $NAME
		echo 'Number of files:' $(find $NAME -type f | wc -l)
	else
		echo 'Unknown' $NAME
	fi
done
