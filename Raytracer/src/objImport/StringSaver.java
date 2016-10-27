package objImport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Zusatzaufgabe

/**
 * Utility Class for saving and loading Strings
 * 
 * @author Carsten Gericke
 * @author Florian Breslich
 */
public final class StringSaver {
    
    private StringSaver(){}
    
    /**
     * Saves a String ArrayList to a File
     *
     * @param strings The ArrayList that you want to save
     * @param path The File where to save
     */
    public static void save(List<String> strings, File path) {
        
        try {
            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            
            for(String s : strings) {
                bw.write(s);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Loads a String ArrayList from a File
     *
     * @param path The File where to load from
     * @return The ArrayList loaded from the File
     * @throws FileNotFoundException If the File path does not contain a File
     */
    public static List<String> load(File path) throws FileNotFoundException{
        
        List<String> ret = new ArrayList<>();
        
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        
        try {
            for (String s = br.readLine(); s != null; s = br.readLine()) {
                ret.add(s);
            }
            br.close();
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        return ret;
    }
}
