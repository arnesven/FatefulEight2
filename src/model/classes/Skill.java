package model.classes;

public enum Skill implements Comparable<Skill> {
    Acrobatics("Acrobatics"),
    Axes("Axes"),
    Blades("Blades"),
    BluntWeapons("Blunt Weapons"),
    Bows("Bows"),
    Endurance("Endurance"),
    Entertain("Entertain"),
    Labor("Labor"),
    Leadership("Leadership"),
    Logic("Logic"),
    MagicBlack("Magic (Black)"),
    MagicBlue("Magic (Blue)"),
    MagicGreen("Magic (Green)"),
    MagicRed("Magic (Red)"),
    MagicWhite("Magic (White)"),
    MagicAny("Magic (Any)"),
    Perception("Perception"),
    Persuade("Persuade"),
    Polearms("Polearms"),
    Search("Search"),
    Security("Security"),
    SeekInfo("Seek Info"),
    Sneak("Sneak"),
    SpellCasting("Spell Casting"),
    Survival("Survival"),

    UnarmedCombat("Unarmed Combat");

    private static final int[][] RANK_MATRIX = new int[][]{
            new int[]{0, 0, 1, 1, 1, 2}, // Weight 1
            new int[]{1, 1, 1, 2, 2, 3}, // Weight 2
            new int[]{2, 2, 3, 3, 3, 4}, // Weight 3
            new int[]{2, 3, 3, 4, 4, 5}, // Weight 4
            new int[]{2, 3, 4, 5, 5, 6}, // Weight 5
            new int[]{3, 4, 5, 5, 6, 7}  // Weight 6
    };
    private String name;

    Skill(String name) {
        this.name = name;
    }

    public static int getRankForSkill(int weightForSkill, int level) {
        if (weightForSkill == 0) {
            return 0;
        }
        return RANK_MATRIX[weightForSkill-1][level-1];
    }

    public String getName() {
        return name;
    }

    public boolean isMagic() {
        return name.contains("Magic");
    }
}
