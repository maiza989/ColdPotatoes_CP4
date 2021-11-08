
This project encapsulates the directory structure required for the final project in CS322. Additionally, it contains an ant build.xml file you can use to buid/compile/clean your project if you wish. Refer to the build.xml file for the build/clean targets.

# ColdPotatoes_CP4
The program was crated by Maitham Alghamgham, Bobby Gabriel.


### how to Comile the Comiler with ANT:
Assuming that you imported build.xml, knightcode.g4 and everything 

	 This script will enable you to convert a program from KnightCode to Java Source Code.
     build-grammar - ant will parse the grammar files.
     clean-grammar - ant will clean and delete all relevant files.
     compile-grammar - ant will compile the java files.
     compile-knightcode - ant will compile the java files but will not compile the parser files.
     run-compiler - ant will attempt to run the compiler. Used for basic test runs.
	 
#1 build-grammar runs clean-grammar
#2 compile-grammar, compile-knightcode, and run-compiler require build-grammar to be run prior to their use.
#3 compile-grammar runs compile-knightcode automatically.
#4 run-compiler runs compile-grammar automatically.
#5 run-compiler only tests the first program. Other programs must be manually entered.

### How To Run the Compiler to Compile a .kc File:

Keep in mind, that it is crucial that you specify the file types after entering each file name or else the program will not run correctly. 

Here is an example of a successful program;

		java kcc tests/program1.kc output/Program1

### Project Log:

Date | Team Member(s) | Summary of Work session | Hours

11/04/21 - Maitham - Creating the repository and adding the knightcode skeleton

11/04/21 - Maitham - Adding myListener class and setting up ASM class opening and closing methods - 2 hours

11/04/21 - Maitham - Added enteries and exits for listener after trying to figure how to force the output from antlr to a folder - 1.5-2 hours

11/04/21 - Maitham - Edited README.md file - 30 minutes 

11/06/21 - Maitham - Worked on adding entires and exiting notifications - 20 minutes  

11/08/21 - Maitham - Worked on figuring out the symbol table/hashmap,but no look - 2 hours