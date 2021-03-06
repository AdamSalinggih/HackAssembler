/**
 * @author Adam Salinggih
 * @version 1.0
 * @project Project 7 of nand2tetris.
 * @date	03/16/2020
 *
 * 	Assembler program to convert an assembly file
 * 	into a hack file by translating all instructions
 * 	within the given file into its machine language(binary codes)
 */

import java.io.File;
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
				
				if(!file.getName().substring(file.getName().indexOf(".")).equals(".asm"))
					throw new Exception();
				
				parser = new Parser(file);
				
				while(parser.hasMoreCommands()) {
					parser.advance();
				}

				JOptionPane.showMessageDialog(null, file.getName() + " has been successfully assembled.", "Success!", 1);

				//close all running reader and writer objects
				parser.terminate();

				break;	
			}
			catch(Parser.InvalidAssemblyInstructionException iaie){
				iaie.printStackTrace();
				JOptionPane.showMessageDialog(null, "Unable to translate the assembly file", "Error", 0);
				System.exit(0);
			}
			catch(Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please select an assembly file.", "Invalid File Format",  0);
			}
		}
	}

}
