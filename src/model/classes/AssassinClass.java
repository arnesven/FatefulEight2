package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.weapons.Dirk;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.FaceAndClothesSprite;
import view.sprites.ClothesSprite;
import view.sprites.FaceAndClothesSpriteWithBack;

public class AssassinClass extends CharacterClass {
    private static final MyColors CLOTHING_COLOR = MyColors.GRAY;

    protected AssassinClass() {
        super("Assassin", "ASN", 6, 7, false, 14,
                new WeightedSkill[] {
                        new WeightedSkill(Skill.Acrobatics, 5),
                        new WeightedSkill(Skill.Blades, 5),
                        new WeightedSkill(Skill.Bows, 3),
                        new WeightedSkill(Skill.MagicBlue, 2),
                        new WeightedSkill(Skill.Perception, 3),
                        new WeightedSkill(Skill.Search, 4),
                        new WeightedSkill(Skill.Security, 4),
                        new WeightedSkill(Skill.SeekInfo, 4),
                        new WeightedSkill(Skill.Sneak, 5)}
                );
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, CLOTHING_COLOR);
        Looks.putOnHood(characterAppearance, CLOTHING_COLOR);
        putOnMask(characterAppearance);
    }

    private void putOnMask(CharacterAppearance characterAppearance) {
        characterAppearance.getSprite(2, 2).setColor3(CLOTHING_COLOR);
        characterAppearance.getSprite(4, 2).setColor3(CLOTHING_COLOR);
        characterAppearance.setSprite(2, 4, new FaceAndClothesSpriteWithBack(0x31, CLOTHING_COLOR, MyColors.DARK_GRAY));
        characterAppearance.setSprite(3, 4, new ClothesSprite(0x70, CLOTHING_COLOR));
        characterAppearance.setSprite(4, 4, new FaceAndClothesSpriteWithBack(0x41, CLOTHING_COLOR, MyColors.DARK_GRAY));
        characterAppearance.setSprite(3, 5, new FaceAndClothesSprite(0x91, CLOTHING_COLOR));
    }

    @Override
    public void takeClothesOff(CharacterAppearance characterAppearance) {
        characterAppearance.getSprite(2, 2).setColor3(MyColors.BLACK);
        characterAppearance.getSprite(4, 2).setColor3(MyColors.BLACK);
    }

    @Override
    public AvatarSprite getAvatar(Race race) {
        return new AvatarSprite(race, 0x60, MyColors.GRAY);
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Dirk());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        if (appearance.hasBeard()) {
            appearance.getSprite(2, 4).setColor1(appearance.getHairColor());
            appearance.getSprite(4, 4).setColor1(appearance.getHairColor());
        }
    }
}
