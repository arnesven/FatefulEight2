package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.Skill;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ClothesSprite;
import view.sprites.FaceAndClothesSprite;
import view.sprites.PortraitSprite;

public class PirateClass extends SpecialCharacterClass {

    private final MyColors shirtAndHatColor;

    public PirateClass(MyColors shirtAndHatColor) {
        super("Pirate", "PIR", 8, 6, false, 0, new WeightedSkill[]{
                new WeightedSkill(Skill.Blades, 3),
                new WeightedSkill(Skill.Perception, 3),
                new WeightedSkill(Skill.Endurance, 3),
                new WeightedSkill(Skill.Entertain, 3)
        });
        this.shirtAndHatColor = shirtAndHatColor;
    }

    public PirateClass() {
        this(MyColors.DARK_RED);
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        putOnScarfHat(characterAppearance, shirtAndHatColor);
        Looks.putOnLightArmor(characterAppearance, shirtAndHatColor, characterAppearance.getRace().getColor());
        characterAppearance.setSprite(3, 6, new ClothesSprite(characterAppearance.isFemale() ? 0xAD : 0xAC,
                shirtAndHatColor, characterAppearance.getRace().getColor()));
    }

    private void putOnScarfHat(CharacterAppearance appearance, MyColors fillColor) {
        appearance.removeOuterHair();
        for (int y = 1; y < 3; ++y) {
            for (int x = 2; x < 6; ++x) {
                PortraitSprite spr =  new FaceAndClothesSprite(0x206 + 0x10 * y + x, appearance.getHairColor(), MyColors.DARK_GRAY);
                spr.setColor3(fillColor);
                appearance.setSprite(x, y, spr);
            }
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x3A0, MyColors.WHITE, race.getColor(), shirtAndHatColor,
                appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment();
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
