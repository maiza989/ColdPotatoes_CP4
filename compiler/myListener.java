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
	/**
	 * creating a method to call for setting up an ASM class to write bytecode with
	 */
	public void setupClass() {
		
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
	
	@Override
	public void enterFile(KnightCodeParser.FileContext ctx){

		System.out.println("Enter program rule for first time");
		setupClass();
		
	}// end of enterFile
	
	@Override
	public void exitFile(KnightCodeParser.FileContext ctx){

		System.out.println("Leaving program rule. . .");
		closeClass();

	}// end of exitFile
	
	@Override 
	public void enterDeclare(KnightCodeParser.DeclareContext ctx){
		
		System.out.println("Enter Declare!");

	}// end of enterDeclare
	@Override 
	public void exitDeclare(KnightCodeParser.DeclareContext ctx){
		
		System.out.println("Leaving Declare . . .");
	}// end of exitDeclare

	public void enterVariable(KnightCodeParser.VariableContext ctx){
		
		System.out.println("Enter Variable!");
		
	}// end of enterVariable
	@Override 
	public void exitVariable(KnightCodeParser.VariableContext ctx){ 
	
		System.out.println("Leaving Variable . . .");
	}// end of exitVariable
	
	public void enterIdentifier(KnightCodeParser.IdentifierContext ctx){
		
		System.out.println("Enter Identifier!");
	}// end of enterIdentifier
	
	@Override 
	public void exitIdentifier(KnightCodeParser.IdentifierContext ctx){ 
	
		System.out.println("Leaving Identifier . . .");
		
	}// end of exitIdentifier
	
	@Override 
	public void enterBody(KnightCodeParser.BodyContext ctx){ 
		
		System.out.println("Enter Body!");
	}// end of enterBody
	@Override 
	public void exitBody(KnightCodeParser.BodyContext ctx){ 
		
		System.out.println("Leaving Body . . .");
	}// end of exitBody
	
	
}// end of myListener
