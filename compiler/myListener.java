package compiler;
/**
 * A class that gathers a hashmap symbol table to store data the listener needs to use along with ANTLR classes to 
 * build a compiler with the use of lexers and parser ANTLR generates.  
 * @author Maitham Alghamgham, Bobby Gabriel 
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
	
	//prints the symbol table using the hashmap
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
	
	/**
	 * A method to check if a variable is a string
	 * @param var
	 * @return
	 */
	public boolean isString(variable var){
		
		if(var.variableType.equals(STR))
			return true;
		return false;
		
	}//end isString
	
	/**
	 * Constructor for myListener class 
	 * @param programName
	 * @param debug
	 */
	public myListener(String programName, boolean debug){
	       
		this.programName = programName;
		this.debug = debug;

	}//end myListener constructor
	
	public myListener(String programName){
	       
		this.programName = programName;
		debug = false;

	}//end myListener constructor
	
	
	//all usable labels
	public Label printSkipIf = new Label();
	
	//using ASM and bytecode to create a class
	public void setupClass() {
				
		if(exit) {
			return;
					
		}//end if
				
		//Seting up the classwriter
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC,"output/"+this.programName, null, "java/lang/Object",null);
			
		
		//Using local MethodVisitor to create the constructor for the object
		MethodVisitor mv=cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0); //load the first local variable: this
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V",false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(1,1);
		mv.visitEnd();
		       	
		
		//Using global MethodVisitor to write bytecode according to entries in the parsetree	
		mainVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,  "main", "([Ljava/lang/String;)V", null, null);
		mainVisitor.visitCode();
		
	}//end setupClass
	
	//close the created class
	public void closeClass() {
			
		if(exit) {
			return
		}//end if 
				
				
		//Use global MethodVisitor to finish writing the bytecode and write the binary file.
		mainVisitor.visitInsn(Opcodes.RETURN);
		mainVisitor.visitMaxs(3, 3);
		mainVisitor.visitEnd();
		cw.visitEnd();

		byte[] b = cw.toByteArray();
		
		Utilities.writeFile(b,"output/"+this.programName+".class");    
		System.out.println("Done!");
		       	
	}// end closeClass
	
	/**
	 * Under here to contain will the enteries and exits from the KnightCodeParser
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
		
	}//end enterFile
	
	/**
	 * a method to exit files
	 * @param ctx
	 */
	@Override
	public void exitFile(KnightCodeParser.FileContext ctx){
		
		if(exit) {
			return
		}// end if
		
		System.out.println("Leaving program rule. . .");
		closeClass();

	}//end exitFile
	
	/**
	 * A method to enter Declare 
	 * get child count
	 * @param ctx
	 */
	@Override 
	public void enterDeclare(KnightCodeParser.DeclareContext ctx){
		
		if(exit) {
			return
		}//end if
		
		System.out.println("Enter Declare!");

		count = ctx.getChildCount();
		
	}//end enterDeclare
	
	/**
	 * a method to exit Declare
	 * Call printHashMap and pass in SymbolTable as an Arg 
	 * @param ctx
	 */
	@Override 
	public void exitDeclare(KnightCodeParser.DeclareContext ctx){
		
		if(exit){
			return;
		}//end if 
		
		printHashMap(SymbolTable);
		
		System.out.println("Leaving Declare . . .");
		
	}//end exitDeclare
	
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
		}//end if 
		
		System.out.println("Enter Variable!");
		variable var = new variable();
		
		String identifier = ctx.getChild(1).getText();
		var.variableType = ctx.getChild(0).getText();
		var.memLoc = memoryCounter;
		
		SymbolTable.put(identifier, var);

		memoryCounter++;
	}//end enterVariable
	
	/**
	 *A method to exit Variable 
	 * @param ctx
	 */
	@Override 
	public void exitVariable(KnightCodeParser.VariableContext ctx){ 
		
		if(exit){
			return;
		}//end if 
		
		System.out.println("Leaving Variable . . .");
	}//end exitVariable
	
	/**
	 * A method to Enter Identifier
	 * @param ctx
	 */
	public void enterIdentifier(KnightCodeParser.IdentifierContext ctx){
		
		if(exit){
			return;
		}//end if 
		
		System.out.println("Enter Identifier!");
	}//end enterIdentifier
	
	@Override 
	public void exitIdentifier(KnightCodeParser.IdentifierContext ctx){ 
	
		if(exit){
			return;
		}//end if 
		
		System.out.println("Leaving Identifier . . .");	
	}//end exitIdentifier
	
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
		}//end if 
		
		System.out.println("Enter Body!");
		count = ctx.getChildCount();
		
	}//end enterBody
	
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
		}//end if 
		
		printHashMap(SymbolTable);
		mainVisitor.visitLabel(printSkipIf);
		System.out.println("Leaving Body . . .");
		
	}//end exitBody
	
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
		}//end if 
			
		System.out.println("Enter Setvar");
		operationCount = 0;
		genIntStr = "";
		
		if(ctx.getChild(1) != null){
			key = ctx.getChild(1).getText();
			
		}//end if 
		
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
		
		}//end if/else 
		
		if(isString(currvar)){
		
			if(ctx.getChild(3).getChildCount() != 0){
			
				System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				
				System.out.println("Variable being set to " + key + " is not a string!");
				exit = true;
				return;
			
			}//end if 
		
			genIntStr = ctx.getChild(3).getText();
			System.out.println(genIntStr);
			
		}//end if 
	
	}//end enterSetvar 
	
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
			
		}//end if/else statement
		
		/**
		 * assgining current variable value to true
		 * and puting it into the symbol table
		 */
		currvar.valueSet = true;
		SymbolTable.put(key, currvar);
		
    		operation = "";
    	    genIntStr = "";
    	    	
    	    if(elseCount1 > 0){
    	    	System.out.println("elseCount : " + elseCount1);
			
			/**
			 * an if statement to check count value and if its equal to 1 in goes to switch case
			 * 
			 */
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
						
				}//end switch 
				
				System.out.println("-------------------------------------------------------");	
				System.out.println("GOTO, end label= " + tempEnd);
				System.out.println("Visit startOfElse Label= " + temper);
				System.out.println("-------------------------------------------------------");
	
				mainVisitor.visitJumpInsn(GOTO, tempEnd);
				mainVisitor.visitLabel(temper);
			
			}//end if 
					
		}//end if
		
		if(ifCount1 > 0) {
			ifCount1--;
		}//end if
		
		System.out.println("ifCount = " + ifCount1);
		System.out.println("Exit Setvar");
		
	}//end exitSetvar 
	
	/**
	 * A method to enter Numbers
	 * assgining enterAndExitNumber to ctx and get the text
	 * 
	 */
	@Override 
	public void enterNumber(KnightCodeParser.NumberContext ctx){ 
	
		if(exit){
			return;
		}//end if 
		
		System.out.println("Enter Number");
		enterAndExitNumber = ctx.getText();
		genIntStr += enterAndExitNumber;
					
	}//end enterNumber 
	
	/**
	 * A method to exit Numbers
	 * assgin num to be an integer of the value of enterAndExitNumber
	 * Using ASM with store num 
	 * @param ctx
	 */
	@Override 
	public void exitNumber(KnightCodeParser.NumberContext ctx){ 
	
		if(exit){
			return;
		}//end if 
	
		num = Integer.valueOf(enterAndExitNumber);	
		mainVisitor.visitIntInsn(SIPUSH, num);
		System.out.println("Exit Number");
		
	}//end exitNumber 
	
	/**
	 * A method to enter Id
	 * @param ctx
	 */
	@Override 
	public void enterId(KnightCodeParser.IdContext ctx){ 
		if(exit)
			return;
		
		System.out.println("enter ID");
		
		/**
		 * assgining keyID to ctx and get text
		 */
		keyID = ctx.getText();
		
		/**
		 * if the symbol table contain keyID
		 * assgin var1 to the symboltable and get the keyID
		 * op1 will be the keyID
		 */
		if(SymbolTable.containsKey(keyID)){
			var1 = SymbolTable.get(keyID);
			op1 = keyID;
	
			operator1 = var1.memLoc;
				
			/**
			 * print error if this happens
			 */
			if(var1.variableType.equalsIgnoreCase(STR) && operationCount > 0){
				System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				
				System.out.println("Cannot perform arithmetic operations on a String");

				exit = true;
				return;
			
			}//end if
			
			/**
			 * if var1 is string load address using ASM ESLE load Integer operatior1 ELSE print error
			 * 
			 */
			if(isString(var1)){
				mainVisitor.visitIntInsn(ALOAD, operator1);
			} else {
				mainVisitor.visitIntInsn(ILOAD, operator1);
			}//end if/else
			
		}//end if 
		else {
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			
			System.out.println("ID: " + keyID + " does not exist!");
			exit = true;
			return;
		
		}//end if/else
		
		genIntStr += op1;
		
	}//end enterId
	
	/**
	 * A method to exit Id
	 * @param ctx
	 */
	@Override 
	public void exitId(KnightCodeParser.IdContext ctx){ 
		if(exit)
			return;
	
		System.out.println("Exit ID");

		genIntStr += arithmeticOperation.charAt(0);
		if(arithmeticOperation.length() != 0) {
			arithmeticOperation = arithmeticOperation.substring(1);
		}//end if
		
		if(printTwice){
			genIntStr += arithmeticOperation.charAt(0);
			if(arithmeticOperation.length() != 0)
				arithmeticOperation = arithmeticOperation.substring(1);
			printTwice = false;	
		}//end if
		
	}//end exitId
	
	/**
	 * A mathod to enter Parenthiesises
	 */
	@Override 
	public void enterParenthesis(KnightCodeParser.ParenthesisContext ctx){ 
		if(exit)
			return;
		System.out.println("Enter parenthesis");
		
		/**
		 * assgining the parenthiesies
		 */
		genIntStr += "(";
		arithmeticOperation = ")" + arithmeticOperation;
		
	
	}//end enterParenthesis
	
	/**
	 * a method to exit Parenthiesises
	 * @param ctx
	 */
	@Override 
	public void exitParenthesis(KnightCodeParser.ParenthesisContext ctx){ 
		if(exit)
			return;
		//skipCount = 1;
		
		genIntStr += arithmeticOperation.charAt(0);
		if(arithmeticOperation.length() != 0)
			arithmeticOperation = arithmeticOperation.substring(1);
		
		System.out.println("Exit parenthesis");
	}//end exitParenthesis
	
	
	/**
	 *  Addion/subtraction/multiplication/Division are all the say other than change the operand 
	 */
	
	/**
	 * a method to enter Addition
	 */
	@Override 
	public void enterAddition(KnightCodeParser.AdditionContext ctx){ 
		if(exit)
			return;
		
		System.out.println("Enter addition");
		/**
		 * increaing operationCounter
		 * and assgining + 
		 */
		operationCount++;
		
		arithmeticOperation = "+" + arithmeticOperation;
	}//end enterAddition
	
	/**
	 * A method to exit addition
	 * @param ctx
	 */
	@Override 
	public void exitAddition(KnightCodeParser.AdditionContext ctx){ 
		if(exit)
			return;

		/**
		* decreasing operationCounter
		*/
		operationCount--;	

		/**
		 * USing ASM preform addtion for integer
		 */
		mainVisitor.visitInsn(IADD);
                      	
		System.out.println("Exit addition");
	}//end exitAddition
	
	/**
	 * A method to enter Mulitiplaction 
	 */
	@Override 
	public void enterMultiplication(KnightCodeParser.MultiplicationContext ctx){ 
		if(exit)
			return;
	
		System.out.println("Enter multiplication");
		operationCount++;
		arithmeticOperation = "*" + arithmeticOperation;
	
	}//end enterMultiplication

	@Override 
	public void exitMultiplication(KnightCodeParser.MultiplicationContext ctx){ 
		if(exit)
			return;
			
		operationCount--;	
		
		/**
		 * Using ASM preform multiplication for integers
		 */
		mainVisitor.visitInsn(IMUL);
            		
		System.out.println("Exit multiplication");
	
	}//end exitMultiplication
	
	/**
	 * A method to enter Division
	 */
	@Override 
	public void enterDivision(KnightCodeParser.DivisionContext ctx){ 
		if(exit)
			return;
	
		System.out.println("Enter division");
		operationCount++;
		arithmeticOperation = "/"+arithmeticOperation;
	
	}//end enterDivision

	@Override 
	public void exitDivision(KnightCodeParser.DivisionContext ctx){ 
		if(exit)
			return;
			
		operationCount--;	
		
		/**
		 * Using ASM preform Division for integer
		 */
		mainVisitor.visitInsn(IDIV);
            		
		System.out.println("Exit division");
	
	}//end exitDivision
	
	/**
	 * A method to enter Subtraction
	 * @param ctx
	 */
	@Override 
	public void enterSubtraction(KnightCodeParser.SubtractionContext ctx){ 
		if(exit)
			return;
			
		System.out.println("Enter subtraction");
		operationCount++;
		arithmeticOperation = "-"+ arithmeticOperation;
	
	}//end enterSubtraction
	
	/**
	 * A method to exitSubtraction
	 * @param ctx
	 */
	@Override 
	public void exitSubtraction(KnightCodeParser.SubtractionContext ctx){ 
		if(exit)
			return;
			
		operationCount--;
			
		/**
		 * Using ASM preform Subtraction for integers
		 */
		mainVisitor.visitInsn(ISUB);
            	           	
		System.out.println("Exit subtraction");

	}//end exitSubtraction
	
	/**
	 * A method to enter Comparison 
	 * @param ctx
	 */
	@Override 
	public void enterComparison(KnightCodeParser.ComparisonContext ctx){ 
		if(exit)
			return;
		System.out.println("Enter Comparison");
		
		/**
		 *An if statement to check if the child count is not zero
		 */
		if(ctx.getChildCount() != 0){
			compString = ctx.getChild(1).getChild(0).getText();	
			operation = compString + operation;
			/**
			 * if statement to check the comparsion 
			 */
			if(compString.equals("<>"))
				printTwice = true;
			//System.out.println(compString);
		}//end if	
	
	}//end enterComparison

	/**
	 * A method to exit Comparison
	 * @param ctx
	 */
	@Override 
	public void exitComparison(KnightCodeParser.ComparisonContext ctx){ 
		if(exit)
			return;		 
		
		Label label1 = new Label();
        Label label2 = new Label();
        		
        /**
        * An if else nested statement to  do the comparison using ASM
        */
        if(compString.equals(">")){

			mainVisitor.visitJumpInsn(IF_ICMPLE, label1);
			arithmeticOperation = ">"+arithmeticOperation;
				
		} else if(compString.equals("<")){
				
			mainVisitor.visitJumpInsn(IF_ICMPGE, label1);
			arithmeticOperation = "<"+arithmeticOperation;
			
		} else if(compString.equals("<>")){
			
			mainVisitor.visitJumpInsn(IF_ICMPEQ, label1);
			arithmeticOperation = "<>"+arithmeticOperation;
			
		} else if(compString.equals("=")){
			
			mainVisitor.visitJumpInsn(IF_ICMPNE, label1);
			arithmeticOperation = "="+arithmeticOperation;
			
		}
			
        		
        /**
        * ASM to do the jumps condition 
        */
		mainVisitor.visitInsn(ICONST_1);
		mainVisitor.visitJumpInsn(GOTO, label2);
		mainVisitor.visitLabel(label1);
		mainVisitor.visitInsn(ICONST_0);
		mainVisitor.visitLabel(label2);
		    	
		System.out.println("Exit Comparison");

	}//end exitComparison
	
	@Override 
	public void enterComp(KnightCodeParser.CompContext ctx){ 
		if(exit)
			return;
			
		System.out.println("Enter Comp");	
			
	}//end enterComp

	@Override 
	public void exitComp(KnightCodeParser.CompContext ctx){ 
		if(exit)
			return;
					
		System.out.println("Exit Comp");
	}//end exitComp
	
	Label endDecLab0 = new Label();
	Label endDecLab1 = new Label();
	Label endDecLab2 = new Label();
	Label endDecLab3 = new Label();
	Label endDecLab4 = new Label();
	Label endDecLab5 = new Label();
	Label endDecLab6 = new Label();
	Label endDecLab7 = new Label();
	Label endDecLab8 = new Label();
	Label endDecLab9 = new Label();
	
	Label startOfElse0 = new Label();
	Label startOfElse1 = new Label();
	Label startOfElse2 = new Label();
	Label startOfElse3 = new Label();
	Label startOfElse4 = new Label();
	Label startOfElse5 = new Label();
	Label startOfElse6 = new Label();
	Label startOfElse7 = new Label();
	Label startOfElse8 = new Label();
	Label startOfElse9 = new Label();
	
	
	public static int ifCount1 = 0;
	public static int elseCount1 = 0;
	public static int decLabCount = 0;
	public int decCount2 = 0;

	public String decNestStack = "000";	
	public String decElseStack = "000";	
	public String decIfStack = "000";
	
	public stacker decIfStacker = new stacker();
	public stacker decElseStacker = new stacker();

	public boolean firstNestedDec = false;
	
	
	/**
	 * A method to enter Decision
	 * 
	 * It checks the number of descisons. 
	 * Then checks for a syntax error.
	 * Count the number of If statments and else statements > this is needed so the program will know where to put labels
	 * @param ctx
	 */
	@Override 
	public void enterDecision(KnightCodeParser.DecisionContext ctx){ 
		if(exit)
			return;

		System.out.println("Enter Decision");
		if(decLabCount > 9){
		
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			
			System.out.println("Too many If-Else statements, compiler can only handle 10 or less!");
			exit = true;
			return;

		} else {
			decLabCount++;	
			decCount2++;		
		}
		decNestStack = decLabCount + decNestStack;

		decCount = ctx.getChildCount();

		if(decCount < 7){
		
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			
			System.out.println("Syntax is wrong for If-Else statement!");
			
			exit = true;
			return;

		}//end if
		
		
		if(!ctx.getChild(0).getText().equalsIgnoreCase("IF")){
		
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			
			System.out.println("Syntax is wrong for If-Else statement!");
			
			exit = true;
			return;
		
		
		}//end if
		
		if(!ctx.getChild(4).getText().equalsIgnoreCase("THEN")){
		
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			
			System.out.println("Syntax is wrong for If-Else statement! If statement must be followed by then");
			
			exit = true;
			return;
		
		
		}//end if
		
		if(ctx.getChild(decCount-2).getText().equalsIgnoreCase("ELSE")||ctx.getChild(5).getText().equalsIgnoreCase("ELSE")){
		
				System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				System.out.println("Syntax is wrong for If-Else statement at ELSE");
			
				exit = true;
				return;	
		}//end if
		
		if(!ctx.getChild(decCount-1).getText().equalsIgnoreCase("ENDIF")){
		
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			

			System.out.println("Syntax is wrong for If-Else statement at ENDIF");
			exit = true;
			return;
		}//end if

		decOp1 = ctx.getChild(1).getText();
		if(SymbolTable.containsKey(decOp1)){
			var1 = SymbolTable.get(decOp1);
			
			if(!var1.valueSet||var1.variableType.equalsIgnoreCase(STR)){
				
				System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
			
				System.out.println("Comparison failed!");
				System.out.println("ID: " + decOp1 + " value has not been set, or ID is a String!");
				
				exit = true;
				return;
        			
			}//end if
			
		operator1 = var1.memLoc;
		mainVisitor.visitIntInsn(ILOAD, operator1);
		} else {
			
			try{            			
				decOperator1 = Integer.valueOf(decOp1);
				mainVisitor.visitIntInsn(SIPUSH, decOperator1);
            			            		            	
       		     }//end try
			
				catch(NumberFormatException e){
        			System.out.println("\n\n------------------------------------------");
        			System.out.println("COMPILER ERROR");
        			System.out.println("------------------------------------------");
        			System.out.println("Comparison failed!");
        			System.out.println("ID: " + decOp1 + " does not exist, is not assigned a value or is not a valid integer.");
				
        			exit = true;
        			return;
        			
        		     }//end catch
		
		}//end if/else
		
		decOp2 = ctx.getChild(3).getText();
		if(SymbolTable.containsKey(decOp2)){
			var2 = SymbolTable.get(decOp2);

			if(!var2.valueSet || var2.variableType.equalsIgnoreCase(STR)){
				
				System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				System.out.println("Comparison failed!");
				System.out.println("ID: " + decOp1 + " value has not been set, or ID is a String!");
				exit = true;
				return;	
				
			}//end if
			
			operator2 = var2.memLoc;
			mainVisitor.visitIntInsn(ILOAD, operator2);
		} else {
		
			  try{
				decOperator2 = Integer.valueOf(decOp2);
				mainVisitor.visitIntInsn(SIPUSH, decOperator2);	
       		  }//end try
			  
			  catch(NumberFormatException e){
        		System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				System.out.println("Comparison failed!");
				System.out.println("ID: " + decOp2 + " does not exist, is not assigned a value or is not a valid integer.");
				
				exit = true;
				return;
        			
			  }//end catch	
		}//end if/else		
			
		decCompSymbol = ctx.getChild(2).getChild(0).getText();

		String temporaryCounterString;
		
		int temporaryIfCounter = 0;
		int temporaryElseCounter = 0;
		int tempI = 5;
		int elseNodeNum = -1;
		boolean elseFound = false;
		
		while(tempI < decCount && !elseFound){
	
			if(ctx.getChild(tempI).getText().equalsIgnoreCase("ELSE")){
				prev = "ELSE";

				elseNodeNum = tempI;
				elseFound = true;
			} 
			else {
			
				if(!ctx.getChild(tempI).getText().equalsIgnoreCase("ENDIF")){
					temporaryIfCounter++;
					
					if(ctx.getChild(tempI).getText().substring(0,5).equalsIgnoreCase("WHILE")){
						int t = ctx.getChild(tempI).getChild(0).getChildCount();
						int f = ctx.getChild(tempI).getChild(0).getChildCount()-6;
						String tempoStringer = "";							
						int i = 5;
							
						while(i<t-1){								
							tempoStringer = ctx.getChild(tempI).getChild(0).getChild(i).getChild(0).getChild(0).getText();
							
							if(tempoStringer.length()>=5){	
								
								if(ctx.getChild(tempI).getChild(0).getChild(i).getChild(0).getChild(0).getText().substring(0,5).equalsIgnoreCase("WHILE")){
									int cll = ctx.getChild(tempI).getChild(0).getChild(i).getChild(0).getChildCount();
									int j = 5;
									
									while(j<cll-1){
										String tempnextstringer = ctx.getChild(tempI).getChild(0).getChild(i).getChild(0).getChild(0).getText();
										
										if(tempnextstringer.length()>=5){
											
											if(ctx.getChild(tempI).getChild(0).getChild(i).getChild(0).getChild(j).getChild(0).getText().substring(0,5).equalsIgnoreCase("WHILE")){

												System.out.println("\n\n------------------------------------------");
												System.out.println("COMPILER ERROR");
												System.out.println("------------------------------------------");
												System.out.println("While-loop overflow");
												System.out.println("Compiler cannot handle more 3 nested while-loops inside an if-bracket");
												System.out.println("A label error would occur.");
				
												exit = true;
												return;
									
											}//end if	
										}//end if
										
										j++;
									
									}//end while
								
									f+= cll-6;
									
								}//end if
							 }//end if
							
							i++;
								
						}//end while
							
						temporaryIfCounter += f;
					
					}//end if
				}//end if
			}//end if/else
				
			tempI++;
		}//end while	
		

		if(elseFound){

			while(tempI < decCount){
			
				if(!ctx.getChild(tempI).getText().equalsIgnoreCase("ENDIF")){
					temporaryElseCounter++;
									
				}//end if
			
				tempI++;
			
			}//end while
		
		}//end if	

		int brukOgKast = pop(decIfStacker);
		
		brukOgKast += temporaryElseCounter;
		push(decIfStacker, brukOgKast);
		push(decIfStacker, temporaryIfCounter);
		push(decElseStacker, temporaryElseCounter);

		int tempElse = elseCount1;
		elseCount1 += temporaryElseCounter;
		ifCount1 +=temporaryIfCounter;	
				
		Label temp; 
		Label tempEnd;
		
			
		int currentUsage = Character.getNumericValue(decNestStack.charAt(0));
		
		switch(currentUsage){
			case 1: {
				temp = startOfElse0;
				tempEnd = endDecLab0;
				break;
			}
			case 2: {
				temp = startOfElse1;
				tempEnd = endDecLab1;
				break;
			}
			case 3: {
				temp = startOfElse2;
				tempEnd = endDecLab2;
				break;
			}
			case 4: {
				temp = startOfElse3;
				tempEnd = endDecLab3;
				break;
			}
			case 5: {
				temp = startOfElse4;
				tempEnd = endDecLab4;
				break;
			}
			case 6: {
				temp = startOfElse5;
				tempEnd = endDecLab5;
				break;
			}
			case 7: { 
				temp = startOfElse6;
				tempEnd = endDecLab6;
				break;
				
			}
			case 8: {
				temp = startOfElse7;
				tempEnd = endDecLab7;
				break;
				
			}
			case 9: { 
				temp = startOfElse8;
				tempEnd = endDecLab8;
				break;
				
			}
			case 10: { 
				temp = startOfElse9;
				tempEnd = endDecLab9;
				break;
				
			}
			default: {
				System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				System.out.println("jump label failure for if-else statement at enter!");
					
				exit = true;
				return;
			}		
		}//end switch
			

		String tempStringDecBla = "ifComp... ";
		if(elseCount1 > tempElse){
		} 
		else {
			temp = tempEnd;		
		}//end if/else	
		
		if(decCompSymbol.equals(">")){
			mainVisitor.visitJumpInsn(IF_ICMPLE, temp);
		} 
		else if(decCompSymbol.equals("<")){
			mainVisitor.visitJumpInsn(IF_ICMPGE,temp);
		} 
		else if(decCompSymbol.equals("<>")){
			mainVisitor.visitJumpInsn(IF_ICMPEQ, temp);
		} 
		else if(decCompSymbol.equals("=")){
			mainVisitor.visitJumpInsn(IF_ICMPNE, temp);			
		}//end if/else
			
	

	}//end enterDecision
	@Override 
	public void exitDecision(KnightCodeParser.DecisionContext ctx){ 
		if(exit)
			return;
		
			Label temper; 	
			Label temp;
			int currentUsage = Character.getNumericValue(decNestStack.charAt(0));
			
			switch(currentUsage) {
		
				case 1: {
					temp = endDecLab0;
					temper = startOfElse0;	
					break;
				}
				case 2: {
					temp = endDecLab1;
					temper = startOfElse1;	
					break;
				}
				case 3: {
					temp = endDecLab2;
					temper = startOfElse1;	
					break;
				}
				case 4: {
					temp = endDecLab3;
					temper = startOfElse1;	
					break;
				}
				case 5: {
					temp = endDecLab4;
					temper = startOfElse1;	
					break;
				}
				case 6: {
					temper = startOfElse5;
					temp = endDecLab5;
					break;
				}
				case 7: { 
					temper = startOfElse6;
					temp = endDecLab6;
					break;
				
				}
				case 8: {
					temper = startOfElse7;
					temp = endDecLab7;
					break;
				
				}
				case 9: { 
					temper = startOfElse8;
					temp = endDecLab8;
					break;
				
				}
				case 10: { 
					temper = startOfElse9;
					temp = endDecLab9;
					break;
				
				}

				default: {
					temper = startOfElse1;	
			
					System.out.println("\n\n------------------------------------------");
					System.out.println("COMPILER ERROR");
					System.out.println("------------------------------------------");
				
					System.out.println("Case 1: jump label failure for if-else statement at exit");
					
					exit = true;
					return;
				}	
					
			}//end switch
			
			decCount2--;

		
		//ASM
		mainVisitor.visitLabel(temp);
		
		if(decNestStack.length() != 0)
			decNestStack = decNestStack.substring(1);
		
		int tempBoolElse = peek(decElseStacker);
		int newUsage;
		if(tempBoolElse > 0){
			newUsage = peek(decIfStacker);
		if(newUsage == 1){
			Label tempEnd;
			currentUsage = Character.getNumericValue(decNestStack.charAt(0));
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
					System.out.println("jump label failure for if-else statement in print!");
					exit = true;
					return;
				}	
							
			}//end switch
					
		
			mainVisitor.visitJumpInsn(GOTO, tempEnd);
			mainVisitor.visitLabel(temper);
						
			pop(decElseStacker);
			pop(decIfStacker);
				

		} else if(newUsage > 1){
			newUsage = pop(decIfStacker);
			newUsage--;
			push(decIfStacker,newUsage);	
		}
		
		} else {
			pop(decIfStacker);
			pop(decElseStacker);
			newUsage = pop(decIfStacker);
			newUsage--;
			push(decIfStacker,newUsage);
		}

		System.out.println("Exit Decision");


	}//end exitDecision
	
	/**
	 * A method to enter Print
	 * This method handle which data to print. whether  its an Identifier or a string
	 * It also handle laoding memeory location of  identifiers from symbol table
	 * It also print string from constent pool
	 * @param ctx
	 */
	@Override
	public void enterPrint(KnightCodeParser.PrintContext ctx){
		if(exit)
			return;

		System.out.println("Enter print");

		key2 = ctx.getChild(1).getText();

		//ASM bytecode	
		mainVisitor.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System", "out", "Ljava/io/PrintStream;");
		
		if(SymbolTable.containsKey(key2)){
			genPrint = false;
			extravar = SymbolTable.get(key2);
			
			outputInt = extravar.memLoc;
			if(isString(extravar)){
				printString = true;
			} else { 	
				printString = false;
			}//end if/else
			
		} else {
			genPrint = true;
			outputString = key2; 
		}//end if/else
		
	
	}//end enterPrint
	
	/**
	 * A method to enter Read
	 * This method handle creating and initializing a scanner instance 
	 * @param ctx
	 */
	
	@Override 
	public void exitPrint(KnightCodeParser.PrintContext ctx){ 
		if(exit)
			return;

		if(genPrint){
			mainVisitor.visitLdcInsn(outputString);
			mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",  "println", "(Ljava/lang/String;)V", false);
			
		} 
		else {
			if(printString){
				mainVisitor.visitVarInsn(ALOAD, outputInt);
				mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",  "println", "(Ljava/lang/String;)V", false);
				
			} else {
				mainVisitor.visitVarInsn(Opcodes.ILOAD, outputInt);
				mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
			}//end if/else
		}//end if/else
		
		int tempBoolElse = peek(decElseStacker);

		if(tempBoolElse > 0){
			int newUsage = peek(decIfStacker);
				if(newUsage == 1){
					Label temper;
					Label tempEnd;
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
						
							System.out.println("jump label failure for if-else statement in print!");
							
							exit = true;
							return;
						}	
							
					}//end switch
				
					mainVisitor.visitJumpInsn(GOTO, tempEnd);
					mainVisitor.visitLabel(temper);
					
					pop(decElseStacker);
					pop(decIfStacker);
				
				} else if(newUsage > 1){

					newUsage = pop(decIfStacker);
					newUsage--;
					push(decIfStacker,newUsage);
					
				}//end if/else
			
			
		}//end if

		System.out.println("Exit print");
	}//end exitPrint
	
	public boolean alreadyRead = false;
	public int readStoredLocation;
	
	@Override 
	public void enterRead(KnightCodeParser.ReadContext ctx){ 
		if(exit)
			return;
		System.out.println("Enter read\n");
		
		
		if(ctx.getChild(1) != null)
			key = ctx.getChild(1).getText();
			

		if(SymbolTable.containsKey(key)){
			currvar = SymbolTable.get(key);
		} else {
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			
			System.out.println("Identifier: " + key + " was not declared");
			exit = true;
			return;
		
		}//end if/else
		
		if(!alreadyRead){
			alreadyRead = true;
			readStoredLocation = memoryCounter;
			mainVisitor.visitTypeInsn(NEW, "java/util/Scanner");
            		mainVisitor.visitInsn(DUP);
            		mainVisitor.visitFieldInsn(GETSTATIC,"java/lang/System", "in", "Ljava/io/InputStream;");
            		mainVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V" , false);
            		mainVisitor.visitVarInsn(ASTORE,readStoredLocation);
		
			memoryCounter++;
			
		}//end if
		
	}//end enterRead
	
	@Override 
	public void exitRead(KnightCodeParser.ReadContext ctx){ 
		if(exit)
			return;

		mainVisitor.visitVarInsn(ALOAD,readStoredLocation);
		genBool = isString(currvar);
			
		if(genBool){
			mainVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);
			mainVisitor.visitVarInsn(ASTORE,currvar.memLoc);
			
		} 
		else {
			mainVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextInt", "()I", false);
			mainVisitor.visitVarInsn(ISTORE,currvar.memLoc);
			mainVisitor.visitVarInsn(ALOAD,readStoredLocation);
			mainVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);
			mainVisitor.visitInsn(POP);
			
		}//end if/else
		
		currvar.valueSet = true;
		SymbolTable.put(key, currvar);
		
		int tempBoolElse = peek(decElseStacker);
		if(tempBoolElse > 0){
			int newUsage = peek(decIfStacker);

				if(newUsage == 1){
					Label temper;
					Label tempEnd;
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
						
							System.out.println("jump label failure for if-else statement in print!");
							
							exit = true;
							return;
						}	
							
					}//end switch
			
					mainVisitor.visitJumpInsn(GOTO, tempEnd);
					mainVisitor.visitLabel(temper);
					
					pop(decElseStacker);
					pop(decIfStacker);
					
				} else if(newUsage > 1){
					newUsage = pop(decIfStacker);
					newUsage--;
					push(decIfStacker,newUsage);
				}//end if/else
			
			
		}//end if
		
		System.out.println("Exit read\n");
		
	}//end exitRead


	public int loopLabCount = 0;
	public int loopCount = 0;
	public String loopOp1;
	public int loopOperator1;
	public String loopOp2;
	public int loopOperator2;
	public String loopCompSymbol;
	public String loopNestStack = "000";	
	
	Label endOfloop0 = new Label();
	Label endOfloop1 = new Label();
	Label endOfloop2 = new Label();
	Label endOfloop3 = new Label();
	Label endOfloop4 = new Label();
	Label endOfloop5 = new Label();
	Label endOfloop6 = new Label();
	Label endOfloop7 = new Label();
	Label endOfloop8 = new Label();
	Label endOfloop9 = new Label();
	
	Label startOfloop0 = new Label();
	Label startOfloop1 = new Label();
	Label startOfloop2 = new Label();
	Label startOfloop3 = new Label();
	Label startOfloop4 = new Label();
	Label startOfloop5 = new Label();
	Label startOfloop6 = new Label();
	Label startOfloop7 = new Label();
	Label startOfloop8 = new Label();
	Label startOfloop9 = new Label();
	
	@Override 
	public void enterLoop(KnightCodeParser.LoopContext ctx){ 
		if(exit)
			return;
		System.out.println("Enter loop");
		
		if(loopLabCount > 9){
		
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			
			System.out.println("Too many While-loops, compiler can only handle 10 or less!");
			exit = true;
			return;

		} 
		else {
			loopLabCount++;	
			loopCount++;		
		}//end if/else
		
		loopNestStack = loopLabCount + loopNestStack;

		int syntaxTest = ctx.getChildCount();
		if(syntaxTest < 7){
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			
			System.out.println("Syntax is wrong for loop statement!");
			
			exit = true;
			return;
		
		}//end if
		
		
		if(!ctx.getChild(0).getText().equalsIgnoreCase("WHILE")){
		
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			System.out.println("Syntax is wrong for while loop");
			
			exit = true;
			return;
		
		}//end if
		
		if(!ctx.getChild(4).getText().equalsIgnoreCase("DO")){
		
			System.out.println("\n\n------------------------------------------");
			System.out.println("COMPILER ERROR");
			System.out.println("------------------------------------------");
			System.out.println("Syntax is wrong for while-loop! after comparison must come \"DO\"");
			
			exit = true;
			return;
		
		
		}//end if
		if(!ctx.getChild(syntaxTest-1).getText().equalsIgnoreCase("ENDWHILE")){
		
				System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				System.out.println("Syntax is wrong for while-loop, must end with \"ENDWHILE\"");
			
				exit = true;
				return;	
		}//end if
		
		
		Label temp; 
		Label tempEnd;
		
			int currentUsage = Character.getNumericValue(loopNestStack.charAt(0));
			switch(currentUsage){
				case 1: {
					temp = startOfloop0;
					tempEnd = endOfloop0;
					break;
				}
				case 2: {
					temp = startOfloop1;
					tempEnd = endOfloop1;
					break;
				}
				case 3: {
					temp = startOfloop2;
					tempEnd = endOfloop2;
					break;
				}
				case 4: {
					temp = startOfloop3;
					tempEnd = endOfloop3;
					break;
				}
				case 5: {
					temp = startOfloop4;
					tempEnd = endOfloop4;
					break;
				}
				case 6: {
					temp = startOfloop5;
					tempEnd = endOfloop5;
					break;
				}
				case 7: { 
					temp = startOfloop6;
					tempEnd = endOfloop6;
					break;
				
				}
				case 8: {
					temp = startOfloop7;
					tempEnd = endOfloop7;
					break;
				
				}
				case 9: { 
					temp = startOfloop8;
					tempEnd = endOfloop8;
					break;
				
				}
				case 10: { 
					temp = startOfloop9;
					tempEnd = endOfloop9;
					break;
				
				}
				default: {
				
					System.out.println("\n\n------------------------------------------");
					System.out.println("COMPILER ERROR");
					System.out.println("------------------------------------------");
				
					System.out.println("jump label failure for loop at enter!");
					
					exit = true;
					return;
				}		
			}//end switch
		
		/**
		 * Using ASM to visit a label
		 */
		mainVisitor.visitLabel(temp);
		loopOp1 = ctx.getChild(1).getText();
	
		if(SymbolTable.containsKey(loopOp1)){
			var1 = SymbolTable.get(loopOp1);
			
			if(!var1.valueSet||var1.variableType.equalsIgnoreCase(STR)){
				System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				System.out.println("Comparison failed!");
				System.out.println("ID: " + loopOp1 + " value has not been set, or ID is a String!");
				
				exit = true;
				return;
        		
			}//end if
			
			operator1 = var1.memLoc;
			mainVisitor.visitIntInsn(ILOAD, operator1);
			
		} else {
			
			try{
            	loopOperator1 = Integer.valueOf(loopOp1);
            	mainVisitor.visitIntInsn(SIPUSH, loopOperator1);
            			
            			
       		}//end try
			
			catch(NumberFormatException e){
        		System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				System.out.println("Comparison failed!");
				System.out.println("ID: " + loopOp1 + " does not exist, is not assigned a value or is not a valid integer.");
				
				exit = true;
				return;
        			
			}//end catch
		
		}//end if/else
		
		loopOp2 = ctx.getChild(3).getText();
		
		if(SymbolTable.containsKey(loopOp2)){
			var2 = SymbolTable.get(loopOp2);

			if(!var2.valueSet || var2.variableType.equalsIgnoreCase(STR)){
				System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				System.out.println("Comparison failed!");
				System.out.println("ID: " + loopOp1 + " value has not been set, or ID is a String!");
				
				exit = true;
				return;	
			}//end if
			
			operator2 = var2.memLoc;
			mainVisitor.visitIntInsn(ILOAD, operator2);
			
		} else {
		
			  try{
            	loopOperator2 = Integer.valueOf(loopOp2);
            	mainVisitor.visitIntInsn(SIPUSH, loopOperator2);	
       		  }//end try
			  
			  catch(NumberFormatException e){
        		System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				System.out.println("Comparison failed!");
				System.out.println("ID: " + loopOp2 + " does not exist, is not assigned a value or is not a valid integer.");
				
				exit = true;
				return;
        			
			  }//end catch
		}//end if/else		
			
		loopCompSymbol = ctx.getChild(2).getChild(0).getText();
	
		if(loopCompSymbol.equals(">")){
			mainVisitor.visitJumpInsn(IF_ICMPLE, tempEnd);

			} else if(decCompSymbol.equals("<")){	
				mainVisitor.visitJumpInsn(IF_ICMPGE,tempEnd);
			
			} else if(decCompSymbol.equals("<>")){
				mainVisitor.visitJumpInsn(IF_ICMPEQ, tempEnd);
			
			} else if(decCompSymbol.equals("=")){
				mainVisitor.visitJumpInsn(IF_ICMPNE, tempEnd);
			
			}//end if/else

			syntaxTest = ctx.getChildCount() - 6;		
		
	}//end enterLoop
	
	@Override 
	public void exitLoop(KnightCodeParser.LoopContext ctx){ 
		if(exit)
			return;
			
		Label temp;
		Label temper;
		int currentUsage = Character.getNumericValue(loopNestStack.charAt(0));
			
		switch(currentUsage) {
		
			case 1: {
				temp = endOfloop0;
				temper = startOfloop0;
				break;
			}
			case 2: {
				temp = endOfloop1;
				temper = startOfloop1;
				break;
			}
			case 3: {
				temp = endOfloop2;
				temper = startOfloop2;
				break;
			}
			case 4: {
				temp = endOfloop3;
				temper = startOfloop3;
				break;
			}
			case 5: {
				temp = endOfloop4;
				temper = startOfloop4;
				break;
			}
			case 6: {
				temp = endOfloop5;
				temper = startOfloop5;
				break;
			}
			case 7: { 
				temp = endOfloop6;
				temper = startOfloop6;
				break;
				
			}
			case 8: {
				temp = endOfloop7;
				temper = startOfloop7;
				break;
				
			}
			case 9: {
				temp = endOfloop8;
				temper = startOfloop8;
				break;
				
			}
			case 10: { 
				temp = endOfloop9;
				temper = startOfloop9;
				break;
				
			}
			default: {
				System.out.println("\n\n------------------------------------------");
				System.out.println("COMPILER ERROR");
				System.out.println("------------------------------------------");
				System.out.println("jump label failure for if-else statement at exit");
					
				exit = true;
				return;
			}	
					
		}//end switch
			
		/**
		 * Using ASM to do jump conditions
		 */
		mainVisitor.visitJumpInsn(GOTO,temper);
		mainVisitor.visitLabel(temp);
		
		if(loopNestStack.length() != 0)
			loopNestStack = loopNestStack.substring(1);
		
		int tempBoolElse = peek(decElseStacker);

		if(tempBoolElse > 0){
			int newUsage = peek(decIfStacker);
		
		if(newUsage == 1){
			Label tempEnd;
			currentUsage = Character.getNumericValue(decNestStack.charAt(0));
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
					System.out.println("jump label failure for if-else statement in print!");
					
					exit = true;
					return;
				}	
							
			}//end switch
					
					/**
					 * Using ASM to do jump conditions 
					 */
					mainVisitor.visitJumpInsn(GOTO, tempEnd);
					mainVisitor.visitLabel(temper);
					
					pop(decElseStacker);
					pop(decIfStacker);

				} else if(newUsage > 1){

					newUsage = pop(decIfStacker);
					newUsage--;
					push(decIfStacker,newUsage);
					
				}//end if/else
						
		}//end if
	
		System.out.println("Exit loop");

	}// end of exitLoop
}// end of myListener