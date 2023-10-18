package development;

import model.Model;
import model.characters.GameCharacter;
import model.characters.LonnieLiebgott;
import model.classes.Classes;
import model.items.Item;
import model.items.ItemDeck;
import model.items.Prevalence;
import model.items.weapons.Longsword;
import model.items.weapons.StaffWeapon;
import model.items.weapons.WandWeapon;
import model.items.weapons.Weapon;
import model.races.Race;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Balancing {
    private static final int MAX_RANKS = 8;
    private static final double COST_DELTA = 2.0;
    private static final double COMMON_MULTIPLIER = 0.7;
    private static final double UNCOMMON_MULTIPLIER = 1.0;
    private static final double RARE_MULTIPLIER = 1.5;

    public static void runWeaponValueAnalysis(Model model) {
        Longsword longsword = new Longsword();
        double baselineDamage = calcTotalDamage(longsword);

        System.out.println("BASELINE: " + longsword.getName());
        System.out.println("          Damage: " + baselineDamage);
        double baselineCost = longsword.getCost();
        System.out.println("          Value: " + baselineCost);
        double baselineRatio = baselineDamage / longsword.getCost();
        System.out.println("          Ratio: " + baselineRatio);

        List<Weapon> weapons = new ArrayList<>(ItemDeck.allWeapons());
        Collections.sort(weapons, Comparator.comparing(Item::getName));
        System.out.println("Name                 damage  cost  ratio    suggested");
        for (Weapon w : weapons) {
            double weaponDamage = calcTotalDamage(w);
            double ratio = weaponDamage / w.getCost();
            double suggest =  weaponDamage * baselineCost / baselineDamage;
            if (w.isRangedAttack()) {
                suggest *= 1.5;
            }
            suggest += (w.getSpeedModifier() * 2);
            if (w instanceof WandWeapon || w instanceof StaffWeapon) {
                suggest *= 1.25;
            }
            String tableRow = String.format("%-20s %3.1f  %4d   %2.6f   %2.2f", w.getName(), weaponDamage, w.getCost(), ratio, suggest);
            double diff = suggest - w.getCost();
            if (w.getCost() == 5) {
                System.out.println(tableRow + " (not considered - starting weapon)");
            } else if (suggest < w.getCost() - getUpperTolerance(w)) {
                System.err.println(tableRow + " TO EXPENSIVE? Diff: " + diff);
            } else if (suggest > w.getCost() + getLowerTolerance(w)) {
                System.err.println(tableRow + " TO CHEAP? Diff: " + diff);
            } else {
                System.out.println(tableRow);
            }
        }

    }

    private static double getLowerTolerance(Weapon w) {
        return 2.0;
    }

    private static double getUpperTolerance(Weapon w) {
        if (w.getPrevalence() == Prevalence.rare) {
            return 8.0;
        }
        return 2.0;
    }

    private static double calcTotalDamage(Weapon weapon) {
        double sum = 0;
        for (int i = 0; i < MAX_RANKS; ++i) {
            GameCharacter testDummy = new GameCharacter("Test Dummy", "",
                    Race.NORTHERN_HUMAN, Classes.None,
                    new LonnieLiebgott(), Classes.NO_OTHER_CLASSES);
            testDummy.addTemporaryBonus(weapon.getSkill(), i, true);
            testDummy.getEquipment().setWeapon(weapon);
            sum += testDummy.calcAverageDamage();
        }
        return sum;
    }
}
