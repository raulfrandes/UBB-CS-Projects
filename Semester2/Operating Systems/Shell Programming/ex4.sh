#!/bin/bash

read -p "First word: " WORD1
read -p "Second word: " WORD2

for FILE in $*
do
	sed -i 's/'$WORD1'/'$WORD2'/' $FILE
done
