#!/bin/bash

codes="lab1prog01.java lab1prog02.java lab1prog03B.java lab1prog03.java lab1prog04.java lab1prog05.java 
lab1prog06.java lab1prog07.java lab1prog08.java lab1prog09B.java lab1prog09.java lab1prog10.java lab1prog11.java 
lab1prog12.java lab1prog13.java lab1prog14.java lab1prog15.java lab1prog16.java lab1prog17.java"


for i in $codes; do 
   echo $i
   javac $i
done; 


