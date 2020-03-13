import javax.print.attribute.standard.NumberUp;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class SymbolTable {

    private int varCount = 16;
    private int lineCount = 0;
    private HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();

    public SymbolTable(File file) throws FileNotFoundException {

        Scanner reader = new Scanner(file);

        while(reader.hasNextLine()){

            String line = clean(reader.nextLine());

            if(line.isEmpty())
                continue;
            else if(line.charAt(0) == '(' && line.charAt(line.length() - 1) == ')') {
                line = line.replace("(", "");
                line = line.replace(")", "");

                if(!symbolTable.containsKey(line))
                    addEntry(line, lineCount);
            }
            else {
                System.out.println("Line: " + lineCount + "\tInstruction: " + line);
                lineCount++;
                continue;
            }
        }


        System.out.println(symbolTable);
    }

    /**
     * Add variable or label entries to the symbolTable
     *
     * @param key   Variable names or label names
     * @param address   RAM address or ROM address
     * @return      True for successful entry
     */
    private boolean addEntry(String key, int address){
        symbolTable.put(key, address);

        return true;
    }

    /**
     * Remove comments and empty spaces within a line
     *
     * @param text The string
     * @return Cleared instruction line
     */
    private String clean(String text) {
        if(text.contains("//"))
            text = text.substring(0, text.indexOf("//")).trim();

        return text;
    }
}
