package test;

import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import util.MyLists;
import util.MyPair;
import util.MyStringFunction;
import util.MyStrings;

import java.util.*;

public class ClassDiffCostTable extends HashMap<CharacterClass, HashMap<CharacterClass, Integer>> {
    private final List<CharacterClass> classes;
    private final Map<CharacterClass, List<MyPair<CharacterClass, Integer>>> diffTable;

    public ClassDiffCostTable() {
        this.classes = new ArrayList<>(Arrays.asList(Classes.allClasses));
        classes.remove(Classes.None);
        this.diffTable = new HashMap<>();
        for (CharacterClass classOne : classes) {
            put(classOne, new HashMap<>());
            List<MyPair<CharacterClass, Integer>> innerList = new ArrayList<>();
            for (CharacterClass classTwo : classes) {
                int cost = classDifference(classOne, classTwo);
                innerList.add(new MyPair<>(classTwo, cost));
                get(classOne).put(classTwo, cost);
            }
            diffTable.put(classOne, innerList);
        }
    }

    private static int classDifference(CharacterClass classOne, CharacterClass classTwo) {
        double skillDiff = 0.0;
        for (Skill s : Skill.values()) {
            skillDiff += Math.pow(classOne.getWeightForSkill(s).getBalancingScore() - classTwo.getWeightForSkill(s).getBalancingScore(), 2);
        }
        double hpDiff = Math.pow(classOne.getHP() * 1.5 - classTwo.getHP() * 1.5, 2);
        double hvyDiff = classOne.canUseHeavyArmor() == classTwo.canUseHeavyArmor() ? 0 : 36;
        double alignDiff = Math.pow(getAlignment(classOne) * 3 - getAlignment(classTwo) * 3, 2);
        return (int)Math.round(Math.sqrt(skillDiff + hpDiff + hvyDiff + alignDiff)) - 5;
    }

    private static int getAlignment(CharacterClass cls) {
        if (!Classes.ALIGNMENT.containsKey(cls.id())) {
            return 0;
        }
        return Classes.ALIGNMENT.get(cls.id());
    }

    public void print() {
        System.out.printf("    ");
        for (CharacterClass classOne : classes) {
            System.out.printf("%4s", classOne.getShortName());
        }
        System.out.println();
        for (CharacterClass classOne : classes) {
            System.out.printf("%4s", classOne.getShortName());
            for (int i = 0; i < classes.size(); ++i) {
                if (classOne == classes.get(i)) {
                    System.out.printf(" n/a");
                } else {
                    System.out.printf("%4d", diffTable.get(classOne).get(i).second);
                }
            }
            System.out.println();
        }

    }

    public void printOptimumCosts() {
        int totalOfTotals = 0;
        for (CharacterClass cls : classes) {
            List<MyPair<CharacterClass, Integer>> costList = new ArrayList<>(diffTable.get(cls));
            costList.sort(Comparator.comparingInt(o -> o.second));
            costList = MyLists.take(costList, 5).subList(1, 5);
            int total = MyLists.intAccumulate(costList, p -> p.second);
            System.out.printf("%3s: %s, total: %d\n", cls.getShortName(), MyLists.commaAndJoin(costList,
                    p -> "(" + p.first.getShortName() + " " + p.second + ")"),
                    total);
            totalOfTotals += total;
        }
        System.out.println("Grand total: " + totalOfTotals/2);
    }
}
