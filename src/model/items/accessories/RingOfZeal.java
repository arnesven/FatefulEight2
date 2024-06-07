package model.items.accessories;

import model.combat.conditions.Condition;
import model.combat.conditions.FatigueCondition;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RingOfZeal extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(11, 12, MyColors.ORANGE, MyColors.LIGHT_YELLOW, MyColors.GOLD);;

    public RingOfZeal() {
        super("Ring of Zeal", 126);
    }

    @Override
    public boolean grantsConditionImmunity(Condition cond) {
        return cond instanceof FatigueCondition;
    }

    @Override
    public String getExtraText() {
        return "Wielder can never get fatigued in combat.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RingOfZeal();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
