#!/bin/bash

NF=0 #or declare -i NF=0 will be an integer and than we can write NF=NF+1
NTL=0

for FILE in $(find $1 -type f)
do
	if file $FILE | grep -q 'ASCII text$'
	then
		NL=$(wc -l <$FILE)
		NTL=$[NTL+NL] #same as $(())
		NF=$((NF+1))
	fi
done

echo 'NF:' $NF 'NTL:' $NTL 'The average number of lines:' $[NTL/NF]
