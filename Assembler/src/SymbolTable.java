import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SymbolTable {
    private ArrayList<String> variableNames = new ArrayList<>();
    private int varCount = 16;
    private int lineCount = 0;
    private HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();

    public SymbolTable(File file) throws FileNotFoundException {
        Scanner labelReader = new Scanner(file);
        addEntry("SCREEN", 16384);
        addEntry("KBD", 24576);
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
     * Return assigned RAM or ROM addresses for specified the
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
     * @return          True for successful entry
     */
    private boolean addEntry(String key, int address){
        symbolTable.put(key, address);

        return true;
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

        return text;
    }

    /**
     * Return whether the address is a variable or label
     *
     * @param key   The address
     * @return  True if the address is either a variable or label
     */
    public boolean contains(String key){
        return symbolTable.containsKey(key);
    }
}