package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.items.Item;
import model.items.clothing.PilgrimsCloak;
import model.items.spells.LevitateSpell;
import model.items.weapons.Dirk;
import model.items.Equipment;
import model.items.weapons.ThrowingKnives;
import model.items.weapons.ThrowingStars;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.FaceAndClothesSprite;
import view.sprites.ClothesSprite;
import view.sprites.FaceAndClothesSpriteWithBack;

import java.util.List;

public class AssassinClass extends CharacterClass {
    private final MyColors clothingColor;

    protected AssassinClass(String className, String classShortName, MyColors suitColor) {
        super(className, classShortName, 6, 7, false, 14,
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
        clothingColor = suitColor;
    }

    public AssassinClass() {
        this("Assassin", "ASN", MyColors.GRAY);
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, clothingColor);
        Looks.putOnHood(characterAppearance, clothingColor);
        Looks.putOnMask(characterAppearance, clothingColor);
    }

    @Override
    public void takeClothesOff(CharacterAppearance characterAppearance) {
        characterAppearance.getSprite(2, 2).setColor3(MyColors.BLACK);
        characterAppearance.getSprite(4, 2).setColor3(MyColors.BLACK);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0xC0, clothingColor, CharacterAppearance.noHair());
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

    @Override
    public List<Item> getStartingItems() {
        return List.of(new ThrowingKnives(), new PilgrimsCloak(), new LevitateSpell());
    }
}
