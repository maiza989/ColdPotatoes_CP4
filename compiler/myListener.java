package compiler;
/**
 * A class that gather hashmap symbol table to store data the listener need to use along with ANTLR classes to 
 * build a compiler with the use of lexers and parser ANTLR generates.  
 * @author Bobby Gebriel 
 * @version 1.0
 * Programming Project 4
 * CS322 - Compiler Construction 
 * Fall 2021
 * 
 */

//ASM and ANTLR packages
import static utils.Utilities.writeFile;
import lexparse.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.objectweb.asm.Label;
import static org.objectweb.asm.Opcodes.*;
import java.util.*;

public class myListener {
	


	public class myListener extends KnightCodeBaseListener {

		private ClassWriter cw;
		private MethodVisitor mainVisitor;
		private boolean debug;
		private String programName:
			
			
			/**
			 * A class for variables
			 * @author maith
			 *
			 */
		public class variable{

			public String variableType = "";
			public String value = "";
			public int memLoc = -1;
			public boolean valueSet = false;

			public variable(String variableType, String value){
			
				this.variableType = variableType;
				this.value = value;
			
			}//end of variable constructor
			
			public variable(){
			
				variableType = "";
				value = "";
				
			}//end of variable constructor


		}//end of variable class
}
