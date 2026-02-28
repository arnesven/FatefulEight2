package model.classes.prestige;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.classes.normal.*;
import model.items.Equipment;
import model.items.Item;
import model.items.accessories.GreatHelm;
import model.items.accessories.LargeShield;
import model.items.weapons.RaidersAxe;
import model.races.Race;
import util.MyLists;
import util.MyTriplet;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;
import view.sprites.Sprite8x8;

import java.util.List;

public class VikingClass extends PrestigeClass {
    private static final List<Class<? extends CharacterClass>> FROM_CLASSES = List.of(
            AmazonClass.class,
            BarbarianClass.class,
            CaptainClass.class,
            DruidClass.class,
            ForesterClass.class,
            MinerClass.class);

    private final String descriptionClasses;

    public VikingClass() {
        super("Viking", "V", 12, 5, true, 6,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 4),
                        new WeightedSkill(Skill.Axes, 6),
                        new WeightedSkill(Skill.Blades, 5),
                        new WeightedSkill(Skill.Bows, 3),
                        new WeightedSkill(Skill.Endurance, 5),
                        new WeightedSkill(Skill.Entertain, 4),
                        new WeightedSkillMinus(Skill.Labor, 4),
                        new WeightedSkill(Skill.Leadership, 4),
                        new WeightedSkill(Skill.Mercantile, 4),
                        new WeightedSkill(Skill.Search, 4),
                        new WeightedSkill(Skill.Survival, 5)
                });
        this.descriptionClasses = MyLists.commaAndJoin(FROM_CLASSES, x ->
                x.getSimpleName().replace("Class", ""));
    }

    @Override
    protected boolean canBecomeFrom(CharacterClass charClass) {
        return MyLists.any(FROM_CLASSES, cc -> charClass.getClass().isAssignableFrom(cc));
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, MyColors.DARK_RED);
        CaptainClass.putOnHalfHelm(characterAppearance);
        putOnHorns(characterAppearance, MyColors.GOLD);
    }

    public static void putOnHorns(CharacterAppearance characterAppearance, MyColors hornsColor) {
        for (int j = 0; j < 2; ++j) {
            for (int i = 0; i < 2; ++i) {
                Sprite8x8 mask = new Sprite8x8("vikinghornsl" + i, "clothes.png", 0x1A2 + 0x10*j + i);
                mask.setColor1(MyColors.DARK_GRAY);
                mask.setColor3(hornsColor);
                characterAppearance.addSpriteOnTop(1 + i, 0 + j, mask);

                mask = new Sprite8x8("vikinghornsr" + i, "clothes.png", 0x1A4 + 0x10*j + i);
                mask.setColor1(MyColors.DARK_GRAY);
                mask.setColor3(hornsColor);
                characterAppearance.addSpriteOnTop(4 + i, 0 + j, mask);
            }
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, appearance.getGender() ? AvatarSprite.LOOSE_SHIRT_FEMALE_BASE : AvatarSprite.LOOSE_SHIRT_BASE,
                MyColors.DARK_RED, race.getColor(), MyColors.DARK_RED,
                appearance.getBackHairOnly(), appearance.getHalfBackHair(),
                makeVikingHelmet(appearance));
    }

    private MyTriplet<Sprite, Sprite, Sprite> makeVikingHelmet(CharacterAppearance appearance) {
        return AvatarSprite.makeHat(appearance, "vikinghelmet", 0x0D,
                MyColors.BLACK, MyColors.DARK_RED, MyColors.GOLD, MyColors.PINK);
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new RaidersAxe());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Savage, mean and primitive, but cunning warriors. Vikings are masters of the sea and hand-to-hand combat, " +
                "but are also able laborers and traders. " +
                "This Prestige Class can only be taken on by characters of one of the following classes: " +
                descriptionClasses + ".";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new RaidersAxe(), new LargeShield(), new GreatHelm());
    }
}
