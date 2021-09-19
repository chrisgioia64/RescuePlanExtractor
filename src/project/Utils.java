package project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    
    public static Pattern pattern = Pattern.compile("(.*)\\.txt$");

    /**
     * For a filename of the form (YYY.txt), extracts the YYY 
     */
    public static String getPrefix(String filename) {
        Matcher m = pattern.matcher(filename);
        if (m.matches()) {
            return m.group(1);
        }
        return null;
    }
    
    public static boolean isDigit(char c) {
        return (c >= '0') && (c <= '9');
    }
    
    public static String formatDollar(Double amount) {
        if (amount == null) {
            return "";
        }
        return String.format("$%,.2f", amount);
    }
    
}
