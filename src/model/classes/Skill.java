package model.classes;

public enum Skill implements Comparable<Skill> {
    Acrobatics("Acrobatics", "acrobatic and athletic activities, like climbing, jumping, swimming, tumbling, running and crawling.\n\nSpecifically this skill is used in combat to avoid opportunity attacks when a character is moving from front formation to back."),       // 12
    Axes("Axes", "handling all types of axes, both during common work and in combat."),                   // 1
    Blades("Blades", "handling and fighting with all types of bladed weapons, from daggers to claymores."),               // 3
    BluntWeapons("Blunt Weapons", "handling and fighting with blunt weapons, like clubs, maces, hammers, flails and staffs."),  // 0
    Bows("Bows", "archery. This includes proficiency in using crossbows."),                   // 4
    Endurance("Endurance", "resisting all kinds of elements and strain, like cold weather, searing heat, poisons or performing physical activity for long periods of time.\n\nSpecifically this skill is used in combat to stave off fatigue."),         // 12
    Entertain("Entertain", "all kinds of entertainment, like singing, acting, doing party tricks or making speeches. This skill also covers less formal occasions like just being able to engage well in conversation. This skill is also used for when a character needs to disguise herself. "),         // 8
    Labor("Labor", "all kinds of day-to-day work, like chopping wood, mending garments, plowing fields, painting a house or repairing a fence."),                 // 9
    Leadership("Leadership", "leading and inspiring other individuals.\n\nSpecifically this skill is used by the leader of the party at quest decision points and during flee attempts in combat."),       // 4
    Logic("Logic", "representing a persons capacity for reasoning and deduction, and to some degree knowledge. This skill is also used for representing a person's memory."),                 // 13
    MagicBlack("Magic (Black)", "knowledge about black magic. Black magic is associated with darkness, death, decay and emptiness."),    // 1
    MagicBlue("Magic (Blue)", "knowledge about blue magic. Blue magic is associated with alteration, illusions, thinking and the element of water."),      // 2
    MagicGreen("Magic (Green)", "knowledge about green magic. Green magic is associated with nature, change, growth and the element of earth."),    // 1
    MagicRed("Magic (Red)", "knowledge about red magic. Red magic is associated with destruction, chaos and the element of fire."),        // 0
    MagicWhite("Magic (White)", "knowledge about white magic. White magic is associated with light, healing, warmth and the element of air."),    // 1
    MagicAny("Magic (Any)", "knowledge about any magic.\n\nCharacters do not have ranks in this skill. When a game effect refers to this skill it uses the character's best rank from any of Black, Blue, Green, Red or White magic."),        // 3
    Perception("Perception", "representing a character's senses. Most of the time this skill will be used for vision but it may represent a character's hearing, smell or taste as well."),       // 16
    Persuade("Persuade", "representing a character's ability to negotiate, sweet-talk, intimidate or cajole others into doing their bidding."),           // 22
    Polearms("Polearms", "handling spears, glaives, halberds, pikes and the like."),           // 1
    Search("Search", "represents a character's ability to find things that are hidden. Where the perception skill only represents a character's senses, this skill includes applying method to the searching in order to do it effectively."),               // 14
    Security("Security", "knowledge about locks and other security devices, like safes, traps and secret doors. This also includes knowledge about how to disable or bypass security devices."),           // 12
    SeekInfo("Seek Info", "represents a character's ability to find information in towns or other urban locations. This may include figuring out who to talk to and what questions to ask, and even how to speak to people to get the most information out of them."),          // 13
    Sneak("Sneak", "moving covertly and hiding."),                 // 13
    SpellCasting("Spell Casting", "casting spells.\n\nA character's rank in this skill is added to each skill check when that character is attempting to cast a spell."),  // 3
    Survival("Survival", "knowledge and ability to be able to survive in the wilderness. This includes many things like navigating rough terrain, taming animals, finding food and being able to cook it and setting up an adequate shelter or avoiding injury while traveling."),           // 16

    UnarmedCombat("Unarmed Combat", "for fighting without a weapon.\n\nA character does not have ranks in this skill, but it is used when making an unarmed attack in combat and can receive bonuses in certain situations.");

    private static final int[][] RANK_MATRIX = new int[][]{
            new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3}, // Weight 1
            new int[]{1, 1, 1, 2, 2, 3, 3, 3, 4}, // Weight 2
            new int[]{2, 2, 3, 3, 3, 4, 4, 5, 5}, // Weight 3
            new int[]{2, 3, 3, 4, 4, 5, 5, 5, 6}, // Weight 4
            new int[]{2, 3, 4, 5, 5, 6, 6, 6, 6}, // Weight 5
            new int[]{3, 4, 5, 5, 6, 7, 7, 7, 7}  // Weight 6
            // level  1  2  3  4  5  6  7  8  9
    };
    private String name;
    private String description;

    Skill(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static int getRankForSkill(int weightForSkill, int level) {
        if (weightForSkill == 0 || level == 0) {
            return 0;
        }
        if (level > RANK_MATRIX[0].length) {
            level = RANK_MATRIX[0].length;
        }
        return RANK_MATRIX[weightForSkill-1][level-1];
    }

    public static boolean isMagicSkill(Skill skill) {
        return skill.getName().startsWith("Magic");
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

    public String getDescription() {
        return name + " is the skill for " + description;
    }
}
