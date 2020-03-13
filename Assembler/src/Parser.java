import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {
	protected static Scanner scan;
	protected static FileWriter writer;
	private HashMap<String, String> labelList = new HashMap<String, String>();
	private HashMap<String, String> varList = new HashMap<String, String>();
	private CInstructionMapper mapper = new CInstructionMapper();
	private String compMnemonic;
	private String destMnemonic;
	private String jumpMnemonic;
	private String rawLine;

	
	public Parser(File file) throws IOException {
		scan = new Scanner(file);
		writer = new FileWriter(file.getName().substring(0, file.getName().indexOf(".")) + ".hack");
		SymbolTable table = new SymbolTable(file);
	}
	
	public void advance() throws NumberFormatException, IOException {
		rawLine = scan.nextLine();
		rawLine = cleanText(rawLine);
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
		
		return text;	
	}
	
	public Command getInstructionType(String text) {
		if(text.contains("@"))
			return Command.A_INSTRUCTION;
		else if(text.contains("="))
			return Command.C_INSTRUCTION;
		else if(text.contains("("))
			return Command.L_INSTRUCTION;	
		else 
			return Command.NO_INSTRUCTION;	
	}
	
	/**
	 * Convert an integer to a 16-bit binary
	 * 
	 * @param num
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
		if(text.contains(";"))
			text = text.substring(0, text.indexOf(";"));
		
		if(text.contains("="))
			text = text.substring(0, rawLine.indexOf('='));
		
		return text;		
	}
	
	/**
	 * Read the instruction string and identify the comp mnemonic
	 * 
	 * @param textmThe instruction string
	 * @return The comp mnemonic
	 */
	private String getCompMnemonic(String text) {	
		if(text.contains(";"))
			return text.substring(text.indexOf('=') + 1, text.indexOf(";"));
		else
			return text.substring(text.indexOf('=') + 1);		
	}
	
	/**
	 * Read the instruction string and identify the jump mnemonic
	 * 
	 * @param text The instruction string
	 * @return The jump mnemonic
	 */
	private String getJumpMnemonic(String text) {
		if(text.contains(";"))
			return text.substring(text.indexOf(";"));
		else
			return "null";
	}
	
	/**
	 * Read a label instruction and returns the label's name
	 * 
	 * @param text The instruction string
	 * @return The label's name
	 */
	private String getLabelMnemonic(String text) {
		return text.substring(1, text.length() - 1);
	}
	
	/**
	 * 
	 * @param text The string
	 * @return True if the string is within parentheses
	 */
	private boolean isLabelInstruction(String text) {
		if(text.charAt(0) == '(' && text.charAt(text.length() - 1) == ')')
			return true;
		
		return false;
	}
	
	/**
	 * Identify whether the input string is an address instruction
	 * 
	 * @param text The string
	 * @return True if the string is an address instruction;
	 */
	private boolean isAddressInstruction(String text) {
		if(text.charAt(0) == '@') {
			text = text.substring(1);
			if(text.charAt(0) == 'R')
				text = text.substring(1);
			
			try {
				int val = Integer.parseInt(text);
				
				if(val < 16384)
					return true;
			}
			catch(NumberFormatException nfe) {
				return false;
			}
		}		
		return false;
	}
	
	/**
	 * Identify whether the string is a screen instruction
	 * 
	 * @param text The string
	 * @return True if the string is "@SCREEN" or any integer between 16384 and 24575;
	 */
	private boolean isSreenInstruction(String text) {
		if(text.equals("@SCREEN"))
			return true;
		
		if(text.charAt(0) == '@') {
			text = text.substring(1);			
			try {
				int val = Integer.parseInt(text);		
				if(val > 16383 && val < 24576)
					return true;
			}
			catch(NumberFormatException nfe) {
				return false;
			}
		}		
		return false;
	}
	
	
	/**
	 * Identify whether an instruction is accessing the keyboard
	 * 
	 * @param text The instruction string
	 * @return True if it accesses keyboard addresses
	 */
	private boolean isKeyboardInstruction(String text) {
		if(text.equals("@KBD"))
			return true;
		
		if(text.charAt(0) == '@') {
			text = text.substring(1);			
			try {
				int val = Integer.parseInt(text);		
				if(val > 24575)
					return true;
			}
			catch(NumberFormatException nfe) {
				return false;
			}
		}		
		return false;
	}	
}
