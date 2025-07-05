package model.classes.prestige;

import model.characters.appearance.CharacterAppearance;
import model.classes.*;
import model.classes.normal.*;
import model.items.Equipment;
import model.items.Item;
import model.items.accessories.Spyglass;
import model.items.clothing.FancyJerkin;
import model.items.weapons.Cutlass;
import model.items.weapons.Dirk;
import model.races.Race;
import util.MyLists;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ClothesSprite;
import view.sprites.PortraitSprite;

import java.util.List;


public class PirateCaptainClass extends PrestigeClass {
    private static final List<Class<? extends CharacterClass>> FROM_CLASSES = List.of(
            ArtisanClass.class,
            AssassinClass.class,
            BardClass.class,
            CaptainClass.class,
            MagicianClass.class,
            NobleClass.class,
            SpyClass.class,
            ThiefClass.class);

    private static final MyColors HAT_COLOR_BASE = MyColors.DARK_BROWN;
    private static final MyColors HAT_HIGH_LIGHT = MyColors.GOLD;
    private final String descriptionClasses;

    public PirateCaptainClass() {
        super("Pirate Cap'n", "PCN", 8, 6,
                false, 10, new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 3),
                        new WeightedSkill(Skill.Blades, 5),
                        new WeightedSkill(Skill.Entertain, 3),
                        new WeightedSkill(Skill.Endurance, 3),
                        new WeightedSkill(Skill.Leadership, 5),
                        new WeightedSkill(Skill.Mercantile, 3),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Persuade, 4),
                        new WeightedSkill(Skill.Search, 4),
                        new WeightedSkill(Skill.Security, 3),
                        new WeightedSkill(Skill.SeekInfo, 5),
                        new WeightedSkill(Skill.Sneak, 4)
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
        Looks.putOnFancyRobe(characterAppearance, MyColors.DARK_RED, MyColors.DARK_BLUE);
        putOnPirateCaptainHat(characterAppearance);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x380, MyColors.DARK_RED, race.getColor(), MyColors.DARK_BLUE,
                appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    public String getDescription() {
        return "A cunning, and versatile fighter, the Pirate Captain has many fine " +
                "qualities, and some not-so-fine ones too! " +
                "This Prestige Class can only be taken on by characters of one of the following classes: " +
                descriptionClasses + ".";

    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new Cutlass(), new FancyJerkin(), new Spyglass());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new Dirk());
    }


    private void putOnPirateCaptainHat(CharacterAppearance characterAppearance) {
        characterAppearance.removeOuterHair();
        for (int y = 0; y <= 2; ++y) {
            for (int x = 0; x <= 6; ++x) {
                if ((y == 0 && 2 <= x && x <= 4) || y > 0) {
                    PortraitSprite spr = new ClothesSprite(0x130 + 0x10 * y + x + 3, HAT_COLOR_BASE, MyColors.DARK_RED);
                    spr.setColor4(HAT_HIGH_LIGHT);
                    if (y == 2 && 2 <= x && x <= 4) {
                        characterAppearance.addSpriteOnTop(x, y, spr);
                    } else {
                        characterAppearance.setSprite(x, y, spr);
                    }
                }
            }
        }
        PortraitSprite ll = new ClothesSprite(0x138, HAT_COLOR_BASE, MyColors.DARK_RED);
        ll.setColor4(HAT_HIGH_LIGHT);
        characterAppearance.setSprite(1, 3, ll);
        PortraitSprite lr = new ClothesSprite(0x139, HAT_COLOR_BASE, MyColors.DARK_RED);
        ll.setColor4(HAT_HIGH_LIGHT);
        characterAppearance.setSprite(5, 3, lr);
    }
}
