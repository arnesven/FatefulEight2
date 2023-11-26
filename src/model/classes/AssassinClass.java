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
        Looks.putOnMask(characterAppearance, CLOTHING_COLOR);
    }

    @Override
    public void takeClothesOff(CharacterAppearance characterAppearance) {
        characterAppearance.getSprite(2, 2).setColor3(MyColors.BLACK);
        characterAppearance.getSprite(4, 2).setColor3(MyColors.BLACK);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x60, MyColors.GRAY, CharacterAppearance.noHair());
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

    @Override
    public boolean coversEars() {
        return true;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.LIGHT_GRAY;
    }

    @Override
    protected int getIconNumber() {
        return 0x07;
    }

    @Override
    public boolean showFacialHair() {
        return false;
    }

    @Override
    public boolean showHairInBack() {
        return false;
    }

    @Override
    public String getHowToLearn() {
        return "The assassin's guild are practically in every town. Perhaps they would be willing to teach you their craft?";
    }

    @Override
    public String getDescription() {
        return "Assassins are trained killers who use stealth and deception to get to, " +
                "and eliminate their targets. They can climb smooth walls, pick complex locks and " +
                "even cast the odd blue spell. In combat, assassins prefer blades and bows.";
    }
}
