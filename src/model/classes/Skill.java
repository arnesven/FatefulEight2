package model.classes;

public enum Skill implements Comparable<Skill> {
    Acrobatics("Acrobatics"),       // 20
    Axes("Axes"),                   // 8
    Blades("Blades"),               // 15
    BluntWeapons("Blunt Weapons"),  // 14
    Bows("Bows"),                   // 10
    Endurance("Endurance"),         // 20
    Entertain("Entertain"),         // 18
    Labor("Labor"),                 // 16
    Leadership("Leadership"),       // 18
    Logic("Logic"),                 // 17
    MagicBlack("Magic (Black)"),    // 9
    MagicBlue("Magic (Blue)"),      // 8
    MagicGreen("Magic (Green)"),    // 10
    MagicRed("Magic (Red)"),        // 7
    MagicWhite("Magic (White)"),    // 9
    MagicAny("Magic (Any)"),        // 6
    Perception("Perception"),       // 27
    Persuade("Persuade"),           // 26
    Polearms("Polearms"),           // 8
    Search("Search"),               // 20
    Security("Security"),           // 16
    SeekInfo("Seek Info"),          // 20
    Sneak("Sneak"),                 // 25
    SpellCasting("Spell Casting"),  // 10
    Survival("Survival"),           // 31

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
