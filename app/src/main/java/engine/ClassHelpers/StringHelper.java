package engine.ClassHelpers;

import java.util.ArrayList;
import java.util.List;

public class StringHelper {
    

    public static List<String> SplitByCharExcludingQuotes(String string, char character) {
        List<String> array = new ArrayList<String>();
        boolean Inqotes = false;

        String Construct = "";
        for (int c = 0; c < string.length(); c++) {
            char cc = string.charAt(c);

            if (cc == '"') {
                Inqotes = !Inqotes;
                continue;
            }

            if (cc == character) {
                if (!Inqotes) {
                    array.add(Construct);
                    Construct = "";
                    continue;
                }
            }

            Construct = Construct + cc;
        }

        array.add(Construct);

        return array;
    }
    public static List<String> SplitBySpaceExcludingQuotes(String string) {return SplitByCharExcludingQuotes(string, ' ');}

    public static List<Integer> getIntListFromString(String string) {
        List<Integer> intList = new ArrayList<Integer>();

        string = string.trim();
        string = string.replaceAll("[()]", "");

        System.out.println(string);

        String[] numbers = string.split(",");

        

        return intList;
    }
}
