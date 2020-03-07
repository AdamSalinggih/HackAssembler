import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
	protected static Scanner scan;
	protected static FileWriter writer;
	private CInstructionMapper mapper = new CInstructionMapper();
	private String compMnemonic;
	private String destMnemonic;
	private String jumpMnemonic;
	private String rawLine;
	private String cleanLine;
	private String destAType;
	int    lineNumber;
	
	public Parser(File file) throws IOException {
		scan = new Scanner(file);
		
		writer = new FileWriter(file.getName().substring(0, file.getName().indexOf(".")) + ".hack");
	}
	
	public void advance() throws NumberFormatException, IOException {
		rawLine = scan.nextLine();
		cleanText();
		
		if( getInstructionType(rawLine) == Command.A_INSTRUCTION) {
			
			if( rawLine.charAt(1) == 'R' && Character.isDigit(rawLine.charAt(2))) {
				rawLine.replace("R", "");
				
				writer.write(decimalToBinary(Integer.parseInt( rawLine.substring(1))));
				System.out.println("IF Executed");
			}
			else {
				rawLine = rawLine.replace("@", "");
				
				writer.write( decimalToBinary(Integer.parseInt(rawLine)) + "\n");				
			}	
		}
		
		if( getInstructionType(rawLine) == Command.C_INSTRUCTION) {
			destMnemonic = rawLine.substring(0, rawLine.indexOf('='));
			destMnemonic = mapper.dest(destMnemonic);
			
			compMnemonic = rawLine.substring(rawLine.indexOf('=') + 1);
			compMnemonic = mapper.comp(compMnemonic);
			
			destAType = getDestAType(rawLine);

			writer.write("111" + destAType + compMnemonic + destMnemonic + "000\n");

		}

	}
	
	public boolean hasMoreCommands() {
		return scan.hasNextLine();
	}
	
	private String cleanText() {
		
		if(rawLine.contains("//"))
			rawLine = rawLine.substring(0, rawLine.indexOf("//")).trim();
		
	
		return rawLine;	
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
	
	private String decimalToBinary(int num) {
		
		String binary = Integer.toBinaryString(num);
		String binary16 = "";
		
		for(int i = 0; i < 16 - binary.length(); i++)
			binary16 += "0";
		
		binary16 += binary;
		
		return binary16;
	}
	
	private String getDestAType(String rawLine) {
		if( rawLine.equals("M")   |
			rawLine.equals("!M")  |
			rawLine.equals("-M")  |
			rawLine.equals("M+1") |
			rawLine.equals("D+M") |
			rawLine.equals("D-M") |
			rawLine.equals("M-D") |
			rawLine.equals("D&M") |
			rawLine.equals("D|M") ) {
			return "1";
		}
		else
			return "0";		
	}
}
