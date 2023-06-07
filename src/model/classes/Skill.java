package model.classes;

public enum Skill implements Comparable<Skill> {
    Acrobatics("Acrobatics"),       // 12
    Axes("Axes"),                   // 1
    Blades("Blades"),               // 3
    BluntWeapons("Blunt Weapons"),  // 0
    Bows("Bows"),                   // 4
    Endurance("Endurance"),         // 12
    Entertain("Entertain"),         // 8
    Labor("Labor"),                 // 9
    Leadership("Leadership"),       // 4
    Logic("Logic"),                 // 13
    MagicBlack("Magic (Black)"),    // 1
    MagicBlue("Magic (Blue)"),      // 2
    MagicGreen("Magic (Green)"),    // 1
    MagicRed("Magic (Red)"),        // 0
    MagicWhite("Magic (White)"),    // 1
    MagicAny("Magic (Any)"),        // 3
    Perception("Perception"),       // 16
    Persuade("Persuade"),           // 22
    Polearms("Polearms"),           // 1
    Search("Search"),               // 14
    Security("Security"),           // 12
    SeekInfo("Seek Info"),          // 13
    Sneak("Sneak"),                 // 13
    SpellCasting("Spell Casting"),  // 3
    Survival("Survival"),           // 16

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

    public boolean areEqual(Skill other) {
        return getName().equals(other.getName());
    }
}
