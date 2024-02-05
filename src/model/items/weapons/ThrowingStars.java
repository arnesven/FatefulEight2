package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ThrowingStars extends Weapon {

    private static final Sprite SPRITE = new ItemSprite(8, 12);

    public ThrowingStars() {
        super("Throwing Stars", 18, Skill.Acrobatics, new int[]{9, 11, 12, 12});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getCriticalTarget() {
        return 9;
    }

    @Override
    public int getSpeedModifier() {
        return 2;
    }

    @Override
    public int getWeight() {
        return 100;
    }

    @Override
    public Item copy() {
        return new ThrowingStars();
    }

    @Override
    public String getSound() {
        return "blade";
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return null;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
