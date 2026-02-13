package test;

import model.items.Item;
import model.items.ItemDeck;
import model.items.Prevalence;
import model.items.weapons.Weapon;
import util.MyLists;
import util.MyStrings;

import java.util.*;
import java.util.function.Predicate;

public class WeaponSummary {

    private static HashMap<Weapon, Double> avgDamageMap;
    private static final Scanner scanner = new Scanner(System.in);

    private static List<Weapon> makeWeaponList() {
        List<Weapon> weapons = ItemDeck.allWeapons();
        weapons.addAll(ItemDeck.allUniqueWeapons());

        avgDamageMap = new HashMap<>();
        for (Weapon w : weapons) {
            avgDamageMap.put(w, Balancing.getAverageDamage(w));
        }
        return weapons;
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        scanner.useLocale(Locale.US);
        List<Weapon> weapons = makeWeaponList();

        boolean done = false;
        do {
            System.out.println();
            System.out.println("== WEAPONS (" + weapons.size() + ") ======================================================================================");
            System.out.println("NAME                 DISTANCE TYPE           DAMAGE         AVG DMG SPD COST PREVALENCE SKILL        INFO");
            System.out.println("--------------------|--------|--------------|--------------|-------|---|----|----------|-------------|---------");
            for (Weapon w : weapons) {
                System.out.printf("%-20s %-8s %-14s %-14s %7.2f %+3d %4d %-10s %-13s %s\n", w.getName(),
                        getDistance(w), getWeaponType(w), w.getDamageTableAsString(), getAverageDamage(w),
                        w.getSpeedModifier(), w.getCost(), getPrevalence(w), getSkillName(w), w.getExtraText());
            }
            System.out.println();

            do {
                System.out.print("Sort (S), Filter (F), Reset (R) or Quit (Q): ");
                String input = scanner.nextLine();
                if (!input.isEmpty()) {
                    if (firstCharIs(input, 'S')) {
                        sort(weapons);
                        break;
                    } else if (firstCharIs(input, 'F')) {
                        weapons = filter(weapons);
                        break;
                    } else if (firstCharIs(input, 'R')) {
                        weapons = makeWeaponList();
                        break;
                    } else if (firstCharIs(input, 'Q')) {
                        done = true;
                        break;
                    }
                }
            } while (true);
        } while (!done);

    }

    private static void sort(List<Weapon> weapons) {
        System.out.println("How would you like to sort the weapon list?");
        System.out.println("N - By Name");
        System.out.println("D - Distance");
        System.out.println("T - By Type");
        System.out.println("A - By Average Damage");
        System.out.println("C - By Cost");
        System.out.println("P - By Prevalence");
        System.out.println("S - By Skill");
        System.out.println("X - Not at all");

        System.out.print("? ");
        String input = scanner.nextLine();
        if (!input.isEmpty()) {
            if (firstCharIs(input, 'N')) {
                weapons.sort(Comparator.comparing(Item::getName));
            } else if (firstCharIs(input, 'D')) {
                weapons.sort(Comparator.comparing(WeaponSummary::getDistance));
            } else if (firstCharIs(input, 'T')) {
                weapons.sort(Comparator.comparing(WeaponSummary::getWeaponType));
            } else if (firstCharIs(input, 'A')) {
                weapons.sort(Comparator.comparingDouble(WeaponSummary::getAverageDamage));
            } else if (firstCharIs(input, 'C')) {
                weapons.sort(Comparator.comparingInt(Item::getCost));
            } else if (firstCharIs(input, 'P')) {
                weapons.sort(Comparator.comparing(Item::getPrevalence));
            } else if (firstCharIs(input, 'S')) {
                weapons.sort(Comparator.comparing(Weapon::getSkill));
            }
        }
    }

