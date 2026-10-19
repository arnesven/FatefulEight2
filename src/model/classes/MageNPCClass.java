package model.classes;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.SpiralStaffDetail;
import model.classes.npcs.NPCClass;
import model.races.Race;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.FancyLongSleeve;
import view.MyColors;
import view.sprites.AvatarSprite;

public class MageNPCClass extends NPCClass {
    private static final MyColors ROBE_COLOR = MyColors.RED;
    private static final MyColors ROBE_DETAIL_COLOR = MyColors.LIGHT_RED;
    private final SpiralStaffDetail staff;

    protected MageNPCClass() {
        super("Mage", new WeightedSkill[]{
                new WeightedSkill(Skill.MagicRed, 3),
                new WeightedSkill(Skill.MagicBlue, 3),
                new WeightedSkill(Skill.MagicGreen, 3),
                new WeightedSkill(Skill.MagicWhite, 3),
                new WeightedSkill(Skill.MagicBlack, 3),
                new WeightedSkill(Skill.SpellCasting, 3),
        });
        this.staff = new SpiralStaffDetail(MyColors.BROWN, MyColors.ORANGE);
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnPointyHat(characterAppearance, MyColors.RED, MyColors.DARK_RED, MyColors.DARK_RED);
        Looks.putOnRobe(characterAppearance, ROBE_COLOR, ROBE_DETAIL_COLOR);
        if (characterAppearance instanceof AdvancedAppearance) {
            staff.applyYourself((AdvancedAppearance) characterAppearance,
                    characterAppearance.getRace());
        }
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new FancyLongSleeve(ROBE_COLOR, ROBE_DETAIL_COLOR);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x40, MyColors.RED, race.getColor(), appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }
}
