package util;

import java.util.ArrayList;
import java.util.List;

public class MyStrings {
    private static final List<String> NUM_WORDS = List.of(
            "zero", "one", "two", "three", "four", "five", "six",
            "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen",
            "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty");

    public static String[] partition(String text, int maxWidth) {
        if (text.length() >= maxWidth) {
            List<String> strs = new ArrayList<>();
            do {
                String[] splits = text.split(" ");
                if (splits.length == 1) {
                    return splits;
                }
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

    public static String[] partitionWithLineBreaks(String text, int maxWidth) {
        List<String> strs = new ArrayList<>();
        String[] parts = text.split("\n");
        for (String s : parts) {
            for (String s2 : partition(s, maxWidth)) {
                strs.add(s2);
            }
        }
        return strs.toArray(new String[0]);
    }

    public static String withPlus(int x) {
        if (x > 0) {
            return "+" + x;
        }
        return "" + x;
    }

    public static String numberWord(int num) {
        return NUM_WORDS.get(num);
    }
}
