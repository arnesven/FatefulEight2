package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.Hatchet;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ClothesSprite;

public class ArtisanClass extends CharacterClass {
    private static final MyColors APRON_COLOR = MyColors.BROWN;
    private static final MyColors SHIRT_COLOR = MyColors.LIGHT_BLUE;

    protected ArtisanClass() {
        super("Artisan", "ART", 7, 4, false, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Axes, 3),
                        new WeightedSkill(Skill.Blades, 3),
                        new WeightedSkill(Skill.Entertain, 2),
                        new WeightedSkill(Skill.Labor, 5),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Persuade, 2),
                        new WeightedSkill(Skill.Search, 2),
                        new WeightedSkill(Skill.Security, 4),
                        new WeightedSkill(Skill.SeekInfo, 4)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, SHIRT_COLOR);
        putOnApron(characterAppearance);
    }

    private void putOnApron(CharacterAppearance characterAppearance) {
        characterAppearance.setSprite(1, 6, new ClothesSprite(0xC6, SHIRT_COLOR, APRON_COLOR));
        characterAppearance.setSprite(2, 6, new ClothesSprite(0xC7, SHIRT_COLOR, APRON_COLOR));
        characterAppearance.setSprite(3, 6, new ClothesSprite(0xC7, SHIRT_COLOR, APRON_COLOR));
        characterAppearance.setSprite(4, 6, new ClothesSprite(0xC7, SHIRT_COLOR, APRON_COLOR));
        characterAppearance.setSprite(5, 6, new ClothesSprite(0xC8, SHIRT_COLOR, APRON_COLOR));

        characterAppearance.setSprite(1, 5, new ClothesSprite(0xB6, SHIRT_COLOR, APRON_COLOR));
        characterAppearance.setSprite(5, 5, new ClothesSprite(0xB8, SHIRT_COLOR, APRON_COLOR));
    }

    @Override
    public AvatarSprite getAvatar(Race race) {
        return new AvatarSprite(race, 0x67, MyColors.LIGHT_BLUE, MyColors.BROWN);
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Hatchet());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
