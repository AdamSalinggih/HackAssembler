/**
 *CInstructionMapper.java
 *
 * This class assigns all valid instructions
 * into HashMap objects which will be used
 * during the translation of the assembly files
 */

import java.util.HashMap;

public class CInstructionMapper {
	HashMap<String, String> compCode = new HashMap<String,String>();
	HashMap<String, String> destCode = new HashMap<String,String>();
	HashMap<String, String> jumpCode = new HashMap<String,String>();
	
	/**
	 * Constructor for CInstructionMapper class
	 * Assign all valid mnemonics with its associated binary instructions.
	 */
	public CInstructionMapper() {
		//All valid comp instructions
		compCode.put("0", "101010");
		compCode.put("1", "111111");
		compCode.put("-1", "111010");
		compCode.put("D", "001100");
		compCode.put("A", "110000");
		compCode.put("!D", "001101");
		compCode.put("!A", "110001");
		compCode.put("-D", "001111");
		compCode.put("-A", "110011");
		compCode.put("D+1", "011111");
		compCode.put("A+1", "110111");
		compCode.put("D-1", "001110");
		compCode.put("A-1", "110010");
		compCode.put("D+A", "000010");
		compCode.put("A+D", "000010");
		compCode.put("D-A", "010011");
		compCode.put("A-D", "000111");
		compCode.put("D&A", "000000");
		compCode.put("D|A", "010101");
		compCode.put("M", "110000");
		compCode.put("!M", "110001");
		compCode.put("-M", "110011");
		compCode.put("M+1", "110111");
		compCode.put("M-1", "110010");
		compCode.put("D+M", "000010");
		compCode.put("M+D", "000010");
		compCode.put("D-M", "010011");
		compCode.put("M-D", "000111");
		compCode.put("D&M", "000000");
		compCode.put("D|M", "010101");

		//All valid dest instructions
		destCode.put("null", "000");
		destCode.put("M", "001");
		destCode.put("D", "010");
		destCode.put("MD", "011");
		destCode.put("A", "100");
		destCode.put("AM", "101");
		destCode.put("AD", "110");
		destCode.put("AMD", "111");

		//All valid jump instructions
		jumpCode.put("null", "000");
		jumpCode.put("JGT", "001");
		jumpCode.put("JEQ", "010");
		jumpCode.put("JGE", "011");
		jumpCode.put("JLT", "100");
		jumpCode.put("JNE", "101");
		jumpCode.put("JLE", "110");
		jumpCode.put("JMP", "111");
	}
	
	/**
	 * Return the binary code for computation instruction segment
	 * 
	 * @param mnemonic The passed mnemonic
	 * @return Binary code instruction
	 */
	public String comp(String mnemonic) {
		return compCode.get(mnemonic);
	}
	
	/**
	 * Return the binary code for destination instruction segment
	 * 
	 * @param mnemonic The passed mnemonic
	 * @return Binary code instruction
	 */
	public String dest(String mnemonic) {
		return destCode.get(mnemonic);
	}
	
	/**
	 * Return the binary code for jump instruction segment
	 * 
	 * @param mnemonic The passed mnemonic
	 * @return Binary code instruction instruction
	 */
	public String jump(String mnemonic) {
		return jumpCode.get(mnemonic);
	}
}