    private static List<Weapon> filter(List<Weapon> weapons) {
        System.out.println("How would you like to filter the weapon list?");
        System.out.println("N - By Name");
        System.out.println("T - By Type");
        System.out.println("D - By Distance");
        System.out.println("A - By Average Damage");
        System.out.println("C - By Cost");
        System.out.println("P - By Prevalence");
        System.out.println("S - By Skill");

        System.out.print("? ");
        String input = scanner.nextLine();
        if (!input.isEmpty()) {
            char c = input.charAt(0);
            if (firstCharIs(input, 'N')) {
                return MyLists.filter(weapons, stringFilter("Name", Weapon::getName));
            } else if (firstCharIs(input, 'T')) {
                return MyLists.filter(weapons, stringFilter("Type", WeaponSummary::getWeaponType));
            } else if (firstCharIs(input, 'P')) {
                return MyLists.filter(weapons, stringFilter("Prevalence", WeaponSummary::getPrevalence));
            } else if (firstCharIs(input, 'D')) {
                return MyLists.filter(weapons, stringFilter("Distance", WeaponSummary::getDistance));
            } else if (firstCharIs(input, 'S')) {
                return MyLists.filter(weapons, stringFilter("Skill", WeaponSummary::getSkillName));
            } else if (firstCharIs(input, 'C')) {
                return MyLists.filter(weapons, minMaxFilter("cost", WeaponSummary::getWeaponCost));
            } else if (firstCharIs(input, 'A')) {
                return MyLists.filter(weapons, minMaxFilter("damage", WeaponSummary::getAverageDamage));
            }
        }
        return weapons;
    }

    private static String getSkillName(Weapon weapon) {
        return weapon.getSkill().getName();
    }

    private static boolean firstCharIs(String input, char c) {
        return input.charAt(0) == c || input.toUpperCase().charAt(0) == c;
    }

    private static double getAverageDamage(Weapon w) {
        return avgDamageMap.get(w);
    }

    private static String getWeaponType(Weapon w) {
        String type = w.getClass().getSuperclass().getSimpleName().replace("Weapon", "");
        if (type.equals("SmallBladed")) {
            return "Bladed (Small)";
        }
        return type;
    }

    private static String getPrevalence(Item w) {
        if (w.getPrevalence() == Prevalence.veryRare) {
            return "Very Rare";
        }
        return MyStrings.capitalize(w.getPrevalence().toString());
    }

    private static String getDistance(Weapon w) {
        return w.isRangedAttack() ? "Ranged" : "Melee";
    }

    private static Predicate<Weapon> stringFilter(String type, filterFunction<String> func) {
        System.out.println("What kind of filter?");
        System.out.println("1. Exact matching");
        System.out.println("2. Partial matching");
        System.out.println("3. Not matching");

        do {
            try {
                int kind = scanner.nextInt();
                scanner.nextLine();
                if (kind == 1) {
                    System.out.print(type + " exactly matching what? ");
                    String key = scanner.nextLine();
                    return w -> func.get(w).equals(key) || func.get(w).toLowerCase().equals(key);
                } else if (kind == 2) {
                    System.out.print(type + " partially matching what? ");
                    String key = scanner.nextLine();
                    return w -> func.get(w).contains(key) || func.get(w).toLowerCase().contains(key);
                } else if (kind == 3) {
                    System.out.print(type + " not matching what? ");
                    String key = scanner.nextLine();
                    return w -> !func.get(w).equals(key);
                } else {
                    System.out.println("Please enter 1, 2 or 3.");
                }
            } catch (InputMismatchException ime) {
                System.out.println("Bad input!");
            }
        } while (true);

    }

    private static double getWeaponCost(Weapon w) {
        return w.getCost();
    }

    private static Predicate<Weapon> minMaxFilter(String type, filterFunction<Double> func) {
        System.out.print("Min " + type + ": ");
        double min = scanner.nextDouble();
        System.out.print("Max " + type + ": ");
        double max = scanner.nextDouble();
        scanner.nextLine();
        return w -> min <= func.get(w) && func.get(w) <= max;
    }

    private interface filterFunction<E> {
        E get(Weapon w);
    }
}
