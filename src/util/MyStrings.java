package util;

import java.util.ArrayList;
import java.util.List;

public class MyStrings {
    public static String[] partition(String text, int maxWidth) {
        if (text.length() >= maxWidth) {
            List<String> strs = new ArrayList<>();
            do {
                String[] splits = text.split(" ");
                StringBuilder bldr = new StringBuilder();
                int i = 0;
                do {
                    bldr.append(splits[i++] + " ");
                } while (bldr.length() + splits[i].length() + 1 < maxWidth);
                strs.add(bldr.toString());
                bldr = new StringBuilder();
                for (; i < splits.length; ++i) {
                    bldr.append(splits[i] + " ");
                }
                text = bldr.toString();
            } while (text.length() >= maxWidth);
            strs.add(text);
            return strs.toArray(new String[0]);
        } else {
            return new String[]{text};
        }
    }

    public static String withPlus(int x) {
        if (x > 0) {
            return "+" + x;
        }
        return "" + x;
    }
}
