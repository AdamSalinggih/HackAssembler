import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
	Scanner scan;
	String compMnemonic;
	String destMnemonic;
	String jumpMnemonc;
	String rawLine;
	String cleanLine;
	String symbol;
	int    lineNumber;
	
	public Parser(File file) throws IOException {
		scan = new Scanner(file);
	}
	
	public void advance() {
		rawLine = scan.nextLine();
		
		if(rawLine.contains("//"))
			rawLine = rawLine.substring(0, rawLine.indexOf("//")).trim();
		
		lineNumber++;
	}
	
	public boolean hasMoreCommands() {
		return scan.hasNextLine();
	}
}
