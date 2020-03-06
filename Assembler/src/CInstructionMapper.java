import java.util.HashMap;

public class CInstructionMapper {
	HashMap<String, String> compCode = new HashMap<String,String>();
	HashMap<String, String> destCode = new HashMap<String,String>();
	HashMap<String, String> jumpCode = new HashMap<String,String>();
	
	public CInstructionMapper() {
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
		compCode.put("D-M", "010011");
		compCode.put("M-D", "000111");
		compCode.put("D&M", "000000");
		compCode.put("D|M", "010101");




	}


}
