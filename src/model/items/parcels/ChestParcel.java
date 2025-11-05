package model.items.parcels;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.GoldDummyItem;
import model.items.Item;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ChestParcel extends Parcel {
    private static final Sprite SPRITE = new ItemSprite(4, 13, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.DARK_GRAY);
    private final int level;
    private final int amount;
    private final boolean containsPaper;


    public ChestParcel() {
        super("Chest", 5.0);
        this.containsPaper = MyRandom.randInt(4) == 0;
        this.level = MyRandom.randInt(1, 4) + MyRandom.randInt(1, 4);
        this.amount = MyRandom.randInt(10, 45) * level + MyRandom.randInt(10);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 3500;
    }

    @Override
    public String getShoppingDetails() {
        return "A locked chest";
    }

    @Override
    public Item copy() {
        return new ChestParcel();
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    protected Item getInnerItem() {
        if (containsPaper) {
            return null;
        }
        return new GoldDummyItem(amount);
    }

    @Override
    public boolean removeAfterUse() {
        return false;
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        if (gc.getRankForSkill(Skill.Security) < level) {
            return "Security Skill too Low!";
        }
        model.getParty().removeFromInventory(this);
        return gc.getFirstName() + " unlocked the chest. " + super.useYourself(model, gc);
    }

    @Override
    protected int getNotoriety() {
        if (containsPaper) {
            return 1;
        }
        return level*5;
    }
}
