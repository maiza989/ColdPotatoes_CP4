
import static utils.Utilities.writeFile;
import lexparse.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.*;
import org.antlr.v4.runtime.ParserRuleContext;

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
}// end of myListener
