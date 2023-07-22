package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.RedKnightsHelm;
import model.items.accessories.GreatHelm;
import model.items.clothing.RedKnightsArmor;
import model.items.weapons.BastardSword;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ClothesSpriteWithBack;
import view.sprites.FilledBlockSprite;

import static model.classes.BlackKnightClass.putOnKnightsHelm;

public class RedKnightClass extends SpecialCharacterClass {
    protected RedKnightClass() {
        super("Red Knight", "RKN", 8, 5, true, 0, new WeightedSkill[]{
                new WeightedSkill(Skill.Axes, 4),
                new WeightedSkill(Skill.Blades, 5),
                new WeightedSkill(Skill.BluntWeapons, 2),
                new WeightedSkill(Skill.Endurance, 4),
                new WeightedSkill(Skill.Labor, 3),
                new WeightedSkill(Skill.Search, 2),
                new WeightedSkill(Skill.SeekInfo, 3),
                new WeightedSkill(Skill.Survival, 5)
        });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnArmor(characterAppearance, MyColors.DARK_RED, MyColors.RED);
        putOnKnightsHelm(characterAppearance, MyColors.DARK_RED, MyColors.RED);
        for (int y = 3; y <= 4; ++y) {
            for (int x = 2; x <= 4; ++x) {
                characterAppearance.setSprite(x, y, new ClothesSpriteWithBack(0x35 + 0x10 * y + x, MyColors.DARK_RED, MyColors.RED));
            }
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x40, MyColors.DARK_RED, CharacterAppearance.noHair());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new BastardSword(), new RedKnightsArmor(), new RedKnightsHelm());
    }


    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        // remove ears
        appearance.setSprite(1, 3, new FilledBlockSprite(MyColors.BLACK));
        appearance.setSprite(5, 3, new FilledBlockSprite(MyColors.BLACK));
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }


    @Override
    public boolean showFacialHair() {
        return false;
    }

    @Override
    public boolean coversEars() {
        return true;
    }
}
