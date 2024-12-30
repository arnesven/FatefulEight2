package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.classes.npcs.NPCClass;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class MageNPCClass extends NPCClass {
    protected MageNPCClass() {
        super("Mage", new WeightedSkill[]{
                new WeightedSkill(Skill.MagicRed, 3),
                new WeightedSkill(Skill.MagicBlue, 3),
                new WeightedSkill(Skill.MagicGreen, 3),
                new WeightedSkill(Skill.MagicWhite, 3),
                new WeightedSkill(Skill.MagicBlack, 3),
                new WeightedSkill(Skill.SpellCasting, 3),
        });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnPointyHat(characterAppearance, MyColors.RED, MyColors.DARK_RED, MyColors.DARK_RED);
        Looks.putOnRobe(characterAppearance, MyColors.RED, MyColors.LIGHT_RED);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x40, MyColors.RED, race.getColor(), appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }
}
