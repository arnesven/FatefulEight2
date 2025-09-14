package util;

import model.characters.GameCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyStrings {
    private static final List<String> NUM_WORDS = List.of(
            "zero", "one", "two", "three", "four", "five", "six",
            "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen",
            "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty",
            "twenty-one", "twenty-two", "twenty-three", "twenty-four");
    private static final List<String> NTH_WORDS = List.of(
            "zeroth", "first", "second", "third", "fourth", "fifth", "sixth",
            "seventh", "eighth", "ninth", "tenth", "eleventh", "twelvth", "thirteenth",
            "fourteenth", "fifteenth", "sixteenth", "seventeenth", "eighteenth", "nineteenth", "twentieth",
            "twenty-first", "twenty-second", "twenty-third", "twenty-fourth");

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
                } while (i < splits.length && bldr.length() + splits[i].length() + 1 < maxWidth);
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

    public static String nthWord(int num) {
        return NTH_WORDS.get(num);
    }

    public static String capitalize(String name) {
        String first = name.substring(0, 1).toUpperCase();
        String rest = name.substring(1).toLowerCase();
        return first+rest;
    }

    public static <E> String makeString(List<E> list, MyStringFunction<E> fun) {
        StringBuilder bldr = new StringBuilder();
        for (E e : list) {
            bldr.append(fun.getString(e));
        }
        return bldr.toString();
    }

    public static String aOrAn(String itemName) {
        String vowels = "auieo";
        String lowerCase = itemName.toLowerCase();
        for (int i = 0; i < vowels.length(); ++i) {
            if (lowerCase.startsWith(vowels.charAt(i) + "")) {
                return "an";
            }
        }
        return "a";
    }

    public static String itOrThem(String itemName) {
        if (itemName.endsWith("s")) {
            return "them";
        }
        return "it";
    }

    public static String padRight(String s, char c, int desiredLength) {
        if (s.length() >= desiredLength) {
            return s;
        }
        StringBuilder bldr = new StringBuilder(s);
        bldr.append(String.valueOf(c).repeat(desiredLength - s.length()));
        return bldr.toString();
    }
}
