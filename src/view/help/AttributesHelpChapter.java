package view.help;

import model.Model;
import view.GameView;
import view.MyColors;
import view.party.DrawableObject;
import view.widget.TopText;

import java.util.List;

public class AttributesHelpChapter extends ExpandableHelpDialog {
    private static final String TEXT = "Each character has six attributes; Health Points (HP), Stamina Points (SP), " +
            "Physical Armor Points (AP), Magical Armor Points (MP), Experience Points (XP) and Speed.\n\nThese attributes are always displayed by the " +
            "character's name and portrait in the main game view, but also in the Party menu.";
    private static final String ARMOR_TEXT = "A character's Physical and Magical Armor Points (AP/MP) are based on what equipment the character " +
            "currently has equipped. Physical Armor protects a character from physical harm, whereas Magical Armor protects her from magical harm, like spells." +
            "\n\nSome spells and conditions affect a player's current AP or MP.\n\n" +
            "    Physical Armor (No Heavy)\n" +
            "    Physical Armor (Some Heavy)\n" +
            "    Magical Armor\n\n" +
            "A player equipped with items providing heavy armor can become Fatigued in combat.";
    private static final String HP_TEXT = "A character's Health Points (HP) are based on a character's class and receives " +
            "a bonus from that character's race. Each gained level also adds 1 to a character's HP.\n\n" +
            "When a character reaches 0 HP, they die, and unless a friendly party member has a resurrection potion handy, they " +
            "will be removed from the party for good.\n\n" +
            "Some spells and conditions affect a player's current HP.";
    private static final String SP_TEXT = "A character's Stamina Points (SP) represents how rested and concentrated a character is. " +
            "Normally a character's maximum SP is 2, but some game effects can modify this. SP can be expended during skill checks " +
            "to re-roll the die. Resting at taverns and inns replenishes a character's SP.";
    private static final String XP_TEXT = "A character's Experience Points (XP) represents how much that player has learned. " +
            "When a character successfully passes a skill check, defeats an enemy in combat, or casts a spell, that character gains XP.\n\n" +
            "When a character gains enough XP they will level up to the next level. The amount of XP needed for a character to reach " +
            "the next level can be seen in the Party menu.\n\n" +
            "Experience Points can also be gained from completing some quests, and various other game effects.";
    private static final String SPEED_TEXT = "A character's Speed shows how quick that character is and is based on class " +
            "and receives a bonus from that character's race. Speed determines the initiative order in combat, and is the " +
            "attribute upon which a character's chance to evade incoming attacks is based.\n\n" +
            "Many weapons and other items affect a character's speed.";

    public AttributesHelpChapter(GameView view) {
        super(view, "Attributes", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(new ArmorSubChapter(view),
                new AttributesSubChapter(view, "Health Points", HP_TEXT),
                new AttributesSubChapter(view, "Stamina Points", SP_TEXT),
                new AttributesSubChapter(view, "Experience Points", XP_TEXT),
                new AttributesSubChapter(view, "Speed", SPEED_TEXT));
    }

    private static class AttributesSubChapter extends SubChapterHelpDialog {
        public AttributesSubChapter(GameView view, String title, String text) {
            super(view, title, text);
        }
    }

    private static class ArmorSubChapter extends AttributesSubChapter {
        public ArmorSubChapter(GameView view) {
            super(view, "Armor Points", ARMOR_TEXT);
        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            List<DrawableObject> textContent = super.buildDecorations(model, xStart, yStart);
            textContent.add(new TextDecoration("" + (char)(0x8F), xStart + 4, yStart + 17,
                    MyColors.WHITE, MyColors.BLUE, false));
            textContent.add(new TextDecoration("" + (char)(0x8E), xStart + 4, yStart + 18,
                    MyColors.WHITE, MyColors.BLUE, false));
            textContent.add(new TextDecoration("" + (char)(0x8F), xStart + 4, yStart + 19,
                    MyColors.PURPLE, MyColors.BLUE, false));
            return textContent;
        }
    }
}
