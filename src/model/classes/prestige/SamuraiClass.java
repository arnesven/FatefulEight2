package model.classes.prestige;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.classes.normal.*;
import model.classes.Looks;
import model.items.Equipment;
import model.items.Item;
import model.items.accessories.FullHelm;
import model.items.clothing.LeatherArmor;
import model.items.clothing.PlateMailArmor;
import model.items.weapons.Katana;
import model.items.weapons.Wakizashi;
import model.races.Race;
import util.MyLists;
import util.MyTriplet;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ClothesSprite;
import view.sprites.PortraitSprite;
import view.sprites.Sprite;

import java.util.List;

public class SamuraiClass extends PrestigeClass {

    private static final List<Class<? extends CharacterClass>> FROM_CLASSES = List.of(
            BlackKnightClass.class,
            BarbarianClass.class,
            CaptainClass.class,
            MarksmanClass.class,
            MinerClass.class,
            NobleClass.class,
            PaladinClass.class);

    private static final MyColors DEFAULT_ARMOR_COLOR = MyColors.DARK_RED;
    private static final MyColors DEFAULT_SECONDARY_COLOR = MyColors.GRAY_RED;
    private static final MyColors DEFAULT_DETAIL_COLOR = MyColors.RED;
    private final MyColors armorColor;
    private final MyColors secondaryColor;
    private final MyColors helmetDetail;
    private final String descriptionClasses;

    public SamuraiClass(MyColors armorColor, MyColors secondaryColor, MyColors helmetDetail) {
        super("Samurai", "SAM", 10, 7, true, 10, new WeightedSkill[]{
                new WeightedSkill(Skill.Acrobatics, 3),
                new WeightedSkill(Skill.Blades, 5),
                new WeightedSkill(Skill.Bows, 3),
                new WeightedSkill(Skill.Endurance, 4),
                new WeightedSkill(Skill.Leadership, 4),
                new WeightedSkill(Skill.Labor, 3),
                new WeightedSkill(Skill.Logic, 4),
                new WeightedSkill(Skill.Persuade, 3),
                new WeightedSkill(Skill.Polearms, 3),
                new WeightedSkill(Skill.Survival, 4),
                new WeightedSkill(Skill.Sneak, 3),
        });
        this.armorColor = armorColor;
        this.secondaryColor = secondaryColor;
        this.helmetDetail = helmetDetail;
        this.descriptionClasses = MyLists.commaAndJoin(FROM_CLASSES, x ->
                x.getSimpleName().replace("Class", ""));
    }

    public SamuraiClass() {
        this(DEFAULT_ARMOR_COLOR, DEFAULT_SECONDARY_COLOR, DEFAULT_DETAIL_COLOR);
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, secondaryColor);
        Looks.putOnLightArmor(characterAppearance, armorColor, secondaryColor);
        putOnSamuraiHelm(characterAppearance);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, AvatarSprite.LIGHT_ARMOR_BASE, armorColor, race.getColor(), helmetDetail,
                appearance.getBackHairOnly(), appearance.getHalfBackHair(), makeHelmet(appearance));
    }

    private MyTriplet<Sprite, Sprite, Sprite> makeHelmet(CharacterAppearance appearance) {
        return AvatarSprite.makeHat(appearance, "samuraihelmet", 0x0C,
                MyColors.BLACK, armorColor, helmetDetail, MyColors.DARK_GRAY);
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new Wakizashi(), new LeatherArmor(), null);
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    public String getDescription() {
        return "A disciplined fighter, trained for pitched battles and assaults, as well as serving as body guards." +
        "This Prestige Class can only be taken on by characters of one of the following classes: " +
                descriptionClasses + ".";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new Katana(), new PlateMailArmor(), new FullHelm());
    }

    private void putOnSamuraiHelm(CharacterAppearance characterAppearance) {
        characterAppearance.removeOuterHair();
        for (int y = 0; y <= 4; ++y) {
            for (int x = 1; x <= 5; ++x) {
                PortraitSprite spr = new ClothesSprite(0x130 + 0x10 * y + x + 10, armorColor, secondaryColor);
                if (y < 3) {
                    spr.setColor4(helmetDetail);
                } else {
                    spr.setColor4(MyColors.DARK_GRAY);
                }
                if ((y == 3 && 2 <= x && x <= 4) || y > 3) {
                    characterAppearance.addSpriteOnTop(x, y, spr);
                } else {
                    characterAppearance.setSprite(x, y, spr);
                }
            }
        }
    }

    @Override
    protected boolean canBecomeFrom(CharacterClass charClass) {
        return MyLists.any(FROM_CLASSES, cc -> charClass.getClass().isAssignableFrom(cc));
    }
}
