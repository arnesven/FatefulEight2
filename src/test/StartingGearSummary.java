package test;

import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Item;
import util.MyLists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class StartingGearSummary {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        List<CharacterClass> classes = new ArrayList<>(Arrays.asList(Classes.allClasses));


        for (CharacterClass cls : classes) {
            System.out.printf("%-6s %s\n", cls.getShortName(), MyLists.commaAndJoin(cls.getStartingItems(), Item::getName));
        }
    }
}
