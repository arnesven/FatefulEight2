package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.OldWand;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ClothesSpriteWithBack;

public class MagicianClass extends CharacterClass {
    protected MagicianClass() {
        super("Magician", "MAG", 7, 5, false, 5,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 1),
                        new WeightedSkill(Skill.Entertain, 5),
                        new WeightedSkill(Skill.Logic, 4),
                        new WeightedSkill(Skill.MagicBlue, 4),
                        new WeightedSkill(Skill.MagicWhite, 3),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Persuade, 4),
                        new WeightedSkill(Skill.Security, 3),
                        new WeightedSkill(Skill.Sneak, 2),
                        new WeightedSkill(Skill.SpellCasting, 1),
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnFancyRobe(characterAppearance, MyColors.DARK_PURPLE, MyColors.DARK_RED);
        putOnTopHat(characterAppearance, MyColors.DARK_GRAY, MyColors.DARK_PURPLE);
    }

    public static void putOnTopHat(CharacterAppearance characterAppearance, MyColors color1, MyColors color2) {
        characterAppearance.removeOuterHair();
        for (int y = 0; y <= 2; ++y) {
            for (int x = 2; x <= 4; ++x) {
                characterAppearance.setSprite(x, y, new ClothesSpriteWithBack(0x62 + 0x10 * y + x, color1, color2  ));
            }
        }
        characterAppearance.setSprite(1, 2, new ClothesSpriteWithBack(0x83, color1, color2  ));
        characterAppearance.setSprite(5, 2, new ClothesSpriteWithBack(0x87, color1, color2  ));
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x47, MyColors.DARK_PURPLE, MyColors.DARK_RED, appearance.getBackHairOnly());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new OldWand());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.PURPLE;
    }

    @Override
    protected int getIconNumber() {
        return 0x0B;
    }

    @Override
    public String getHowToLearn() {
        return "Magicians usually travel the world to show their tricks to people as they go. Look for them on the road.";
    }

    @Override
    public String getDescription() {
        return "Magicians are individuals who mostly use magic as a form of entertainment. However, should " +
                "the need arise, the can cast spells to protect themselves. They are often charismatic and do " +
                "well in social contexts. It is not unheard of that magician also take jobs of the less " +
                "reputable variety.";
    }
}
