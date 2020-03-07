import java.io.File;
import java.util.NoSuchElementException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Assembler {
	public static void main(String[] args) {
		JFileChooser fileChooser = new JFileChooser();
		Parser parser;
		File file;
		
		while(true) {
			try {
				if( fileChooser.showDialog(null, "Open Assembly File") == JFileChooser.CANCEL_OPTION)
					break;
				
				file = fileChooser.getSelectedFile();
				
				if(!file.getName().substring(file.getName().indexOf("."), file.getName().length()).equals(".asm"))
					throw new Exception();
				
				parser = new Parser(file);
				
				while(parser.hasMoreCommands()) {
					parser.advance();
				}
				
				
				break;
			}
			catch(NoSuchElementException ne) {
				JOptionPane.showMessageDialog(null, "All instructions have been succesfully parsed.", "Success!", 1);			
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Please select an assembly file.", "Invalid File Format",  0);
			}
		}
	}
	
	private static String decimalToBinary(int num) {
		
		String binary = Integer.toBinaryString(num);
		String binary16 = "";
		
		for(int i = 0; i < 16 - binary.length(); i++)
			binary16 += "0";
		
		binary16 += binary;
		
		return binary16;
	}

}
