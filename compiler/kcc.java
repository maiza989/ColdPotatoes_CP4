package compiler;
/**
 * Description
 * @author Bobby Gabriel 
 * @version 1.0
 * Programming Project 4
 * CS322 - Compiler Construction 
 * Fall 2021
 */

//Exception and ANTLR packages
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.gui.Trees;
import lexparse.*;
import java.util.*;
import java.io.IOException;


public class kcc {

	public static void main(String[] args) {
		CharStream input;
	    KnightCodeLexer lexer;
	    CommonTokenStream token;
	    KnightCodeParser parser;

	    String file;
	    String output;
		
		
		//An if else statment that pass the knightcode file to the output folder for result files.
		 
		if(args.length == 2){
		
			file = args[0];
			output = args[1];
			
		} else if(args.length == 1){ 
		
			file = args[0];
			output = "output/result";
		
		} else {
		
			file = "tests/program1.kc";
			output = "output/result";
		
		}//end if/else 
		
		
		 //takes input from the user and then compiles it using myListener class
		 
	        try{
	        
	            input = CharStreams.fromFileName(file);//file input from user
	            lexer = new KnightCodeLexer(input); //creates the lexer
	            token = new CommonTokenStream(lexer);//creates the token stream
	            parser = new KnightCodeParser(token);//creates the parser
	        
	       
	            //begins the start location of parsers
	            ParseTree tree = parser.file(); 
	            
	            
	            //uses the listner to begin walking the parse tree
	            myListener listener = new myListener(output);
	            ParseTreeWalker walker = new ParseTreeWalker();
		    
	            walker.walk(listener, tree);
	            
	        
	        }//end try
	        
	        catch(IOException e){
	            System.out.println(e.getMessage());
	        }//end catch 
	        
	    }//end main
	    
	}//end kcc
