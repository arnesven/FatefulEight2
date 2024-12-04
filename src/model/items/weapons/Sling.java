package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Sling extends Weapon {

    private static final Sprite SPRITE = new ItemSprite(5, 16, MyColors.BROWN, MyColors.GRAY);

    public Sling() {
        super("Sling", 5, Skill.Acrobatics, new int[]{8, 9});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public boolean isRangedAttack() {
        return true;
    }

    @Override
    public int getWeight() {
        return 50;
    }

    @Override
    public Item copy() {
        return new Sling();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }

    @Override
    public String getSound() {
        return "wood-small";
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return null;
    }
}
