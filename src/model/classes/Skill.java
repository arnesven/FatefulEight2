package model.classes;

import model.states.ShopState;

import java.util.List;
import java.util.Map;

public enum Skill implements Comparable<Skill> {
    Acrobatics("Acrobatics", "Acr","acrobatic and athletic activities, like climbing, jumping, swimming, tumbling, running and crawling.\n\nSpecifically this skill is used in combat to avoid opportunity attacks when a character is moving from front formation to back."),       // 12
    Axes("Axes", "Axe","handling all types of axes, both during common work and in combat."),                   // 1
    Blades("Blades", "Bla", "handling and fighting with all types of bladed weapons, from daggers to claymores."),               // 3
    BluntWeapons("Blunt Weapons", "BlW", "handling and fighting with blunt weapons, like clubs, maces, hammers, flails and staffs."),  // 0
    Bows("Bows", "Bow", "archery. This includes proficiency in using crossbows."),                   // 4
    Endurance("Endurance", "End", "resisting all kinds of elements and strain, like cold weather, searing heat, poisons or performing physical activity for long periods of time.\n\nSpecifically this skill is used in combat to stave off fatigue."),         // 12
    Entertain("Entertain", "Ent", "all kinds of entertainment, like singing, acting, playing musical instruments, doing party tricks or making speeches. This skill also covers less formal occasions like just being able to engage well in conversation. This skill is also used for when a character needs to disguise herself. "),         // 8
    Labor("Labor", "Lab", "all kinds of day-to-day work, like chopping wood, mending garments, plowing fields, painting a house or repairing a fence."),                 // 9
    Leadership("Leadership", "Lea", "leading and inspiring other individuals.\n\nSpecifically this skill is used by the leader of the party at quest decision points and during flee attempts in combat."),       // 4
    Logic("Logic", "Log", "representing a persons capacity for reasoning and deduction, and to some degree knowledge. This skill is also used for representing a person's memory."),                 // 13
    MagicBlack("Magic (Black)", "MaK", "knowledge about black magic. Black magic is associated with darkness, death, decay and emptiness."),    // 1
    MagicBlue("Magic (Blue)", "MaB", "knowledge about blue magic. Blue magic is associated with alteration, illusions, thinking and the element of water."),      // 2
    MagicGreen("Magic (Green)", "MaG", "knowledge about green magic. Green magic is associated with nature, change, growth and the element of earth."),    // 1
    MagicRed("Magic (Red)", "MaR", "knowledge about red magic. Red magic is associated with destruction, chaos and the element of fire."),        // 0
    MagicWhite("Magic (White)", "MaW", "knowledge about white magic. White magic is associated with light, healing, warmth and the element of air."),    // 1
    MagicAny("Magic (Any)", "MaA", "knowledge about any magic.\n\nCharacters do not have ranks in this skill. When a game effect refers to this skill it uses the character's best rank from any of Black, Blue, Green, Red or White magic."),        // 3
    Mercantile("Mercantile", "Mer", "appraising goods, bargaining and conducting business."),
    Perception("Perception", "Perc", "representing a character's senses. Most of the time this skill will be used for vision but it may represent a character's hearing, smell or taste as well."),       // 16
    Persuade("Persuade", "Pers", "representing a character's ability to negotiate, sweet-talk, intimidate or cajole others into doing their bidding. This skill is also used when a character is lying."),           // 22
    Polearms("Polearms", "Pol", "handling spears, glaives, halberds, pikes and the like."),           // 1
    Search("Search", "Sea", "represents a character's ability to find things that are hidden. Where the perception skill only represents a character's senses, this skill includes applying method to the searching in order to do it effectively."),               // 14
    Security("Security", "Sec", "knowledge about locks and other security devices, like safes, traps and secret doors. This also includes knowledge about how to disable or bypass security devices."),           // 12
    SeekInfo("Seek Info", "SeI", "represents a character's ability to find information in towns or other urban locations. This may include figuring out who to talk to and what questions to ask, and even how to speak to people to get the most information out of them."),          // 13
    Sneak("Sneak", "Sne", "moving covertly and hiding."),                 // 13
    SpellCasting("Spell Casting", "SpC", "casting spells.\n\nA character's rank in this skill is added to each skill check when that character is attempting to cast a spell."),  // 3
    Survival("Survival", "Sur", "knowledge and ability to be able to survive in the wilderness. This includes many things like navigating rough terrain, taming animals, finding food and being able to cook it and setting up an adequate shelter or avoiding injury while traveling."),           // 16

    UnarmedCombat("Unarmed Combat", "UnC", "for fighting without a weapon.\n\nA character does not have ranks in this skill, but it is used when making an unarmed attack in combat and can receive bonuses in certain situations.");

    private static final Map<String, List<Skill>> ATTRIBUTES =
            Map.of("Wits",       List.of(Skill.Logic, Skill.Perception, Skill.Search, Skill.SpellCasting, Skill.Survival),
                    "Strength",  List.of(Skill.Acrobatics, Skill.Axes, Skill.BluntWeapons, Skill.Endurance, Skill.Labor),
                    "Dexterity", List.of(Skill.Blades, Skill.Bows, Skill.Polearms, Skill.Sneak, Skill.Security),
                    "Charisma",  List.of(Skill.Entertain, Skill.Leadership, Skill.Persuade, Skill.SeekInfo));

    private final String name;
    private final String shortName;
    private final String description;

    Skill(String name, String shortName, String description) {
        this.name = name;
        this.shortName = shortName;
        this.description = description;
    }

    public static boolean isMagicSkill(Skill skill) {
        return skill.getName().startsWith("Magic");
    }

    public static List<Skill> getCharismaSkills() {
        return Skill.ATTRIBUTES.get("Charisma");
    }

    public static List<Skill> getDexteritySkills() {
        return Skill.ATTRIBUTES.get("Dexterity");
    }

    public static List<Skill> getStrengthSkills() {
        return Skill.ATTRIBUTES.get("Strength");
    }

    public static List<Skill> getWitsSkills() {
        return Skill.ATTRIBUTES.get("Wits");
    }

    public static Map<String, List<Skill>> getAttributeSets() {
        return ATTRIBUTES;
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

    public String getShortName() {
        return shortName;
    }

    public String getMiscHelpText() {
        if (this == Mercantile) {
            StringBuilder table = new StringBuilder("    Rank Rate\n");
            for (int rank = 0; rank < 11; rank++) {
                int percentage = (int) (100.0 * (ShopState.getSellRateForMercantile(rank)));
                table.append(String.format("      %2d %2d", rank, percentage)).append("%\n");
            }
            table.append("     etc.\n");

            return "\n\nThe party member with the highest rank of Mercantile will determine the amount of " +
                    "gold you gain when selling items.\n\n" + table.toString();
        }
        return "";
    }
}
