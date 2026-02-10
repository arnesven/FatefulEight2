package test;

import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import util.MyLists;
import util.MyUnaryIntFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ClassSummary {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
         List<CharacterClass> classes = new ArrayList<>(Arrays.asList(Classes.allClasses));
         classes.remove(Classes.None);

        //List<CharacterClass> classes =
        //List.of(Classes.DRU, Classes.MAG, Classes.PRI, Classes.WIZ, Classes.WIT, Classes.SOR);


        System.out.println("SKILLS / CLASSES");
        System.out.print("                  Abi  Count  Sum ");
        for (CharacterClass cls : classes) {
            System.out.printf("%5s ", cls.getShortName());
        }
        System.out.println();

        for (Skill s : Skill.values()) {
            double sum = calcSkillWeightSum(classes, s);
            int count = calcSkillCount(classes, s);
            //if (sum > 0.0) {
                System.out.format("%-16s %4s %6s %5.1f", s.getName(), getAttributeFor(s), count, sum);
                for (CharacterClass cls : classes) {
                    double w = cls.getWeightForSkill(s).getBalancingScore();
                    if (w > 0.0) {
                        System.out.printf("%5.1f ", w);
                    } else {
                        System.out.print("   -  ");
                    }
                }
                System.out.print("\n");
            //}
        }
    }

    private static int calcSkillCount(List<CharacterClass> classes, Skill s) {
        return MyLists.intAccumulate(classes, characterClass -> characterClass.getSkills().contains(s) ? 1 : 0);
    }

    private static double calcSkillWeightSum(List<CharacterClass> classes, Skill s) {
        return MyLists.doubleAccumulate(classes, cls -> cls.getWeightForSkill(s).getBalancingScore());
    }

    private static Object getAttributeFor(Skill s) {
        if (Skill.getStrengthSkills().contains(s)) {
            return "STR";
        }
        if (Skill.getDexteritySkills().contains(s)) {
            return "DEX";
        }
        if (Skill.getCharismaSkills().contains(s)) {
            return "CHA";
        }
        if (Skill.getWitsSkills().contains(s)) {
            return "WIT";
        }
        return "None";
    }
}
