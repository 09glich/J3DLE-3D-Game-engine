package engine.ClassHelpers;

import java.util.List;

public class ListHelper {
    
    public static <T> void FillToIndex(List<T> list, int Index) {
        while (list.size() < Index) {
            list.add(null);
        }
    }
}
