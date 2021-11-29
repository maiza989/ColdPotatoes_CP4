package compiler;
/**
 * A class that gathers a hashmap symbol table to store data the listener needs to use along with ANTLR classes to 
 * build a compiler with the use of lexers and parser ANTLR generates.  
 * @author Bobby Gabriel 
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
			
			
		//all variables
		public class variable{

			public String variableType = "";
			public String value = "";
			public int memLoc = -1;
			public boolean valueSet = false;

			public variable(String variableType, String value){
			
				this.variableType = variableType;
				this.value = value;
			
			}//end constructor
			
			public variable(){
			
				variableType = "";
				value = "";
				
			}//end empty argument constructor
			
			
		}//end variable	
			
			//Hashmap for the symbol table
			
			public HashMap<String, variable> SymbolTable = new HashMap<String, variable>();
			public static final String INT = "INTEGER";
			public static final String STR = "STRING";
			
			public variable currvar;
			public variable extravar;
			public variable var1;
			public variable var2;
			
			public String outputString;
			public String key;
			public String key2;
			public String keyID;
			public String id;
			public String genNum;
			public String genIntStr = "  ";
			public String genString;
			public String op1 = "";
			public String op2 = "";
			public String operation = "  ";
			public String arithmeticOperation = "  ";
			public String compString;
			public String decOp1;
			public String decOp2;
			public String decCompSymbol;
			public String prev;
			public String then1;
			public String then2;
			public String else1;
			public String else2;
			
			public int decComparison;
			public int decOperator1;
			public int decOperator2;
			public int operator1;
			public int operator2;
			public int num;
			public int outputInt;
			public int count;
			public int decCount;
			public int skipCount = 0;
			public int tempInt;
			
			public boolean exit = false;
			public boolean printString;
			public boolean operationDone;
			public boolean genBool;
			public boolean genPrint;
			public boolean expre;
			public boolean expression1;
			public boolean printTwice;
			
			public int memoryCounter = 1;

			//Prints the symbol table using hashmap
			public void printHashMap(HashMap<String,variable> map){
				
				Object[]keys = map.keySet().toArray();
				String val;
				int mem;
				boolean set;
				
				for(int i = 0; i < keys.length; i++){
					System.out.print(keys[i]);
					System.out.print(": " + map.get(keys[i]).variableType); 
					val = map.get(keys[i]).value;
					mem = map.get(keys[i]).memLoc;
					set = map.get(keys[i]).valueSet;
					System.out.println(", " + val + ", " + mem + ", " + set);
					
				}//end for	
				
			}//end printHashMap
		
			//Starting to use ASM to create a class
			public void createClass() {
				
				
				//An if statement to return if exit from program
				if(exit) {
					return;
					
				}//end if 
				
				
				//ASM classWriter to create the class				
				cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		       	cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC,"output/"+this.programName, null, "java/lang/Object",null);
			
		       	//ASM methodVisitor for the constructor
				MethodVisitor mv=cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		       	mv.visitCode();
		       	mv.visitVarInsn(Opcodes.ALOAD, 0); //load the first local variable: this
		       	mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V",false);
		       	mv.visitInsn(Opcodes.RETURN);
		       	mv.visitMaxs(1,1);
		       	mv.visitEnd();
		       	
		       	
			 	mainVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,  "main", "([Ljava/lang/String;)V", null, null);
		       	mainVisitor.visitCode();
		
			}//end setupClass
	
			//close the created ASM class
			public void closeClass() {
			
				if(exit) {
					return
				}//end if
				
				//write the bytecode file using utilites class
				mainVisitor.visitInsn(Opcodes.RETURN);
				mainVisitor.visitMaxs(3, 3);
				mainVisitor.visitEnd();
				cw.visitEnd();

		        byte[] b = cw.toByteArray();


		        Utilities.writeFile(b,"output/"+this.programName+".class");
		       	System.out.println("Done!");
		       	
	}// end closeClass
			
			
			
		
}//end myListener
