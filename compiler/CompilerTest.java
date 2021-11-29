package knightcodecompiler;
/**
 * This class encapsulates a basic grammar test.
 * @author Bobby Gabriel
 * @version 1.0
 * Programming Project 4
 * CS322 - Compiler Construction
 * Fall 2021
 */

//Exception and Antlr packages
import java.io.IOException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.gui.Trees;

public class CompilerTest{


    public static void main(String[] args){
        CharStream input;
        KnightCodeLexer lexer;
        CommonTokenStream tokens;
        KnightCodeParser parser;

        try{
            input = CharStreams.fromFileName(args[0]);  //get the input
            lexer = new KnightCodeLexer(input); //create the lexer
            tokens = new CommonTokenStream(lexer); //create the token stream
            parser = new KnightCodeParser(tokens); //create the parser
       
            ParseTree tree = parser.file();//set the start location of the parser
             
            
            Trees.inspect(tree, parser);
            
        
        }//end try
        catch(IOException e){
            System.out.println(e.getMessage());
        }//end catch


    }//end main




}//end CompilerTest