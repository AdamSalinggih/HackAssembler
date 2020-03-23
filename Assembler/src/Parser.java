import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
	private Scanner scan;
	private FileWriter writer;
	private CInstructionMapper mapper = new CInstructionMapper();
	private String line;
	int		lineCount;
	private SymbolTable table;
	

	
	public Parser(File file) throws IOException {
		scan = new Scanner(file);
		writer = new FileWriter(file.getName().substring(0, file.getName().indexOf(".")) + ".hack");
		table = new SymbolTable(file);
	}
	
	/**
	 * Read the next available line in the assembly file
	 * 
	 * @throws NumberFormatException If the string is not digits
	 * @throws IOException	IOException occurs
	 */
	public void advance() throws NumberFormatException, IOException, InvalidAssemblyInstructionException{
		line = cleanText(scan.nextLine().trim());
		Command commandType = getInstructionType(line);

		if(commandType == Command.INVALID_INSTRUCTION)
				throw new InvalidAssemblyInstructionException();
		else if(commandType == Command.A_INSTRUCTION){
			line = line.substring(1);


			if( table.contains(line) )
				writer.write(decimalToBinary(table.getAddress(line)) + "\n");
			else if(isDigit(line))
				writer.write(decimalToBinary(Integer.parseInt(line)) +"\n");
			else if(line.charAt(0) == 'R' && isDigit(line.substring(1)))
				writer.write(decimalToBinary(Integer.parseInt(line.substring(1))) + "\n");
		}
		//else if(commandType == Command.L_INSTRUCTION){
		//	writer.write(decimalToBinary(table.getAddress(line)));
		//}
		else if(commandType == Command.C_INSTRUCTION){
			String destMnemonic = mapper.dest(getDestMnemonic(line));
			String compMnemonic = mapper.comp(getCompMnemonic(line));
			String jumpMnemonic = mapper.jump(getJumpMnemonic(line));

			checkForInvalidInstruction(getDestMnemonic(line), getCompMnemonic(line), getJumpMnemonic(line));
			int compBit = getCompAddressType(getCompMnemonic(line));

			writer.write("111" + compBit + compMnemonic + destMnemonic +jumpMnemonic + "\n");
		}
		lineCount++;
	}
	
	/**
	 * Identify whether there is more line to read in the file
	 * 
	 * @return True if there is another line to read
	 */
	public boolean hasMoreCommands() {
		return scan.hasNextLine();
	}
	
	/**
	 * Remove comments and empty spaces within a line
	 * 
	 * @param text The string
	 * @return Cleared instruction line 
	 */
	private String cleanText(String text) {	
		if(text.contains("//"))
			text = text.substring(0, text.indexOf("//")).trim();

		text = text.replace(" ", "");
		return text;	
	}
	
	/**
	 * Return the current line's instruction type
	 * 
	 * @param text	The assembly instruction
	 * @return	Command instruction type
	 */
	private Command getInstructionType(String text) {
		if(text.isEmpty())
			return Command.NO_INSTRUCTION;
		else if(text.charAt(0) == '@')
			return Command.A_INSTRUCTION;
		else if(text.contains("=") || text.contains(";"))
			return Command.C_INSTRUCTION;
		else if(text.charAt(0) == '(' && text.charAt(text.length() - 1) == ')')
			return Command.L_INSTRUCTION;		
		else return Command.INVALID_INSTRUCTION;
	}
	
	/**
	 * Convert an integer to a 16-bit binary
	 * 
	 * @param num The integer
	 * @return 16-bit binary representation
	 */
	private String decimalToBinary(int num) {
		
		String binary = Integer.toBinaryString(num);
		String binary16 = "";
		
		for(int i = 0; i < 16 - binary.length(); i++)
			binary16 += "0";
		
		binary16 += binary;		
		return binary16;
	}
	
	/**
	 * Read the instruction string and identify the destination mnemonic
	 * 
	 * @param text The instruction string
	 * @return The destination mnemonic
	 */
	private String getDestMnemonic(String text) {
		if(!text.contains("="))
			return "null";

		if(text.contains(";"))
			text = text.substring(0, text.indexOf(";"));
		if(text.contains("="))
			text = text.substring(0, text.indexOf('='));
		
		return text;		
	}
	
	/**
	 * Read the instruction string and identify the comp mnemonic
	 * 
	 * @param text The instruction string
	 * @return The comp mnemonic
	 */
	private String getCompMnemonic(String text) {
		if(!text.contains("=") && text.contains(";"))
			return text.substring(0, text.indexOf(";"));

		if(text.contains(";"))
			return text.substring(text.indexOf("=") + 1, text.indexOf(";"));
		else {
			return text.substring(text.indexOf("=") + 1);

		}
	}
	
	/**
	 * Read the instruction string and identify the jump mnemonic
	 * 
	 * @param text The instruction string
	 * @return The jump mnemonic
	 */
	private String getJumpMnemonic(String text) {
		if(text.contains(";"))
			return text.substring(text.indexOf(";") + 1);
		else
			return "null";
	}


	/**
	 * Determines whether the String is digits
	 *
	 * @param text The string
	 * @return	True if every char in the string is digit
	 */
	private boolean isDigit(String text){
		try{
			Integer.parseInt(text);
			return true;
		}
		catch(NumberFormatException nfe){
			return false;
		}
	}

	/**
	 * Return the comp mnemonic address type
	 *
	 * @param text The mnemonic
	 * @return 0 for A type/ 1 for M type
	 */
	private int getCompAddressType(String text){
		if(     text.equals("M") ||
				text.equals("!M") ||
				text.equals("-M") ||
				text.equals("M+1") ||
				text.equals("M-1") ||
				text.equals("D+M") ||
				text.equals("M+D") ||
				text.equals("D-M") ||
				text.equals("M-D") ||
				text.equals("D&M") ||
				text.equals("D|M"))
			return 1;

		return 0;
	}

	/**
	 * Terminate running Scanner and FileWriter objects
	 *
	 * @throws IOException If IOException occurs
	 */
	public void terminate() throws IOException {
		scan.close();
		writer.close();
	}

	/**
	 * Checking whether the C-Instruction has any invalid mnemonic
	 *
	 * @param dest	dest mnemonic
	 * @param comp	comp mnemonic
	 * @param jump	jump mnemonic
	 * @throws InvalidAssemblyInstructionException If the mnemonic is invalid
	 */
	private void checkForInvalidInstruction(String dest, String comp, String jump) throws InvalidAssemblyInstructionException {
		if( mapper.dest(dest) == null || mapper.comp(comp) == null || mapper.jump(jump) == null)
			throw new InvalidAssemblyInstructionException();
	}

	/**
	 * A custom exception class to handle invalid assembly instruction
	 *
	 * Should this exception be thrown, it will be caught in the main class
	 * by notifying the user of where the invalid instruction is located in the
	 * assembly file. It will then proceed to terminate current running instance
	 * of the program
	 */
	public class InvalidAssemblyInstructionException extends Exception{
		private InvalidAssemblyInstructionException() {
			JOptionPane.showMessageDialog(null, "Invalid assembly instruction found at line: " + lineCount + "\nString: " + line, "Error", 0);
		}
	}
}
