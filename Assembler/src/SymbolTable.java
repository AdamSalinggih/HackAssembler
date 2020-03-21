/**
 * SymbolTable.java
 *
 * This class reads all labels in the assembly file and assign them
 * into a hashmap object. Once all labels are read, it will read the
 * assembly file one more time to read all variables and also assign
 * them into the same hashmap object. Reserved keywords are assigned
 * to the hashmap in the beginning of the construction call.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class SymbolTable {
    private int varCount = 16;
    private int lineCount = 0;
    private HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();

    /**
     * Create SymbolTable object, add designated keywords and read
     * all labels and variables in the assembly file
     *
     * @param file  The file
     * @throws FileNotFoundException If file is not found
     */
    public SymbolTable(File file) throws FileNotFoundException {
        Scanner labelReader = new Scanner(file);

        //Adding all designated keywords to the table
        addEntry("SCREEN", 16384);
        addEntry("KBD", 24576);
        addEntry("SP", 0);
        addEntry("LCL", 1);
        addEntry("ARG", 2);
        addEntry("THIS", 3);
        addEntry("THAT", 4);

        //read all labels
        while(labelReader.hasNextLine()){
            String line = clean(labelReader.nextLine());
            if(line.isEmpty())
                continue;
            else if(line.charAt(0) == '(' && line.charAt(line.length() - 1) == ')') {
                line = line.replace("(", "");
                line = line.replace(")", "");

                if(!symbolTable.containsKey(line))
                    addEntry(line, lineCount);
            }
            else {
                lineCount++;
                continue;
            }
        }

        //read all variables
        Scanner variableReader = new Scanner(file);
        while(variableReader.hasNextLine()) {
            String line = clean(variableReader.nextLine());
            if (line.isEmpty())
                continue;
            else if (line.charAt(0) == '@' && line.charAt(1) == 'R') {
                line = line.substring(2);
                try {
                    Integer.parseInt(line);
                    continue;
                } catch (NumberFormatException nfe) {
                    if (!symbolTable.containsKey("R" + line))
                        symbolTable.put("R" + line, varCount++);
                    continue;
                }
            } else if (line.charAt(0) == '@') {
                line = line.substring(1);
                try {
                    Integer.parseInt(line);
                } catch (NumberFormatException nfe) {
                    if (!symbolTable.containsKey(line))
                        symbolTable.put(line, varCount++);
                }
            }
        }

        labelReader.close();
        variableReader.close();
    }

    /**
     * Return assigned RAM or ROM addresses for the specified
     * variable or label
     *
     * @param text      The variable or label
     * @return          The RAM or ROM address
     */
    public int getAddress(String text){
        return symbolTable.get(text);
    }

    /**
     * Add variable or label entries to the symbolTable
     *
     * @param key       Variable names or label names
     * @param address   RAM address or ROM address
     */
    private void addEntry(String key, int address){
        symbolTable.put(key, address);
    }

    /**
     * Remove comments and empty spaces within a line
     *
     * @param text   The string
     * @return       Cleared instruction line
     */
    private String clean(String text) {
        if(text.contains("//"))
            text = text.substring(0, text.indexOf("//")).trim();

        text = text.replace(" ", "").trim();
        return text;
    }

    /**
     * Return whether the specified variable or label exists
     *
     * @param key   The name of the variable or label
     * @return  True if the specified variable or label exists in the hashmap
     */
    public boolean contains(String key){
        return symbolTable.containsKey(key);
    }
}