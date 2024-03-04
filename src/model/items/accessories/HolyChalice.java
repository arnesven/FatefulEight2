package model.items.accessories;

import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class HolyChalice extends ShieldItem {
    private static final Sprite SPRITE = new ItemSprite(15, 11,
            MyColors.YELLOW, MyColors.GOLD, MyColors.RED);

    private static final Sprite[] SHIELD_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x64, MyColors.BROWN, MyColors.YELLOW, MyColors.PINK, MyColors.BEIGE));


    public HolyChalice() {
        super("Holy Chalice", 60, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.MagicWhite, 2),
                       new MyPair<>(Skill.MagicGreen, 2),
                       new MyPair<>(Skill.Persuade, 2));
    }

    @Override
    public int getHealthBonus() {
        return 2;
    }

    @Override
    public Item copy() {
        return new HolyChalice();
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
    @Override
    public Sprite getOnAvatarSprite(GameCharacter gameCharacter) {
        return SHIELD_SPRITES[gameCharacter.getCharClass().getWeaponShift(gameCharacter) + 1];
    }
}
