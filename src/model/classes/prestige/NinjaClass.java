package model.classes.prestige;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.classes.normal.*;
import model.items.Equipment;
import model.items.Item;
import model.items.weapons.*;
import model.races.Race;
import util.MyLists;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.List;

public class NinjaClass extends PrestigeClass {

    private static final List<Class<? extends CharacterClass>> FROM_CLASSES = List.of(
            AssassinClass.class,
            BardClass.class,
            BlackKnightClass.class,
            MagicianClass.class,
            SpyClass.class,
            ThiefClass.class,
            WitchClass.class);

    private final MyColors clothingColor;
    private final String descriptionClasses;

    public NinjaClass() {
        super("Ninja", "NJA", 8, 9, false, 10,
                new WeightedSkill[] {
                        new WeightedSkill(Skill.Acrobatics, 6),
                        new WeightedSkill(Skill.Blades, 6),
                        new WeightedSkill(Skill.Endurance, 4),
                        new WeightedSkill(Skill.Perception, 5),
                        new WeightedSkill(Skill.Search, 5),
                        new WeightedSkill(Skill.Security, 5),
                        new WeightedSkill(Skill.SeekInfo, 4),
                        new WeightedSkill(Skill.Sneak, 6),
                        new WeightedSkill(Skill.UnarmedCombat, 4)}
        );
        this.clothingColor = MyColors.DARK_GRAY;
        this.descriptionClasses = MyLists.commaAndJoin(FROM_CLASSES, x ->
                x.getSimpleName().replace("Class", ""));
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, clothingColor);
        Looks.putOnHood(characterAppearance, clothingColor);
        Looks.putOnMask(characterAppearance, clothingColor);
        ThiefClass.putOnThiefsMask(characterAppearance);
    }

    @Override
    public void takeClothesOff(CharacterAppearance characterAppearance) {
        characterAppearance.getSprite(2, 2).setColor3(MyColors.BLACK);
        characterAppearance.getSprite(4, 2).setColor3(MyColors.BLACK);
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
    public boolean showFacialHair() {
        return false;
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0xC0, clothingColor, race.getColor(),
                CharacterAppearance.noHair(), CharacterAppearance.noHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new ThrowingKnives());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Ninjas are individuals who belong to a covert guild of shadow assassins from the east." +
                "This Prestige Class can only be taken on by characters of one of the following classes: " +
                descriptionClasses + ".";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new ThrowingStars(), new ThrowingKnives(), new Wakizashi(), new RitualDagger(), new Katana());
    }

    @Override
    protected boolean canBecomeFrom(CharacterClass charClass) {
        return MyLists.any(FROM_CLASSES, cc -> charClass.getClass().isAssignableFrom(cc));
    }
}
