/**
 * A class that gather hashmap symbol table to store data the listener need to use along with ANTLR classes to 
 * build a compiler with the use of lexers and parser ANTLR generates.  
 * @author Maitham Alghamgham, Bobby Gebriel 
 * @version 1.0
 * Project 4
 * CS322 - Compiler Construction 
 * Fall 2021
 */


package compiler;

import static utils.Utilities.writeFile;
import lexparse.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.objectweb.asm.Label;
import static org.objectweb.asm.Opcodes.*;
import java.util.*;



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
		
	/**
	 * Setting up the hashmap for the symbol table
	 * All of our String and int and boolean we will be using throughout the program
	 */
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
	/**
	 * A method to print the Symbol table for stored vairable using hashmap
	 * @param map
	 */
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
			
		}//end of for loop	
		
	}//end of printHashMap
	
	/**
	 * A method to check if a variable is a string
	 * @param var
	 * @return
	 */
	public boolean isString(variable var){
		
		if(var.variableType.equals(STR))
			return true;
		return false;
		
	}//end ofisString
	
	/**
	 * Constrcoter for myListener class 
	 * @param programName
	 * @param debug
	 */
	public myListener(String programName, boolean debug){
	       
		this.programName = programName;
		this.debug = debug;

	}//end of myListener constructor
	
	public myListener(String programName){
	       
		this.programName = programName;
		debug = false;

	}//end of myListener constructor
	
	/**
	 * All usable Labels are here
	 */
	
	public Label printSkipIf = new Label();
	
	
	/**
	 * creating a method to call for setting up an ASM class to write bytecode with
	 */
	public void setupClass() {
		
				/**
				 * An if statement to return if exit from program
				 */
				if(exit) {
					return;
					
				}// end of if statement 
				
				/**
				 * Set up the classwriter
				 */
				cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		       	cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC,"output/"+this.programName, null, "java/lang/Object",null);
			
		       	/**
		       	 * Use local MethodVisitor to create the constructor for the object
		       	 */
				MethodVisitor mv=cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		       	mv.visitCode();
		       	mv.visitVarInsn(Opcodes.ALOAD, 0); //load the first local variable: this
		       	mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V",false);
		       	mv.visitInsn(Opcodes.RETURN);
		       	mv.visitMaxs(1,1);
		       	mv.visitEnd();
		       	
		       	
				/**
				 * Use global MethodVisitor to write bytecode according to entries in the parsetree	
				 */
			 	mainVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,  "main", "([Ljava/lang/String;)V", null, null);
		       	mainVisitor.visitCode();
		
	}// end of setupClass
	
	/**
	 * A method to close an ASM class.
	 */
	public void closeClass() {
			
				if(exit) {
					return
				}// end of if statment
				
				/**
				 * Use global MethodVisitor to finish writing the bytecode and write the binary file.
				 */
				mainVisitor.visitInsn(Opcodes.RETURN);
				mainVisitor.visitMaxs(3, 3);
				mainVisitor.visitEnd();

				cw.visitEnd();

		        byte[] b = cw.toByteArray();


		        Utilities.writeFile(b,"output/"+this.programName+".class");
		        
		       	System.out.println("Done!");
		       	
	}// end of closeClass
	/**
	 * Under here to contain will the enteries and exities fro KnightCodeParser
	 * @param ctx
	 */
	
	/**
	 * a method to enter File
	 * @param ctx
	 */
	@Override
	public void enterFile(KnightCodeParser.FileContext ctx){

		System.out.println("Enter program rule for first time");
		setupClass();
		
	}// end of enterFile
	
	/**
	 * a method to exit files
	 * @param ctx
	 */
	@Override
	public void exitFile(KnightCodeParser.FileContext ctx){
		
		if(exit) {
			return
		}// end of if statment
		
		System.out.println("Leaving program rule. . .");
		closeClass();

	}// end of exitFile
	
	/**
	 * A method to enter Declare 
	 * get child count
	 * @param ctx
	 */
	@Override 
	public void enterDeclare(KnightCodeParser.DeclareContext ctx){
		
		if(exit) {
			return
		}// end of if statment
		
		System.out.println("Enter Declare!");

		count = ctx.getChildCount();
		
	}// end of enterDeclare
	
	/**
	 * a method to exit Declare
	 * Call printHashMap and pass in SymbolTable as an Arg 
	 * @param ctx
	 */
	@Override 
	public void exitDeclare(KnightCodeParser.DeclareContext ctx){
		
		if(exit){
			return;
		}//end of if statement
		
		printHashMap(SymbolTable);
		
		System.out.println("Leaving Declare . . .");
		
	}// end of exitDeclare
	
	/**
	 * A method to enter Variables
	 * call variable method
	 * Assgin a String named identifier to get child(1) text
	 * using var from variable method to get child(0) text 
	 * using var to count memory location of that child
	 * pushing to the symbol table
	 * and keeping count of memory
	 * @param ctx
	 */
	public void enterVariable(KnightCodeParser.VariableContext ctx){
		
		if(exit){
			return;
		}//end of if statement
		
		System.out.println("Enter Variable!");
		
		variable var = new variable();
		
		String identifier = ctx.getChild(1).getText();
		var.variableType = ctx.getChild(0).getText();
		var.memLoc = memoryCounter;
		
		SymbolTable.put(identifier, var);

		memoryCounter++;
	}// end of enterVariable
	
	/**
	 *A method to exit Variable 
	 * @param ctx
	 */
	@Override 
	public void exitVariable(KnightCodeParser.VariableContext ctx){ 
		
		if(exit){
			return;
		}//end of if statement
		
		System.out.println("Leaving Variable . . .");
	}// end of exitVariable
	
	/**
	 * A method to Enter Identifier
	 * @param ctx
	 */
	public void enterIdentifier(KnightCodeParser.IdentifierContext ctx){
		
		if(exit){
			return;
		}//end of if statement
		
		System.out.println("Enter Identifier!");
	}// end of enterIdentifier
	@Override 
	public void exitIdentifier(KnightCodeParser.IdentifierContext ctx){ 
	
		if(exit){
			return;
		}//end of if statement
		
		System.out.println("Leaving Identifier . . .");	
	}// end of exitIdentifier
	
	/**
	 * No use currently for entering and exiting Vartype
	 * @param ctx
	 */
	@Override public void enterVartype(KnightCodeParser.VartypeContext ctx) { }
	@Override public void exitVartype(KnightCodeParser.VartypeContext ctx) { }
	
	/**
	 * A method to enter body
	 * get child count
	 * @param ctx
	 */
	@Override 
	public void enterBody(KnightCodeParser.BodyContext ctx){ 
		
		if(exit){
			return;
		}//end of if statement
		
		System.out.println("Enter Body!");
		
		count = ctx.getChildCount();
		
	}// end of enterBody
	
	/**
	 * A method to exit body
	 * call printHashMap and pass Symbol table as an Arg
	 * visiting lable using ASMBytcode to label printSkipIf
	 * @param ctx
	 */
	@Override 
	public void exitBody(KnightCodeParser.BodyContext ctx){ 
		
		if(exit){
			return;
		}//end of if statement
		
		printHashMap(SymbolTable);
		
		mainVisitor.visitLabel(printSkipIf);
		
		System.out.println("Leaving Body . . .");
	}// end of exitBody
	
	/**
	 * No use currently for entering and exiting Stat
	 * @param ctx
	 */
	@Override public void enterStat(KnightCodeParser.StatContext ctx) { }
	@Override public void exitStat(KnightCodeParser.StatContext ctx) { }
	
	/**
	 * A public int to keep count of operations
	 */
	public int operationCount = 0;
	
	/**
	 *A method to enter Setvar
	 * Using operation to keep count of operations 
	 * genIntStr used as palce to hold strings\
	 * 
	 * If child(1) is not equal to null the key will get child(1) text
	 * 
	 * print out the key
	 * 
	 * check if symbol table contain the key
	 * assgin current variable to the key in the symbol table
	 * 
	 * else print an error and exit
	 * 
	 * check if current variable is a string
	 * check if child(3) count is not 0
	 * 
	 * print error and exit
	 * 
	 * assgin genIntStri to child(3) text and print it out
	 * 
	 * @param ctx
	 */
	@Override 
	public void enterSetvar(KnightCodeParser.SetvarContext ctx){ 
		
		if(exit){
			return;
		}//end of if statement
			
		System.out.println("Enter Setvar");
		operationCount = 0;
		genIntStr = "";
		
		if(ctx.getChild(1) != null){
		
			key = ctx.getChild(1).getText();
			
		}//end of if statement
		
		System.out.println("\n"+key);	
		if(SymbolTable.containsKey(key)){
			currvar = SymbolTable.get(key);
		} else {
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			
			System.out.println("Identifier: " + key + " was not declared");
			exit = true;
			return;
		
		}//end of if else statement
		
		if(isString(currvar)){
		
			if(ctx.getChild(3).getChildCount() != 0){
			
				System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				
				System.out.println("Variable being set to " + key + " is not a string!");
				exit = true;
				return;
			
			}//end of nested if statement
		
			genIntStr = ctx.getChild(3).getText();
			System.out.println(genIntStr);
			
		}//end of if statement
	
	}//end of enterSetvar 
	
	/**
	 * A method to exit Set variable
	 * 
	 */
	@Override 
	public void exitSetvar(KnightCodeParser.SetvarContext ctx){ 
		
		if(exit)
			return;
		
		/**
		 * print out the final value of generated integer string
		 * assgin currvar to the value of the genIntStr
		 * creating a new Integer named store to hold the location memory of the current variable
		 * a vairable to check if the current vairable ot string using isString method
		 */
		System.out.println("Final value of id = " + genIntStr);
		currvar.value = genIntStr;
		
		int store = currvar.memLoc;
		
		genBool = isString(currvar);
			
		/**
		 * An if else statment using genBool as an Arg 
		 * with ASM storing the current variable value ADDRESS in store
		 * 
		 * ELSE
		 * 
		 * Store INTEGER in store 
		 */
		if(genBool){
	
			mainVisitor.visitLdcInsn(currvar.value);
			mainVisitor.visitVarInsn(ASTORE,store);
			
		} else {
		
			mainVisitor.visitVarInsn(ISTORE,store);
			
		}//end of if else statement
		
		/**
		 * assgining current variable value to true
		 * and puting it into the symbol table
		 */
		currvar.valueSet = true;
		SymbolTable.put(key, currvar);
		
    	    
    	    	operation = "";
    	    	genIntStr = "";
    	    	
    	    	/**
    	    	 *  An if statment to check if (if else) is less than 0
    	    	 */
    	    	if(elseCount1 > 0){
		
			System.out.println("elseCount : " + elseCount1);
			
			if(ifCount1 == 1){
				
				Label tempEnd;
				Label temper;
				
				int currentUsage = Character.getNumericValue(decNestStack.charAt(0));
				
				switch(currentUsage){
					case 1: {
						tempEnd = endDecLab0;
						temper = startOfElse0;						
						break;
					}
					case 2: {
						tempEnd = endDecLab1;
						temper = startOfElse1;
						break;
					}
					case 3: {
						tempEnd = endDecLab2;
						temper = startOfElse2;						
						break;
					}
					case 4: {
						tempEnd = endDecLab3;
						temper = startOfElse3;
						break;
					}
					case 5: {
						tempEnd = endDecLab4;
						temper = startOfElse4;
						break;
					}
					case 6: { 
						tempEnd = endDecLab5;
						temper = startOfElse5;
						break;
					}
					case 7: { 
						tempEnd = endDecLab6;
						temper = startOfElse6;
						break;
					}
					case 8: { 
						tempEnd = endDecLab7;
						temper = startOfElse7;
						break;
					}
					case 9: { 
						tempEnd = endDecLab8;
						temper = startOfElse8;
						break;
					}
					case 10: {
						tempEnd = endDecLab9;
						temper = startOfElse9;
						break;
					}
					default: {
					
						System.out.println("\n\n------------------------------------------");
						System.out.println("COMPILER ERROR");
						System.out.println("------------------------------------------");
					
						System.out.println("Case 2: jump label failure for if-else statement in exit decision!");
						
						exit = true;
						return;
					}//end default	
						
				}//end switch statement
				
				System.out.println("-------------------------------------------------------");	
				System.out.println("GOTO, end label= " + tempEnd);
				System.out.println("Visit startOfElse Label= " + temper);
				System.out.println("-------------------------------------------------------");
	
				mainVisitor.visitJumpInsn(GOTO, tempEnd);
				mainVisitor.visitLabel(temper);
			
			}//end if statement
					
		}//end if statement
		
		if(ifCount1 > 0)
			ifCount1--;
		
		System.out.println("ifCount = " + ifCount1);
    	    	
		System.out.println("Exit Setvar");
		
	}//end exitSetvar method
	
}// end of myListener
